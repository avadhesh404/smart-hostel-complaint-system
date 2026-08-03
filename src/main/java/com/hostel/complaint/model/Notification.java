package com.hostel.complaint.model;

import java.time.LocalDateTime;

/**
 * Notification model class
 * Represents notifications sent to users
 */
public class Notification {
    private int notificationId;
    private int userId;
    private UserType userType;
    private Integer complaintId;
    private String title;
    private String message;
    private boolean isRead;
    private LocalDateTime createdAt;

    public enum UserType {
        STUDENT, STAFF, ADMIN
    }

    public Notification() {
    }

    public Notification(int userId, UserType userType, Integer complaintId, String title, String message) {
        this.userId = userId;
        this.userType = userType;
        this.complaintId = complaintId;
        this.title = title;
        this.message = message;
        this.isRead = false;
    }

    // Getters and Setters
    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Integer getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(Integer complaintId) {
        this.complaintId = complaintId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notificationId=" + notificationId +
                ", userId=" + userId +
                ", userType=" + userType +
                ", complaintId=" + complaintId +
                ", title='" + title + '\'' +
                ", isRead=" + isRead +
                ", createdAt=" + createdAt +
                '}';
    }
}
