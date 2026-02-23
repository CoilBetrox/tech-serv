package com.techservback.service;

import java.util.List;
import java.util.Optional;

import com.techservback.repository.model.User;

public interface IUserService {

    List<User> findAll();
    Optional<User> findById(Long id);
    User findByEmail(String email);

}
