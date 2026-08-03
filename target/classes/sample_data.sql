-- Sample Data for Smart Hostel Complaint Management System
-- Run this after schema.sql to populate test data

USE hostel_complaint_db;

-- ============================================
-- INSERT SAMPLE ROOMS
-- ============================================
INSERT INTO rooms (room_number, block_id, capacity, current_occupancy, floor_number, room_type) VALUES
-- Boys Block A rooms
('101', 1, 3, 2, 1, 'Triple'), ('102', 1, 3, 3, 1, 'Triple'),
('103', 1, 3, 1, 1, 'Triple'), ('104', 1, 3, 3, 1, 'Triple'),
('201', 1, 3, 2, 2, 'Triple'), ('202', 1, 3, 3, 2, 'Triple'),
('203', 1, 3, 2, 2, 'Triple'), ('204', 1, 3, 1, 2, 'Triple'),
('301', 1, 3, 3, 3, 'Triple'), ('302', 1, 3, 2, 3, 'Triple'),
-- Boys Block B rooms
('101', 2, 3, 2, 1, 'Triple'), ('102', 2, 3, 3, 1, 'Triple'),
('201', 2, 3, 1, 2, 'Triple'), ('202', 2, 3, 2, 2, 'Triple'),
-- Girls Block A rooms
('101', 3, 3, 3, 1, 'Triple'), ('102', 3, 3, 2, 1, 'Triple'),
('103', 3, 3, 3, 1, 'Triple'), ('104', 3, 3, 1, 1, 'Triple'),
('201', 3, 3, 2, 2, 'Triple'), ('202', 3, 3, 3, 2, 'Triple'),
-- Girls Block B rooms
('101', 4, 3, 2, 1, 'Triple'), ('102', 4, 3, 3, 1, 'Triple'),
('201', 4, 3, 1, 2, 'Triple'), ('202', 4, 3, 2, 2, 'Triple');

-- ============================================
-- INSERT SAMPLE HOSTEL STAFF
-- ============================================
-- Password: staff123 (hashed)
INSERT INTO hostel_staff (employee_number, username, password, full_name, email, phone, role, specialization, block_id) VALUES
('STF001', 'warden1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Rajesh Kumar', 'warden1@hostel.com', '+91-9876543220', 'Warden', 'Student Welfare', 1),
('STF002', 'maint1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Sunil Verma', 'maint1@hostel.com', '+91-9876543221', 'Maintenance', 'Electrical', NULL),
('STF003', 'maint2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Mohan Singh', 'maint2@hostel.com', '+91-9876543222', 'Maintenance', 'Plumbing', NULL),
('STF004', 'clean1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Anita Devi', 'clean1@hostel.com', '+91-9876543223', 'Cleaning', 'Sanitation', NULL),
('STF005', 'sec1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Ramesh Gupta', 'sec1@hostel.com', '+91-9876543224', 'Security', 'Patrol', NULL),
('STF006', 'warden2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Sunita Sharma', 'warden2@hostel.com', '+91-9876543225', 'Warden', 'Student Welfare', 3);

-- ============================================
-- INSERT SAMPLE STUDENTS
-- ============================================
-- Password: student123 (hashed)
INSERT INTO students (student_number, username, password, full_name, email, phone, block_id, room_id, course, year_of_study) VALUES
('STU2024001', 'student1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Amit Patel', 'amit.patel@college.edu', '+91-9876543301', 1, 1, 'B.Tech Computer Science', 2),
('STU2024002', 'student2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Priya Singh', 'priya.singh@college.edu', '+91-9876543302', 3, 13, 'B.Tech Computer Science', 2),
('STU2024003', 'student3', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Rahul Sharma', 'rahul.sharma@college.edu', '+91-9876543303', 1, 2, 'B.Tech Mechanical', 3),
('STU2024004', 'student4', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Neha Gupta', 'neha.gupta@college.edu', '+91-9876543304', 3, 14, 'B.Tech Electrical', 2),
('STU2024005', 'student5', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Vikram Singh', 'vikram.singh@college.edu', '+91-9876543305', 1, 3, 'M.Tech Computer Science', 1),
('STU2024006', 'student6', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Pooja Verma', 'pooja.verma@college.edu', '+91-9876543306', 3, 15, 'B.Tech Civil', 3),
('STU2024007', 'student7', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Karan Mehta', 'karan.mehta@college.edu', '+91-9876543307', 2, 17, 'B.Tech Computer Science', 2),
('STU2024008', 'student8', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Anjali Rao', 'anjali.rao@college.edu', '+91-9876543308', 4, 19, 'B.Tech Electronics', 2);

-- ============================================
-- INSERT SAMPLE COMPLAINTS
-- ============================================
INSERT INTO complaints (complaint_number, student_id, category_id, assigned_staff_id, title, description, block_id, room_id, priority, status, submitted_at, assigned_at, in_progress_at, resolved_at, resolution_notes) VALUES
-- Submitted complaints
('CMP2024001', 1, 1, NULL, 'Fan not working', 'Ceiling fan in room 101 is not working. It makes noise but does not rotate.', 1, 1, 'High', 'Submitted', '2024-01-15 09:30:00', NULL, NULL, NULL, NULL),
('CMP2024002', 2, 3, NULL, 'No WiFi connectivity', 'WiFi signal is very weak in room 101. Unable to connect to internet.', 3, 13, 'Medium', 'Submitted', '2024-01-15 10:15:00', NULL, NULL, NULL, NULL),
-- Assigned complaints
('CMP2024003', 3, 2, 3, 'Tap leaking', 'Washroom tap is leaking continuously. Water is being wasted.', 1, 2, 'High', 'Assigned', '2024-01-14 14:00:00', '2024-01-14 15:30:00', NULL, NULL, NULL),
-- In Progress complaints
('CMP2024004', 4, 5, 2, 'Broken chair', 'One of the chairs in the room is broken. The seat is detached from the frame.', 3, 14, 'Low', 'In Progress', '2024-01-13 11:00:00', '2024-01-13 12:00:00', '2024-01-13 14:00:00', NULL, NULL),
-- Resolved complaints
('CMP2024005', 1, 7, 4, 'Room not cleaned', 'Room was not cleaned properly. Dust accumulation on shelves.', 1, 1, 'Medium', 'Resolved', '2024-01-10 08:00:00', '2024-01-10 09:00:00', '2024-01-10 10:00:00', '2024-01-10 16:00:00', 'Room cleaned thoroughly. All dust removed.'),
('CMP2024006', 5, 1, 2, 'Light flickering', 'Tube light in room 103 is flickering continuously.', 1, 3, 'Medium', 'Resolved', '2024-01-09 15:30:00', '2024-01-09 16:00:00', '2024-01-09 17:00:00', '2024-01-09 18:30:00', 'Replaced faulty tube light and starter.'),
-- Closed complaints
('CMP2024007', 2, 6, 3, 'No water supply', 'No water supply in the washroom since morning.', 3, 13, 'Critical', 'Closed', '2024-01-05 07:00:00', '2024-01-05 07:30:00', '2024-01-05 08:00:00', '2024-01-05 12:00:00', 'Main valve was closed. Opened it and checked all connections.'),
('CMP2024008', 3, 9, 2, 'Window broken', 'Window pane is cracked. Needs replacement.', 1, 2, 'Low', 'Closed', '2024-01-03 10:00:00', '2024-01-03 11:00:00', '2024-01-03 13:00:00', '2024-01-04 17:00:00', 'Replaced broken window pane with new glass.');

-- ============================================
-- INSERT SAMPLE COMPLAINT STATUS HISTORY
-- ============================================
INSERT INTO complaint_status_history (complaint_id, old_status, new_status, changed_by, changed_by_role, notes, changed_at) VALUES
-- CMP2024003 history
(3, 'Submitted', 'Assigned', 'admin', 'Admin', 'Assigned to maintenance staff', '2024-01-14 15:30:00'),
-- CMP2024004 history
(4, 'Submitted', 'Assigned', 'admin', 'Admin', 'Assigned to electrical maintenance', '2024-01-13 12:00:00'),
(4, 'Assigned', 'In Progress', 'maint1', 'Staff', 'Started working on the repair', '2024-01-13 14:00:00'),
-- CMP2024005 history
(5, 'Submitted', 'Assigned', 'admin', 'Admin', 'Assigned to cleaning staff', '2024-01-10 09:00:00'),
(5, 'Assigned', 'In Progress', 'clean1', 'Staff', 'Started cleaning process', '2024-01-10 10:00:00'),
(5, 'In Progress', 'Resolved', 'clean1', 'Staff', 'Room cleaned successfully', '2024-01-10 16:00:00'),
-- CMP2024006 history
(6, 'Submitted', 'Assigned', 'admin', 'Admin', 'Assigned to electrical maintenance', '2024-01-09 16:00:00'),
(6, 'Assigned', 'In Progress', 'maint1', 'Staff', 'Diagnosing the issue', '2024-01-09 17:00:00'),
(6, 'In Progress', 'Resolved', 'maint1', 'Staff', 'Replaced faulty components', '2024-01-09 18:30:00'),
-- CMP2024007 history
(7, 'Submitted', 'Assigned', 'admin', 'Admin', 'Critical issue - assigned immediately', '2024-01-05 07:30:00'),
(7, 'Assigned', 'In Progress', 'maint2', 'Staff', 'Checking water supply lines', '2024-01-05 08:00:00'),
(7, 'In Progress', 'Resolved', 'maint2', 'Staff', 'Water supply restored', '2024-01-05 12:00:00'),
(7, 'Resolved', 'Closed', 'student3', 'Student', 'Issue resolved satisfactorily', '2024-01-05 18:00:00'),
-- CMP2024008 history
(8, 'Submitted', 'Assigned', 'admin', 'Admin', 'Assigned to maintenance staff', '2024-01-03 11:00:00'),
(8, 'Assigned', 'In Progress', 'maint1', 'Staff', 'Ordered new glass pane', '2024-01-03 13:00:00'),
(8, 'In Progress', 'Resolved', 'maint1', 'Staff', 'Window replaced successfully', '2024-01-04 17:00:00'),
(8, 'Resolved', 'Closed', 'student3', 'Student', 'Work completed satisfactorily', '2024-01-05 10:00:00');

-- ============================================
-- INSERT SAMPLE NOTIFICATIONS
-- ============================================
INSERT INTO notifications (user_id, user_type, complaint_id, title, message, is_read, created_at) VALUES
(1, 'Student', 3, 'Complaint Assigned', 'Your complaint CMP2024003 has been assigned to maintenance staff.', FALSE, '2024-01-14 15:35:00'),
(2, 'Student', 4, 'Complaint In Progress', 'Your complaint CMP2024004 is now being processed.', FALSE, '2024-01-13 14:05:00'),
(1, 'Student', 5, 'Complaint Resolved', 'Your complaint CMP2024005 has been resolved. Please rate the service.', FALSE, '2024-01-10 16:05:00'),
(3, 'Staff', 5, 'New Complaint Assigned', 'New complaint CMP2024005 has been assigned to you.', TRUE, '2024-01-10 09:05:00'),
(2, 'Staff', 6, 'New Complaint Assigned', 'New complaint CMP2024006 has been assigned to you.', TRUE, '2024-01-09 16:05:00'),
(3, 'Staff', 7, 'URGENT: Critical Complaint', 'Critical complaint CMP2024007 requires immediate attention.', TRUE, '2024-01-05 07:35:00');

-- ============================================
-- INSERT SAMPLE FEEDBACK
-- ============================================
INSERT INTO feedback (complaint_id, student_id, rating, comments, submitted_at) VALUES
(5, 1, 4, 'Good response time. Room was cleaned properly.', '2024-01-10 18:00:00'),
(6, 5, 5, 'Excellent service. Problem was resolved quickly.', '2024-01-09 19:00:00'),
(7, 3, 5, 'Very fast response to critical issue. Satisfied with the service.', '2024-01-05 18:30:00'),
(8, 3, 4, 'Work was done well, but took a day longer than expected.', '2024-01-05 10:30:00');

-- ============================================
-- INSERT SAMPLE AUDIT LOGS
-- ============================================
INSERT INTO audit_logs (user_id, user_type, action, table_name, record_id, old_values, new_values, ip_address, created_at) VALUES
(NULL, 'System', 'INSERT', 'students', 1, NULL, '{"student_number":"STU2024001","username":"student1"}', '127.0.0.1', '2024-01-01 10:00:00'),
(NULL, 'System', 'INSERT', 'students', 2, NULL, '{"student_number":"STU2024002","username":"student2"}', '127.0.0.1', '2024-01-01 10:00:01'),
(1, 'Admin', 'UPDATE', 'complaints', 3, '{"status":"Submitted"}', '{"status":"Assigned"}', '192.168.1.100', '2024-01-14 15:30:00'),
(1, 'Admin', 'UPDATE', 'complaints', 4, '{"status":"Submitted"}', '{"status":"Assigned"}', '192.168.1.100', '2024-01-13 12:00:00'),
(3, 'Staff', 'UPDATE', 'complaints', 5, '{"status":"Assigned"}', '{"status":"In Progress"}', '192.168.1.105', '2024-01-10 10:00:00'),
(3, 'Staff', 'UPDATE', 'complaints', 5, '{"status":"In Progress"}', '{"status":"Resolved"}', '192.168.1.105', '2024-01-10 16:00:00');
