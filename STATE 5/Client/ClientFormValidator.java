import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ClientFormValidator {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^[A-Za-z0-9_]{3,20}$");
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[0-9+\\- ]{7,15}$");

    private final Map<String, String> errors;

    public ClientFormValidator() {
        this.errors = new LinkedHashMap<>();
    }

    public void validateRequired(String fieldName, String value, String label) {
        if (value == null || value.trim().isEmpty()) {
            errors.put(fieldName, label + " is required");
        }
    }

    public void validateEmail(String fieldName, String value) {
        if (value == null || value.trim().isEmpty()) {
            errors.put(fieldName, "Email is required");
        } else if (!EMAIL_PATTERN.matcher(value.trim()).matches()) {
            errors.put(fieldName, "Enter a valid email address");
        }
    }

    public void validateUsername(String fieldName, String value) {
        if (value == null || value.trim().isEmpty()) {
            errors.put(fieldName, "Username is required");
        } else if (!USERNAME_PATTERN.matcher(value.trim()).matches()) {
            errors.put(fieldName, "Username must be 3-20 letters, digits or underscores");
        }
    }

    public void validatePassword(String fieldName, String value) {
        if (value == null || value.isEmpty()) {
            errors.put(fieldName, "Password is required");
        } else if (value.length() < 6) {
            errors.put(fieldName, "Password must be at least 6 characters");
        }
    }

    public void validatePasswordMatch(String value, String confirm, String label) {
        if (value == null || confirm == null || !value.equals(confirm)) {
            errors.put("confirmPassword", label + " and confirmation do not match");
        }
    }

    public void validatePhone(String fieldName, String value) {
        if (value != null && !value.trim().isEmpty()
                && !PHONE_PATTERN.matcher(value.trim()).matches()) {
            errors.put(fieldName, "Enter a valid phone number");
        }
    }

    public void validateLength(String fieldName, String value, int max, String label) {
        if (value != null && value.length() > max) {
            errors.put(fieldName, label + " must be at most " + max + " characters");
        }
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public String firstError() {
        return errors.isEmpty() ? null : errors.values().iterator().next();
    }
}
