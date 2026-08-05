import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MathInterface extends Remote {

    public double power(double base, double exponent) throws RemoteException;

    public double square(double value) throws RemoteException;

    public double squareRoot(double value) throws RemoteException;

    public double naturalLog(double value) throws RemoteException;
}