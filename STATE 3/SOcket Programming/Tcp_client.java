import java.io.*;
import java.net.*;

public class Tcp_client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 5000);
        System.out.println("Connected to server");
        
        // Send message to server
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // auto-flush
        out.println("Hello from client!");
        
        // Read response from server
        BufferedReader in = new BufferedReader(
            new InputStreamReader(socket.getInputStream())
        );
        String response = in.readLine();
        System.out.println("Server says: " + response);
        
        socket.close();
    }
}