package unit7.boot;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<Product> all() {
        return service.all();
    }

    @PostMapping
    public Product add(@RequestBody Map<String, Object> body) {
        String name = String.valueOf(body.get("name"));
        double price = Double.parseDouble(String.valueOf(body.get("price")));
        return service.add(name, price);
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> delete(@PathVariable long id) {
        return Map.of("deleted", service.delete(id));
    }
}
