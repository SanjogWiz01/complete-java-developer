import java.util.Objects;
import java.util.regex.Pattern;

public final class Customer {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private final String customerId;
    private final String fullName;
    private final String email;
    private final String shippingAddress;

    public Customer(String customerId, String fullName, String email, String shippingAddress) {
        this.customerId = requireText(customerId, "Customer id");
        this.fullName = requireText(fullName, "Customer name");
        this.email = requireEmail(email);
        this.shippingAddress = requireText(shippingAddress, "Shipping address");
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public String displayName() {
        return fullName + " <" + email + ">";
    }

    private static String requireText(String value, String fieldName) {
        ValidationException.when(value == null || value.trim().isEmpty(), fieldName + " is required.");
        return value.trim();
    }

    private static String requireEmail(String value) {
        String normalizedEmail = requireText(value, "Email").toLowerCase();
        ValidationException.when(!EMAIL_PATTERN.matcher(normalizedEmail).matches(), "Email format is invalid.");
        return normalizedEmail;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Customer customer)) {
            return false;
        }
        return customerId.equals(customer.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId='" + customerId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", shippingAddress='" + shippingAddress + '\'' +
                '}';
    }
}
