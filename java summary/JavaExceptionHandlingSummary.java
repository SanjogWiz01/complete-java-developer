public class JavaExceptionHandlingSummary {
    public static void main(String[] args) {
        printSummary();
        showExample();
    }

    private static void printSummary() {
        String[] points = {
                "Exceptions represent errors or unusual situations during program execution.",
                "try contains code that might fail.",
                "catch handles a specific exception type.",
                "finally runs after try and catch, whether an exception happens or not.",
                "Use checked exceptions for recoverable problems and unchecked exceptions for programming mistakes."
        };

        for (String point : points) {
            System.out.println("- " + point);
        }
    }

    private static void showExample() {
        String value = "42";

        try {
            int number = Integer.parseInt(value);
            System.out.println("Parsed number: " + number);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid number format.");
        } finally {
            System.out.println("Parsing example finished.");
        }
    }
}
