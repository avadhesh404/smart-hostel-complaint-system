package com.hostel.complaint.model;

import java.time.LocalDateTime;

/**
 * Room model class
 * Represents hostel rooms
 */
public class Room {
    private int roomId;
    private String roomNumber;
    private int blockId;
    private int capacity;
    private int currentOccupancy;
    private Integer floorNumber;
    private RoomType roomType;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // For display purposes
    private String blockName;
    private String blockCode;

    public enum RoomType {
        SINGLE, DOUBLE, TRIPLE, FOUR_BED
    }

    public Room() {
    }

    public Room(String roomNumber, int blockId, int capacity, Integer floorNumber, RoomType roomType) {
        this.roomNumber = roomNumber;
        this.blockId = blockId;
        this.capacity = capacity;
        this.floorNumber = floorNumber;
        this.roomType = roomType;
        this.isActive = true;
    }

    // Getters and Setters
    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCurrentOccupancy() {
        return currentOccupancy;
    }

    public void setCurrentOccupancy(int currentOccupancy) {
        this.currentOccupancy = currentOccupancy;
    }

    public Integer getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
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

    /**
     * Check if room has available space
     * @return true if room is not at full capacity
     */
    public boolean hasAvailability() {
        return currentOccupancy < capacity;
    }

    /**
     * Get available beds in the room
     * @return Number of available beds
     */
    public int getAvailableBeds() {
        return capacity - currentOccupancy;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", roomNumber='" + roomNumber + '\'' +
                ", blockId=" + blockId +
                ", capacity=" + capacity +
                ", currentOccupancy=" + currentOccupancy +
                ", floorNumber=" + floorNumber +
                ", roomType=" + roomType +
                ", isActive=" + isActive +
                '}';
    }
}
