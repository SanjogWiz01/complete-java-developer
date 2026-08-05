import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class BankServer {

    public static void main(String[] args) {

        try {
            BankImpl bank = new BankImpl();

            Registry registry = LocateRegistry.createRegistry(1099);

            registry.rebind("Bank", bank);

            System.out.println("Bank Server is ready.");

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}