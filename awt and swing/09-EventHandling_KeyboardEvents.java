import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class EventHandling_KeyboardEvents extends JFrame {
    private JTextArea textArea;
    private JLabel statusLabel;

    public EventHandling_KeyboardEvents() {
        setTitle("Keyboard Events Demo");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        statusLabel = new JLabel("Press any key...");

        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                statusLabel.setText("Key Pressed: " + KeyEvent.getKeyText(e.getKeyCode()));
            }
            @Override
            public void keyReleased(KeyEvent e) {
                statusLabel.setText("Key Released: " + KeyEvent.getKeyText(e.getKeyCode()));
            }
            @Override
            public void keyTyped(KeyEvent e) {
                statusLabel.setText("Key Typed: " + e.getKeyChar());
            }
        });

        add(new JScrollPane(textArea), BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EventHandling_KeyboardEvents());
    }
}

