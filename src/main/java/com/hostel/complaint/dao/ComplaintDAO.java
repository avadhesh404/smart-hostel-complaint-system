package com.hostel.complaint.dao;

import com.hostel.complaint.model.Complaint;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Complaint DAO interface
 * Defines operations for complaint management
 */
public interface ComplaintDAO extends BaseDAO<Complaint> {
    
    /**
     * Find a complaint by complaint number
     * @param complaintNumber The complaint number
     * @return Optional containing the complaint if found
     * @throws SQLException if database error occurs
     */
    Optional<Complaint> findByComplaintNumber(String complaintNumber) throws SQLException;
    
    /**
     * Find complaints by student ID
     * @param studentId The student ID
     * @return List of complaints for the student
     * @throws SQLException if database error occurs
     */
    List<Complaint> findByStudentId(int studentId) throws SQLException;
    
    /**
     * Find complaints by assigned staff ID
     * @param staffId The staff ID
     * @return List of complaints assigned to the staff
     * @throws SQLException if database error occurs
     */
    List<Complaint> findByStaffId(int staffId) throws SQLException;
    
    /**
     * Find complaints by status
     * @param status The complaint status
     * @return List of complaints with the given status
     * @throws SQLException if database error occurs
     */
    List<Complaint> findByStatus(Complaint.ComplaintStatus status) throws SQLException;
    
    /**
     * Find complaints by priority
     * @param priority The complaint priority
     * @return List of complaints with the given priority
     * @throws SQLException if database error occurs
     */
    List<Complaint> findByPriority(Complaint.Priority priority) throws SQLException;
    
    /**
     * Find complaints by category
     * @param categoryId The category ID
     * @return List of complaints in the category
     * @throws SQLException if database error occurs
     */
    List<Complaint> findByCategory(int categoryId) throws SQLException;
    
    /**
     * Find complaints by block and room
     * @param blockId The block ID
     * @param roomId The room ID
     * @return List of complaints for the block/room
     * @throws SQLException if database error occurs
     */
    List<Complaint> findByBlockAndRoom(int blockId, int roomId) throws SQLException;
    
    /**
     * Find complaints submitted within a date range
     * @param startDate Start date
     * @param endDate End date
     * @return List of complaints in the date range
     * @throws SQLException if database error occurs
     */
    List<Complaint> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws SQLException;
    
    /**
     * Find complaints with filters
     * @param status Optional status filter
     * @param priority Optional priority filter
     * @param categoryId Optional category filter
     * @param blockId Optional block filter
     * @param roomId Optional room filter
     * @param startDate Optional start date filter
     * @param endDate Optional end date filter
     * @return List of filtered complaints
     * @throws SQLException if database error occurs
     */
    List<Complaint> findWithFilters(Complaint.ComplaintStatus status, Complaint.Priority priority, 
                                   Integer categoryId, Integer blockId, Integer roomId,
                                   LocalDateTime startDate, LocalDateTime endDate) throws SQLException;
    
    /**
     * Update complaint status
     * @param complaintId The complaint ID
     * @param newStatus The new status
     * @param changedBy Username of person changing status
     * @param changedByRole Role of person changing status
     * @param notes Optional notes about the change
     * @return true if update was successful
     * @throws SQLException if database error occurs
     */
    boolean updateStatus(int complaintId, Complaint.ComplaintStatus newStatus, String changedBy, 
                       ComplaintStatusHistoryDAO.ChangedByRole changedByRole, String notes) throws SQLException;
    
    /**
     * Assign complaint to staff
     * @param complaintId The complaint ID
     * @param staffId The staff ID
     * @return true if assignment was successful
     * @throws SQLException if database error occurs
     */
    boolean assignToStaff(int complaintId, int staffId) throws SQLException;
    
    /**
     * Update complaint resolution
     * @param complaintId The complaint ID
     * @param resolutionNotes Resolution notes
     * @param completionImagePath Optional completion image path
     * @return true if update was successful
     * @throws SQLException if database error occurs
     */
    boolean updateResolution(int complaintId, String resolutionNotes, String completionImagePath) throws SQLException;
    
    /**
     * Get complaint statistics
     * @return Map containing statistics (total, pending, assigned, resolved, closed, critical)
     * @throws SQLException if database error occurs
     */
    java.util.Map<String, Long> getStatistics() throws SQLException;
    
    /**
     * Get average resolution time in hours
     * @return Average resolution time
     * @throws SQLException if database error occurs
     */
    double getAverageResolutionTime() throws SQLException;
    
    /**
     * Generate next complaint number
     * @return Next complaint number in format CMPYYYYXXXX
     * @throws SQLException if database error occurs
     */
    String generateNextComplaintNumber() throws SQLException;
    
    /**
     * Search complaints by student name
     * @param studentName The student name to search
     * @return List of matching complaints
     * @throws SQLException if database error occurs
     */
    List<Complaint> searchByStudentName(String studentName) throws SQLException;
    
    /**
     * Get complaints by category with statistics
     * @return Map of category name to complaint count
     * @throws SQLException if database error occurs
     */
    java.util.Map<String, Long> getComplaintsByCategory() throws SQLException;
    
    /**
     * Get monthly complaint trends
     * @param months Number of months to look back
     * @return List of monthly complaint counts
     * @throws SQLException if database error occurs
     */
    List<java.util.Map<String, Object>> getMonthlyTrends(int months) throws SQLException;
}
