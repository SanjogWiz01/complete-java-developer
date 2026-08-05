import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BankInterface extends Remote {

    public void deposit(double amount) throws RemoteException;

    public void withdraw(double amount) throws RemoteException;

    public double getBalance() throws RemoteException;
}