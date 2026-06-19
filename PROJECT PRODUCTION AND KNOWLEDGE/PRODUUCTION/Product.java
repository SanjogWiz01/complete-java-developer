import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Product {
    private final String sku;
    private final String name;
    private final BigDecimal unitPrice;
    private final int stockQuantity;
    private final boolean active;

    public Product(String sku, String name, BigDecimal unitPrice, int stockQuantity, boolean active) {
        this.sku = requireText(sku, "Product sku");
        this.name = requireText(name, "Product name");
        this.unitPrice = normalizePrice(unitPrice);
        this.stockQuantity = requireNonNegative(stockQuantity, "Stock quantity");
        this.active = active;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public boolean isActive() {
        return active;
    }

    public boolean hasEnoughStock(int requestedQuantity) {
        return active && requestedQuantity > 0 && stockQuantity >= requestedQuantity;
    }

    public Product reduceStock(int quantity) {
        ValidationException.when(quantity <= 0, "Quantity must be greater than zero.");
        ValidationException.when(stockQuantity < quantity, "Not enough stock for product " + sku + ".");
        return new Product(sku, name, unitPrice, stockQuantity - quantity, active);
    }

    public Product activate() {
        return new Product(sku, name, unitPrice, stockQuantity, true);
    }

    public Product deactivate() {
        return new Product(sku, name, unitPrice, stockQuantity, false);
    }

    private static String requireText(String value, String fieldName) {
        ValidationException.when(value == null || value.trim().isEmpty(), fieldName + " is required.");
        return value.trim();
    }

    private static BigDecimal normalizePrice(BigDecimal price) {
        ValidationException.when(price == null, "Product price is required.");
        ValidationException.when(price.compareTo(BigDecimal.ZERO) <= 0, "Product price must be greater than zero.");
        return price.setScale(2, RoundingMode.HALF_UP);
    }

    private static int requireNonNegative(int value, String fieldName) {
        ValidationException.when(value < 0, fieldName + " cannot be negative.");
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Product product)) {
            return false;
        }
        return sku.equals(product.sku);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sku);
    }

    @Override
    public String toString() {
        return "Product{" +
                "sku='" + sku + '\'' +
                ", name='" + name + '\'' +
                ", unitPrice=" + unitPrice +
                ", stockQuantity=" + stockQuantity +
                ", active=" + active +
                '}';
    }
}
