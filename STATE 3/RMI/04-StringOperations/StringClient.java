import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class StringClient {

    public static void main(String[] args) {

        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            StringInterface stringService = (StringInterface) registry.lookup("StringService");

            String text = "java remote method invocation";

            System.out.println("Original  : " + text);
            System.out.println("Reversed  : " + stringService.reverse(text));
            System.out.println("Uppercase : " + stringService.toUpperCase(text));
            System.out.println("Word count: " + stringService.countWords(text));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}