package com.hostel.complaint.service;

import com.hostel.complaint.dao.*;
import com.hostel.complaint.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Admin Service
 * Handles admin-related business logic and system management
 */
public class AdminService {
    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);
    
    private final AdminDAO adminDAO;
    private final StudentDAO studentDAO;
    private final HostelStaffDAO staffDAO;
    private final CategoryDAO categoryDAO;
    private final HostelBlockDAO blockDAO;
    private final RoomDAO roomDAO;
    private final ComplaintDAO complaintDAO;
    private final FeedbackDAO feedbackDAO;
    private final AuditLogDAO auditLogDAO;
    
    public AdminService(AdminDAO adminDAO, StudentDAO studentDAO, HostelStaffDAO staffDAO,
                       CategoryDAO categoryDAO, HostelBlockDAO blockDAO, RoomDAO roomDAO,
                       ComplaintDAO complaintDAO, FeedbackDAO feedbackDAO, AuditLogDAO auditLogDAO) {
        this.adminDAO = adminDAO;
        this.studentDAO = studentDAO;
        this.staffDAO = staffDAO;
        this.categoryDAO = categoryDAO;
        this.blockDAO = blockDAO;
        this.roomDAO = roomDAO;
        this.complaintDAO = complaintDAO;
        this.feedbackDAO = feedbackDAO;
        this.auditLogDAO = auditLogDAO;
    }
    
    /**
     * Create a new admin
     * @param admin Admin to create
     * @return Created admin with generated ID
     * @throws SQLException if database error occurs
     * @throws ServiceException if business logic validation fails
     */
    public Admin createAdmin(Admin admin) throws SQLException, ServiceException {
        // Validate admin data
        validateAdmin(admin);
        
        // Check if username already exists
        if (adminDAO.usernameExists(admin.getUsername())) {
            throw new ServiceException("Username already exists");
        }
        
        // Check if email already exists
        if (adminDAO.emailExists(admin.getEmail())) {
            throw new ServiceException("Email already exists");
        }
        
        // Save admin
        Admin savedAdmin = adminDAO.save(admin);
        
        logger.info("Admin created successfully: {}", admin.getUsername());
        return savedAdmin;
    }
    
    /**
     * Update admin profile
     * @param admin Admin to update
     * @return true if update successful
     * @throws SQLException if database error occurs
     * @throws ServiceException if business logic validation fails
     */
    public boolean updateAdmin(Admin admin) throws SQLException, ServiceException {
        // Validate admin data
        validateAdmin(admin);
        
        // Check if admin exists
        if (!adminDAO.exists(admin.getAdminId())) {
            throw new ServiceException("Admin not found");
        }
        
        // Check if username is taken by another admin
        Optional<Admin> existingByUsername = adminDAO.findByUsername(admin.getUsername());
        if (existingByUsername.isPresent() && existingByUsername.get().getAdminId() != admin.getAdminId()) {
            throw new ServiceException("Username already exists");
        }
        
        // Check if email is taken by another admin
        Optional<Admin> existingByEmail = adminDAO.findByEmail(admin.getEmail());
        if (existingByEmail.isPresent() && existingByEmail.get().getAdminId() != admin.getAdminId()) {
            throw new ServiceException("Email already exists");
        }
        
        return adminDAO.update(admin);
    }
    
    /**
     * Change admin password
     * @param adminId Admin ID
     * @param oldPassword Old password (plain text)
     * @param newPassword New password (plain text)
     * @param authService Authentication service for password verification
     * @return true if password changed successfully
     * @throws SQLException if database error occurs
     * @throws ServiceException if validation fails
     */
    public boolean changePassword(int adminId, String oldPassword, String newPassword, 
                                  AuthenticationService authService) throws SQLException, ServiceException {
        // Get admin
        Admin admin = getAdmin(adminId);
        
        // Verify old password
        if (!authService.verifyPassword(oldPassword, admin.getPassword())) {
            throw new ServiceException("Current password is incorrect");
        }
        
        // Validate new password
        if (newPassword == null || newPassword.length() < 6) {
            throw new ServiceException("New password must be at least 6 characters");
        }
        
        // Hash new password
        String hashedPassword = authService.hashPassword(newPassword);
        
        // Update password
        boolean success = adminDAO.updatePassword(adminId, hashedPassword);
        
        if (success) {
            logger.info("Password changed for admin: {}", adminId);
        }
        
        return success;
    }
    
    /**
     * Get admin by ID
     * @param adminId Admin ID
     * @return Admin if found
     * @throws SQLException if database error occurs
     * @throws ServiceException if admin not found
     */
    public Admin getAdmin(int adminId) throws SQLException, ServiceException {
        Optional<Admin> adminOpt = adminDAO.findById(adminId);
        
        if (!adminOpt.isPresent()) {
            throw new ServiceException("Admin not found");
        }
        
        return adminOpt.get();
    }
    
    /**
     * Get all admins
     * @return List of all admins
     * @throws SQLException if database error occurs
     */
    public List<Admin> getAllAdmins() throws SQLException {
        return adminDAO.findAll();
    }
    
    /**
     * Manage categories
     */
    public Category createCategory(Category category) throws SQLException, ServiceException {
        validateCategory(category);
        
        if (categoryDAO.nameExists(category.getCategoryName())) {
            throw new ServiceException("Category name already exists");
        }
        
        return categoryDAO.save(category);
    }
    
    public boolean updateCategory(Category category) throws SQLException, ServiceException {
        validateCategory(category);
        
        if (!categoryDAO.exists(category.getCategoryId())) {
            throw new ServiceException("Category not found");
        }
        
        return categoryDAO.update(category);
    }
    
    public boolean deleteCategory(int categoryId) throws SQLException, ServiceException {
        if (!categoryDAO.exists(categoryId)) {
            throw new ServiceException("Category not found");
        }
        
        return categoryDAO.delete(categoryId);
    }
    
    public List<Category> getAllCategories() throws SQLException {
        return categoryDAO.findAll();
    }
    
    public List<Category> getActiveCategories() throws SQLException {
        return categoryDAO.findActive();
    }
    
    /**
     * Manage hostel blocks
     */
    public HostelBlock createBlock(HostelBlock block) throws SQLException, ServiceException {
        validateBlock(block);
        
        if (blockDAO.blockCodeExists(block.getBlockCode())) {
            throw new ServiceException("Block code already exists");
        }
        
        return blockDAO.save(block);
    }
    
    public boolean updateBlock(HostelBlock block) throws SQLException, ServiceException {
        validateBlock(block);
        
        if (!blockDAO.exists(block.getBlockId())) {
            throw new ServiceException("Block not found");
        }
        
        return blockDAO.update(block);
    }
    
    public boolean deleteBlock(int blockId) throws SQLException, ServiceException {
        if (!blockDAO.exists(blockId)) {
            throw new ServiceException("Block not found");
        }
        
        return blockDAO.delete(blockId);
    }
    
    public List<HostelBlock> getAllBlocks() throws SQLException {
        return blockDAO.findAll();
    }
    
    public List<HostelBlock> getActiveBlocks() throws SQLException {
        return blockDAO.findActive();
    }
    
    /**
     * Manage rooms
     */
    public Room createRoom(Room room) throws SQLException, ServiceException {
        validateRoom(room);
        
        if (roomDAO.roomExistsInBlock(room.getRoomNumber(), room.getBlockId())) {
            throw new ServiceException("Room number already exists in this block");
        }
        
        Room savedRoom = roomDAO.save(room);
        
        // Update block room count
        blockDAO.updateRoomCount(room.getBlockId(), blockDAO.count());
        
        return savedRoom;
    }
    
    public boolean updateRoom(Room room) throws SQLException, ServiceException {
        validateRoom(room);
        
        if (!roomDAO.exists(room.getRoomId())) {
            throw new ServiceException("Room not found");
        }
        
        return roomDAO.update(room);
    }
    
    public boolean deleteRoom(int roomId) throws SQLException, ServiceException {
        if (!roomDAO.exists(roomId)) {
            throw new ServiceException("Room not found");
        }
        
        Room room = roomDAO.findById(roomId).orElse(null);
        boolean success = roomDAO.delete(roomId);
        
        if (success && room != null) {
            // Update block room count
            blockDAO.updateRoomCount(room.getBlockId(), blockDAO.count());
        }
        
        return success;
    }
    
    public List<Room> getAllRooms() throws SQLException {
        return roomDAO.findAll();
    }
    
    public List<Room> getRoomsByBlock(int blockId) throws SQLException {
        return roomDAO.findByBlockId(blockId);
    }
    
    /**
     * Get dashboard statistics
     */
    public Map<String, Object> getDashboardStatistics() throws SQLException {
        Map<String, Object> stats = new java.util.HashMap<>();
        
        // User counts
        stats.put("totalStudents", studentDAO.count());
        stats.put("totalStaff", staffDAO.count());
        stats.put("totalAdmins", adminDAO.count());
        
        // Infrastructure counts
        stats.put("totalBlocks", blockDAO.count());
        stats.put("totalRooms", roomDAO.count());
        stats.put("totalCategories", categoryDAO.count());
        
        // Complaint statistics
        Map<String, Long> complaintStats = complaintDAO.getStatistics();
        stats.putAll(complaintStats);
        
        // Performance metrics
        stats.put("averageResolutionTime", complaintDAO.getAverageResolutionTime());
        stats.put("overallRating", feedbackDAO.getOverallAverageRating());
        
        return stats;
    }
    
    /**
     * Get complaints by category statistics
     */
    public Map<String, Long> getComplaintsByCategory() throws SQLException {
        return complaintDAO.getComplaintsByCategory();
    }
    
    /**
     * Get monthly complaint trends
     */
    public List<Map<String, Object>> getMonthlyTrends(int months) throws SQLException {
        return complaintDAO.getMonthlyTrends(months);
    }
    
    /**
     * Get recent audit logs
     */
    public List<AuditLog> getRecentAuditLogs(int limit) throws SQLException {
        return auditLogDAO.findRecent(limit);
    }
    
    /**
     * Validate admin data
     */
    private void validateAdmin(Admin admin) throws ServiceException {
        if (admin.getUsername() == null || admin.getUsername().trim().isEmpty()) {
            throw new ServiceException("Username is required");
        }
        
        if (admin.getPassword() == null || admin.getPassword().trim().isEmpty()) {
            throw new ServiceException("Password is required");
        }
        
        if (admin.getFullName() == null || admin.getFullName().trim().isEmpty()) {
            throw new ServiceException("Full name is required");
        }
        
        if (admin.getEmail() == null || admin.getEmail().trim().isEmpty()) {
            throw new ServiceException("Email is required");
        }
        
        if (!admin.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ServiceException("Invalid email format");
        }
    }
    
    /**
     * Validate category data
     */
    private void validateCategory(Category category) throws ServiceException {
        if (category.getCategoryName() == null || category.getCategoryName().trim().isEmpty()) {
            throw new ServiceException("Category name is required");
        }
        
        if (category.getDefaultPriority() == null) {
            throw new ServiceException("Default priority is required");
        }
        
        if (category.getEstimatedResolutionHours() <= 0) {
            throw new ServiceException("Estimated resolution hours must be positive");
        }
    }
    
    /**
     * Validate block data
     */
    private void validateBlock(HostelBlock block) throws ServiceException {
        if (block.getBlockName() == null || block.getBlockName().trim().isEmpty()) {
            throw new ServiceException("Block name is required");
        }
        
        if (block.getBlockCode() == null || block.getBlockCode().trim().isEmpty()) {
            throw new ServiceException("Block code is required");
        }
    }
    
    /**
     * Validate room data
     */
    private void validateRoom(Room room) throws ServiceException {
        if (room.getRoomNumber() == null || room.getRoomNumber().trim().isEmpty()) {
            throw new ServiceException("Room number is required");
        }
        
        if (room.getBlockId() <= 0) {
            throw new ServiceException("Valid block ID is required");
        }
        
        if (room.getCapacity() <= 0) {
            throw new ServiceException("Capacity must be positive");
        }
        
        if (room.getRoomType() == null) {
            throw new ServiceException("Room type is required");
        }
    }
    
    public static class ServiceException extends Exception {
        public ServiceException(String message) {
            super(message);
        }
    }
}
