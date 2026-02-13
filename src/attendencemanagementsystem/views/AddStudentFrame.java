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
        setLayout(new BorderLayout());
        
        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel("STUDENT MANAGEMENT");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titleLabel);
        
        statusLabel = new JLabel("Total Students: 0");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        headerPanel.add(statusLabel);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Form Panel - Simple GridLayout
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBackground(new Color(240, 240, 240));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Row 1
        formPanel.add(new JLabel("Student ID:"));
        idField = new JTextField();
        formPanel.add(idField);
        
        // Row 2
        formPanel.add(new JLabel("Full Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);
        
        // Row 3
        formPanel.add(new JLabel("Department:"));
        deptField = new JTextField();
        formPanel.add(deptField);
        
        // Row 4
        formPanel.add(new JLabel("Semester:"));
        semesterBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8});
        formPanel.add(semesterBox);
        
        // Add Button
        JButton addButton = new JButton("ADD STUDENT");
        addButton.setBackground(new Color(46, 204, 113));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> addStudent());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 240, 240));
        buttonPanel.add(addButton);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(formPanel, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Table Panel
        String[] columns = {"S.No", "Roll No", "Name", "Department", "Semester"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(30);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Student List"));
        scrollPane.setPreferredSize(new Dimension(800, 250));
        
        // Delete Button
        JButton deleteButton = new JButton("DELETE SELECTED");
        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(e -> deleteStudent());
        
        JPanel tableButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        tableButtonPanel.add(deleteButton);
        
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(scrollPane, BorderLayout.CENTER);
        southPanel.add(tableButtonPanel, BorderLayout.SOUTH);
        
        add(southPanel, BorderLayout.SOUTH);
        
        loadData();
        updateStats();
    }
    
    private void addStudent() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String dept = deptField.getText().trim();
        int sem = (int) semesterBox.getSelectedItem();
        
        if (id.isEmpty() || name.isEmpty() || dept.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }
        
        if (studentDAO.addStudent(new Student(id, name, dept, sem), currentUser.getId())) {
            JOptionPane.showMessageDialog(this, "Student added successfully!");
            clearForm();
            loadData();
            updateStats();
        } else {
            JOptionPane.showMessageDialog(this, "Student ID already exists!");
        }
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<Student> students = studentDAO.getAllStudents();
        int i = 1;
        for (Student s : students) {
            tableModel.addRow(new Object[]{
                i++, s.getStudentId(), s.getName(), s.getDepartment(), "Sem " + s.getSemester()
            });
        }
    }
    
    private void updateStats() {
        statusLabel.setText("Total Students: " + tableModel.getRowCount());
    }
    
    private void deleteStudent() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a student!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this student?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            List<Student> students = studentDAO.getAllStudents();
            if (row < students.size()) {
                studentDAO.deleteStudent(students.get(row).getId());
                loadData();
                updateStats();
            }
        }
    }
    
    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        deptField.setText("");
        semesterBox.setSelectedIndex(0);
    }
    
    public void refreshData() {
        loadData();
        updateStats();
    }
}