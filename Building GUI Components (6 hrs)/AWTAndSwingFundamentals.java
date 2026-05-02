import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class AWTAndSwingFundamentals {
    public static void main(String[] args) {
        JFrame frame = new JFrame("AWT & Swing Fundamentals");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 180);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Swing uses AWT underneath for windowing."), BorderLayout.CENTER);
        panel.add(new JButton("Click Me"), BorderLayout.SOUTH);

        frame.setContentPane(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
