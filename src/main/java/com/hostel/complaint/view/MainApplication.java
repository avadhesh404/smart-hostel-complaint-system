package com.hostel.complaint.view;

import com.hostel.complaint.dao.*;
import com.hostel.complaint.dao.impl.*;
import com.hostel.complaint.database.DatabaseConnection;
import com.hostel.complaint.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Main Application Entry Point
 * Initializes all components and manages application lifecycle
 */
public class MainApplication {
    private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);
    
    // DAO instances
    private final StudentDAO studentDAO;
    private final HostelStaffDAO staffDAO;
    private final AdminDAO adminDAO;
    private final ComplaintDAO complaintDAO;
    private final ComplaintStatusHistoryDAO statusHistoryDAO;
    private final ComplaintImageDAO imageDAO;
    private final CategoryDAO categoryDAO;
    private final HostelBlockDAO blockDAO;
    private final RoomDAO roomDAO;
    private final NotificationDAO notificationDAO;
    private final FeedbackDAO feedbackDAO;
    private final AuditLogDAO auditLogDAO;
    
    // Service instances
    private final AuthenticationService authService;
    private final ComplaintService complaintService;
    private final StudentService studentService;
    private final StaffService staffService;
    private final AdminService adminService;
    
    // UI components
    private LoginView loginView;
    private StudentDashboard studentDashboard;
    
    public MainApplication() {
        // Initialize DAOs
        this.studentDAO = new StudentDAOImpl();
        this.staffDAO = new HostelStaffDAOImpl();
        this.adminDAO = new AdminDAOImpl();
        this.statusHistoryDAO = new ComplaintStatusHistoryDAOImpl();
        this.imageDAO = new ComplaintImageDAOImpl();
        this.categoryDAO = new CategoryDAOImpl();
        this.blockDAO = new HostelBlockDAOImpl();
        this.roomDAO = new RoomDAOImpl();
        this.notificationDAO = new NotificationDAOImpl();
        this.feedbackDAO = new FeedbackDAOImpl();
        this.auditLogDAO = new AuditLogDAOImpl();
        
        // Initialize ComplaintDAO with dependencies
        this.complaintDAO = new ComplaintDAOImpl(statusHistoryDAO);
        
        // Initialize Services
        this.authService = new AuthenticationService(studentDAO, staffDAO, adminDAO);
        this.complaintService = new ComplaintService(complaintDAO, statusHistoryDAO, imageDAO, 
                                                   categoryDAO, studentDAO, staffDAO, 
                                                   notificationDAO, feedbackDAO);
        this.studentService = new StudentService(studentDAO, roomDAO, blockDAO);
        this.staffService = new StaffService(staffDAO, blockDAO);
        this.adminService = new AdminService(adminDAO, studentDAO, staffDAO, categoryDAO, 
                                            blockDAO, roomDAO, complaintDAO, feedbackDAO, auditLogDAO);
        
        // Initialize database connection pool
        DatabaseConnection.initialize();
        
        logger.info("Application initialized successfully");
    }
    
    public void start() {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                logger.error("Error setting look and feel", e);
            }
            
            // Show login view
            showLoginView();
        });
    }
    
    private void showLoginView() {
        loginView = new LoginView();
        loginView.setLoginListener(new LoginView.LoginListener() {
            @Override
            public void onLoginAttempt(String username, String password, String userType) {
                handleLogin(username, password, userType);
            }
            
            @Override
            public void onForgotPassword() {
                JOptionPane.showMessageDialog(loginView, 
                    "Please contact administrator to reset your password", 
                    "Forgot Password", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        loginView.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                shutdown();
            }
        });
        
        loginView.setVisible(true);
    }
    
    private void handleLogin(String username, String password, String userType) {
        try {
            AuthenticationService.UserSession session = null;
            
            switch (userType) {
                case "Student":
                    session = authService.authenticateStudent(username, password);
                    break;
                case "Hostel Staff":
                    session = authService.authenticateStaff(username, password);
                    break;
                case "Admin":
                    session = authService.authenticateAdmin(username, password);
                    break;
            }
            
            if (session != null) {
                loginView.dispose();
                showDashboard(session);
            }
            
        } catch (AuthenticationService.AuthenticationException e) {
            loginView.showError(e.getMessage());
        } catch (Exception e) {
            logger.error("Login error", e);
            loginView.showError("An error occurred during login. Please try again.");
        }
    }
    
    private void showDashboard(AuthenticationService.UserSession session) {
        switch (session.getRole()) {
            case STUDENT:
                showStudentDashboard(session);
                break;
            case STAFF:
                showStaffDashboard(session);
                break;
            case ADMIN:
                showAdminDashboard(session);
                break;
        }
    }
    
    private void showStudentDashboard(AuthenticationService.UserSession session) {
        try {
            // Get student details
            var student = studentService.getStudent(session.getUserId());
            
            studentDashboard = new StudentDashboard(session.getFullName(), student.getStudentNumber());
            studentDashboard.setListener(new StudentDashboard.StudentDashboardListener() {
                @Override
                public void onSubmitComplaint() {
                    JOptionPane.showMessageDialog(studentDashboard, "Submit Complaint feature coming soon");
                }
                
                @Override
                public void onViewComplaints() {
                    JOptionPane.showMessageDialog(studentDashboard, "View Complaints feature coming soon");
                }
                
                @Override
                public void onTrackComplaint() {
                    JOptionPane.showMessageDialog(studentDashboard, "Track Complaint feature coming soon");
                }
                
                @Override
                public void onEditProfile() {
                    JOptionPane.showMessageDialog(studentDashboard, "Edit Profile feature coming soon");
                }
                
                @Override
                public void onViewNotifications() {
                    JOptionPane.showMessageDialog(studentDashboard, "Notifications feature coming soon");
                }
                
                @Override
                public void onLogout() {
                    AuthenticationService.logout();
                    studentDashboard.dispose();
                    showLoginView();
                }
            });
            
            studentDashboard.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    shutdown();
                }
            });
            
            studentDashboard.setVisible(true);
            
        } catch (Exception e) {
            logger.error("Error loading student dashboard", e);
            JOptionPane.showMessageDialog(null, "Error loading dashboard", "Error", JOptionPane.ERROR_MESSAGE);
            showLoginView();
        }
    }
    
    private void showStaffDashboard(AuthenticationService.UserSession session) {
        JOptionPane.showMessageDialog(null, "Staff Dashboard coming soon");
        showLoginView();
    }
    
    private void showAdminDashboard(AuthenticationService.UserSession session) {
        JOptionPane.showMessageDialog(null, "Admin Dashboard coming soon");
        showLoginView();
    }
    
    private void shutdown() {
        logger.info("Shutting down application");
        
        // Close database connections
        DatabaseConnection.closeAll();
        
        // Exit application
        System.exit(0);
    }
    
    public static void main(String[] args) {
        logger.info("Starting Smart Hostel Complaint Management System");
        
        try {
            MainApplication app = new MainApplication();
            app.start();
        } catch (Exception e) {
            logger.error("Failed to start application", e);
            JOptionPane.showMessageDialog(null, 
                "Failed to start application. Please check the logs.", 
                "Startup Error", 
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}
