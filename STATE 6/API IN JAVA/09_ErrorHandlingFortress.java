import java.time.LocalDateTime;

public class ErrorHandlingFortress {

    public static void main(String[] args) {
        System.out.println("=== ERROR HANDLING FORTRESS ===\n");
        exceptionTypes();
        globalExceptionHandler();
        customErrors();
        handlingStrategies();
    }

    private static void exceptionTypes() {
        System.out.println("--- 1. Exception Types in APIs ---\n");
        System.out.println("RuntimeExceptions (unchecked, common):");
        System.out.println("  - ResourceNotFoundException  -> 404 Not Found");
        System.out.println("  - BadRequestException         -> 400 Bad Request");
        System.out.println("  - UnauthorizedException       -> 401 Unauthorized");
        System.out.println("  - ForbiddenException          -> 403 Forbidden");
        System.out.println("  - ConflictException           -> 409 Conflict\n");

        System.out.println("Checked Exceptions:");
        System.out.println("  - IOException, SQLException");
        System.out.println("  - Typically handled in service layer.\n");

        System.out.println("Spring exceptions:");
        System.out.println("  - HttpRequestMethodNotSupportedException -> 405");
        System.out.println("  - HttpMediaTypeNotSupportedException     -> 415");
        System.out.println("  - MethodArgumentNotValidException         -> 400\n");
    }

    private static void globalExceptionHandler() {
        System.out.println("--- 2. Global Exception Handling with @ControllerAdvice ---\n");
        System.out.println("Handle all exceptions in one place.\n");

        System.out.println("Code pattern:");
        System.out.println("  @RestControllerAdvice");
        System.out.println("  public class GlobalExceptionHandler {");
        System.out.println();
        System.out.println("      @ExceptionHandler(ResourceNotFoundException.class)");
        System.out.println("      public ResponseEntity<ErrorResponse> handleNotFound(");
        System.out.println("              ResourceNotFoundException ex) {");
        System.out.println("          ErrorResponse error = new ErrorResponse(");
        System.out.println("              HttpStatus.NOT_FOUND.value(),");
        System.out.println("              ex.getMessage(),");
        System.out.println("              LocalDateTime.now());");
        System.out.println("          return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);");
        System.out.println("      }");
        System.out.println();
        System.out.println("      @ExceptionHandler(Exception.class)");
        System.out.println("      public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {");
        System.out.println("          ErrorResponse error = new ErrorResponse(");
        System.out.println("              HttpStatus.INTERNAL_SERVER_ERROR.value(),");
        System.out.println("              \"Something went wrong\",");
        System.out.println("              LocalDateTime.now());");
        System.out.println("          return ResponseEntity.internalServerError().body(error);");
        System.out.println("      }");
        System.out.println("  }\n");
    }

    private static void customErrors() {
        System.out.println("--- 3. Custom Error Response ---\n");

        System.out.println("ErrorResponse class:");
        System.out.println("  public class ErrorResponse {");
        System.out.println("      private int status;");
        System.out.println("      private String message;");
        System.out.println("      private LocalDateTime timestamp;");
        System.out.println("      // constructor, getters");
        System.out.println("  }\n");

        System.out.println("Example JSON response on error:");
        System.out.println("  {");
        System.out.println("    \"status\": 404,");
        System.out.println("    \"message\": \"Game with id 99 not found\",");
        System.out.println("    \"timestamp\": \"2024-01-15T10:30:00\"");
        System.out.println("  }\n");

        System.out.println("Custom exception class:");
        System.out.println("  public class ResourceNotFoundException extends RuntimeException {");
        System.out.println("      public ResourceNotFoundException(String message) {");
        System.out.println("          super(message);");
        System.out.println("      }");
        System.out.println("  }\n");
    }

    private static void handlingStrategies() {
        System.out.println("--- 4. Error Handling Strategies ---\n");

        System.out.println("Strategy 1: Throw and let @ControllerAdvice handle it.");
        System.out.println("  if (game == null) {");
        System.out.println("      throw new ResourceNotFoundException(\"Game not found: \" + id);");
        System.out.println("  }\n");

        System.out.println("Strategy 2: Return error responses directly.");
        System.out.println("  @GetMapping(\"/api/games/{id}\")");
        System.out.println("  public ResponseEntity<?> getGame(@PathVariable Long id) {");
        System.out.println("      Optional<Game> game = gameService.findById(id);");
        System.out.println("      if (game.isEmpty()) {");
        System.out.println("          return ResponseEntity.status(404)");
        System.out.println("              .body(Map.of(\"error\", \"Game not found\"));");
        System.out.println("      }");
        System.out.println("      return ResponseEntity.ok(game.get());");
        System.out.println("  }\n");

        System.out.println("Strategy 3: Use try-catch in controller for business logic errors.");
        System.out.println("  try {");
        System.out.println("      gameService.transferGame(id, targetId);");
        System.out.println("  } catch (IllegalStateException e) {");
        System.out.println("      return ResponseEntity.badRequest().body(Map.of(\"error\", e.getMessage()));");
        System.out.println("  }\n");

        System.out.println("Best practice: Use @ControllerAdvice for consistency across the API.\n");

        System.out.println("=== End of Error Handling Fortress ===");
    }
}
