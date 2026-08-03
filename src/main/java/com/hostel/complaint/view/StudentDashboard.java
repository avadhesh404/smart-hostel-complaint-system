package com.hostel.complaint.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * Student Dashboard View
 * Main interface for student users
 */
public class StudentDashboard extends BaseFrame {
    
    private JLabel welcomeLabel;
    private JLabel userInfoLabel;
    private JButton submitComplaintButton;
    private JButton viewComplaintsButton;
    private JButton trackComplaintButton;
    private JButton editProfileButton;
    private JButton viewNotificationsButton;
    private JButton logoutButton;
    
    private JPanel statsPanel;
    private JLabel totalComplaintsLabel;
    private JLabel pendingComplaintsLabel;
    private JLabel resolvedComplaintsLabel;
    
    private StudentDashboardListener listener;
    
    public StudentDashboard(String studentName, String studentNumber) {
        super("Student Dashboard");
        initializeComponents(studentName, studentNumber);
        layoutComponents();
    }
    
    private void initializeComponents(String studentName, String studentNumber) {
        // Welcome label
        welcomeLabel = new JLabel("Welcome, " + studentName);
        welcomeLabel.setFont(UIConstants.TITLE_FONT);
        welcomeLabel.setForeground(UIConstants.PRIMARY_COLOR);
        
        // User info label
        userInfoLabel = new JLabel("Student Number: " + studentNumber);
        userInfoLabel.setFont(UIConstants.NORMAL_FONT);
        userInfoLabel.setForeground(UIConstants.TEXT_LIGHT_COLOR);
        
        // Action buttons
        submitComplaintButton = createButton("Submit Complaint");
        submitComplaintButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listener != null) {
                    listener.onSubmitComplaint();
                }
            }
        });
        
        viewComplaintsButton = createButton("View My Complaints");
        viewComplaintsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listener != null) {
                    listener.onViewComplaints();
                }
            }
        });
        
        trackComplaintButton = createButton("Track Complaint");
        trackComplaintButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listener != null) {
                    listener.onTrackComplaint();
                }
            }
        });
        
        editProfileButton = createButton("Edit Profile");
        editProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listener != null) {
                    listener.onEditProfile();
                }
            }
        });
        
        viewNotificationsButton = createButton("Notifications");
        viewNotificationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listener != null) {
                    listener.onViewNotifications();
                }
            }
        });
        
        logoutButton = createButton("Logout", UIConstants.DANGER_COLOR);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listener != null) {
                    listener.onLogout();
                }
            }
        });
        
        // Statistics labels
        totalComplaintsLabel = createLabel("Total: 0");
        totalComplaintsLabel.setFont(UIConstants.HEADER_FONT);
        totalComplaintsLabel.setForeground(UIConstants.PRIMARY_COLOR);
        
        pendingComplaintsLabel = createLabel("Pending: 0");
        pendingComplaintsLabel.setFont(UIConstants.HEADER_FONT);
        pendingComplaintsLabel.setForeground(UIConstants.WARNING_COLOR);
        
        resolvedComplaintsLabel = createLabel("Resolved: 0");
        resolvedComplaintsLabel.setFont(UIConstants.HEADER_FONT);
        resolvedComplaintsLabel.setForeground(UIConstants.SUCCESS_COLOR);
    }
    
    private void layoutComponents() {
        contentPanel.setLayout(new BorderLayout(10, 10));
        
        // Header panel
        JPanel headerPanel = createPanel(new BorderLayout(10, 10));
        headerPanel.add(welcomeLabel, BorderLayout.NORTH);
        headerPanel.add(userInfoLabel, BorderLayout.CENTER);
        
        // Stats panel
        statsPanel = createTitledPanel("My Complaint Statistics");
        statsPanel.setLayout(new GridLayout(1, 3, 20, 0));
        statsPanel.add(totalComplaintsLabel);
        statsPanel.add(pendingComplaintsLabel);
        statsPanel.add(resolvedComplaintsLabel);
        
        // Button panel
        JPanel buttonPanel = createPanel(new GridLayout(5, 1, 10, 10));
        buttonPanel.add(submitComplaintButton);
        buttonPanel.add(viewComplaintsButton);
        buttonPanel.add(trackComplaintButton);
        buttonPanel.add(editProfileButton);
        buttonPanel.add(viewNotificationsButton);
        
        // Logout panel
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.setBackground(UIConstants.PANEL_COLOR);
        logoutPanel.add(logoutButton);
        
        // Main layout
        JPanel centerPanel = createPanel(new BorderLayout(20, 20));
        centerPanel.add(statsPanel, BorderLayout.NORTH);
        centerPanel.add(buttonPanel, BorderLayout.CENTER);
        
        contentPanel.add(headerPanel, BorderLayout.NORTH);
        contentPanel.add(centerPanel, BorderLayout.CENTER);
        contentPanel.add(logoutPanel, BorderLayout.SOUTH);
    }
    
    public void updateStatistics(Map<String, Long> stats) {
        if (stats != null) {
            long total = stats.getOrDefault("total", 0L);
            long pending = stats.getOrDefault("submitted", 0L) + stats.getOrDefault("assigned", 0L) + 
                          stats.getOrDefault("in_progress", 0L);
            long resolved = stats.getOrDefault("resolved", 0L);
            
            totalComplaintsLabel.setText("Total: " + total);
            pendingComplaintsLabel.setText("Pending: " + pending);
            resolvedComplaintsLabel.setText("Resolved: " + resolved);
        }
    }
    
    public void setListener(StudentDashboardListener listener) {
        this.listener = listener;
    }
    
    public interface StudentDashboardListener {
        void onSubmitComplaint();
        void onViewComplaints();
        void onTrackComplaint();
        void onEditProfile();
        void onViewNotifications();
        void onLogout();
    }
}
