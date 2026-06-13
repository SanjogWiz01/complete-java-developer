import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class KeyListener_Shortcuts extends JFrame {
    private JTextArea textArea;
    private JLabel statusLabel;

    public KeyListener_Shortcuts() {
        setTitle("Keyboard Shortcuts Demo");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setFont(new Font("Courier", Font.PLAIN, 12));
        statusLabel = new JLabel("Status: Ready | Shortcuts: Ctrl+B (Bold), Ctrl+I (Italic), Ctrl+N (New)");

        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown()) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_B:
                            textArea.setFont(new Font("Courier", Font.BOLD, 12));
                            statusLabel.setText("Bold mode enabled");
                            break;
                        case KeyEvent.VK_I:
                            textArea.setFont(new Font("Courier", Font.ITALIC, 12));
                            statusLabel.setText("Italic mode enabled");
                            break;
                        case KeyEvent.VK_N:
                            textArea.setText("");
                            textArea.setFont(new Font("Courier", Font.PLAIN, 12));
                            statusLabel.setText("New document");
                            break;
                    }
                }
            }
        });

        add(new JScrollPane(textArea), BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KeyListener_Shortcuts());
    }
}

