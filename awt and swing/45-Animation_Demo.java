import javax.swing.*;
import java.awt.*;

class Animation_Demo extends JFrame {
    public Animation_Demo() {
        setTitle("Animation Demo");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new AnimationPanel();
        add(panel);
        setVisible(true);
    }

    static class AnimationPanel extends JPanel {
        private int x = 0;
        private int y = 0;
        private int dx = 5;
        private int dy = 5;

        public AnimationPanel() {
            Timer timer = new Timer(50, e -> {
                x += dx;
                y += dy;

                if (x < 0 || x > getWidth() - 50) dx = -dx;
                if (y < 0 || y > getHeight() - 50) dy = -dy;

                repaint();
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(Color.BLUE);
            g2.fillOval(x, y, 50, 50);

            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString("Moving Circle", 10, 30);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Animation_Demo());
    }
}

