import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final List<Task> TASKS = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addTask(scanner);
                    break;
                case "2":
                    listTasks();
                    break;
                case "3":
                    completeTask(scanner);
                    break;
                case "4":
                    deleteTask(scanner);
                    break;
                case "5":
                    running = false;
                    break;
                default:
                    System.out.println("Choose a menu option from 1 to 5.");
            }
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("To-Do List Console");
        System.out.println("1. Add task");
        System.out.println("2. List tasks");
        System.out.println("3. Complete task");
        System.out.println("4. Delete task");
        System.out.println("5. Exit");
        System.out.print("Choice: ");
    }

    private static void addTask(Scanner scanner) {
        System.out.print("Task title: ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) {
            System.out.println("Task title cannot be empty.");
            return;
        }
        TASKS.add(new Task(title));
        System.out.println("Task added.");
    }

    private static void listTasks() {
        if (TASKS.isEmpty()) {
            System.out.println("No tasks yet.");
            return;
        }

        for (int i = 0; i < TASKS.size(); i++) {
            Task task = TASKS.get(i);
            String status = task.completed ? "done" : "open";
            System.out.printf("%d. [%s] %s%n", i + 1, status, task.title);
        }
    }

    private static void completeTask(Scanner scanner) {
        int index = readTaskIndex(scanner);
        if (index == -1) {
            return;
        }
        TASKS.get(index).completed = true;
        System.out.println("Task completed.");
    }

    private static void deleteTask(Scanner scanner) {
        int index = readTaskIndex(scanner);
        if (index == -1) {
            return;
        }
        TASKS.remove(index);
        System.out.println("Task deleted.");
    }

    private static int readTaskIndex(Scanner scanner) {
        listTasks();
        if (TASKS.isEmpty()) {
            return -1;
        }

        System.out.print("Task number: ");
        try {
            int taskNumber = Integer.parseInt(scanner.nextLine().trim());
            if (taskNumber >= 1 && taskNumber <= TASKS.size()) {
                return taskNumber - 1;
            }
        } catch (NumberFormatException ignored) {
            // Fall through to the validation message below.
        }

        System.out.println("Invalid task number.");
        return -1;
    }

    private static class Task {
        private final String title;
        private boolean completed;

        private Task(String title) {
            this.title = title;
        }
    }
}
