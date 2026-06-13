import javax.swing.*;
import java.awt.*;
import java.io.File;

class ImageHandling_Display extends JFrame {
    public ImageHandling_Display() {
        setTitle("Image Display Demo");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel imageLabel = new JLabel("Image not loaded");
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setBackground(new Color(200, 200, 200));
        imageLabel.setOpaque(true);

        JButton loadButton = new JButton("Load Image");
        loadButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Image files", "jpg", "jpeg", "png", "gif"
            ));

            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                String path = chooser.getSelectedFile().getAbsolutePath();
                ImageIcon icon = new ImageIcon(path);
                Image scaledImage = icon.getImage().getScaledInstance(600, 400, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
                imageLabel.setText("");
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loadButton);

        add(new JScrollPane(imageLabel), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ImageHandling_Display());
    }
}

