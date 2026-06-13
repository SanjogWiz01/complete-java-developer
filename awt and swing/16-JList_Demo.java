import javax.swing.*;
import java.awt.*;

class JList_Demo extends JFrame {
    public JList_Demo() {
        setTitle("JList Demo");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        String[] cities = {"New York", "Los Angeles", "Chicago", "Houston", "Phoenix", 
                          "Philadelphia", "San Antonio", "San Diego", "Dallas", "San Jose"};
        
        JList<String> list = new JList<>(cities);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setVisibleRowCount(5);
        list.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel selectedLabel = new JLabel("Selected: None");
        
        list.addListSelectionListener(e -> {
            java.util.List<String> selected = list.getSelectedValuesList();
            selectedLabel.setText("Selected: " + selected);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton selectAllButton = new JButton("Select All");
        selectAllButton.addActionListener(e -> {
            list.setSelectionInterval(0, list.getModel().getSize() - 1);
        });
        
        JButton clearButton = new JButton("Clear Selection");
        clearButton.addActionListener(e -> list.clearSelection());

        buttonPanel.add(selectAllButton);
        buttonPanel.add(clearButton);

        add(new JScrollPane(list), BorderLayout.CENTER);
        add(selectedLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
        
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JList_Demo());
    }
}

