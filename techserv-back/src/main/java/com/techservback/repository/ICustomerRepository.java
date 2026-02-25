package com.techservback.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.techservback.repository.model.Customer;

public interface ICustomerRepository extends CrudRepository<Customer, Long> {
	Optional<Customer> findFirstByEmail(String email);
	List<Customer> findByUserId(Long userId);

}
