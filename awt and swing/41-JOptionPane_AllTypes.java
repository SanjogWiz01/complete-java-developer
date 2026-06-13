import javax.swing.*;

class JOptionPane_AllTypes extends JFrame {
    public JOptionPane_AllTypes() {
        setTitle("JOptionPane Types Demo");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JButton messageButton = new JButton("Show Message");
        messageButton.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, "This is a message dialog", "Message", JOptionPane.INFORMATION_MESSAGE)
        );

        JButton confirmButton = new JButton("Show Confirm");
        confirmButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this, "Do you want to continue?", "Confirm", JOptionPane.YES_NO_CANCEL_OPTION);
            System.out.println("User selected: " + result);
        });

        JButton inputButton = new JButton("Show Input");
        inputButton.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Enter your name:");
            System.out.println("Input: " + input);
        });

        JButton warningButton = new JButton("Show Warning");
        warningButton.addActionListener(e ->
            JOptionPane.showMessageDialog(this, "This is a warning!", "Warning", JOptionPane.WARNING_MESSAGE)
        );

        JButton errorButton = new JButton("Show Error");
        errorButton.addActionListener(e ->
            JOptionPane.showMessageDialog(this, "An error occurred!", "Error", JOptionPane.ERROR_MESSAGE)
        );

        add(messageButton);
        add(confirmButton);
        add(inputButton);
        add(warningButton);
        add(errorButton);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JOptionPane_AllTypes());
    }
}

