package com.hostel.complaint.dao.impl;

import com.hostel.complaint.dao.AdminDAO;
import com.hostel.complaint.database.DatabaseConnection;
import com.hostel.complaint.model.Admin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Admin DAO implementation
 * Provides database operations for admin management
 */
public class AdminDAOImpl implements AdminDAO {
    private static final Logger logger = LoggerFactory.getLogger(AdminDAOImpl.class);

    @Override
    public Admin save(Admin admin) throws SQLException {
        String sql = "INSERT INTO admin (username, password, full_name, email, phone) VALUES (?, ?, ?, ?, ?)";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            statement.setString(1, admin.getUsername());
            statement.setString(2, admin.getPassword());
            statement.setString(3, admin.getFullName());
            statement.setString(4, admin.getEmail());
            statement.setString(5, admin.getPhone());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating admin failed, no rows affected.");
            }
            
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                admin.setAdminId(resultSet.getInt(1));
            }
            
            connection.commit();
            logger.info("Admin saved successfully: {}", admin.getUsername());
            return admin;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error saving admin", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean update(Admin admin) throws SQLException {
        String sql = "UPDATE admin SET username = ?, password = ?, full_name = ?, email = ?, phone = ?, " +
                     "updated_at = ? WHERE admin_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            
            statement.setString(1, admin.getUsername());
            statement.setString(2, admin.getPassword());
            statement.setString(3, admin.getFullName());
            statement.setString(4, admin.getEmail());
            statement.setString(5, admin.getPhone());
            statement.setTimestamp(6, Timestamp.valueOf(java.time.LocalDateTime.now()));
            statement.setInt(7, admin.getAdminId());
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            
            logger.info("Admin updated successfully: {}", admin.getAdminId());
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error updating admin", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM admin WHERE admin_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            
            logger.info("Admin deleted successfully: {}", id);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error deleting admin", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    @Override
    public Optional<Admin> findById(int id) throws SQLException {
        String sql = "SELECT * FROM admin WHERE admin_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToAdmin(resultSet));
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error finding admin by ID", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<Admin> findAll() throws SQLException {
        String sql = "SELECT * FROM admin ORDER BY full_name";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            
            resultSet = statement.executeQuery();
            
            List<Admin> admins = new ArrayList<>();
            while (resultSet.next()) {
                admins.add(mapResultSetToAdmin(resultSet));
            }
            
            return admins;
            
        } catch (SQLException e) {
            logger.error("Error finding all admins", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean exists(int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM admin WHERE admin_id = ?";
        
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
            logger.error("Error checking admin existence", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM admin";
        
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
            logger.error("Error counting admins", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public Optional<Admin> findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM admin WHERE username = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToAdmin(resultSet));
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error finding admin by username", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public Optional<Admin> findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM admin WHERE email = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToAdmin(resultSet));
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error finding admin by email", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM admin WHERE username = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error checking username existence", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean emailExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM admin WHERE email = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error checking email existence", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean updateLastLogin(int adminId) throws SQLException {
        String sql = "UPDATE admin SET last_login = ? WHERE admin_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setTimestamp(1, Timestamp.valueOf(java.time.LocalDateTime.now()));
            statement.setInt(2, adminId);
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error updating last login", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    @Override
    public boolean updatePassword(int adminId, String newPassword) throws SQLException {
        String sql = "UPDATE admin SET password = ?, updated_at = ? WHERE admin_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, newPassword);
            statement.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now()));
            statement.setInt(3, adminId);
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            
            logger.info("Password updated for admin: {}", adminId);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error updating password", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    private Admin mapResultSetToAdmin(ResultSet resultSet) throws SQLException {
        Admin admin = new Admin();
        
        admin.setAdminId(resultSet.getInt("admin_id"));
        admin.setUsername(resultSet.getString("username"));
        admin.setPassword(resultSet.getString("password"));
        admin.setFullName(resultSet.getString("full_name"));
        admin.setEmail(resultSet.getString("email"));
        admin.setPhone(resultSet.getString("phone"));
        
        Timestamp createdAt = resultSet.getTimestamp("created_at");
        if (createdAt != null) admin.setCreatedAt(createdAt.toLocalDateTime());
        
        Timestamp updatedAt = resultSet.getTimestamp("updated_at");
        if (updatedAt != null) admin.setUpdatedAt(updatedAt.toLocalDateTime());
        
        Timestamp lastLogin = resultSet.getTimestamp("last_login");
        if (lastLogin != null) admin.setLastLogin(lastLogin.toLocalDateTime());
        
        admin.setActive(resultSet.getBoolean("is_active"));
        
        return admin;
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
