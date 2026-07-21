# 09 - Error Handling Fortress

## Game Name: Error Handling Fortress

## What You Learn
- Common exception types in APIs
- Global exception handling with @ControllerAdvice
- Custom error response objects
- Error handling strategies

## Key Concepts

### 1. Exception Types
| Exception | HTTP Status | When |
|-----------|-------------|------|
| ResourceNotFoundException | 404 | Resource doesn't exist |
| BadRequestException | 400 | Invalid input |
| UnauthorizedException | 401 | Auth required/failed |
| ForbiddenException | 403 | Not allowed |
| ConflictException | 409 | Duplicate/conflict |

### 2. Global Exception Handler
```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Something went wrong",
            LocalDateTime.now());
        return ResponseEntity.internalServerError().body(error);
    }
}
```

### 3. Custom Error Response
```java
{
    "status": 404,
    "message": "Game with id 99 not found",
    "timestamp": "2024-01-15T10:30:00"
}
```

### 4. Error Handling Strategies
1. **Throw and let @ControllerAdvice handle** (recommended)
2. **Return error responses directly** in controller
3. **Try-catch** for business logic errors in controller

## Run This File
```bash
javac 09_ErrorHandlingFortress.java
java ErrorHandlingFortress
```

## Next Topic
[10 - Validation Gateway](10_ValidationGateway.md) - Input Validation
