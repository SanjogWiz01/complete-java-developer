import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class StringServer {

    public static void main(String[] args) {

        try {
            StringImpl stringService = new StringImpl();

            Registry registry = LocateRegistry.createRegistry(1099);

            registry.rebind("StringService", stringService);

            System.out.println("String Server is ready.");

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}