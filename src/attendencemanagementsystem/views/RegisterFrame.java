package attendencemanagementsystem.views;

import attendencemanagementsystem.dao.UserDAO;
import attendencemanagementsystem.models.User;
import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private JTextField fullNameField;
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmField;
    private UserDAO userDAO;
    
    public RegisterFrame() {
        userDAO = new UserDAO();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Attendance Management System - Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Main Panel with gradient background
        JPanel mainPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(44, 62, 80);
                Color color2 = new Color(52, 73, 94);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Register Card
        JPanel registerCard = new JPanel(new GridBagLayout());
        registerCard.setBackground(Color.WHITE);
        registerCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(25, 40, 25, 40)
        ));
        
        GridBagConstraints cardGbc = new GridBagConstraints();
        cardGbc.insets = new Insets(6, 8, 6, 8);
        cardGbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = new JLabel("📝 CREATE ACCOUNT");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));
        cardGbc.gridx = 0;
        cardGbc.gridy = 0;
        cardGbc.gridwidth = 2;
        cardGbc.anchor = GridBagConstraints.CENTER;
        registerCard.add(titleLabel, cardGbc);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Fill in your details to register");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        cardGbc.gridy = 1;
        registerCard.add(subtitleLabel, cardGbc);
        
        // Separator
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(200, 200, 200));
        cardGbc.gridy = 2;
        cardGbc.insets = new Insets(15, 8, 15, 8);
        registerCard.add(separator, cardGbc);
        cardGbc.insets = new Insets(6, 8, 6, 8);
        
        // Full Name
        cardGbc.gridwidth = 1;
        cardGbc.gridy = 3;
        cardGbc.gridx = 0;
        JLabel nameLabel = new JLabel("👤 Full Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        nameLabel.setForeground(new Color(80, 80, 80));
        registerCard.add(nameLabel, cardGbc);
        
        cardGbc.gridy = 4;
        cardGbc.gridx = 0;
        cardGbc.gridwidth = 2;
        fullNameField = new JTextField(20);
        fullNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        fullNameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        fullNameField.setToolTipText("Enter your full name");
        registerCard.add(fullNameField, cardGbc);
        
        // Username
        cardGbc.gridwidth = 1;
        cardGbc.gridy = 5;
        cardGbc.gridx = 0;
        JLabel userLabel = new JLabel("🔑 Username:");
        userLabel.setFont(new Font("Arial", Font.BOLD, 13));
        userLabel.setForeground(new Color(80, 80, 80));
        registerCard.add(userLabel, cardGbc);
        
        cardGbc.gridy = 6;
        cardGbc.gridx = 0;
        cardGbc.gridwidth = 2;
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        usernameField.setToolTipText("Choose a unique username");
        registerCard.add(usernameField, cardGbc);
        
        // Email
        cardGbc.gridwidth = 1;
        cardGbc.gridy = 7;
        cardGbc.gridx = 0;
        JLabel emailLabel = new JLabel("📧 Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 13));
        emailLabel.setForeground(new Color(80, 80, 80));
        registerCard.add(emailLabel, cardGbc);
        
        cardGbc.gridy = 8;
        cardGbc.gridx = 0;
        cardGbc.gridwidth = 2;
        emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        emailField.setToolTipText("Enter a valid email address");
        registerCard.add(emailField, cardGbc);
        
        // Password
        cardGbc.gridwidth = 1;
        cardGbc.gridy = 9;
        cardGbc.gridx = 0;
        JLabel passLabel = new JLabel("🔒 Password:");
        passLabel.setFont(new Font("Arial", Font.BOLD, 13));
        passLabel.setForeground(new Color(80, 80, 80));
        registerCard.add(passLabel, cardGbc);
        
        cardGbc.gridy = 10;
        cardGbc.gridx = 0;
        cardGbc.gridwidth = 2;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        passwordField.setToolTipText("Create a strong password");
        registerCard.add(passwordField, cardGbc);
        
        // Confirm Password
        cardGbc.gridwidth = 1;
        cardGbc.gridy = 11;
        cardGbc.gridx = 0;
        JLabel confirmLabel = new JLabel("✓ Confirm:");
        confirmLabel.setFont(new Font("Arial", Font.BOLD, 13));
        confirmLabel.setForeground(new Color(80, 80, 80));
        registerCard.add(confirmLabel, cardGbc);
        
        cardGbc.gridy = 12;
        cardGbc.gridx = 0;
        cardGbc.gridwidth = 2;
        confirmField = new JPasswordField(20);
        confirmField.setFont(new Font("Arial", Font.PLAIN, 14));
        confirmField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        confirmField.setToolTipText("Re-enter your password");
        registerCard.add(confirmField, cardGbc);
        
        // Buttons Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton registerBtn = new JButton("✅ REGISTER");
        registerBtn.setFont(new Font("Arial", Font.BOLD, 14));
        registerBtn.setBackground(new Color(46, 204, 113));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);
        registerBtn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerBtn.addActionListener(e -> register());
        buttonPanel.add(registerBtn);
        
        JButton backBtn = new JButton("🔙 BACK");
        backBtn.setFont(new Font("Arial", Font.BOLD, 14));
        backBtn.setBackground(new Color(52, 152, 219));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        buttonPanel.add(backBtn);
        
        cardGbc.gridy = 13;
        cardGbc.insets = new Insets(20, 8, 8, 8);
        registerCard.add(buttonPanel, cardGbc);
        
        // Terms hint
        cardGbc.gridy = 14;
        cardGbc.insets = new Insets(15, 8, 8, 8);
        JLabel termsLabel = new JLabel("By registering, you agree to our Terms & Conditions");
        termsLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        termsLabel.setForeground(new Color(150, 150, 150));
        termsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        registerCard.add(termsLabel, cardGbc);
        
        mainPanel.add(registerCard);
        add(mainPanel);
        
        setSize(500, 700);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void register() {
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmField.getPassword());
        
        // Validation
        if (fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "❌ Full name is required!",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            fullNameField.requestFocus();
            return;
        }
        
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "❌ Username is required!",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            usernameField.requestFocus();
            return;
        }
        
        if (username.length() < 3) {
            JOptionPane.showMessageDialog(this,
                "❌ Username must be at least 3 characters!",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            usernameField.requestFocus();
            usernameField.selectAll();
            return;
        }
        
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "❌ Email is required!",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            emailField.requestFocus();
            return;
        }
        
        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this,
                "❌ Please enter a valid email address!",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            emailField.requestFocus();
            emailField.selectAll();
            return;
        }
        
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "❌ Password is required!",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            passwordField.requestFocus();
            return;
        }
        
        if (password.length() < 4) {
            JOptionPane.showMessageDialog(this,
                "❌ Password must be at least 4 characters!",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            passwordField.requestFocus();
            passwordField.selectAll();
            return;
        }
        
        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this,
                "❌ Passwords do not match!",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            confirmField.setText("");
            confirmField.requestFocus();
            return;
        }
        
        // Check if username exists
        if (userDAO.isUsernameExists(username)) {
            JOptionPane.showMessageDialog(this,
                "❌ Username '" + username + "' is already taken!\nPlease choose a different username.",
                "Username Taken",
                JOptionPane.ERROR_MESSAGE);
            usernameField.requestFocus();
            usernameField.selectAll();
            return;
        }
        
        // Check if email exists
        if (userDAO.isEmailExists(email)) {
            JOptionPane.showMessageDialog(this,
                "❌ Email '" + email + "' is already registered!\nPlease use a different email.",
                "Email Registered",
                JOptionPane.ERROR_MESSAGE);
            emailField.requestFocus();
            emailField.selectAll();
            return;
        }
        
        // Create user
        User user = new User(username, email, password, fullName);
        
        if (userDAO.register(user)) {
            JOptionPane.showMessageDialog(this,
                "✅ Registration Successful!\n\n" +
                "Welcome, " + fullName + "!\n" +
                "You can now login with your credentials.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            new LoginFrame().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "❌ Registration failed!\nPlease try again later.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}