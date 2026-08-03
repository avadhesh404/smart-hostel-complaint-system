package com.hostel.complaint.service;

import com.hostel.complaint.dao.*;
import com.hostel.complaint.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Complaint Service
 * Handles complaint business logic
 */
public class ComplaintService {
    private static final Logger logger = LoggerFactory.getLogger(ComplaintService.class);
    
    private final ComplaintDAO complaintDAO;
    private final ComplaintStatusHistoryDAO statusHistoryDAO;
    private final ComplaintImageDAO imageDAO;
    private final CategoryDAO categoryDAO;
    private final StudentDAO studentDAO;
    private final HostelStaffDAO staffDAO;
    private final NotificationDAO notificationDAO;
    private final FeedbackDAO feedbackDAO;
    
    public ComplaintService(ComplaintDAO complaintDAO, ComplaintStatusHistoryDAO statusHistoryDAO,
                          ComplaintImageDAO imageDAO, CategoryDAO categoryDAO,
                          StudentDAO studentDAO, HostelStaffDAO staffDAO,
                          NotificationDAO notificationDAO, FeedbackDAO feedbackDAO) {
        this.complaintDAO = complaintDAO;
        this.statusHistoryDAO = statusHistoryDAO;
        this.imageDAO = imageDAO;
        this.categoryDAO = categoryDAO;
        this.studentDAO = studentDAO;
        this.staffDAO = staffDAO;
        this.notificationDAO = notificationDAO;
        this.feedbackDAO = feedbackDAO;
    }
    
    /**
     * Submit a new complaint
     * @param complaint Complaint to submit
     * @return Submitted complaint with generated ID
     * @throws SQLException if database error occurs
     * @throws ServiceException if business logic validation fails
     */
    public Complaint submitComplaint(Complaint complaint) throws SQLException, ServiceException {
        // Validate complaint
        validateComplaint(complaint);
        
        // Check if student exists
        if (!studentDAO.exists(complaint.getStudentId())) {
            throw new ServiceException("Student not found");
        }
        
        // Check if category exists
        if (!categoryDAO.exists(complaint.getCategoryId())) {
            throw new ServiceException("Category not found");
        }
        
        // Set default priority from category if not set
        if (complaint.getPriority() == null) {
            Optional<Category> categoryOpt = categoryDAO.findById(complaint.getCategoryId());
            if (categoryOpt.isPresent()) {
                complaint.setPriority(categoryOpt.get().getDefaultPriority());
            }
        }
        
        // Generate complaint number
        String complaintNumber = complaintDAO.generateNextComplaintNumber();
        complaint.setComplaintNumber(complaintNumber);
        complaint.setStatus(Complaint.ComplaintStatus.SUBMITTED);
        complaint.setSubmittedAt(LocalDateTime.now());
        
        // Save complaint
        Complaint savedComplaint = complaintDAO.save(complaint);
        
        // Save images if any
        if (complaint.getImages() != null && !complaint.getImages().isEmpty()) {
            for (ComplaintImage image : complaint.getImages()) {
                image.setComplaintId(savedComplaint.getComplaintId());
                imageDAO.save(image);
            }
        }
        
        // Notify admins about new complaint
        notifyNewComplaint(savedComplaint);
        
        logger.info("Complaint submitted successfully: {}", complaintNumber);
        return savedComplaint;
    }
    
    /**
     * Get complaint with full details including history and images
     * @param complaintId Complaint ID
     * @return Complaint with full details
     * @throws SQLException if database error occurs
     * @throws ServiceException if complaint not found
     */
    public Complaint getComplaintWithDetails(int complaintId) throws SQLException, ServiceException {
        Optional<Complaint> complaintOpt = complaintDAO.findById(complaintId);
        
        if (!complaintOpt.isPresent()) {
            throw new ServiceException("Complaint not found");
        }
        
        Complaint complaint = complaintOpt.get();
        
        // Load status history
        List<ComplaintStatusHistory> history = statusHistoryDAO.findByComplaintId(complaintId);
        complaint.setStatusHistory(history);
        
        // Load images
        List<ComplaintImage> images = imageDAO.findByComplaintId(complaintId);
        complaint.setImages(images);
        
        return complaint;
    }
    
    /**
     * Assign complaint to staff
     * @param complaintId Complaint ID
     * @param staffId Staff ID
     * @param changedBy Username of person making the change
     * @param changedByRole Role of person making the change
     * @throws SQLException if database error occurs
     * @throws ServiceException if business logic validation fails
     */
    public void assignComplaint(int complaintId, int staffId, String changedBy, 
                               ComplaintStatusHistoryDAO.ChangedByRole changedByRole) 
            throws SQLException, ServiceException {
        
        // Validate complaint exists
        Optional<Complaint> complaintOpt = complaintDAO.findById(complaintId);
        if (!complaintOpt.isPresent()) {
            throw new ServiceException("Complaint not found");
        }
        
        Complaint complaint = complaintOpt.get();
        
        // Validate staff exists and is available
        Optional<HostelStaff> staffOpt = staffDAO.findById(staffId);
        if (!staffOpt.isPresent()) {
            throw new ServiceException("Staff not found");
        }
        
        HostelStaff staff = staffOpt.get();
        if (!staff.isAvailable() || !staff.isActive()) {
            throw new ServiceException("Staff is not available");
        }
        
        // Assign complaint
        complaintDAO.assignToStaff(complaintId, staffId);
        
        // Update status
        complaintDAO.updateStatus(complaintId, Complaint.ComplaintStatus.ASSIGNED, 
                                changedBy, changedByRole, "Assigned to " + staff.getFullName());
        
        // Notify staff
        notifyStaffAssignment(complaint, staff);
        
        logger.info("Complaint assigned: complaintId={}, staffId={}", complaintId, staffId);
    }
    
    /**
     * Update complaint status
     * @param complaintId Complaint ID
     * @param newStatus New status
     * @param changedBy Username of person making the change
     * @param changedByRole Role of person making the change
     * @param notes Optional notes
     * @throws SQLException if database error occurs
     * @throws ServiceException if business logic validation fails
     */
    public void updateComplaintStatus(int complaintId, Complaint.ComplaintStatus newStatus, 
                                     String changedBy, ComplaintStatusHistoryDAO.ChangedByRole changedByRole, 
                                     String notes) throws SQLException, ServiceException {
        
        // Validate complaint exists
        Optional<Complaint> complaintOpt = complaintDAO.findById(complaintId);
        if (!complaintOpt.isPresent()) {
            throw new ServiceException("Complaint not found");
        }
        
        Complaint complaint = complaintOpt.get();
        
        // Validate status transition
        if (!isValidStatusTransition(complaint.getStatus(), newStatus)) {
            throw new ServiceException("Invalid status transition from " + 
                                      complaint.getStatus() + " to " + newStatus);
        }
        
        // Update status
        complaintDAO.updateStatus(complaintId, newStatus, changedBy, changedByRole, notes);
        
        // Notify relevant users
        notifyStatusChange(complaint, newStatus);
        
        logger.info("Complaint status updated: complaintId={}, newStatus={}", complaintId, newStatus);
    }
    
    /**
     * Resolve complaint
     * @param complaintId Complaint ID
     * @param resolutionNotes Resolution notes
     * @param completionImagePath Optional completion image path
     * @param changedBy Username of person resolving
     * @param changedByRole Role of person resolving
     * @throws SQLException if database error occurs
     * @throws ServiceException if business logic validation fails
     */
    public void resolveComplaint(int complaintId, String resolutionNotes, String completionImagePath,
                                String changedBy, ComplaintStatusHistoryDAO.ChangedByRole changedByRole) 
            throws SQLException, ServiceException {
        
        // Validate complaint exists
        Optional<Complaint> complaintOpt = complaintDAO.findById(complaintId);
        if (!complaintOpt.isPresent()) {
            throw new ServiceException("Complaint not found");
        }
        
        Complaint complaint = complaintOpt.get();
        
        // Validate current status
        if (complaint.getStatus() != Complaint.ComplaintStatus.IN_PROGRESS) {
            throw new ServiceException("Complaint must be in progress to be resolved");
        }
        
        // Update resolution
        complaintDAO.updateResolution(complaintId, resolutionNotes, completionImagePath);
        
        // Update status
        complaintDAO.updateStatus(complaintId, Complaint.ComplaintStatus.RESOLVED, 
                                changedBy, changedByRole, resolutionNotes);
        
        // Notify student
        notifyResolution(complaint);
        
        logger.info("Complaint resolved: complaintId={}", complaintId);
    }
    
    /**
     * Close complaint
     * @param complaintId Complaint ID
     * @param changedBy Username of person closing
     * @param changedByRole Role of person closing
     * @throws SQLException if database error occurs
     * @throws ServiceException if business logic validation fails
     */
    public void closeComplaint(int complaintId, String changedBy, 
                             ComplaintStatusHistoryDAO.ChangedByRole changedByRole) 
            throws SQLException, ServiceException {
        
        // Validate complaint exists
        Optional<Complaint> complaintOpt = complaintDAO.findById(complaintId);
        if (!complaintOpt.isPresent()) {
            throw new ServiceException("Complaint not found");
        }
        
        Complaint complaint = complaintOpt.get();
        
        // Validate current status
        if (complaint.getStatus() != Complaint.ComplaintStatus.RESOLVED) {
            throw new ServiceException("Complaint must be resolved before closing");
        }
        
        // Update status
        complaintDAO.updateStatus(complaintId, Complaint.ComplaintStatus.CLOSED, 
                                changedBy, changedByRole, "Complaint closed");
        
        logger.info("Complaint closed: complaintId={}", complaintId);
    }
    
    /**
     * Submit feedback for resolved complaint
     * @param feedback Feedback to submit
     * @throws SQLException if database error occurs
     * @throws ServiceException if business logic validation fails
     */
    public void submitFeedback(Feedback feedback) throws SQLException, ServiceException {
        // Validate complaint exists and is resolved
        Optional<Complaint> complaintOpt = complaintDAO.findById(feedback.getComplaintId());
        if (!complaintOpt.isPresent()) {
            throw new ServiceException("Complaint not found");
        }
        
        Complaint complaint = complaintOpt.get();
        if (complaint.getStatus() != Complaint.ComplaintStatus.RESOLVED && 
            complaint.getStatus() != Complaint.ComplaintStatus.CLOSED) {
            throw new ServiceException("Can only submit feedback for resolved complaints");
        }
        
        // Check if feedback already exists
        if (feedbackDAO.existsForComplaint(feedback.getComplaintId(), feedback.getStudentId())) {
            throw new ServiceException("Feedback already submitted for this complaint");
        }
        
        // Validate rating
        if (feedback.getRating() != null && (feedback.getRating() < 1 || feedback.getRating() > 5)) {
            throw new ServiceException("Rating must be between 1 and 5");
        }
        
        feedbackDAO.save(feedback);
        
        logger.info("Feedback submitted: complaintId={}", feedback.getComplaintId());
    }
    
    /**
     * Get complaint statistics
     * @return Map containing statistics
     * @throws SQLException if database error occurs
     */
    public Map<String, Long> getStatistics() throws SQLException {
        return complaintDAO.getStatistics();
    }
    
    /**
     * Get complaints by student
     * @param studentId Student ID
     * @return List of complaints
     * @throws SQLException if database error occurs
     */
    public List<Complaint> getComplaintsByStudent(int studentId) throws SQLException {
        return complaintDAO.findByStudentId(studentId);
    }
    
    /**
     * Get complaints assigned to staff
     * @param staffId Staff ID
     * @return List of complaints
     * @throws SQLException if database error occurs
     */
    public List<Complaint> getComplaintsByStaff(int staffId) throws SQLException {
        return complaintDAO.findByStaffId(staffId);
    }
    
    /**
     * Search complaints with filters
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
    public List<Complaint> searchComplaints(Complaint.ComplaintStatus status, Complaint.Priority priority,
                                            Integer categoryId, Integer blockId, Integer roomId,
                                            LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        return complaintDAO.findWithFilters(status, priority, categoryId, blockId, roomId, startDate, endDate);
    }
    
    /**
     * Get available staff for assignment
     * @param role Optional role filter
     * @return List of available staff
     * @throws SQLException if database error occurs
     */
    public List<HostelStaff> getAvailableStaff(HostelStaff.StaffRole role) throws SQLException {
        if (role != null) {
            return staffDAO.findAvailableByRole(role);
        }
        return staffDAO.findAvailable();
    }
    
    /**
     * Validate complaint before submission
     * @param complaint Complaint to validate
     * @throws ServiceException if validation fails
     */
    private void validateComplaint(Complaint complaint) throws ServiceException {
        if (complaint.getTitle() == null || complaint.getTitle().trim().isEmpty()) {
            throw new ServiceException("Title is required");
        }
        
        if (complaint.getDescription() == null || complaint.getDescription().trim().isEmpty()) {
            throw new ServiceException("Description is required");
        }
        
        if (complaint.getStudentId() <= 0) {
            throw new ServiceException("Valid student ID is required");
        }
        
        if (complaint.getCategoryId() <= 0) {
            throw new ServiceException("Valid category ID is required");
        }
        
        if (complaint.getBlockId() <= 0) {
            throw new ServiceException("Valid block ID is required");
        }
        
        if (complaint.getRoomId() <= 0) {
            throw new ServiceException("Valid room ID is required");
        }
    }
    
    /**
     * Check if status transition is valid
     * @param currentStatus Current status
     * @param newStatus New status
     * @return true if transition is valid
     */
    private boolean isValidStatusTransition(Complaint.ComplaintStatus currentStatus, 
                                           Complaint.ComplaintStatus newStatus) {
        switch (currentStatus) {
            case SUBMITTED:
                return newStatus == Complaint.ComplaintStatus.ASSIGNED;
            case ASSIGNED:
                return newStatus == Complaint.ComplaintStatus.IN_PROGRESS;
            case IN_PROGRESS:
                return newStatus == Complaint.ComplaintStatus.RESOLVED;
            case RESOLVED:
                return newStatus == Complaint.ComplaintStatus.CLOSED;
            case CLOSED:
                return false; // Cannot change from closed
            default:
                return false;
        }
    }
    
    /**
     * Notify admins about new complaint
     * @param complaint New complaint
     * @throws SQLException if database error occurs
     */
    private void notifyNewComplaint(Complaint complaint) throws SQLException {
        // In a real application, this would notify all admins
        // For now, we'll just log it
        logger.info("New complaint notification: {}", complaint.getComplaintNumber());
    }
    
    /**
     * Notify staff about assignment
     * @param complaint Assigned complaint
     * @param staff Assigned staff
     * @throws SQLException if database error occurs
     */
    private void notifyStaffAssignment(Complaint complaint, HostelStaff staff) throws SQLException {
        Notification notification = new Notification(
            staff.getStaffId(),
            Notification.UserType.STAFF,
            complaint.getComplaintId(),
            "New Complaint Assigned",
            "Complaint " + complaint.getComplaintNumber() + " has been assigned to you."
        );
        notificationDAO.save(notification);
    }
    
    /**
     * Notify about status change
     * @param complaint Complaint with status change
     * @param newStatus New status
     * @throws SQLException if database error occurs
     */
    private void notifyStatusChange(Complaint complaint, Complaint.ComplaintStatus newStatus) throws SQLException {
        Notification notification = new Notification(
            complaint.getStudentId(),
            Notification.UserType.STUDENT,
            complaint.getComplaintId(),
            "Complaint Status Updated",
            "Your complaint " + complaint.getComplaintNumber() + " status is now: " + newStatus
        );
        notificationDAO.save(notification);
    }
    
    /**
     * Notify student about resolution
     * @param complaint Resolved complaint
     * @throws SQLException if database error occurs
     */
    private void notifyResolution(Complaint complaint) throws SQLException {
        Notification notification = new Notification(
            complaint.getStudentId(),
            Notification.UserType.STUDENT,
            complaint.getComplaintId(),
            "Complaint Resolved",
            "Your complaint " + complaint.getComplaintNumber() + " has been resolved. Please rate the service."
        );
        notificationDAO.save(notification);
    }
    
    public static class ServiceException extends Exception {
        public ServiceException(String message) {
            super(message);
        }
    }
}
