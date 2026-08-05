import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TimeClient {

    public static void main(String[] args) {

        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            TimeInterface time = (TimeInterface) registry.lookup("TimeService");

            System.out.println("Current server time: " + time.getCurrentTime());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}