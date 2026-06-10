import javax.swing.*;
import java.awt.*;
import java.fx

public class JavaFXLayouts {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Layout Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

            JButton b1 = new JButton("Button 1");
            JButton b2 = new JButton("Button 2");
            JButton b3 = new JButton("Button 3");

            b1.setAlignmentX(Component.CENTER_ALIGNMENT);
            b2.setAlignmentX(Component.CENTER_ALIGNMENT);
            b3.setAlignmentX(Component.CENTER_ALIGNMENT);

            panel.add(b1);
            panel.add(Box.createRigidArea(new Dimension(0,10)));
            panel.add(b2);
            panel.add(Box.createRigidArea(new Dimension(0,10)));
            panel.add(b3);

            frame.getContentPane().add(panel);
            frame.setSize(300, 200);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
            frame.setVisible(true);
        });
    }
}