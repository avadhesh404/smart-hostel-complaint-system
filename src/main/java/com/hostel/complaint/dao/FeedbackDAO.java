package com.hostel.complaint.dao;

import com.hostel.complaint.model.Feedback;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Feedback DAO interface
 * Defines operations for feedback management
 */
public interface FeedbackDAO extends BaseDAO<Feedback> {
    
    /**
     * Find feedback by complaint ID
     * @param complaintId The complaint ID
     * @return Optional containing the feedback if found
     * @throws SQLException if database error occurs
     */
    Optional<Feedback> findByComplaintId(int complaintId) throws SQLException;
    
    /**
     * Find feedback by student ID
     * @param studentId The student ID
     * @return List of feedback from the student
     * @throws SQLException if database error occurs
     */
    List<Feedback> findByStudentId(int studentId) throws SQLException;
    
    /**
     * Check if feedback exists for complaint
     * @param complaintId The complaint ID
     * @param studentId The student ID
     * @return true if feedback exists
     * @throws SQLException if database error occurs
     */
    boolean existsForComplaint(int complaintId, int studentId) throws SQLException;
    
    /**
     * Get average rating for a staff member
     * @param staffId The staff ID
     * @return Average rating
     * @throws SQLException if database error occurs
     */
    Double getAverageRatingForStaff(int staffId) throws SQLException;
    
    /**
     * Get average rating for a category
     * @param categoryId The category ID
     * @return Average rating
     * @throws SQLException if database error occurs
     */
    Double getAverageRatingForCategory(int categoryId) throws SQLException;
    
    /**
     * Get overall average rating
     * @return Overall average rating
     * @throws SQLException if database error occurs
     */
    Double getOverallAverageRating() throws SQLException;
}
