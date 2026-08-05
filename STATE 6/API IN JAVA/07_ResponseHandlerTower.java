import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseHandlerTower {

    public static void main(String[] args) {
        System.out.println("=== RESPONSE HANDLER TOWER ===\n");
        returningJson();
        httpStatusCodes();
        responseEntityDemo();
        customResponseWrapper();
    }

    private static void returningJson() {
        System.out.println("--- 1. Returning JSON ---\n");
        System.out.println("Spring auto-converts Java objects to JSON using Jackson.\n");

        System.out.println("Just return an object from a @RestController method:");
        System.out.println("  @GetMapping(\"/api/games/{id}\")");
        System.out.println("  public Game getGame(@PathVariable Long id) {");
        System.out.println("      return gameService.findById(id);");
        System.out.println("  }");
        System.out.println();
        System.out.println("Spring automatically:");
        System.out.println("  1. Serializes Game object to JSON.");
        System.out.println("  2. Sets Content-Type: application/json header.");
        System.out.println("  3. Returns 200 OK status.\n");

        System.out.println("For XML, add @XmlRootElement to model and set Accept: application/xml.\n");
    }

    private static void httpStatusCodes() {
        System.out.println("--- 2. Setting HTTP Status Codes ---\n");
        System.out.println("Use ResponseEntity or @ResponseStatus to control status codes.\n");

        System.out.println("With ResponseEntity:");
        System.out.println("  @PostMapping(\"/api/games\")");
        System.out.println("  public ResponseEntity<Game> create(@RequestBody Game game) {");
        System.out.println("      Game saved = gameService.save(game);");
        System.out.println("      return ResponseEntity.status(HttpStatus.CREATED).body(saved);");
        System.out.println("  }\n");

        System.out.println("With @ResponseStatus:");
        System.out.println("  @DeleteMapping(\"/api/games/{id}\")");
        System.out.println("  @ResponseStatus(HttpStatus.NO_CONTENT)");
        System.out.println("  public void delete(@PathVariable Long id) {");
        System.out.println("      gameService.delete(id);");
        System.out.println("  }\n");

        System.out.println("Common status codes to return:");
        String[][] statusCodes = {
            {"200 OK", "Successful GET, PUT, PATCH."},
            {"201 Created", "Successful POST that creates a resource."},
            {"204 No Content", "Successful DELETE with no body returned."},
            {"400 Bad Request", "Invalid input from client."},
            {"404 Not Found", "Resource does not exist."},
            {"500 Internal Server Error", "Unexpected server failure."}
        };
        for (String[] sc : statusCodes) {
            System.out.println("  " + sc[0] + " -> " + sc[1]);
        }
        System.out.println();
    }

    private static void responseEntityDemo() {
        System.out.println("--- 3. ResponseEntity Full Demo ---\n");
        System.out.println("ResponseEntity gives full control over the HTTP response.\n");

        System.out.println("Code pattern:");
        System.out.println("  public ResponseEntity<?> getPlayer(@PathVariable Long id) {");
        System.out.println("      Optional<Player> player = playerService.findById(id);");
        System.out.println("      if (player.isPresent()) {");
        System.out.println("          return ResponseEntity.ok(player.get());");
        System.out.println("      } else {");
        System.out.println("          Map<String, String> error = new HashMap<>();");
        System.out.println("          error.put(\"message\", \"Player not found\");");
        System.out.println("          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);");
        System.out.println("      }");
        System.out.println("  }\n");
    }

    private static void customResponseWrapper() {
        System.out.println("--- 4. Custom Response Wrapper ---\n");
        System.out.println("Wrap responses in a consistent structure for your API.\n");

        System.out.println("Generic wrapper class:");
        System.out.println("  public class ApiResponse<T> {");
        System.out.println("      private boolean success;");
        System.out.println("      private String message;");
        System.out.println("      private T data;");
        System.out.println("      // constructor, getters, setters");
        System.out.println("  }\n");

        System.out.println("Usage:");
        System.out.println("  @GetMapping(\"/api/games/{id}\")");
        System.out.println("  public ResponseEntity<ApiResponse<Game>> getGame(@PathVariable Long id) {");
        System.out.println("      Game game = gameService.findById(id);");
        System.out.println("      ApiResponse<Game> response = new ApiResponse<>(true, \"Success\", game);");
        System.out.println("      return ResponseEntity.ok(response);");
        System.out.println("  }");
        System.out.println();
        System.out.println("Benefits: consistent format, easy for frontend to parse.\n");

        System.out.println("=== End of Response Handler Tower ===");
    }
}
