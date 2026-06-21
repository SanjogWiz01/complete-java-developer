import java.io.*;
import java.net.*;

public class TCPServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Server listening on port 5000...");
        
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());
            
            // Read from client
            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream())
            );
            String message = in.readLine();
            System.out.println("Received: " + message);
            
            // Send response to client
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("Echo: " + message);
            
            clientSocket.close();
        }
    }
}