package com.techservback.service;

import java.util.List;
import java.util.Optional;
import com.techservback.repository.model.Customer;

public interface ICustomerService {
    Customer createCustomer(Customer customer);
    Optional<Customer> getCustomerById(Long id);
    Optional<Customer> findByEmail(String email);
    List<Customer> getCustomersByUserId(Long userId);
}
