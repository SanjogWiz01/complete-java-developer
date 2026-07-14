import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostRequestDemo {

    public static void main(String[] args) {
        System.out.println("=== POST Request Demo ===\n");

        try {
            URL url = new URL("https://jsonplaceholder.typicode.com/posts");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInput = "{"
                    + "\"title\": \"Java API Post\","
                    + "\"body\": \"This post was created using HttpURLConnection POST request.\","
                    + "\"userId\": 1"
                    + "}";

            System.out.println("Sending POST request...");
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

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
