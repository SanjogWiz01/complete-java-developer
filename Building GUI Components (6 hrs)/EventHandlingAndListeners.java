import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EventHandlingAndListeners {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Event Handling & Listeners");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 180);

        JPanel panel = new JPanel();
        JLabel label = new JLabel("Button not clicked yet.");
        JButton button = new JButton("Click");

        button.addActionListener(e -> label.setText("Button clicked at least once!"));

        panel.add(label);
        panel.add(button);
        frame.setContentPane(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
