import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MessageClient {

    public static void main(String[] args) {

        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            MessageInterface messageServer =
                    (MessageInterface) registry.lookup("MessageServer");

            CallbackInterface callback = new CallbackImpl("Client-1");

            messageServer.registerClient(callback);

            messageServer.sendMessage("Hello from the first client");
            messageServer.sendMessage("This is a broadcast message");

            System.out.println("--- Message history ---");
            for (String msg : messageServer.getHistory()) {
                System.out.println(msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}