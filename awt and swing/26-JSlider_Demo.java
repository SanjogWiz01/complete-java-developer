import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;

class JSlider_Demo extends JFrame {
    public JSlider_Demo() {
        setTitle("JSlider Demo");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JSlider horizontalSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        horizontalSlider.setMajorTickSpacing(10);
        horizontalSlider.setMinorTickSpacing(1);
        horizontalSlider.setPaintTicks(true);
        horizontalSlider.setPaintLabels(true);

        JSlider verticalSlider = new JSlider(JSlider.VERTICAL, 0, 100, 50);
        verticalSlider.setMajorTickSpacing(10);
        verticalSlider.setMinorTickSpacing(1);
        verticalSlider.setPaintTicks(true);
        verticalSlider.setPaintLabels(true);

        JLabel valueLabel = new JLabel("Value: 50");
        valueLabel.setFont(new Font("Arial", Font.BOLD, 16));

        horizontalSlider.addChangeListener(e -> {
            valueLabel.setText("Value: " + horizontalSlider.getValue());
        });

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(horizontalSlider, BorderLayout.NORTH);
        mainPanel.add(verticalSlider, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
        add(valueLabel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JSlider_Demo());
    }
}

