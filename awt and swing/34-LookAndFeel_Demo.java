import javax.swing.*;
import java.awt.*;

class LookAndFeel_Demo extends JFrame {
    public LookAndFeel_Demo() {
        setTitle("Look and Feel Demo");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 10, 10));

        JButton demoButton = new JButton("Demo Button");
        JCheckBox demoCheck = new JCheckBox("Demo Checkbox");
        JRadioButton demoRadio = new JRadioButton("Demo Radio");
        JComboBox<String> demoCombo = new JComboBox<>(new String[]{"Option 1", "Option 2"});
        JTextArea demoText = new JTextArea("Demo Text Area");

        add(new JLabel("Current Theme: " + UIManager.getLookAndFeel().getName()));
        add(new JButton("Current Theme"));

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
            
            add(new JLabel("Metal Theme"));
            JButton metalBtn = new JButton("Apply Metal");
            metalBtn.addActionListener(e -> applyLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel"));
            add(metalBtn);
            
            add(new JLabel("System Theme"));
            JButton systemBtn = new JButton("Apply System");
            systemBtn.addActionListener(e -> applyLookAndFeel(UIManager.getSystemLookAndFeelClassName()));
            add(systemBtn);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        add(demoButton);
        add(demoCheck);

        setVisible(true);
    }

    private void applyLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LookAndFeel_Demo());
    }
}

