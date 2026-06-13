import javax.swing.*;
import java.awt.*;

class ResponsiveUI_LayoutResize extends JFrame {
    public ResponsiveUI_LayoutResize() {
        setTitle("Responsive UI Demo");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(100, 150, 200));
        topPanel.add(new JLabel("Header"));
        topPanel.setPreferredSize(new Dimension(0, 60));

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(200, 150, 100));
        leftPanel.setPreferredSize(new Dimension(150, 0));
        leftPanel.add(new JLabel("Sidebar"));

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(200, 200, 200));
        centerPanel.setLayout(new GridLayout(2, 2, 10, 10));
        for (int i = 1; i <= 4; i++) {
            JPanel card = new JPanel();
            card.setBackground(new Color(100, 100, 100 + i * 30));
            card.add(new JLabel("Card " + i));
            centerPanel.add(card);
        }

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(150, 100, 200));
        bottomPanel.add(new JLabel("Footer"));
        bottomPanel.setPreferredSize(new Dimension(0, 40));

        add(topPanel, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ResponsiveUI_LayoutResize());
    }
}

