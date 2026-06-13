import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;

class DocumentListener_Demo extends JFrame {
    public DocumentListener_Demo() {
        setTitle("DocumentListener Demo");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel charCountLabel = new JLabel("Characters: 0");
        JLabel wordCountLabel = new JLabel("Words: 0");
        JLabel lineCountLabel = new JLabel("Lines: 0");

        JTextArea textArea = new JTextArea(10, 40);
        textArea.setFont(new Font("Arial", Font.PLAIN, 12));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        textArea.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateCounts(); }
            public void removeUpdate(DocumentEvent e) { updateCounts(); }
            public void changedUpdate(DocumentEvent e) { updateCounts(); }

            void updateCounts() {
                String text = textArea.getText();
                charCountLabel.setText("Characters: " + text.length());
                wordCountLabel.setText("Words: " + text.split("\\s+").length);
                lineCountLabel.setText("Lines: " + textArea.getLineCount());
            }
        });

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        statsPanel.add(charCountLabel);
        statsPanel.add(wordCountLabel);
        statsPanel.add(lineCountLabel);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(new JScrollPane(textArea), BorderLayout.CENTER);
        add(statsPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DocumentListener_Demo());
    }
}

