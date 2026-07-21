public class TestingGauntlet {

    public static void main(String[] args) {
        System.out.println("=== TESTING GAUNTLET: Testing APIs ===\n");
        testingPyramid();
        unitTesting();
        springMockMvc();
        restAssured();
        testingBestPractices();
    }

    private static void testingPyramid() {
        System.out.println("--- Testing Pyramid ---\n");
        System.out.println("        /\\");
        System.out.println("       /  \\       E2E Tests (few, slow, expensive)");
        System.out.println("      /    \\");
        System.out.println("     /------\\     Integration Tests (moderate)");
        System.out.println("    /        \\");
        System.out.println("   /----------\\   Unit Tests (many, fast, cheap)");
        System.out.println("  /____________\\\n");
        System.out.println("  - Unit tests: test individual classes/methods in isolation.");
        System.out.println("  - Integration tests: test multiple components working together.");
        System.out.println("  - E2E tests: test the full application from client to database.\n");
    }

    private static void unitTesting() {
        System.out.println("--- 1. Unit Testing with JUnit 5 + Mockito ---\n");

        System.out.println("Dependencies:");
        System.out.println("  <dependency>");
        System.out.println("    <groupId>org.springframework.boot</groupId>");
        System.out.println("    <artifactId>spring-boot-starter-test</artifactId>");
        System.out.println("    <scope>test</scope>");
        System.out.println("  </dependency>\n");

        System.out.println("Example: Testing a service method:");
        System.out.println("  @ExtendWith(MockitoExtension.class)");
        System.out.println("  class GameServiceTest {");
        System.out.println();
        System.out.println("      @Mock");
        System.out.println("      private GameRepository repository;");
        System.out.println();
        System.out.println("      @InjectMocks");
        System.out.println("      private GameService gameService;");
        System.out.println();
        System.out.println("      @Test");
        System.out.println("      void shouldReturnGameWhenIdExists() {");
        System.out.println("          Game game = new Game(1L, \"RPG Quest\", \"RPG\");");
        System.out.println("          when(repository.findById(1L)).thenReturn(Optional.of(game));");
        System.out.println();
        System.out.println("          Game result = gameService.findById(1L);");
        System.out.println();
        System.out.println("          assertNotNull(result);");
        System.out.println("          assertEquals(\"RPG Quest\", result.getName());");
        System.out.println("          verify(repository, times(1)).findById(1L);");
        System.out.println("      }");
        System.out.println();
        System.out.println("      @Test");
        System.out.println("      void shouldThrowExceptionWhenIdNotFound() {");
        System.out.println("          when(repository.findById(99L)).thenReturn(Optional.empty());");
        System.out.println();
        System.out.println("          assertThrows(ResourceNotFoundException.class,");
        System.out.println("              () -> gameService.findById(99L));");
        System.out.println("      }");
        System.out.println("  }\n");
    }

    private static void springMockMvc() {
        System.out.println("--- 2. Integration Testing with MockMvc ---\n");
        System.out.println("MockMvc simulates HTTP requests without starting a real server.\n");

        System.out.println("Example:");
        System.out.println("  @SpringBootTest");
        System.out.println("  @AutoConfigureMockMvc");
        System.out.println("  class GameControllerTest {");
        System.out.println();
        System.out.println("      @Autowired");
        System.out.println("      private MockMvc mockMvc;");
        System.out.println();
        System.out.println("      @Autowired");
        System.out.println("      private ObjectMapper objectMapper;");
        System.out.println();
        System.out.println("      @Test");
        System.out.println("      void shouldReturnGameById() throws Exception {");
        System.out.println("          mockMvc.perform(get(\"/api/games/1\")");
        System.out.println("              .contentType(MediaType.APPLICATION_JSON))");
        System.out.println("              .andExpect(status().isOk())");
        System.out.println("              .andExpect(jsonPath(\"$.name\").value(\"RPG Quest\"))");
        System.out.println("              .andExpect(jsonPath(\"$.genre\").value(\"RPG\"));");
        System.out.println("      }");
        System.out.println();
        System.out.println("      @Test");
        System.out.println("      void shouldCreateGame() throws Exception {");
        System.out.println("          Game game = new Game(null, \"New Game\", \"Puzzle\");");
        System.out.println("          mockMvc.perform(post(\"/api/games\")");
        System.out.println("              .contentType(MediaType.APPLICATION_JSON)");
        System.out.println("              .content(objectMapper.writeValueAsString(game)))");
        System.out.println("              .andExpect(status().isCreated())");
        System.out.println("              .andExpect(jsonPath(\"$.id\").isNumber());");
        System.out.println("      }");
        System.out.println("  }\n");
    }

    private static void restAssured() {
        System.out.println("--- 3. REST Assured ---\n");
        System.out.println("REST Assured is a Java library for testing REST APIs.\n");
        System.out.println("Dependency:");
        System.out.println("  <dependency>");
        System.out.println("    <groupId>io.rest-assured</groupId>");
        System.out.println("    <artifactId>rest-assured</artifactId>");
        System.out.println("    <scope>test</scope>");
        System.out.println("  </dependency>\n");

        System.out.println("Example:");
        System.out.println("  @Test");
        System.out.println("  void shouldGetGameFromRunningServer() {");
        System.out.println("      given()");
        System.out.println("          .baseUri(\"http://localhost:8080\")");
        System.out.println("          .header(\"Accept\", \"application/json\")");
        System.out.println("      .when()");
        System.out.println("          .get(\"/api/games/1\")");
        System.out.println("      .then()");
        System.out.println("          .statusCode(200)");
        System.out.println("          .body(\"name\", equalTo(\"RPG Quest\"));");
        System.out.println("  }\n");
    }

    private static void testingBestPractices() {
        System.out.println("--- Testing Best Practices ---\n");
        System.out.println("  1. Follow AAA pattern: Arrange, Act, Assert.");
        System.out.println("  2. Test one thing per test method.");
        System.out.println("  3. Use meaningful test names: shouldReturn404WhenGameNotFound.");
        System.out.println("  4. Mock external dependencies (database, APIs).");
        System.out.println("  5. Test both happy path and error cases.");
        System.out.println("  6. Use @DataJpaTest for repository-level tests (auto-configured in-memory DB).");
        System.out.println("  7. Use @WebMvcTest for controller-only tests (lighter, faster).");
        System.out.println("  8. Use test containers for real database integration tests.\n");

        System.out.println("=== End of Testing Gauntlet ===");
    }
}
