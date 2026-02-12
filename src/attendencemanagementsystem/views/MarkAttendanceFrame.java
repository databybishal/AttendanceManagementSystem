package attendencemanagementsystem.views;

import attendencemanagementsystem.dao.*;
import attendencemanagementsystem.models.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class MarkAttendanceFrame extends JPanel {
    private User currentUser;
    private SubjectDAO subjectDAO;
    private StudentDAO studentDAO;
    private AttendanceDAO attendanceDAO;
    private JComboBox<Subject> subjectBox;
    private JSpinner dateSpinner;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Student> currentStudents;
    private JLabel infoLabel;
    private JButton refreshBtn;
    
    public MarkAttendanceFrame(User user) {
        this.currentUser = user;
        this.subjectDAO = new SubjectDAO();
        this.studentDAO = new StudentDAO();
        this.attendanceDAO = new AttendanceDAO();
        setBackground(Color.WHITE);
        setLayout(new BorderLayout(0, 20));
        initComponents();
        refreshSubjects(); // Load subjects initially
    }
    
    private void initComponents() {
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("📝 MARK ATTENDANCE");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(44, 62, 80));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        infoLabel = new JLabel("Select a subject to begin");
        infoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        infoLabel.setForeground(new Color(52, 152, 219));
        headerPanel.add(infoLabel, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Control Panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        controlPanel.setBackground(new Color(249, 249, 249));
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 25, 15, 25)
        ));
        
        // Subject Selection
        controlPanel.add(new JLabel("📚 SUBJECT:"));
        subjectBox = new JComboBox<>();
        subjectBox.setPreferredSize(new Dimension(280, 40));
        subjectBox.setFont(new Font("Arial", Font.PLAIN, 14));
        subjectBox.setBackground(Color.WHITE);
        subjectBox.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        subjectBox.addActionListener(e -> {
            if (subjectBox.getSelectedItem() != null) {
                loadStudents();
            }
        });
        controlPanel.add(subjectBox);
        
        // Date Selection
        controlPanel.add(new JLabel("📅 DATE:"));
        dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
        dateSpinner.setPreferredSize(new Dimension(140, 40));
        dateSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        dateSpinner.setValue(new Date());
        dateSpinner.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        controlPanel.add(dateSpinner);
        
        // Load Button
        JButton loadBtn = new JButton("🔄 LOAD STUDENTS");
        loadBtn.setFont(new Font("Arial", Font.BOLD, 13));
        loadBtn.setBackground(new Color(52, 152, 219));
        loadBtn.setForeground(Color.WHITE);
        loadBtn.setFocusPainted(false);
        loadBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        loadBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loadBtn.addActionListener(e -> loadStudents());
        controlPanel.add(loadBtn);
        
        // Refresh Button
        refreshBtn = new JButton("🔄 REFRESH SUBJECTS");
        refreshBtn.setFont(new Font("Arial", Font.BOLD, 13));
        refreshBtn.setBackground(new Color(155, 89, 182));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> refreshSubjects());
        controlPanel.add(refreshBtn);
        
        // Save Button
        JButton saveBtn = new JButton("💾 SAVE ATTENDANCE");
        saveBtn.setFont(new Font("Arial", Font.BOLD, 13));
        saveBtn.setBackground(new Color(46, 204, 113));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveBtn.addActionListener(e -> saveAttendance());
        controlPanel.add(saveBtn);
        
        add(controlPanel, BorderLayout.CENTER);
        
        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JLabel tableTitle = new JLabel("📋 STUDENT LIST");
        tableTitle.setFont(new Font("Arial", Font.BOLD, 18));
        tableTitle.setForeground(new Color(44, 62, 80));
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        tablePanel.add(tableTitle, BorderLayout.NORTH);
        
        String[] columns = {"📌 Roll No", "👤 Student Name", "🏛️ Department", "📚 Semester", "✅ Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 4; }
            @Override public Class<?> getColumnClass(int c) { return String.class; }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(45);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(52, 152, 219));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 45));
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(52, 152, 219, 50));
        table.setRowSelectionAllowed(false);
        
        // Status column editor and renderer
        String[] statusOptions = {"❌ ABSENT", "✅ PRESENT"};
        JComboBox<String> statusCombo = new JComboBox<>(statusOptions);
        statusCombo.setFont(new Font("Arial", Font.BOLD, 12));
        table.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(statusCombo));
        table.getColumnModel().getColumn(4).setCellRenderer(new StatusRenderer());
        
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        table.getColumnModel().getColumn(2).setPreferredWidth(180);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scrollPane.setPreferredSize(new Dimension(900, 350));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Summary Panel
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JLabel summaryTitle = new JLabel("Ready to mark attendance");
        summaryTitle.setFont(new Font("Arial", Font.ITALIC, 13));
        summaryTitle.setForeground(new Color(100, 100, 100));
        summaryPanel.add(summaryTitle);
        
        tablePanel.add(summaryPanel, BorderLayout.SOUTH);
        
        add(tablePanel, BorderLayout.SOUTH);
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
            
            if ("✅ PRESENT".equals(status) || "PRESENT".equals(status)) {
                setBackground(new Color(212, 237, 218));
                setForeground(new Color(30, 132, 73));
                setBorder(BorderFactory.createLineBorder(new Color(46, 204, 113), 1));
            } else {
                setBackground(new Color(248, 215, 218));
                setForeground(new Color(157, 38, 50));
                setBorder(BorderFactory.createLineBorder(new Color(231, 76, 60), 1));
            }
            
            if (s) {
                setBackground(getBackground().darker());
            }
            
            return this;
        }
    }
    
    // Public method to refresh subjects - Call this from Dashboard when switching to this panel
    public void refreshSubjects() {
        subjectBox.removeAllItems();
        List<Subject> subjects = subjectDAO.getAllSubjects();
        
        if (subjects.isEmpty()) {
            subjectBox.addItem(null);
            infoLabel.setText("⚠️ No subjects available. Please add subjects first.");
            infoLabel.setForeground(new Color(231, 76, 60));
        } else {
            for (Subject s : subjects) {
                subjectBox.addItem(s);
            }
            infoLabel.setText("✅ " + subjects.size() + " subjects loaded");
            infoLabel.setForeground(new Color(46, 204, 113));
        }
        
        // Set custom renderer for subject combo box
        subjectBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                if (value == null) {
                    value = "-- No Subjects Available --";
                } else if (value instanceof Subject) {
                    Subject s = (Subject) value;
                    value = s.getSubjectName() + " (" + s.getSubjectCode() + ") - Sem " + s.getSemester();
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
    }
    
    private void loadStudents() {
        tableModel.setRowCount(0);
        Subject subject = (Subject) subjectBox.getSelectedItem();
        
        if (subject == null) {
            JOptionPane.showMessageDialog(this,
                "⚠️ Please select a subject first!\n\n" +
                "Choose a subject from the dropdown menu.",
                "No Subject Selected",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Load students for the selected department and semester
        currentStudents = studentDAO.getStudentsByDepartmentAndSemester(
            subject.getDepartment(), subject.getSemester());
        
        if (currentStudents.isEmpty()) {
            infoLabel.setText("⚠️ No students found for " + subject.getSubjectName());
            infoLabel.setForeground(new Color(231, 76, 60));
            
            JOptionPane.showMessageDialog(this,
                "⚠️ No students available!\n\n" +
                "Department: " + subject.getDepartment() + "\n" +
                "Semester: " + subject.getSemester() + "\n\n" +
                "Please add students for this department and semester first.",
                "No Students Found",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Date selectedDate = (Date) dateSpinner.getValue();
        int presentCount = 0;
        
        for (Student s : currentStudents) {
            String status = "❌ ABSENT";
            if (attendanceDAO.isAttendanceExists(s.getId(), subject.getId(), selectedDate)) {
                status = "✅ PRESENT";
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
        
        infoLabel.setText(String.format("✅ Loaded %d students | Present: %d | Absent: %d", 
            tableModel.getRowCount(), presentCount, tableModel.getRowCount() - presentCount));
        infoLabel.setForeground(new Color(52, 152, 219));
    }
    
    private void saveAttendance() {
        Subject subject = (Subject) subjectBox.getSelectedItem();
        Date date = (Date) dateSpinner.getValue();
        
        if (subject == null) {
            JOptionPane.showMessageDialog(this,
                "⚠️ Please select a subject!",
                "No Subject",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "⚠️ No students to mark attendance!\n\n" +
                "Please load students first.",
                "No Data",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int saved = 0;
        int present = 0;
        int absent = 0;
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String roll = (String) tableModel.getValueAt(i, 0);
            String status = (String) tableModel.getValueAt(i, 4);
            
            // Convert display status to database status
            String dbStatus = status.contains("PRESENT") ? "PRESENT" : "ABSENT";
            if ("✅ PRESENT".equals(status)) dbStatus = "PRESENT";
            if ("❌ ABSENT".equals(status)) dbStatus = "ABSENT";
            
            if ("PRESENT".equals(dbStatus)) present++;
            else absent++;
            
            Student s = getStudent(roll);
            if (s != null) {
                Attendance a = new Attendance(s.getId(), subject.getId(), date, dbStatus);
                if (attendanceDAO.markOrUpdateAttendance(a, currentUser.getId())) {
                    saved++;
                }
            }
        }
        
        JOptionPane.showMessageDialog(this,
            "✅ ATTENDANCE SAVED SUCCESSFULLY!\n\n" +
            "📊 SUMMARY:\n" +
            "────────────────\n" +
            "✅ Present:  " + present + " students\n" +
            "❌ Absent:   " + absent + " students\n" +
            "📚 Total:    " + tableModel.getRowCount() + " students\n" +
            "💾 Saved:    " + saved + " records\n" +
            "────────────────\n" +
            "📅 Date: " + new java.text.SimpleDateFormat("yyyy-MM-dd").format(date) + "\n" +
            "📖 Subject: " + subject.getSubjectName(),
            "Attendance Saved",
            JOptionPane.INFORMATION_MESSAGE);
        
        infoLabel.setText(String.format("✅ Last saved: %d/%d records | Present: %d | Absent: %d", 
            saved, tableModel.getRowCount(), present, absent));
    }
    
    private Student getStudent(String roll) {
        if (currentStudents != null) {
            for (Student s : currentStudents) {
                if (s.getStudentId().equals(roll)) return s;
            }
        }
        return null;
    }
}