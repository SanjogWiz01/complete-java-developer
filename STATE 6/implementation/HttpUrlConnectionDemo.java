import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;

public class HttpUrlConnectionDemo {

    public static void main(String[] args) {
        System.out.println("=== HttpURLConnection Demo ===\n");

        try {
            URL url = new URL("https://jsonplaceholder.typicode.com/posts/1");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            System.out.println("Connection Details:");
            System.out.println("URL: " + con.getURL());
            System.out.println("Method: " + con.getRequestMethod());
            System.out.println("Connect Timeout: " + con.getConnectTimeout() + "ms");
            System.out.println("Read Timeout: " + con.getReadTimeout() + "ms");
            System.out.println();

            int status = con.getResponseCode();
            System.out.println("Response:");
            System.out.println("Status Code: " + status);
            System.out.println("Content-Type: " + con.getContentType());
            System.out.println("Content-Length: " + con.getContentLength());
            System.out.println("Header Fields:");
            con.getHeaderFields().forEach((key, value) -> {
                System.out.println("  " + key + ": " + value);
            });
            System.out.println();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();
            con.disconnect();

            System.out.println("Body: " + response.toString());

        } catch (MalformedURLException e) {
            System.out.println("Invalid URL: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }
}
