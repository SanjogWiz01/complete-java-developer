import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class HelloServer {

    public static void main(String[] args) {

        try {
            HelloInterface hello = new HelloImpl();

            Registry registry = LocateRegistry.createRegistry(1099);

            registry.rebind("Hello", hello);

            System.out.println("Hello Server is ready.");

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}