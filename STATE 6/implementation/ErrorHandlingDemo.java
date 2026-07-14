import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class ErrorHandlingDemo {

    public static void main(String[] args) {
        System.out.println("=== Error Handling Demo ===\n");

        System.out.println("1. Valid Request:");
        makeRequest("https://jsonplaceholder.typicode.com/posts/1");

        System.out.println("\n2. 404 Not Found:");
        makeRequest("https://jsonplaceholder.typicode.com/nonexistent");

        System.out.println("\n3. Timeout Handling:");
        handleTimeout("https://jsonplaceholder.typicode.com/posts/1");

        System.out.println("\n4. Invalid URL:");
        handleInvalidUrl("not-a-valid-url");
    }

    private static void makeRequest(String urlString) {
        HttpURLConnection con = null;
        try {
            URL url = new URL(urlString);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            int status = con.getResponseCode();
            System.out.println("Status: " + status);

            if (status == 200) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();
                System.out.println("Response length: " + response.length() + " chars");
            } else {
                System.out.println("Error response code: " + status);
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getErrorStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println("Error: " + line);
                }
                br.close();
            }

        } catch (SocketTimeoutException e) {
            System.out.println("Request timed out: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        } finally {
            if (con != null) con.disconnect();
        }
    }

    private static void handleTimeout(String urlString) {
        HttpURLConnection con = null;
        try {
            URL url = new URL(urlString);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(1);
            con.setReadTimeout(1);

            int status = con.getResponseCode();
            System.out.println("Status: " + status);

        } catch (SocketTimeoutException e) {
            System.out.println("Timeout caught: " + e.getMessage());
            System.out.println("Solution: Increase timeout or implement retry logic");
        } catch (IOException e) {
            System.out.println("IO Error: " + e.getMessage());
        } finally {
            if (con != null) con.disconnect();
        }
    }

    private static void handleInvalidUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.getResponseCode();
        } catch (java.net.MalformedURLException e) {
            System.out.println("Malformed URL: " + e.getMessage());
            System.out.println("Solution: Validate URL before making request");
        } catch (IOException e) {
            System.out.println("IO Error: " + e.getMessage());
        }
    }
}
