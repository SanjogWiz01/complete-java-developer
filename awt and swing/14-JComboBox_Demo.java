import javax.swing.*;
import java.awt.*;

class JComboBox_Demo extends JFrame {
    public JComboBox_Demo() {
        setTitle("JComboBox Demo");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel countryLabel = new JLabel("Select Country:");
        String[] countries = {"USA", "India", "Japan", "Germany", "France"};
        JComboBox<String> countryCombo = new JComboBox<>(countries);
        
        JLabel skillLabel = new JLabel("Select Skill:");
        String[] skills = {"Java", "Python", "JavaScript", "C++", "Go"};
        JComboBox<String> skillCombo = new JComboBox<>(skills);
        
        JLabel statusLabel = new JLabel("Status:");
        JLabel selectedLabel = new JLabel("None");

        countryCombo.addActionListener(e -> 
            selectedLabel.setText("Country: " + countryCombo.getSelectedItem())
        );

        skillCombo.addActionListener(e ->
            selectedLabel.setText("Skill: " + skillCombo.getSelectedItem())
        );

        panel.add(countryLabel);
        panel.add(countryCombo);
        panel.add(skillLabel);
        panel.add(skillCombo);
        panel.add(statusLabel);
        panel.add(selectedLabel);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JComboBox_Demo());
    }
}

