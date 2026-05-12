import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

class HelloButtonSwing {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Hello Button");
            JButton button = new JButton("Hello");
            JPanel panel = new JPanel();

            button.addActionListener(event -> button.setText("Hello Java Swing!"));
            panel.add(button);

            frame.setContentPane(panel);
            frame.setSize(300, 180);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
} // chnage the text of button when we click on it.
