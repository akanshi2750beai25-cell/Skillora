-- ==============================================================================
-- Skillora Database Schema & Seed Data (Akanshi's Contribution)
-- Module: Student Registration, Authentication & Profile Management
-- Course: Programming in Java (25CSE0202)
-- ==============================================================================

CREATE DATABASE IF NOT EXISTS skillora_db;
USE skillora_db;

-- 1. Users Table (Base entity for authentication & role identification)
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('STUDENT', 'ADMIN') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Students Table (Child entity extending Users with academic attributes)
CREATE TABLE IF NOT EXISTS students (
    student_id INT PRIMARY KEY,
    roll_number VARCHAR(50) NOT NULL UNIQUE,
    college VARCHAR(150) NOT NULL,
    branch VARCHAR(100) NOT NULL,
    semester INT NOT NULL,
    cgpa DECIMAL(3,2) NOT NULL,
    FOREIGN KEY (student_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 3. Admins Table (Child entity extending Users for college placement staff)
CREATE TABLE IF NOT EXISTS admins (
    admin_id INT PRIMARY KEY,
    department VARCHAR(100) NOT NULL,
    staff_id VARCHAR(50) NOT NULL UNIQUE,
    FOREIGN KEY (admin_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- ==============================================================================
-- Seed Data for Evaluation & Testing
-- ==============================================================================

-- Default Administrator
INSERT IGNORE INTO users (user_id, name, email, password, role) 
VALUES (1, 'Dr. Sharma (Placement Officer)', 'admin@skillora.edu', 'admin123', 'ADMIN');

INSERT IGNORE INTO admins (admin_id, department, staff_id) 
VALUES (1, 'Training & Placement Cell', 'TPO-001');

-- Default Seed Student (Akanshi)
INSERT IGNORE INTO users (user_id, name, email, password, role) 
VALUES (2, 'Akanshi Sharma', 'akanshi@skillora.edu', 'pass123', 'STUDENT');

INSERT IGNORE INTO students (student_id, roll_number, college, branch, semester, cgpa) 
VALUES (2, '23CS101', 'Chitkara University', 'Computer Science', 5, 8.75);
