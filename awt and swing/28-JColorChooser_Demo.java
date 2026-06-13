import javax.swing.*;
import java.awt.*;

class JColorChooser_Demo extends JFrame {
    private JPanel colorPanel;
    private JLabel colorLabel;

    public JColorChooser_Demo() {
        setTitle("JColorChooser Demo");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        colorPanel = new JPanel();
        colorPanel.setBackground(Color.WHITE);
        colorPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        colorLabel = new JLabel("Selected Color: RGB(255, 255, 255)");
        colorLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JButton colorChooserButton = new JButton("Choose Color");
        colorChooserButton.addActionListener(e -> {
            Color selectedColor = JColorChooser.showDialog(
                this,
                "Select a Color",
                Color.WHITE
            );
            
            if (selectedColor != null) {
                colorPanel.setBackground(selectedColor);
                colorLabel.setText(String.format(
                    "Selected Color: RGB(%d, %d, %d)",
                    selectedColor.getRed(),
                    selectedColor.getGreen(),
                    selectedColor.getBlue()
                ));
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(colorChooserButton);

        add(colorPanel, BorderLayout.CENTER);
        add(colorLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JColorChooser_Demo());
    }
}

