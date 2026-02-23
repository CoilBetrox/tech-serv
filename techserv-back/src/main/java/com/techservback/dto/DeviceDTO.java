package com.techservback.dto;

import lombok.Data;

@Data
public class DeviceDTO {
    private Long id;
    private String type;
    private String brand;
    private String model;
    private String serialNumber;
    private CustomerDTO customer;
}
