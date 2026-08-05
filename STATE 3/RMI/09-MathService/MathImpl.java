import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MathImpl extends UnicastRemoteObject implements MathInterface {

    public MathImpl() throws RemoteException {
        super();
    }

    @Override
    public double power(double base, double exponent) throws RemoteException {
        return Math.pow(base, exponent);
    }

    @Override
    public double square(double value) throws RemoteException {
        return value * value;
    }

    @Override
    public double squareRoot(double value) throws RemoteException {
        if (value < 0) {
            throw new RemoteException("Cannot compute square root of a negative number.");
        }
        return Math.sqrt(value);
    }

    @Override
    public double naturalLog(double value) throws RemoteException {
        if (value <= 0) {
            throw new RemoteException("Logarithm requires a positive number.");
        }
        return Math.log(value);
    }
}