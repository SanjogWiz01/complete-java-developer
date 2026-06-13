import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class MouseListener_Draw extends JFrame {
    public MouseListener_Draw() {
        setTitle("Mouse Drawing App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DrawCanvas canvas = new DrawCanvas();
        add(canvas);
        setVisible(true);
    }

    static class DrawCanvas extends JPanel {
        private java.util.List<Point> points = new ArrayList<>();
        private Point currentPoint;

        public DrawCanvas() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    currentPoint = e.getPoint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    currentPoint = null;
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    points.add(e.getPoint());
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
            g2.setStroke(new BasicStroke(2));

            for (int i = 0; i < points.size() - 1; i++) {
                Point p1 = points.get(i);
                Point p2 = points.get(i + 1);
                g2.drawLine(p1.x, p1.y, p2.x, p2.y);
            }

            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.drawString("Draw by dragging mouse | Points: " + points.size(), 10, 20);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MouseListener_Draw());
    }
}

