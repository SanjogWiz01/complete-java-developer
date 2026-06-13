import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class CustomComponent_Paint extends JFrame {
    public CustomComponent_Paint() {
        setTitle("Custom Component with Painting");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DrawingPanel panel = new DrawingPanel();
        add(panel);
        setVisible(true);
    }

    static class DrawingPanel extends JPanel {
        private int mouseX = 0;
        private int mouseY = 0;

        public DrawingPanel() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    mouseX = e.getX();
                    mouseY = e.getY();
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    mouseX = e.getX();
                    mouseY = e.getY();
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(Color.BLUE);
            g2.fillOval(mouseX - 20, mouseY - 20, 40, 40);

            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString("Move mouse to draw", 10, 30);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CustomComponent_Paint());
    }
}

