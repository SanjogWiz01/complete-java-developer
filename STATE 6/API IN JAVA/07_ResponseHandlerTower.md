# 07 - Response Handler Tower

## Game Name: Response Handler Tower

## What You Learn
- Returning JSON from controllers
- Setting HTTP status codes
- Using ResponseEntity for full control
- Custom response wrappers

## Key Concepts

### 1. Returning JSON
Spring auto-converts Java objects to JSON using Jackson:
```java
@GetMapping("/api/games/{id}")
public Game getGame(@PathVariable Long id) {
    return gameService.findById(id);
}
```
Spring automatically sets `Content-Type: application/json` and returns `200 OK`.

### 2. Setting Status Codes
**With ResponseEntity:**
```java
@PostMapping("/api/games")
public ResponseEntity<Game> create(@RequestBody Game game) {
    Game saved = gameService.save(game);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
}
```

**With @ResponseStatus:**
```java
@DeleteMapping("/api/games/{id}")
@ResponseStatus(HttpStatus.NO_CONTENT)
public void delete(@PathVariable Long id) {
    gameService.delete(id);
}
```

### 3. ResponseEntity Full Control
```java
public ResponseEntity<?> getPlayer(@PathVariable Long id) {
    Optional<Player> player = playerService.findById(id);
    if (player.isPresent()) {
        return ResponseEntity.ok(player.get());
    } else {
        Map<String, String> error = new HashMap<>();
        error.put("message", "Player not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
```

### 4. Custom Response Wrapper
Consistent API response format:
```java
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
}
```

## Run This File
```bash
javac 07_ResponseHandlerTower.java
java ResponseHandlerTower
```

## Next Topic
[08 - Serialization Workshop](08_SerializationWorkshop.md) - JSON Serialization & Deserialization
