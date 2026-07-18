import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class ApiTimeoutDemo {

    public static void main(String[] args) {
        System.out.println("=== API Timeout Demo ===\n");

        testWithTimeout(5000, 5000, "https://jsonplaceholder.typicode.com/posts/1");
        System.out.println();
        testWithTimeout(1000, 5000, "https://jsonplaceholder.typicode.com/posts/1");
        System.out.println();
        testWithTimeout(5000, 1000, "https://jsonplaceholder.typicode.com/posts/1");
        System.out.println();
        demonstrateReadTimeout();
    }

    private static void testWithTimeout(int connectTimeout, int readTimeout, String urlString) {
        System.out.println("Connect Timeout: " + connectTimeout + "ms, Read Timeout: " + readTimeout + "ms");
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(connectTimeout);
            con.setReadTimeout(readTimeout);
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");

            long startTime = System.currentTimeMillis();
            int status = con.getResponseCode();
            long endTime = System.currentTimeMillis();

            System.out.println("Status: " + status);
            System.out.println("Response Time: " + (endTime - startTime) + "ms");

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();
            con.disconnect();

            System.out.println("Response Length: " + response.length() + " chars");

        } catch (SocketTimeoutException e) {
            System.out.println("Timeout Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO Error: " + e.getMessage());
        }
    }

    private static void demonstrateReadTimeout() {
        System.out.println("=== Read Timeout Demo ===");
        try {
            URL url = new URL("https://httpstat.us/200?sleep=3000");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5000);
            con.setReadTimeout(2000);
            con.setRequestMethod("GET");

            long startTime = System.currentTimeMillis();
            int status = con.getResponseCode();
            long endTime = System.currentTimeMillis();

            System.out.println("Status: " + status);
            System.out.println("Response Time: " + (endTime - startTime) + "ms");
            con.disconnect();

        } catch (SocketTimeoutException e) {
            System.out.println("Read Timeout Occurred: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
