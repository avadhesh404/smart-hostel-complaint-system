package com.hostel.complaint.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Login View
 * Handles user authentication interface
 */
public class LoginView extends BaseFrame {
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeCombo;
    private JButton loginButton;
    private JButton forgotPasswordButton;
    
    private LoginListener loginListener;
    
    public LoginView() {
        super("Smart Hostel Complaint Management System - Login");
        initializeComponents();
        layoutComponents();
    }
    
    private void initializeComponents() {
        // Username field
        usernameField = createTextField(20);
        usernameField.setToolTipText("Enter your username");
        
        // Password field
        passwordField = new JPasswordField(20);
        passwordField.setFont(UIConstants.NORMAL_FONT);
        passwordField.setPreferredSize(new Dimension(200, UIConstants.TEXT_FIELD_HEIGHT));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        passwordField.setToolTipText("Enter your password");
        
        // User type combo box
        String[] userTypes = {"Student", "Hostel Staff", "Admin"};
        userTypeCombo = createComboBox(userTypes);
        userTypeCombo.setToolTipText("Select your user type");
        
        // Login button
        loginButton = createButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        
        // Forgot password button
        forgotPasswordButton = new JButton("Forgot Password?");
        forgotPasswordButton.setFont(UIConstants.SMALL_FONT);
        forgotPasswordButton.setBorderPainted(false);
        forgotPasswordButton.setContentAreaFilled(false);
        forgotPasswordButton.setForeground(UIConstants.PRIMARY_COLOR);
        forgotPasswordButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleForgotPassword();
            }
        });
        
        // Add Enter key support for login
        ActionListener enterAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        };
        passwordField.addActionListener(enterAction);
        usernameField.addActionListener(enterAction);
    }
    
    private void layoutComponents() {
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = new JLabel("Hostel Complaint Management");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.PRIMARY_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(titleLabel, gbc);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Please login to continue");
        subtitleLabel.setFont(UIConstants.NORMAL_FONT);
        subtitleLabel.setForeground(UIConstants.TEXT_LIGHT_COLOR);
        gbc.gridy = 1;
        contentPanel.add(subtitleLabel, gbc);
        
        // Empty row for spacing
        gbc.gridy = 2;
        contentPanel.add(Box.createVerticalStrut(30), gbc);
        
        // User type label
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridy = 3;
        contentPanel.add(createLabel("User Type:"), gbc);
        
        // User type combo
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        contentPanel.add(userTypeCombo, gbc);
        
        // Username label
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        contentPanel.add(createLabel("Username:"), gbc);
        
        // Username field
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        contentPanel.add(usernameField, gbc);
        
        // Password label
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        contentPanel.add(createLabel("Password:"), gbc);
        
        // Password field
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        contentPanel.add(passwordField, gbc);
        
        // Empty row for spacing
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 6;
        contentPanel.add(Box.createVerticalStrut(20), gbc);
        
        // Login button
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(loginButton, gbc);
        
        // Forgot password button
        gbc.gridy = 8;
        contentPanel.add(forgotPasswordButton, gbc);
        
        // Set focus to username field
        SwingUtilities.invokeLater(() -> usernameField.requestFocusInWindow());
    }
    
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String userType = (String) userTypeCombo.getSelectedItem();
        
        if (username.isEmpty()) {
            showError("Please enter username");
            usernameField.requestFocusInWindow();
            return;
        }
        
        if (password.isEmpty()) {
            showError("Please enter password");
            passwordField.requestFocusInWindow();
            return;
        }
        
        if (loginListener != null) {
            loginListener.onLoginAttempt(username, password, userType);
        }
    }
    
    private void handleForgotPassword() {
        if (loginListener != null) {
            loginListener.onForgotPassword();
        }
    }
    
    public void setLoginListener(LoginListener listener) {
        this.loginListener = listener;
    }
    
    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        usernameField.requestFocusInWindow();
    }
    
    public interface LoginListener {
        void onLoginAttempt(String username, String password, String userType);
        void onForgotPassword();
    }
}
