package com.hostel.complaint.dao;

import com.hostel.complaint.model.Student;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Student DAO interface
 * Defines operations for student management
 */
public interface StudentDAO extends BaseDAO<Student> {
    
    /**
     * Find a student by student number
     * @param studentNumber The student number
     * @return Optional containing the student if found
     * @throws SQLException if database error occurs
     */
    Optional<Student> findByStudentNumber(String studentNumber) throws SQLException;
    
    /**
     * Find a student by username
     * @param username The username
     * @return Optional containing the student if found
     * @throws SQLException if database error occurs
     */
    Optional<Student> findByUsername(String username) throws SQLException;
    
    /**
     * Find a student by email
     * @param email The email address
     * @return Optional containing the student if found
     * @throws SQLException if database error occurs
     */
    Optional<Student> findByEmail(String email) throws SQLException;
    
    /**
     * Find students by block
     * @param blockId The block ID
     * @return List of students in the block
     * @throws SQLException if database error occurs
     */
    List<Student> findByBlockId(int blockId) throws SQLException;
    
    /**
     * Find students by room
     * @param roomId The room ID
     * @return List of students in the room
     * @throws SQLException if database error occurs
     */
    List<Student> findByRoomId(int roomId) throws SQLException;
    
    /**
     * Find students by course
     * @param course The course name
     * @return List of students in the course
     * @throws SQLException if database error occurs
     */
    List<Student> findByCourse(String course) throws SQLException;
    
    /**
     * Find students by year of study
     * @param year The year of study
     * @return List of students in the year
     * @throws SQLException if database error occurs
     */
    List<Student> findByYearOfStudy(int year) throws SQLException;
    
    /**
     * Search students by name
     * @param name The name to search
     * @return List of matching students
     * @throws SQLException if database error occurs
     */
    List<Student> searchByName(String name) throws SQLException;
    
    /**
     * Check if student number exists
     * @param studentNumber The student number
     * @return true if student number exists
     * @throws SQLException if database error occurs
     */
    boolean studentNumberExists(String studentNumber) throws SQLException;
    
    /**
     * Check if username exists
     * @param username The username
     * @return true if username exists
     * @throws SQLException if database error occurs
     */
    boolean usernameExists(String username) throws SQLException;
    
    /**
     * Check if email exists
     * @param email The email address
     * @return true if email exists
     * @throws SQLException if database error occurs
     */
    boolean emailExists(String email) throws SQLException;
    
    /**
     * Update last login timestamp
     * @param studentId The student ID
     * @return true if update was successful
     * @throws SQLException if database error occurs
     */
    boolean updateLastLogin(int studentId) throws SQLException;
    
    /**
     * Update student password
     * @param studentId The student ID
     * @param newPassword The new hashed password
     * @return true if update was successful
     * @throws SQLException if database error occurs
     */
    boolean updatePassword(int studentId, String newPassword) throws SQLException;
    
    /**
     * Assign student to room
     * @param studentId The student ID
     * @param blockId The block ID
     * @param roomId The room ID
     * @return true if assignment was successful
     * @throws SQLException if database error occurs
     */
    boolean assignToRoom(int studentId, int blockId, int roomId) throws SQLException;
}
