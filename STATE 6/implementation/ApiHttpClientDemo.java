import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class ApiHttpClientDemo {

    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    public static void main(String[] args) {
        System.out.println("=== Java HttpClient (11+) Demo ===\n");

        System.out.println("1. Basic GET Request:");
        basicGetRequest();

        System.out.println("\n2. GET with Headers:");
        getWithHeaders();

        System.out.println("\n3. POST Request:");
        postRequest();

        System.out.println("\n4. PUT Request:");
        putRequest();

        System.out.println("\n5. DELETE Request:");
        deleteRequest();

        System.out.println("\n6. Async GET Request:");
        asyncGetRequest();

        System.out.println("\n7. Async POST Request:");
        asyncPostRequest();

        System.out.println("\n8. Request with Timeout:");
        requestWithTimeout();

        System.out.println("\n9. Response Info:");
        responseInfo();

        System.out.println("\n10. Custom HttpClient Configuration:");
        customConfiguration();
    }

    private static void basicGetRequest() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("  Status: " + response.statusCode());
            System.out.println("  Body length: " + response.body().length() + " chars");
            System.out.println("  Body preview: " + response.body().substring(0, Math.min(80, response.body().length())) + "...");

        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    private static void getWithHeaders() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
                    .header("Accept", "application/json")
                    .header("User-Agent", "JavaHttpClient/11")
                    .header("X-Custom", "CustomValue")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("  Status: " + response.statusCode());
            System.out.println("  Headers:");
            response.headers().map().forEach((key, values) ->
                System.out.println("    " + key + ": " + values)
            );

        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    private static void postRequest() {
        try {
            String jsonBody = "{\"title\": \"Java HttpClient\", \"body\": \"Post created with Java 11+ HttpClient\", \"userId\": 1}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://jsonplaceholder.typicode.com/posts"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("  Status: " + response.statusCode());
            System.out.println("  Body: " + response.body());

        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    private static void putRequest() {
        try {
            String jsonBody = "{\"id\": 1, \"title\": \"Updated Title\", \"body\": \"Updated body\", \"userId\": 1}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("  Status: " + response.statusCode());
            System.out.println("  Body: " + response.body());

        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    private static void deleteRequest() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("  Status: " + response.statusCode());
            System.out.println("  Body: " + response.body());

        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    private static void asyncGetRequest() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
                    .GET()
                    .build();

            CompletableFuture<HttpResponse<String>> future = client.sendAsync(
                request, HttpResponse.BodyHandlers.ofString()
            );

            HttpResponse<String> response = future.join();

            System.out.println("  Status: " + response.statusCode());
            System.out.println("  Body length: " + response.body().length() + " chars");

        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    private static void asyncPostRequest() {
        try {
            String jsonBody = "{\"title\": \"Async Post\", \"body\": \"Created asynchronously\", \"userId\": 1}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://jsonplaceholder.typicode.com/posts"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            CompletableFuture<HttpResponse<String>> future = client.sendAsync(
                request, HttpResponse.BodyHandlers.ofString()
            );

            future.thenApply(HttpResponse::body)
                  .thenAccept(body -> System.out.println("  Async response: " + body))
                  .join();

        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    private static void requestWithTimeout() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("  Status: " + response.statusCode());
            System.out.println("  Response within timeout");

        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    private static void responseInfo() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("  Status Code: " + response.statusCode());
            System.out.println("  HTTP Version: " + response.version());
            System.out.println("  Headers:");
            response.headers().map().forEach((key, values) ->
                System.out.println("    " + key + ": " + values)
            );
            System.out.println("  Body Length: " + response.body().length() + " chars");

        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    private static void customConfiguration() {
        HttpClient customClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NEVER)
                .authenticator(null)
                .build();

        System.out.println("  Custom HttpClient configured:");
        System.out.println("  - Connect timeout: 15 seconds");
        System.out.println("  - HTTP version: 1.1");
        System.out.println("  - Follow redirects: NEVER");
        System.out.println("  - Authenticator: null");

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
                    .GET()
                    .build();

            HttpResponse<String> response = customClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("  Request successful: Status " + response.statusCode());

        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }
}
