package com.techservback.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techservback.repository.ITechnicalServiceRepository;
import com.techservback.repository.model.TechnicalService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TechnicalServiceImpl implements ITechnicalServiceService {

    private static final String TICKET_PREFIX = "TKT-";
    private static final String TICKET_CHARSET = "ABCDEFGHJKLMNPQRSTUVWXYZ123456789";
    private static final int TICKET_BODY_LENGTH = 8;
    private static final int MAX_TICKET_GENERATION_ATTEMPTS = 25;

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
            service.setTicketCode(generateReadableTicketCode());
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

        validateStatusTransition(service.getStatus(), normalizedStatus);

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

    private String generateReadableTicketCode() {
        for (int attempt = 0; attempt < MAX_TICKET_GENERATION_ATTEMPTS; attempt++) {
            StringBuilder body = new StringBuilder(TICKET_BODY_LENGTH);
            for (int i = 0; i < TICKET_BODY_LENGTH; i++) {
                int index = ThreadLocalRandom.current().nextInt(TICKET_CHARSET.length());
                body.append(TICKET_CHARSET.charAt(index));
            }

            String candidate = TICKET_PREFIX + body;
            if (technicalServiceRepository.findByTicketCode(candidate).isEmpty()) {
                return candidate;
            }
        }

        throw new IllegalStateException("No se pudo generar un código de ticket único");
    }

    private void validateStatusTransition(String currentStatus, String targetStatus) {
        String current = normalizeStatus(currentStatus);
        String target = normalizeStatus(targetStatus);

        if ("ENTREGADO".equals(current)) {
            throw new IllegalStateException("La orden ya fue entregada y no permite más cambios");
        }

        int currentRank = getStatusRank(current);
        int targetRank = getStatusRank(target);

        if (targetRank < currentRank) {
            throw new IllegalStateException("No se puede regresar a un estado anterior");
        }
    }

    private int getStatusRank(String status) {
        return switch (normalizeStatus(status)) {
            case "RECIBIDO" -> 0;
            case "EN_DIAGNOSTICO", "EN_REPARACION" -> 1;
            case "LISTO_PARA_ENTREGA" -> 2;
            case "ENTREGADO" -> 3;
            default -> throw new IllegalArgumentException("Estado no soportado: " + status);
        };
    }

}
