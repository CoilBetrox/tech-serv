package com.techservback.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

import com.techservback.repository.model.TechnicalService;

public interface ITechnicalServiceRepository extends CrudRepository<TechnicalService, Long> {
    List<TechnicalService> findByDeviceCustomerId(Long customerId);
    List<TechnicalService> findByTicketCodeContaining(String ticketCode);
    java.util.Optional<TechnicalService> findByTicketCode(String ticketCode);
    List<TechnicalService> findByDescriptionContaining(String description);
    
    // NUEVO: Buscar órdenes creadas por un ADMIN específico
    List<TechnicalService> findByAdminId(Long adminId);
    
    // NUEVO: Buscar órdenes creadas por ADMIN, ordenadas por fecha descendente
    List<TechnicalService> findByAdminIdOrderByCreatedAtDesc(Long adminId);
}
