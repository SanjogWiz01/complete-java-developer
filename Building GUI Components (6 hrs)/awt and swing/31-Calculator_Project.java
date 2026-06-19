import javax.swing.*;
import java.awt.*;
import javax.script.*;

class Calculator_Project extends JFrame {
    private JTextField display;
    private StringBuilder expression;

    public Calculator_Project() {
        setTitle("Calculator");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        display = new JTextField();
        display.setFont(new Font("Arial", Font.BOLD, 24));
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setText("0");

        expression = new StringBuilder();

        JPanel buttonPanel = new JPanel(new GridLayout(5, 4, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        String[] buttons = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+",
            "C", "CE", "Back", "^"
        };

        for (String btnText : buttons) {
            JButton btn = new JButton(btnText);
            btn.setFont(new Font("Arial", Font.BOLD, 18));
            btn.addActionListener(e -> handleButtonClick(btnText, display));
            buttonPanel.add(btn);
        }

        add(display, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void handleButtonClick(String btn, JTextField display) {
        String currentText = display.getText();
        
        if (btn.equals("C")) {
            display.setText("0");
            expression.setLength(0);
        } else if (btn.equals("CE")) {
            display.setText("0");
        } else if (btn.equals("Back")) {
            if (currentText.length() > 1) {
                display.setText(currentText.substring(0, currentText.length() - 1));
            } else {
                display.setText("0");
            }
        } else if (btn.equals("=")) {
            try {
                String result = String.valueOf(eval(expression.toString()));
                display.setText(result);
                expression.setLength(0);
            } catch (Exception e) {
                display.setText("Error");
            }
        } else if ("+-*/.^".contains(btn)) {
            if (!currentText.equals("0")) {
                expression.append(currentText).append(btn);
            }
            display.setText("0");
        } else {
            if (currentText.equals("0")) {
                display.setText(btn);
            } else {
                display.setText(currentText + btn);
            }
        }
    }

    private double eval(String expr) {
        return new javax.script.ScriptEngineManager()
            .getEngineByName("JavaScript")
            .eval(expr) instanceof Number ? 
            ((Number) new javax.script.ScriptEngineManager()
                .getEngineByName("JavaScript")
                .eval(expr)).doubleValue() : 0;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Calculator_Project());
    }
}
