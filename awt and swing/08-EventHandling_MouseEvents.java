import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

class EventHandling_MouseEvents extends JFrame {
    private JLabel statusLabel;
    private JPanel drawPanel;

    public EventHandling_MouseEvents() {
        setTitle("Mouse Events Demo");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        statusLabel = new JLabel("Mouse Position: N/A | Button: N/A");
        drawPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawString("Move mouse to see coordinates", 10, 20);
            }
        };
        drawPanel.setBackground(new Color(240, 240, 240));

        drawPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                statusLabel.setText("Mouse Entered Panel");
            }
            @Override
            public void mouseExited(MouseEvent e) {
                statusLabel.setText("Mouse Exited Panel");
            }
            @Override
            public void mousePressed(MouseEvent e) {
                statusLabel.setText("Button: " + e.getButton() + " Pressed at (" + e.getX() + ", " + e.getY() + ")");
            }
        });

        drawPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                statusLabel.setText("Position: (" + e.getX() + ", " + e.getY() + ")");
            }
        });

        add(statusLabel, BorderLayout.NORTH);
        add(drawPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EventHandling_MouseEvents());
    }
}

