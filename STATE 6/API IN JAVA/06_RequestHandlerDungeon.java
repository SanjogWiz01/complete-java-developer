public class RequestHandlerDungeon {

    public static void main(String[] args) {
        System.out.println("=== REQUEST HANDLER DUNGEON ===\n");
        pathVariables();
        queryParameters();
        requestBody();
        formData();
    }

    private static void pathVariables() {
        System.out.println("--- 1. Path Variables ---\n");
        System.out.println("Path variables extract values from the URI path.\n");
        System.out.println("URL: /api/games/42/levels/7");
        System.out.println("Mapping: @GetMapping(\"/api/games/{gameId}/levels/{levelId}\")\n");

        System.out.println("Code pattern:");
        System.out.println("  @GetMapping(\"/api/games/{gameId}/levels/{levelId}\")");
        System.out.println("  public ResponseEntity<Level> getLevel(");
        System.out.println("      @PathVariable Long gameId,");
        System.out.println("      @PathVariable Long levelId) {");
        System.out.println("      return ResponseEntity.ok(levelService.find(gameId, levelId));");
        System.out.println("  }\n");

        System.out.println("Multiple path variables in one method are common.");
        System.out.println("Use @PathVariable(\"name\") when variable name differs from param name.\n");
    }

    private static void queryParameters() {
        System.out.println("--- 2. Query Parameters ---\n");
        System.out.println("Query parameters come after the ? in a URL.\n");
        System.out.println("URL: /api/players?role=mage&minLevel=5&sort=name\n");

        System.out.println("Code pattern:");
        System.out.println("  @GetMapping(\"/api/players\")");
        System.out.println("  public ResponseEntity<List<Player>> searchPlayers(");
        System.out.println("      @RequestParam(required = false) String role,");
        System.out.println("      @RequestParam(defaultValue = \"1\") int minLevel,");
        System.out.println("      @RequestParam(defaultValue = \"name\") String sort) {");
        System.out.println("      return ResponseEntity.ok(playerService.search(role, minLevel, sort));");
        System.out.println("  }\n");

        System.out.println("Key points:");
        System.out.println("  - @RequestParam binds URL query params to method parameters.");
        System.out.println("  - required = false makes it optional.");
        System.out.println("  - defaultValue provides a fallback when param is missing.\n");
    }

    private static void requestBody() {
        System.out.println("--- 3. Request Body ---\n");
        System.out.println("Request body carries data in POST, PUT, and PATCH requests.\n");
        System.out.println("JSON sent by client:");
        System.out.println("  {");
        System.out.println("    \"name\": \"Dragon Slayer\",");
        System.out.println("    \"genre\": \"RPG\",");
        System.out.println("    \"difficulty\": \"HARD\"");
        System.out.println("  }\n");

        System.out.println("Code pattern:");
        System.out.println("  @PostMapping(\"/api/games\")");
        System.out.println("  public ResponseEntity<Game> createGame(@RequestBody Game game) {");
        System.out.println("      Game saved = gameService.save(game);");
        System.out.println("      return ResponseEntity.status(HttpStatus.CREATED).body(saved);");
        System.out.println("  }\n");

        System.out.println("@RequestBody deserializes JSON to Java object using Jackson.");
        System.out.println("Content-Type header must be application/json.\n");
    }

    private static void formData() {
        System.out.println("--- 4. Form Data ---\n");
        System.out.println("Form data is sent as key-value pairs (Content-Type: application/x-www-form-urlencoded).\n");

        System.out.println("Code pattern:");
        System.out.println("  @PostMapping(\"/api/login\")");
        System.out.println("  public ResponseEntity<String> login(");
        System.out.println("      @RequestParam String username,");
        System.out.println("      @RequestParam String password) {");
        System.out.println("      // authenticate user");
        System.out.println("      return ResponseEntity.ok(\"Login successful\");");
        System.out.println("  }\n");

        System.out.println("For multipart form data (file uploads):");
        System.out.println("  @PostMapping(\"/api/upload\")");
        System.out.println("  public ResponseEntity<String> upload(");
        System.out.println("      @RequestParam String title,");
        System.out.println("      @RequestParam MultipartFile file) {");
        System.out.println("      // handle file upload");
        System.out.println("      return ResponseEntity.ok(\"Uploaded: \" + file.getOriginalFilename());");
        System.out.println("  }\n");

        System.out.println("@RequestParam works for both regular form data and multipart.");
        System.out.println("@ModelAttribute is used when binding to a POJO object.\n");

        System.out.println("=== End of Request Handler Dungeon ===");
    }
}
