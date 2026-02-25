package com.techservback.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.techservback.repository.ICustomerRepository;
import com.techservback.repository.model.Customer;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerServiceImpl implements ICustomerService {

    final private ICustomerRepository customerRepository;

    public CustomerServiceImpl(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public Customer createCustomer(Customer customer) {
        log.info("Creating customer: {} {}", customer.getFirstName(), customer.getLastName());
        return customerRepository.save(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findFirstByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getCustomersByUserId(Long userId) {
        log.info("Fetching customers for user ID: {}", userId);
        return customerRepository.findByUserId(userId);
    }
}
