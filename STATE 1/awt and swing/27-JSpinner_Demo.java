import javax.swing.*;
import java.awt.*;

class JSpinner_Demo extends JFrame {
    public JSpinner_Demo() {
        setTitle("JSpinner Demo");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel numLabel = new JLabel("Number Spinner:");
        JSpinner numSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        
        JLabel doubleLabel = new JLabel("Double Spinner:");
        JSpinner doubleSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 10.0, 0.5));
        
        JLabel dateLabel = new JLabel("Date Spinner:");
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        
        JLabel listLabel = new JLabel("List Spinner:");
        JSpinner listSpinner = new JSpinner(new SpinnerListModel(new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"}));

        panel.add(numLabel);
        panel.add(numSpinner);
        panel.add(doubleLabel);
        panel.add(doubleSpinner);
        panel.add(dateLabel);
        panel.add(dateSpinner);
        panel.add(listLabel);
        panel.add(listSpinner);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JSpinner_Demo());
    }
}

