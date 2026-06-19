import javax.swing.*;
import java.awt.*;

class TodoApp_Project extends JFrame {
    private DefaultListModel<String> todoModel;
    private JList<String> todoList;
    private JTextField inputField;

    public TodoApp_Project() {
        setTitle("Todo Application");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        todoModel = new DefaultListModel<>();
        todoList = new JList<>(todoModel);
        todoList.setFont(new Font("Arial", Font.PLAIN, 14));

        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        JButton completeButton = new JButton("Mark Complete");

        addButton.addActionListener(e -> addTodo());
        deleteButton.addActionListener(e -> deleteTodo());
        completeButton.addActionListener(e -> completeTodo());

        inputField.addActionListener(e -> addTodo());

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(deleteButton);
        buttonPanel.add(completeButton);

        add(new JScrollPane(todoList), BorderLayout.CENTER);
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void addTodo() {
        String text = inputField.getText().trim();
        if (!text.isEmpty()) {
            todoModel.addElement("[ ] " + text);
            inputField.setText("");
        }
    }

    private void deleteTodo() {
        int selectedIndex = todoList.getSelectedIndex();
        if (selectedIndex >= 0) {
            todoModel.remove(selectedIndex);
        }
    }

    private void completeTodo() {
        int selectedIndex = todoList.getSelectedIndex();
        if (selectedIndex >= 0) {
            String item = todoModel.get(selectedIndex);
            if (item.startsWith("[ ]")) {
                todoModel.set(selectedIndex, "[X] " + item.substring(4));
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TodoApp_Project());
    }
}
