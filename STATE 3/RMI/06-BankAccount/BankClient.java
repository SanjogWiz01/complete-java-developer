import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class BankClient {

    public static void main(String[] args) {

        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            BankInterface bank = (BankInterface) registry.lookup("Bank");

            System.out.println("Initial balance: " + bank.getBalance());

            bank.deposit(5000);
            bank.deposit(1500);
            bank.withdraw(2000);

            System.out.println("Final balance: " + bank.getBalance());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}