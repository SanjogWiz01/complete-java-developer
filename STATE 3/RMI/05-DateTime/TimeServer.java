import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TimeServer {

    public static void main(String[] args) {

        try {
            TimeImpl time = new TimeImpl();

            Registry registry = LocateRegistry.createRegistry(1099);

            registry.rebind("TimeService", time);

            System.out.println("Time Server is ready.");

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}