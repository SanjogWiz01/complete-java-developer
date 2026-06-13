import javax.swing.*;
import java.awt.event.*;

class EventHandling_ButtonClick extends JFrame {
    private JButton button;
    private JLabel label;
    private int clickCount = 0;

    public EventHandling_ButtonClick() {
        setTitle("Button Click Event Demo");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        label = new JLabel("Clicks: " + clickCount);
        button = new JButton("Click Me");

        button.addActionListener(e -> {
            clickCount++;
            label.setText("Clicks: " + clickCount);
            System.out.println("Button clicked " + clickCount + " times");
        });

        add(label);
        add(button);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EventHandling_ButtonClick());
    }
}

