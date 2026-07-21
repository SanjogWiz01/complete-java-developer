# 06 - Request Handler Dungeon

## Game Name: Request Handler Dungeon

## What You Learn
- Path variables for dynamic URLs
- Query parameters for filtering
- Request body for data submission
- Form data handling

## Key Concepts

### 1. Path Variables
Extract values from the URL path:
```java
@GetMapping("/api/games/{gameId}/levels/{levelId}")
public ResponseEntity<Level> getLevel(
    @PathVariable Long gameId,
    @PathVariable Long levelId) {
    return ResponseEntity.ok(levelService.find(gameId, levelId));
}
```

### 2. Query Parameters
Parameters after the `?` in a URL:
```
GET /api/players?role=mage&minLevel=5&sort=name
```
```java
@GetMapping("/api/players")
public ResponseEntity<List<Player>> searchPlayers(
    @RequestParam(required = false) String role,
    @RequestParam(defaultValue = "1") int minLevel,
    @RequestParam(defaultValue = "name") String sort) {
    return ResponseEntity.ok(playerService.search(role, minLevel, sort));
}
```

### 3. Request Body
Data sent in POST, PUT, PATCH requests:
```java
@PostMapping("/api/games")
public ResponseEntity<Game> createGame(@RequestBody Game game) {
    Game saved = gameService.save(game);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
}
```

### 4. Form Data
Key-value pairs (Content-Type: application/x-www-form-urlencoded):
```java
@PostMapping("/api/login")
public ResponseEntity<String> login(
    @RequestParam String username,
    @RequestParam String password) {
    return ResponseEntity.ok("Login successful");
}
```

## Run This File
```bash
javac 06_RequestHandlerDungeon.java
java RequestHandlerDungeon
```

## Next Topic
[07 - Response Handler Tower](07_ResponseHandlerTower.md) - HTTP Response Handling
