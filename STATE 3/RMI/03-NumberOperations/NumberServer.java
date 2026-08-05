import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class NumberServer {

    public static void main(String[] args) {

        try {
            NumberImpl number = new NumberImpl();

            Registry registry = LocateRegistry.createRegistry(1099);

            registry.rebind("NumberOps", number);

            System.out.println("Number Server is ready.");

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}