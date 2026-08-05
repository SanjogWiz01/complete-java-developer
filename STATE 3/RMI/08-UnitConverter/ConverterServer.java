import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ConverterServer {

    public static void main(String[] args) {

        try {
            ConverterImpl converter = new ConverterImpl();

            Registry registry = LocateRegistry.createRegistry(1099);

            registry.rebind("Converter", converter);

            System.out.println("Converter Server is ready.");

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}