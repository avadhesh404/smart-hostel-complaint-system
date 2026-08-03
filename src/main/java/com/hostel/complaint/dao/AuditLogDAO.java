package com.hostel.complaint.dao;

import com.hostel.complaint.model.AuditLog;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Audit Log DAO interface
 * Defines operations for audit log management
 */
public interface AuditLogDAO extends BaseDAO<AuditLog> {
    
    /**
     * Find audit logs by user ID and type
     * @param userId The user ID
     * @param userType The user type
     * @return List of audit logs for the user
     * @throws SQLException if database error occurs
     */
    List<AuditLog> findByUserId(int userId, AuditLog.UserType userType) throws SQLException;
    
    /**
     * Find audit logs by action
     * @param action The action
     * @return List of audit logs with the action
     * @throws SQLException if database error occurs
     */
    List<AuditLog> findByAction(String action) throws SQLException;
    
    /**
     * Find audit logs by table name
     * @param tableName The table name
     * @return List of audit logs for the table
     * @throws SQLException if database error occurs
     */
    List<AuditLog> findByTableName(String tableName) throws SQLException;
    
    /**
     * Find audit logs by record
     * @param tableName The table name
     * @param recordId The record ID
     * @return List of audit logs for the record
     * @throws SQLException if database error occurs
     */
    List<AuditLog> findByRecord(String tableName, int recordId) throws SQLException;
    
    /**
     * Find audit logs within a date range
     * @param startDate Start date
     * @param endDate End date
     * @return List of audit logs in the date range
     * @throws SQLException if database error occurs
     */
    List<AuditLog> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws SQLException;
    
    /**
     * Find recent audit logs
     * @param limit Maximum number of logs to return
     * @return List of recent audit logs
     * @throws SQLException if database error occurs
     */
    List<AuditLog> findRecent(int limit) throws SQLException;
    
    /**
     * Delete old audit logs
     * @param days Number of days to keep
     * @return Number of deleted logs
     * @throws SQLException if database error occurs
     */
    int deleteOldLogs(int days) throws SQLException;
}
