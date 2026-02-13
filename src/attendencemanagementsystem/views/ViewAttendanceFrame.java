package attendencemanagementsystem.views;

import attendencemanagementsystem.dao.*;
import attendencemanagementsystem.models.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.text.SimpleDateFormat;

public class ViewAttendanceFrame extends JPanel {
    private AttendanceDAO attendanceDAO;
    private StudentDAO studentDAO;
    private SubjectDAO subjectDAO;
    
    private JComboBox<String> studentBox;
    private JComboBox<String> subjectBox;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel summaryLabel;
    private JLabel totalStudentsLabel;
    private JLabel totalSubjectsLabel;
    private JLabel totalRecordsLabel;
    private JLabel overallPercentLabel;
    
    private List<Student> studentsList;
    private List<Subject> subjectsList;
    
    public ViewAttendanceFrame() {
        this.attendanceDAO = new AttendanceDAO();
        this.studentDAO = new StudentDAO();
        this.subjectDAO = new SubjectDAO();
        
        setBackground(Color.WHITE);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        initComponents();
        refreshData();
    }
    
    private void initComponents() {
        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("ATTENDANCE REPORTS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titleLabel);
        
        JLabel dateLabel = new JLabel(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        headerPanel.add(dateLabel);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Center Panel - Split into left and right
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setBackground(Color.WHITE);
        
        // LEFT PANEL - Filters and Student Stats
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBackground(Color.WHITE);
        
        // Filter Panel
        JPanel filterPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        filterPanel.setBackground(new Color(240, 240, 240));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filter Attendance"));
        
        filterPanel.add(new JLabel("Student:"));
        studentBox = new JComboBox<>();
        studentBox.addActionListener(e -> loadAttendance());
        filterPanel.add(studentBox);
        
        filterPanel.add(new JLabel("Subject:"));
        subjectBox = new JComboBox<>();
        subjectBox.addActionListener(e -> loadAttendance());
        filterPanel.add(subjectBox);
        
        JButton refreshBtn = new JButton("REFRESH");
        refreshBtn.addActionListener(e -> refreshData());
        filterPanel.add(refreshBtn);
        
        JButton clearBtn = new JButton("CLEAR");
        clearBtn.addActionListener(e -> {
            studentBox.setSelectedIndex(0);
            subjectBox.setSelectedIndex(0);
        });
        filterPanel.add(clearBtn);
        
        leftPanel.add(filterPanel, BorderLayout.NORTH);
        
        // Student Stats Panel
        JPanel statsPanel = new JPanel(new GridLayout(8, 2, 10, 5));
        statsPanel.setBackground(new Color(240, 240, 240));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Student Statistics"));
        
        statsPanel.add(new JLabel("Roll No:"));
        JLabel rollNoVal = new JLabel("-");
        rollNoVal.setFont(new Font("Arial", Font.BOLD, 12));
        statsPanel.add(rollNoVal);
        
        statsPanel.add(new JLabel("Name:"));
        JLabel nameVal = new JLabel("-");
        nameVal.setFont(new Font("Arial", Font.BOLD, 12));
        statsPanel.add(nameVal);
        
        statsPanel.add(new JLabel("Department:"));
        JLabel deptVal = new JLabel("-");
        deptVal.setFont(new Font("Arial", Font.BOLD, 12));
        statsPanel.add(deptVal);
        
        statsPanel.add(new JLabel("Semester:"));
        JLabel semVal = new JLabel("-");
        semVal.setFont(new Font("Arial", Font.BOLD, 12));
        statsPanel.add(semVal);
        
        statsPanel.add(new JLabel("Present:"));
        JLabel presentVal = new JLabel("0");
        presentVal.setForeground(new Color(46, 204, 113));
        presentVal.setFont(new Font("Arial", Font.BOLD, 14));
        statsPanel.add(presentVal);
        
        statsPanel.add(new JLabel("Absent:"));
        JLabel absentVal = new JLabel("0");
        absentVal.setForeground(new Color(231, 76, 60));
        absentVal.setFont(new Font("Arial", Font.BOLD, 14));
        statsPanel.add(absentVal);
        
        statsPanel.add(new JLabel("Total Classes:"));
        JLabel totalVal = new JLabel("0");
        totalVal.setFont(new Font("Arial", Font.BOLD, 14));
        statsPanel.add(totalVal);
        
        statsPanel.add(new JLabel("Percentage:"));
        JLabel percentVal = new JLabel("0%");
        percentVal.setFont(new Font("Arial", Font.BOLD, 16));
        statsPanel.add(percentVal);
        
        leftPanel.add(statsPanel, BorderLayout.CENTER);
        
        // RIGHT PANEL - Overall Statistics
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBackground(Color.WHITE);
        
        JPanel overallPanel = new JPanel(new GridLayout(5, 2, 10, 15));
        overallPanel.setBackground(new Color(240, 240, 240));
        overallPanel.setBorder(BorderFactory.createTitledBorder("Overall Statistics"));
        
        overallPanel.add(new JLabel("Total Students:"));
        totalStudentsLabel = new JLabel("0");
        totalStudentsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        overallPanel.add(totalStudentsLabel);
        
        overallPanel.add(new JLabel("Total Subjects:"));
        totalSubjectsLabel = new JLabel("0");
        totalSubjectsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        overallPanel.add(totalSubjectsLabel);
        
        overallPanel.add(new JLabel("Total Records:"));
        totalRecordsLabel = new JLabel("0");
        totalRecordsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        overallPanel.add(totalRecordsLabel);
        
        overallPanel.add(new JLabel("Overall Attendance:"));
        overallPercentLabel = new JLabel("0%");
        overallPercentLabel.setFont(new Font("Arial", Font.BOLD, 18));
        overallPanel.add(overallPercentLabel);
        
        // Simple Chart Panel (just text representation)
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawSimpleChart(g);
            }
        };
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createTitledBorder("Attendance Distribution"));
        chartPanel.setPreferredSize(new Dimension(300, 200));
        
        rightPanel.add(overallPanel, BorderLayout.NORTH);
        rightPanel.add(chartPanel, BorderLayout.CENTER);
        
        centerPanel.add(leftPanel);
        centerPanel.add(rightPanel);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Table Panel
        String[] columns = {"Date", "Subject Code", "Subject Name", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(3).setCellRenderer(new StatusRenderer());
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Attendance Records"));
        scrollPane.setPreferredSize(new Dimension(900, 200));
        
        summaryLabel = new JLabel("Select a student to view attendance details");
        summaryLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(scrollPane, BorderLayout.CENTER);
        southPanel.add(summaryLabel, BorderLayout.SOUTH);
        
        add(southPanel, BorderLayout.SOUTH);
        
        // Store references for updating stats
        this.rollNoVal = rollNoVal;
        this.nameVal = nameVal;
        this.deptVal = deptVal;
        this.semVal = semVal;
        this.presentVal = presentVal;
        this.absentVal = absentVal;
        this.totalVal = totalVal;
        this.percentVal = percentVal;
    }
    
    // References for updating stats
    private JLabel rollNoVal, nameVal, deptVal, semVal, presentVal, absentVal, totalVal, percentVal;
    
    private void drawSimpleChart(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        int width = getWidth() - 60;
        int height = 30;
        int x = 30;
        int y = 50;
        
        // Get selected student
        int studentIndex = studentBox.getSelectedIndex() - 1;
        int subjectIndex = subjectBox.getSelectedIndex() - 1;
        
        int total = 0;
        int present = 0;
        
        if (studentIndex >= 0 && studentIndex < studentsList.size()) {
            Student student = studentsList.get(studentIndex);
            List<Attendance> list = attendanceDAO.getAttendanceByStudent(student.getId());
            
            if (subjectIndex >= 0 && subjectIndex < subjectsList.size()) {
                Subject subject = subjectsList.get(subjectIndex);
                list.removeIf(a -> a.getSubjectId() != subject.getId());
            }
            
            total = list.size();
            present = (int) list.stream().filter(a -> "PRESENT".equals(a.getStatus())).count();
        }
        
        int absent = total - present;
        
        // Draw title
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("Present vs Absent", x, y - 10);
        
        if (total == 0) {
            g2d.drawString("No data", x, y + 50);
            return;
        }
        
        // Draw present bar
        int presentWidth = (int) ((present * 1.0 / total) * width);
        g2d.setColor(new Color(46, 204, 113));
        g2d.fillRect(x, y, presentWidth, height);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Present: " + present, x + 5, y + 20);
        
        // Draw absent bar
        int absentWidth = (int) ((absent * 1.0 / total) * width);
        g2d.setColor(new Color(231, 76, 60));
        g2d.fillRect(x + presentWidth, y, absentWidth, height);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Absent: " + absent, x + presentWidth + 5, y + 20);
        
        // Draw percentage
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString(String.format("%.1f%%", (present * 100.0 / total)), x + width/2 - 30, y + height + 30);
    }
    
    // Status Renderer
    class StatusRenderer extends JLabel implements TableCellRenderer {
        public StatusRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable t, Object v,
                boolean s, boolean f, int r, int c) {
            String status = (String) v;
            setText(status);
            
            if ("PRESENT".equals(status)) {
                setBackground(new Color(212, 237, 218));
                setForeground(new Color(30, 132, 73));
            } else {
                setBackground(new Color(248, 215, 218));
                setForeground(new Color(157, 38, 50));
            }
            
            return this;
        }
    }
    
    private void loadAttendance() {
        tableModel.setRowCount(0);
        
        int studentIndex = studentBox.getSelectedIndex() - 1;
        if (studentIndex < 0 || studentIndex >= studentsList.size()) {
            summaryLabel.setText("Select a student to view attendance");
            resetStudentStats();
            repaint();
            return;
        }
        
        Student student = studentsList.get(studentIndex);
        
        int subjectIndex = subjectBox.getSelectedIndex() - 1;
        Subject subject = null;
        if (subjectIndex >= 0 && subjectIndex < subjectsList.size()) {
            subject = subjectsList.get(subjectIndex);
        }
        
        // Get attendance records
        List<Attendance> list = attendanceDAO.getAttendanceByStudent(student.getId());
        
        if (subject != null) {
            list.removeIf(a -> a.getSubjectId() != subject.getId());
        }
        
        // Sort by date (newest first)
        list.sort((a, b) -> b.getDate().compareTo(a.getDate()));
        
        for (Attendance a : list) {
            tableModel.addRow(new Object[]{
                a.getFormattedDate(),
                a.getSubjectCode(),
                a.getSubjectName(),
                a.getStatus()
            });
        }
        
        // Calculate stats
        int total = list.size();
        int present = (int) list.stream().filter(a -> "PRESENT".equals(a.getStatus())).count();
        int absent = total - present;
        double percentage = total > 0 ? (present * 100.0 / total) : 0;
        
        // Update student stats
        updateStudentStats(student, total, present, absent, percentage);
        
        // Update summary
        if (subject != null) {
            summaryLabel.setText(String.format("%s (%s) - %s: %.1f%% (%d/%d)", 
                student.getName(), student.getStudentId(), subject.getSubjectName(), percentage, present, total));
        } else {
            summaryLabel.setText(String.format("%s (%s): %.1f%% (%d/%d)", 
                student.getName(), student.getStudentId(), percentage, present, total));
        }
        
        repaint();
    }
    
    private void updateStudentStats(Student student, int total, int present, int absent, double percentage) {
        rollNoVal.setText(student.getStudentId());
        nameVal.setText(student.getName());
        deptVal.setText(student.getDepartment());
        semVal.setText("Sem " + student.getSemester());
        presentVal.setText(String.valueOf(present));
        absentVal.setText(String.valueOf(absent));
        totalVal.setText(String.valueOf(total));
        percentVal.setText(String.format("%.1f%%", percentage));
        
        // Color code percentage
        if (percentage >= 75) {
            percentVal.setForeground(new Color(46, 204, 113));
        } else if (percentage >= 60) {
            percentVal.setForeground(new Color(241, 196, 15));
        } else {
            percentVal.setForeground(new Color(231, 76, 60));
        }
    }
    
    private void resetStudentStats() {
        rollNoVal.setText("-");
        nameVal.setText("-");
        deptVal.setText("-");
        semVal.setText("-");
        presentVal.setText("0");
        absentVal.setText("0");
        totalVal.setText("0");
        percentVal.setText("0%");
        percentVal.setForeground(Color.BLACK);
    }
    
    private void loadOverallStatistics() {
        // Get counts
        int studentCount = studentDAO.getAllStudents().size();
        int subjectCount = subjectDAO.getAllSubjects().size();
        
        // Calculate overall attendance
        int totalRecords = 0;
        int totalPresent = 0;
        
        for (Student s : studentDAO.getAllStudents()) {
            List<Attendance> list = attendanceDAO.getAttendanceByStudent(s.getId());
            totalRecords += list.size();
            totalPresent += list.stream().filter(a -> "PRESENT".equals(a.getStatus())).count();
        }
        
        double percentage = totalRecords > 0 ? (totalPresent * 100.0 / totalRecords) : 0;
        
        // Update labels
        totalStudentsLabel.setText(String.valueOf(studentCount));
        totalSubjectsLabel.setText(String.valueOf(subjectCount));
        totalRecordsLabel.setText(String.valueOf(totalRecords));
        overallPercentLabel.setText(String.format("%.1f%%", percentage));
        
        // Color code overall percentage
        if (percentage >= 75) {
            overallPercentLabel.setForeground(new Color(46, 204, 113));
        } else if (percentage >= 60) {
            overallPercentLabel.setForeground(new Color(241, 196, 15));
        } else {
            overallPercentLabel.setForeground(new Color(231, 76, 60));
        }
    }
    
    public void refreshData() {
        // Load students
        studentBox.removeAllItems();
        studentBox.addItem("-- Select Student --");
        
        studentsList = studentDAO.getAllStudents();
        for (Student s : studentsList) {
            studentBox.addItem(s.getStudentId() + " - " + s.getName());
        }
        
        // Load subjects
        subjectBox.removeAllItems();
        subjectBox.addItem("-- All Subjects --");
        
        subjectsList = subjectDAO.getAllSubjects();
        for (Subject s : subjectsList) {
            subjectBox.addItem(s.getSubjectCode() + " - " + s.getSubjectName());
        }
        
        // Reset stats
        resetStudentStats();
        tableModel.setRowCount(0);
        summaryLabel.setText("Select a student to view attendance details");
        
        // Load overall stats
        loadOverallStatistics();
        
        repaint();
    }
    
    public void refreshView() {
        refreshData();
    }
}