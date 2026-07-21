public class SpringBootArena {

    public static void main(String[] args) {
        System.out.println("=== SPRING BOOT ARENA: Spring Boot for API Development ===\n");
        projectSetup();
        controllers();
        requestMappings();
        dependencyInjection();
    }

    private static void projectSetup() {
        System.out.println("--- 1. Project Setup ---\n");
        System.out.println("Use Spring Initializr (https://start.spring.io) to generate a project.\n");
        System.out.println("Key dependencies for API development:");
        System.out.println("  - spring-boot-starter-web  : REST API support (embedded Tomcat).");
        System.out.println("  - spring-boot-starter-data-jpa : Database access.");
        System.out.println("  - spring-boot-starter-validation : Input validation.");
        System.out.println("  - spring-boot-starter-security : Authentication/authorization.");
        System.out.println("  - lombok : Reduce boilerplate code.");
        System.out.println("  - jackson : JSON serialization (included with starter-web).\n");

        System.out.println("Maven pom.xml snippet:");
        System.out.println("  <parent>");
        System.out.println("    <groupId>org.springframework.boot</groupId>");
        System.out.println("    <artifactId>spring-boot-starter-parent</artifactId>");
        System.out.println("    <version>3.2.0</version>");
        System.out.println("  </parent>");
        System.out.println("  <dependencies>");
        System.out.println("    <dependency>");
        System.out.println("      <groupId>org.springframework.boot</groupId>");
        System.out.println("      <artifactId>spring-boot-starter-web</artifactId>");
        System.out.println("    </dependency>");
        System.out.println("  </dependencies>\n");
    }

    private static void controllers() {
        System.out.println("--- 2. Controllers ---\n");
        System.out.println("Controllers handle incoming HTTP requests and return responses.\n");

        System.out.println("Code pattern (PlayerController.java):");
        System.out.println("  @RestController");
        System.out.println("  @RequestMapping(\"/api/players\")");
        System.out.println("  public class PlayerController {");
        System.out.println();
        System.out.println("      @GetMapping");
        System.out.println("      public ResponseEntity<List<Player>> getAllPlayers() {");
        System.out.println("          return ResponseEntity.ok(playerService.findAll());");
        System.out.println("      }");
        System.out.println();
        System.out.println("      @GetMapping(\"/{id}\")");
        System.out.println("      public ResponseEntity<Player> getPlayerById(@PathVariable Long id) {");
        System.out.println("          return ResponseEntity.ok(playerService.findById(id));");
        System.out.println("      }");
        System.out.println();
        System.out.println("      @PostMapping");
        System.out.println("      public ResponseEntity<Player> createPlayer(@RequestBody Player player) {");
        System.out.println("          Player saved = playerService.save(player);");
        System.out.println("          return ResponseEntity.status(HttpStatus.CREATED).body(saved);");
        System.out.println("      }");
        System.out.println("  }\n");
        System.out.println("@RestController = @Controller + @ResponseBody (auto-converts to JSON).\n");
    }

    private static void requestMappings() {
        System.out.println("--- 3. Request Mappings ---\n");
        System.out.println("Annotations for mapping HTTP methods:\n");

        String[][] mappings = {
            {"@GetMapping(\"/path\")", "Maps GET requests to the path."},
            {"@PostMapping(\"/path\")", "Maps POST requests."},
            {"@PutMapping(\"/path\")", "Maps PUT requests."},
            {"@PatchMapping(\"/path\")", "Maps PATCH requests."},
            {"@DeleteMapping(\"/path\")", "Maps DELETE requests."},
            {"@RequestMapping(\"/path\")", "Maps ALL HTTP methods (or specify method attribute)."}
        };

        for (String[] m : mappings) {
            System.out.println("  " + m[0]);
            System.out.println("    -> " + m[1]);
        }
        System.out.println();
    }

    private static void dependencyInjection() {
        System.out.println("--- 4. Dependency Injection (Service Layer) ---\n");
        System.out.println("Separate business logic from controllers using services.\n");

        System.out.println("  @Service");
        System.out.println("  public class PlayerService {");
        System.out.println();
        System.out.println("      private final PlayerRepository repository;");
        System.out.println();
        System.out.println("      @Autowired");
        System.out.println("      public PlayerService(PlayerRepository repository) {");
        System.out.println("          this.repository = repository;");
        System.out.println("      }");
        System.out.println();
        System.out.println("      public List<Player> findAll() {");
        System.out.println("          return repository.findAll();");
        System.out.println("      }");
        System.out.println();
        System.out.println("      public Player save(Player player) {");
        System.out.println("          return repository.save(player);");
        System.out.println("      }");
        System.out.println("  }\n");

        System.out.println("@Service marks the class as a Spring-managed bean.");
        System.out.println("Spring automatically injects the repository dependency.\n");

        System.out.println("=== End of Spring Boot Arena ===");
    }
}
