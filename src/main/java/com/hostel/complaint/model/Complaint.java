package com.hostel.complaint.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/**
 * Complaint model class
 * Represents complaints submitted by students
 */
public class Complaint {
    private int complaintId;
    private String complaintNumber;
    private int studentId;
    private int categoryId;
    private Integer assignedStaffId;
    private String title;
    private String description;
    private int blockId;
    private int roomId;
    private Priority priority;
    private ComplaintStatus status;
    private LocalDateTime submittedAt;
    private LocalDateTime assignedAt;
    private LocalDateTime inProgressAt;
    private LocalDateTime resolvedAt;
    private LocalDateTime closedAt;
    private String resolutionNotes;
    private String completionImagePath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // For display purposes - not stored in database
    private String studentName;
    private String studentNumber;
    private String categoryName;
    private String blockName;
    private String roomNumber;
    private String staffName;
    private String staffRole;
    private List<ComplaintImage> images;
    private List<ComplaintStatusHistory> statusHistory;

    public enum Priority {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    public enum ComplaintStatus {
        SUBMITTED, ASSIGNED, IN_PROGRESS, RESOLVED, CLOSED
    }

    public Complaint() {
        this.images = new ArrayList<>();
        this.statusHistory = new ArrayList<>();
    }

    public Complaint(int studentId, int categoryId, String title, String description, 
                     int blockId, int roomId, Priority priority) {
        this();
        this.studentId = studentId;
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.blockId = blockId;
        this.roomId = roomId;
        this.priority = priority;
        this.status = ComplaintStatus.SUBMITTED;
    }

    // Getters and Setters
    public int getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(int complaintId) {
        this.complaintId = complaintId;
    }

    public String getComplaintNumber() {
        return complaintNumber;
    }

    public void setComplaintNumber(String complaintNumber) {
        this.complaintNumber = complaintNumber;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getAssignedStaffId() {
        return assignedStaffId;
    }

    public void setAssignedStaffId(Integer assignedStaffId) {
        this.assignedStaffId = assignedStaffId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public ComplaintStatus getStatus() {
        return status;
    }

    public void setStatus(ComplaintStatus status) {
        this.status = status;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public LocalDateTime getInProgressAt() {
        return inProgressAt;
    }

    public void setInProgressAt(LocalDateTime inProgressAt) {
        this.inProgressAt = inProgressAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(LocalDateTime closedAt) {
        this.closedAt = closedAt;
    }

    public String getResolutionNotes() {
        return resolutionNotes;
    }

    public void setResolutionNotes(String resolutionNotes) {
        this.resolutionNotes = resolutionNotes;
    }

    public String getCompletionImagePath() {
        return completionImagePath;
    }

    public void setCompletionImagePath(String completionImagePath) {
        this.completionImagePath = completionImagePath;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Display field getters and setters
    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffRole() {
        return staffRole;
    }

    public void setStaffRole(String staffRole) {
        this.staffRole = staffRole;
    }

    public List<ComplaintImage> getImages() {
        return images;
    }

    public void setImages(List<ComplaintImage> images) {
        this.images = images;
    }

    public List<ComplaintStatusHistory> getStatusHistory() {
        return statusHistory;
    }

    public void setStatusHistory(List<ComplaintStatusHistory> statusHistory) {
        this.statusHistory = statusHistory;
    }

    public void addImage(ComplaintImage image) {
        this.images.add(image);
    }

    public void addStatusHistory(ComplaintStatusHistory history) {
        this.statusHistory.add(history);
    }

    /**
     * Calculate resolution time in hours
     * @return Resolution time in hours, or -1 if not resolved
     */
    public long getResolutionTimeHours() {
        if (resolvedAt == null || submittedAt == null) {
            return -1;
        }
        return java.time.Duration.between(submittedAt, resolvedAt).toHours();
    }

    @Override
    public String toString() {
        return "Complaint{" +
                "complaintId=" + complaintId +
                ", complaintNumber='" + complaintNumber + '\'' +
                ", studentId=" + studentId +
                ", categoryId=" + categoryId +
                ", assignedStaffId=" + assignedStaffId +
                ", title='" + title + '\'' +
                ", priority=" + priority +
                ", status=" + status +
                ", submittedAt=" + submittedAt +
                '}';
    }
}
