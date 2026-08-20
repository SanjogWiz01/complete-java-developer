package com.sanjogwiz.state8.repository;

import com.sanjogwiz.state8.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}

