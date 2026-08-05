import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class MessageImpl extends UnicastRemoteObject implements MessageInterface {

    private List<CallbackInterface> clients;
    private List<String> history;

    public MessageImpl() throws RemoteException {
        super();
        clients = new ArrayList<>();
        history = new ArrayList<>();
    }

    @Override
    public void registerClient(CallbackInterface client) throws RemoteException {
        clients.add(client);
        System.out.println("Client registered. Total clients: " + clients.size());
    }

    @Override
    public void sendMessage(String message) throws RemoteException {
        history.add(message);
        System.out.println("Broadcasting: " + message);
        for (CallbackInterface client : clients) {
            client.notifyMessage(message);
        }
    }

    @Override
    public List<String> getHistory() throws RemoteException {
        return history;
    }
}