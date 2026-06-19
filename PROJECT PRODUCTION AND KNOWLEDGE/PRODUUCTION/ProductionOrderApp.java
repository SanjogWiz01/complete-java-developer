import java.math.BigDecimal;
import java.util.List;

public class ProductionOrderApp {
    public static void main(String[] args) {
        ProductCatalogRepository productRepository = new InMemoryProductCatalogRepository();
        OrderPricingService pricingService = new OrderPricingService();
        OrderService orderService = new OrderService(productRepository, pricingService);

        printSection("Available Products");
        printCatalog(productRepository.findActiveProducts());

        Customer customer = new Customer(
                "CUST-1001",
                "Sanjog Wizard",
                "sanjog@example.com",
                "Kathmandu, Nepal");

        Order order = orderService.startOrder(customer);
        order = orderService.addProduct(order.getOrderId(), "JAVA-BOOK", 2);
        order = orderService.addProduct(order.getOrderId(), "API-COURSE", 1);
        order = orderService.applyDiscount(order.getOrderId(), new BigDecimal("15.00"));

        printSection("Draft Order");
        printReceipt(order);
        printPricing(orderService.price(order.getOrderId()));

        order = orderService.confirmOrder(order.getOrderId());
        order = orderService.markPaid(order.getOrderId());
        order = orderService.shipOrder(order.getOrderId());

        printSection("Completed Order");
        printReceipt(order);

        printSection("Remaining Active Products");
        printCatalog(productRepository.findActiveProducts());

        printSection("Order History");
        for (Order savedOrder : orderService.listOrders()) {
            System.out.println(savedOrder.getOrderId()
                    + " | " + savedOrder.getStatus()
                    + " | " + savedOrder.customerSummary()
                    + " | " + savedOrder.moneySummary());
        }
    }

    private static void printSection(String title) {
        System.out.println();
        System.out.println("============================================================");
        System.out.println(title);
        System.out.println("============================================================");
    }

    private static void printCatalog(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("No active products are available.");
            return;
        }

        for (Product product : products) {
            System.out.printf(
                    "%-12s | %-28s | price=%8s | stock=%3d%n",
                    product.getSku(),
                    product.getName(),
                    product.getUnitPrice(),
                    product.getStockQuantity());
        }
    }

    private static void printReceipt(Order order) {
        for (String line : order.receiptLines()) {
            System.out.println(line);
        }
    }

    private static void printPricing(OrderPricingService.PricingBreakdown breakdown) {
        System.out.println("Pricing breakdown: " + breakdown.toDisplayText());
    }
}
