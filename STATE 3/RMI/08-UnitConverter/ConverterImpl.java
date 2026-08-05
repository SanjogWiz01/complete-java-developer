import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ConverterImpl extends UnicastRemoteObject implements ConverterInterface {

    public ConverterImpl() throws RemoteException {
        super();
    }

    @Override
    public double celsiusToFahrenheit(double celsius) throws RemoteException {
        return (celsius * 9.0 / 5.0) + 32.0;
    }

    @Override
    public double fahrenheitToCelsius(double fahrenheit) throws RemoteException {
        return (fahrenheit - 32.0) * 5.0 / 9.0;
    }

    @Override
    public double kmToMiles(double km) throws RemoteException {
        return km * 0.621371;
    }
}