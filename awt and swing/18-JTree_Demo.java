import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;

class JTree_Demo extends JFrame {
    public JTree_Demo() {
        setTitle("JTree Demo");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Company");
        
        DefaultMutableTreeNode engineering = new DefaultMutableTreeNode("Engineering");
        DefaultMutableTreeNode backend = new DefaultMutableTreeNode("Backend");
        DefaultMutableTreeNode frontend = new DefaultMutableTreeNode("Frontend");
        engineering.add(backend);
        engineering.add(frontend);
        
        DefaultMutableTreeNode sales = new DefaultMutableTreeNode("Sales");
        DefaultMutableTreeNode marketing = new DefaultMutableTreeNode("Marketing");
        
        backend.add(new DefaultMutableTreeNode("Java Developer"));
        backend.add(new DefaultMutableTreeNode("Python Developer"));
        frontend.add(new DefaultMutableTreeNode("React Developer"));
        frontend.add(new DefaultMutableTreeNode("Angular Developer"));
        
        root.add(engineering);
        root.add(sales);
        root.add(marketing);

        JTree tree = new JTree(root);
        tree.setFont(new Font("Arial", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(tree);
        
        JLabel statusLabel = new JLabel("Select a node");
        
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (node != null) {
                statusLabel.setText("Selected: " + node.getUserObject());
            }
        });

        add(scrollPane, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
        
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JTree_Demo());
    }
}

