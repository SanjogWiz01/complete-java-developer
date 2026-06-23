import java.net.*;
import java.io.*;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 5000);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true);

            out.println("Hello from Client");

            String response = in.readLine();
            System.out.println("Server: " + response);

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}