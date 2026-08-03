package com.hostel.complaint.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Database connection pool manager
 * Implements connection pooling for efficient database access
 */
public class DatabaseConnection {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
    
    private static BlockingQueue<Connection> connectionPool;
    private static final int INITIAL_POOL_SIZE = DatabaseConfig.getInitialPoolSize();
    private static final int MAX_POOL_SIZE = DatabaseConfig.getMaxPoolSize();
    private static volatile boolean initialized = false;

    private DatabaseConnection() {
        // Private constructor to prevent instantiation
    }

    /**
     * Initialize the connection pool
     */
    public static synchronized void initialize() {
        if (initialized) {
            return;
        }

        try {
            Class.forName(DatabaseConfig.getDriver());
            connectionPool = new ArrayBlockingQueue<>(MAX_POOL_SIZE);

            // Create initial connections
            for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
                connectionPool.offer(createNewConnection());
            }

            initialized = true;
            logger.info("Database connection pool initialized with {} connections", INITIAL_POOL_SIZE);
        } catch (ClassNotFoundException e) {
            logger.error("Database driver not found", e);
            throw new RuntimeException("Failed to initialize database connection pool", e);
        }
    }

    /**
     * Create a new database connection
     */
    private static Connection createNewConnection() {
        try {
            Connection connection = DriverManager.getConnection(
                DatabaseConfig.getUrl(),
                DatabaseConfig.getUsername(),
                DatabaseConfig.getPassword()
            );
            connection.setAutoCommit(false);
            logger.debug("Created new database connection");
            return connection;
        } catch (SQLException e) {
            logger.error("Failed to create database connection", e);
            throw new RuntimeException("Failed to create database connection", e);
        }
    }

    /**
     * Get a connection from the pool
     * @return Connection object
     */
    public static Connection getConnection() {
        if (!initialized) {
            initialize();
        }

        try {
            Connection connection = connectionPool.poll(5, TimeUnit.SECONDS);
            
            if (connection == null) {
                // Pool is empty, try to create a new connection if under max size
                if (connectionPool.size() < MAX_POOL_SIZE) {
                    connection = createNewConnection();
                } else {
                    // Wait for a connection to become available
                    connection = connectionPool.take();
                }
            }

            // Validate connection
            if (!connection.isValid(2)) {
                logger.warn("Invalid connection found, creating new one");
                connection.close();
                connection = createNewConnection();
            }

            logger.debug("Connection acquired from pool. Active connections: {}", MAX_POOL_SIZE - connectionPool.size());
            return connection;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Interrupted while waiting for connection", e);
            throw new RuntimeException("Failed to get database connection", e);
        } catch (SQLException e) {
            logger.error("Failed to validate connection", e);
            throw new RuntimeException("Failed to get database connection", e);
        }
    }

    /**
     * Return a connection to the pool
     * @param connection Connection to return
     */
    public static void releaseConnection(Connection connection) {
        if (connection != null) {
            try {
                // Reset connection to default state
                if (!connection.getAutoCommit()) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                }
                
                // Clear any warnings
                connection.clearWarnings();
                
                // Return to pool if not full
                if (!connectionPool.offer(connection)) {
                    connection.close();
                    logger.debug("Connection closed (pool full)");
                } else {
                    logger.debug("Connection returned to pool. Active connections: {}", MAX_POOL_SIZE - connectionPool.size());
                }
            } catch (SQLException e) {
                logger.error("Error returning connection to pool", e);
                try {
                    connection.close();
                } catch (SQLException ex) {
                    logger.error("Error closing connection", ex);
                }
            }
        }
    }

    /**
     * Close all connections in the pool
     */
    public static synchronized void closeAll() {
        if (connectionPool != null) {
            for (Connection connection : connectionPool) {
                try {
                    if (connection != null && !connection.isClosed()) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    logger.error("Error closing connection", e);
                }
            }
            connectionPool.clear();
            initialized = false;
            logger.info("All database connections closed");
        }
    }

    /**
     * Get the current pool size
     * @return Number of available connections in the pool
     */
    public static int getAvailableConnections() {
        return connectionPool != null ? connectionPool.size() : 0;
    }

    /**
     * Get the number of active connections
     * @return Number of connections currently in use
     */
    public static int getActiveConnections() {
        return connectionPool != null ? MAX_POOL_SIZE - connectionPool.size() : 0;
    }
}
