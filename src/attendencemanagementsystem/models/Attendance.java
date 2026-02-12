package attendencemanagementsystem.models;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Attendance {
    private int id;
    private int studentId;
    private int subjectId;
    private Date date;
    private String status;
    private String studentName;
    private String studentRoll;
    private String subjectName;
    private String subjectCode;
    
    public Attendance() {}
    
    public Attendance(int studentId, int subjectId, Date date, String status) {
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.date = date;
        this.status = status;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public int getSubjectId() { return subjectId; }
    public void setSubjectId(int subjectId) { this.subjectId = subjectId; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public String getFormattedDate() { 
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getStudentRoll() { return studentRoll; }
    public void setStudentRoll(String studentRoll) { this.studentRoll = studentRoll; }
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }
}