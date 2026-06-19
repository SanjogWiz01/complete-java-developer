import javax.swing.*;
import java.awt.*;

class JSplitPane_Demo extends JFrame {
    public JSplitPane_Demo() {
        setTitle("JSplitPane Demo");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JList<String> list = new JList<>(new String[]{
            "Item 1", "Item 2", "Item 3", "Item 4", "Item 5"
        });
        
        JTextArea textArea = new JTextArea();
        textArea.setText("Select an item from the list");
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        list.addListSelectionListener(e -> {
            textArea.setText("You selected: " + list.getSelectedValue());
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(list),
                new JScrollPane(textArea));
        
        splitPane.setDividerLocation(200);
        splitPane.setOneTouchExpandable(true);
        splitPane.setContinuousLayout(true);

        add(splitPane);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JSplitPane_Demo());
    }
}

