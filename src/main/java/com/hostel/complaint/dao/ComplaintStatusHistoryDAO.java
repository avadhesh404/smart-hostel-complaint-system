package com.hostel.complaint.dao;

import com.hostel.complaint.model.ComplaintStatusHistory;

import java.sql.SQLException;
import java.util.List;

/**
 * Complaint Status History DAO interface
 * Defines operations for tracking complaint status changes
 */
public interface ComplaintStatusHistoryDAO extends BaseDAO<ComplaintStatusHistory> {
    
    /**
     * Find status history for a complaint
     * @param complaintId The complaint ID
     * @return List of status history entries
     * @throws SQLException if database error occurs
     */
    List<ComplaintStatusHistory> findByComplaintId(int complaintId) throws SQLException;
    
    /**
     * Changed by role enum for database compatibility
     */
    enum ChangedByRole {
        STUDENT, STAFF, ADMIN
    }
}
