public class Kobra04_Loops {
    public static void main(String[] args) {
        System.out.println("For loop:");
        for (int i = 1; i <= 5; i++) {
            System.out.print(i + " ");
        }

        System.out.println("\nWhile loop:");
        int j = 1;
        while (j <= 5) {
            System.out.print(j + " ");
            j++;
        }

        System.out.println("\nDo-while loop:");
        int k = 1;
        do {
            System.out.print(k + " ");
            k++;
        } while (k <= 5);

        System.out.println("\nEnhanced for loop:");
        int[] numbers = {10, 20, 30, 40, 50};
        for (int num : numbers) {
            System.out.print(num + " ");
        }
        System.out.println();
    }
}
