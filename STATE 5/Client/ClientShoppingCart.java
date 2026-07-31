import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClientShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    public static class CartItem implements Serializable {

        private static final long serialVersionUID = 1L;

        private final int productId;
        private final String name;
        private final BigDecimal unitPrice;
        private int quantity;

        public CartItem(int productId, String name, BigDecimal unitPrice, int quantity) {
            this.productId = productId;
            this.name = name;
            this.unitPrice = unitPrice;
            this.quantity = quantity;
        }

        public int getProductId() {
            return productId;
        }

        public String getName() {
            return name;
        }

        public BigDecimal getUnitPrice() {
            return unitPrice;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = Math.max(0, quantity);
        }

        public BigDecimal getLineTotal() {
            return unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }

    private final Map<Integer, CartItem> items;

    public ClientShoppingCart() {
        this.items = new LinkedHashMap<>();
    }

    public void add(int productId, String name, BigDecimal unitPrice, int quantity) {
        if (quantity <= 0) {
            return;
        }
        CartItem existing = items.get(productId);
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
        } else {
            items.put(productId, new CartItem(productId, name, unitPrice, quantity));
        }
    }

    public void updateQuantity(int productId, int quantity) {
        CartItem existing = items.get(productId);
        if (existing == null) {
            return;
        }
        if (quantity <= 0) {
            items.remove(productId);
        } else {
            existing.setQuantity(quantity);
        }
    }

    public void remove(int productId) {
        items.remove(productId);
    }

    public void clear() {
        items.clear();
    }

    public int getItemCount() {
        int count = 0;
        for (CartItem item : items.values()) {
            count += item.getQuantity();
        }
        return count;
    }

    public int getUniqueItemCount() {
        return items.size();
    }

    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items.values()) {
            total = total.add(item.getLineTotal());
        }
        return total;
    }

    public Map<Integer, CartItem> getItems() {
        return items;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
