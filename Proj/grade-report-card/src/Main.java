import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Grade Report Card");
        System.out.print("Number of subjects: ");
        int subjectCount = scanner.nextInt();

        if (subjectCount <= 0) {
            System.out.println("Subject count must be greater than zero.");
            return;
        }

        double total = 0.0;
        for (int i = 1; i <= subjectCount; i++) {
            double mark = readMark(scanner, i);
            total += mark;
        }

        double average = total / subjectCount;
        System.out.printf("Total: %.2f%n", total);
        System.out.printf("Average: %.2f%n", average);
        System.out.println("Grade: " + gradeFor(average));
    }

    private static double readMark(Scanner scanner, int subjectNumber) {
        while (true) {
            System.out.print("Mark for subject " + subjectNumber + ": ");
            double mark = scanner.nextDouble();
            if (mark >= 0.0 && mark <= 100.0) {
                return mark;
            }
            System.out.println("Enter a mark from 0 to 100.");
        }
    }

    private static String gradeFor(double average) {
        if (average >= 90.0) {
            return "A";
        }
        if (average >= 80.0) {
            return "B";
        }
        if (average >= 70.0) {
            return "C";
        }
        if (average >= 60.0) {
            return "D";
        }
        return "F";
    }
}
