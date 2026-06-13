import javax.swing.*;
import java.awt.*;

class Swing_InternalFrames extends JFrame {
    public Swing_InternalFrames() {
        setTitle("Internal Frames Demo");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JDesktopPane desktopPane = new JDesktopPane();
        desktopPane.setBackground(new Color(200, 200, 200));

        createInternalFrame(desktopPane, "Frame 1", 50, 50, 300, 200);
        createInternalFrame(desktopPane, "Frame 2", 100, 100, 300, 200);
        createInternalFrame(desktopPane, "Frame 3", 150, 150, 300, 200);

        add(desktopPane);
        setVisible(true);
    }

    private void createInternalFrame(JDesktopPane desktopPane, String title, int x, int y, int width, int height) {
        JInternalFrame internalFrame = new JInternalFrame(title, true, true, true, true);
        internalFrame.setSize(width, height);
        internalFrame.setLocation(x, y);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Content of " + title));
        internalFrame.add(panel);

        desktopPane.add(internalFrame);
        internalFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Swing_InternalFrames());
    }
}

