public class Kobra05_Methods {
    public static void main(String[] args) {
        greet("Kobra");
        System.out.println("Sum: " + add(5, 7));
        System.out.println("Factorial of 5: " + factorial(5));
    }

    static void greet(String name) {
        System.out.println("Hello, " + name + "!");
    }

    static int add(int a, int b) {
        return a + b;
    }

    static long factorial(int n) {
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
}
