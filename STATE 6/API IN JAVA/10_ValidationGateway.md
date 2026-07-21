# 10 - Validation Gateway

## Game Name: Validation Gateway

## What You Learn
- Why input validation matters
- javax.validation annotations
- Using @Valid in controllers
- Creating custom validation annotations

## Key Concepts

### 1. Common Validation Annotations
| Annotation | Rule |
|-----------|------|
| @NotNull | Must not be null |
| @NotBlank | Not null, not empty, not whitespace |
| @Size(min, max) | Length within range |
| @Min(value) | Number >= value |
| @Max(value) | Number <= value |
| @Email | Valid email format |
| @Pattern(regex) | Matches regex pattern |
| @Positive | Number > 0 |
| @Past | Date must be in the past |

### 2. Model with Validation
```java
public class Game {
    @NotBlank(message = "Game name is required")
    private String name;

    @NotBlank(message = "Genre is required")
    private String genre;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 10, message = "Rating cannot exceed 10")
    private int rating;

    @Size(min = 10, max = 500, message = "Description must be 10-500 chars")
    private String description;
}
```

### 3. Controller with @Valid
```java
@PostMapping("/api/games")
public ResponseEntity<?> createGame(@Valid @RequestBody Game game) {
    Game saved = gameService.save(game);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
}
```

### 4. Handle Validation Errors
```java
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<Map<String, String>> handleValidation(
        MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(
        error -> errors.put(error.getField(), error.getDefaultMessage()));
    return ResponseEntity.badRequest().body(errors);
}
```

### 5. Custom Validation
Create your own annotation for complex rules using `@Constraint(validatedBy = ...)`.

## Run This File
```bash
javac 10_ValidationGateway.java
java ValidationGateway
```

## Next Topic
[11 - Auth Bastion](11_AuthBastion.md) - Authentication & Authorization
