import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

public interface TimeInterface extends Remote {

    public Date getCurrentTime() throws RemoteException;
}