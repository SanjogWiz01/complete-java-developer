import javax.swing.*;
import java.awt.*;

class JDialog_Demo extends JFrame {
    public JDialog_Demo() {
        setTitle("JDialog Demo");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JButton modalButton = new JButton("Show Modal Dialog");
        modalButton.addActionListener(e -> {
            JDialog dialog = new JDialog(this, "Modal Dialog", true);
            dialog.setSize(300, 150);
            dialog.setLayout(new FlowLayout());
            dialog.add(new JLabel("This is a modal dialog. Close it to continue."));
            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(ce -> dialog.dispose());
            dialog.add(closeButton);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });

        JButton modelessButton = new JButton("Show Modeless Dialog");
        modelessButton.addActionListener(e -> {
            JDialog dialog = new JDialog(this, "Modeless Dialog", false);
            dialog.setSize(300, 150);
            dialog.setLayout(new FlowLayout());
            dialog.add(new JLabel("This is a modeless dialog."));
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });

        add(modalButton);
        add(modelessButton);
        
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JDialog_Demo());
    }
}

