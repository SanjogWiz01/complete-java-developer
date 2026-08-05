import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CalculatorClient {

    public static void main(String[] args) {

        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            CalculatorInterface calc = (CalculatorInterface) registry.lookup("Calculator");

            int a = 20;
            int b = 5;

            System.out.println(a + " + " + b + " = " + calc.add(a, b));
            System.out.println(a + " - " + b + " = " + calc.subtract(a, b));
            System.out.println(a + " * " + b + " = " + calc.multiply(a, b));
            System.out.println(a + " / " + b + " = " + calc.divide(a, b));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}