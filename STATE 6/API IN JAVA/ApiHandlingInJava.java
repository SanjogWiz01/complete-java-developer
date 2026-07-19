import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ApiHandlingInJava {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    public static void main(String[] args) {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        try {
            sendGetRequest(client);
            sendPostRequest(client);
        } catch (IOException exception) {
            System.out.println("Network or stream error: " + exception.getMessage());
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            System.out.println("Request was interrupted.");
        }
    }

    private static void sendGetRequest(HttpClient client) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/posts/1"))
                .timeout(Duration.ofSeconds(15))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        handleResponse("GET post by id", response);
    }

    private static void sendPostRequest(HttpClient client) throws IOException, InterruptedException {
        String jsonBody = "{"
                + "\"title\":\"Learning API handling in Java\","
                + "\"body\":\"Java sends HTTP requests, receives responses, and checks status codes.\","
                + "\"userId\":1"
                + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/posts"))
                .timeout(Duration.ofSeconds(15))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        handleResponse("POST create post", response);
    }

    private static void handleResponse(String operation, HttpResponse<String> response) {
        int statusCode = response.statusCode();

        System.out.println();
        System.out.println("Operation: " + operation);
        System.out.println("Status code: " + statusCode);

        if (statusCode >= 200 && statusCode < 300) {
            System.out.println("Success response body:");
            System.out.println(response.body());
            return;
        }

        if (statusCode >= 400 && statusCode < 500) {
            System.out.println("Client error. Check URL, request method, headers, or request body.");
        } else if (statusCode >= 500) {
            System.out.println("Server error. The remote API failed while processing the request.");
        } else {
            System.out.println("Unexpected response status.");
        }

        System.out.println("Error response body:");
        System.out.println(response.body());
    }
}
