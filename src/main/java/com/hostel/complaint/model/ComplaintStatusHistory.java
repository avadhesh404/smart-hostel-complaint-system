package com.hostel.complaint.model;

import java.time.LocalDateTime;

/**
 * ComplaintStatusHistory model class
 * Represents the history of status changes for complaints
 */
public class ComplaintStatusHistory {
    private int historyId;
    private int complaintId;
    private Complaint.ComplaintStatus oldStatus;
    private Complaint.ComplaintStatus newStatus;
    private String changedBy;
    private ChangedByRole changedByRole;
    private String notes;
    private LocalDateTime changedAt;

    public enum ChangedByRole {
        STUDENT, STAFF, ADMIN
    }

    public ComplaintStatusHistory() {
    }

    public ComplaintStatusHistory(int complaintId, Complaint.ComplaintStatus oldStatus, 
                                  Complaint.ComplaintStatus newStatus, String changedBy, 
                                  ChangedByRole changedByRole, String notes) {
        this.complaintId = complaintId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.changedBy = changedBy;
        this.changedByRole = changedByRole;
        this.notes = notes;
    }

    // Getters and Setters
    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    public int getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(int complaintId) {
        this.complaintId = complaintId;
    }

    public Complaint.ComplaintStatus getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(Complaint.ComplaintStatus oldStatus) {
        this.oldStatus = oldStatus;
    }

    public Complaint.ComplaintStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(Complaint.ComplaintStatus newStatus) {
        this.newStatus = newStatus;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public ChangedByRole getChangedByRole() {
        return changedByRole;
    }

    public void setChangedByRole(ChangedByRole changedByRole) {
        this.changedByRole = changedByRole;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }

    @Override
    public String toString() {
        return "ComplaintStatusHistory{" +
                "historyId=" + historyId +
                ", complaintId=" + complaintId +
                ", oldStatus=" + oldStatus +
                ", newStatus=" + newStatus +
                ", changedBy='" + changedBy + '\'' +
                ", changedByRole=" + changedByRole +
                ", notes='" + notes + '\'' +
                ", changedAt=" + changedAt +
                '}';
    }
}
