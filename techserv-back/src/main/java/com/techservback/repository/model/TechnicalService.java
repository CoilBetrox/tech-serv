package com.techservback.repository.model;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.PrePersist;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Data;

@Entity
@Table(name = "technical_services")
@Data
public class TechnicalService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ticketCode; // AUTO
    private String description;
    private String status; // RECIBIDO, EN_DIAGNOSTICO, EN_REPARACION, LISTO_PARA_ENTREGA, ENTREGADO
    private LocalDate entryDate;
    private Double estimatedCost;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

    // NUEVO: Vinculación al ADMIN que creó la orden
    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    @JsonIgnoreProperties({"email","password","role","createdServices"})
    private User admin;

    // NUEVO: Timestamp de creación
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Auto-asignar timestamp al persistir
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}
