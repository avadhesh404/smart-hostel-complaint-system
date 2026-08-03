package com.hostel.complaint.service;

import com.hostel.complaint.dao.AdminDAO;
import com.hostel.complaint.dao.StudentDAO;
import com.hostel.complaint.dao.HostelStaffDAO;
import com.hostel.complaint.model.Admin;
import com.hostel.complaint.model.Student;
import com.hostel.complaint.model.HostelStaff;
import at.favre.lib.crypto.bcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Authentication Service
 * Handles user authentication and session management
 */
public class AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    
    private final StudentDAO studentDAO;
    private final HostelStaffDAO staffDAO;
    private final AdminDAO adminDAO;
    
    // Session management
    private static UserSession currentUserSession;
    
    public enum UserRole {
        STUDENT, STAFF, ADMIN
    }
    
    public static class UserSession {
        private final int userId;
        private final String username;
        private final String fullName;
        private final UserRole role;
        private final long loginTime;
        
        public UserSession(int userId, String username, String fullName, UserRole role) {
            this.userId = userId;
            this.username = username;
            this.fullName = fullName;
            this.role = role;
            this.loginTime = System.currentTimeMillis();
        }
        
        public int getUserId() { return userId; }
        public String getUsername() { return username; }
        public String getFullName() { return fullName; }
        public UserRole getRole() { return role; }
        public long getLoginTime() { return loginTime; }
        
        public boolean isSessionValid(long timeoutMillis) {
            return (System.currentTimeMillis() - loginTime) < timeoutMillis;
        }
    }
    
    public AuthenticationService(StudentDAO studentDAO, HostelStaffDAO staffDAO, AdminDAO adminDAO) {
        this.studentDAO = studentDAO;
        this.staffDAO = staffDAO;
        this.adminDAO = adminDAO;
    }
    
    /**
     * Authenticate a student
     * @param username Username
     * @param password Plain text password
     * @return UserSession if authentication successful
     * @throws SQLException if database error occurs
     * @throws AuthenticationException if authentication fails
     */
    public UserSession authenticateStudent(String username, String password) throws SQLException, AuthenticationException {
        Optional<Student> studentOpt = studentDAO.findByUsername(username);
        
        if (!studentOpt.isPresent()) {
            throw new AuthenticationException("Invalid username or password");
        }
        
        Student student = studentOpt.get();
        
        if (!student.isActive()) {
            throw new AuthenticationException("Account is inactive");
        }
        
        if (!verifyPassword(password, student.getPassword())) {
            throw new AuthenticationException("Invalid username or password");
        }
        
        // Update last login
        studentDAO.updateLastLogin(student.getStudentId());
        
        UserSession session = new UserSession(student.getStudentId(), student.getUsername(), 
                                            student.getFullName(), UserRole.STUDENT);
        currentUserSession = session;
        
        logger.info("Student authenticated successfully: {}", username);
        return session;
    }
    
    /**
     * Authenticate hostel staff
     * @param username Username
     * @param password Plain text password
     * @return UserSession if authentication successful
     * @throws SQLException if database error occurs
     * @throws AuthenticationException if authentication fails
     */
    public UserSession authenticateStaff(String username, String password) throws SQLException, AuthenticationException {
        Optional<HostelStaff> staffOpt = staffDAO.findByUsername(username);
        
        if (!staffOpt.isPresent()) {
            throw new AuthenticationException("Invalid username or password");
        }
        
        HostelStaff staff = staffOpt.get();
        
        if (!staff.isActive()) {
            throw new AuthenticationException("Account is inactive");
        }
        
        if (!verifyPassword(password, staff.getPassword())) {
            throw new AuthenticationException("Invalid username or password");
        }
        
        // Update last login
        staffDAO.updateLastLogin(staff.getStaffId());
        
        UserSession session = new UserSession(staff.getStaffId(), staff.getUsername(), 
                                            staff.getFullName(), UserRole.STAFF);
        currentUserSession = session;
        
        logger.info("Staff authenticated successfully: {}", username);
        return session;
    }
    
    /**
     * Authenticate admin
     * @param username Username
     * @param password Plain text password
     * @return UserSession if authentication successful
     * @throws SQLException if database error occurs
     * @throws AuthenticationException if authentication fails
     */
    public UserSession authenticateAdmin(String username, String password) throws SQLException, AuthenticationException {
        Optional<Admin> adminOpt = adminDAO.findByUsername(username);
        
        if (!adminOpt.isPresent()) {
            throw new AuthenticationException("Invalid username or password");
        }
        
        Admin admin = adminOpt.get();
        
        if (!admin.isActive()) {
            throw new AuthenticationException("Account is inactive");
        }
        
        if (!verifyPassword(password, admin.getPassword())) {
            throw new AuthenticationException("Invalid username or password");
        }
        
        // Update last login
        adminDAO.updateLastLogin(admin.getAdminId());
        
        UserSession session = new UserSession(admin.getAdminId(), admin.getUsername(), 
                                            admin.getFullName(), UserRole.ADMIN);
        currentUserSession = session;
        
        logger.info("Admin authenticated successfully: {}", username);
        return session;
    }
    
    /**
     * Hash a password using BCrypt
     * @param plainPassword Plain text password
     * @return Hashed password
     */
    public String hashPassword(String plainPassword) {
        return BCrypt.withDefaults().hashToString(12, plainPassword.toCharArray());
    }
    
    /**
     * Verify a password against a hash
     * @param plainPassword Plain text password
     * @param hashedPassword Hashed password
     * @return true if password matches
     */
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        try {
            BCrypt.Result result = BCrypt.verifyer().verify(plainPassword.toCharArray(), hashedPassword);
            return result.verified;
        } catch (Exception e) {
            logger.error("Error verifying password", e);
            return false;
        }
    }
    
    /**
     * Get current user session
     * @return Current UserSession or null if not logged in
     */
    public static UserSession getCurrentSession() {
        return currentUserSession;
    }
    
    /**
     * Check if user is logged in
     * @return true if user is logged in
     */
    public static boolean isLoggedIn() {
        return currentUserSession != null;
    }
    
    /**
     * Check if current user has specific role
     * @param role Role to check
     * @return true if current user has the role
     */
    public static boolean hasRole(UserRole role) {
        return currentUserSession != null && currentUserSession.getRole() == role;
    }
    
    /**
     * Logout current user
     */
    public static void logout() {
        if (currentUserSession != null) {
            logger.info("User logged out: {}", currentUserSession.getUsername());
            currentUserSession = null;
        }
    }
    
    /**
     * Check if session is valid
     * @param timeoutMillis Session timeout in milliseconds
     * @return true if session is valid
     */
    public static boolean isSessionValid(long timeoutMillis) {
        return currentUserSession != null && currentUserSession.isSessionValid(timeoutMillis);
    }
    
    public static class AuthenticationException extends Exception {
        public AuthenticationException(String message) {
            super(message);
        }
    }
}
