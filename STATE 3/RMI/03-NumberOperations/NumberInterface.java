import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NumberInterface extends Remote {

    public long factorial(int n) throws RemoteException;

    public boolean isPrime(int n) throws RemoteException;

    public int reverse(int n) throws RemoteException;
}