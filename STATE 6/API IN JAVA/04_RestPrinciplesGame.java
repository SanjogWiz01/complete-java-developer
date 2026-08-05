public class RestPrinciplesGame {

    public static void main(String[] args) {
        System.out.println("=== REST PRINCIPLES GAME ===\n");
        statelessness();
        resourceBasedUris();
        dataFormats();
        designBestPractices();
    }

    private static void statelessness() {
        System.out.println("--- 1. Statelessness ---\n");
        System.out.println("Rule: Each request must contain ALL information the server needs.");
        System.out.println("The server does NOT remember previous requests from the same client.\n");
        System.out.println("Why?");
        System.out.println("  - Easy to scale: any server instance can handle any request.");
        System.out.println("  - Reliable: no session data lost if a server crashes.");
        System.out.println("  - Simple: server logic is straightforward.\n");
        System.out.println("How is state maintained then?");
        System.out.println("  - Client sends tokens (JWT, API key) with each request.");
        System.out.println("  - Server validates the token but does not store session state.\n");

        String[] stateless = {"GET /api/orders - requires Auth header", "POST /api/orders - requires Auth header + body"};
        for (String s : stateless) {
            System.out.println("  -> " + s);
        }
        System.out.println("  Each request is self-contained.\n");
    }

    private static void resourceBasedUris() {
        System.out.println("--- 2. Resource-Based URIs ---\n");
        System.out.println("Everything is a resource. URIs identify resources (not actions).\n");

        System.out.println("GOOD (resource-based):");
        String[] goodUris = {
            "GET    /api/users          -> list all users",
            "GET    /api/users/42       -> get user 42",
            "POST   /api/users          -> create a new user",
            "PUT    /api/users/42       -> update user 42",
            "DELETE /api/users/42       -> delete user 42",
            "GET    /api/users/42/orders -> get orders of user 42"
        };
        for (String uri : goodUris) {
            System.out.println("  " + uri);
        }

        System.out.println("\nBAD (action-based):");
        String[] badUris = {
            "GET /api/getUser?id=42",
            "POST /api/createUser",
            "POST /api/deleteUser?id=42"
        };
        for (String uri : badUris) {
            System.out.println("  " + uri);
        }
        System.out.println();
    }

    private static void dataFormats() {
        System.out.println("--- 3. Data Formats: JSON vs XML ---\n");

        System.out.println("JSON (JavaScript Object Notation):");
        System.out.println("  {");
        System.out.println("    \"id\": 1,");
        System.out.println("    \"name\": \"Player One\",");
        System.out.println("    \"level\": 5");
        System.out.println("  }");
        System.out.println("  Pros: Lightweight, easy to read, native to JavaScript.\n");

        System.out.println("XML (Extensible Markup Language):");
        System.out.println("  <user>");
        System.out.println("    <id>1</id>");
        System.out.println("    <name>Player One</name>");
        System.out.println("    <level>5</level>");
        System.out.println("  </user>");
        System.out.println("  Pros: Strict typing, schemas (XSD), used in SOAP.\n");

        System.out.println("JSON is the default for modern REST APIs.");
        System.out.println("Set Content-Type and Accept headers to specify format.\n");
    }

    private static void designBestPractices() {
        System.out.println("--- REST Design Best Practices ---\n");
        System.out.println("  1. Use plural nouns for resources: /api/users not /api/user");
        System.out.println("  2. Nest resources for relationships: /api/users/42/orders");
        System.out.println("  3. Use HTTP methods correctly (GET=read, POST=create, etc.)");
        System.out.println("  4. Return proper status codes (201 for create, 404 for not found).");
        System.out.println("  5. Support filtering: /api/users?role=admin&status=active");
        System.out.println("  6. Version your API: /api/v1/users");
        System.out.println("  7. Use HATEOAS links for navigation when possible.\n");

        System.out.println("=== End of Rest Principles Game ===");
    }
}
