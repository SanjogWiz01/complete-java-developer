import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public final class Order {
    private static final BigDecimal DEFAULT_TAX_RATE = new BigDecimal("0.13");
    private static final DateTimeFormatter DISPLAY_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final String orderId;
    private final Customer customer;
    private final List<OrderLine> lines;
    private final OrderStatus status;
    private final BigDecimal discountAmount;
    private final BigDecimal shippingFee;
    private final BigDecimal taxRate;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private Order(
            String orderId,
            Customer customer,
            List<OrderLine> lines,
            OrderStatus status,
            BigDecimal discountAmount,
            BigDecimal shippingFee,
            BigDecimal taxRate,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        this.orderId = requireText(orderId, "Order id");
        this.customer = requireCustomer(customer);
        this.lines = Collections.unmodifiableList(new ArrayList<>(requireLines(lines)));
        this.status = Objects.requireNonNull(status, "Order status is required.");
        this.discountAmount = normalizeMoney(discountAmount, "Discount amount");
        this.shippingFee = normalizeMoney(shippingFee, "Shipping fee");
        this.taxRate = normalizeTaxRate(taxRate);
        this.createdAt = Objects.requireNonNull(createdAt, "Created timestamp is required.");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Updated timestamp is required.");

        ValidationException.when(updatedAt.isBefore(createdAt), "Updated timestamp cannot be before created timestamp.");
        ValidationException.when(this.discountAmount.compareTo(subtotalBeforeDiscount(this.lines)) > 0,
                "Discount cannot be greater than subtotal.");
    }

    public static Order create(Customer customer) {
        return create(customer, Clock.systemDefaultZone());
    }

    public static Order create(Customer customer, Clock clock) {
        LocalDateTime now = LocalDateTime.now(clock);
        return new Order(
                UUID.randomUUID().toString(),
                customer,
                List.of(),
                OrderStatus.DRAFT,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                DEFAULT_TAX_RATE,
                now,
                now);
    }

    public String getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<OrderLine> getLines() {
        return lines;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public BigDecimal getShippingFee() {
        return shippingFee;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isEditable() {
        return status == OrderStatus.DRAFT;
    }

    public boolean isEmpty() {
        return lines.isEmpty();
    }

    public int totalItems() {
        return lines.stream()
                .mapToInt(OrderLine::getQuantity)
                .sum();
    }

    public boolean containsProduct(String sku) {
        return lines.stream()
                .anyMatch(line -> line.getSku().equalsIgnoreCase(requireText(sku, "Product sku")));
    }

    public Optional<OrderLine> findLine(String sku) {
        String normalizedSku = requireText(sku, "Product sku");
        return lines.stream()
                .filter(line -> line.getSku().equalsIgnoreCase(normalizedSku))
                .findFirst();
    }

    public Order addLine(Product product, int quantity) {
        requireEditableOrder();
        OrderLine newLine = new OrderLine(product, quantity);
        List<OrderLine> nextLines = new ArrayList<>(lines);
        int existingIndex = findLineIndex(product.getSku(), nextLines);

        if (existingIndex >= 0) {
            OrderLine existingLine = nextLines.get(existingIndex);
            nextLines.set(existingIndex, existingLine.withQuantity(existingLine.getQuantity() + quantity));
        } else {
            nextLines.add(newLine);
        }

        return copyWith(nextLines, status, discountAmount, shippingFee, taxRate);
    }

    public Order updateLineQuantity(String sku, int quantity) {
        requireEditableOrder();
        ValidationException.when(quantity <= 0, "Quantity must be greater than zero.");

        List<OrderLine> nextLines = new ArrayList<>(lines);
        int existingIndex = findLineIndex(sku, nextLines);
        ValidationException.when(existingIndex < 0, "Product " + sku + " is not in the order.");

        OrderLine existingLine = nextLines.get(existingIndex);
        nextLines.set(existingIndex, existingLine.withQuantity(quantity));
        return copyWith(nextLines, status, discountAmount, shippingFee, taxRate);
    }

    public Order removeLine(String sku) {
        requireEditableOrder();
        List<OrderLine> nextLines = new ArrayList<>(lines);
        int existingIndex = findLineIndex(sku, nextLines);
        ValidationException.when(existingIndex < 0, "Product " + sku + " is not in the order.");
        nextLines.remove(existingIndex);

        BigDecimal nextDiscount = discountAmount.min(subtotalBeforeDiscount(nextLines));
        return copyWith(nextLines, status, nextDiscount, shippingFee, taxRate);
    }

    public Order clearLines() {
        requireEditableOrder();
        return copyWith(List.of(), status, BigDecimal.ZERO, shippingFee, taxRate);
    }

    public Order applyDiscount(BigDecimal amount) {
        requireEditableOrder();
        BigDecimal normalizedAmount = normalizeMoney(amount, "Discount amount");
        ValidationException.when(normalizedAmount.compareTo(subtotal()) > 0,
                "Discount cannot be greater than current subtotal.");
        return copyWith(lines, status, normalizedAmount, shippingFee, taxRate);
    }

    public Order removeDiscount() {
        requireEditableOrder();
        return copyWith(lines, status, BigDecimal.ZERO, shippingFee, taxRate);
    }

    public Order setShippingFee(BigDecimal amount) {
        requireEditableOrder();
        return copyWith(lines, status, discountAmount, normalizeMoney(amount, "Shipping fee"), taxRate);
    }

    public Order setTaxRate(BigDecimal rate) {
        requireEditableOrder();
        return copyWith(lines, status, discountAmount, shippingFee, normalizeTaxRate(rate));
    }

    public BigDecimal subtotal() {
        return subtotalBeforeDiscount(lines);
    }

    public BigDecimal taxableAmount() {
        BigDecimal taxable = subtotal()
                .subtract(discountAmount)
                .max(BigDecimal.ZERO);
        return taxable.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal taxAmount() {
        return taxableAmount()
                .multiply(taxRate)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal total() {
        return taxableAmount()
                .add(taxAmount())
                .add(shippingFee)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public Order confirm() {
        validateCanCheckout();
        return moveTo(OrderStatus.CONFIRMED);
    }

    public Order markPaid() {
        return moveTo(OrderStatus.PAID);
    }

    public Order ship() {
        return moveTo(OrderStatus.SHIPPED);
    }

    public Order cancel(String reason) {
        ValidationException.when(status.isTerminal(), "Terminal orders cannot be cancelled.");
        ValidationException.when(reason == null || reason.trim().length() < 5,
                "Cancellation reason must be at least 5 characters.");
        return moveTo(OrderStatus.CANCELLED);
    }

    public String customerSummary() {
        return customer.displayName() + " | " + customer.getShippingAddress();
    }

    public String moneySummary() {
        return "subtotal=" + subtotal()
                + ", discount=" + discountAmount
                + ", tax=" + taxAmount()
                + ", shipping=" + shippingFee
                + ", total=" + total();
    }

    public List<String> receiptLines() {
        List<String> receipt = new ArrayList<>();
        receipt.add("Order: " + orderId);
        receipt.add("Customer: " + customer.displayName());
        receipt.add("Status: " + status);
        receipt.add("Created: " + DISPLAY_DATE.format(createdAt));
        receipt.add("Items:");

        for (OrderLine line : lines) {
            receipt.add(" - " + line.getSku()
                    + " | " + line.getProductName()
                    + " | qty=" + line.getQuantity()
                    + " | unit=" + line.getUnitPriceAtPurchase()
                    + " | subtotal=" + line.subtotal());
        }

        receipt.add("Subtotal: " + subtotal());
        receipt.add("Discount: " + discountAmount);
        receipt.add("Tax: " + taxAmount());
        receipt.add("Shipping: " + shippingFee);
        receipt.add("Total: " + total());
        return Collections.unmodifiableList(receipt);
    }

    private Order moveTo(OrderStatus nextStatus) {
        ValidationException.when(!status.canMoveTo(nextStatus),
                "Order cannot move from " + status + " to " + nextStatus + ".");
        return copyWith(lines, nextStatus, discountAmount, shippingFee, taxRate);
    }

    private void validateCanCheckout() {
        ValidationException.when(lines.isEmpty(), "Cannot confirm an empty order.");
        ValidationException.when(total().compareTo(BigDecimal.ZERO) <= 0, "Order total must be greater than zero.");
    }

    private void requireEditableOrder() {
        ValidationException.when(!isEditable(), "Only draft orders can be edited.");
    }

    private Order copyWith(
            List<OrderLine> nextLines,
            OrderStatus nextStatus,
            BigDecimal nextDiscount,
            BigDecimal nextShippingFee,
            BigDecimal nextTaxRate) {

        return new Order(
                orderId,
                customer,
                nextLines,
                nextStatus,
                nextDiscount,
                nextShippingFee,
                nextTaxRate,
                createdAt,
                LocalDateTime.now());
    }

    private static int findLineIndex(String sku, List<OrderLine> sourceLines) {
        String normalizedSku = requireText(sku, "Product sku");

        for (int index = 0; index < sourceLines.size(); index++) {
            if (sourceLines.get(index).getSku().equalsIgnoreCase(normalizedSku)) {
                return index;
            }
        }

        return -1;
    }

    private static BigDecimal subtotalBeforeDiscount(List<OrderLine> sourceLines) {
        return sourceLines.stream()
                .map(OrderLine::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private static List<OrderLine> requireLines(List<OrderLine> sourceLines) {
        ValidationException.when(sourceLines == null, "Order lines are required.");
        ValidationException.when(sourceLines.stream().anyMatch(Objects::isNull), "Order line cannot be null.");
        return sourceLines;
    }

    private static Customer requireCustomer(Customer customer) {
        ValidationException.when(customer == null, "Customer is required.");
        return customer;
    }

    private static String requireText(String value, String fieldName) {
        ValidationException.when(value == null || value.trim().isEmpty(), fieldName + " is required.");
        return value.trim();
    }

    private static BigDecimal normalizeMoney(BigDecimal value, String fieldName) {
        ValidationException.when(value == null, fieldName + " is required.");
        ValidationException.when(value.compareTo(BigDecimal.ZERO) < 0, fieldName + " cannot be negative.");
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    private static BigDecimal normalizeTaxRate(BigDecimal rate) {
        ValidationException.when(rate == null, "Tax rate is required.");
        ValidationException.when(rate.compareTo(BigDecimal.ZERO) < 0, "Tax rate cannot be negative.");
        ValidationException.when(rate.compareTo(BigDecimal.ONE) > 0, "Tax rate cannot be greater than 1.");
        return rate.setScale(4, RoundingMode.HALF_UP);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Order order)) {
            return false;
        }
        return orderId.equals(order.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", customer=" + customer.displayName() +
                ", lines=" + lines.size() +
                ", status=" + status +
                ", discountAmount=" + discountAmount +
                ", shippingFee=" + shippingFee +
                ", taxRate=" + taxRate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
