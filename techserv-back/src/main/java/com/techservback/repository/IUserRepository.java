package com.techservback.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.techservback.repository.model.User;

public interface IUserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

