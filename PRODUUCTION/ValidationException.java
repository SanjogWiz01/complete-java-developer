public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }

    public static void when(boolean condition, String message) {
        if (condition) {
            throw new ValidationException(message);
        }
    }
}
