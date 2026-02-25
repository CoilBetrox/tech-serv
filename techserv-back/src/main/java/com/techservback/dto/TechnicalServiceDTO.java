package com.techservback.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TechnicalServiceDTO {
    private Long id;
    private String ticketCode;
    private String description;
    private String status;
    private LocalDate entryDate;
    private Double estimatedCost;
    private LocalDateTime createdAt;
    private LocalDateTime receivedAt;
    private LocalDateTime inProgressAt;
    private LocalDateTime finalizedAt;
    private LocalDateTime deliveredAt;
    private DeviceDTO device;
    private Long adminId;
}
