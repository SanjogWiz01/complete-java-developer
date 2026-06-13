import javax.swing.*;
import java.awt.*;

class JCheckBox_JRadioButton extends JFrame {
    public JCheckBox_JRadioButton() {
        setTitle("CheckBox and RadioButton Demo");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel checkPanel = new JPanel();
        checkPanel.setBorder(BorderFactory.createTitledBorder("Interests"));
        checkPanel.setLayout(new GridLayout(3, 1));
        
        JCheckBox java = new JCheckBox("Java");
        JCheckBox web = new JCheckBox("Web Development");
        JCheckBox ai = new JCheckBox("AI/ML");
        
        java.addActionListener(e -> System.out.println("Java: " + java.isSelected()));
        web.addActionListener(e -> System.out.println("Web: " + web.isSelected()));
        ai.addActionListener(e -> System.out.println("AI/ML: " + ai.isSelected()));
        
        checkPanel.add(java);
        checkPanel.add(web);
        checkPanel.add(ai);

        JPanel radioPanel = new JPanel();
        radioPanel.setBorder(BorderFactory.createTitledBorder("Experience"));
        radioPanel.setLayout(new GridLayout(3, 1));
        
        ButtonGroup group = new ButtonGroup();
        JRadioButton junior = new JRadioButton("Junior (0-2 years)");
        JRadioButton mid = new JRadioButton("Mid (2-5 years)");
        JRadioButton senior = new JRadioButton("Senior (5+ years)");
        
        group.add(junior);
        group.add(mid);
        group.add(senior);
        
        radioPanel.add(junior);
        radioPanel.add(mid);
        radioPanel.add(senior);

        mainPanel.add(checkPanel);
        mainPanel.add(radioPanel);
        
        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JCheckBox_JRadioButton());
    }
}

