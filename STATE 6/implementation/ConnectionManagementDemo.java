import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionManagementDemo {

    public static void main(String[] args) {
        System.out.println("=== Connection Management Demo ===\n");

        System.out.println("1. Setting Timeouts:");
        demonstrateTimeouts();

        System.out.println("\n2. Proper Resource Cleanup:");
        demonstrateCleanup();

        System.out.println("\n3. Multiple Requests Reusing Pattern:");
        demonstrateMultipleRequests();
    }

    private static void demonstrateTimeouts() {
        HttpURLConnection con = null;
        try {
            URL url = new URL("https://jsonplaceholder.typicode.com/posts/1");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            System.out.println("Connect Timeout: " + con.getConnectTimeout() + "ms");
            System.out.println("Read Timeout: " + con.getReadTimeout() + "ms");

            int status = con.getResponseCode();
            System.out.println("Status: " + status);

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            if (con != null) {
                con.disconnect();
                System.out.println("Connection disconnected in finally block");
            }
        }
    }

    private static void demonstrateCleanup() {
        HttpURLConnection con = null;
        BufferedReader br = null;
        try {
            URL url = new URL("https://jsonplaceholder.typicode.com/posts/1");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            br = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            System.out.println("Response length: " + response.length() + " chars");

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException e) {
                System.out.println("Error closing reader: " + e.getMessage());
            }
            if (con != null) con.disconnect();
            System.out.println("Resources cleaned up in finally block");
        }
    }

    private static void demonstrateMultipleRequests() {
        String[] urls = {
            "https://jsonplaceholder.typicode.com/posts/1",
            "https://jsonplaceholder.typicode.com/posts/2",
            "https://jsonplaceholder.typicode.com/posts/3"
        };

        for (int i = 0; i < urls.length; i++) {
            HttpURLConnection con = null;
            try {
                URL url = new URL(urls[i]);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);

                int status = con.getResponseCode();
                System.out.println("Request " + (i + 1) + " - Status: " + status);

            } catch (IOException e) {
                System.out.println("Request " + (i + 1) + " - Error: " + e.getMessage());
            } finally {
                if (con != null) con.disconnect();
            }
        }
    }
}
