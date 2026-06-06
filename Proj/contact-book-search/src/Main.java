import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    private static final List<Contact> CONTACTS = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addContact(scanner);
                    break;
                case "2":
                    listContacts(CONTACTS);
                    break;
                case "3":
                    searchContacts(scanner);
                    break;
                case "4":
                    running = false;
                    break;
                default:
                    System.out.println("Choose a menu option from 1 to 4.");
            }
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("Contact Book Search");
        System.out.println("1. Add contact");
        System.out.println("2. List contacts");
        System.out.println("3. Search by name");
        System.out.println("4. Exit");
        System.out.print("Choice: ");
    }

    private static void addContact(Scanner scanner) {
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Phone: ");
        String phone = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            System.out.println("All fields are required.");
            return;
        }

        CONTACTS.add(new Contact(name, phone, email));
        System.out.println("Contact added.");
    }

    private static void searchContacts(Scanner scanner) {
        System.out.print("Search name: ");
        String query = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
        List<Contact> matches = new ArrayList<>();

        for (Contact contact : CONTACTS) {
            if (contact.name.toLowerCase(Locale.ROOT).contains(query)) {
                matches.add(contact);
            }
        }

        listContacts(matches);
    }

    private static void listContacts(List<Contact> contacts) {
        if (contacts.isEmpty()) {
            System.out.println("No contacts found.");
            return;
        }

        for (int i = 0; i < contacts.size(); i++) {
            Contact contact = contacts.get(i);
            System.out.printf("%d. %s | %s | %s%n", i + 1, contact.name, contact.phone, contact.email);
        }
    }

    private static class Contact {
        private final String name;
        private final String phone;
        private final String email;

        private Contact(String name, String phone, String email) {
            this.name = name;
            this.phone = phone;
            this.email = email;
        }
    }
}
