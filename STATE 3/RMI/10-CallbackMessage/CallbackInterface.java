import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CallbackInterface extends Remote {

    public void notifyMessage(String message) throws RemoteException;
}