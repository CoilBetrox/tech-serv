package com.techservback.repository;

import org.springframework.data.repository.CrudRepository;

import com.techservback.repository.model.Customer;

public interface ICustomerRepository extends CrudRepository<Customer, Long> {

}
