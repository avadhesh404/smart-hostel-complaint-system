package com.hostel.complaint.dao;

import com.hostel.complaint.model.HostelStaff;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Hostel Staff DAO interface
 * Defines operations for hostel staff management
 */
public interface HostelStaffDAO extends BaseDAO<HostelStaff> {
    
    /**
     * Find staff by employee number
     * @param employeeNumber The employee number
     * @return Optional containing the staff if found
     * @throws SQLException if database error occurs
     */
    Optional<HostelStaff> findByEmployeeNumber(String employeeNumber) throws SQLException;
    
    /**
     * Find staff by username
     * @param username The username
     * @return Optional containing the staff if found
     * @throws SQLException if database error occurs
     */
    Optional<HostelStaff> findByUsername(String username) throws SQLException;
    
    /**
     * Find staff by email
     * @param email The email address
     * @return Optional containing the staff if found
     * @throws SQLException if database error occurs
     */
    Optional<HostelStaff> findByEmail(String email) throws SQLException;
    
    /**
     * Find staff by role
     * @param role The staff role
     * @return List of staff with the given role
     * @throws SQLException if database error occurs
     */
    List<HostelStaff> findByRole(HostelStaff.StaffRole role) throws SQLException;
    
    /**
     * Find staff by block
     * @param blockId The block ID
     * @return List of staff assigned to the block
     * @throws SQLException if database error occurs
     */
    List<HostelStaff> findByBlockId(int blockId) throws SQLException;
    
    /**
     * Find available staff
     * @return List of available staff
     * @throws SQLException if database error occurs
     */
    List<HostelStaff> findAvailable() throws SQLException;
    
    /**
     * Find available staff by role
     * @param role The staff role
     * @return List of available staff with the given role
     * @throws SQLException if database error occurs
     */
    List<HostelStaff> findAvailableByRole(HostelStaff.StaffRole role) throws SQLException;
    
    /**
     * Search staff by name
     * @param name The name to search
     * @return List of matching staff
     * @throws SQLException if database error occurs
     */
    List<HostelStaff> searchByName(String name) throws SQLException;
    
    /**
     * Check if employee number exists
     * @param employeeNumber The employee number
     * @return true if employee number exists
     * @throws SQLException if database error occurs
     */
    boolean employeeNumberExists(String employeeNumber) throws SQLException;
    
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
     * @param staffId The staff ID
     * @return true if update was successful
     * @throws SQLException if database error occurs
     */
    boolean updateLastLogin(int staffId) throws SQLException;
    
    /**
     * Update staff password
     * @param staffId The staff ID
     * @param newPassword The new hashed password
     * @return true if update was successful
     * @throws SQLException if database error occurs
     */
    boolean updatePassword(int staffId, String newPassword) throws SQLException;
    
    /**
     * Update staff availability
     * @param staffId The staff ID
     * @param isAvailable The availability status
     * @return true if update was successful
     * @throws SQLException if database error occurs
     */
    boolean updateAvailability(int staffId, boolean isAvailable) throws SQLException;
    
    /**
     * Assign staff to block
     * @param staffId The staff ID
     * @param blockId The block ID
     * @return true if assignment was successful
     * @throws SQLException if database error occurs
     */
    boolean assignToBlock(int staffId, int blockId) throws SQLException;
}
