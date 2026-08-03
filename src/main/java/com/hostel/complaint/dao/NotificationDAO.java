package com.hostel.complaint.dao;

import com.hostel.complaint.model.Notification;

import java.sql.SQLException;
import java.util.List;

/**
 * Notification DAO interface
 * Defines operations for notification management
 */
public interface NotificationDAO extends BaseDAO<Notification> {
    
    /**
     * Find notifications by user ID and type
     * @param userId The user ID
     * @param userType The user type
     * @return List of notifications for the user
     * @throws SQLException if database error occurs
     */
    List<Notification> findByUserId(int userId, Notification.UserType userType) throws SQLException;
    
    /**
     * Find unread notifications by user ID and type
     * @param userId The user ID
     * @param userType The user type
     * @return List of unread notifications for the user
     * @throws SQLException if database error occurs
     */
    List<Notification> findUnreadByUserId(int userId, Notification.UserType userType) throws SQLException;
    
    /**
     * Find notifications by complaint ID
     * @param complaintId The complaint ID
     * @return List of notifications for the complaint
     * @throws SQLException if database error occurs
     */
    List<Notification> findByComplaintId(int complaintId) throws SQLException;
    
    /**
     * Mark notification as read
     * @param notificationId The notification ID
     * @return true if update was successful
     * @throws SQLException if database error occurs
     */
    boolean markAsRead(int notificationId) throws SQLException;
    
    /**
     * Mark all notifications as read for a user
     * @param userId The user ID
     * @param userType The user type
     * @return true if update was successful
     * @throws SQLException if database error occurs
     */
    boolean markAllAsRead(int userId, Notification.UserType userType) throws SQLException;
    
    /**
     * Count unread notifications for a user
     * @param userId The user ID
     * @param userType The user type
     * @return Count of unread notifications
     * @throws SQLException if database error occurs
     */
    int countUnread(int userId, Notification.UserType userType) throws SQLException;
    
    /**
     * Delete old notifications
     * @param days Number of days to keep
     * @return Number of deleted notifications
     * @throws SQLException if database error occurs
     */
    int deleteOldNotifications(int days) throws SQLException;
}
