package com.hostel.complaint.dao;

import com.hostel.complaint.model.Room;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Room DAO interface
 * Defines operations for room management
 */
public interface RoomDAO extends BaseDAO<Room> {
    
    /**
     * Find room by room number and block
     * @param roomNumber The room number
     * @param blockId The block ID
     * @return Optional containing the room if found
     * @throws SQLException if database error occurs
     */
    Optional<Room> findByRoomNumberAndBlock(String roomNumber, int blockId) throws SQLException;
    
    /**
     * Find rooms by block
     * @param blockId The block ID
     * @return List of rooms in the block
     * @throws SQLException if database error occurs
     */
    List<Room> findByBlockId(int blockId) throws SQLException;
    
    /**
     * Find all active rooms
     * @return List of active rooms
     * @throws SQLException if database error occurs
     */
    List<Room> findActive() throws SQLException;
    
    /**
     * Find available rooms (with space)
     * @return List of rooms with available space
     * @throws SQLException if database error occurs
     */
    List<Room> findAvailable() throws SQLException;
    
    /**
     * Find available rooms in a block
     * @param blockId The block ID
     * @return List of available rooms in the block
     * @throws SQLException if database error occurs
     */
    List<Room> findAvailableByBlock(int blockId) throws SQLException;
    
    /**
     * Check if room number exists in block
     * @param roomNumber The room number
     * @param blockId The block ID
     * @return true if room exists
     * @throws SQLException if database error occurs
     */
    boolean roomExistsInBlock(String roomNumber, int blockId) throws SQLException;
    
    /**
     * Update room occupancy
     * @param roomId The room ID
     * @param currentOccupancy The current occupancy
     * @return true if update was successful
     * @throws SQLException if database error occurs
     */
    boolean updateOccupancy(int roomId, int currentOccupancy) throws SQLException;
    
    /**
     * Increment room occupancy
     * @param roomId The room ID
     * @return true if update was successful
     * @throws SQLException if database error occurs
     */
    boolean incrementOccupancy(int roomId) throws SQLException;
    
    /**
     * Decrement room occupancy
     * @param roomId The room ID
     * @return true if update was successful
     * @throws SQLException if database error occurs
     */
    boolean decrementOccupancy(int roomId) throws SQLException;
}
