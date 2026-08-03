package com.hostel.complaint.model;

import java.time.LocalDateTime;

/**
 * Category model class
 * Represents complaint categories
 */
public class Category {
    private int categoryId;
    private String categoryName;
    private String description;
    private Complaint.Priority defaultPriority;
    private int estimatedResolutionHours;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Category() {
    }

    public Category(String categoryName, String description, Complaint.Priority defaultPriority, int estimatedResolutionHours) {
        this.categoryName = categoryName;
        this.description = description;
        this.defaultPriority = defaultPriority;
        this.estimatedResolutionHours = estimatedResolutionHours;
        this.isActive = true;
    }

    // Getters and Setters
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Complaint.Priority getDefaultPriority() {
        return defaultPriority;
    }

    public void setDefaultPriority(Complaint.Priority defaultPriority) {
        this.defaultPriority = defaultPriority;
    }

    public int getEstimatedResolutionHours() {
        return estimatedResolutionHours;
    }

    public void setEstimatedResolutionHours(int estimatedResolutionHours) {
        this.estimatedResolutionHours = estimatedResolutionHours;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", description='" + description + '\'' +
                ", defaultPriority=" + defaultPriority +
                ", estimatedResolutionHours=" + estimatedResolutionHours +
                ", isActive=" + isActive +
                '}';
    }
}
