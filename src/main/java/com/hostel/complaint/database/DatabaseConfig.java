package com.hostel.complaint.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Database configuration utility class
 * Loads database configuration from properties file
 */
public class DatabaseConfig {
    private static final String CONFIG_FILE = "/config.properties";
    private static Properties properties;

    static {
        loadProperties();
    }

    private static void loadProperties() {
        properties = new Properties();
        try (InputStream input = DatabaseConfig.class.getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("Unable to find " + CONFIG_FILE);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error loading database configuration", e);
        }
    }

    public static String getUrl() {
        return properties.getProperty("db.url");
    }

    public static String getUsername() {
        return properties.getProperty("db.username");
    }

    public static String getPassword() {
        return properties.getProperty("db.password");
    }

    public static String getDriver() {
        return properties.getProperty("db.driver");
    }

    public static int getInitialPoolSize() {
        return Integer.parseInt(properties.getProperty("db.pool.initialSize", "5"));
    }

    public static int getMaxPoolSize() {
        return Integer.parseInt(properties.getProperty("db.pool.maxSize", "20"));
    }

    public static String getAppName() {
        return properties.getProperty("app.name", "Smart Hostel Complaint Management System");
    }

    public static String getAppVersion() {
        return properties.getProperty("app.version", "1.0.0");
    }

    public static long getMaxFileSize() {
        return Long.parseLong(properties.getProperty("upload.maxFileSize", "5242880"));
    }

    public static String getUploadDirectory() {
        return properties.getProperty("upload.directory", "uploads");
    }

    public static String[] getAllowedExtensions() {
        return properties.getProperty("upload.allowedExtensions", "jpg,jpeg,png,gif").split(",");
    }
}
