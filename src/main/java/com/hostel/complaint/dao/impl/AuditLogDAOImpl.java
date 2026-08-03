package com.hostel.complaint.dao.impl;

import com.hostel.complaint.dao.AuditLogDAO;
import com.hostel.complaint.database.DatabaseConnection;
import com.hostel.complaint.model.AuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Audit Log DAO implementation
 */
public class AuditLogDAOImpl implements AuditLogDAO {
    private static final Logger logger = LoggerFactory.getLogger(AuditLogDAOImpl.class);

    @Override
    public AuditLog save(AuditLog auditLog) throws SQLException {
        String sql = "INSERT INTO audit_logs (user_id, user_type, action, table_name, record_id, old_values, " +
                     "new_values, ip_address, user_agent) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            if (auditLog.getUserId() != null) {
                statement.setInt(1, auditLog.getUserId());
            } else {
                statement.setNull(1, Types.INTEGER);
            }
            
            statement.setString(2, auditLog.getUserType().name());
            statement.setString(3, auditLog.getAction());
            statement.setString(4, auditLog.getTableName());
            
            if (auditLog.getRecordId() != null) {
                statement.setInt(5, auditLog.getRecordId());
            } else {
                statement.setNull(5, Types.INTEGER);
            }
            
            statement.setString(6, auditLog.getOldValues());
            statement.setString(7, auditLog.getNewValues());
            statement.setString(8, auditLog.getIpAddress());
            statement.setString(9, auditLog.getUserAgent());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating audit log failed, no rows affected.");
            }
            
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                auditLog.setLogId(resultSet.getInt(1));
            }
            
            connection.commit();
            return auditLog;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error saving audit log", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean update(AuditLog auditLog) throws SQLException {
        // Audit logs are immutable - no update needed
        return false;
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM audit_logs WHERE log_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error deleting audit log", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    @Override
    public Optional<AuditLog> findById(int id) throws SQLException {
        String sql = "SELECT * FROM audit_logs WHERE log_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToAuditLog(resultSet));
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error finding audit log by ID", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<AuditLog> findAll() throws SQLException {
        String sql = "SELECT * FROM audit_logs ORDER BY created_at DESC";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            
            resultSet = statement.executeQuery();
            
            List<AuditLog> auditLogs = new ArrayList<>();
            while (resultSet.next()) {
                auditLogs.add(mapResultSetToAuditLog(resultSet));
            }
            
            return auditLogs;
            
        } catch (SQLException e) {
            logger.error("Error finding all audit logs", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean exists(int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM audit_logs WHERE log_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error checking audit log existence", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM audit_logs";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            
            return 0;
            
        } catch (SQLException e) {
            logger.error("Error counting audit logs", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<AuditLog> findByUserId(int userId, AuditLog.UserType userType) throws SQLException {
        String sql = "SELECT * FROM audit_logs WHERE user_id = ? AND user_type = ? ORDER BY created_at DESC";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setString(2, userType.name());
            
            resultSet = statement.executeQuery();
            
            List<AuditLog> auditLogs = new ArrayList<>();
            while (resultSet.next()) {
                auditLogs.add(mapResultSetToAuditLog(resultSet));
            }
            
            return auditLogs;
            
        } catch (SQLException e) {
            logger.error("Error finding audit logs by user ID", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<AuditLog> findByAction(String action) throws SQLException {
        String sql = "SELECT * FROM audit_logs WHERE action = ? ORDER BY created_at DESC";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, action);
            
            resultSet = statement.executeQuery();
            
            List<AuditLog> auditLogs = new ArrayList<>();
            while (resultSet.next()) {
                auditLogs.add(mapResultSetToAuditLog(resultSet));
            }
            
            return auditLogs;
            
        } catch (SQLException e) {
            logger.error("Error finding audit logs by action", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<AuditLog> findByTableName(String tableName) throws SQLException {
        String sql = "SELECT * FROM audit_logs WHERE table_name = ? ORDER BY created_at DESC";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, tableName);
            
            resultSet = statement.executeQuery();
            
            List<AuditLog> auditLogs = new ArrayList<>();
            while (resultSet.next()) {
                auditLogs.add(mapResultSetToAuditLog(resultSet));
            }
            
            return auditLogs;
            
        } catch (SQLException e) {
            logger.error("Error finding audit logs by table name", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<AuditLog> findByRecord(String tableName, int recordId) throws SQLException {
        String sql = "SELECT * FROM audit_logs WHERE table_name = ? AND record_id = ? ORDER BY created_at DESC";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, tableName);
            statement.setInt(2, recordId);
            
            resultSet = statement.executeQuery();
            
            List<AuditLog> auditLogs = new ArrayList<>();
            while (resultSet.next()) {
                auditLogs.add(mapResultSetToAuditLog(resultSet));
            }
            
            return auditLogs;
            
        } catch (SQLException e) {
            logger.error("Error finding audit logs by record", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<AuditLog> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        String sql = "SELECT * FROM audit_logs WHERE created_at BETWEEN ? AND ? ORDER BY created_at DESC";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setTimestamp(1, Timestamp.valueOf(startDate));
            statement.setTimestamp(2, Timestamp.valueOf(endDate));
            
            resultSet = statement.executeQuery();
            
            List<AuditLog> auditLogs = new ArrayList<>();
            while (resultSet.next()) {
                auditLogs.add(mapResultSetToAuditLog(resultSet));
            }
            
            return auditLogs;
            
        } catch (SQLException e) {
            logger.error("Error finding audit logs by date range", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<AuditLog> findRecent(int limit) throws SQLException {
        String sql = "SELECT * FROM audit_logs ORDER BY created_at DESC LIMIT ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, limit);
            
            resultSet = statement.executeQuery();
            
            List<AuditLog> auditLogs = new ArrayList<>();
            while (resultSet.next()) {
                auditLogs.add(mapResultSetToAuditLog(resultSet));
            }
            
            return auditLogs;
            
        } catch (SQLException e) {
            logger.error("Error finding recent audit logs", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public int deleteOldLogs(int days) throws SQLException {
        String sql = "DELETE FROM audit_logs WHERE created_at < DATE_SUB(NOW(), INTERVAL ? DAY)";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, days);
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            return affectedRows;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error deleting old audit logs", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    private AuditLog mapResultSetToAuditLog(ResultSet resultSet) throws SQLException {
        AuditLog auditLog = new AuditLog();
        
        auditLog.setLogId(resultSet.getInt("log_id"));
        
        int userId = resultSet.getInt("user_id");
        if (!resultSet.wasNull()) {
            auditLog.setUserId(userId);
        }
        
        auditLog.setUserType(AuditLog.UserType.valueOf(resultSet.getString("user_type")));
        auditLog.setAction(resultSet.getString("action"));
        auditLog.setTableName(resultSet.getString("tableName"));
        
        int recordId = resultSet.getInt("record_id");
        if (!resultSet.wasNull()) {
            auditLog.setRecordId(recordId);
        }
        
        auditLog.setOldValues(resultSet.getString("old_values"));
        auditLog.setNewValues(resultSet.getString("new_values"));
        auditLog.setIpAddress(resultSet.getString("ip_address"));
        auditLog.setUserAgent(resultSet.getString("user_agent"));
        
        Timestamp createdAt = resultSet.getTimestamp("created_at");
        if (createdAt != null) {
            auditLog.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return auditLog;
    }

    private void closeResources(ResultSet resultSet, PreparedStatement statement, Connection connection) {
        try {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) DatabaseConnection.releaseConnection(connection);
        } catch (SQLException e) {
            logger.error("Error closing resources", e);
        }
    }
}
