package com.sanjogwiz.state8.data;

import com.sanjogwiz.state8.domain.Customer;
import com.sanjogwiz.state8.domain.Product;
import com.sanjogwiz.state8.repository.CustomerRepository;
import com.sanjogwiz.state8.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SeedDataLoader implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public SeedDataLoader(CustomerRepository customerRepository, ProductRepository productRepository) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (customerRepository.count() == 0L) {
            Customer customer = new Customer();
            customer.setFullName("Sanjog Demo");
            customer.setEmail("sanjog.demo@example.com");
            customerRepository.save(customer);
        }

        if (productRepository.count() == 0L) {
            Product keyboard = new Product();
            keyboard.setSku("KEY-001");
            keyboard.setName("Mechanical Keyboard");
            keyboard.setUnitPrice(new BigDecimal("89.99"));
            keyboard.setStockQuantity(50);
            productRepository.save(keyboard);

            Product mouse = new Product();
            mouse.setSku("MSE-002");
            mouse.setName("Wireless Mouse");
            mouse.setUnitPrice(new BigDecimal("34.50"));
            mouse.setStockQuantity(80);
            productRepository.save(mouse);
        }
    }
}

