import javax.swing.*;
import java.awt.*;
import java.text.*;

class JFormattedTextField_Demo extends JFrame {
    public JFormattedTextField_Demo() {
        setTitle("Formatted TextField Demo");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel phoneLabel = new JLabel("Phone Number:");
        JFormattedTextField phoneField = new JFormattedTextField(new MaskFormatter("(###) ###-####"));
        phoneField.setColumns(15);

        JLabel dateLabel = new JLabel("Date (MM/dd/yyyy):");
        JFormattedTextField dateField = new JFormattedTextField(new SimpleDateFormat("MM/dd/yyyy"));
        dateField.setColumns(15);

        JLabel numberLabel = new JLabel("Number:");
        JFormattedTextField numberField = new JFormattedTextField(NumberFormat.getInstance());
        numberField.setColumns(15);
        numberField.setValue(12345.67);

        JLabel currencyLabel = new JLabel("Currency:");
        JFormattedTextField currencyField = new JFormattedTextField(NumberFormat.getCurrencyInstance());
        currencyField.setColumns(15);
        currencyField.setValue(99.99);

        panel.add(phoneLabel);
        panel.add(phoneField);
        panel.add(dateLabel);
        panel.add(dateField);
        panel.add(numberLabel);
        panel.add(numberField);
        panel.add(currencyLabel);
        panel.add(currencyField);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JFormattedTextField_Demo());
    }
}

