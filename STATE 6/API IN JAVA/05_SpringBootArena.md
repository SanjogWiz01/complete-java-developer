# 05 - Spring Boot Arena: Spring Boot for API Development

## Game Name: Spring Boot Arena

## What You Learn
- Setting up a Spring Boot project
- Creating REST controllers
- Request mapping annotations
- Service layer and dependency injection

## Key Concepts

### 1. Project Setup
Use [Spring Initializr](https://start.spring.io) with these dependencies:
- `spring-boot-starter-web` - REST API support
- `spring-boot-starter-data-jpa` - Database access
- `spring-boot-starter-validation` - Input validation
- `lombok` - Reduce boilerplate

### 2. Controller Pattern
```java
@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @GetMapping
    public ResponseEntity<List<Player>> getAllPlayers() {
        return ResponseEntity.ok(playerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable Long id) {
        return ResponseEntity.ok(playerService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        Player saved = playerService.save(player);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
```

### 3. Mapping Annotations
| Annotation | HTTP Method |
|-----------|-------------|
| @GetMapping | GET |
| @PostMapping | POST |
| @PutMapping | PUT |
| @PatchMapping | PATCH |
| @DeleteMapping | DELETE |

### 4. Service Layer
Separate business logic from controllers:
```java
@Service
public class PlayerService {
    private final PlayerRepository repository;

    public PlayerService(PlayerRepository repository) {
        this.repository = repository;
    }

    public List<Player> findAll() {
        return repository.findAll();
    }
}
```

## Run This File
```bash
javac 05_SpringBootArena.java
java SpringBootArena
```

## Next Topic
[06 - Request Handler Dungeon](06_RequestHandlerDungeon.md) - Handling HTTP Requests
