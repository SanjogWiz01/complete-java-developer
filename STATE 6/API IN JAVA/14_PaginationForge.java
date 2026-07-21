import java.util.ArrayList;
import java.util.List;

public class PaginationForge {

    public static void main(String[] args) {
        System.out.println("=== PAGINATION FORGE: Performance & Pagination ===\n");
        whyPagination();
        paginationImplementation();
        filteringAndSorting();
        cachingStrategies();
    }

    private static void whyPagination() {
        System.out.println("--- Why Pagination? ---\n");
        System.out.println("Without pagination:");
        System.out.println("  GET /api/games -> returns 10,000 games at once.");
        System.out.println("  Problems: slow response, high memory usage, poor client performance.\n");

        System.out.println("With pagination:");
        System.out.println("  GET /api/games?page=0&size=20 -> returns first 20 games.");
        System.out.println("  Benefits: fast responses, manageable data, scalable.\n");
    }

    private static void paginationImplementation() {
        System.out.println("--- 1. Spring Data JPA Pagination ---\n");

        System.out.println("Repository:");
        System.out.println("  public interface GameRepository extends JpaRepository<Game, Long> {");
        System.out.println("      Page<Game> findAll(Pageable pageable);");
        System.out.println("  }\n");

        System.out.println("Service:");
        System.out.println("  public Page<Game> getGames(int page, int size) {");
        System.out.println("      Pageable pageable = PageRequest.of(page, size, Sort.by(\"name\").ascending());");
        System.out.println("      return gameRepository.findAll(pageable);");
        System.out.println("  }\n");

        System.out.println("Controller:");
        System.out.println("  @GetMapping(\"/api/games\")");
        System.out.println("  public ResponseEntity<Page<Game>> getGames(");
        System.out.println("      @RequestParam(defaultValue = \"0\") int page,");
        System.out.println("      @RequestParam(defaultValue = \"20\") int size) {");
        System.out.println("      Page<Game> games = gameService.getGames(page, size);");
        System.out.println("      return ResponseEntity.ok(games);");
        System.out.println("  }\n");

        System.out.println("Response structure:");
        System.out.println("  {");
        System.out.println("    \"content\": [{\"id\":1,\"name\":\"Game A\"}, ...],");
        System.out.println("    \"pageable\": {\"pageNumber\":0,\"pageSize\":20},");
        System.out.println("    \"totalElements\": 150,");
        System.out.println("    \"totalPages\": 8,");
        System.out.println("    \"last\": false,");
        System.out.println("    \"first\": true,");
        System.out.println("    \"numberOfElements\": 20");
        System.out.println("  }\n");
    }

    private static void filteringAndSorting() {
        System.out.println("--- 2. Filtering & Sorting ---\n");

        System.out.println("Query parameters for filtering:");
        System.out.println("  GET /api/games?page=0&size=10&genre=RPG&minRating=7&sort=name,asc\n");

        System.out.println("Spring Data JPA Specification approach:");
        System.out.println("  @GetMapping(\"/api/games\")");
        System.out.println("  public ResponseEntity<Page<Game>> searchGames(");
        System.out.println("      @RequestParam(required = false) String genre,");
        System.out.println("      @RequestParam(required = false) Integer minRating,");
        System.out.println("      @RequestParam(defaultValue = \"name\") String sort,");
        System.out.println("      @RequestParam(defaultValue = \"asc\") String direction,");
        System.out.println("      @RequestParam(defaultValue = \"0\") int page,");
        System.out.println("      @RequestParam(defaultValue = \"20\") int size) {");
        System.out.println();
        System.out.println("      Sort sortObj = direction.equals(\"asc\")");
        System.out.println("          ? Sort.by(sort).ascending()");
        System.out.println("          : Sort.by(sort).descending();");
        System.out.println("      Pageable pageable = PageRequest.of(page, size, sortObj);");
        System.out.println();
        System.out.println("      Page<Game> games = gameService.search(genre, minRating, pageable);");
        System.out.println("      return ResponseEntity.ok(games);");
        System.out.println("  }\n");

        System.out.println("Cursor-based pagination (alternative for large datasets):");
        System.out.println("  GET /api/games?cursor=abc123&limit=20");
        System.out.println("  Uses an opaque cursor instead of page numbers.");
        System.out.println("  Better for real-time feeds and infinite scroll.\n");
    }

    private static void cachingStrategies() {
        System.out.println("--- 3. Caching ---\n");

        System.out.println("Spring Cache with @Cacheable:");
        System.out.println("  @Service");
        System.out.println("  public class GameService {");
        System.out.println();
        System.out.println("      @Cacheable(value = \"games\", key = \"#id\")");
        System.out.println("      public Game findById(Long id) {");
        System.out.println("          // Database call (only executed on cache miss)");
        System.out.println("          return gameRepository.findById(id).orElse(null);");
        System.out.println("      }");
        System.out.println();
        System.out.println("      @CacheEvict(value = \"games\", key = \"#id\")");
        System.out.println("      public void deleteById(Long id) {");
        System.out.println("          gameRepository.deleteById(id);");
        System.out.println("      }");
        System.out.println("  }\n");

        System.out.println("Enable caching:");
        System.out.println("  @SpringBootApplication");
        System.out.println("  @EnableCaching");
        System.out.println("  public class Application { ... }\n");

        System.out.println("Cache providers:");
        System.out.println("  - Simple in-memory (default, good for development).");
        System.out.println("  - EhCache: distributed in-memory caching.");
        System.out.println("  - Redis: fast, distributed, used in production.");
        System.out.println("  - Caffeine: high-performance in-memory cache.\n");

        System.out.println("Performance tips:");
        System.out.println("  1. Always paginate large result sets.");
        System.out.println("  2. Use database indexes on frequently queried columns.");
        System.out.println("  3. Cache frequently read, rarely changed data.");
        System.out.println("  4. Use lazy loading for relationships.");
        System.out.println("  5. Limit response fields with DTOs (do not expose full entities).\n");

        System.out.println("=== End of Pagination Forge ===");
    }
}
