package com.hostel.complaint.service;

import com.hostel.complaint.dao.StudentDAO;
import com.hostel.complaint.dao.RoomDAO;
import com.hostel.complaint.dao.HostelBlockDAO;
import com.hostel.complaint.model.Student;
import com.hostel.complaint.model.Room;
import com.hostel.complaint.model.HostelBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Student Service
 * Handles student-related business logic
 */
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    
    private final StudentDAO studentDAO;
    private final RoomDAO roomDAO;
    private final HostelBlockDAO blockDAO;
    
    public StudentService(StudentDAO studentDAO, RoomDAO roomDAO, HostelBlockDAO blockDAO) {
        this.studentDAO = studentDAO;
        this.roomDAO = roomDAO;
        this.blockDAO = blockDAO;
    }
    
    /**
     * Register a new student
     * @param student Student to register
     * @return Registered student with generated ID
     * @throws SQLException if database error occurs
     * @throws ServiceException if business logic validation fails
     */
    public Student registerStudent(Student student) throws SQLException, ServiceException {
        // Validate student data
        validateStudent(student);
        
        // Check if student number already exists
        if (studentDAO.studentNumberExists(student.getStudentNumber())) {
            throw new ServiceException("Student number already exists");
        }
        
        // Check if username already exists
        if (studentDAO.usernameExists(student.getUsername())) {
            throw new ServiceException("Username already exists");
        }
        
        // Check if email already exists
        if (studentDAO.emailExists(student.getEmail())) {
            throw new ServiceException("Email already exists");
        }
        
        // Validate room assignment if provided
        if (student.getBlockId() != null && student.getRoomId() != null) {
            validateRoomAssignment(student.getBlockId(), student.getRoomId());
        }
        
        // Save student
        Student savedStudent = studentDAO.save(student);
        
        // Update room occupancy if assigned
        if (student.getRoomId() != null) {
            roomDAO.incrementOccupancy(student.getRoomId());
        }
        
        logger.info("Student registered successfully: {}", student.getStudentNumber());
        return savedStudent;
    }
    
    /**
     * Update student profile
     * @param student Student to update
     * @return true if update successful
     * @throws SQLException if database error occurs
     * @throws ServiceException if business logic validation fails
     */
    public boolean updateStudent(Student student) throws SQLException, ServiceException {
        // Validate student data
        validateStudent(student);
        
        // Check if student exists
        if (!studentDAO.exists(student.getStudentId())) {
            throw new ServiceException("Student not found");
        }
        
        // Check if student number is taken by another student
        Optional<Student> existingByNumber = studentDAO.findByStudentNumber(student.getStudentNumber());
        if (existingByNumber.isPresent() && existingByNumber.get().getStudentId() != student.getStudentId()) {
            throw new ServiceException("Student number already exists");
        }
        
        // Check if username is taken by another student
        Optional<Student> existingByUsername = studentDAO.findByUsername(student.getUsername());
        if (existingByUsername.isPresent() && existingByUsername.get().getStudentId() != student.getStudentId()) {
            throw new ServiceException("Username already exists");
        }
        
        // Check if email is taken by another student
        Optional<Student> existingByEmail = studentDAO.findByEmail(student.getEmail());
        if (existingByEmail.isPresent() && existingByEmail.get().getStudentId() != student.getStudentId()) {
            throw new ServiceException("Email already exists");
        }
        
        // Handle room assignment changes
        Student currentStudent = studentDAO.findById(student.getStudentId()).orElse(null);
        if (currentStudent != null) {
            // Decrement old room occupancy if changing rooms
            if (currentStudent.getRoomId() != null && !currentStudent.getRoomId().equals(student.getRoomId())) {
                roomDAO.decrementOccupancy(currentStudent.getRoomId());
            }
            
            // Increment new room occupancy if assigning to new room
            if (student.getRoomId() != null && !student.getRoomId().equals(currentStudent.getRoomId())) {
                validateRoomAssignment(student.getBlockId(), student.getRoomId());
                roomDAO.incrementOccupancy(student.getRoomId());
            }
        }
        
        return studentDAO.update(student);
    }
    
    /**
     * Get student by ID
     * @param studentId Student ID
     * @return Student if found
     * @throws SQLException if database error occurs
     * @throws ServiceException if student not found
     */
    public Student getStudent(int studentId) throws SQLException, ServiceException {
        Optional<Student> studentOpt = studentDAO.findById(studentId);
        
        if (!studentOpt.isPresent()) {
            throw new ServiceException("Student not found");
        }
        
        return studentOpt.get();
    }
    
    /**
     * Get student by username
     * @param username Username
     * @return Student if found
     * @throws SQLException if database error occurs
     * @throws ServiceException if student not found
     */
    public Student getStudentByUsername(String username) throws SQLException, ServiceException {
        Optional<Student> studentOpt = studentDAO.findByUsername(username);
        
        if (!studentOpt.isPresent()) {
            throw new ServiceException("Student not found");
        }
        
        return studentOpt.get();
    }
    
    /**
     * Change student password
     * @param studentId Student ID
     * @param oldPassword Old password (plain text)
     * @param newPassword New password (plain text)
     * @param authService Authentication service for password verification
     * @return true if password changed successfully
     * @throws SQLException if database error occurs
     * @throws ServiceException if validation fails
     */
    public boolean changePassword(int studentId, String oldPassword, String newPassword, 
                                  AuthenticationService authService) throws SQLException, ServiceException {
        // Get student
        Student student = getStudent(studentId);
        
        // Verify old password
        if (!authService.verifyPassword(oldPassword, student.getPassword())) {
            throw new ServiceException("Current password is incorrect");
        }
        
        // Validate new password
        if (newPassword == null || newPassword.length() < 6) {
            throw new ServiceException("New password must be at least 6 characters");
        }
        
        // Hash new password
        String hashedPassword = authService.hashPassword(newPassword);
        
        // Update password
        boolean success = studentDAO.updatePassword(studentId, hashedPassword);
        
        if (success) {
            logger.info("Password changed for student: {}", studentId);
        }
        
        return success;
    }
    
    /**
     * Assign student to room
     * @param studentId Student ID
     * @param blockId Block ID
     * @param roomId Room ID
     * @return true if assignment successful
     * @throws SQLException if database error occurs
     * @throws ServiceException if validation fails
     */
    public boolean assignToRoom(int studentId, int blockId, int roomId) throws SQLException, ServiceException {
        // Validate room
        validateRoomAssignment(blockId, roomId);
        
        // Get current student
        Student currentStudent = getStudent(studentId);
        
        // Decrement old room occupancy if changing rooms
        if (currentStudent.getRoomId() != null && currentStudent.getRoomId() != roomId) {
            roomDAO.decrementOccupancy(currentStudent.getRoomId());
        }
        
        // Assign to new room
        boolean success = studentDAO.assignToRoom(studentId, blockId, roomId);
        
        if (success) {
            // Increment new room occupancy
            roomDAO.incrementOccupancy(roomId);
            logger.info("Student assigned to room: studentId={}, roomId={}", studentId, roomId);
        }
        
        return success;
    }
    
    /**
     * Get all students
     * @return List of all students
     * @throws SQLException if database error occurs
     */
    public List<Student> getAllStudents() throws SQLException {
        return studentDAO.findAll();
    }
    
    /**
     * Search students by name
     * @param name Name to search
     * @return List of matching students
     * @throws SQLException if database error occurs
     */
    public List<Student> searchStudents(String name) throws SQLException {
        return studentDAO.searchByName(name);
    }
    
    /**
     * Get students by block
     * @param blockId Block ID
     * @return List of students in the block
     * @throws SQLException if database error occurs
     */
    public List<Student> getStudentsByBlock(int blockId) throws SQLException {
        return studentDAO.findByBlockId(blockId);
    }
    
    /**
     * Get students by room
     * @param roomId Room ID
     * @return List of students in the room
     * @throws SQLException if database error occurs
     */
    public List<Student> getStudentsByRoom(int roomId) throws SQLException {
        return studentDAO.findByRoomId(roomId);
    }
    
    /**
     * Validate student data
     * @param student Student to validate
     * @throws ServiceException if validation fails
     */
    private void validateStudent(Student student) throws ServiceException {
        if (student.getStudentNumber() == null || student.getStudentNumber().trim().isEmpty()) {
            throw new ServiceException("Student number is required");
        }
        
        if (student.getUsername() == null || student.getUsername().trim().isEmpty()) {
            throw new ServiceException("Username is required");
        }
        
        if (student.getPassword() == null || student.getPassword().trim().isEmpty()) {
            throw new ServiceException("Password is required");
        }
        
        if (student.getFullName() == null || student.getFullName().trim().isEmpty()) {
            throw new ServiceException("Full name is required");
        }
        
        if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
            throw new ServiceException("Email is required");
        }
        
        // Basic email validation
        if (!student.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ServiceException("Invalid email format");
        }
        
        if (student.getYearOfStudy() != null && (student.getYearOfStudy() < 1 || student.getYearOfStudy() > 5)) {
            throw new ServiceException("Year of study must be between 1 and 5");
        }
    }
    
    /**
     * Validate room assignment
     * @param blockId Block ID
     * @param roomId Room ID
     * @throws SQLException if database error occurs
     * @throws ServiceException if validation fails
     */
    private void validateRoomAssignment(int blockId, int roomId) throws SQLException, ServiceException {
        // Check if block exists
        Optional<HostelBlock> blockOpt = blockDAO.findById(blockId);
        if (!blockOpt.isPresent()) {
            throw new ServiceException("Block not found");
        }
        
        // Check if room exists
        Optional<Room> roomOpt = roomDAO.findById(roomId);
        if (!roomOpt.isPresent()) {
            throw new ServiceException("Room not found");
        }
        
        Room room = roomOpt.get();
        
        // Check if room belongs to block
        if (room.getBlockId() != blockId) {
            throw new ServiceException("Room does not belong to the specified block");
        }
        
        // Check if room has availability
        if (!room.hasAvailability()) {
            throw new ServiceException("Room is at full capacity");
        }
        
        // Check if room is active
        if (!room.isActive()) {
            throw new ServiceException("Room is not active");
        }
    }
    
    public static class ServiceException extends Exception {
        public ServiceException(String message) {
            super(message);
        }
    }
}
