package unit7.boot;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductService {
    private final AtomicLong ids = new AtomicLong(2);
    private final List<Product> products = new ArrayList<>(
            List.of(new Product(1L, "Keyboard", 1800), new Product(2L, "Mouse", 900))
    );

    public List<Product> all() {
        return List.copyOf(products);
    }

    public Product add(String name, double price) {
        Product p = new Product(ids.incrementAndGet(), name, price);
        products.add(p);
        return p;
    }

    public boolean delete(long id) {
        return products.removeIf(p -> p.id().equals(id));
    }
}
