import javax.swing.*;
import java.awt.*;

class Graphics_DrawingShapes extends JFrame {
    public Graphics_DrawingShapes() {
        setTitle("Drawing Shapes Demo");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g.setColor(Color.BLUE);
                g.drawRect(50, 50, 100, 80);
                g.fillRect(200, 50, 100, 80);

                g.setColor(Color.RED);
                g.drawOval(350, 50, 100, 100);
                g.fillOval(480, 50, 100, 100);

                g.setColor(Color.GREEN);
                int[] xPoints = {50, 100, 75};
                int[] yPoints = {250, 250, 300};
                g.drawPolygon(xPoints, yPoints, 3);
                g.fillPolygon(xPoints, yPoints, 3);

                g.setColor(Color.ORANGE);
                g.drawArc(200, 200, 150, 150, 0, 90);
                g.fillArc(200, 200, 150, 150, 0, 90);

                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.BOLD, 16));
                g.drawString("Graphics Demo", 350, 100);
            }
        };

        panel.setBackground(new Color(240, 240, 240));
        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Graphics_DrawingShapes());
    }
}

