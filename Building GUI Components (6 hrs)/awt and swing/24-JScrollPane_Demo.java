import javax.swing.*;
import java.awt.*;

class JScrollPane_Demo extends JFrame {
    public JScrollPane_Demo() {
        setTitle("JScrollPane Demo");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextArea textArea = new JTextArea(20, 40);
        textArea.setFont(new Font("Courier", Font.PLAIN, 12));
        
        StringBuilder content = new StringBuilder();
        for (int i = 1; i <= 50; i++) {
            content.append("Line ").append(i).append(": This is a sample line of text.\n");
        }
        textArea.setText(content.toString());
        textArea.setEditable(true);

        JScrollPane scrollPane = new JScrollPane(textArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JScrollPane_Demo());
    }
}

