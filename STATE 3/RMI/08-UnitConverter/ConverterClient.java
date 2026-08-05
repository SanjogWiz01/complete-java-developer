import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ConverterClient {

    public static void main(String[] args) {

        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            ConverterInterface converter = (ConverterInterface) registry.lookup("Converter");

            System.out.println("100 C = " + converter.celsiusToFahrenheit(100) + " F");
            System.out.println("212 F = " + converter.fahrenheitToCelsius(212) + " C");
            System.out.println("10 km = " + converter.kmToMiles(10) + " miles");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}