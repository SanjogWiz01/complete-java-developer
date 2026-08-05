import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CalculatorServer {

    public static void main(String[] args) {

        try {
            CalculatorImpl calculator = new CalculatorImpl();

            Registry registry = LocateRegistry.createRegistry(1099);

            registry.rebind("Calculator", calculator);

            System.out.println("Calculator Server is ready.");

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}