import java.rmi.Remote;
import java.rmi.RemoteException;

public interface StringInterface extends Remote {

    public String reverse(String input) throws RemoteException;

    public String toUpperCase(String input) throws RemoteException;

    public int countWords(String input) throws RemoteException;
}