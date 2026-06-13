import javax.swing.*;
import java.io.*;

class JFileChooser_Demo extends JFrame {
    private JLabel selectedFileLabel;

    public JFileChooser_Demo() {
        setTitle("JFileChooser Demo");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        selectedFileLabel = new JLabel("No file selected");

        JButton openButton = new JButton("Open File");
        openButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                selectedFileLabel.setText("Selected: " + selectedFile.getAbsolutePath());
            }
        });

        JButton saveButton = new JButton("Save File");
        saveButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                selectedFileLabel.setText("Save as: " + selectedFile.getAbsolutePath());
            }
        });

        JButton folderButton = new JButton("Select Folder");
        folderButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                selectedFileLabel.setText("Folder: " + selectedFile.getAbsolutePath());
            }
        });

        add(openButton);
        add(saveButton);
        add(folderButton);
        add(selectedFileLabel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JFileChooser_Demo());
    }
}

