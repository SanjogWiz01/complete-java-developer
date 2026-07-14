import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestApiConceptsDemo {

    public static void main(String[] args) {
        System.out.println("=== REST API Concepts Demo ===\n");

        String[] endpoints = {
            "https://jsonplaceholder.typicode.com/posts",
            "https://jsonplaceholder.typicode.com/users",
            "https://jsonplaceholder.typicode.com/comments"
        };

        String[] names = {"Posts", "Users", "Comments"};

        for (int i = 0; i < endpoints.length; i++) {
            try {
                URL url = new URL(endpoints[i]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Accept", "application/json");

                int status = con.getResponseCode();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();
                con.disconnect();

                System.out.println("Resource: " + names[i]);
                System.out.println("Endpoint: " + endpoints[i]);
                System.out.println("Status: " + status);
                System.out.println("Data Length: " + response.length() + " chars");
                System.out.println("---");

            } catch (IOException e) {
                System.out.println("Error fetching " + names[i] + ": " + e.getMessage());
            }
        }

        System.out.println("\nREST Principles:");
        System.out.println("1. Stateless - each request is independent");
        System.out.println("2. Client-Server separation");
        System.out.println("3. Cacheable responses");
        System.out.println("4. Uniform interface (HTTP methods)");
    }
}
