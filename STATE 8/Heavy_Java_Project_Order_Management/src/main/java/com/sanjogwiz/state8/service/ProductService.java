package com.sanjogwiz.state8.service;

import com.sanjogwiz.state8.domain.Product;
import com.sanjogwiz.state8.dto.CreateProductRequest;
import com.sanjogwiz.state8.exception.ResourceNotFoundException;
import com.sanjogwiz.state8.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product create(CreateProductRequest request) {
        Product product = new Product();
        product.setSku(request.sku());
        product.setName(request.name());
        product.setUnitPrice(request.unitPrice());
        product.setStockQuantity(request.stockQuantity());
        return productRepository.save(product);
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found for id " + id));
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }
}

