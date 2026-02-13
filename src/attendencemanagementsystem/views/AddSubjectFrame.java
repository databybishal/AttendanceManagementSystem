package attendencemanagementsystem.views;

import attendencemanagementsystem.dao.SubjectDAO;
import attendencemanagementsystem.models.Subject;
import attendencemanagementsystem.models.User;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class AddSubjectFrame extends JPanel {
    private User currentUser;
    private SubjectDAO subjectDAO;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField codeField, nameField, deptField;
    private JComboBox<Integer> semesterBox;
    private JLabel statusLabel;
    
    public AddSubjectFrame(User user) {
        this.currentUser = user;
        this.subjectDAO = new SubjectDAO();
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        
        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel("SUBJECT MANAGEMENT");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titleLabel);
        
        statusLabel = new JLabel("Total Subjects: 0");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        headerPanel.add(statusLabel);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Form Panel - Simple GridLayout
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBackground(new Color(240, 240, 240));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Row 1
        formPanel.add(new JLabel("Subject Code:"));
        codeField = new JTextField();
        formPanel.add(codeField);
        
        // Row 2
        formPanel.add(new JLabel("Subject Name:"));
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
        JButton addButton = new JButton("ADD SUBJECT");
        addButton.setBackground(new Color(155, 89, 182));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> addSubject());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 240, 240));
        buttonPanel.add(addButton);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(formPanel, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Table Panel
        String[] columns = {"S.No", "Code", "Subject Name", "Department", "Semester"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(30);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Subject List"));
        scrollPane.setPreferredSize(new Dimension(800, 250));
        
        // Delete Button
        JButton deleteButton = new JButton("DELETE SELECTED");
        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(e -> deleteSubject());
        
        JPanel tableButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        tableButtonPanel.add(deleteButton);
        
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(scrollPane, BorderLayout.CENTER);
        southPanel.add(tableButtonPanel, BorderLayout.SOUTH);
        
        add(southPanel, BorderLayout.SOUTH);
        
        loadData();
        updateStats();
    }
    
    private void addSubject() {
        String code = codeField.getText().trim();
        String name = nameField.getText().trim();
        String dept = deptField.getText().trim();
        int sem = (int) semesterBox.getSelectedItem();
        
        if (code.isEmpty() || name.isEmpty() || dept.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }
        
        if (subjectDAO.addSubject(new Subject(code, name, dept, sem), currentUser.getId())) {
            JOptionPane.showMessageDialog(this, "Subject added successfully!");
            clearForm();
            loadData();
            updateStats();
        } else {
            JOptionPane.showMessageDialog(this, "Subject Code already exists!");
        }
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<Subject> subjects = subjectDAO.getAllSubjects();
        int i = 1;
        for (Subject s : subjects) {
            tableModel.addRow(new Object[]{
                i++, s.getSubjectCode(), s.getSubjectName(), s.getDepartment(), "Sem " + s.getSemester()
            });
        }
    }
    
    private void updateStats() {
        statusLabel.setText("Total Subjects: " + tableModel.getRowCount());
    }
    
    private void deleteSubject() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a subject!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this subject?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            List<Subject> subjects = subjectDAO.getAllSubjects();
            if (row < subjects.size()) {
                subjectDAO.deleteSubject(subjects.get(row).getId());
                loadData();
                updateStats();
            }
        }
    }
    
    private void clearForm() {
        codeField.setText("");
        nameField.setText("");
        deptField.setText("");
        semesterBox.setSelectedIndex(0);
    }
    
    public void refreshData() {
        loadData();
        updateStats();
    }
}