package com.jyotibank.dao;

import com.jyotibank.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    long create(Customer customer);
    Optional<Customer> findById(long customerId);
    List<Customer> findAllActive();
    void update(Customer customer);
}
