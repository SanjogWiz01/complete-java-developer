import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class HelloClient {

    public static void main(String[] args) {

        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            HelloInterface hello = (HelloInterface) registry.lookup("Hello");

            String message = hello.sayHello();

            System.out.println("Server says: " + message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}