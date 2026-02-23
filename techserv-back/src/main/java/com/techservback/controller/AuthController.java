package com.techservback.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techservback.service.IAuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    final private IAuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            logger.info("Login attempt for email: {}", request.getEmail());
            String token = authService.login(request.getEmail(), request.getPassword());
            logger.info("Login successful for email: {}", request.getEmail());
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (Exception e) {
            logger.error("Login failed for email: {} - Error: {}", request.getEmail(), e.getMessage(), e);
            return ResponseEntity.status(401).body(new ErrorResponse("Invalid credentials"));
        }
    }

    // DTO Classes
    static class LoginRequest {
        private String email;
        private String password;

        public LoginRequest() {}
        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    static class LoginResponse {
        private String token;

        public LoginResponse(String token) {
            this.token = token;
        }

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }

    static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            com.techservback.repository.model.User created = authService.registerUser(request.getEmail(), request.getPassword(), request.getRole());
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new ErrorResponse(e.getMessage()));
        }
    }

    static class RegisterRequest {
        private String email;
        private String password;
        private String role = "CUSTOMER";

        public RegisterRequest() {}
        public RegisterRequest(String email, String password, String role) {
            this.email = email;
            this.password = password;
            this.role = role;
        }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }
}
