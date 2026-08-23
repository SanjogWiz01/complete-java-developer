package com.jyotibank.presentation;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * ConsoleIO — thin wrapper over System.in/out for the console UI.
 *
 * <p>Centralising input parsing here keeps every menu free of Scanner
 * boilerplate and gives consistent handling of bad input (re-prompt,
 * never crash).
 */
public final class ConsoleIO {

    private static final Scanner SCANNER = new Scanner(System.in);

    private ConsoleIO() {}

    public static String readLine(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine().trim();
    }

    public static String readNonEmpty(String prompt) {
        while (true) {
            String value = readLine(prompt);
            if (!value.isBlank()) return value;
            System.out.println("  Value must not be empty.");
        }
    }

    public static int readInt(String prompt, int min, int max) {
        while (true) {
            try {
                int value = Integer.parseInt(readLine(prompt));
                if (value >= min && value <= max) return value;
            } catch (NumberFormatException ignored) {
                // fall through to error message
            }
            System.out.println("  Enter a number between " + min + " and " + max + ".");
        }
    }

    public static long readLong(String prompt) {
        while (true) {
            try {
                return Long.parseLong(readLine(prompt));
            } catch (NumberFormatException ignored) {
                System.out.println("  Enter a valid number.");
            }
        }
    }

    public static BigDecimal readAmount(String prompt) {
        while (true) {
            try {
                return new BigDecimal(readNonEmpty(prompt));
            } catch (NumberFormatException ignored) {
                System.out.println("  Enter a valid amount, e.g. 1500.00");
            }
        }
    }

    public static boolean confirm(String prompt) {
        String answer = readLine(prompt + " [y/N]: ").toLowerCase();
        return answer.equals("y") || answer.equals("yes");
    }

    public static void pause() {
        readLine("\nPress ENTER to continue...");
    }

    public static void header(String title) {
        System.out.println();
        System.out.println("──── " + title + " ────");
    }

    public static void error(String message) {
        System.out.println("  [ERROR] " + message);
    }

    public static void info(String message) {
        System.out.println("  " + message);
    }
}
