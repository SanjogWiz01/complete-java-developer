import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Daily BMI Checker");
        System.out.print("Weight in kilograms: ");
        double weightKg = scanner.nextDouble();

        System.out.print("Height in centimeters: ");
        double heightCm = scanner.nextDouble();

        double bmi = calculateBmi(weightKg, heightCm);
        System.out.printf("BMI: %.1f%n", bmi);
        System.out.println("Category: " + categoryFor(bmi));
    }

    private static double calculateBmi(double weightKg, double heightCm) {
        double heightMeters = heightCm / 100.0;
        return weightKg / (heightMeters * heightMeters);
    }

    private static String categoryFor(double bmi) {
        if (bmi < 18.5) {
            return "Underweight";
        }
        if (bmi < 25.0) {
            return "Normal";
        }
        if (bmi < 30.0) {
            return "Overweight";
        }
        return "Obese";
    }
}
