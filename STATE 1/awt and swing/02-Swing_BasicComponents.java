import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Swing_BasicComponents extends JFrame {
    private JLabel label;
    private JButton button;
    private JTextField textField;
    private JTextArea textArea;
    private JCheckBox checkBox;

    public Swing_BasicComponents() {
        setTitle("Swing Basic Components");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        label = new JLabel("Hello Swing!");
        button = new JButton("Click Me");
        textField = new JTextField("Enter text here", 20);
        textArea = new JTextArea(5, 30);
        checkBox = new JCheckBox("Accept Terms");

        add(label);
        add(button);
        add(textField);
        add(textArea);
        add(checkBox);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Swing_BasicComponents());
    }
}

