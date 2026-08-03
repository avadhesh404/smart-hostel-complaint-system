package com.hostel.complaint.dao;

import com.hostel.complaint.model.Category;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Category DAO interface
 * Defines operations for category management
 */
public interface CategoryDAO extends BaseDAO<Category> {
    
    /**
     * Find category by name
     * @param categoryName The category name
     * @return Optional containing the category if found
     * @throws SQLException if database error occurs
     */
    Optional<Category> findByName(String categoryName) throws SQLException;
    
    /**
     * Find all active categories
     * @return List of active categories
     * @throws SQLException if database error occurs
     */
    List<Category> findActive() throws SQLException;
    
    /**
     * Check if category name exists
     * @param categoryName The category name
     * @return true if category name exists
     * @throws SQLException if database error occurs
     */
    boolean nameExists(String categoryName) throws SQLException;
}
