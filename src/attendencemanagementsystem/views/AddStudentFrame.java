package attendencemanagementsystem.views;

import attendencemanagementsystem.dao.StudentDAO;
import attendencemanagementsystem.models.Student;
import attendencemanagementsystem.models.User;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class AddStudentFrame extends JPanel {
    private User currentUser;
    private StudentDAO studentDAO;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField idField, nameField, deptField;
    private JComboBox<Integer> semesterBox;
    private JLabel statusLabel;
    
    public AddStudentFrame(User user) {
        this.currentUser = user;
        this.studentDAO = new StudentDAO();
        setBackground(Color.WHITE);
        setLayout(new BorderLayout(0, 20));
        initComponents();
        loadData();
        updateStats();
    }
    
    private void initComponents() {
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("👨‍🎓 STUDENT MANAGEMENT");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(44, 62, 80));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        statusLabel = new JLabel("Total Students: 0");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setForeground(new Color(52, 152, 219));
        headerPanel.add(statusLabel, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Form Panel - Add Student
        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(249, 249, 249));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Form Title
        JLabel formTitle = new JLabel("➕ ADD NEW STUDENT");
        formTitle.setFont(new Font("Arial", Font.BOLD, 18));
        formTitle.setForeground(new Color(46, 204, 113));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(formTitle, gbc);
        
        // Separator Line
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(200, 200, 200));
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(separator, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Row 1: Student ID and Full Name
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        
        // Student ID Label with Hint
        gbc.gridx = 0;
        JLabel idLabel = new JLabel("📌 STUDENT ID:");
        idLabel.setFont(new Font("Arial", Font.BOLD, 13));
        idLabel.setForeground(new Color(44, 62, 80));
        formPanel.add(idLabel, gbc);
        
        // Student ID Field
        gbc.gridx = 1;
        idField = new JTextField(15);
        idField.setFont(new Font("Arial", Font.PLAIN, 14));
        idField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        idField.setToolTipText("Enter unique roll number (e.g., STU001)");
        formPanel.add(idField, gbc);
        
        // Full Name Label
        gbc.gridx = 2;
        JLabel nameLabel = new JLabel("👤 FULL NAME:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        nameLabel.setForeground(new Color(44, 62, 80));
        formPanel.add(nameLabel, gbc);
        
        // Full Name Field
        gbc.gridx = 3;
        nameField = new JTextField(15);
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        nameField.setToolTipText("Enter student's full name");
        formPanel.add(nameField, gbc);
        
        // Row 2: Department and Semester
        gbc.gridy = 3;
        
        // Department Label
        gbc.gridx = 0;
        JLabel deptLabel = new JLabel("🏛️ DEPARTMENT:");
        deptLabel.setFont(new Font("Arial", Font.BOLD, 13));
        deptLabel.setForeground(new Color(44, 62, 80));
        formPanel.add(deptLabel, gbc);
        
        // Department Field
        gbc.gridx = 1;
        deptField = new JTextField(15);
        deptField.setFont(new Font("Arial", Font.PLAIN, 14));
        deptField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        deptField.setToolTipText("e.g., Computer Science, IT, Electronics");
        formPanel.add(deptField, gbc);
        
        // Semester Label
        gbc.gridx = 2;
        JLabel semLabel = new JLabel("📚 SEMESTER:");
        semLabel.setFont(new Font("Arial", Font.BOLD, 13));
        semLabel.setForeground(new Color(44, 62, 80));
        formPanel.add(semLabel, gbc);
        
        // Semester ComboBox
        gbc.gridx = 3;
        semesterBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8});
        semesterBox.setFont(new Font("Arial", Font.PLAIN, 14));
        semesterBox.setBackground(Color.WHITE);
        semesterBox.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        semesterBox.setPreferredSize(new Dimension(100, 45));
        semesterBox.setToolTipText("Select current semester");
        formPanel.add(semesterBox, gbc);
        
        // Row 3: Add Button
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 10, 10);
        
        JButton addButton = new JButton("➕ ADD STUDENT");
        addButton.setFont(new Font("Arial", Font.BOLD, 16));
        addButton.setBackground(new Color(46, 204, 113));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.addActionListener(e -> addStudent());
        formPanel.add(addButton, gbc);
        
        // Hint Label
        gbc.gridy = 5;
        JLabel hintLabel = new JLabel("ⓘ All fields are required. Student ID must be unique.");
        hintLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        hintLabel.setForeground(new Color(100, 100, 100));
        formPanel.add(hintLabel, gbc);
        
        add(formPanel, BorderLayout.CENTER);
        
        // Table Panel - Student List
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JLabel tableTitle = new JLabel("📋 STUDENT LIST");
        tableTitle.setFont(new Font("Arial", Font.BOLD, 18));
        tableTitle.setForeground(new Color(44, 62, 80));
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        tablePanel.add(tableTitle, BorderLayout.NORTH);
        
        // Table Columns
        String[] columns = {
            "S.No", 
            "📌 Roll No", 
            "👤 Student Name", 
            "🏛️ Department", 
            "📚 Semester"
        };
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(40);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(52, 152, 219));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 45));
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(52, 152, 219, 50));
        table.setRowSelectionAllowed(true);
        
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scrollPane.setPreferredSize(new Dimension(900, 250));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Delete Button Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JButton deleteButton = new JButton("🗑️ DELETE SELECTED");
        deleteButton.setFont(new Font("Arial", Font.BOLD, 13));
        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.addActionListener(e -> deleteStudent());
        bottomPanel.add(deleteButton);
        
        tablePanel.add(bottomPanel, BorderLayout.SOUTH);
        add(tablePanel, BorderLayout.SOUTH);
    }
    
    // Add this hint in the success message

    private void addStudent() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String dept = deptField.getText().trim();
        int sem = (int) semesterBox.getSelectedItem();

        if (id.isEmpty() || name.isEmpty() || dept.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "❌ All fields are required!",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student student = new Student(id, name, dept, sem);

        if (studentDAO.addStudent(student, currentUser.getId())) {
            JOptionPane.showMessageDialog(this,
                "✅ Student added successfully!\n\n" +
                "📌 Roll No: " + id + "\n" +
                "👤 Name: " + name + "\n" +
                "🏛️ Department: " + dept + "\n" +
                "📚 Semester: " + sem,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadData();
            updateStats();

            // Show hint for attendance
            JOptionPane.showMessageDialog(this,
                "💡 TIP: You can now mark attendance for this student\n" +
                "in the 'Mark Attendance' panel.",
                "Next Step",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "❌ Student ID already exists!\nPlease use a different Roll Number.",
                "Duplicate Entry",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<Student> students = studentDAO.getAllStudents();
        int serialNo = 1;
        for (Student s : students) {
            tableModel.addRow(new Object[]{
                serialNo++,
                s.getStudentId(),
                s.getName(),
                s.getDepartment(),
                "Sem " + s.getSemester()
            });
        }
    }
    
    private void updateStats() {
        int count = tableModel.getRowCount();
        statusLabel.setText("📊 Total Students: " + count);
        
        if (count == 0) {
            statusLabel.setForeground(new Color(231, 76, 60));
        } else {
            statusLabel.setForeground(new Color(46, 204, 113));
        }
    }
    
    private void deleteStudent() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "⚠️ Please select a student to delete.\n" +
                "Click on any row in the table to select.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String studentName = (String) tableModel.getValueAt(row, 2);
        String rollNo = (String) tableModel.getValueAt(row, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "🗑️ Are you sure you want to delete?\n\n" +
            "Student: " + studentName + "\n" +
            "Roll No: " + rollNo + "\n\n" +
            "This action cannot be undone!",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(row, 0);
            // Get actual student ID from database
            List<Student> students = studentDAO.getAllStudents();
            if (row < students.size()) {
                int studentDbId = students.get(row).getId();
                if (studentDAO.deleteStudent(studentDbId)) {
                    JOptionPane.showMessageDialog(this,
                        "✅ Student deleted successfully!\n\n" +
                        "Student: " + studentName + "\n" +
                        "Roll No: " + rollNo,
                        "Deleted",
                        JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                    updateStats();
                }
            }
        }
    }
    
    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        deptField.setText("");
        semesterBox.setSelectedIndex(0);
        idField.requestFocus();
    }
    
    // Add this method to refresh student data
    public void refreshData() {
        loadData();
        updateStats();
    }
}