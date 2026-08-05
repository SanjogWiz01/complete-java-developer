import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class BankImpl extends UnicastRemoteObject implements BankInterface {

    private double balance;

    public BankImpl() throws RemoteException {
        super();
        this.balance = 0.0;
    }

    @Override
    public void deposit(double amount) throws RemoteException {
        balance += amount;
        System.out.println("Deposited " + amount + ". New balance: " + balance);
    }

    @Override
    public void withdraw(double amount) throws RemoteException {
        if (amount > balance) {
            throw new RemoteException("Insufficient balance.");
        }
        balance -= amount;
        System.out.println("Withdrew " + amount + ". New balance: " + balance);
    }

    @Override
    public double getBalance() throws RemoteException {
        return balance;
    }
}