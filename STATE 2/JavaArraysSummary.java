public class JavaArraysSummary {
    public static void main(String[] args) {
        printSummary();
        showExample();
    }

    private static void printSummary() {
        String[] points = {
                "Arrays store multiple values of the same type in one variable.",
                "Array indexes start at 0.",
                "The length property gives the number of elements.",
                "Use a normal for loop when the index matters.",
                "Use an enhanced for loop when only the value matters."
        };

        for (String point : points) {
            System.out.println("- " + point);
        }
    }

    private static void showExample() {
        int[] marks = {75, 88, 91, 67};
        int total = 0;

        for (int mark : marks) {
            total += mark;
        }

        double average = (double) total / marks.length;
        System.out.println("Average marks: " + average);
    }
}
