import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface MessageInterface extends Remote {

    public void registerClient(CallbackInterface client) throws RemoteException;

    public void sendMessage(String message) throws RemoteException;

    public List<String> getHistory() throws RemoteException;
}