package com.techservback.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techservback.repository.model.TechnicalService;
import com.techservback.repository.model.User;
import com.techservback.repository.model.Device;
import com.techservback.repository.model.Customer;
import com.techservback.service.ITechnicalServiceService;
import com.techservback.service.IUserService;
import com.techservback.service.IDeviceService;
import com.techservback.service.ICustomerService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/services")
@Slf4j
public class TechnicalServiceController {

    final private ITechnicalServiceService technicalServiceService;
    final private IUserService userService;
    final private IDeviceService deviceService;
    final private ICustomerService customerService;

    public TechnicalServiceController(ITechnicalServiceService technicalServiceService, IUserService userService,
            IDeviceService deviceService, ICustomerService customerService) {
        this.technicalServiceService = technicalServiceService;
        this.userService = userService;
        this.deviceService = deviceService;
        this.customerService = customerService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<?> createService(
            @RequestBody TechnicalService service,
            Authentication authentication,
            @RequestHeader(value = "X-Timezone", required = false) String clientTimeZone) {
        try {
            // Extraer email del JWT token
            Jwt jwt = (Jwt) authentication.getPrincipal();
            String email = jwt.getClaimAsString("email");

            // Obtener usuario ADMIN de la BD
            User admin = userService.findByEmail(email);
            if (admin == null) {
                return ResponseEntity.status(404).body("Admin user not found");
            }

            // Preparar device y customer si vienen embebidos
            Device device = service.getDevice();
            if (device == null) {
                return ResponseEntity.badRequest().body("Device must be provided");
            }

            if (device.getId() == null) {
                // Need to create or attach customer first
                Customer customer = device.getCustomer();
                if (customer == null) {
                    return ResponseEntity.badRequest().body("Device.customer must be provided when creating a new device");
                }

                if (customer.getId() == null) {
                    // create new customer and associate to admin user
                    customer.setUser(admin);  // NUEVO: Asociar el admin logueado al customer
                    customer = customerService.createCustomer(customer);
                    log.info("New customer '{}' created and associated to admin '{}'", customer.getEmail(), admin.getEmail());
                } else {
                    // ensure exists
                    var existing = customerService.getCustomerById(customer.getId());
                    if (existing.isEmpty()) {
                        return ResponseEntity.status(404).body("Customer with id " + customer.getId() + " not found");
                    }
                    customer = existing.get();
                }

                device.setCustomer(customer);
                device = deviceService.createDevice(device);
                service.setDevice(device);
            } else {
                // verify device exists
                var existing = deviceService.getDeviceById(device.getId());
                if (existing.isEmpty()) {
                    return ResponseEntity.status(404).body("Device with id " + device.getId() + " not found");
                }
                service.setDevice(existing.get());
            }

            // Asignar ADMIN a la orden
            service.setAdmin(admin);

            log.info("Creating order by admin: {}", email);
            TechnicalService created = technicalServiceService.createService(service, clientTimeZone);
            return ResponseEntity.ok(com.techservback.dto.TechnicalServiceMapper.toDto(created));
        } catch (Exception e) {
            log.error("Error creating service: {}", e.getMessage());
            return ResponseEntity.status(400).body("Error creating service: " + e.getMessage());
        }
    }

    @GetMapping("/my-services")
    public ResponseEntity<?> getMyServices(@RequestParam Long customerId) {
        List<TechnicalService> services = technicalServiceService.getMyServices(customerId);
        var dtos = services.stream().map(com.techservback.dto.TechnicalServiceMapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateServiceStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest request,
            @RequestHeader(value = "X-Timezone", required = false) String clientTimeZone) {
        try {
            TechnicalService updated = technicalServiceService.updateServiceStatus(
                    id,
                    request.getStatus(),
                    request.getMessage(),
                    clientTimeZone);
            return ResponseEntity.ok(com.techservback.dto.TechnicalServiceMapper.toDto(updated));
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchServices(@RequestParam String query) {
        List<TechnicalService> services = technicalServiceService.searchServices(query);
        var dtos = services.stream().map(com.techservback.dto.TechnicalServiceMapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/ticket/{ticketCode}")
    public ResponseEntity<?> getByTicketCode(@PathVariable String ticketCode) {
        return technicalServiceService.getByTicketCode(ticketCode)
                .map(com.techservback.dto.TechnicalServiceMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // NUEVO ENDPOINT: Obtener órdenes creadas por el ADMIN logueado
    @GetMapping("/created-by-me")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getMyCreatedServices(Authentication authentication) {
        try {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            String email = jwt.getClaimAsString("email");
            
            User admin = userService.findByEmail(email);
            if (admin == null) {
                return ResponseEntity.status(404).body("Admin user not found");
            }
            
            log.info("ADMIN {} requesting his created services", email);
            List<TechnicalService> services = technicalServiceService.getServicesByAdmin(admin.getId());
            var dtos = services.stream().map(com.techservback.dto.TechnicalServiceMapper::toDto).toList();
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            log.error("Error getting created services: {}", e.getMessage());
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    // NUEVO ENDPOINT: Obtener órdenes creadas por un ADMIN específico
    @GetMapping("/admin/{adminId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getServicesByAdmin(@PathVariable Long adminId) {
        log.info("Getting services created by admin ID: {}", adminId);
        List<TechnicalService> services = technicalServiceService.getServicesByAdmin(adminId);
        var dtos = services.stream().map(com.techservback.dto.TechnicalServiceMapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    // DTO Class
    static class StatusUpdateRequest {
        private String status;
        private String message;

        public StatusUpdateRequest() {}
        public StatusUpdateRequest(String status, String message) {
            this.status = status;
            this.message = message;
        }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
