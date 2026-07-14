import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestApiServerDemo {

    private static final int PORT = 8080;
    private static int requestCount = 0;

    public static void main(String[] args) {
        System.out.println("=== REST API Server Demo ===\n");

        Thread serverThread = new Thread(() -> startServer());
        serverThread.setDaemon(true);
        serverThread.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Server started on port " + PORT);
        System.out.println("Testing API endpoints...\n");

        testEndpoint("GET", "/");
        testEndpoint("GET", "/users");
        testEndpoint("GET", "/status");
        testEndpoint("POST", "/users");

        System.out.println("\nTotal requests handled: " + requestCount);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleRequest(clientSocket);
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    private static void handleRequest(Socket clientSocket) {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String requestLine = in.readLine();
            if (requestLine == null) {
                clientSocket.close();
                return;
            }

            String[] parts = requestLine.split(" ");
            String method = parts[0];
            String path = parts[1];

            String header;
            while ((header = in.readLine()) != null && !header.isEmpty()) {
            }

            requestCount++;

            String responseBody;
            int statusCode = 200;

            if (path.equals("/") && method.equals("GET")) {
                responseBody = "{\"message\": \"Welcome to Java REST API\", \"version\": \"1.0\"}";
            } else if (path.equals("/users") && method.equals("GET")) {
                responseBody = "{\"users\": [{\"id\": 1, \"name\": \"Sanjog\"}, {\"id\": 2, \"name\": \"Java\"}]}";
            } else if (path.equals("/status") && method.equals("GET")) {
                responseBody = "{\"status\": \"ok\", \"requests\": " + requestCount + "}";
            } else if (path.equals("/users") && method.equals("POST")) {
                statusCode = 201;
                responseBody = "{\"message\": \"User created\", \"id\": 3}";
            } else {
                statusCode = 404;
                responseBody = "{\"error\": \"Not found\"}";
            }

            out.println("HTTP/1.1 " + statusCode + (statusCode == 200 ? " OK" : statusCode == 201 ? " Created" : " Not Found"));
            out.println("Content-Type: application/json");
            out.println("Content-Length: " + responseBody.length());
            out.println("Connection: close");
            out.println();
            out.println(responseBody);

            clientSocket.close();

        } catch (IOException e) {
            System.out.println("Error handling request: " + e.getMessage());
        }
    }

    private static void testEndpoint(String method, String path) {
        try {
            URL url = new URL("http://localhost:" + PORT + path);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method);
            con.setRequestProperty("Accept", "application/json");

            int status = con.getResponseCode();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();
            con.disconnect();

            System.out.println(method + " " + path + " -> " + status);
            System.out.println("  Response: " + response.toString());

        } catch (IOException e) {
            System.out.println(method + " " + path + " -> Error: " + e.getMessage());
        }
    }
}
