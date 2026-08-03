package com.hostel.complaint.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Base DAO interface
 * Defines common CRUD operations for all entities
 * @param <T> The entity type
 */
public interface BaseDAO<T> {
    
    /**
     * Save a new entity
     * @param entity The entity to save
     * @return The saved entity with generated ID
     * @throws SQLException if database error occurs
     */
    T save(T entity) throws SQLException;
    
    /**
     * Update an existing entity
     * @param entity The entity to update
     * @return true if update was successful
     * @throws SQLException if database error occurs
     */
    boolean update(T entity) throws SQLException;
    
    /**
     * Delete an entity by ID
     * @param id The entity ID
     * @return true if deletion was successful
     * @throws SQLException if database error occurs
     */
    boolean delete(int id) throws SQLException;
    
    /**
     * Find an entity by ID
     * @param id The entity ID
     * @return Optional containing the entity if found
     * @throws SQLException if database error occurs
     */
    Optional<T> findById(int id) throws SQLException;
    
    /**
     * Find all entities
     * @return List of all entities
     * @throws SQLException if database error occurs
     */
    List<T> findAll() throws SQLException;
    
    /**
     * Check if an entity exists by ID
     * @param id The entity ID
     * @return true if entity exists
     * @throws SQLException if database error occurs
     */
    boolean exists(int id) throws SQLException;
    
    /**
     * Count total number of entities
     * @return Total count
     * @throws SQLException if database error occurs
     */
    int count() throws SQLException;
}
