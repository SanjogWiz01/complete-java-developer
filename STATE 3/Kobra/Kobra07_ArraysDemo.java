public class Kobra07_ArraysDemo {
    public static void main(String[] args) {
        int[] numbers = {5, 3, 8, 1, 9, 2};

        System.out.println("Original:");
        printArray(numbers);

        int sum = 0;
        int max = numbers[0];
        for (int n : numbers) {
            sum += n;
            if (n > max) {
                max = n;
            }
        }

        System.out.println("Sum: " + sum);
        System.out.println("Max: " + max);

        java.util.Arrays.sort(numbers);
        System.out.println("Sorted:");
        printArray(numbers);

        String[][] grid = {
            {"1", "2", "3"},
            {"4", "5", "6"}
        };
        System.out.println("Grid[1][2] = " + grid[1][2]);
    }

    static void printArray(int[] arr) {
        for (int n : arr) {
            System.out.print(n + " ");
        }
        System.out.println();
    }
}
