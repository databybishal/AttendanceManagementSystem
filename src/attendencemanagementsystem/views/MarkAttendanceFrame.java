package attendencemanagementsystem.views;

import attendencemanagementsystem.dao.*;
import attendencemanagementsystem.models.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MarkAttendanceFrame extends JPanel {
    private User currentUser;
    private SubjectDAO subjectDAO;
    private StudentDAO studentDAO;
    private AttendanceDAO attendanceDAO;
    private JComboBox<String> subjectBox;
    private JTextField dateField;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Student> currentStudents;
    private JLabel statusLabel;
    private List<Subject> subjectsList;
    
    public MarkAttendanceFrame(User user) {
        this.currentUser = user;
        this.subjectDAO = new SubjectDAO();
        this.studentDAO = new StudentDAO();
        this.attendanceDAO = new AttendanceDAO();
        
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        
        initComponents();
        loadSubjects();
    }
    
    private void initComponents() {
        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titleLabel = new JLabel("MARK ATTENDANCE");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titleLabel);
        
        statusLabel = new JLabel("Select a subject");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        headerPanel.add(statusLabel);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Control Panel
        JPanel controlPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        controlPanel.setBackground(new Color(240, 240, 240));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Subject
        controlPanel.add(new JLabel("Subject:"));
        subjectBox = new JComboBox<>();
        subjectBox.addActionListener(e -> loadStudents());
        controlPanel.add(subjectBox);
        
        // Date
        controlPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        dateField = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        controlPanel.add(dateField);
        
        // Buttons
        JButton loadBtn = new JButton("LOAD STUDENTS");
        loadBtn.addActionListener(e -> loadStudents());
        controlPanel.add(loadBtn);
        
        JButton saveBtn = new JButton("SAVE ATTENDANCE");
        saveBtn.setBackground(new Color(46, 204, 113));
        saveBtn.addActionListener(e -> saveAttendance());
        controlPanel.add(saveBtn);
        
        add(controlPanel, BorderLayout.CENTER);
        
        // Table
        String[] columns = {"Roll No", "Student Name", "Department", "Semester", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 4;
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(30);
        
        // Status ComboBox
        String[] statusOptions = {"ABSENT", "PRESENT"};
        JComboBox<String> statusCombo = new JComboBox<>(statusOptions);
        table.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(statusCombo));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Student List"));
        scrollPane.setPreferredSize(new Dimension(900, 350));
        
        add(scrollPane, BorderLayout.SOUTH);
    }
    
    private void loadSubjects() {
        subjectBox.removeAllItems();
        subjectsList = subjectDAO.getAllSubjects();
        
        if (subjectsList.isEmpty()) {
            subjectBox.addItem("-- No Subjects Available --");
            statusLabel.setText("No subjects found. Please add subjects first.");
        } else {
            for (Subject s : subjectsList) {
                subjectBox.addItem(s.getSubjectCode() + " - " + s.getSubjectName() + " (Sem " + s.getSemester() + ")");
            }
            statusLabel.setText(subjectsList.size() + " subjects loaded");
        }
    }
    
    private void loadStudents() {
        tableModel.setRowCount(0);
        
        int selectedIndex = subjectBox.getSelectedIndex();
        if (selectedIndex < 0 || subjectsList == null || subjectsList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a subject!");
            return;
        }
        
        Subject subject = subjectsList.get(selectedIndex);
        
        // Get students
        currentStudents = studentDAO.getStudentsByDepartmentAndSemester(
            subject.getDepartment(), subject.getSemester());
        
        if (currentStudents.isEmpty()) {
            statusLabel.setText("No students found for this subject");
            JOptionPane.showMessageDialog(this, 
                "No students in " + subject.getDepartment() + " - Semester " + subject.getSemester());
            return;
        }
        
        // Get selected date
        Date selectedDate;
        try {
            selectedDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateField.getText());
        } catch (Exception e) {
            selectedDate = new Date();
            dateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(selectedDate));
        }
        
        int presentCount = 0;
        
        for (Student s : currentStudents) {
            String status = "ABSENT";
            if (attendanceDAO.isAttendanceExists(s.getId(), subject.getId(), selectedDate)) {
                status = "PRESENT";
                presentCount++;
            }
            
            tableModel.addRow(new Object[]{
                s.getStudentId(),
                s.getName(),
                s.getDepartment(),
                "Sem " + s.getSemester(),
                status
            });
        }
        
        statusLabel.setText(String.format("Loaded: %d students | Present: %d | Absent: %d", 
            tableModel.getRowCount(), presentCount, tableModel.getRowCount() - presentCount));
    }
    
    private void saveAttendance() {
        int selectedIndex = subjectBox.getSelectedIndex();
        if (selectedIndex < 0 || subjectsList == null || subjectsList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a subject!");
            return;
        }
        
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No students to mark attendance!");
            return;
        }
        
        Subject subject = subjectsList.get(selectedIndex);
        
        Date selectedDate;
        try {
            selectedDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateField.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid date format! Use YYYY-MM-DD");
            return;
        }
        
        int saved = 0;
        int present = 0;
        int absent = 0;
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String rollNo = (String) tableModel.getValueAt(i, 0);
            String status = (String) tableModel.getValueAt(i, 4);
            
            if ("PRESENT".equals(status)) present++;
            else absent++;
            
            Student student = getStudentByRoll(rollNo);
            if (student != null) {
                Attendance attendance = new Attendance(
                    student.getId(), 
                    subject.getId(), 
                    selectedDate, 
                    status
                );
                
                if (attendanceDAO.markOrUpdateAttendance(attendance, currentUser.getId())) {
                    saved++;
                }
            }
        }
        
        JOptionPane.showMessageDialog(this,
            "Attendance Saved!\n\n" +
            "Present: " + present + "\n" +
            "Absent: " + absent + "\n" +
            "Total: " + tableModel.getRowCount() + "\n" +
            "Saved: " + saved + " records",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
        
        statusLabel.setText(String.format("Saved: %d/%d | Present: %d | Absent: %d", 
            saved, tableModel.getRowCount(), present, absent));
    }
    
    private Student getStudentByRoll(String rollNo) {
        if (currentStudents != null) {
            for (Student s : currentStudents) {
                if (s.getStudentId().equals(rollNo)) {
                    return s;
                }
            }
        }
        return null;
    }
    
    public void refreshData() {
        loadSubjects();
        tableModel.setRowCount(0);
        dateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }
}