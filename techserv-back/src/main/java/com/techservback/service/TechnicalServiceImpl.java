package com.techservback.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techservback.repository.ITechnicalServiceRepository;
import com.techservback.repository.model.TechnicalService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TechnicalServiceImpl implements ITechnicalServiceService {

    final private ITechnicalServiceRepository technicalServiceRepository;

    public TechnicalServiceImpl(ITechnicalServiceRepository technicalServiceRepository) {
        this.technicalServiceRepository = technicalServiceRepository;
    }

    @Override
    @Transactional
    public TechnicalService createService(TechnicalService service) {
        // Validar que admin esté asignado
        if (service.getAdmin() == null) {
            throw new IllegalArgumentException("Admin debe ser especificado al crear la orden");
        }
        
        // Auto-generate ticket code if not provided
        if (service.getTicketCode() == null || service.getTicketCode().isEmpty()) {
            service.setTicketCode("TKT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        
        // Set initial status if not provided
        if (service.getStatus() == null || service.getStatus().isEmpty()) {
            service.setStatus("RECIBIDO");
        } else {
            service.setStatus(normalizeStatus(service.getStatus()));
        }
        
        // Set entry date if not provided
        if (service.getEntryDate() == null) {
            service.setEntryDate(LocalDate.now());
        }

        applyStatusDates(service, service.getStatus());
        
        log.info("Creating technical service - Ticket: {} - Admin: {} - Device: {}", 
            service.getTicketCode(), 
            service.getAdmin().getEmail(), 
            service.getDevice().getId());
        
        return technicalServiceRepository.save(service);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TechnicalService> getServiceById(Long id) {
        return technicalServiceRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TechnicalService> getMyServices(Long customerId) {
        log.info("Getting services for customer: {}", customerId);
        return technicalServiceRepository.findByDeviceCustomerId(customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TechnicalService> searchServices(String query) {
        log.info("Searching services with query: {}", query);
        // Search in both ticket code and description
        List<TechnicalService> byTicket = technicalServiceRepository.findByTicketCodeContaining(query);
        List<TechnicalService> byDescription = technicalServiceRepository.findByDescriptionContaining(query);
        
        // Combine results and remove duplicates
        byTicket.addAll(byDescription);
        return byTicket.stream().distinct().toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TechnicalService> getByTicketCode(String ticketCode) {
        log.info("Getting service by ticketCode: {}", ticketCode);
        return technicalServiceRepository.findByTicketCode(ticketCode);
    }

    @Override
    @Transactional
    public TechnicalService updateServiceStatus(Long id, String status, String message) {
        TechnicalService service = technicalServiceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Service not found"));
        String normalizedStatus = normalizeStatus(status);
        if (normalizedStatus.isEmpty()) {
            throw new IllegalArgumentException("Status must not be empty");
        }

        log.info("Updating service {} status to: {} with message: {}", id, normalizedStatus, message);
        service.setStatus(normalizedStatus);
        applyStatusDates(service, normalizedStatus);
        applyStatusMessage(service, normalizedStatus, message);
        return technicalServiceRepository.save(service);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TechnicalService> getAllServices() {
        return (List<TechnicalService>) technicalServiceRepository.findAll();
    }

    // NUEVO: Obtener órdenes creadas por un ADMIN específico
    @Override
    @Transactional(readOnly = true)
    public List<TechnicalService> getServicesByAdmin(Long adminId) {
        log.info("Getting services created by admin: {}", adminId);
        return technicalServiceRepository.findByAdminIdOrderByCreatedAtDesc(adminId);
    }

    private String normalizeStatus(String status) {
        return status == null ? "" : status.trim().toUpperCase();
    }

    private void applyStatusDates(TechnicalService service, String status) {
        LocalDateTime now = LocalDateTime.now();

        switch (normalizeStatus(status)) {
            case "RECIBIDO" -> {
                if (service.getReceivedAt() == null) service.setReceivedAt(now);
            }
            case "EN_DIAGNOSTICO", "EN_REPARACION" -> {
                if (service.getReceivedAt() == null) service.setReceivedAt(now);
                if (service.getInProgressAt() == null) service.setInProgressAt(now);
            }
            case "LISTO_PARA_ENTREGA" -> {
                if (service.getReceivedAt() == null) service.setReceivedAt(now);
                if (service.getInProgressAt() == null) service.setInProgressAt(now);
                if (service.getFinalizedAt() == null) service.setFinalizedAt(now);
            }
            case "ENTREGADO" -> {
                if (service.getReceivedAt() == null) service.setReceivedAt(now);
                if (service.getInProgressAt() == null) service.setInProgressAt(now);
                if (service.getFinalizedAt() == null) service.setFinalizedAt(now);
                if (service.getDeliveredAt() == null) service.setDeliveredAt(now);
            }
            default -> {
                // no-op for unknown statuses
            }
        }
    }

    private void applyStatusMessage(TechnicalService service, String status, String message) {
        if (message == null || message.trim().isEmpty()) {
            return; // No message to apply
        }

        switch (normalizeStatus(status)) {
            case "EN_DIAGNOSTICO", "EN_REPARACION" -> {
                service.setInProgressMessage(message);
                log.info("Applied message to inProgressMessage");
            }
            case "LISTO_PARA_ENTREGA" -> {
                service.setFinalizedMessage(message);
                log.info("Applied message to finalizedMessage");
            }
            case "ENTREGADO" -> {
                service.setDeliveredMessage(message);
                log.info("Applied message to deliveredMessage");
            }
            default -> {
                // Para RECIBIDO u otros estados, no se guarda mensaje (usa description)
                log.debug("No specific message field for status: {}", status);
            }
        }
    }

}
