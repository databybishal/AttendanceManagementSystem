package attendencemanagementsystem.dao;

import attendencemanagementsystem.database.DatabaseConnection;
import attendencemanagementsystem.models.Attendance;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {
    
    public boolean markOrUpdateAttendance(Attendance attendance, int userId) {
        if (isAttendanceExists(attendance.getStudentId(), attendance.getSubjectId(), attendance.getDate())) {
            return updateAttendance(attendance, userId);
        } else {
            return insertAttendance(attendance, userId);
        }
    }
    
    private boolean insertAttendance(Attendance attendance, int userId) {
        String sql = "INSERT INTO attendance (student_id, subject_id, date, status, marked_by) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, attendance.getStudentId());
            pstmt.setInt(2, attendance.getSubjectId());
            pstmt.setDate(3, new java.sql.Date(attendance.getDate().getTime()));
            pstmt.setString(4, attendance.getStatus());
            pstmt.setInt(5, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean updateAttendance(Attendance attendance, int userId) {
        String sql = "UPDATE attendance SET status = ?, marked_by = ? WHERE student_id = ? AND subject_id = ? AND date = ?";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, attendance.getStatus());
            pstmt.setInt(2, userId);
            pstmt.setInt(3, attendance.getStudentId());
            pstmt.setInt(4, attendance.getSubjectId());
            pstmt.setDate(5, new java.sql.Date(attendance.getDate().getTime()));
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean isAttendanceExists(int studentId, int subjectId, java.util.Date date) {
        String sql = "SELECT COUNT(*) FROM attendance WHERE student_id = ? AND subject_id = ? AND date = ?";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, subjectId);
            pstmt.setDate(3, new java.sql.Date(date.getTime()));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<Attendance> getAttendanceByStudent(int studentId) {
        List<Attendance> list = new ArrayList<>();
        String sql = "SELECT a.*, s.name as student_name, s.student_id as student_roll, " +
                     "sub.subject_name, sub.subject_code " +
                     "FROM attendance a " +
                     "JOIN students s ON a.student_id = s.id " +
                     "JOIN subjects sub ON a.subject_id = sub.id " +
                     "WHERE a.student_id = ? " +
                     "ORDER BY a.date DESC";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Attendance a = new Attendance();
                a.setId(rs.getInt("id"));
                a.setStudentId(rs.getInt("student_id"));
                a.setSubjectId(rs.getInt("subject_id"));
                a.setDate(rs.getDate("date"));
                a.setStatus(rs.getString("status"));
                a.setStudentName(rs.getString("student_name"));
                a.setStudentRoll(rs.getString("student_roll"));
                a.setSubjectName(rs.getString("subject_name"));
                a.setSubjectCode(rs.getString("subject_code"));
                list.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public double getAttendancePercentage(int studentId, int subjectId) {
        String sql = "SELECT " +
                     "SUM(CASE WHEN status = 'PRESENT' THEN 1 ELSE 0 END) * 100.0 / COUNT(*) as percentage " +
                     "FROM attendance WHERE student_id = ? AND subject_id = ?";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, subjectId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("percentage");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}