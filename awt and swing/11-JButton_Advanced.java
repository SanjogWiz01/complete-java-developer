import javax.swing.*;
import java.awt.*;

class JButton_Advanced extends JFrame {
    public JButton_Advanced() {
        setTitle("Advanced JButton Demo");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton normalButton = new JButton("Normal Button");
        
        JButton toggleButton = new JButton("Toggle Button");
        toggleButton.setModel(new JToggleButton.ToggleButtonModel());
        
        JButton flatButton = new JButton("Flat Button");
        flatButton.setFocusPainted(false);
        flatButton.setBackground(new Color(70, 130, 180));
        flatButton.setForeground(Color.WHITE);
        flatButton.setOpaque(true);
        
        JButton iconButton = new JButton("With Icon");
        iconButton.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
        
        JButton roundButton = new JButton("Rounded");
        roundButton.setContentAreaFilled(false);
        roundButton.setBorderPainted(true);

        panel.add(normalButton);
        panel.add(toggleButton);
        panel.add(flatButton);
        panel.add(iconButton);
        panel.add(roundButton);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JButton_Advanced());
    }
}

