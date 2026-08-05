import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MessageServer {

    public static void main(String[] args) {

        try {
            MessageImpl message = new MessageImpl();

            Registry registry = LocateRegistry.createRegistry(1099);

            registry.rebind("MessageServer", message);

            System.out.println("Message Server is ready.");

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}