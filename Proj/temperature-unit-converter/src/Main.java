import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Temperature Unit Converter");
        System.out.print("Temperature value: ");
        double value = scanner.nextDouble();

        System.out.print("From unit (C/F/K): ");
        String from = scanner.next().toUpperCase(Locale.ROOT);

        System.out.print("To unit (C/F/K): ");
        String to = scanner.next().toUpperCase(Locale.ROOT);

        if (!isValidUnit(from) || !isValidUnit(to)) {
            System.out.println("Use only C, F, or K.");
            return;
        }

        double converted = convert(value, from, to);
        System.out.printf("%.2f %s = %.2f %s%n", value, from, converted, to);
    }

    private static boolean isValidUnit(String unit) {
        return unit.equals("C") || unit.equals("F") || unit.equals("K");
    }

    private static double convert(double value, String from, String to) {
        double celsius = toCelsius(value, from);
        return fromCelsius(celsius, to);
    }

    private static double toCelsius(double value, String unit) {
        if (unit.equals("F")) {
            return (value - 32.0) * 5.0 / 9.0;
        }
        if (unit.equals("K")) {
            return value - 273.15;
        }
        return value;
    }

    private static double fromCelsius(double celsius, String unit) {
        if (unit.equals("F")) {
            return celsius * 9.0 / 5.0 + 32.0;
        }
        if (unit.equals("K")) {
            return celsius + 273.15;
        }
        return celsius;
    }
}
