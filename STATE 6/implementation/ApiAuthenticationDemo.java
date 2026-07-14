import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class ApiAuthenticationDemo {

    private static final String API_KEY = "demo-api-key-12345";

    public static void main(String[] args) {
        System.out.println("=== API Authentication Demo ===\n");

        System.out.println("1. API Key Authentication:");
        demonstrateApiKey();

        System.out.println("\n2. Basic Authentication:");
        demonstrateBasicAuth();

        System.out.println("\n3. Bearer Token Authentication:");
        demonstrateBearerToken();
    }

    private static void demonstrateApiKey() {
        try {
            URL url = new URL("https://jsonplaceholder.typicode.com/posts/1");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-API-Key", API_KEY);

            System.out.println("Header: X-API-Key: " + API_KEY);
            System.out.println("Status: " + con.getResponseCode());
            con.disconnect();

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void demonstrateBasicAuth() {
        try {
            String username = "admin";
            String password = "secret123";
            String credentials = username + ":" + password;
            String encoded = Base64.getEncoder().encodeToString(credentials.getBytes());

            URL url = new URL("https://jsonplaceholder.typicode.com/posts/1");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Basic " + encoded);

            System.out.println("Credentials: " + username + ":" + password);
            System.out.println("Encoded: " + encoded);
            System.out.println("Header: Authorization: Basic " + encoded);
            System.out.println("Status: " + con.getResponseCode());
            con.disconnect();

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void demonstrateBearerToken() {
        try {
            String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.demo-token";

            URL url = new URL("https://jsonplaceholder.typicode.com/posts/1");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + token);

            System.out.println("Token: " + token);
            System.out.println("Header: Authorization: Bearer " + token);
            System.out.println("Status: " + con.getResponseCode());
            con.disconnect();

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
