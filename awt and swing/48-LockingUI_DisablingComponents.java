import javax.swing.*;
import java.awt.*;

class LockingUI_DisablingComponents extends JFrame {
    public LockingUI_DisablingComponents() {
        setTitle("UI Locking Demo");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JCheckBox checkbox = new JCheckBox("Feature Enabled");
        JButton button = new JButton("Click Me");
        JTextField textField = new JTextField("Enter text");
        JComboBox<String> combo = new JComboBox<>(new String[]{"Option 1", "Option 2", "Option 3"});
        JTextArea textArea = new JTextArea("Text area");

        button.setEnabled(false);
        textField.setEnabled(false);
        combo.setEnabled(false);
        textArea.setEnabled(false);

        checkbox.addActionListener(e -> {
            boolean enabled = checkbox.isSelected();
            button.setEnabled(enabled);
            textField.setEnabled(enabled);
            combo.setEnabled(enabled);
            textArea.setEnabled(enabled);
        });

        panel.add(checkbox);
        panel.add(button);
        panel.add(textField);
        panel.add(combo);
        panel.add(textArea);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LockingUI_DisablingComponents());
    }
}

