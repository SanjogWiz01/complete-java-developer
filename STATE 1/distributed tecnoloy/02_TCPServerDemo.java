import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServerDemo {
    public static void main(String[] args) throws Exception {
        int port = 5000;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("TCP Server listening on port " + port);
            Socket client = serverSocket.accept();
            System.out.println("Client connected: " + client.getInetAddress());

            try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                 PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
                String message = in.readLine();
                System.out.println("Received: " + message);
                out.println("Server ACK: " + message);
            }

            client.close();
        }
    }
}
