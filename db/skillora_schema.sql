-- ==============================================================================
-- Skillora Database Schema & Evaluation Demo Seed Data
-- Course: Programming in Java (25CSE0202)
-- Project: Skillora - Career, Internship & Student Networking Platform
-- ==============================================================================

CREATE DATABASE IF NOT EXISTS skillora_db;
USE skillora_db;

-- 1. Users Table (Base entity for authentication)
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('STUDENT', 'ADMIN') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Students Table (Child entity extending Users)
CREATE TABLE IF NOT EXISTS students (
    student_id INT PRIMARY KEY,
    roll_number VARCHAR(50) NOT NULL UNIQUE,
    college VARCHAR(150) NOT NULL,
    branch VARCHAR(100) NOT NULL,
    semester INT NOT NULL,
    cgpa DECIMAL(3,2) NOT NULL,
    FOREIGN KEY (student_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 3. Admins Table (Child entity extending Users)
CREATE TABLE IF NOT EXISTS admins (
    admin_id INT PRIMARY KEY,
    department VARCHAR(100) NOT NULL,
    staff_id VARCHAR(50) NOT NULL UNIQUE,
    FOREIGN KEY (admin_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 4. Skills Catalog Table
CREATE TABLE IF NOT EXISTS skills (
    skill_id INT AUTO_INCREMENT PRIMARY KEY,
    skill_name VARCHAR(100) NOT NULL UNIQUE,
    category VARCHAR(100) NOT NULL
);

-- 5. Student-Skills Mapping (Many-to-Many with proficiency)
CREATE TABLE IF NOT EXISTS student_skills (
    student_id INT NOT NULL,
    skill_id INT NOT NULL,
    proficiency_level ENUM('Beginner', 'Intermediate', 'Advanced') DEFAULT 'Beginner',
    PRIMARY KEY (student_id, skill_id),
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(skill_id) ON DELETE CASCADE
);

-- 6. Companies Table
CREATE TABLE IF NOT EXISTS companies (
    company_id INT AUTO_INCREMENT PRIMARY KEY,
    company_name VARCHAR(150) NOT NULL UNIQUE,
    industry VARCHAR(100) NOT NULL,
    website VARCHAR(150),
    contact_email VARCHAR(100)
);

-- 7. Company Visits Table (Placement Calendar)
CREATE TABLE IF NOT EXISTS company_visits (
    visit_id INT AUTO_INCREMENT PRIMARY KEY,
    company_id INT NOT NULL,
    college_name VARCHAR(150) NOT NULL,
    visit_date DATE NOT NULL,
    drive_type ENUM('On-Campus', 'Pool-Campus', 'Virtual') DEFAULT 'On-Campus',
    eligible_branches VARCHAR(255) NOT NULL,
    FOREIGN KEY (company_id) REFERENCES companies(company_id) ON DELETE CASCADE
);

-- 8. Internships Table (Opportunities with eligibility gates)
CREATE TABLE IF NOT EXISTS internships (
    internship_id INT AUTO_INCREMENT PRIMARY KEY,
    company_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    stipend DECIMAL(10,2) NOT NULL,
    duration_months INT NOT NULL,
    location VARCHAR(100) NOT NULL,
    min_cgpa DECIMAL(3,2) DEFAULT 0.00,
    eligible_branches VARCHAR(255) NOT NULL,
    min_semester INT DEFAULT 1,
    required_skills VARCHAR(255),
    deadline DATE NOT NULL,
    status ENUM('OPEN', 'CLOSED') DEFAULT 'OPEN',
    FOREIGN KEY (company_id) REFERENCES companies(company_id) ON DELETE CASCADE
);

-- 9. Applications Table (Student applications with duplicate prevention)
CREATE TABLE IF NOT EXISTS applications (
    application_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    internship_id INT NOT NULL,
    application_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('APPLIED', 'SHORTLISTED', 'REJECTED') DEFAULT 'APPLIED',
    remarks VARCHAR(255),
    UNIQUE KEY unique_application (student_id, internship_id),
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (internship_id) REFERENCES internships(internship_id) ON DELETE CASCADE
);

-- 10. Hackathons Table
CREATE TABLE IF NOT EXISTS hackathons (
    hackathon_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    host_college VARCHAR(150) NOT NULL,
    event_date DATE NOT NULL,
    domain VARCHAR(100) NOT NULL,
    location VARCHAR(100) NOT NULL,
    team_size_limit INT DEFAULT 4,
    registration_deadline DATE NOT NULL
);

-- ==============================================================================
-- Seed Data for Evaluation Demo
-- ==============================================================================

-- Default Administrator
INSERT IGNORE INTO users (user_id, name, email, password, role) 
VALUES (1, 'Dr. Sharma (Placement Officer)', 'admin@skillora.edu', 'admin123', 'ADMIN');

INSERT IGNORE INTO admins (admin_id, department, staff_id) 
VALUES (1, 'Training & Placement Cell', 'TPO-001');

-- Default Seed Students
INSERT IGNORE INTO users (user_id, name, email, password, role) VALUES 
(2, 'Akanshi Sharma', 'akanshi@skillora.edu', 'pass123', 'STUDENT'),
(3, 'Mridul Verma', 'mridul@skillora.edu', 'pass123', 'STUDENT'),
(4, 'Navdeep Singh', 'navdeep@skillora.edu', 'pass123', 'STUDENT'),
(5, 'Paras Malhotra', 'paras@skillora.edu', 'pass123', 'STUDENT');

INSERT IGNORE INTO students (student_id, roll_number, college, branch, semester, cgpa) VALUES 
(2, '23CS101', 'Chitkara University', 'Computer Science', 5, 8.75),
(3, '23CS104', 'Chitkara University', 'Computer Science', 5, 8.20),
(4, '23CS109', 'Chitkara University', 'Computer Science', 5, 8.50),
(5, '23IT205', 'Chitkara University', 'Information Technology', 5, 7.90);

-- Skills Catalog
INSERT IGNORE INTO skills (skill_id, skill_name, category) VALUES
(1, 'Java', 'Programming'),
(2, 'Python', 'Programming'),
(3, 'MySQL', 'Database'),
(4, 'React', 'Frontend'),
(5, 'Spring Boot', 'Backend'),
(6, 'Machine Learning', 'AI/Data Science'),
(7, 'Flutter', 'Mobile Development');

-- Assign Skills to Seed Students
INSERT IGNORE INTO student_skills (student_id, skill_id, proficiency_level) VALUES
(2, 1, 'Advanced'),
(2, 3, 'Intermediate'),
(3, 1, 'Intermediate'),
(3, 2, 'Advanced'),
(4, 1, 'Advanced'),
(4, 4, 'Intermediate'),
(5, 1, 'Intermediate'),
(5, 7, 'Advanced');

-- Sample Companies
INSERT IGNORE INTO companies (company_id, company_name, industry, website, contact_email) VALUES
(1, 'Google India', 'Technology', 'https://careers.google.com', 'campus@google.com'),
(2, 'Infosys', 'IT Services', 'https://infosys.com', 'careers@infosys.com'),
(3, 'Microsoft IDC', 'Software Development', 'https://careers.microsoft.com', 'recruit@microsoft.com');

-- Sample Company Visits
INSERT IGNORE INTO company_visits (visit_id, company_id, college_name, visit_date, drive_type, eligible_branches) VALUES
(1, 1, 'Chitkara University', '2026-12-05', 'On-Campus', 'Computer Science, Information Technology'),
(2, 2, 'Chitkara University', '2026-12-12', 'On-Campus', 'Computer Science, Information Technology, Electronics'),
(3, 3, 'Chitkara University', '2026-12-20', 'Virtual', 'Computer Science, Information Technology');

-- Sample Internships
INSERT IGNORE INTO internships (internship_id, company_id, title, description, stipend, duration_months, location, min_cgpa, eligible_branches, min_semester, required_skills, deadline) VALUES
(101, 1, 'Software Engineer Intern', 'Develop scalable backend microservices and REST APIs.', 50000.00, 6, 'Bangalore', 8.00, 'Computer Science, Information Technology', 5, 'Java, MySQL', '2026-12-25'),
(102, 2, 'Systems Trainee Intern', 'Enterprise application maintenance, debugging, and testing.', 25000.00, 3, 'Chandigarh', 7.00, 'Computer Science, Information Technology, Electronics', 4, 'Java', '2026-12-20'),
(103, 3, 'Cloud Solutions Intern', 'Assist with Azure deployment pipelines and monitoring.', 45000.00, 6, 'Hyderabad', 8.25, 'Computer Science, Information Technology', 5, 'Python, Cloud', '2026-12-30');

-- Sample Hackathons
INSERT IGNORE INTO hackathons (hackathon_id, title, host_college, event_date, domain, location, team_size_limit, registration_deadline) VALUES
(1, 'Smart India Innovation Hackathon', 'IIT Delhi', '2026-12-18', 'FinTech & Web3', 'New Delhi', 4, '2026-12-10'),
(2, 'HackPulse 2026', 'Chitkara University', '2026-12-22', 'Healthcare & AI', 'Chandigarh', 4, '2026-12-15'),
(3, 'CodeQuest National Challenge', 'BITS Pilani', '2027-01-10', 'EdTech & Smart Cities', 'Pilani', 3, '2026-12-28');
