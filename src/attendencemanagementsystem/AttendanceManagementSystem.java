package attendencemanagementsystem;

import attendencemanagementsystem.database.DatabaseConnection;
import attendencemanagementsystem.views.LoginFrame;
import javax.swing.*;

public class AttendanceManagementSystem {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}