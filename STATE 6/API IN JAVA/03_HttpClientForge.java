import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HttpClientForge {

    public static void main(String[] args) {
        System.out.println("=== HTTP CLIENT FORGE: Java HTTP Clients ===\n");
        demonstrateHttpURLConnection();
        demonstrateHttpClient();
        demonstrateOkHttpPattern();
        demonstrateApacheHttpClientPattern();
    }

    private static void demonstrateHttpURLConnection() {
        System.out.println("--- 1. java.net.HttpURLConnection (Old Way) ---\n");
        System.out.println("Code pattern:");
        System.out.println("  URL url = new URL(\"https://jsonplaceholder.typicode.com/posts/1\");");
        System.out.println("  HttpURLConnection con = (HttpURLConnection) url.openConnection();");
        System.out.println("  con.setRequestMethod(\"GET\");");
        System.out.println("  con.setRequestProperty(\"Accept\", \"application/json\");");
        System.out.println();
        System.out.println("Pros: Built into Java, no external libraries.");
        System.out.println("Cons: Verbose, manual stream handling, lacks modern features.");
        System.out.println("Verdict: Works but outdated. Use HttpClient (Java 11+) instead.\n");
    }

    private static void demonstrateHttpClient() {
        System.out.println("--- 2. java.net.http.HttpClient (Java 11+, Recommended) ---\n");

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
                    .timeout(Duration.ofSeconds(15))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status: " + response.statusCode());
            System.out.println("Body (first 150 chars): " + response.body().substring(0, Math.min(150, response.body().length())) + "...");
        } catch (IOException | InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("\nPros: Modern, async support, built-in, clean API.");
        System.out.println("Cons: Only in Java 11+.\n");
    }

    private static void demonstrateOkHttpPattern() {
        System.out.println("--- 3. OkHttp (Third-Party Library) ---\n");
        System.out.println("Dependency (Maven):");
        System.out.println("  <dependency>");
        System.out.println("    <groupId>com.squareup.okhttp3</groupId>");
        System.out.println("    <artifactId>okhttp</artifactId>");
        System.out.println("    <version>4.12.0</version>");
        System.out.println("  </dependency>");
        System.out.println();
        System.out.println("Code pattern:");
        System.out.println("  OkHttpClient client = new OkHttpClient();");
        System.out.println("  Request request = new Request.Builder()");
        System.out.println("      .url(\"https://api.example.com/data\")");
        System.out.println("      .build();");
        System.out.println("  Response response = client.newCall(request).execute();");
        System.out.println("  String body = response.body().string();");
        System.out.println();
        System.out.println("Pros: Fast, lightweight, supports HTTP/2 and WebSockets.");
        System.out.println("Cons: External dependency.\n");
    }

    private static void demonstrateApacheHttpClientPattern() {
        System.out.println("--- 4. Apache HttpClient (Third-Party Library) ---\n");
        System.out.println("Dependency (Maven):");
        System.out.println("  <dependency>");
        System.out.println("    <groupId>org.apache.httpcomponents.client5</groupId>");
        System.out.println("    <artifactId>httpclient5</artifactId>");
        System.out.println("    <version>5.3</version>");
        System.out.println("  </dependency>");
        System.out.println();
        System.out.println("Code pattern:");
        System.out.println("  try (CloseableHttpClient httpclient = HttpClients.createDefault()) {");
        System.out.println("    HttpGet request = new HttpGet(\"https://api.example.com/data\");");
        System.out.println("    CloseableHttpResponse response = httpclient.execute(request);");
        System.out.println("    String body = EntityUtils.toString(response.getEntity());");
        System.out.println("  }");
        System.out.println();
        System.out.println("Pros: Feature-rich, connection pooling, proxy support.");
        System.out.println("Cons: Heavier, more complex API.\n");

        System.out.println("=== End of HttpClient Forge ===");
    }
}
