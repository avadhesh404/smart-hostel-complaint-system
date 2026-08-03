package com.hostel.complaint.util;

import java.util.regex.Pattern;

/**
 * Validation utility class
 * Provides common validation methods
 */
public class ValidationUtil {
    
    // Email pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    // Phone pattern (basic international format)
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[+]?[0-9]{10,15}$"
    );
    
    // Username pattern (alphanumeric, underscore, hyphen)
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_-]{3,20}$"
    );
    
    // Student number pattern
    private static final Pattern STUDENT_NUMBER_PATTERN = Pattern.compile(
        "^[A-Za-z0-9]{5,20}$"
    );
    
    // Employee number pattern
    private static final Pattern EMPLOYEE_NUMBER_PATTERN = Pattern.compile(
        "^[A-Za-z0-9]{5,20}$"
    );
    
    /**
     * Validate email address
     * @param email Email to validate
     * @return true if valid
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Validate phone number
     * @param phone Phone number to validate
     * @return true if valid
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        String cleanPhone = phone.replaceAll("[\\s\\-\\(\\)]", "");
        return PHONE_PATTERN.matcher(cleanPhone).matches();
    }
    
    /**
     * Validate username
     * @param username Username to validate
     * @return true if valid
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username).matches();
    }
    
    /**
     * Validate student number
     * @param studentNumber Student number to validate
     * @return true if valid
     */
    public static boolean isValidStudentNumber(String studentNumber) {
        if (studentNumber == null || studentNumber.trim().isEmpty()) {
            return false;
        }
        return STUDENT_NUMBER_PATTERN.matcher(studentNumber).matches();
    }
    
    /**
     * Validate employee number
     * @param employeeNumber Employee number to validate
     * @return true if valid
     */
    public static boolean isValidEmployeeNumber(String employeeNumber) {
        if (employeeNumber == null || employeeNumber.trim().isEmpty()) {
            return false;
        }
        return EMPLOYEE_NUMBER_PATTERN.matcher(employeeNumber).matches();
    }
    
    /**
     * Validate password strength
     * @param password Password to validate
     * @return true if valid (at least 6 characters)
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        return true;
    }
    
    /**
     * Validate strong password (at least 8 characters, 1 uppercase, 1 lowercase, 1 digit)
     * @param password Password to validate
     * @return true if strong
     */
    public static boolean isStrongPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasLowercase = !password.equals(password.toUpperCase());
        boolean hasDigit = password.matches(".*\\d.*");
        
        return hasUppercase && hasLowercase && hasDigit;
    }
    
    /**
     * Validate name (letters, spaces, hyphens, apostrophes)
     * @param name Name to validate
     * @return true if valid
     */
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return name.matches("^[a-zA-Z\\s'-]{2,50}$");
    }
    
    /**
     * Validate numeric string
     * @param value String to validate
     * @return true if numeric
     */
    public static boolean isNumeric(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validate positive integer
     * @param value String to validate
     * @return true if positive integer
     */
    public static boolean isPositiveInteger(String value) {
        if (!isNumeric(value)) {
            return false;
        }
        try {
            int num = Integer.parseInt(value);
            return num > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validate rating (1-5)
     * @param rating Rating to validate
     * @return true if valid
     */
    public static boolean isValidRating(Integer rating) {
        if (rating == null) {
            return false;
        }
        return rating >= 1 && rating <= 5;
    }
    
    /**
     * Validate file extension
     * @param fileName File name
     * @param allowedExtensions Allowed extensions
     * @return true if extension is allowed
     */
    public static boolean isValidFileExtension(String fileName, String[] allowedExtensions) {
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }
        
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        
        for (String allowed : allowedExtensions) {
            if (allowed.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Validate file size
     * @param fileSize File size in bytes
     * @param maxSize Maximum allowed size in bytes
     * @return true if within size limit
     */
    public static boolean isValidFileSize(long fileSize, long maxSize) {
        return fileSize > 0 && fileSize <= maxSize;
    }
    
    /**
     * Sanitize string input (remove potentially dangerous characters)
     * @param input String to sanitize
     * @return Sanitized string
     */
    public static String sanitize(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("[<>\"'&]", "");
    }
    
    /**
     * Check if string is null or empty
     * @param value String to check
     * @return true if null or empty
     */
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
    
    /**
     * Check if string is not null or empty
     * @param value String to check
     * @return true if not null and not empty
     */
    public static boolean isNotNullOrEmpty(String value) {
        return !isNullOrEmpty(value);
    }
    
    /**
     * Truncate string to maximum length
     * @param value String to truncate
     * @param maxLength Maximum length
     * @return Truncated string
     */
    public static String truncate(String value, int maxLength) {
        if (value == null) {
            return null;
        }
        if (value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength);
    }
}
