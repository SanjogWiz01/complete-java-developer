import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketProgrammingTCP {
    public static void main(String[] args) throws Exception {
        int port = 7000;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("SocketProgrammingTCP server on " + port);
            Socket socket = serverSocket.accept();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                System.out.println("Message from client: " + reader.readLine());
            }
            socket.close();
        }
    }
}
