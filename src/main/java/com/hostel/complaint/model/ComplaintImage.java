package com.hostel.complaint.model;

import java.time.LocalDateTime;

/**
 * ComplaintImage model class
 * Represents images uploaded with complaints
 */
public class ComplaintImage {
    private int imageId;
    private int complaintId;
    private String imagePath;
    private String imageName;
    private LocalDateTime uploadedAt;

    public ComplaintImage() {
    }

    public ComplaintImage(int complaintId, String imagePath, String imageName) {
        this.complaintId = complaintId;
        this.imagePath = imagePath;
        this.imageName = imageName;
    }

    // Getters and Setters
    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(int complaintId) {
        this.complaintId = complaintId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    @Override
    public String toString() {
        return "ComplaintImage{" +
                "imageId=" + imageId +
                ", complaintId=" + complaintId +
                ", imagePath='" + imagePath + '\'' +
                ", imageName='" + imageName + '\'' +
                ", uploadedAt=" + uploadedAt +
                '}';
    }
}
