package attendencemanagementsystem.views;

import attendencemanagementsystem.dao.UserDAO;
import attendencemanagementsystem.models.User;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private UserDAO userDAO;
    
    public LoginFrame() {
        userDAO = new UserDAO();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Attendance Management System - Login");
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
        
        // Login Card
        JPanel loginCard = new JPanel(new GridBagLayout());
        loginCard.setBackground(Color.WHITE);
        loginCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        
        GridBagConstraints cardGbc = new GridBagConstraints();
        cardGbc.insets = new Insets(8, 8, 8, 8);
        cardGbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = new JLabel("📊 ATTENDANCE SYSTEM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));
        cardGbc.gridx = 0;
        cardGbc.gridy = 0;
        cardGbc.gridwidth = 2;
        cardGbc.anchor = GridBagConstraints.CENTER;
        loginCard.add(titleLabel, cardGbc);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Sign in to your account");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        cardGbc.gridy = 1;
        loginCard.add(subtitleLabel, cardGbc);
        
        // Separator
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(200, 200, 200));
        cardGbc.gridy = 2;
        cardGbc.insets = new Insets(15, 8, 15, 8);
        loginCard.add(separator, cardGbc);
        cardGbc.insets = new Insets(8, 8, 8, 8);
        
        // Username Label
        cardGbc.gridwidth = 1;
        cardGbc.gridy = 3;
        cardGbc.gridx = 0;
        JLabel userLabel = new JLabel("👤 Username:");
        userLabel.setFont(new Font("Arial", Font.BOLD, 13));
        userLabel.setForeground(new Color(80, 80, 80));
        loginCard.add(userLabel, cardGbc);
        
        // Username Field
        cardGbc.gridy = 4;
        cardGbc.gridx = 0;
        cardGbc.gridwidth = 2;
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        loginCard.add(usernameField, cardGbc);
        
        // Password Label
        cardGbc.gridwidth = 1;
        cardGbc.gridy = 5;
        cardGbc.gridx = 0;
        JLabel passLabel = new JLabel("🔒 Password:");
        passLabel.setFont(new Font("Arial", Font.BOLD, 13));
        passLabel.setForeground(new Color(80, 80, 80));
        loginCard.add(passLabel, cardGbc);
        
        // Password Field
        cardGbc.gridy = 6;
        cardGbc.gridx = 0;
        cardGbc.gridwidth = 2;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        loginCard.add(passwordField, cardGbc);
        
        // Buttons Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton loginButton = new JButton("🔑 SIGN IN");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(52, 152, 219));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> login());
        buttonPanel.add(loginButton);
        
        JButton registerButton = new JButton("📝 REGISTER");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(46, 204, 113));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            dispose();
        });
        buttonPanel.add(registerButton);
        
        cardGbc.gridy = 7;
        cardGbc.insets = new Insets(25, 8, 8, 8);
        loginCard.add(buttonPanel, cardGbc);
        
        // Forgot password hint
        cardGbc.gridy = 8;
        cardGbc.insets = new Insets(15, 8, 8, 8);
        JLabel forgotLabel = new JLabel("Don't have an account? Register above");
        forgotLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        forgotLabel.setForeground(new Color(150, 150, 150));
        forgotLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginCard.add(forgotLabel, cardGbc);
        
        mainPanel.add(loginCard);
        add(mainPanel);
        
        setSize(500, 550);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "❌ Please enter both username and password!",
                "Login Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        User user = userDAO.login(username, password);
        if (user != null) {
            JOptionPane.showMessageDialog(this,
                "✅ Welcome back, " + user.getFullName() + "!",
                "Login Successful",
                JOptionPane.INFORMATION_MESSAGE);
            new DashboardFrame(user).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "❌ Invalid username or password!",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            passwordField.requestFocus();
        }
    }
}