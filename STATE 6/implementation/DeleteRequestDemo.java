import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DeleteRequestDemo {

    public static void main(String[] args) {
        System.out.println("=== DELETE Request Demo ===\n");

        try {
            URL url = new URL("https://jsonplaceholder.typicode.com/posts/1");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");
            con.setRequestProperty("Accept", "application/json");

            System.out.println("Sending DELETE request...");
            System.out.println("URL: " + url);
            System.out.println();

            int status = con.getResponseCode();
            System.out.println("Status Code: " + status);

            if (status == 200) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();
                System.out.println("Response: " + response.toString());
            } else if (status == 204) {
                System.out.println("Deleted successfully (no content)");
            } else {
                System.out.println("Delete failed with status: " + status);
            }
            con.disconnect();

            System.out.println("\nDELETE Status Codes:");
            System.out.println("200 - Deleted, response body included");
            System.out.println("204 - Deleted, no response body");
            System.out.println("404 - Resource not found");
            System.out.println("403 - Access denied");

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
