import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridLayout;

public class GUIControlsAndMenuElements {
    public static void main(String[] args) {
        JFrame frame = new JFrame("GUI Controls & Menu Elements");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 220);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(new JMenuItem("New"));
        fileMenu.add(new JMenuItem("Open"));
        fileMenu.add(new JMenuItem("Exit"));
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        JPanel panel = new JPanel(new GridLayout(3, 2, 6, 6));
        panel.add(new JLabel("Name:"));
        panel.add(new JTextField());
        panel.add(new JLabel("Subscribe:"));
        panel.add(new JCheckBox());

        frame.setContentPane(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
