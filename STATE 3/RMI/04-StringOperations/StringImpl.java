import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class StringImpl extends UnicastRemoteObject implements StringInterface {

    public StringImpl() throws RemoteException {
        super();
    }

    @Override
    public String reverse(String input) throws RemoteException {
        return new StringBuilder(input).reverse().toString();
    }

    @Override
    public String toUpperCase(String input) throws RemoteException {
        return input.toUpperCase();
    }

    @Override
    public int countWords(String input) throws RemoteException {
        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            return 0;
        }
        return trimmed.split("\\s+").length;
    }
}