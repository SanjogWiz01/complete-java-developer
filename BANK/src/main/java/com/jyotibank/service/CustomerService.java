package com.jyotibank.service;

import com.jyotibank.dao.CustomerDao;
import com.jyotibank.exception.CustomerNotFoundException;
import com.jyotibank.model.Customer;
import com.jyotibank.util.InputValidator;

import java.util.List;

public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public long createCustomer(Customer customer) {
        if (!InputValidator.isValidEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Invalid email address.");
        }
        if (!InputValidator.isValidPhone(customer.getPhone())) {
            throw new IllegalArgumentException("Invalid phone number.");
        }
        customer.setActive(true);
        return customerDao.create(customer);
    }

    public Customer getCustomer(long customerId) {
        return customerDao.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found: " + customerId));
    }

    public List<Customer> listActiveCustomers() {
        return customerDao.findAllActive();
    }

    public void updateCustomer(Customer customer) {
        getCustomer(customer.getCustomerId());
        customerDao.update(customer);
    }
}
