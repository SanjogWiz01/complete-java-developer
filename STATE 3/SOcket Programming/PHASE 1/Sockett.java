import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Sockett{

    public static void main(String[] args) {

        // Server Thread
        Thread server = new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(5000);
                System.out.println("Server started. Waiting for client...");

                Socket socket = serverSocket.accept();
                System.out.println("Client connected.");

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));

                PrintWriter out = new PrintWriter(
                        socket.getOutputStream(), true);

                String msg = in.readLine();
                System.out.println("Client says: " + msg);

                out.println("Hello from Server");

                socket.close();
                serverSocket.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Client Thread
        Thread client = new Thread(() -> {
            try {
                Thread.sleep(1000); // Wait for server

                Socket socket = new Socket("localhost", 5000);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));

                PrintWriter out = new PrintWriter(
                        socket.getOutputStream(), true);

                out.println("Hello from Client");

                String response = in.readLine();
                System.out.println("Server says: " + response);

                socket.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        server.start();
        client.start();
    }
}