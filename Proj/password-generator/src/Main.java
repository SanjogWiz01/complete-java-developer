import java.security.SecureRandom;
import java.util.Scanner;

public class Main {
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*()-_=+[]{}";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Password Generator");
        System.out.print("Password length: ");
        int length = scanner.nextInt();

        if (length < 4 || length > 64) {
            System.out.println("Choose a length from 4 to 64.");
            return;
        }

        String pool = buildCharacterPool(scanner);
        if (pool.isEmpty()) {
            System.out.println("Select at least one character group.");
            return;
        }

        System.out.println("Generated password: " + generatePassword(length, pool));
    }

    private static String buildCharacterPool(Scanner scanner) {
        StringBuilder pool = new StringBuilder();
        if (answerYes(scanner, "Use lowercase letters? (y/n): ")) {
            pool.append(LOWERCASE);
        }
        if (answerYes(scanner, "Use uppercase letters? (y/n): ")) {
            pool.append(UPPERCASE);
        }
        if (answerYes(scanner, "Use digits? (y/n): ")) {
            pool.append(DIGITS);
        }
        if (answerYes(scanner, "Use symbols? (y/n): ")) {
            pool.append(SYMBOLS);
        }
        return pool.toString();
    }

    private static boolean answerYes(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.next().trim().equalsIgnoreCase("y");
    }

    private static String generatePassword(int length, String pool) {
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(pool.length());
            password.append(pool.charAt(index));
        }
        return password.toString();
    }
}
