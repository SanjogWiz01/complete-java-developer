public class ApiQuest {

    public static void main(String[] args) {
        System.out.println("=== API QUEST: Understanding API Basics ===\n");
        whatIsAnApi();
        typesOfApis();
        howJavaInteracts();
    }

    private static void whatIsAnApi() {
        System.out.println("--- What is an API? ---");
        System.out.println("API = Application Programming Interface.");
        System.out.println("It is a contract between two software components.");
        System.out.println("Client sends a request, server sends back a response.\n");
        System.out.println("Real-world analogy:");
        System.out.println("  Restaurant: You (client) give order to waiter (API) who talks to kitchen (server).");
        System.out.println("  You do not need to know how the kitchen works. You just use the menu (API docs).\n");
    }

    private static void typesOfApis() {
        System.out.println("--- Types of APIs ---\n");

        System.out.println("1. REST (Representational State Transfer):");
        System.out.println("   - Uses standard HTTP methods (GET, POST, PUT, DELETE).");
        System.out.println("   - Stateless: each request is independent.");
        System.out.println("   - Data format: JSON or XML.");
        System.out.println("   - Most popular for web and mobile apps.\n");

        System.out.println("2. SOAP (Simple Object Access Protocol):");
        System.out.println("   - Uses XML only.");
        System.out.println("   - Has strict standards and built-in security (WS-Security).");
        System.out.println("   - Heavier, used in enterprise/banking systems.\n");

        System.out.println("3. GraphQL:");
        System.out.println("   - Client specifies exactly what data it wants.");
        System.out.println("   - Single endpoint for all queries.");
        System.out.println("   - Reduces over-fetching and under-fetching.\n");
    }

    private static void howJavaInteracts() {
        System.out.println("--- How Java Interacts with APIs ---\n");

        System.out.println("Java provides built-in classes:");
        System.out.println("  - java.net.HttpURLConnection (old way).");
        System.out.println("  - java.net.http.HttpClient (Java 11+, modern way).");
        System.out.println();
        System.out.println("Third-party libraries:");
        System.out.println("  - OkHttp: lightweight and fast.");
        System.out.println("  - Apache HttpClient: feature-rich.");
        System.out.println("  - RestTemplate / WebClient: Spring framework.\n");

        System.out.println("Workflow in Java:");
        System.out.println("  1. Build the request (URL, method, headers, body).");
        System.out.println("  2. Send the request using an HTTP client.");
        System.out.println("  3. Receive the response.");
        System.out.println("  4. Parse the response (JSON/XML to Java objects).");
        System.out.println("  5. Handle errors if status code is not 2xx.\n");

        String exampleJson = "{\"id\":1,\"title\":\"Java API Basics\",\"status\":\"success\"}";
        System.out.println("Example JSON response: " + exampleJson);
        System.out.println("\n=== End of API Quest ===");
    }
}
