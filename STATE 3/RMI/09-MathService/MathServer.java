import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MathServer {

    public static void main(String[] args) {

        try {
            MathImpl math = new MathImpl();

            Registry registry = LocateRegistry.createRegistry(1099);

            registry.rebind("MathService", math);

            System.out.println("Math Server is ready.");

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}