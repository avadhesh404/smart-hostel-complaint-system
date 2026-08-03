package com.hostel.complaint.model;

import java.time.LocalDateTime;

/**
 * HostelBlock model class
 * Represents hostel blocks
 */
public class HostelBlock {
    private int blockId;
    private String blockName;
    private String blockCode;
    private String description;
    private int totalRooms;
    private String wardenName;
    private String wardenPhone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isActive;

    public HostelBlock() {
    }

    public HostelBlock(String blockName, String blockCode, String description, String wardenName, String wardenPhone) {
        this.blockName = blockName;
        this.blockCode = blockCode;
        this.description = description;
        this.wardenName = wardenName;
        this.wardenPhone = wardenPhone;
        this.isActive = true;
    }

    // Getters and Setters
    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getBlockCode() {
        return blockCode;
    }

    public void setBlockCode(String blockCode) {
        this.blockCode = blockCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(int totalRooms) {
        this.totalRooms = totalRooms;
    }

    public String getWardenName() {
        return wardenName;
    }

    public void setWardenName(String wardenName) {
        this.wardenName = wardenName;
    }

    public String getWardenPhone() {
        return wardenPhone;
    }

    public void setWardenPhone(String wardenPhone) {
        this.wardenPhone = wardenPhone;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "HostelBlock{" +
                "blockId=" + blockId +
                ", blockName='" + blockName + '\'' +
                ", blockCode='" + blockCode + '\'' +
                ", description='" + description + '\'' +
                ", totalRooms=" + totalRooms +
                ", wardenName='" + wardenName + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
