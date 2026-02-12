-- Drop database if exists and create new one
DROP DATABASE IF EXISTS attendance_management;
CREATE DATABASE attendance_management;
USE attendance_management;

-- Users table
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Students table (simplified - no email/phone)
CREATE TABLE students (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(50) NOT NULL,
    semester INT NOT NULL,
    created_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL
);

-- Subjects table (simplified - no credits)
CREATE TABLE subjects (
    id INT PRIMARY KEY AUTO_INCREMENT,
    subject_code VARCHAR(20) UNIQUE NOT NULL,
    subject_name VARCHAR(100) NOT NULL,
    department VARCHAR(50) NOT NULL,
    semester INT NOT NULL,
    created_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL
);

-- Attendance table
CREATE TABLE attendance (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    subject_id INT NOT NULL,
    date DATE NOT NULL,
    status ENUM('PRESENT', 'ABSENT') NOT NULL,
    marked_by INT,
    marked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE,
    FOREIGN KEY (marked_by) REFERENCES users(id) ON DELETE SET NULL,
    UNIQUE KEY unique_attendance (student_id, subject_id, date)
);

-- Insert sample data for testing
INSERT INTO users (username, email, password, full_name) VALUES 
('admin', 'admin@example.com', 'admin123', 'System Administrator');

INSERT INTO students (student_id, name, department, semester, created_by) VALUES
('S001', 'John Doe', 'Computer Science', 5, 1),
('S002', 'Jane Smith', 'Computer Science', 5, 1),
('S003', 'Bob Johnson', 'Information Technology', 3, 1);

INSERT INTO subjects (subject_code, subject_name, department, semester, created_by) VALUES
('CS501', 'Database Management', 'Computer Science', 5, 1),
('CS502', 'Operating Systems', 'Computer Science', 5, 1),
('IT301', 'Web Development', 'Information Technology', 3, 1);
