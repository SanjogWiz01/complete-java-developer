import javax.swing.*;
import java.awt.*;

class JTabbedPane_Demo extends JFrame {
    public JTabbedPane_Demo() {
        setTitle("JTabbedPane Demo");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel homePanel = new JPanel();
        homePanel.setBackground(new Color(200, 220, 240));
        homePanel.add(new JLabel("Welcome to Tab 1 - Home"));
        
        JPanel profilePanel = new JPanel();
        profilePanel.setBackground(new Color(220, 200, 240));
        profilePanel.setLayout(new GridLayout(3, 2, 10, 10));
        profilePanel.add(new JLabel("Name:"));
        profilePanel.add(new JTextField(15));
        profilePanel.add(new JLabel("Email:"));
        profilePanel.add(new JTextField(15));
        profilePanel.add(new JLabel("Phone:"));
        profilePanel.add(new JTextField(15));
        
        JPanel settingsPanel = new JPanel();
        settingsPanel.setBackground(new Color(240, 220, 200));
        settingsPanel.add(new JCheckBox("Enable Notifications"));
        settingsPanel.add(new JCheckBox("Dark Mode"));
        
        JPanel aboutPanel = new JPanel();
        aboutPanel.setBackground(new Color(220, 240, 200));
        aboutPanel.add(new JLabel("About this application v1.0"));

        tabbedPane.addTab("Home", homePanel);
        tabbedPane.addTab("Profile", profilePanel);
        tabbedPane.addTab("Settings", settingsPanel);
        tabbedPane.addTab("About", aboutPanel);

        add(tabbedPane);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JTabbedPane_Demo());
    }
}

