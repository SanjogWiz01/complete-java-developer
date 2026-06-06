import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final List<Book> BOOKS = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addBook(scanner);
                    break;
                case "2":
                    listBooks();
                    break;
                case "3":
                    updateCheckout(scanner, true);
                    break;
                case "4":
                    updateCheckout(scanner, false);
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
        System.out.println("Library Inventory CLI");
        System.out.println("1. Add book");
        System.out.println("2. List books");
        System.out.println("3. Check out book");
        System.out.println("4. Return book");
        System.out.println("5. Exit");
        System.out.print("Choice: ");
    }

    private static void addBook(Scanner scanner) {
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Author: ");
        String author = scanner.nextLine().trim();

        if (title.isEmpty() || author.isEmpty()) {
            System.out.println("Title and author are required.");
            return;
        }

        BOOKS.add(new Book(title, author));
        System.out.println("Book added.");
    }

    private static void listBooks() {
        if (BOOKS.isEmpty()) {
            System.out.println("No books in inventory.");
            return;
        }

        for (int i = 0; i < BOOKS.size(); i++) {
            Book book = BOOKS.get(i);
            String status = book.checkedOut ? "checked out" : "available";
            System.out.printf("%d. %s by %s (%s)%n", i + 1, book.title, book.author, status);
        }
    }

    private static void updateCheckout(Scanner scanner, boolean checkedOut) {
        int index = readBookIndex(scanner);
        if (index == -1) {
            return;
        }

        Book book = BOOKS.get(index);
        if (book.checkedOut == checkedOut) {
            System.out.println("Book is already " + (checkedOut ? "checked out." : "available."));
            return;
        }

        book.checkedOut = checkedOut;
        System.out.println(checkedOut ? "Book checked out." : "Book returned.");
    }

    private static int readBookIndex(Scanner scanner) {
        listBooks();
        if (BOOKS.isEmpty()) {
            return -1;
        }

        System.out.print("Book number: ");
        try {
            int bookNumber = Integer.parseInt(scanner.nextLine().trim());
            if (bookNumber >= 1 && bookNumber <= BOOKS.size()) {
                return bookNumber - 1;
            }
        } catch (NumberFormatException ignored) {
            // Fall through to the validation message below.
        }

        System.out.println("Invalid book number.");
        return -1;
    }

    private static class Book {
        private final String title;
        private final String author;
        private boolean checkedOut;

        private Book(String title, String author) {
            this.title = title;
            this.author = author;
        }
    }
}
