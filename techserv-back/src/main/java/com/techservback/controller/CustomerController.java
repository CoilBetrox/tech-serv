package com.techservback.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techservback.repository.model.Customer;
import com.techservback.repository.model.User;
import com.techservback.service.ICustomerService;
import com.techservback.service.IUserService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/customers")
@Slf4j
public class CustomerController {

    final private ICustomerService customerService;
    final private IUserService userService;

    public CustomerController(ICustomerService customerService, IUserService userService) {
        this.customerService = customerService;
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createCustomer(@RequestBody Customer customer) {
        Customer created = customerService.createCustomer(customer);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomer(@PathVariable Long id) {
        return customerService.getCustomerById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/by-email")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCustomerByEmail(@RequestParam String email) {
        return customerService.findByEmail(email)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // NUEVO ENDPOINT: Obtener todos los customers asociados al ADMIN logueado
    @GetMapping("/my-customers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getMyCustomers(Authentication authentication) {
        try {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            String email = jwt.getClaimAsString("email");
            
            User admin = userService.findByEmail(email);
            if (admin == null) {
                return ResponseEntity.status(404).body("Admin user not found");
            }
            
            log.info("ADMIN {} requesting his associated customers", email);
            List<Customer> customers = customerService.getCustomersByUserId(admin.getId());
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            log.error("Error getting customers: {}", e.getMessage());
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    // NUEVO ENDPOINT: Obtener customers de un ADMIN específico (por ID)
    @GetMapping("/admin/{adminId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCustomersByAdmin(@PathVariable Long adminId) {
        log.info("Getting customers associated to admin ID: {}", adminId);
        List<Customer> customers = customerService.getCustomersByUserId(adminId);
        return ResponseEntity.ok(customers);
    }
}
