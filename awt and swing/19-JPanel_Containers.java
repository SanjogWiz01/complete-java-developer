import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

class JPanel_Containers extends JFrame {
    public JPanel_Containers() {
        setTitle("JPanel & Container Demo");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 2, 10, 10));

        JPanel panel1 = new JPanel();
        panel1.setBorder(new TitledBorder("Panel with TitledBorder"));
        panel1.setBackground(new Color(200, 220, 240));
        panel1.add(new JButton("Button 1"));

        JPanel panel2 = new JPanel();
        panel2.setBorder(new BevelBorder(BevelBorder.LOWERED));
        panel2.setBackground(new Color(240, 220, 200));
        panel2.add(new JLabel("Bevel Border Panel"));

        JPanel panel3 = new JPanel();
        panel3.setBorder(new LineBorder(Color.BLACK, 3));
        panel3.setBackground(new Color(220, 240, 200));
        panel3.add(new JCheckBox("Line Border Panel"));

        JPanel panel4 = new JPanel();
        panel4.setBorder(new CompoundBorder(
            new LineBorder(Color.BLUE, 2),
            new EmptyBorder(10, 10, 10, 10)
        ));
        panel4.setBackground(new Color(240, 200, 240));
        panel4.add(new JLabel("Compound Border"));

        add(panel1);
        add(panel2);
        add(panel3);
        add(panel4);
        
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JPanel_Containers());
    }
}

