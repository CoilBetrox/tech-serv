package com.techservback.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techservback.repository.IUserRepository;
import com.techservback.repository.model.User;
import com.techservback.security.JwtTokenProvider;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthServiceImpl implements IAuthService {

    final private IUserRepository userRepository;
    final private PasswordEncoder passwordEncoder;
    final private JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(IUserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    @Transactional(readOnly = true)
    public String login(String email, String password) {
        log.info("Attempting login for email: {}", email);
        
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> {
                log.error("User not found with email: {}", email);
                return new RuntimeException("User not found");
            });
        
        log.info("User found: ID={}, Email={}", user.getId(), user.getEmail());
        log.info("Stored password hash: {}", user.getPassword());
        log.info("Input password: {}", password);
        
        boolean passwordMatches = passwordEncoder.matches(password, user.getPassword());
        log.info("Password matches: {}", passwordMatches);
        
        if (!passwordMatches) {
            log.error("Invalid password for user: {}", email);
            throw new RuntimeException("Invalid password");
        }

        log.info("Login successful, generating token for user: {}", email);
        return jwtTokenProvider.generateToken(user);
    }

    @Override
    @Transactional
    public User registerUser(String email, String password, String role) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        return userRepository.save(user);
    }

}
