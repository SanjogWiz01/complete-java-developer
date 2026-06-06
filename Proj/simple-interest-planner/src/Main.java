import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Simple Interest Planner");
        System.out.print("Principal amount: ");
        double principal = scanner.nextDouble();

        System.out.print("Annual interest rate (%): ");
        double annualRate = scanner.nextDouble();

        System.out.print("Time in years: ");
        double years = scanner.nextDouble();

        double interest = calculateInterest(principal, annualRate, years);
        double total = principal + interest;

        System.out.printf("Interest: %.2f%n", interest);
        System.out.printf("Total amount: %.2f%n", total);
    }

    private static double calculateInterest(double principal, double annualRate, double years) {
        return principal * annualRate * years / 100.0;
    }
}
