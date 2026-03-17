package com.techservback.dto;

import lombok.Data;

@Data
public class PublicDeviceDTO {
    private Long id;
    private String type;
    private String brand;
    private String model;
    private String serialNumber;
    private PublicCustomerDTO customer;
}
