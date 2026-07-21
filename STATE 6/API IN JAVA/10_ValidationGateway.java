import java.util.List;

public class ValidationGateway {

    public static void main(String[] args) {
        System.out.println("=== VALIDATION GATEWAY ===\n");
        whyValidate();
        javaxValidationAnnotations();
        springValidation();
        customValidation();
    }

    private static void whyValidate() {
        System.out.println("--- Why Validate Input? ---\n");
        System.out.println("  1. Prevent invalid data from entering your system.");
        System.out.println("  2. Return clear error messages to the API client.");
        System.out.println("  3. Protect database integrity.");
        System.out.println("  4. Security: prevent injection attacks.\n");
        System.out.println("Never trust client input. Always validate on the server side.\n");
    }

    private static void javaxValidationAnnotations() {
        System.out.println("--- javax.validation Annotations ---\n");
        System.out.println("Dependency (Maven):");
        System.out.println("  <dependency>");
        System.out.println("    <groupId>org.springframework.boot</groupId>");
        System.out.println("    <artifactId>spring-boot-starter-validation</artifactId>");
        System.out.println("  </dependency>\n");

        System.out.println("Common annotations:");
        String[][] annotations = {
            {"@NotNull", "Field must not be null."},
            {"@NotBlank", "String must not be null, empty, or whitespace only."},
            {"@NotEmpty", "Collection/string must not be null or empty."},
            {"@Size(min, max)", "String or collection length must be within range."},
            {"@Min(value)", "Number must be >= value."},
            {"@Max(value)", "Number must be <= value."},
            {"@Email", "Must be a valid email address."},
            {"@Pattern(regex)", "String must match the regex pattern."},
            {"@Positive", "Number must be positive (> 0)."},
            {"@PositiveOrZero", "Number must be >= 0."},
            {"@Past", "Date must be in the past."},
            {"@Future", "Date must be in the future."}
        };
        for (String[] a : annotations) {
            System.out.println("  " + a[0] + " -> " + a[1]);
        }
        System.out.println();
    }

    private static void springValidation() {
        System.out.println("--- Using @Valid in Controllers ---\n");

        System.out.println("Model class:");
        System.out.println("  public class Game {");
        System.out.println("      @NotBlank(message = \"Game name is required\")");
        System.out.println("      private String name;");
        System.out.println();
        System.out.println("      @NotBlank(message = \"Genre is required\")");
        System.out.println("      private String genre;");
        System.out.println();
        System.out.println("      @Min(value = 1, message = \"Rating must be at least 1\")");
        System.out.println("      @Max(value = 10, message = \"Rating cannot exceed 10\")");
        System.out.println("      private int rating;");
        System.out.println();
        System.out.println("      @Size(min = 10, max = 500, message = \"Description must be 10-500 characters\")");
        System.out.println("      private String description;");
        System.out.println("  }\n");

        System.out.println("Controller:");
        System.out.println("  @PostMapping(\"/api/games\")");
        System.out.println("  public ResponseEntity<?> createGame(@Valid @RequestBody Game game) {");
        System.out.println("      Game saved = gameService.save(game);");
        System.out.println("      return ResponseEntity.status(HttpStatus.CREATED).body(saved);");
        System.out.println("  }\n");

        System.out.println("When validation fails, Spring throws MethodArgumentNotValidException.");
        System.out.println("Handle it in @ControllerAdvice:");
        System.out.println("  @ExceptionHandler(MethodArgumentNotValidException.class)");
        System.out.println("  public ResponseEntity<Map<String, String>> handleValidation(");
        System.out.println("          MethodArgumentNotValidException ex) {");
        System.out.println("      Map<String, String> errors = new HashMap<>();");
        System.out.println("      ex.getBindingResult().getFieldErrors().forEach(");
        System.out.println("          error -> errors.put(error.getField(), error.getDefaultMessage()));");
        System.out.println("      return ResponseEntity.badRequest().body(errors);");
        System.out.println("  }\n");
    }

    private static void customValidation() {
        System.out.println("--- Custom Validation ---\n");
        System.out.println("Create your own annotation for complex rules.\n");

        System.out.println("Step 1: Define annotation:");
        System.out.println("  @Target({ElementType.FIELD})");
        System.out.println("  @Retention(RetentionPolicy.RUNTIME)");
        System.out.println("  @Constraint(validatedBy = GameNameValidator.class)");
        System.out.println("  public @interface ValidGameName {");
        System.out.println("      String message() default \"Invalid game name\";");
        System.out.println("      Class<?>[] groups() default {};");
        System.out.println("      Class<? extends Payload>[] payload() default {};");
        System.out.println("  }\n");

        System.out.println("Step 2: Implement validator:");
        System.out.println("  public class GameNameValidator implements ConstraintValidator<ValidGameName, String> {");
        System.out.println("      @Override");
        System.out.println("      public boolean isValid(String value, ConstraintValidatorContext ctx) {");
        System.out.println("          return value != null && value.length() >= 3 && value.length() <= 50;");
        System.out.println("      }");
        System.out.println("  }\n");

        System.out.println("Step 3: Use it:");
        System.out.println("  @ValidGameName");
        System.out.println("  private String name;\n");

        System.out.println("=== End of Validation Gateway ===");
    }
}
