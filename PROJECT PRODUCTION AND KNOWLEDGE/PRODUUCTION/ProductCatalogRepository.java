import java.util.List;
import java.util.Optional;

public interface ProductCatalogRepository {
    Optional<Product> findBySku(String sku);

    List<Product> findActiveProducts();

    Product save(Product product);

    void reduceStock(String sku, int quantity);
}
