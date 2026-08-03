package com.hostel.complaint.service;

import com.hostel.complaint.dao.HostelStaffDAO;
import com.hostel.complaint.dao.HostelBlockDAO;
import com.hostel.complaint.model.HostelStaff;
import com.hostel.complaint.model.HostelBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Hostel Staff Service
 * Handles staff-related business logic
 */
public class StaffService {
    private static final Logger logger = LoggerFactory.getLogger(StaffService.class);
    
    private final HostelStaffDAO staffDAO;
    private final HostelBlockDAO blockDAO;
    
    public StaffService(HostelStaffDAO staffDAO, HostelBlockDAO blockDAO) {
        this.staffDAO = staffDAO;
        this.blockDAO = blockDAO;
    }
    
    /**
     * Register a new staff member
     * @param staff Staff to register
     * @return Registered staff with generated ID
     * @throws SQLException if database error occurs
     * @throws ServiceException if business logic validation fails
     */
    public HostelStaff registerStaff(HostelStaff staff) throws SQLException, ServiceException {
        // Validate staff data
        validateStaff(staff);
        
        // Check if employee number already exists
        if (staffDAO.employeeNumberExists(staff.getEmployeeNumber())) {
            throw new ServiceException("Employee number already exists");
        }
        
        // Check if username already exists
        if (staffDAO.usernameExists(staff.getUsername())) {
            throw new ServiceException("Username already exists");
        }
        
        // Check if email already exists
        if (staffDAO.emailExists(staff.getEmail())) {
            throw new ServiceException("Email already exists");
        }
        
        // Validate block assignment if provided
        if (staff.getBlockId() != null) {
            validateBlockAssignment(staff.getBlockId());
        }
        
        // Save staff
        HostelStaff savedStaff = staffDAO.save(staff);
        
        logger.info("Staff registered successfully: {}", staff.getEmployeeNumber());
        return savedStaff;
    }
    
    /**
     * Update staff profile
     * @param staff Staff to update
     * @return true if update successful
     * @throws SQLException if database error occurs
     * @throws ServiceException if business logic validation fails
     */
    public boolean updateStaff(HostelStaff staff) throws SQLException, ServiceException {
        // Validate staff data
        validateStaff(staff);
        
        // Check if staff exists
        if (!staffDAO.exists(staff.getStaffId())) {
            throw new ServiceException("Staff not found");
        }
        
        // Check if employee number is taken by another staff
        Optional<HostelStaff> existingByNumber = staffDAO.findByEmployeeNumber(staff.getEmployeeNumber());
        if (existingByNumber.isPresent() && existingByNumber.get().getStaffId() != staff.getStaffId()) {
            throw new ServiceException("Employee number already exists");
        }
        
        // Check if username is taken by another staff
        Optional<HostelStaff> existingByUsername = staffDAO.findByUsername(staff.getUsername());
        if (existingByUsername.isPresent() && existingByUsername.get().getStaffId() != staff.getStaffId()) {
            throw new ServiceException("Username already exists");
        }
        
        // Check if email is taken by another staff
        Optional<HostelStaff> existingByEmail = staffDAO.findByEmail(staff.getEmail());
        if (existingByEmail.isPresent() && existingByEmail.get().getStaffId() != staff.getStaffId()) {
            throw new ServiceException("Email already exists");
        }
        
        // Validate block assignment if provided
        if (staff.getBlockId() != null) {
            validateBlockAssignment(staff.getBlockId());
        }
        
        return staffDAO.update(staff);
    }
    
    /**
     * Get staff by ID
     * @param staffId Staff ID
     * @return Staff if found
     * @throws SQLException if database error occurs
     * @throws ServiceException if staff not found
     */
    public HostelStaff getStaff(int staffId) throws SQLException, ServiceException {
        Optional<HostelStaff> staffOpt = staffDAO.findById(staffId);
        
        if (!staffOpt.isPresent()) {
            throw new ServiceException("Staff not found");
        }
        
        return staffOpt.get();
    }
    
    /**
     * Get staff by username
     * @param username Username
     * @return Staff if found
     * @throws SQLException if database error occurs
     * @throws ServiceException if staff not found
     */
    public HostelStaff getStaffByUsername(String username) throws SQLException, ServiceException {
        Optional<HostelStaff> staffOpt = staffDAO.findByUsername(username);
        
        if (!staffOpt.isPresent()) {
            throw new ServiceException("Staff not found");
        }
        
        return staffOpt.get();
    }
    
    /**
     * Change staff password
     * @param staffId Staff ID
     * @param oldPassword Old password (plain text)
     * @param newPassword New password (plain text)
     * @param authService Authentication service for password verification
     * @return true if password changed successfully
     * @throws SQLException if database error occurs
     * @throws ServiceException if validation fails
     */
    public boolean changePassword(int staffId, String oldPassword, String newPassword, 
                                  AuthenticationService authService) throws SQLException, ServiceException {
        // Get staff
        HostelStaff staff = getStaff(staffId);
        
        // Verify old password
        if (!authService.verifyPassword(oldPassword, staff.getPassword())) {
            throw new ServiceException("Current password is incorrect");
        }
        
        // Validate new password
        if (newPassword == null || newPassword.length() < 6) {
            throw new ServiceException("New password must be at least 6 characters");
        }
        
        // Hash new password
        String hashedPassword = authService.hashPassword(newPassword);
        
        // Update password
        boolean success = staffDAO.updatePassword(staffId, hashedPassword);
        
        if (success) {
            logger.info("Password changed for staff: {}", staffId);
        }
        
        return success;
    }
    
    /**
     * Assign staff to block
     * @param staffId Staff ID
     * @param blockId Block ID
     * @return true if assignment successful
     * @throws SQLException if database error occurs
     * @throws ServiceException if validation fails
     */
    public boolean assignToBlock(int staffId, int blockId) throws SQLException, ServiceException {
        // Validate block
        validateBlockAssignment(blockId);
        
        // Assign to block
        boolean success = staffDAO.assignToBlock(staffId, blockId);
        
        if (success) {
            logger.info("Staff assigned to block: staffId={}, blockId={}", staffId, blockId);
        }
        
        return success;
    }
    
    /**
     * Update staff availability
     * @param staffId Staff ID
     * @param isAvailable Availability status
     * @return true if update successful
     * @throws SQLException if database error occurs
     * @throws ServiceException if validation fails
     */
    public boolean updateAvailability(int staffId, boolean isAvailable) throws SQLException, ServiceException {
        // Check if staff exists
        if (!staffDAO.exists(staffId)) {
            throw new ServiceException("Staff not found");
        }
        
        boolean success = staffDAO.updateAvailability(staffId, isAvailable);
        
        if (success) {
            logger.info("Staff availability updated: staffId={}, available={}", staffId, isAvailable);
        }
        
        return success;
    }
    
    /**
     * Get all staff
     * @return List of all staff
     * @throws SQLException if database error occurs
     */
    public List<HostelStaff> getAllStaff() throws SQLException {
        return staffDAO.findAll();
    }
    
    /**
     * Search staff by name
     * @param name Name to search
     * @return List of matching staff
     * @throws SQLException if database error occurs
     */
    public List<HostelStaff> searchStaff(String name) throws SQLException {
        return staffDAO.searchByName(name);
    }
    
    /**
     * Get staff by role
     * @param role Staff role
     * @return List of staff with the role
     * @throws SQLException if database error occurs
     */
    public List<HostelStaff> getStaffByRole(HostelStaff.StaffRole role) throws SQLException {
        return staffDAO.findByRole(role);
    }
    
    /**
     * Get staff by block
     * @param blockId Block ID
     * @return List of staff in the block
     * @throws SQLException if database error occurs
     */
    public List<HostelStaff> getStaffByBlock(int blockId) throws SQLException {
        return staffDAO.findByBlockId(blockId);
    }
    
    /**
     * Get available staff
     * @return List of available staff
     * @throws SQLException if database error occurs
     */
    public List<HostelStaff> getAvailableStaff() throws SQLException {
        return staffDAO.findAvailable();
    }
    
    /**
     * Get available staff by role
     * @param role Staff role
     * @return List of available staff with the role
     * @throws SQLException if database error occurs
     */
    public List<HostelStaff> getAvailableStaffByRole(HostelStaff.StaffRole role) throws SQLException {
        return staffDAO.findAvailableByRole(role);
    }
    
    /**
     * Validate staff data
     * @param staff Staff to validate
     * @throws ServiceException if validation fails
     */
    private void validateStaff(HostelStaff staff) throws ServiceException {
        if (staff.getEmployeeNumber() == null || staff.getEmployeeNumber().trim().isEmpty()) {
            throw new ServiceException("Employee number is required");
        }
        
        if (staff.getUsername() == null || staff.getUsername().trim().isEmpty()) {
            throw new ServiceException("Username is required");
        }
        
        if (staff.getPassword() == null || staff.getPassword().trim().isEmpty()) {
            throw new ServiceException("Password is required");
        }
        
        if (staff.getFullName() == null || staff.getFullName().trim().isEmpty()) {
            throw new ServiceException("Full name is required");
        }
        
        if (staff.getEmail() == null || staff.getEmail().trim().isEmpty()) {
            throw new ServiceException("Email is required");
        }
        
        // Basic email validation
        if (!staff.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ServiceException("Invalid email format");
        }
        
        if (staff.getRole() == null) {
            throw new ServiceException("Role is required");
        }
    }
    
    /**
     * Validate block assignment
     * @param blockId Block ID
     * @throws SQLException if database error occurs
     * @throws ServiceException if validation fails
     */
    private void validateBlockAssignment(int blockId) throws SQLException, ServiceException {
        // Check if block exists
        Optional<HostelBlock> blockOpt = blockDAO.findById(blockId);
        if (!blockOpt.isPresent()) {
            throw new ServiceException("Block not found");
        }
        
        // Check if block is active
        if (!blockOpt.get().isActive()) {
            throw new ServiceException("Block is not active");
        }
    }
    
    public static class ServiceException extends Exception {
        public ServiceException(String message) {
            super(message);
        }
    }
}
