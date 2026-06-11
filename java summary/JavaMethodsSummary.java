public class JavaMethodsSummary {
    public static void main(String[] args) {
        printSummary();
        int total = add(12, 8);
        System.out.println("Total: " + total);
    }

    private static void printSummary() {
        String[] points = {
                "Methods group related statements under a reusable name.",
                "A method can receive input through parameters.",
                "A method can return one value using its return type.",
                "void means the method does not return a value.",
                "Method overloading allows methods with the same name but different parameter lists."
        };

        for (String point : points) {
            System.out.println("- " + point);
        }
    }

    private static int add(int first, int second) {
        return first + second;
    }

    private static double add(double first, double second) {
        return first + second;
    }
}
