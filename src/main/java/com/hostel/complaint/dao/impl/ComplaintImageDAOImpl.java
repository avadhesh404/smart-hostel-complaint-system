package com.hostel.complaint.dao.impl;

import com.hostel.complaint.dao.ComplaintImageDAO;
import com.hostel.complaint.database.DatabaseConnection;
import com.hostel.complaint.model.ComplaintImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Complaint Image DAO implementation
 */
public class ComplaintImageDAOImpl implements ComplaintImageDAO {
    private static final Logger logger = LoggerFactory.getLogger(ComplaintImageDAOImpl.class);

    @Override
    public ComplaintImage save(ComplaintImage image) throws SQLException {
        String sql = "INSERT INTO complaint_images (complaint_id, image_path, image_name) VALUES (?, ?, ?)";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            statement.setInt(1, image.getComplaintId());
            statement.setString(2, image.getImagePath());
            statement.setString(3, image.getImageName());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating complaint image failed, no rows affected.");
            }
            
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                image.setImageId(resultSet.getInt(1));
            }
            
            connection.commit();
            return image;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error saving complaint image", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean update(ComplaintImage image) throws SQLException {
        // Images are immutable - no update needed
        return false;
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM complaint_images WHERE image_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error deleting complaint image", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    @Override
    public Optional<ComplaintImage> findById(int id) throws SQLException {
        String sql = "SELECT * FROM complaint_images WHERE image_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToImage(resultSet));
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error finding complaint image by ID", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<ComplaintImage> findAll() throws SQLException {
        String sql = "SELECT * FROM complaint_images ORDER BY uploaded_at DESC";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            
            resultSet = statement.executeQuery();
            
            List<ComplaintImage> images = new ArrayList<>();
            while (resultSet.next()) {
                images.add(mapResultSetToImage(resultSet));
            }
            
            return images;
            
        } catch (SQLException e) {
            logger.error("Error finding all complaint images", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean exists(int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM complaint_images WHERE image_id = ?";
        
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
            logger.error("Error checking complaint image existence", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM complaint_images";
        
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
            logger.error("Error counting complaint images", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<ComplaintImage> findByComplaintId(int complaintId) throws SQLException {
        String sql = "SELECT * FROM complaint_images WHERE complaint_id = ? ORDER BY uploaded_at DESC";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, complaintId);
            
            resultSet = statement.executeQuery();
            
            List<ComplaintImage> images = new ArrayList<>();
            while (resultSet.next()) {
                images.add(mapResultSetToImage(resultSet));
            }
            
            return images;
            
        } catch (SQLException e) {
            logger.error("Error finding complaint images by complaint ID", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean deleteByComplaintId(int complaintId) throws SQLException {
        String sql = "DELETE FROM complaint_images WHERE complaint_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, complaintId);
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error deleting complaint images by complaint ID", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    private ComplaintImage mapResultSetToImage(ResultSet resultSet) throws SQLException {
        ComplaintImage image = new ComplaintImage();
        
        image.setImageId(resultSet.getInt("image_id"));
        image.setComplaintId(resultSet.getInt("complaint_id"));
        image.setImagePath(resultSet.getString("image_path"));
        image.setImageName(resultSet.getString("image_name"));
        
        Timestamp uploadedAt = resultSet.getTimestamp("uploaded_at");
        if (uploadedAt != null) {
            image.setUploadedAt(uploadedAt.toLocalDateTime());
        }
        
        return image;
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
