package attendencemanagementsystem.dao;

import attendencemanagementsystem.database.DatabaseConnection;
import attendencemanagementsystem.models.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    
    public boolean addStudent(Student student, int userId) {
        String sql = "INSERT INTO students (student_id, name, department, semester, created_by) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, student.getStudentId());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getDepartment());
            pstmt.setInt(4, student.getSemester());
            pstmt.setInt(5, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY student_id";
        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Student s = new Student();
                s.setId(rs.getInt("id"));
                s.setStudentId(rs.getString("student_id"));
                s.setName(rs.getString("name"));
                s.setDepartment(rs.getString("department"));
                s.setSemester(rs.getInt("semester"));
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public List<Student> getStudentsByDepartmentAndSemester(String department, int semester) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE department = ? AND semester = ? ORDER BY student_id";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, department);
            pstmt.setInt(2, semester);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Student s = new Student();
                s.setId(rs.getInt("id"));
                s.setStudentId(rs.getString("student_id"));
                s.setName(rs.getString("name"));
                s.setDepartment(rs.getString("department"));
                s.setSemester(rs.getInt("semester"));
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public boolean deleteStudent(int studentId) {
        String sql = "DELETE FROM students WHERE id = ?";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}