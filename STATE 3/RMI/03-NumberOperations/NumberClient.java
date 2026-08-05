import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class NumberClient {

    public static void main(String[] args) {

        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            NumberInterface number = (NumberInterface) registry.lookup("NumberOps");

            int value = 7;

            System.out.println("Factorial of " + value + " = " + number.factorial(value));
            System.out.println("Is " + value + " prime? " + number.isPrime(value));
            System.out.println("Reverse of " + value + " = " + number.reverse(value));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}