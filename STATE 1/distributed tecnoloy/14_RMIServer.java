import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    public static void main(String[] args) throws Exception {
        HelloService service = new HelloServiceImpl();
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("HelloService", service);
        System.out.println("RMI Server is running on port 1099.");
    }
}
