import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class OrderLine {
    private final Product product;
    private final int quantity;
    private final BigDecimal unitPriceAtPurchase;

    public OrderLine(Product product, int quantity) {
        ValidationException.when(product == null, "Product is required for an order line.");
        ValidationException.when(!product.isActive(), "Inactive products cannot be ordered.");
        ValidationException.when(quantity <= 0, "Order quantity must be greater than zero.");
        ValidationException.when(!product.hasEnoughStock(quantity), "Not enough stock for " + product.getSku() + ".");

        this.product = product;
        this.quantity = quantity;
        this.unitPriceAtPurchase = product.getUnitPrice().setScale(2, RoundingMode.HALF_UP);
    }

    private OrderLine(Product product, int quantity, BigDecimal unitPriceAtPurchase) {
        this.product = product;
        this.quantity = quantity;
        this.unitPriceAtPurchase = unitPriceAtPurchase.setScale(2, RoundingMode.HALF_UP);
    }

    public Product getProduct() {
        return product;
    }

    public String getSku() {
        return product.getSku();
    }

    public String getProductName() {
        return product.getName();
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPriceAtPurchase() {
        return unitPriceAtPurchase;
    }

    public BigDecimal subtotal() {
        return unitPriceAtPurchase
                .multiply(BigDecimal.valueOf(quantity))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public OrderLine withQuantity(int newQuantity) {
        ValidationException.when(newQuantity <= 0, "Order quantity must be greater than zero.");
        ValidationException.when(!product.hasEnoughStock(newQuantity), "Not enough stock for " + product.getSku() + ".");
        return new OrderLine(product, newQuantity, unitPriceAtPurchase);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof OrderLine orderLine)) {
            return false;
        }
        return product.equals(orderLine.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product);
    }

    @Override
    public String toString() {
        return "OrderLine{" +
                "sku='" + getSku() + '\'' +
                ", productName='" + getProductName() + '\'' +
                ", quantity=" + quantity +
                ", unitPriceAtPurchase=" + unitPriceAtPurchase +
                ", subtotal=" + subtotal() +
                '}';
    }
}
