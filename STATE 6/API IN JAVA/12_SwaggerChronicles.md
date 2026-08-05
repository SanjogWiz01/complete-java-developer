# 12 - Swagger Chronicles: API Documentation

## Game Name: Swagger Chronicles

## What You Learn
- Why API documentation matters
- OpenAPI specification overview
- Springdoc setup in Spring Boot
- Swagger annotations for customization

## Key Concepts

### 1. Why Document?
- Frontend devs need to know endpoints, parameters, formats
- QA teams write test cases from docs
- Third-party integrators need clear contracts
- Self-documenting reduces maintenance

### 2. OpenAPI Specification
Standard for describing REST APIs (JSON or YAML). Describes endpoints, request/response formats, auth, schemas.

### 3. Springdoc Setup (Recommended)
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

**Access points:**
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

### 4. Key Annotations
| Annotation | Purpose |
|-----------|---------|
| @Tag(name, description) | Group endpoints |
| @Operation(summary, description) | Describe endpoint |
| @ApiResponse | Document response codes |
| @Parameter | Describe parameters |
| @Schema | Describe model fields |

### 5. Example
```java
@Tag(name = "Games", description = "Game CRUD operations")
@RestController
@RequestMapping("/api/games")
public class GameController {

    @Operation(summary = "Get all games")
    @GetMapping
    public List<Game> getAll() {
        return gameService.findAll();
    }
}
```

### Best Practices
1. Use meaningful descriptions
2. Provide example values
3. Document all error responses
4. Use tags to group endpoints
5. Keep docs up to date

## Run This File
```bash
javac 12_SwaggerChronicles.java
java SwaggerChronicles
```

## Next Topic
[13 - Testing Gauntlet](13_TestingGauntlet.md) - Testing APIs
