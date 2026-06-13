import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class JTextArea_Formatting extends JFrame {
    private JTextArea textArea;

    public JTextArea_Formatting() {
        setTitle("JTextArea Formatting Demo");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        textArea = new JTextArea();
        textArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setText("This is a sample text area.\nYou can edit and format text here.\nLine wrapping is enabled.");
        textArea.setBackground(new Color(240, 240, 240));
        textArea.setForeground(Color.BLACK);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton boldButton = new JButton("Bold Font");
        boldButton.addActionListener(e -> textArea.setFont(new Font("Courier New", Font.BOLD, 12)));
        
        JButton italicButton = new JButton("Italic Font");
        italicButton.addActionListener(e -> textArea.setFont(new Font("Courier New", Font.ITALIC, 12)));
        
        JButton plainButton = new JButton("Plain Font");
        plainButton.addActionListener(e -> textArea.setFont(new Font("Courier New", Font.PLAIN, 12)));
        
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> textArea.setText(""));

        buttonPanel.add(boldButton);
        buttonPanel.add(italicButton);
        buttonPanel.add(plainButton);
        buttonPanel.add(clearButton);

        add(new JScrollPane(textArea), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JTextArea_Formatting());
    }
}

