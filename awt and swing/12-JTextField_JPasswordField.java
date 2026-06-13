import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;

class JTextField_JPasswordField extends JFrame {
    public JTextField_JPasswordField() {
        setTitle("Text Input Components");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(15);
        
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(15);
        
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(15);
        emailField.setToolTipText("Enter valid email");
        
        JLabel statusLabel = new JLabel("Status:");
        JLabel statusValue = new JLabel("Type to see updates");

        userField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateStatus(); }
            public void removeUpdate(DocumentEvent e) { updateStatus(); }
            public void changedUpdate(DocumentEvent e) { updateStatus(); }
            void updateStatus() {
                statusValue.setText("Username: " + userField.getText());
            }
        });

        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(statusLabel);
        panel.add(statusValue);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JTextField_JPasswordField());
    }
}

