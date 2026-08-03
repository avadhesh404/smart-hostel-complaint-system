package com.hostel.complaint.dao;

import com.hostel.complaint.model.ComplaintImage;

import java.sql.SQLException;
import java.util.List;

/**
 * Complaint Image DAO interface
 * Defines operations for complaint image management
 */
public interface ComplaintImageDAO extends BaseDAO<ComplaintImage> {
    
    /**
     * Find images by complaint ID
     * @param complaintId The complaint ID
     * @return List of images for the complaint
     * @throws SQLException if database error occurs
     */
    List<ComplaintImage> findByComplaintId(int complaintId) throws SQLException;
    
    /**
     * Delete all images for a complaint
     * @param complaintId The complaint ID
     * @return true if deletion was successful
     * @throws SQLException if database error occurs
     */
    boolean deleteByComplaintId(int complaintId) throws SQLException;
}
