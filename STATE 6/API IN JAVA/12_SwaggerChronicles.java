import java.util.List;

public class SwaggerChronicles {

    public static void main(String[] args) {
        System.out.println("=== SWAGGER CHRONICLES: API Documentation ===\n");
        whyDocument();
        openApiOverview();
        springdocSetup();
        annotationsAndUI();
        bestPractices();
    }

    private static void whyDocument() {
        System.out.println("--- Why Document Your API? ---\n");
        System.out.println("  1. Frontend developers need to know endpoints, parameters, and formats.");
        System.out.println("  2. QA teams need documentation to write test cases.");
        System.out.println("  3. Third-party integrators need clear contracts.");
        System.out.println("  4. Self-documenting reduces maintenance overhead.\n");
    }

    private static void openApiOverview() {
        System.out.println("--- OpenAPI Specification ---\n");
        System.out.println("OpenAPI (formerly Swagger) is a standard for describing REST APIs.\n");
        System.out.println("Key concepts:");
        System.out.println("  - OpenAPI 3.0: current standard (JSON or YAML format).");
        System.out.println("  - Describes: endpoints, request/response formats, auth, schemas.");
        System.out.println("  - Tools generate client SDKs, server stubs, and documentation.\n");
    }

    private static void springdocSetup() {
        System.out.println("--- Springdoc Setup (Recommended) ---\n");
        System.out.println("Dependency:");
        System.out.println("  <dependency>");
        System.out.println("    <groupId>org.springdoc</groupId>");
        System.out.println("    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>");
        System.out.println("    <version>2.3.0</version>");
        System.out.println("  </dependency>\n");

        System.out.println("That's it! Auto-discovers @RestController endpoints.\n");
        System.out.println("Access Swagger UI at: http://localhost:8080/swagger-ui.html");
        System.out.println("Access OpenAPI JSON at: http://localhost:8080/v3/api-docs\n");
    }

    private static void annotationsAndUI() {
        System.out.println("--- Annotations for Customization ---\n");

        System.out.println("@Tag(name = \"Games\", description = \"Game management endpoints\")");
        System.out.println("  - Groups endpoints under a tag in Swagger UI.\n");

        System.out.println("@Operation(summary = \"Get game by ID\", description = \"Retrieves a single game\")");
        System.out.println("  - Describes what a specific endpoint does.\n");

        System.out.println("@ApiResponse(responseCode = \"200\", description = \"Game found\")");
        System.out.println("@ApiResponse(responseCode = \"404\", description = \"Game not found\")");
        System.out.println("  - Documents possible response codes and their meanings.\n");

        System.out.println("@Parameter(description = \"Game ID\", required = true, example = \"1\")");
        System.out.println("  - Describes path variables and query parameters.\n");

        System.out.println("@Schema(description = \"Game entity\")");
        System.out.println("  - Add to model classes to describe fields.\n");

        System.out.println("Example controller with annotations:");
        System.out.println("  @Tag(name = \"Games\", description = \"Game CRUD operations\")");
        System.out.println("  @RestController");
        System.out.println("  @RequestMapping(\"/api/games\")");
        System.out.println("  public class GameController {");
        System.out.println();
        System.out.println("      @Operation(summary = \"Get all games\")");
        System.out.println("      @GetMapping");
        System.out.println("      public List<Game> getAll() {");
        System.out.println("          return gameService.findAll();");
        System.out.println("      }");
        System.out.println();
        System.out.println("      @Operation(summary = \"Get game by ID\")");
        System.out.println("      @ApiResponse(responseCode = \"200\", description = \"Found\")");
        System.out.println("      @ApiResponse(responseCode = \"404\", description = \"Not found\")");
        System.out.println("      @GetMapping(\"/{id}\")");
        System.out.println("      public Game getById(@Parameter(description = \"Game ID\") @PathVariable Long id) {");
        System.out.println("          return gameService.findById(id);");
        System.out.println("      }");
        System.out.println("  }\n");
    }

    private static void bestPractices() {
        System.out.println("--- Documentation Best Practices ---\n");
        System.out.println("  1. Use meaningful descriptions, not just field names.");
        System.out.println("  2. Provide example values for all parameters.");
        System.out.println("  3. Document all error responses (4xx, 5xx).");
        System.out.println("  4. Use tags to group related endpoints.");
        System.out.println("  5. Include request/response examples in descriptions.");
        System.out.println("  6. Keep docs up to date (auto-generation helps!).");
        System.out.println("  7. Version your docs alongside your API.\n");

        System.out.println("=== End of Swagger Chronicles ===");
    }
}
