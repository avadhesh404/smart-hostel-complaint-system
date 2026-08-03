package com.hostel.complaint.dao;

import com.hostel.complaint.model.HostelBlock;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Hostel Block DAO interface
 * Defines operations for hostel block management
 */
public interface HostelBlockDAO extends BaseDAO<HostelBlock> {
    
    /**
     * Find block by block code
     * @param blockCode The block code
     * @return Optional containing the block if found
     * @throws SQLException if database error occurs
     */
    Optional<HostelBlock> findByBlockCode(String blockCode) throws SQLException;
    
    /**
     * Find all active blocks
     * @return List of active blocks
     * @throws SQLException if database error occurs
     */
    List<HostelBlock> findActive() throws SQLException;
    
    /**
     * Check if block code exists
     * @param blockCode The block code
     * @return true if block code exists
     * @throws SQLException if database error occurs
     */
    boolean blockCodeExists(String blockCode) throws SQLException;
    
    /**
     * Update room count for block
     * @param blockId The block ID
     * @param totalRooms The total number of rooms
     * @return true if update was successful
     * @throws SQLException if database error occurs
     */
    boolean updateRoomCount(int blockId, int totalRooms) throws SQLException;
}
