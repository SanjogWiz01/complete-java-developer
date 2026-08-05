import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CallbackImpl extends UnicastRemoteObject implements CallbackInterface {

    private String clientName;

    public CallbackImpl(String clientName) throws RemoteException {
        super();
        this.clientName = clientName;
    }

    @Override
    public void notifyMessage(String message) throws RemoteException {
        System.out.println(clientName + " received callback: " + message);
    }
}