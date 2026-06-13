import javax.swing.*;
import java.awt.*;

class LayoutManager_BorderLayout extends JFrame {
    public LayoutManager_BorderLayout() {
        setTitle("BorderLayout Demo");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JButton north = new JButton("North");
        JButton south = new JButton("South");
        JButton east = new JButton("East");
        JButton west = new JButton("West");
        JButton center = new JButton("Center");

        north.setPreferredSize(new Dimension(0, 50));
        south.setPreferredSize(new Dimension(0, 50));
        east.setPreferredSize(new Dimension(100, 0));
        west.setPreferredSize(new Dimension(100, 0));

        add(north, BorderLayout.NORTH);
        add(south, BorderLayout.SOUTH);
        add(east, BorderLayout.EAST);
        add(west, BorderLayout.WEST);
        add(center, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LayoutManager_BorderLayout());
    }
}

