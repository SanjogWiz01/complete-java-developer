import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

public class LayoutManagement {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Layout Management Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 260);

        JPanel root = new JPanel(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout());
        top.add(new JButton("Flow 1"));
        top.add(new JButton("Flow 2"));

        JPanel center = new JPanel(new GridLayout(2, 2, 8, 8));
        center.add(new JButton("Grid 1"));
        center.add(new JButton("Grid 2"));
        center.add(new JButton("Grid 3"));
        center.add(new JButton("Grid 4"));

        root.add(top, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);

        frame.setContentPane(root);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
