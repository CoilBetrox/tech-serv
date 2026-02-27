package com.techservback.service;

import java.util.List;
import java.util.Optional;

import com.techservback.repository.model.TechnicalService;

public interface ITechnicalServiceService {
    TechnicalService createService(TechnicalService service, String clientTimeZone);
    Optional<TechnicalService> getServiceById(Long id);
    List<TechnicalService> getMyServices(Long customerId);
    List<TechnicalService> searchServices(String query);
    Optional<TechnicalService> getByTicketCode(String ticketCode);
    TechnicalService updateServiceStatus(Long id, String status, String message, String clientTimeZone);
    List<TechnicalService> getAllServices();
    
    // NUEVO: Obtener órdenes creadas por un ADMIN
    List<TechnicalService> getServicesByAdmin(Long adminId);
}
