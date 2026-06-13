import javax.swing.*;
import java.awt.*;

class JTable_Demo extends JFrame {
    public JTable_Demo() {
        setTitle("JTable Demo");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String[] columns = {"ID", "Name", "Department", "Salary"};
        Object[][] data = {
            {1, "Alice Johnson", "Engineering", 85000},
            {2, "Bob Smith", "Marketing", 65000},
            {3, "Carol White", "Sales", 70000},
            {4, "David Brown", "HR", 60000},
            {5, "Eve Davis", "Engineering", 90000},
            {6, "Frank Wilson", "Finance", 75000}
        };

        JTable table = new JTable(data, columns);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        
        JLabel statusLabel = new JLabel("Select a row to view details");
        
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                statusLabel.setText("Selected: " + table.getValueAt(row, 1) + 
                                  " - Salary: $" + table.getValueAt(row, 3));
            }
        });

        add(scrollPane, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
        
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JTable_Demo());
    }
}

