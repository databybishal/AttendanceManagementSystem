package attendencemanagementsystem.views;

import attendencemanagementsystem.dao.*;
import attendencemanagementsystem.models.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.text.SimpleDateFormat;
import javax.swing.border.TitledBorder;

public class ViewAttendanceFrame extends JPanel {
    private AttendanceDAO attendanceDAO;
    private StudentDAO studentDAO;
    private SubjectDAO subjectDAO;
    
    private JComboBox<Student> studentBox;
    private JComboBox<Subject> subjectBox;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel summaryLabel;
    private JLabel percentageLabel;
    private JLabel totalClassesLabel;
    private JLabel presentLabel;
    private JLabel absentLabel;
    private JPanel statsPanel;
    private JPanel chartPanel;
    
    // Color constants
    private final Color PRESENT_COLOR = new Color(46, 204, 113);
    private final Color ABSENT_COLOR = new Color(231, 76, 60);
    private final Color OVERALL_COLOR = new Color(52, 152, 219);
    private final Color WARNING_COLOR = new Color(241, 196, 15);
    
    public ViewAttendanceFrame() {
        this.attendanceDAO = new AttendanceDAO();
        this.studentDAO = new StudentDAO();
        this.subjectDAO = new SubjectDAO();
        setBackground(Color.WHITE);
        setLayout(new BorderLayout(0, 20));
        initComponents();
        refreshData();
        loadOverallStatistics(); // Load overall stats
    }
    
    private void initComponents() {
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("📈 ATTENDANCE REPORTS & ANALYTICS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JLabel dateLabel = new JLabel(new SimpleDateFormat("EEEE, MMMM d, yyyy").format(new Date()));
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dateLabel.setForeground(new Color(100, 100, 100));
        headerPanel.add(dateLabel, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Main Content - Split into two columns
        JPanel mainContent = new JPanel(new GridLayout(1, 2, 20, 0));
        mainContent.setBackground(Color.WHITE);
        
        // LEFT COLUMN - Filters and Student Stats
        JPanel leftColumn = new JPanel(new BorderLayout(0, 15));
        leftColumn.setBackground(Color.WHITE);
        
        // Filter Panel
        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setBackground(new Color(249, 249, 249));
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        
        // Filter Title
        JLabel filterTitle = new JLabel("🔍 FILTER ATTENDANCE");
        filterTitle.setFont(new Font("Arial", Font.BOLD, 16));
        filterTitle.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        filterPanel.add(filterTitle, gbc);
        
        // Separator
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(200, 200, 200));
        gbc.gridy = 1;
        filterPanel.add(sep, gbc);
        
        // Student Selection
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        filterPanel.add(new JLabel("👤 Student:"), gbc);
        gbc.gridx = 1;
        studentBox = new JComboBox<>();
        studentBox.setPreferredSize(new Dimension(200, 35));
        studentBox.setFont(new Font("Arial", Font.PLAIN, 13));
        studentBox.setBackground(Color.WHITE);
        studentBox.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        studentBox.addActionListener(e -> loadStudentAttendance());
        filterPanel.add(studentBox, gbc);
        
        // Subject Selection
        gbc.gridy = 3;
        gbc.gridx = 0;
        filterPanel.add(new JLabel("📚 Subject:"), gbc);
        gbc.gridx = 1;
        subjectBox = new JComboBox<>();
        subjectBox.setPreferredSize(new Dimension(200, 35));
        subjectBox.setFont(new Font("Arial", Font.PLAIN, 13));
        subjectBox.setBackground(Color.WHITE);
        subjectBox.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        subjectBox.addActionListener(e -> loadStudentAttendance());
        filterPanel.add(subjectBox, gbc);
        
        // Refresh Button
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 8, 8, 8);
        JButton refreshBtn = new JButton("🔄 REFRESH DATA");
        refreshBtn.setFont(new Font("Arial", Font.BOLD, 13));
        refreshBtn.setBackground(new Color(52, 152, 219));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> {
            refreshData();
            loadStudentAttendance();
            loadOverallStatistics();
        });
        filterPanel.add(refreshBtn, gbc);
        
        leftColumn.add(filterPanel, BorderLayout.NORTH);
        
        // Student Statistics Panel
        statsPanel = new JPanel();
        statsPanel.setBackground(new Color(249, 249, 249));
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        statsPanel.setLayout(new GridBagLayout());
        leftColumn.add(statsPanel, BorderLayout.CENTER);
        
        // RIGHT COLUMN - Overall Statistics and Chart
        JPanel rightColumn = new JPanel(new BorderLayout(0, 15));
        rightColumn.setBackground(Color.WHITE);
        
        // Overall Statistics Panel
        JPanel overallPanel = new JPanel(new GridBagLayout());
        overallPanel.setBackground(new Color(249, 249, 249));
        overallPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints ogbc = new GridBagConstraints();
        ogbc.insets = new Insets(8, 8, 8, 8);
        ogbc.fill = GridBagConstraints.HORIZONTAL;
        ogbc.gridwidth = 2;
        
        JLabel overallTitle = new JLabel("📊 OVERALL STATISTICS");
        overallTitle.setFont(new Font("Arial", Font.BOLD, 16));
        overallTitle.setForeground(new Color(44, 62, 80));
        ogbc.gridx = 0;
        ogbc.gridy = 0;
        overallPanel.add(overallTitle, ogbc);
        
        JSeparator overallSep = new JSeparator();
        overallSep.setForeground(new Color(200, 200, 200));
        ogbc.gridy = 1;
        overallPanel.add(overallSep, ogbc);
        
        // Total Students
        ogbc.gridwidth = 1;
        ogbc.gridy = 2;
        ogbc.gridx = 0;
        overallPanel.add(new JLabel("👥 Total Students:"), ogbc);
        ogbc.gridx = 1;
        JLabel totalStudentsVal = new JLabel("0");
        totalStudentsVal.setFont(new Font("Arial", Font.BOLD, 14));
        totalStudentsVal.setForeground(OVERALL_COLOR);
        overallPanel.add(totalStudentsVal, ogbc);
        
        // Total Subjects
        ogbc.gridy = 3;
        ogbc.gridx = 0;
        overallPanel.add(new JLabel("📚 Total Subjects:"), ogbc);
        ogbc.gridx = 1;
        JLabel totalSubjectsVal = new JLabel("0");
        totalSubjectsVal.setFont(new Font("Arial", Font.BOLD, 14));
        totalSubjectsVal.setForeground(OVERALL_COLOR);
        overallPanel.add(totalSubjectsVal, ogbc);
        
        // Total Attendance Records
        ogbc.gridy = 4;
        ogbc.gridx = 0;
        overallPanel.add(new JLabel("📋 Total Records:"), ogbc);
        ogbc.gridx = 1;
        totalClassesLabel = new JLabel("0");
        totalClassesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalClassesLabel.setForeground(OVERALL_COLOR);
        overallPanel.add(totalClassesLabel, ogbc);
        
        // Overall Attendance Percentage
        ogbc.gridy = 5;
        ogbc.gridx = 0;
        overallPanel.add(new JLabel("📈 Overall Attendance:"), ogbc);
        ogbc.gridx = 1;
        percentageLabel = new JLabel("0%");
        percentageLabel.setFont(new Font("Arial", Font.BOLD, 18));
        percentageLabel.setForeground(OVERALL_COLOR);
        overallPanel.add(percentageLabel, ogbc);
        
        rightColumn.add(overallPanel, BorderLayout.NORTH);
        
        // Chart Panel
        chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawAttendanceChart(g);
            }
        };
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        chartPanel.setPreferredSize(new Dimension(400, 250));
        rightColumn.add(chartPanel, BorderLayout.CENTER);
        
        mainContent.add(leftColumn);
        mainContent.add(rightColumn);
        
        add(mainContent, BorderLayout.CENTER);
        
        // Attendance Records Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                "📋 ATTENDANCE RECORDS",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                new Color(44, 62, 80)
            ),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        String[] columns = {"Date", "Subject Code", "Subject Name", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(52, 152, 219));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(52, 152, 219, 50));
        table.getColumnModel().getColumn(3).setCellRenderer(new StatusRenderer());
        
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scrollPane.setPreferredSize(new Dimension(900, 200));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Summary Label
        summaryLabel = new JLabel("Select a student to view attendance details");
        summaryLabel.setFont(new Font("Arial", Font.ITALIC, 13));
        summaryLabel.setForeground(new Color(100, 100, 100));
        summaryLabel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
        tablePanel.add(summaryLabel, BorderLayout.SOUTH);
        
        add(tablePanel, BorderLayout.SOUTH);
        
        // Initialize stat labels
        presentLabel = new JLabel("0");
        absentLabel = new JLabel("0");
    }
    
    // Draw attendance chart
    private void drawAttendanceChart(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = chartPanel.getWidth() - 40;
        int height = chartPanel.getHeight() - 60;
        int x = 30;
        int y = 30;
        
        // Get data
        Student selectedStudent = (Student) studentBox.getSelectedItem();
        Subject selectedSubject = (Subject) subjectBox.getSelectedItem();
        
        int total = 0;
        int present = 0;
        
        if (selectedStudent != null) {
            List<Attendance> list = attendanceDAO.getAttendanceByStudent(selectedStudent.getId());
            if (selectedSubject != null) {
                list.removeIf(a -> a.getSubjectId() != selectedSubject.getId());
            }
            total = list.size();
            present = (int) list.stream().filter(a -> "PRESENT".equals(a.getStatus())).count();
        }
        
        int absent = total - present;
        
        // Draw title
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.setColor(new Color(44, 62, 80));
        g2d.drawString("Attendance Distribution", x, y - 10);
        
        if (total == 0) {
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.setColor(Color.GRAY);
            g2d.drawString("No data available", x + 50, y + 80);
            return;
        }
        
        // Draw pie chart
        int startAngle = 0;
        int presentAngle = (int) (present * 360.0 / total);
        int absentAngle = (int) (absent * 360.0 / total);
        
        // Draw present slice
        g2d.setColor(PRESENT_COLOR);
        g2d.fillArc(x, y, width, height, startAngle, presentAngle);
        
        // Draw absent slice
        g2d.setColor(ABSENT_COLOR);
        g2d.fillArc(x, y, width, height, startAngle + presentAngle, absentAngle);
        
        // Draw legend
        int legendX = x + width + 20;
        int legendY = y;
        
        g2d.setColor(PRESENT_COLOR);
        g2d.fillRect(legendX, legendY, 15, 15);
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.drawString("Present: " + present + " (" + String.format("%.1f", (present * 100.0 / total)) + "%)", 
                      legendX + 20, legendY + 12);
        
        g2d.setColor(ABSENT_COLOR);
        g2d.fillRect(legendX, legendY + 25, 15, 15);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Absent: " + absent + " (" + String.format("%.1f", (absent * 100.0 / total)) + "%)", 
                      legendX + 20, legendY + 37);
    }
    
    // Status Renderer Class
    class StatusRenderer extends JLabel implements TableCellRenderer {
        public StatusRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setFont(new Font("Arial", Font.BOLD, 12));
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable t, Object v,
                boolean s, boolean f, int r, int c) {
            String status = (String) v;
            setText(status);
            
            if ("PRESENT".equals(status)) {
                setBackground(new Color(212, 237, 218));
                setForeground(new Color(30, 132, 73));
                setBorder(BorderFactory.createLineBorder(PRESENT_COLOR, 1));
            } else {
                setBackground(new Color(248, 215, 218));
                setForeground(new Color(157, 38, 50));
                setBorder(BorderFactory.createLineBorder(ABSENT_COLOR, 1));
            }
            
            if (s) {
                setBackground(getBackground().darker());
            }
            
            return this;
        }
    }
    
    // Load overall statistics
    private void loadOverallStatistics() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private int studentCount = 0;
            private int subjectCount = 0;
            private int totalRecords = 0;
            private int presentCount = 0;
            private double percentage = 0;
            
            @Override
            protected Void doInBackground() {
                studentCount = studentDAO.getAllStudents().size();
                subjectCount = subjectDAO.getAllSubjects().size();
                
                // Calculate overall attendance
                List<Attendance> allAttendance = new ArrayList<>();
                for (Student s : studentDAO.getAllStudents()) {
                    List<Attendance> list = attendanceDAO.getAttendanceByStudent(s.getId());
                    allAttendance.addAll(list);
                    presentCount += list.stream().filter(a -> "PRESENT".equals(a.getStatus())).count();
                }
                
                totalRecords = allAttendance.size();
                if (totalRecords > 0) {
                    percentage = (presentCount * 100.0) / totalRecords;
                }
                
                return null;
            }
            
            @Override
            protected void done() {
                // Update UI
                Container parent = getParent();
                if (parent != null) {
                    Component[] components = parent.getComponents();
                    for (Component c : components) {
                        if (c instanceof JPanel) {
                            updateOverallStats((JPanel) c, studentCount, subjectCount, totalRecords, percentage);
                        }
                    }
                }
                
                totalClassesLabel.setText(String.valueOf(totalRecords));
                percentageLabel.setText(String.format("%.1f%%", percentage));
                
                // Color code percentage
                if (percentage >= 75) {
                    percentageLabel.setForeground(PRESENT_COLOR);
                } else if (percentage >= 60) {
                    percentageLabel.setForeground(WARNING_COLOR);
                } else {
                    percentageLabel.setForeground(ABSENT_COLOR);
                }
            }
        };
        
        worker.execute();
    }
    
    private void updateOverallStats(JPanel panel, int students, int subjects, int records, double percentage) {
        Component[] components = panel.getComponents();
        for (Component c : components) {
            if (c instanceof JLabel) {
                JLabel label = (JLabel) c;
                String text = label.getText();
                if (text.contains("Total Students:")) {
                    // Find the next label
                    Container parent = label.getParent();
                    if (parent != null) {
                        Component[] siblings = parent.getComponents();
                        for (int i = 0; i < siblings.length; i++) {
                            if (siblings[i] == label && i + 1 < siblings.length && siblings[i + 1] instanceof JLabel) {
                                ((JLabel) siblings[i + 1]).setText(String.valueOf(students));
                                break;
                            }
                        }
                    }
                } else if (text.contains("Total Subjects:")) {
                    Container parent = label.getParent();
                    if (parent != null) {
                        Component[] siblings = parent.getComponents();
                        for (int i = 0; i < siblings.length; i++) {
                            if (siblings[i] == label && i + 1 < siblings.length && siblings[i + 1] instanceof JLabel) {
                                ((JLabel) siblings[i + 1]).setText(String.valueOf(subjects));
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
    
    // Load student-specific attendance
    private void loadStudentAttendance() {
        tableModel.setRowCount(0);
        
        Student student = (Student) studentBox.getSelectedItem();
        if (student == null) {
            summaryLabel.setText("👆 Select a student to view attendance details");
            chartPanel.repaint();
            return;
        }
        
        Subject subject = (Subject) subjectBox.getSelectedItem();
        List<Attendance> list = attendanceDAO.getAttendanceByStudent(student.getId());
        
        if (subject != null) {
            list.removeIf(a -> a.getSubjectId() != subject.getId());
        }
        
        // Sort by date (most recent first)
        list.sort((a, b) -> b.getDate().compareTo(a.getDate()));
        
        for (Attendance a : list) {
            tableModel.addRow(new Object[]{
                a.getFormattedDate(),
                a.getSubjectCode(),
                a.getSubjectName(),
                a.getStatus()
            });
        }
        
        // Calculate statistics
        int total = list.size();
        int present = (int) list.stream().filter(a -> "PRESENT".equals(a.getStatus())).count();
        int absent = total - present;
        double percentage = total > 0 ? (present * 100.0 / total) : 0;
        
        // Update student stats panel
        updateStudentStats(student, subject, total, present, absent, percentage);
        
        // Update summary
        if (subject != null) {
            summaryLabel.setText(String.format(
                "📊 Student: %s (%s) | Subject: %s | Attendance: %.1f%% | Present: %d | Absent: %d | Total Classes: %d",
                student.getName(), student.getStudentId(), subject.getSubjectName(),
                percentage, present, absent, total));
        } else {
            summaryLabel.setText(String.format(
                "📊 Student: %s (%s) | Overall Attendance: %.1f%% | Present: %d | Absent: %d | Total Classes: %d",
                student.getName(), student.getStudentId(), percentage, present, absent, total));
        }
        
        // Repaint chart
        chartPanel.repaint();
    }
    
    private void updateStudentStats(Student student, Subject subject, int total, int present, int absent, double percentage) {
        statsPanel.removeAll();
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        
        // Title
        JLabel title = new JLabel("👤 STUDENT STATISTICS");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0;
        gbc.gridy = 0;
        statsPanel.add(title, gbc);
        
        // Separator
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(200, 200, 200));
        gbc.gridy = 1;
        statsPanel.add(sep, gbc);
        
        // Student Info
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        statsPanel.add(new JLabel("📌 Roll No:"), gbc);
        gbc.gridx = 1;
        JLabel rollVal = new JLabel(student.getStudentId());
        rollVal.setFont(new Font("Arial", Font.BOLD, 13));
        statsPanel.add(rollVal, gbc);
        
        gbc.gridy = 3;
        gbc.gridx = 0;
        statsPanel.add(new JLabel("👤 Name:"), gbc);
        gbc.gridx = 1;
        JLabel nameVal = new JLabel(student.getName());
        nameVal.setFont(new Font("Arial", Font.BOLD, 13));
        statsPanel.add(nameVal, gbc);
        
        gbc.gridy = 4;
        gbc.gridx = 0;
        statsPanel.add(new JLabel("🏛️ Department:"), gbc);
        gbc.gridx = 1;
        JLabel deptVal = new JLabel(student.getDepartment());
        deptVal.setFont(new Font("Arial", Font.BOLD, 13));
        statsPanel.add(deptVal, gbc);
        
        gbc.gridy = 5;
        gbc.gridx = 0;
        statsPanel.add(new JLabel("📚 Semester:"), gbc);
        gbc.gridx = 1;
        JLabel semVal = new JLabel("Sem " + student.getSemester());
        semVal.setFont(new Font("Arial", Font.BOLD, 13));
        statsPanel.add(semVal, gbc);
        
        // Attendance Stats
        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 8, 8, 8);
        JLabel statsTitle = new JLabel("📊 ATTENDANCE SUMMARY");
        statsTitle.setFont(new Font("Arial", Font.BOLD, 14));
        statsTitle.setForeground(new Color(44, 62, 80));
        statsPanel.add(statsTitle, gbc);
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridy = 7;
        gbc.gridx = 0;
        statsPanel.add(new JLabel("✅ Present:"), gbc);
        gbc.gridx = 1;
        JLabel presentVal = new JLabel(String.valueOf(present));
        presentVal.setFont(new Font("Arial", Font.BOLD, 14));
        presentVal.setForeground(PRESENT_COLOR);
        statsPanel.add(presentVal, gbc);
        
        gbc.gridy = 8;
        gbc.gridx = 0;
        statsPanel.add(new JLabel("❌ Absent:"), gbc);
        gbc.gridx = 1;
        JLabel absentVal = new JLabel(String.valueOf(absent));
        absentVal.setFont(new Font("Arial", Font.BOLD, 14));
        absentVal.setForeground(ABSENT_COLOR);
        statsPanel.add(absentVal, gbc);
        
        gbc.gridy = 9;
        gbc.gridx = 0;
        statsPanel.add(new JLabel("📚 Total Classes:"), gbc);
        gbc.gridx = 1;
        JLabel totalVal = new JLabel(String.valueOf(total));
        totalVal.setFont(new Font("Arial", Font.BOLD, 14));
        totalVal.setForeground(OVERALL_COLOR);
        statsPanel.add(totalVal, gbc);
        
        gbc.gridy = 10;
        gbc.gridx = 0;
        statsPanel.add(new JLabel("📈 Percentage:"), gbc);
        gbc.gridx = 1;
        JLabel percentVal = new JLabel(String.format("%.1f%%", percentage));
        percentVal.setFont(new Font("Arial", Font.BOLD, 18));
        
        // Color code percentage
        if (percentage >= 75) {
            percentVal.setForeground(PRESENT_COLOR);
        } else if (percentage >= 60) {
            percentVal.setForeground(WARNING_COLOR);
        } else {
            percentVal.setForeground(ABSENT_COLOR);
        }
        statsPanel.add(percentVal, gbc);
        
        statsPanel.revalidate();
        statsPanel.repaint();
    }
    
    public void refreshData() {
        loadStudents();
        loadSubjects();
    }
    
    private void loadStudents() {
        Student selected = (Student) studentBox.getSelectedItem();
        String selectedId = selected != null ? selected.getStudentId() : null;
        
        studentBox.removeAllItems();
        studentBox.addItem(null);
        
        List<Student> students = studentDAO.getAllStudents();
        for (Student s : students) {
            studentBox.addItem(s);
        }
        
        if (selectedId != null) {
            for (int i = 0; i < studentBox.getItemCount(); i++) {
                Student s = studentBox.getItemAt(i);
                if (s != null && selectedId.equals(s.getStudentId())) {
                    studentBox.setSelectedIndex(i);
                    break;
                }
            }
        }
        
        studentBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object v,
                    int i, boolean s, boolean f) {
                if (v == null) v = "-- Select Student --";
                else if (v instanceof Student) {
                    Student st = (Student) v;
                    v = st.getName() + " (" + st.getStudentId() + ")";
                }
                return super.getListCellRendererComponent(list, v, i, s, f);
            }
        });
    }
    
    private void loadSubjects() {
        Subject selected = (Subject) subjectBox.getSelectedItem();
        String selectedCode = selected != null ? selected.getSubjectCode() : null;
        
        subjectBox.removeAllItems();
        subjectBox.addItem(null);
        
        List<Subject> subjects = subjectDAO.getAllSubjects();
        for (Subject s : subjects) {
            subjectBox.addItem(s);
        }
        
        if (selectedCode != null) {
            for (int i = 0; i < subjectBox.getItemCount(); i++) {
                Subject s = subjectBox.getItemAt(i);
                if (s != null && selectedCode.equals(s.getSubjectCode())) {
                    subjectBox.setSelectedIndex(i);
                    break;
                }
            }
        }
        
        subjectBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object v,
                    int i, boolean s, boolean f) {
                if (v == null) v = "-- All Subjects --";
                else if (v instanceof Subject) {
                    Subject sub = (Subject) v;
                    v = sub.getSubjectName() + " (" + sub.getSubjectCode() + ")";
                }
                return super.getListCellRendererComponent(list, v, i, s, f);
            }
        });
    }
    
    public void refreshView() {
        refreshData();
        loadStudentAttendance();
        loadOverallStatistics();
    }
}