import javax.swing.*;
import java.awt.*;

class LayoutManager_GridBagLayout extends JFrame {
    public LayoutManager_GridBagLayout() {
        setTitle("GridBagLayout Demo - Form");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(nameLabel, gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(emailLabel, gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        JLabel messageLabel = new JLabel("Message:");
        JTextArea messageArea = new JTextArea(5, 15);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(messageLabel, gbc);
        gbc.gridx = 1;
        panel.add(new JScrollPane(messageArea), gbc);

        JButton submit = new JButton("Submit");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(submit, gbc);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LayoutManager_GridBagLayout());
    }
}

