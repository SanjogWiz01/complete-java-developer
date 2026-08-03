public class Kobra09_ExceptionHandling {
    public static void main(String[] args) {
        try {
            int result = divide(10, 0);
            System.out.println("Result: " + result);
        } catch (ArithmeticException e) {
            System.out.println("Arithmetic error: " + e.getMessage());
        } finally {
            System.out.println("Finally block always runs.");
        }

        try {
            int[] arr = {1, 2, 3};
            System.out.println(arr[5]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Array index error: " + e.getMessage());
        }

        try {
            validateAge(16);
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }
    }

    static int divide(int a, int b) {
        return a / b;
    }

    static void validateAge(int age) {
        if (age < 18) {
            throw new IllegalArgumentException("Age must be at least 18");
        }
        System.out.println("Age is valid: " + age);
    }
}
