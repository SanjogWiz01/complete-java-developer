import javax.swing.*;
import java.awt.*;

class LayoutManager_GridLayout extends JFrame {
    public LayoutManager_GridLayout() {
        setTitle("GridLayout Demo - Calculator");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 4, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] buttons = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+"
        };

        for (String btnText : buttons) {
            JButton btn = new JButton(btnText);
            btn.setFont(new Font("Arial", Font.BOLD, 20));
            panel.add(btn);
        }

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LayoutManager_GridLayout());
    }
}

