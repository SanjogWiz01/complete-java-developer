public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public static NotFoundException forResource(String resourceName, String identifier) {
        return new NotFoundException(resourceName + " was not found for id: " + identifier);
    }
}
