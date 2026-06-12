import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OrderService {
    private final ProductCatalogRepository productRepository;
    private final OrderPricingService pricingService;
    private final Map<String, Order> ordersById = new ConcurrentHashMap<>();

    public OrderService(ProductCatalogRepository productRepository, OrderPricingService pricingService) {
        ValidationException.when(productRepository == null, "Product repository is required.");
        ValidationException.when(pricingService == null, "Pricing service is required.");
        this.productRepository = productRepository;
        this.pricingService = pricingService;
    }

    public Order startOrder(Customer customer) {
        Order order = Order.create(customer);
        ordersById.put(order.getOrderId(), order);
        return order;
    }

    public Order addProduct(String orderId, String sku, int quantity) {
        Order order = findOrderOrThrow(orderId);
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> NotFoundException.forResource("Product", sku));

        Order updatedOrder = order.addLine(product, quantity);
        return save(updatedOrder);
    }

    public Order updateQuantity(String orderId, String sku, int quantity) {
        Order order = findOrderOrThrow(orderId);
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> NotFoundException.forResource("Product", sku));

        ValidationException.when(!product.hasEnoughStock(quantity), "Not enough stock for " + sku + ".");
        return save(order.updateLineQuantity(sku, quantity));
    }

    public Order removeProduct(String orderId, String sku) {
        return save(findOrderOrThrow(orderId).removeLine(sku));
    }

    public Order applyDiscount(String orderId, BigDecimal discountAmount) {
        return save(findOrderOrThrow(orderId).applyDiscount(discountAmount));
    }

    public Order removeDiscount(String orderId) {
        return save(findOrderOrThrow(orderId).removeDiscount());
    }

    public Order confirmOrder(String orderId) {
        Order order = findOrderOrThrow(orderId);
        validateStockBeforeCommit(order);

        BigDecimal suggestedShipping = pricingService.suggestedShippingFee(order.subtotal());
        Order pricedOrder = order.setShippingFee(suggestedShipping);
        Order confirmedOrder = pricedOrder.confirm();

        for (OrderLine line : confirmedOrder.getLines()) {
            productRepository.reduceStock(line.getSku(), line.getQuantity());
        }

        return save(confirmedOrder);
    }

    public Order markPaid(String orderId) {
        return save(findOrderOrThrow(orderId).markPaid());
    }

    public Order shipOrder(String orderId) {
        return save(findOrderOrThrow(orderId).ship());
    }

    public Order cancelOrder(String orderId, String reason) {
        return save(findOrderOrThrow(orderId).cancel(reason));
    }

    public Order findOrder(String orderId) {
        return findOrderOrThrow(orderId);
    }

    public List<Order> listOrders() {
        return ordersById.values().stream()
                .sorted(Comparator.comparing(Order::getCreatedAt))
                .toList();
    }

    public OrderPricingService.PricingBreakdown price(String orderId) {
        return pricingService.calculate(findOrderOrThrow(orderId));
    }

    private void validateStockBeforeCommit(Order order) {
        ValidationException.when(order.isEmpty(), "Cannot confirm an empty order.");

        for (OrderLine line : order.getLines()) {
            Product currentProduct = productRepository.findBySku(line.getSku())
                    .orElseThrow(() -> NotFoundException.forResource("Product", line.getSku()));
            ValidationException.when(!currentProduct.hasEnoughStock(line.getQuantity()),
                    "Stock changed before checkout for " + line.getSku() + ".");
        }
    }

    private Order findOrderOrThrow(String orderId) {
        ValidationException.when(orderId == null || orderId.trim().isEmpty(), "Order id is required.");
        Order order = ordersById.get(orderId.trim());
        if (order == null) {
            throw NotFoundException.forResource("Order", orderId);
        }
        return order;
    }

    private Order save(Order order) {
        ordersById.put(order.getOrderId(), order);
        return order;
    }
}
