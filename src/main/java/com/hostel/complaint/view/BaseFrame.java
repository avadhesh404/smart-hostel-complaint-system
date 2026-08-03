package com.hostel.complaint.view;

import javax.swing.*;
import java.awt.*;

/**
 * Base Frame class
 * Provides common functionality for all application frames
 */
public class BaseFrame extends JFrame {
    
    protected JPanel mainPanel;
    protected JPanel contentPanel;
    protected JLabel titleLabel;
    
    public BaseFrame(String title) {
        super(title);
        initializeFrame();
    }
    
    protected void initializeFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));
        
        // Use system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default look and feel if system look and feel fails
        }
        
        // Create main panel with background color
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        
        // Create content panel
        contentPanel = new JPanel();
        contentPanel.setBackground(UIConstants.PANEL_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(UIConstants.PANEL_PADDING, UIConstants.PANEL_PADDING, UIConstants.PANEL_PADDING, UIConstants.PANEL_PADDING));
        
        // Add content panel to main panel with scroll support
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
    }
    
    /**
     * Set the title of the frame
     * @param title Title text
     */
    protected void setTitleLabel(String title) {
        if (titleLabel == null) {
            titleLabel = new JLabel(title);
            titleLabel.setFont(UIConstants.TITLE_FONT);
            titleLabel.setForeground(UIConstants.TEXT_COLOR);
            titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        } else {
            titleLabel.setText(title);
        }
    }
    
    /**
     * Create a styled button
     * @param text Button text
     * @return Styled JButton
     */
    protected JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(UIConstants.BUTTON_FONT);
        button.setPreferredSize(new Dimension(UIConstants.BUTTON_WIDTH, UIConstants.BUTTON_HEIGHT));
        button.setFocusPainted(false);
        button.setBackground(UIConstants.PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    /**
     * Create a styled button with specific color
     * @param text Button text
     * @param color Button color
     * @return Styled JButton
     */
    protected JButton createButton(String text, Color color) {
        JButton button = createButton(text);
        button.setBackground(color);
        return button;
    }
    
    /**
     * Create a styled text field
     * @param columns Number of columns
     * @return Styled JTextField
     */
    protected JTextField createTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setFont(UIConstants.NORMAL_FONT);
        textField.setPreferredSize(new Dimension(200, UIConstants.TEXT_FIELD_HEIGHT));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return textField;
    }
    
    /**
     * Create a styled text area
     * @param rows Number of rows
     * @param columns Number of columns
     * @return Styled JTextArea
     */
    protected JTextArea createTextArea(int rows, int columns) {
        JTextArea textArea = new JTextArea(rows, columns);
        textArea.setFont(UIConstants.NORMAL_FONT);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return textArea;
    }
    
    /**
     * Create a styled combo box
     * @param items Items to add
     * @return Styled JComboBox
     */
    protected <T> JComboBox<T> createComboBox(T[] items) {
        JComboBox<T> comboBox = new JComboBox<>(items);
        comboBox.setFont(UIConstants.NORMAL_FONT);
        comboBox.setPreferredSize(new Dimension(200, UIConstants.TEXT_FIELD_HEIGHT));
        return comboBox;
    }
    
    /**
     * Create a styled label
     * @param text Label text
     * @return Styled JLabel
     */
    protected JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIConstants.NORMAL_FONT);
        label.setForeground(UIConstants.TEXT_COLOR);
        return label;
    }
    
    /**
     * Create a styled header label
     * @param text Label text
     * @return Styled JLabel
     */
    protected JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIConstants.HEADER_FONT);
        label.setForeground(UIConstants.TEXT_COLOR);
        return label;
    }
    
    /**
     * Create a styled panel
     * @return Styled JPanel
     */
    protected JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(UIConstants.PANEL_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(UIConstants.PANEL_PADDING, UIConstants.PANEL_PADDING, UIConstants.PANEL_PADDING, UIConstants.PANEL_PADDING));
        return panel;
    }
    
    /**
     * Create a styled panel with specific layout
     * @param layout Layout manager
     * @return Styled JPanel
     */
    protected JPanel createPanel(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(UIConstants.PANEL_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(UIConstants.PANEL_PADDING, UIConstants.PANEL_PADDING, UIConstants.PANEL_PADDING, UIConstants.PANEL_PADDING));
        return panel;
    }
    
    /**
     * Create a styled table
     * @return Styled JTable
     */
    protected JTable createTable() {
        JTable table = new JTable();
        table.setFont(UIConstants.NORMAL_FONT);
        table.setRowHeight(25);
        table.getTableHeader().setFont(UIConstants.SUBHEADER_FONT);
        table.getTableHeader().setBackground(UIConstants.BACKGROUND_COLOR);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return table;
    }
    
    /**
     * Create a styled scroll pane
     * @param component Component to scroll
     * @return Styled JScrollPane
     */
    protected JScrollPane createScrollPane(Component component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }
    
    /**
     * Create a styled separator
     * @return JSeparator
     */
    protected JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.setForeground(UIConstants.BORDER_COLOR);
        return separator;
    }
    
    /**
     * Create a styled panel with titled border
     * @param title Panel title
     * @return Styled JPanel
     */
    protected JPanel createTitledPanel(String title) {
        JPanel panel = createPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
            title,
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            UIConstants.SUBHEADER_FONT,
            UIConstants.TEXT_COLOR
        ));
        return panel;
    }
    
    /**
     * Show an error message dialog
     * @param message Error message
     */
    protected void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Show an information message dialog
     * @param message Information message
     */
    protected void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Show a confirmation dialog
     * @param message Confirmation message
     * @return true if user confirms
     */
    protected boolean showConfirmation(String message) {
        return JOptionPane.showConfirmDialog(this, message, "Confirm", 
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
    
    /**
     * Show a success message dialog
     * @param message Success message
     */
    protected void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Center the frame on screen
     */
    protected void centerOnScreen() {
        setLocationRelativeTo(null);
    }
    
    /**
     * Apply common styling to a button
     * @param button Button to style
     * @param color Button color
     */
    protected void styleButton(JButton button, Color color) {
        button.setFont(UIConstants.BUTTON_FONT);
        button.setPreferredSize(new Dimension(UIConstants.BUTTON_WIDTH, UIConstants.BUTTON_HEIGHT));
        button.setFocusPainted(false);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
