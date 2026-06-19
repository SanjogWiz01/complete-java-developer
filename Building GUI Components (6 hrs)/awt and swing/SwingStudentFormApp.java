import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class SwingStudentFormApp extends JFrame {
    private final JTextField nameField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JComboBox<String> courseBox = new JComboBox<>(
            new String[] {"Java", "Python", "Web Development", "Database"});
    private final JTextArea outputArea = new JTextArea(6, 28);

    public SwingStudentFormApp() {
        super("Swing Student Form");

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));

        addFormRow(formPanel, 0, "Name:", nameField);
        addFormRow(formPanel, 1, "Email:", emailField);
        addFormRow(formPanel, 2, "Course:", courseBox);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(event -> showStudentDetails());

        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 1;
        buttonConstraints.gridy = 3;
        buttonConstraints.anchor = GridBagConstraints.WEST;
        buttonConstraints.insets = new Insets(8, 0, 0, 0);
        formPanel.add(submitButton, buttonConstraints);

        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);

        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(430, 320);
        setLocationRelativeTo(null);
    }

    private void addFormRow(JPanel panel, int row, String labelText, java.awt.Component field) {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = row;
        labelConstraints.anchor = GridBagConstraints.EAST;
        labelConstraints.insets = new Insets(4, 4, 4, 8);
        panel.add(new JLabel(labelText), labelConstraints);

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.gridx = 1;
        fieldConstraints.gridy = row;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.weightx = 1.0;
        fieldConstraints.insets = new Insets(4, 0, 4, 4);
        panel.add(field, fieldConstraints);
    }

    private void showStudentDetails() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter name and email.");
            return;
        }

        String course = (String) courseBox.getSelectedItem();
        outputArea.setText("Student Details\n");
        outputArea.append("Name: " + name + "\n");
        outputArea.append("Email: " + email + "\n");
        outputArea.append("Course: " + course + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SwingStudentFormApp().setVisible(true));
    }
}
