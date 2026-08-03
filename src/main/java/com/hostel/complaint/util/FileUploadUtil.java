package com.hostel.complaint.util;

import com.hostel.complaint.database.DatabaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * File upload utility class
 * Handles file operations for complaint images
 */
public class FileUploadUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadUtil.class);
    
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    
    /**
     * Upload a file
     * @param sourcePath Source file path
     * @param originalFileName Original file name
     * @return Uploaded file path relative to upload directory
     * @throws IOException if file operation fails
     * @throws FileUploadException if validation fails
     */
    public static String uploadFile(String sourcePath, String originalFileName) 
            throws IOException, FileUploadException {
        
        // Validate file
        validateFile(sourcePath, originalFileName);
        
        // Create upload directory if it doesn't exist
        String uploadDir = DatabaseConfig.getUploadDirectory();
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new FileUploadException("Failed to create upload directory");
            }
        }
        
        // Generate unique filename
        String uniqueFileName = generateUniqueFileName(originalFileName);
        String relativePath = uploadDir + File.separator + uniqueFileName;
        String absolutePath = new File(relativePath).getAbsolutePath();
        
        // Copy file
        Path source = Paths.get(sourcePath);
        Path destination = Paths.get(absolutePath);
        
        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
        
        logger.info("File uploaded successfully: {}", relativePath);
        return relativePath;
    }
    
    /**
     * Upload a file from byte array
     * @param fileData File data as byte array
     * @param originalFileName Original file name
     * @return Uploaded file path relative to upload directory
     * @throws IOException if file operation fails
     * @throws FileUploadException if validation fails
     */
    public static String uploadFile(byte[] fileData, String originalFileName) 
            throws IOException, FileUploadException {
        
        // Validate file data
        if (fileData == null || fileData.length == 0) {
            throw new FileUploadException("File data is empty");
        }
        
        // Validate file name
        validateFileName(originalFileName);
        
        // Create upload directory if it doesn't exist
        String uploadDir = DatabaseConfig.getUploadDirectory();
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new FileUploadException("Failed to create upload directory");
            }
        }
        
        // Generate unique filename
        String uniqueFileName = generateUniqueFileName(originalFileName);
        String relativePath = uploadDir + File.separator + uniqueFileName;
        String absolutePath = new File(relativePath).getAbsolutePath();
        
        // Write file
        Path destination = Paths.get(absolutePath);
        Files.write(destination, fileData);
        
        logger.info("File uploaded successfully: {}", relativePath);
        return relativePath;
    }
    
    /**
     * Delete a file
     * @param filePath File path to delete
     * @return true if deletion successful
     */
    public static boolean deleteFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }
        
        try {
            File file = new File(filePath);
            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    logger.info("File deleted successfully: {}", filePath);
                }
                return deleted;
            }
            return false;
        } catch (Exception e) {
            logger.error("Error deleting file: {}", filePath, e);
            return false;
        }
    }
    
    /**
     * Get file size
     * @param filePath File path
     * @return File size in bytes
     */
    public static long getFileSize(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return 0;
        }
        
        try {
            File file = new File(filePath);
            if (file.exists()) {
                return file.length();
            }
            return 0;
        } catch (Exception e) {
            logger.error("Error getting file size: {}", filePath, e);
            return 0;
        }
    }
    
    /**
     * Check if file exists
     * @param filePath File path
     * @return true if file exists
     */
    public static boolean fileExists(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }
        
        try {
            File file = new File(filePath);
            return file.exists() && file.isFile();
        } catch (Exception e) {
            logger.error("Error checking file existence: {}", filePath, e);
            return false;
        }
    }
    
    /**
     * Validate file before upload
     * @param filePath File path
     * @param fileName Original file name
     * @throws IOException if file cannot be read
     * @throws FileUploadException if validation fails
     */
    private static void validateFile(String filePath, String fileName) 
            throws IOException, FileUploadException {
        
        // Check if source file exists
        File sourceFile = new File(filePath);
        if (!sourceFile.exists()) {
            throw new FileUploadException("Source file does not exist");
        }
        
        // Check file size
        long fileSize = sourceFile.length();
        long maxSize = DatabaseConfig.getMaxFileSize();
        
        if (!ValidationUtil.isValidFileSize(fileSize, maxSize)) {
            throw new FileUploadException("File size exceeds maximum limit of " + 
                                        (maxSize / (1024 * 1024)) + " MB");
        }
        
        // Validate file name
        validateFileName(fileName);
    }
    
    /**
     * Validate file name
     * @param fileName File name to validate
     * @throws FileUploadException if validation fails
     */
    private static void validateFileName(String fileName) throws FileUploadException {
        if (fileName == null || fileName.isEmpty()) {
            throw new FileUploadException("File name is required");
        }
        
        // Check file extension
        String[] allowedExtensions = DatabaseConfig.getAllowedExtensions();
        if (!ValidationUtil.isValidFileExtension(fileName, allowedExtensions)) {
            throw new FileUploadException("Invalid file type. Allowed types: " + 
                                        String.join(", ", allowedExtensions));
        }
    }
    
    /**
     * Generate unique filename
     * @param originalFileName Original file name
     * @return Unique filename
     */
    private static String generateUniqueFileName(String originalFileName) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        
        String extension = "";
        int dotIndex = originalFileName.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFileName.substring(dotIndex);
        }
        
        return timestamp + "_" + uuid + extension;
    }
    
    /**
     * Clean up old files
     * @param days Number of days to keep
     * @return Number of files deleted
     */
    public static int cleanupOldFiles(int days) {
        String uploadDir = DatabaseConfig.getUploadDirectory();
        File directory = new File(uploadDir);
        
        if (!directory.exists() || !directory.isDirectory()) {
            return 0;
        }
        
        long cutoffTime = System.currentTimeMillis() - (days * 24L * 60L * 60L * 1000L);
        int deletedCount = 0;
        
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.lastModified() < cutoffTime) {
                    if (file.delete()) {
                        deletedCount++;
                        logger.info("Deleted old file: {}", file.getName());
                    }
                }
            }
        }
        
        return deletedCount;
    }
    
    public static class FileUploadException extends Exception {
        public FileUploadException(String message) {
            super(message);
        }
        
        public FileUploadException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
