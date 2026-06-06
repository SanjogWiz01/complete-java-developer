import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final int MIN_NUMBER = 1;
    private static final int MAX_NUMBER = 100;
    private static final int MAX_ATTEMPTS = 7;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        int target = random.nextInt(MAX_NUMBER - MIN_NUMBER + 1) + MIN_NUMBER;

        System.out.println("Number Guessing Game");
        System.out.printf("Guess a number from %d to %d.%n", MIN_NUMBER, MAX_NUMBER);

        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            System.out.print("Attempt " + attempt + ": ");
            int guess = scanner.nextInt();

            if (guess == target) {
                System.out.println("Correct! You guessed it in " + attempt + " attempts.");
                return;
            }

            if (guess < target) {
                System.out.println("Higher.");
            } else {
                System.out.println("Lower.");
            }
        }

        System.out.println("Out of attempts. The number was " + target + ".");
    }
}
