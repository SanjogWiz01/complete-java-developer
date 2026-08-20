package com.sanjogwiz.state8.service;

import com.sanjogwiz.state8.domain.Customer;
import com.sanjogwiz.state8.dto.CreateCustomerRequest;
import com.sanjogwiz.state8.exception.ResourceNotFoundException;
import com.sanjogwiz.state8.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer create(CreateCustomerRequest request) {
        Customer customer = new Customer();
        customer.setFullName(request.fullName());
        customer.setEmail(request.email());
        return customerRepository.save(customer);
    }

    public Customer getById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found for id " + id));
    }

    public List<Customer> getAll() {
        return customerRepository.findAll();
    }
}

