package com.sanjogwiz.state8.repository;

import com.sanjogwiz.state8.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

