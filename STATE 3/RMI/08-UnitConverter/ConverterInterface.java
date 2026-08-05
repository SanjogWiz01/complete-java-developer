import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ConverterInterface extends Remote {

    public double celsiusToFahrenheit(double celsius) throws RemoteException;

    public double fahrenheitToCelsius(double fahrenheit) throws RemoteException;

    public double kmToMiles(double km) throws RemoteException;
}