import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class ApiHeadersDemo {

    public static void main(String[] args) {
        try {
            URL url = new URL("https://jsonplaceholder.typicode.com/posts/1");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("User-Agent", "JavaAPIClient/1.0");
            con.setRequestProperty("Authorization", "Bearer sample-token-12345");
            con.setRequestProperty("Cache-Control", "no-cache");
            con.setRequestProperty("X-Custom-Header", "CustomValue123");

            System.out.println("=== API Headers Demo ===");
            System.out.println("Request Headers:");
            for (Map.Entry<String, java.util.List<String>> entry : con.getRequestProperties().entrySet()) {
                System.out.println("  " + entry.getKey() + ": " + entry.getValue());
            }

            int status = con.getResponseCode();
            System.out.println("\nResponse Status: " + status);

            System.out.println("Response Headers:");
            for (Map.Entry<String, java.util.List<String>> entry : con.getHeaderFields().entrySet()) {
                if (entry.getKey() != null) {
                    System.out.println("  " + entry.getKey() + ": " + entry.getValue());
                }
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();
            con.disconnect();

            System.out.println("\nResponse Body: " + response.toString());

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
