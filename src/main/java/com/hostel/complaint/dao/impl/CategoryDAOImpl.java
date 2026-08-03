package com.hostel.complaint.dao.impl;

import com.hostel.complaint.dao.CategoryDAO;
import com.hostel.complaint.database.DatabaseConnection;
import com.hostel.complaint.model.Category;
import com.hostel.complaint.model.Complaint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Category DAO implementation
 */
public class CategoryDAOImpl implements CategoryDAO {
    private static final Logger logger = LoggerFactory.getLogger(CategoryDAOImpl.class);

    @Override
    public Category save(Category category) throws SQLException {
        String sql = "INSERT INTO categories (category_name, description, default_priority, estimated_resolution_hours) " +
                     "VALUES (?, ?, ?, ?)";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            statement.setString(1, category.getCategoryName());
            statement.setString(2, category.getDescription());
            statement.setString(3, category.getDefaultPriority().name());
            statement.setInt(4, category.getEstimatedResolutionHours());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating category failed, no rows affected.");
            }
            
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                category.setCategoryId(resultSet.getInt(1));
            }
            
            connection.commit();
            logger.info("Category saved successfully: {}", category.getCategoryName());
            return category;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error saving category", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean update(Category category) throws SQLException {
        String sql = "UPDATE categories SET category_name = ?, description = ?, default_priority = ?, " +
                     "estimated_resolution_hours = ?, is_active = ?, updated_at = ? WHERE category_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            
            statement.setString(1, category.getCategoryName());
            statement.setString(2, category.getDescription());
            statement.setString(3, category.getDefaultPriority().name());
            statement.setInt(4, category.getEstimatedResolutionHours());
            statement.setBoolean(5, category.isActive());
            statement.setTimestamp(6, Timestamp.valueOf(java.time.LocalDateTime.now()));
            statement.setInt(7, category.getCategoryId());
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            
            logger.info("Category updated successfully: {}", category.getCategoryId());
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error updating category", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM categories WHERE category_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            
            int affectedRows = statement.executeUpdate();
            connection.commit();
            
            logger.info("Category deleted successfully: {}", id);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            logger.error("Error deleting category", e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    @Override
    public Optional<Category> findById(int id) throws SQLException {
        String sql = "SELECT * FROM categories WHERE category_id = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToCategory(resultSet));
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error finding category by ID", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<Category> findAll() throws SQLException {
        String sql = "SELECT * FROM categories ORDER BY category_name";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            
            resultSet = statement.executeQuery();
            
            List<Category> categories = new ArrayList<>();
            while (resultSet.next()) {
                categories.add(mapResultSetToCategory(resultSet));
            }
            
            return categories;
            
        } catch (SQLException e) {
            logger.error("Error finding all categories", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean exists(int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM categories WHERE category_id = ?";
        
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
            logger.error("Error checking category existence", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM categories";
        
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
            logger.error("Error counting categories", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public Optional<Category> findByName(String categoryName) throws SQLException {
        String sql = "SELECT * FROM categories WHERE category_name = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, categoryName);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapResultSetToCategory(resultSet));
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error finding category by name", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public List<Category> findActive() throws SQLException {
        String sql = "SELECT * FROM categories WHERE is_active = true ORDER BY category_name";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            
            resultSet = statement.executeQuery();
            
            List<Category> categories = new ArrayList<>();
            while (resultSet.next()) {
                categories.add(mapResultSetToCategory(resultSet));
            }
            
            return categories;
            
        } catch (SQLException e) {
            logger.error("Error finding active categories", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    @Override
    public boolean nameExists(String categoryName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM categories WHERE category_name = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, categoryName);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error checking category name existence", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    private Category mapResultSetToCategory(ResultSet resultSet) throws SQLException {
        Category category = new Category();
        
        category.setCategoryId(resultSet.getInt("category_id"));
        category.setCategoryName(resultSet.getString("category_name"));
        category.setDescription(resultSet.getString("description"));
        category.setDefaultPriority(Complaint.Priority.valueOf(resultSet.getString("default_priority")));
        category.setEstimatedResolutionHours(resultSet.getInt("estimated_resolution_hours"));
        category.setActive(resultSet.getBoolean("is_active"));
        
        Timestamp createdAt = resultSet.getTimestamp("created_at");
        if (createdAt != null) category.setCreatedAt(createdAt.toLocalDateTime());
        
        Timestamp updatedAt = resultSet.getTimestamp("updated_at");
        if (updatedAt != null) category.setUpdatedAt(updatedAt.toLocalDateTime());
        
        return category;
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
