package com.hostel.complaint.dao;

import com.hostel.complaint.model.Admin;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Admin DAO interface
 * Defines operations for admin management
 */
public interface AdminDAO extends BaseDAO<Admin> {
    
    /**
     * Find admin by username
     * @param username The username
     * @return Optional containing the admin if found
     * @throws SQLException if database error occurs
     */
    Optional<Admin> findByUsername(String username) throws SQLException;
    
    /**
     * Find admin by email
     * @param email The email address
     * @return Optional containing the admin if found
     * @throws SQLException if database error occurs
     */
    Optional<Admin> findByEmail(String email) throws SQLException;
    
    /**
     * Check if username exists
     * @param username The username
     * @return true if username exists
     * @throws SQLException if database error occurs
     */
    boolean usernameExists(String username) throws SQLException;
    
    /**
     * Check if email exists
     * @param email The email address
     * @return true if email exists
     * @throws SQLException if database error occurs
     */
    boolean emailExists(String email) throws SQLException;
    
    /**
     * Update last login timestamp
     * @param adminId The admin ID
     * @return true if update was successful
     * @throws SQLException if database error occurs
     */
    boolean updateLastLogin(int adminId) throws SQLException;
    
    /**
     * Update admin password
     * @param adminId The admin ID
     * @param newPassword The new hashed password
     * @return true if update was successful
     * @throws SQLException if database error occurs
     */
    boolean updatePassword(int adminId, String newPassword) throws SQLException;
}
