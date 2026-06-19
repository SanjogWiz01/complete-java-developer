import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

class DataValidation_Form extends JFrame {
    public DataValidation_Form() {
        setTitle("Form with Validation");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(20);
        
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(20);
        
        JLabel phoneLabel = new JLabel("Phone:");
        JTextField phoneField = new JTextField(20);
        
        JLabel messageLabel = new JLabel("Message:");
        JTextArea messageArea = new JTextArea(5, 20);

        JButton submitButton = new JButton("Submit");
        JButton clearButton = new JButton("Clear");

        submitButton.addActionListener(e -> validateAndSubmit(nameField, emailField, phoneField, messageArea));
        clearButton.addActionListener(e -> clearForm(nameField, emailField, phoneField, messageArea));

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(nameLabel, gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(emailLabel, gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(phoneLabel, gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(messageLabel, gbc);
        gbc.gridx = 1;
        panel.add(new JScrollPane(messageArea), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(submitButton, gbc);
        gbc.gridx = 1;
        panel.add(clearButton, gbc);

        add(panel);
        setVisible(true);
    }

    private void validateAndSubmit(JTextField name, JTextField email, JTextField phone, JTextArea message) {
        String n = name.getText().trim();
        String e = email.getText().trim();
        String p = phone.getText().trim();

        if (n.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required");
        } else if (e.isEmpty() || !e.contains("@")) {
            JOptionPane.showMessageDialog(this, "Valid email is required");
        } else if (p.isEmpty() || p.length() < 10) {
            JOptionPane.showMessageDialog(this, "Valid phone number is required");
        } else {
            JOptionPane.showMessageDialog(this, "Form submitted successfully!");
        }
    }

    private void clearForm(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DataValidation_Form());
    }
}

