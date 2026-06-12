import java.math.BigDecimal;
import java.math.RoundingMode;

public class OrderPricingService {
    private static final BigDecimal FREE_SHIPPING_LIMIT = new BigDecimal("150.00");
    private static final BigDecimal STANDARD_SHIPPING_FEE = new BigDecimal("8.50");

    public PricingBreakdown calculate(Order order) {
        ValidationException.when(order == null, "Order is required.");

        BigDecimal subtotal = order.subtotal();
        BigDecimal discount = order.getDiscountAmount();
        BigDecimal taxableAmount = order.taxableAmount();
        BigDecimal tax = order.taxAmount();
        BigDecimal shipping = order.getShippingFee();
        BigDecimal total = order.total();

        return new PricingBreakdown(subtotal, discount, taxableAmount, tax, shipping, total);
    }

    public BigDecimal suggestedShippingFee(BigDecimal subtotal) {
        ValidationException.when(subtotal == null, "Subtotal is required.");
        ValidationException.when(subtotal.compareTo(BigDecimal.ZERO) < 0, "Subtotal cannot be negative.");

        if (subtotal.compareTo(FREE_SHIPPING_LIMIT) >= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        return STANDARD_SHIPPING_FEE.setScale(2, RoundingMode.HALF_UP);
    }

    public static final class PricingBreakdown {
        private final BigDecimal subtotal;
        private final BigDecimal discount;
        private final BigDecimal taxableAmount;
        private final BigDecimal tax;
        private final BigDecimal shipping;
        private final BigDecimal total;

        public PricingBreakdown(
                BigDecimal subtotal,
                BigDecimal discount,
                BigDecimal taxableAmount,
                BigDecimal tax,
                BigDecimal shipping,
                BigDecimal total) {
            this.subtotal = normalize(subtotal);
            this.discount = normalize(discount);
            this.taxableAmount = normalize(taxableAmount);
            this.tax = normalize(tax);
            this.shipping = normalize(shipping);
            this.total = normalize(total);
        }

        public BigDecimal getSubtotal() {
            return subtotal;
        }

        public BigDecimal getDiscount() {
            return discount;
        }

        public BigDecimal getTaxableAmount() {
            return taxableAmount;
        }

        public BigDecimal getTax() {
            return tax;
        }

        public BigDecimal getShipping() {
            return shipping;
        }

        public BigDecimal getTotal() {
            return total;
        }

        public String toDisplayText() {
            return "subtotal=" + subtotal
                    + ", discount=" + discount
                    + ", taxable=" + taxableAmount
                    + ", tax=" + tax
                    + ", shipping=" + shipping
                    + ", total=" + total;
        }

        private static BigDecimal normalize(BigDecimal value) {
            ValidationException.when(value == null, "Pricing value is required.");
            return value.setScale(2, RoundingMode.HALF_UP);
        }
    }
}
