package com.hostel.complaint.dao.impl;

import com.hostel.complaint.dao.NotificationDAO;
import com.hostel.complaint.database.DatabaseConnection;
import com.hostel.complaint.model.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Notification DAO implementation
 */
public class NotificationDAOImpl implements NotificationDAO {
    private static final Logger logger = LoggerFactory.getLogger(NotificationDAOImpl.class);

    @Override
    public Notification save(Notification notification) throws SQLException {
        String sql = "INSERT INTO notifications (user_id, user_type, complaint_id, title, message) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            statement.setInt(1, notification.getUserId());
            statement.setString(2, notification.getUserType().name());
            
            if (notification.getComplaintId() != null) {
                statement.setInt(3, notification.getComplaintId());
            } else {
                statement.setNull(3, Types.INTEGER);
            }
            
            statement.setString(4, notification.getTitle());
            statement.setString(5, notification.getMessage());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating notification failed, no rows affected.");
            }
            
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                notification.setNotificationId(resultSet.getInt(1));
            }
            
            connection.commit();
            return notification;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error saving notification", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean update(Notification notification) throws SQLException {
        String sql = "UPDATE notifications SET is_read = ? WHERE notification_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setBoolean(1, notification.isRead());
            statement.setInt(2, notification.getNotificationId());
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error updating notification", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM notifications WHERE notification_id = ?";
        
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
            logger.error("Error deleting notification", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    @Override
    public Optional<Notification> findById(int id) throws SQLException {
        String sql = "SELECT * FROM notifications WHERE notification_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToNotification(resultSet));
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error finding notification by ID", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<Notification> findAll() throws SQLException {
        String sql = "SELECT * FROM notifications ORDER BY created_at DESC";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            
            resultSet = statement.executeQuery();
            
            List<Notification> notifications = new ArrayList<>();
            while (resultSet.next()) {
                notifications.add(mapResultSetToNotification(resultSet));
            }
            
            return notifications;
            
        } catch (SQLException e) {
            logger.error("Error finding all notifications", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean exists(int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM notifications WHERE notification_id = ?";
        
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
            logger.error("Error checking notification existence", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM notifications";
        
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
            logger.error("Error counting notifications", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<Notification> findByUserId(int userId, Notification.UserType userType) throws SQLException {
        String sql = "SELECT * FROM notifications WHERE user_id = ? AND user_type = ? ORDER BY created_at DESC";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setString(2, userType.name());
            
            resultSet = statement.executeQuery();
            
            List<Notification> notifications = new ArrayList<>();
            while (resultSet.next()) {
                notifications.add(mapResultSetToNotification(resultSet));
            }
            
            return notifications;
            
        } catch (SQLException e) {
            logger.error("Error finding notifications by user ID", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<Notification> findUnreadByUserId(int userId, Notification.UserType userType) throws SQLException {
        String sql = "SELECT * FROM notifications WHERE user_id = ? AND user_type = ? AND is_read = false " +
                     "ORDER BY created_at DESC";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setString(2, userType.name());
            
            resultSet = statement.executeQuery();
            
            List<Notification> notifications = new ArrayList<>();
            while (resultSet.next()) {
                notifications.add(mapResultSetToNotification(resultSet));
            }
            
            return notifications;
            
        } catch (SQLException e) {
            logger.error("Error finding unread notifications by user ID", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<Notification> findByComplaintId(int complaintId) throws SQLException {
        String sql = "SELECT * FROM notifications WHERE complaint_id = ? ORDER BY created_at DESC";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, complaintId);
            
            resultSet = statement.executeQuery();
            
            List<Notification> notifications = new ArrayList<>();
            while (resultSet.next()) {
                notifications.add(mapResultSetToNotification(resultSet));
            }
            
            return notifications;
            
        } catch (SQLException e) {
            logger.error("Error finding notifications by complaint ID", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean markAsRead(int notificationId) throws SQLException {
        String sql = "UPDATE notifications SET is_read = true WHERE notification_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, notificationId);
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error marking notification as read", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    @Override
    public boolean markAllAsRead(int userId, Notification.UserType userType) throws SQLException {
        String sql = "UPDATE notifications SET is_read = true WHERE user_id = ? AND user_type = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setString(2, userType.name());
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error marking all notifications as read", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    @Override
    public int countUnread(int userId, Notification.UserType userType) throws SQLException {
        String sql = "SELECT COUNT(*) FROM notifications WHERE user_id = ? AND user_type = ? AND is_read = false";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setString(2, userType.name());
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            
            return 0;
            
        } catch (SQLException e) {
            logger.error("Error counting unread notifications", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public int deleteOldNotifications(int days) throws SQLException {
        String sql = "DELETE FROM notifications WHERE created_at < DATE_SUB(NOW(), INTERVAL ? DAY)";
        
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
            logger.error("Error deleting old notifications", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    private Notification mapResultSetToNotification(ResultSet resultSet) throws SQLException {
        Notification notification = new Notification();
        
        notification.setNotificationId(resultSet.getInt("notification_id"));
        notification.setUserId(resultSet.getInt("user_id"));
        notification.setUserType(Notification.UserType.valueOf(resultSet.getString("user_type")));
        
        int complaintId = resultSet.getInt("complaint_id");
        if (!resultSet.wasNull()) {
            notification.setComplaintId(complaintId);
        }
        
        notification.setTitle(resultSet.getString("title"));
        notification.setMessage(resultSet.getString("message"));
        notification.setRead(resultSet.getBoolean("is_read"));
        
        Timestamp createdAt = resultSet.getTimestamp("created_at");
        if (createdAt != null) {
            notification.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return notification;
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
