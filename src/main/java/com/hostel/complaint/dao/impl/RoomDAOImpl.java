package com.hostel.complaint.dao.impl;

import com.hostel.complaint.dao.RoomDAO;
import com.hostel.complaint.database.DatabaseConnection;
import com.hostel.complaint.model.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Room DAO implementation
 */
public class RoomDAOImpl implements RoomDAO {
    private static final Logger logger = LoggerFactory.getLogger(RoomDAOImpl.class);

    @Override
    public Room save(Room room) throws SQLException {
        String sql = "INSERT INTO rooms (room_number, block_id, capacity, floor_number, room_type) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            statement.setString(1, room.getRoomNumber());
            statement.setInt(2, room.getBlockId());
            statement.setInt(3, room.getCapacity());
            
            if (room.getFloorNumber() != null) {
                statement.setInt(4, room.getFloorNumber());
            } else {
                statement.setNull(4, Types.INTEGER);
            }
            
            statement.setString(5, room.getRoomType().name());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating room failed, no rows affected.");
            }
            
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                room.setRoomId(resultSet.getInt(1));
            }
            
            connection.commit();
            logger.info("Room saved successfully: {}", room.getRoomNumber());
            return room;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error saving room", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean update(Room room) throws SQLException {
        String sql = "UPDATE rooms SET room_number = ?, block_id = ?, capacity = ?, current_occupancy = ?, " +
                     "floor_number = ?, room_type = ?, is_active = ?, updated_at = ? WHERE room_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            
            statement.setString(1, room.getRoomNumber());
            statement.setInt(2, room.getBlockId());
            statement.setInt(3, room.getCapacity());
            statement.setInt(4, room.getCurrentOccupancy());
            
            if (room.getFloorNumber() != null) {
                statement.setInt(5, room.getFloorNumber());
            } else {
                statement.setNull(5, Types.INTEGER);
            }
            
            statement.setString(6, room.getRoomType().name());
            statement.setBoolean(7, room.isActive());
            statement.setTimestamp(8, Timestamp.valueOf(java.time.LocalDateTime.now()));
            statement.setInt(9, room.getRoomId());
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            
            logger.info("Room updated successfully: {}", room.getRoomId());
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error updating room", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM rooms WHERE room_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            
            logger.info("Room deleted successfully: {}", id);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error deleting room", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    @Override
    public Optional<Room> findById(int id) throws SQLException {
        String sql = "SELECT r.*, hb.block_name, hb.block_code FROM rooms r " +
                     "LEFT JOIN hostel_blocks hb ON r.block_id = hb.block_id " +
                     "WHERE r.room_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToRoom(resultSet));
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error finding room by ID", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<Room> findAll() throws SQLException {
        String sql = "SELECT r.*, hb.block_name, hb.block_code FROM rooms r " +
                     "LEFT JOIN hostel_blocks hb ON r.block_id = hb.block_id " +
                     "ORDER BY hb.block_name, r.room_number";
        
        return executeQuery(sql);
    }

    @Override
    public boolean exists(int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM rooms WHERE room_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error checking room existence", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM rooms";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            
            return 0;
            
        } catch (SQLException e) {
            logger.error("Error counting rooms", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public Optional<Room> findByRoomNumberAndBlock(String roomNumber, int blockId) throws SQLException {
        String sql = "SELECT r.*, hb.block_name, hb.block_code FROM rooms r " +
                     "LEFT JOIN hostel_blocks hb ON r.block_id = hb.block_id " +
                     "WHERE r.room_number = ? AND r.block_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, roomNumber);
            statement.setInt(2, blockId);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToRoom(resultSet));
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error finding room by number and block", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<Room> findByBlockId(int blockId) throws SQLException {
        String sql = "SELECT r.*, hb.block_name, hb.block_code FROM rooms r " +
                     "LEFT JOIN hostel_blocks hb ON r.block_id = hb.block_id " +
                     "WHERE r.block_id = ? ORDER BY r.room_number";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, blockId);
            
            resultSet = statement.executeQuery();
            
            List<Room> rooms = new ArrayList<>();
            while (resultSet.next()) {
                rooms.add(mapResultSetToRoom(resultSet));
            }
            
            return rooms;
            
        } catch (SQLException e) {
            logger.error("Error finding rooms by block ID", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<Room> findActive() throws SQLException {
        String sql = "SELECT r.*, hb.block_name, hb.block_code FROM rooms r " +
                     "LEFT JOIN hostel_blocks hb ON r.block_id = hb.block_id " +
                     "WHERE r.is_active = true ORDER BY hb.block_name, r.room_number";
        
        return executeQuery(sql);
    }

    @Override
    public List<Room> findAvailable() throws SQLException {
        String sql = "SELECT r.*, hb.block_name, hb.block_code FROM rooms r " +
                     "LEFT JOIN hostel_blocks hb ON r.block_id = hb.block_id " +
                     "WHERE r.is_active = true AND r.current_occupancy < r.capacity " +
                     "ORDER BY hb.block_name, r.room_number";
        
        return executeQuery(sql);
    }

    @Override
    public List<Room> findAvailableByBlock(int blockId) throws SQLException {
        String sql = "SELECT r.*, hb.block_name, hb.block_code FROM rooms r " +
                     "LEFT JOIN hostel_blocks hb ON r.block_id = hb.block_id " +
                     "WHERE r.block_id = ? AND r.is_active = true AND r.current_occupancy < r.capacity " +
                     "ORDER BY r.room_number";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, blockId);
            
            resultSet = statement.executeQuery();
            
            List<Room> rooms = new ArrayList<>();
            while (resultSet.next()) {
                rooms.add(mapResultSetToRoom(resultSet));
            }
            
            return rooms;
            
        } catch (SQLException e) {
            logger.error("Error finding available rooms by block", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean roomExistsInBlock(String roomNumber, int blockId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM rooms WHERE room_number = ? AND block_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, roomNumber);
            statement.setInt(2, blockId);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error checking room existence in block", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean updateOccupancy(int roomId, int currentOccupancy) throws SQLException {
        String sql = "UPDATE rooms SET current_occupancy = ?, updated_at = ? WHERE room_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, currentOccupancy);
            statement.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now()));
            statement.setInt(3, roomId);
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error updating occupancy", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    @Override
    public boolean incrementOccupancy(int roomId) throws SQLException {
        String sql = "UPDATE rooms SET current_occupancy = current_occupancy + 1, updated_at = ? WHERE room_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setTimestamp(1, Timestamp.valueOf(java.time.LocalDateTime.now()));
            statement.setInt(2, roomId);
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error incrementing occupancy", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    @Override
    public boolean decrementOccupancy(int roomId) throws SQLException {
        String sql = "UPDATE rooms SET current_occupancy = GREATEST(0, current_occupancy - 1), updated_at = ? WHERE room_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setTimestamp(1, Timestamp.valueOf(java.time.LocalDateTime.now()));
            statement.setInt(2, roomId);
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error decrementing occupancy", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    private Room mapResultSetToRoom(ResultSet resultSet) throws SQLException {
        Room room = new Room();
        
        room.setRoomId(resultSet.getInt("room_id"));
        room.setRoomNumber(resultSet.getString("room_number"));
        room.setBlockId(resultSet.getInt("block_id"));
        room.setCapacity(resultSet.getInt("capacity"));
        room.setCurrentOccupancy(resultSet.getInt("current_occupancy"));
        
        int floorNumber = resultSet.getInt("floor_number");
        if (!resultSet.wasNull()) {
            room.setFloorNumber(floorNumber);
        }
        
        room.setRoomType(Room.RoomType.valueOf(resultSet.getString("room_type")));
        room.setActive(resultSet.getBoolean("is_active"));
        
        Timestamp createdAt = resultSet.getTimestamp("created_at");
        if (createdAt != null) room.setCreatedAt(createdAt.toLocalDateTime());
        
        Timestamp updatedAt = resultSet.getTimestamp("updated_at");
        if (updatedAt != null) room.setUpdatedAt(updatedAt.toLocalDateTime());
        
        // Display fields
        room.setBlockName(resultSet.getString("block_name"));
        room.setBlockCode(resultSet.getString("block_code"));
        
        return room;
    }

    private List<Room> executeQuery(String sql) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            
            resultSet = statement.executeQuery();
            
            List<Room> rooms = new ArrayList<>();
            while (resultSet.next()) {
                rooms.add(mapResultSetToRoom(resultSet));
            }
            
            return rooms;
            
        } catch (SQLException e) {
            logger.error("Error executing query", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    private void closeResources(ResultSet resultSet, PreparedStatement statement, Connection connection) {
        try {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) DatabaseConnection.releaseConnection(connection);
        } catch (SQLException e) {
            logger.error("Error closing resources", e);
        }
    }
}
