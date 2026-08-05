import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MathClient {

    public static void main(String[] args) {

        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            MathInterface math = (MathInterface) registry.lookup("MathService");

            System.out.println("2 ^ 10       = " + math.power(2, 10));
            System.out.println("Square of 9  = " + math.square(9));
            System.out.println("Sqrt of 144  = " + math.squareRoot(144));
            System.out.println("ln(e^2) ~    = " + math.naturalLog(7.389));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}