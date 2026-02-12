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
        setLayout(new BorderLayout(0, 20));
        initComponents();
        loadData();
        updateStats();
    }
    
    private void initComponents() {
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("📚 SUBJECT MANAGEMENT");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(44, 62, 80));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        statusLabel = new JLabel("Total Subjects: 0");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setForeground(new Color(155, 89, 182));
        headerPanel.add(statusLabel, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Form Panel - Add Subject
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
        JLabel formTitle = new JLabel("➕ ADD NEW SUBJECT");
        formTitle.setFont(new Font("Arial", Font.BOLD, 18));
        formTitle.setForeground(new Color(155, 89, 182));
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
        
        // Row 1: Subject Code and Subject Name
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        
        // Subject Code Label
        gbc.gridx = 0;
        JLabel codeLabel = new JLabel("🔖 SUBJECT CODE:");
        codeLabel.setFont(new Font("Arial", Font.BOLD, 13));
        codeLabel.setForeground(new Color(44, 62, 80));
        formPanel.add(codeLabel, gbc);
        
        // Subject Code Field
        gbc.gridx = 1;
        codeField = new JTextField(15);
        codeField.setFont(new Font("Arial", Font.PLAIN, 14));
        codeField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        codeField.setToolTipText("Enter unique subject code (e.g., CS501, IT301)");
        formPanel.add(codeField, gbc);
        
        // Subject Name Label
        gbc.gridx = 2;
        JLabel nameLabel = new JLabel("📘 SUBJECT NAME:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        nameLabel.setForeground(new Color(44, 62, 80));
        formPanel.add(nameLabel, gbc);
        
        // Subject Name Field
        gbc.gridx = 3;
        nameField = new JTextField(15);
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        nameField.setToolTipText("Enter full subject name (e.g., Database Management)");
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
        semesterBox.setToolTipText("Select semester for this subject");
        formPanel.add(semesterBox, gbc);
        
        // Row 3: Add Button
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 10, 10);
        
        JButton addButton = new JButton("➕ ADD SUBJECT");
        addButton.setFont(new Font("Arial", Font.BOLD, 16));
        addButton.setBackground(new Color(155, 89, 182));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.addActionListener(e -> addSubject());
        formPanel.add(addButton, gbc);
        
        // Hint Label
        gbc.gridy = 5;
        JLabel hintLabel = new JLabel("ⓘ All fields are required. Subject Code must be unique.");
        hintLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        hintLabel.setForeground(new Color(100, 100, 100));
        formPanel.add(hintLabel, gbc);
        
        add(formPanel, BorderLayout.CENTER);
        
        // Table Panel - Subject List
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JLabel tableTitle = new JLabel("📋 SUBJECT LIST");
        tableTitle.setFont(new Font("Arial", Font.BOLD, 18));
        tableTitle.setForeground(new Color(44, 62, 80));
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        tablePanel.add(tableTitle, BorderLayout.NORTH);
        
        // Table Columns
        String[] columns = {
            "S.No", 
            "🔖 Code", 
            "📘 Subject Name", 
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
        table.getTableHeader().setBackground(new Color(155, 89, 182));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 45));
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(155, 89, 182, 50));
        table.setRowSelectionAllowed(true);
        
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(250);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scrollPane.setPreferredSize(new Dimension(900, 250));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        add(tablePanel, BorderLayout.SOUTH);
    }
    
    // Add this method to AddSubjectFrame.java
// This will be called after successfully adding a subject

    // Add this method to AddSubjectFrame.java
// This will be called after successfully adding a subject

    private void addSubject() {
        String code = codeField.getText().trim();
        String name = nameField.getText().trim();
        String dept = deptField.getText().trim();
        int sem = (int) semesterBox.getSelectedItem();

        if (code.isEmpty() || name.isEmpty() || dept.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "❌ All fields are required!",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Subject subject = new Subject(code, name, dept, sem);

        if (subjectDAO.addSubject(subject, currentUser.getId())) {
            JOptionPane.showMessageDialog(this,
                "✅ Subject added successfully!\n\n" +
                "🔖 Code: " + code + "\n" +
                "📘 Name: " + name + "\n" +
                "🏛️ Department: " + dept + "\n" +
                "📚 Semester: " + sem,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadData();
            updateStats();

            // Show hint to go to Mark Attendance
            JOptionPane.showMessageDialog(this,
                "💡 TIP: Go to 'Mark Attendance' panel to take attendance for this subject.",
                "Next Step",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "❌ Subject Code already exists!\nPlease use a different code.",
                "Duplicate Entry",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<Subject> subjects = subjectDAO.getAllSubjects();
        int serialNo = 1;
        for (Subject s : subjects) {
            tableModel.addRow(new Object[]{
                serialNo++,
                s.getSubjectCode(),
                s.getSubjectName(),
                s.getDepartment(),
                "Sem " + s.getSemester()
            });
        }
    }
    
    private void updateStats() {
        int count = tableModel.getRowCount();
        statusLabel.setText("📊 Total Subjects: " + count);
        
        if (count == 0) {
            statusLabel.setForeground(new Color(231, 76, 60));
        } else {
            statusLabel.setForeground(new Color(155, 89, 182));
        }
    }
    
    private void clearForm() {
        codeField.setText("");
        nameField.setText("");
        deptField.setText("");
        semesterBox.setSelectedIndex(0);
        codeField.requestFocus();
    }
    
    // Add this method to refresh student data
    public void refreshData() {
        loadData();
        updateStats();
    }
}