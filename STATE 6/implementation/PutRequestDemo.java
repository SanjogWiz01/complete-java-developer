import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PutRequestDemo {

    public static void main(String[] args) {
        System.out.println("=== PUT Request Demo ===\n");

        try {
            URL url = new URL("https://jsonplaceholder.typicode.com/posts/1");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInput = "{"
                    + "\"id\": 1,"
                    + "\"title\": \"Updated Title\","
                    + "\"body\": \"This post was updated using PUT request.\","
                    + "\"userId\": 1"
                    + "}";

            System.out.println("Sending PUT request to update resource...");
            System.out.println("Body: " + jsonInput);
            System.out.println();

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInput.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int status = con.getResponseCode();
            System.out.println("Status Code: " + status);

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();
            con.disconnect();

            System.out.println("Response:");
            System.out.println(response.toString());

            System.out.println("\nPUT vs PATCH:");
            System.out.println("PUT - Replaces entire resource");
            System.out.println("PATCH - Partial update only");

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
