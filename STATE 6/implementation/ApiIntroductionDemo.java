import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiIntroductionDemo {

    public static void main(String[] args) {
        try {
            URL url = new URL("https://jsonplaceholder.typicode.com/posts/1");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");

            int status = con.getResponseCode();
            System.out.println("=== API Introduction Demo ===");
            System.out.println("URL: " + url);
            System.out.println("Status Code: " + status);
            System.out.println("Content-Type: " + con.getContentType());
            System.out.println("Response Length: " + con.getContentLength());
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

            System.out.println("Response Body:");
            System.out.println(response.toString());

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
