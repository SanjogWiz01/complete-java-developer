import java.io.BufferedReader;
import java.io.InputStreamReader;  // header
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Server is waiting...");

            Socket socket = serverSocket.accept();
            System.out.println("Client connected.");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true);

            String message = in.readLine();
            System.out.println("Client: " + message);

            out.println("Hello from Server");

            socket.close();
            serverSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}