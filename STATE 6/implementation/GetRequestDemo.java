import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetRequestDemo {

    public static void main(String[] args) {
        System.out.println("=== GET Request Demo ===\n");

        try {
            URL url = new URL("https://jsonplaceholder.typicode.com/posts/1");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");

            int status = con.getResponseCode();
            System.out.println("Status Code: " + status);

            if (status == 200) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();

                System.out.println("Response:");
                System.out.println(response.toString());
            } else {
                System.out.println("Failed to fetch data");
            }
            con.disconnect();

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n--- GET with Query Parameters ---\n");

        try {
            String baseUrl = "https://jsonplaceholder.typicode.com/comments";
            String params = "?postId=1";
            URL url = new URL(baseUrl + params);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int status = con.getResponseCode();
            System.out.println("URL: " + baseUrl + params);
            System.out.println("Status: " + status);

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();
            con.disconnect();

            System.out.println("Response length: " + response.length() + " chars");
            System.out.println("First 200 chars: " + response.substring(0, Math.min(200, response.length())));

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
