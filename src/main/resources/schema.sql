-- Smart Hostel Complaint Management System Database Schema
-- MySQL Database Schema

-- Create Database
CREATE DATABASE IF NOT EXISTS hostel_complaint_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE hostel_complaint_db;

-- Drop tables if they exist (for clean setup)
DROP TABLE IF EXISTS audit_logs;
DROP TABLE IF EXISTS feedback;
DROP TABLE IF EXISTS complaint_status_history;
DROP TABLE IF EXISTS complaint_images;
DROP TABLE IF EXISTS complaints;
DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS rooms;
DROP TABLE IF EXISTS hostel_blocks;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS hostel_staff;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS admin;

-- ============================================
-- ADMIN TABLE
-- ============================================
CREATE TABLE admin (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL,
    is_active BOOLEAN DEFAULT TRUE,
    INDEX idx_admin_username (username),
    INDEX idx_admin_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- STUDENTS TABLE
-- ============================================
CREATE TABLE students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    student_number VARCHAR(20) UNIQUE NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    block_id INT,
    room_id INT,
    course VARCHAR(100),
    year_of_study INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (block_id) REFERENCES hostel_blocks(block_id) ON DELETE SET NULL,
    FOREIGN KEY (room_id) REFERENCES rooms(room_id) ON DELETE SET NULL,
    INDEX idx_student_number (student_number),
    INDEX idx_student_username (username),
    INDEX idx_student_email (email),
    INDEX idx_student_block_room (block_id, room_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- HOSTEL BLOCKS TABLE
-- ============================================
CREATE TABLE hostel_blocks (
    block_id INT AUTO_INCREMENT PRIMARY KEY,
    block_name VARCHAR(50) UNIQUE NOT NULL,
    block_code VARCHAR(10) UNIQUE NOT NULL,
    description TEXT,
    total_rooms INT DEFAULT 0,
    warden_name VARCHAR(100),
    warden_phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    INDEX idx_block_code (block_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- ROOMS TABLE
-- ============================================
CREATE TABLE rooms (
    room_id INT AUTO_INCREMENT PRIMARY KEY,
    room_number VARCHAR(20) NOT NULL,
    block_id INT NOT NULL,
    capacity INT DEFAULT 3,
    current_occupancy INT DEFAULT 0,
    floor_number INT,
    room_type ENUM('Single', 'Double', 'Triple', 'Four Bed') DEFAULT 'Triple',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (block_id) REFERENCES hostel_blocks(block_id) ON DELETE CASCADE,
    UNIQUE KEY unique_room_block (room_number, block_id),
    INDEX idx_room_block (block_id),
    INDEX idx_room_number (room_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- HOSTEL STAFF TABLE
-- ============================================
CREATE TABLE hostel_staff (
    staff_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_number VARCHAR(20) UNIQUE NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    role ENUM('Warden', 'Maintenance', 'Cleaning', 'Security', 'Other') NOT NULL,
    specialization VARCHAR(100),
    block_id INT,
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (block_id) REFERENCES hostel_blocks(block_id) ON DELETE SET NULL,
    INDEX idx_staff_username (username),
    INDEX idx_staff_email (email),
    INDEX idx_staff_role (role),
    INDEX idx_staff_block (block_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- CATEGORIES TABLE
-- ============================================
CREATE TABLE categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    default_priority ENUM('Low', 'Medium', 'High', 'Critical') DEFAULT 'Medium',
    estimated_resolution_hours INT DEFAULT 24,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_category_name (category_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- COMPLAINTS TABLE
-- ============================================
CREATE TABLE complaints (
    complaint_id INT AUTO_INCREMENT PRIMARY KEY,
    complaint_number VARCHAR(20) UNIQUE NOT NULL,
    student_id INT NOT NULL,
    category_id INT NOT NULL,
    assigned_staff_id INT,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    block_id INT NOT NULL,
    room_id INT NOT NULL,
    priority ENUM('Low', 'Medium', 'High', 'Critical') DEFAULT 'Medium',
    status ENUM('Submitted', 'Assigned', 'In Progress', 'Resolved', 'Closed') DEFAULT 'Submitted',
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    assigned_at TIMESTAMP NULL,
    in_progress_at TIMESTAMP NULL,
    resolved_at TIMESTAMP NULL,
    closed_at TIMESTAMP NULL,
    resolution_notes TEXT,
    completion_image_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE RESTRICT,
    FOREIGN KEY (assigned_staff_id) REFERENCES hostel_staff(staff_id) ON DELETE SET NULL,
    FOREIGN KEY (block_id) REFERENCES hostel_blocks(block_id) ON DELETE RESTRICT,
    FOREIGN KEY (room_id) REFERENCES rooms(room_id) ON DELETE RESTRICT,
    INDEX idx_complaint_number (complaint_number),
    INDEX idx_complaint_student (student_id),
    INDEX idx_complaint_staff (assigned_staff_id),
    INDEX idx_complaint_category (category_id),
    INDEX idx_complaint_status (status),
    INDEX idx_complaint_priority (priority),
    INDEX idx_complaint_block_room (block_id, room_id),
    INDEX idx_complaint_dates (submitted_at, resolved_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- COMPLAINT IMAGES TABLE
-- ============================================
CREATE TABLE complaint_images (
    image_id INT AUTO_INCREMENT PRIMARY KEY,
    complaint_id INT NOT NULL,
    image_path VARCHAR(255) NOT NULL,
    image_name VARCHAR(255) NOT NULL,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (complaint_id) REFERENCES complaints(complaint_id) ON DELETE CASCADE,
    INDEX idx_image_complaint (complaint_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- COMPLAINT STATUS HISTORY TABLE
-- ============================================
CREATE TABLE complaint_status_history (
    history_id INT AUTO_INCREMENT PRIMARY KEY,
    complaint_id INT NOT NULL,
    old_status ENUM('Submitted', 'Assigned', 'In Progress', 'Resolved', 'Closed'),
    new_status ENUM('Submitted', 'Assigned', 'In Progress', 'Resolved', 'Closed') NOT NULL,
    changed_by VARCHAR(50) NOT NULL,
    changed_by_role ENUM('Student', 'Staff', 'Admin') NOT NULL,
    notes TEXT,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (complaint_id) REFERENCES complaints(complaint_id) ON DELETE CASCADE,
    INDEX idx_history_complaint (complaint_id),
    INDEX idx_history_date (changed_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- NOTIFICATIONS TABLE
-- ============================================
CREATE TABLE notifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    user_type ENUM('Student', 'Staff', 'Admin') NOT NULL,
    complaint_id INT,
    title VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (complaint_id) REFERENCES complaints(complaint_id) ON DELETE CASCADE,
    INDEX idx_notification_user (user_id, user_type),
    INDEX idx_notification_read (is_read),
    INDEX idx_notification_date (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- FEEDBACK TABLE
-- ============================================
CREATE TABLE feedback (
    feedback_id INT AUTO_INCREMENT PRIMARY KEY,
    complaint_id INT NOT NULL,
    student_id INT NOT NULL,
    rating INT CHECK (rating >= 1 AND rating <= 5),
    comments TEXT,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (complaint_id) REFERENCES complaints(complaint_id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    UNIQUE KEY unique_complaint_feedback (complaint_id, student_id),
    INDEX idx_feedback_complaint (complaint_id),
    INDEX idx_feedback_student (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- AUDIT LOGS TABLE
-- ============================================
CREATE TABLE audit_logs (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    user_type ENUM('Student', 'Staff', 'Admin', 'System'),
    action VARCHAR(100) NOT NULL,
    table_name VARCHAR(50),
    record_id INT,
    old_values JSON,
    new_values JSON,
    ip_address VARCHAR(45),
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_audit_user (user_id, user_type),
    INDEX idx_audit_action (action),
    INDEX idx_audit_table (table_name),
    INDEX idx_audit_date (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- INSERT DEFAULT CATEGORIES
-- ============================================
INSERT INTO categories (category_name, description, default_priority, estimated_resolution_hours) VALUES
('Electrical', 'Issues related to electrical fittings, lights, fans, switches', 'High', 12),
('Plumbing', 'Issues related to water supply, drainage, taps, toilets', 'High', 8),
('Internet', 'Issues related to Wi-Fi connectivity, network problems', 'Medium', 24),
('Cleaning', 'Issues related to room and common area cleaning', 'Medium', 24),
('Furniture', 'Issues related to beds, tables, chairs, cupboards', 'Low', 48),
('Water Supply', 'Issues related to water availability and quality', 'Critical', 4),
('Food', 'Issues related to mess food quality and service', 'Medium', 24),
('Security', 'Issues related to safety and security concerns', 'Critical', 2),
('Room Maintenance', 'General room maintenance issues', 'Low', 72),
('Other', 'Any other type of complaint', 'Medium', 48);

-- ============================================
-- INSERT DEFAULT HOSTEL BLOCKS
-- ============================================
INSERT INTO hostel_blocks (block_name, block_code, description, total_rooms, warden_name, warden_phone) VALUES
('Boys Block A', 'BBA', 'Main boys hostel block', 100, 'Dr. R. Sharma', '+91-9876543210'),
('Boys Block B', 'BBB', 'Secondary boys hostel block', 80, 'Mr. A. Kumar', '+91-9876543211'),
('Girls Block A', 'GBA', 'Main girls hostel block', 100, 'Mrs. S. Gupta', '+91-9876543212'),
('Girls Block B', 'GBB', 'Secondary girls hostel block', 80, 'Ms. P. Singh', '+91-9876543213');

-- ============================================
-- INSERT DEFAULT ADMIN USER
-- ============================================
-- Default password: admin123 (will be hashed by application)
INSERT INTO admin (username, password, full_name, email, phone) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'System Administrator', 'admin@hostel.com', '+91-9876543214');

-- ============================================
-- CREATE VIEWS FOR COMMON QUERIES
-- ============================================

-- View for complaint statistics
CREATE OR REPLACE VIEW v_complaint_stats AS
SELECT 
    c.status,
    c.priority,
    cat.category_name,
    COUNT(*) as count,
    AVG(TIMESTAMPDIFF(HOUR, c.submitted_at, COALESCE(c.resolved_at, NOW()))) as avg_resolution_hours
FROM complaints c
JOIN categories cat ON c.category_id = cat.category_id
GROUP BY c.status, c.priority, cat.category_name;

-- View for active complaints with details
CREATE OR REPLACE VIEW v_active_complaints AS
SELECT 
    c.*,
    s.student_number,
    s.full_name as student_name,
    cat.category_name,
    hsb.block_name,
    r.room_number,
    st.full_name as staff_name,
    st.role as staff_role
FROM complaints c
JOIN students s ON c.student_id = s.student_id
JOIN categories cat ON c.category_id = cat.category_id
JOIN hostel_blocks hsb ON c.block_id = hsb.block_id
JOIN rooms r ON c.room_id = r.room_id
LEFT JOIN hostel_staff st ON c.assigned_staff_id = st.staff_id
WHERE c.status != 'Closed';

-- View for staff performance
CREATE OR REPLACE VIEW v_staff_performance AS
SELECT 
    st.staff_id,
    st.employee_number,
    st.full_name,
    st.role,
    COUNT(c.complaint_id) as total_assigned,
    SUM(CASE WHEN c.status = 'Resolved' THEN 1 ELSE 0 END) as resolved,
    SUM(CASE WHEN c.status = 'In Progress' THEN 1 ELSE 0 END) as in_progress,
    AVG(TIMESTAMPDIFF(HOUR, c.assigned_at, COALESCE(c.resolved_at, NOW()))) as avg_resolution_time
FROM hostel_staff st
LEFT JOIN complaints c ON st.staff_id = c.assigned_staff_id
GROUP BY st.staff_id, st.employee_number, st.full_name, st.role;
