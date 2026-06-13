import javax.swing.*;
import java.awt.*;

class LayoutManager_FlowLayout extends JFrame {
    public LayoutManager_FlowLayout() {
        setTitle("FlowLayout Demo");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panel.setBackground(new Color(240, 240, 240));

        for (int i = 1; i <= 8; i++) {
            JButton btn = new JButton("Button " + i);
            btn.setPreferredSize(new Dimension(100, 50));
            panel.add(btn);
        }

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LayoutManager_FlowLayout());
    }
}

