public class JavaVariablesSummary {
    public static void main(String[] args) {
        printSummary();
        showExample();
    }

    private static void printSummary() {
        String[] points = {
                "Variables store values that a program can read and change.",
                "Every variable has a type, a name, and usually an initial value.",
                "Primitive types store simple values such as int, double, boolean, and char.",
                "Reference types store addresses to objects such as String, arrays, and custom classes.",
                "Use final when a value should not be reassigned."
        };

        for (String point : points) {
            System.out.println("- " + point);
        }
    }

    private static void showExample() {
        int age = 21;
        double price = 99.99;
        boolean active = true;
        final String course = "Java";

        System.out.println("Age: " + age);
        System.out.println("Price: " + price);
        System.out.println("Active: " + active);
        System.out.println("Course: " + course);
    }
}
