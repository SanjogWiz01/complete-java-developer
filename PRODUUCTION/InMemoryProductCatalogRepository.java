import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryProductCatalogRepository implements ProductCatalogRepository {
    private final Map<String, Product> productsBySku = new ConcurrentHashMap<>();

    public InMemoryProductCatalogRepository() {
        seedProducts();
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        ValidationException.when(sku == null || sku.trim().isEmpty(), "Product sku is required.");
        return Optional.ofNullable(productsBySku.get(normalizeSku(sku)));
    }

    @Override
    public List<Product> findActiveProducts() {
        return productsBySku.values().stream()
                .filter(Product::isActive)
                .sorted(Comparator.comparing(Product::getName))
                .toList();
    }

    @Override
    public Product save(Product product) {
        ValidationException.when(product == null, "Product is required.");
        productsBySku.put(normalizeSku(product.getSku()), product);
        return product;
    }

    @Override
    public void reduceStock(String sku, int quantity) {
        String normalizedSku = normalizeSku(sku);
        productsBySku.compute(normalizedSku, (key, currentProduct) -> {
            if (currentProduct == null) {
                throw NotFoundException.forResource("Product", sku);
            }
            return currentProduct.reduceStock(quantity);
        });
    }

    private void seedProducts() {
        save(new Product("JAVA-BOOK", "Clean Java Handbook", new BigDecimal("49.99"), 25, true));
        save(new Product("API-COURSE", "REST API Design Course", new BigDecimal("129.00"), 15, true));
        save(new Product("DEPLOY-KIT", "Deployment Checklist Kit", new BigDecimal("19.50"), 40, true));
        save(new Product("LEGACY-CD", "Legacy Training CD", new BigDecimal("9.99"), 2, false));
    }

    private static String normalizeSku(String sku) {
        ValidationException.when(sku == null || sku.trim().isEmpty(), "Product sku is required.");
        return sku.trim().toUpperCase(Locale.ROOT);
    }
}
