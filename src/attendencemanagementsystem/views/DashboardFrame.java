package attendencemanagementsystem.views;

import attendencemanagementsystem.dao.*;
import attendencemanagementsystem.models.User;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DashboardFrame extends JFrame {
    private User currentUser;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JLabel statusLabel;
    private DashboardHomePanel homePanel;
    
    public DashboardFrame(User user) {
        this.currentUser = user;
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Attendance Management System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Top Header Bar
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(44, 62, 80));
        headerPanel.setPreferredSize(new Dimension(800, 70));
        
        JLabel titleLabel = new JLabel("📊 Attendance Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        userPanel.setBackground(new Color(44, 62, 80));
        userPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        
        statusLabel = new JLabel("Welcome, " + currentUser.getFullName());
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        statusLabel.setForeground(Color.WHITE);
        userPanel.add(statusLabel);
        
        JButton logoutButton = new JButton("Sign Out");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
        logoutButton.setBackground(new Color(192, 57, 43));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        logoutButton.addActionListener(e -> logout());
        userPanel.add(logoutButton);
        
        headerPanel.add(userPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);
        
        // Side Navigation
        JPanel navPanel = new JPanel();
        navPanel.setBackground(new Color(52, 73, 94));
        navPanel.setPreferredSize(new Dimension(220, 400));
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBorder(BorderFactory.createEmptyBorder(30, 15, 30, 15));
        
        // Navigation items with icons
        addNavButton(navPanel, "🏠 Dashboard", "Dashboard");
        addNavButton(navPanel, "👥 Students", "Students");
        addNavButton(navPanel, "📚 Subjects", "Subjects");
        addNavButton(navPanel, "📝 Mark Attendance", "Attendance");
        addNavButton(navPanel, "📈 Reports", "Reports");
        
        add(navPanel, BorderLayout.WEST);
        
        // Main Content Area
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create home panel with reference to refresh later
        homePanel = new DashboardHomePanel(currentUser);
        
        // Add panels
        cardPanel.add(homePanel, "Dashboard");
        cardPanel.add(new AddStudentFrame(currentUser), "Students");
        cardPanel.add(new AddSubjectFrame(currentUser), "Subjects");
        cardPanel.add(new MarkAttendanceFrame(currentUser), "Attendance");
        cardPanel.add(new ViewAttendanceFrame(), "Reports");
        
        add(cardPanel, BorderLayout.CENTER);
        
        setSize(1100, 650);
        setLocationRelativeTo(null);
    }
    
    private void addNavButton(JPanel panel, String text, String command) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(190, 45));
        button.setMinimumSize(new Dimension(190, 45));
        button.setPreferredSize(new Dimension(190, 45));
        button.setBackground(new Color(52, 73, 94));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(52, 73, 94));
            }
        });
        
        button.addActionListener(e -> switchPanel(command));
        
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(button);
    }
    
    void switchPanel(String panelName) {
        cardLayout.show(cardPanel, panelName);
        statusLabel.setText(currentUser.getFullName() + " • " + panelName);
        
        // Refresh data when switching to specific panels
        if (panelName.equals("Dashboard")) {
            // Refresh dashboard stats
            homePanel.refreshStats();
        }
        
        if (panelName.equals("Attendance")) {
            // Refresh subjects in Mark Attendance panel
            for (Component c : cardPanel.getComponents()) {
                if (c instanceof MarkAttendanceFrame) {
                    ((MarkAttendanceFrame) c).refreshSubjects();
                    break;
                }
            }
        }
        
        if (panelName.equals("Reports")) {
            // Refresh attendance reports
            for (Component c : cardPanel.getComponents()) {
                if (c instanceof ViewAttendanceFrame) {
                    ((ViewAttendanceFrame) c).refreshView();
                    break;
                }
            }
        }
        
        if (panelName.equals("Students")) {
            // Refresh student list
            for (Component c : cardPanel.getComponents()) {
                if (c instanceof AddStudentFrame) {
                    ((AddStudentFrame) c).refreshData();
                    break;
                }
            }
        }
        
        if (panelName.equals("Subjects")) {
            // Refresh subject list
            for (Component c : cardPanel.getComponents()) {
                if (c instanceof AddSubjectFrame) {
                    ((AddSubjectFrame) c).refreshData();
                    break;
                }
            }
        }
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to sign out?", 
            "Sign Out", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            dispose();
        }
    }
}

// REDESIGNED DASHBOARD HOME PANEL WITH REAL STATISTICS
class DashboardHomePanel extends JPanel {
    private User currentUser;
    private StudentDAO studentDAO;
    private SubjectDAO subjectDAO;
    private AttendanceDAO attendanceDAO;
    
    private JLabel studentCountLabel;
    private JLabel subjectCountLabel;
    private JLabel attendancePercentLabel;
    private JLabel totalAttendanceLabel;
    
    public DashboardHomePanel(User user) {
        this.currentUser = user;
        this.studentDAO = new StudentDAO();
        this.subjectDAO = new SubjectDAO();
        this.attendanceDAO = new AttendanceDAO();
        
        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());
        
        initializeComponents();
        refreshStats(); // Load real data
    }
    
    private void initializeComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Welcome Card
        JPanel welcomeCard = new JPanel();
        welcomeCard.setBackground(new Color(52, 152, 219));
        welcomeCard.setLayout(new BorderLayout());
        welcomeCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(41, 128, 185), 2),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        
        JLabel welcomeLabel = new JLabel("👋 Welcome back, " + currentUser.getFullName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 26));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeCard.add(welcomeLabel, BorderLayout.NORTH);
        
        JLabel infoLabel = new JLabel("<html><center>📊 Attendance Management System<br>Manage students, subjects, and track attendance efficiently.</center></html>");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeCard.add(infoLabel, BorderLayout.CENTER);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        add(welcomeCard, gbc);
        
        // Statistics Title
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(30, 15, 15, 15);
        JLabel statsTitle = new JLabel("📊 SYSTEM STATISTICS");
        statsTitle.setFont(new Font("Arial", Font.BOLD, 20));
        statsTitle.setForeground(new Color(44, 62, 80));
        statsTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(statsTitle, gbc);
        
        // Stats Cards
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.gridy = 2;
        
        // Student Card
        gbc.gridx = 0;
        add(createStatCard(
            "👥 TOTAL STUDENTS", 
            "0", 
            new Color(46, 204, 113),
            "Registered students in the system"
        ), gbc);
        
        // Subject Card
        gbc.gridx = 1;
        add(createStatCard(
            "📚 TOTAL SUBJECTS", 
            "0", 
            new Color(155, 89, 182),
            "Available subjects"
        ), gbc);
        
        // Attendance Card
        gbc.gridx = 2;
        add(createStatCard(
            "📈 AVG ATTENDANCE", 
            "0%", 
            new Color(241, 196, 15),
            "Overall attendance percentage"
        ), gbc);
        
        // Additional Stats Row
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(20, 15, 15, 15);
        
        JPanel additionalStats = new JPanel(new GridLayout(1, 2, 20, 0));
        additionalStats.setBackground(Color.WHITE);
        additionalStats.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        
        // Total Attendance Records
        totalAttendanceLabel = new JLabel("📋 Total Attendance Records: 0");
        totalAttendanceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalAttendanceLabel.setForeground(new Color(52, 73, 94));
        additionalStats.add(totalAttendanceLabel);
        
        // Recent Activity
        JLabel recentLabel = new JLabel("🕒 Last Login: Today");
        recentLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        recentLabel.setForeground(new Color(100, 100, 100));
        additionalStats.add(recentLabel);
        
        add(additionalStats, gbc);
        
        // Quick Actions
        gbc.gridy = 4;
        gbc.insets = new Insets(30, 15, 15, 15);
        JLabel actionsTitle = new JLabel("⚡ QUICK ACTIONS");
        actionsTitle.setFont(new Font("Arial", Font.BOLD, 18));
        actionsTitle.setForeground(new Color(44, 62, 80));
        actionsTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(actionsTitle, gbc);
        
        // Action Buttons
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 15, 20, 15);
        
        gbc.gridx = 0;
        JButton addStudentBtn = createActionButton("➕ Add Student", new Color(46, 204, 113));
        addStudentBtn.addActionListener(e -> {
            // Switch to Students panel
            Container parent = getParent();
            while (parent != null && !(parent instanceof DashboardFrame)) {
                parent = parent.getParent();
            }
            if (parent instanceof DashboardFrame) {
                ((DashboardFrame) parent).switchPanel("Students");
            }
        });
        add(addStudentBtn, gbc);
        
        gbc.gridx = 1;
        JButton addSubjectBtn = createActionButton("📚 Add Subject", new Color(155, 89, 182));
        addSubjectBtn.addActionListener(e -> {
            Container parent = getParent();
            while (parent != null && !(parent instanceof DashboardFrame)) {
                parent = parent.getParent();
            }
            if (parent instanceof DashboardFrame) {
                ((DashboardFrame) parent).switchPanel("Subjects");
            }
        });
        add(addSubjectBtn, gbc);
        
        gbc.gridx = 2;
        JButton markAttendanceBtn = createActionButton("📝 Mark Attendance", new Color(52, 152, 219));
        markAttendanceBtn.addActionListener(e -> {
            Container parent = getParent();
            while (parent != null && !(parent instanceof DashboardFrame)) {
                parent = parent.getParent();
            }
            if (parent instanceof DashboardFrame) {
                ((DashboardFrame) parent).switchPanel("Attendance");
            }
        });
        add(markAttendanceBtn, gbc);
    }
    
    private JPanel createStatCard(String title, String value, Color color, String tooltip) {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        card.setLayout(new BorderLayout(10, 10));
        card.setToolTipText(tooltip);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(new Color(100, 100, 100));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(titleLabel, BorderLayout.NORTH);
        
        if (title.contains("STUDENTS")) {
            studentCountLabel = new JLabel(value);
            studentCountLabel.setFont(new Font("Arial", Font.BOLD, 36));
            studentCountLabel.setForeground(color);
            studentCountLabel.setHorizontalAlignment(SwingConstants.CENTER);
            card.add(studentCountLabel, BorderLayout.CENTER);
        } else if (title.contains("SUBJECTS")) {
            subjectCountLabel = new JLabel(value);
            subjectCountLabel.setFont(new Font("Arial", Font.BOLD, 36));
            subjectCountLabel.setForeground(color);
            subjectCountLabel.setHorizontalAlignment(SwingConstants.CENTER);
            card.add(subjectCountLabel, BorderLayout.CENTER);
        } else if (title.contains("ATTENDANCE")) {
            attendancePercentLabel = new JLabel(value);
            attendancePercentLabel.setFont(new Font("Arial", Font.BOLD, 36));
            attendancePercentLabel.setForeground(color);
            attendancePercentLabel.setHorizontalAlignment(SwingConstants.CENTER);
            card.add(attendancePercentLabel, BorderLayout.CENTER);
        }
        
        return card;
    }
    
    private JButton createActionButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(180, 45));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    // Method to refresh statistics with real data from database
    public void refreshStats() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                // Get real data from DAOs
                int studentCount = studentDAO.getAllStudents().size();
                int subjectCount = subjectDAO.getAllSubjects().size();
                
                // Calculate overall attendance percentage
                double overallPercentage = 0;
                int totalRecords = 0;
                int presentCount = 0;
                
                List<attendencemanagementsystem.models.Attendance> allAttendance = new java.util.ArrayList<>();
                // Get attendance for all students (simplified - you might want to optimize this)
                for (attendencemanagementsystem.models.Student s : studentDAO.getAllStudents()) {
                    List<attendencemanagementsystem.models.Attendance> list = attendanceDAO.getAttendanceByStudent(s.getId());
                    allAttendance.addAll(list);
                    presentCount += list.stream().filter(a -> "PRESENT".equals(a.getStatus())).count();
                }
                
                totalRecords = allAttendance.size();
                if (totalRecords > 0) {
                    overallPercentage = (presentCount * 100.0) / totalRecords;
                }
                
                // Update UI on EDT
                final int finalStudentCount = studentCount;
                final int finalSubjectCount = subjectCount;
                final double finalPercentage = overallPercentage;
                final int finalTotalRecords = totalRecords;
                
                SwingUtilities.invokeLater(() -> {
                    studentCountLabel.setText(String.valueOf(finalStudentCount));
                    subjectCountLabel.setText(String.valueOf(finalSubjectCount));
                    attendancePercentLabel.setText(String.format("%.1f%%", finalPercentage));
                    totalAttendanceLabel.setText("📋 Total Attendance Records: " + finalTotalRecords);
                    
                    // Color code the percentage
                    if (finalPercentage >= 75) {
                        attendancePercentLabel.setForeground(new Color(46, 204, 113));
                    } else if (finalPercentage >= 60) {
                        attendancePercentLabel.setForeground(new Color(241, 196, 15));
                    } else {
                        attendancePercentLabel.setForeground(new Color(231, 76, 60));
                    }
                });
                
                return null;
            }
        };
        
        worker.execute();
    }
}