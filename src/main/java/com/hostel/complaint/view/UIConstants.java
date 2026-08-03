package com.hostel.complaint.view;

import java.awt.Color;
import java.awt.Font;

/**
 * UI Constants
 * Defines colors, fonts, and styling constants for the application
 */
public class UIConstants {
    
    // Color Palette
    public static final Color PRIMARY_COLOR = new Color(66, 133, 244);      // Blue
    public static final Color SECONDARY_COLOR = new Color(52, 168, 83);    // Green
    public static final Color DANGER_COLOR = new Color(234, 67, 53);       // Red
    public static final Color WARNING_COLOR = new Color(255, 152, 0);      // Orange
    public static final Color INFO_COLOR = new Color(3, 169, 244);        // Light Blue
    public static final Color SUCCESS_COLOR = new Color(76, 175, 80);     // Green
    
    public static final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Light Gray
    public static final Color PANEL_COLOR = new Color(255, 255, 255);     // White
    public static final Color TEXT_COLOR = new Color(33, 33, 33);         // Dark Gray
    public static final Color TEXT_LIGHT_COLOR = new Color(117, 117, 117); // Light Gray
    public static final Color BORDER_COLOR = new Color(224, 224, 224);    // Border Gray
    
    // Status Colors
    public static final Color STATUS_SUBMITTED = new Color(156, 39, 176);  // Purple
    public static final Color STATUS_ASSIGNED = new Color(3, 169, 244);    // Blue
    public static final Color STATUS_IN_PROGRESS = new Color(255, 152, 0); // Orange
    public static final Color STATUS_RESOLVED = new Color(76, 175, 80);    // Green
    public static final Color STATUS_CLOSED = new Color(117, 117, 117);   // Gray
    
    // Priority Colors
    public static final Color PRIORITY_LOW = new Color(76, 175, 80);       // Green
    public static final Color PRIORITY_MEDIUM = new Color(255, 152, 0);   // Orange
    public static final Color PRIORITY_HIGH = new Color(244, 67, 54);     // Red
    public static final Color PRIORITY_CRITICAL = new Color(136, 14, 79);  // Dark Red
    
    // Fonts
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font SUBHEADER_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 10);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);
    
    // Dimensions
    public static final int BUTTON_HEIGHT = 35;
    public static final int BUTTON_WIDTH = 120;
    public static final int TEXT_FIELD_HEIGHT = 30;
    public static final int PANEL_PADDING = 20;
    public static final int COMPONENT_GAP = 10;
    
    // Border
    public static final int BORDER_THICKNESS = 1;
    
    // Animation
    public static final int ANIMATION_DELAY = 100;
    
    private UIConstants() {
        // Private constructor to prevent instantiation
    }
}
