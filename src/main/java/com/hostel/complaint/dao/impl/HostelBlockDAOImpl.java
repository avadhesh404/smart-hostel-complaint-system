package com.hostel.complaint.dao.impl;

import com.hostel.complaint.dao.HostelBlockDAO;
import com.hostel.complaint.database.DatabaseConnection;
import com.hostel.complaint.model.HostelBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Hostel Block DAO implementation
 */
public class HostelBlockDAOImpl implements HostelBlockDAO {
    private static final Logger logger = LoggerFactory.getLogger(HostelBlockDAOImpl.class);

    @Override
    public HostelBlock save(HostelBlock block) throws SQLException {
        String sql = "INSERT INTO hostel_blocks (block_name, block_code, description, total_rooms, warden_name, warden_phone) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            statement.setString(1, block.getBlockName());
            statement.setString(2, block.getBlockCode());
            statement.setString(3, block.getDescription());
            statement.setInt(4, block.getTotalRooms());
            statement.setString(5, block.getWardenName());
            statement.setString(6, block.getWardenPhone());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating block failed, no rows affected.");
            }
            
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                block.setBlockId(resultSet.getInt(1));
            }
            
            connection.commit();
            logger.info("Hostel block saved successfully: {}", block.getBlockCode());
            return block;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error saving hostel block", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean update(HostelBlock block) throws SQLException {
        String sql = "UPDATE hostel_blocks SET block_name = ?, block_code = ?, description = ?, " +
                     "total_rooms = ?, warden_name = ?, warden_phone = ?, is_active = ?, updated_at = ? " +
                     "WHERE block_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            
            statement.setString(1, block.getBlockName());
            statement.setString(2, block.getBlockCode());
            statement.setString(3, block.getDescription());
            statement.setInt(4, block.getTotalRooms());
            statement.setString(5, block.getWardenName());
            statement.setString(6, block.getWardenPhone());
            statement.setBoolean(7, block.isActive());
            statement.setTimestamp(8, Timestamp.valueOf(java.time.LocalDateTime.now()));
            statement.setInt(9, block.getBlockId());
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            
            logger.info("Hostel block updated successfully: {}", block.getBlockId());
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error updating hostel block", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM hostel_blocks WHERE block_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            
            logger.info("Hostel block deleted successfully: {}", id);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error deleting hostel block", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    @Override
    public Optional<HostelBlock> findById(int id) throws SQLException {
        String sql = "SELECT * FROM hostel_blocks WHERE block_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToBlock(resultSet));
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error finding hostel block by ID", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<HostelBlock> findAll() throws SQLException {
        String sql = "SELECT * FROM hostel_blocks ORDER BY block_name";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            
            resultSet = statement.executeQuery();
            
            List<HostelBlock> blocks = new ArrayList<>();
            while (resultSet.next()) {
                blocks.add(mapResultSetToBlock(resultSet));
            }
            
            return blocks;
            
        } catch (SQLException e) {
            logger.error("Error finding all hostel blocks", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean exists(int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM hostel_blocks WHERE block_id = ?";
        
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
            logger.error("Error checking hostel block existence", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM hostel_blocks";
        
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
            logger.error("Error counting hostel blocks", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public Optional<HostelBlock> findByBlockCode(String blockCode) throws SQLException {
        String sql = "SELECT * FROM hostel_blocks WHERE block_code = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, blockCode);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToBlock(resultSet));
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error finding hostel block by code", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<HostelBlock> findActive() throws SQLException {
        String sql = "SELECT * FROM hostel_blocks WHERE is_active = true ORDER BY block_name";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            
            resultSet = statement.executeQuery();
            
            List<HostelBlock> blocks = new ArrayList<>();
            while (resultSet.next()) {
                blocks.add(mapResultSetToBlock(resultSet));
            }
            
            return blocks;
            
        } catch (SQLException e) {
            logger.error("Error finding active hostel blocks", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean blockCodeExists(String blockCode) throws SQLException {
        String sql = "SELECT COUNT(*) FROM hostel_blocks WHERE block_code = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, blockCode);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error checking block code existence", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean updateRoomCount(int blockId, int totalRooms) throws SQLException {
        String sql = "UPDATE hostel_blocks SET total_rooms = ?, updated_at = ? WHERE block_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, totalRooms);
            statement.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now()));
            statement.setInt(3, blockId);
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error updating room count", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    private HostelBlock mapResultSetToBlock(ResultSet resultSet) throws SQLException {
        HostelBlock block = new HostelBlock();
        
        block.setBlockId(resultSet.getInt("block_id"));
        block.setBlockName(resultSet.getString("block_name"));
        block.setBlockCode(resultSet.getString("block_code"));
        block.setDescription(resultSet.getString("description"));
        block.setTotalRooms(resultSet.getInt("total_rooms"));
        block.setWardenName(resultSet.getString("warden_name"));
        block.setWardenPhone(resultSet.getString("warden_phone"));
        block.setActive(resultSet.getBoolean("is_active"));
        
        Timestamp createdAt = resultSet.getTimestamp("created_at");
        if (createdAt != null) block.setCreatedAt(createdAt.toLocalDateTime());
        
        Timestamp updatedAt = resultSet.getTimestamp("updated_at");
        if (updatedAt != null) block.setUpdatedAt(updatedAt.toLocalDateTime());
        
        return block;
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
