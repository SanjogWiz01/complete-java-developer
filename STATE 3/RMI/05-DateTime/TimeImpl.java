import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

public class TimeImpl extends UnicastRemoteObject implements TimeInterface {

    public TimeImpl() throws RemoteException {
        super();
    }

    @Override
    public Date getCurrentTime() throws RemoteException {
        return new Date();
    }
}