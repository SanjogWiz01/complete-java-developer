# 14 - Pagination Forge: Performance & Pagination

## Game Name: Pagination Forge

## What You Learn
- Why pagination matters
- Spring Data JPA pagination
- Filtering and sorting
- Caching strategies

## Key Concepts

### 1. Why Pagination?
Without: 10,000 records returned at once (slow, memory-heavy).
With: 20 records per page (fast, scalable).

### 2. Spring Data JPA Pagination
**Repository:**
```java
public interface GameRepository extends JpaRepository<Game, Long> {
    Page<Game> findAll(Pageable pageable);
}
```

**Controller:**
```java
@GetMapping("/api/games")
public ResponseEntity<Page<Game>> getGames(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
    return ResponseEntity.ok(gameRepository.findAll(pageable));
}
```

**Response includes:** content, totalElements, totalPages, pageNumber, pageSize.

### 3. Filtering & Sorting
```
GET /api/games?page=0&size=10&genre=RPG&minRating=7&sort=name,asc
```

### 4. Caching
```java
@Service
public class GameService {

    @Cacheable(value = "games", key = "#id")
    public Game findById(Long id) {
        return gameRepository.findById(id).orElse(null);
    }

    @CacheEvict(value = "games", key = "#id")
    public void deleteById(Long id) {
        gameRepository.deleteById(id);
    }
}
```

Enable with `@EnableCaching` on your main class.

**Cache providers:** Simple in-memory, EhCache, Redis, Caffeine.

### Performance Tips
1. Always paginate large result sets
2. Use database indexes
3. Cache frequently read data
4. Use lazy loading for relationships
5. Use DTOs to limit response fields

## Run This File
```bash
javac 14_PaginationForge.java
java PaginationForge
```

## Next Topic
[15 - Deployment Launchpad](15_DeploymentLaunchpad.md) - Deployment & Versioning
