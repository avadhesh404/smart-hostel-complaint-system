CREATE DATABASE IF NOT EXISTS SmartHostelDB;
USE SmartHostelDB;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    role ENUM('STUDENT', 'ADMIN') NOT NULL
);

CREATE TABLE IF NOT EXISTS complaints (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_username VARCHAR(50) NOT NULL,
    room_number VARCHAR(10) NOT NULL,
    description TEXT NOT NULL,
    status ENUM('PENDING', 'RESOLVED') DEFAULT 'PENDING',
    FOREIGN KEY (student_username) REFERENCES users(username)
);


INSERT IGNORE INTO users (username, password, role) VALUES ('AVADHESH', 'Avadh@12345', 'ADMIN');