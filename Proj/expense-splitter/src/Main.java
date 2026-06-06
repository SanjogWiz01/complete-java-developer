import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Expense Splitter");
        double subtotal = readPositive(scanner, "Bill subtotal: ");
        double tipPercent = readNonNegative(scanner, "Tip percent: ");
        int people = readPeople(scanner);

        double tipAmount = subtotal * tipPercent / 100.0;
        double total = subtotal + tipAmount;
        double share = total / people;

        System.out.printf("Tip amount: %.2f%n", tipAmount);
        System.out.printf("Final total: %.2f%n", total);
        System.out.printf("Each person pays: %.2f%n", share);
    }

    private static double readPositive(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            double value = scanner.nextDouble();
            if (value > 0.0) {
                return value;
            }
            System.out.println("Enter a value greater than zero.");
        }
    }

    private static double readNonNegative(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            double value = scanner.nextDouble();
            if (value >= 0.0) {
                return value;
            }
            System.out.println("Enter a value of zero or more.");
        }
    }

    private static int readPeople(Scanner scanner) {
        while (true) {
            System.out.print("Number of people: ");
            int people = scanner.nextInt();
            if (people > 0) {
                return people;
            }
            System.out.println("There must be at least one person.");
        }
    }
}
