package com.techservback.dto;

import com.techservback.repository.model.TechnicalService;
import com.techservback.repository.model.Device;
import com.techservback.repository.model.Customer;

public class TechnicalServiceMapper {

    public static TechnicalServiceDTO toDto(TechnicalService s) {
        if (s == null) return null;
        TechnicalServiceDTO dto = new TechnicalServiceDTO();
        dto.setId(s.getId());
        dto.setTicketCode(s.getTicketCode());
        dto.setDescription(s.getDescription());
        dto.setStatus(s.getStatus());
        dto.setEntryDate(s.getEntryDate());
        dto.setEstimatedCost(s.getEstimatedCost());
        dto.setCreatedAt(s.getCreatedAt());
        dto.setReceivedAt(s.getReceivedAt());
        dto.setInProgressAt(s.getInProgressAt());
        dto.setFinalizedAt(s.getFinalizedAt());
        dto.setDeliveredAt(s.getDeliveredAt());
        dto.setInProgressMessage(s.getInProgressMessage());
        dto.setFinalizedMessage(s.getFinalizedMessage());
        dto.setDeliveredMessage(s.getDeliveredMessage());

        Device device = s.getDevice();
        if (device != null) {
            DeviceDTO dd = new DeviceDTO();
            dd.setId(device.getId());
            dd.setType(device.getType());
            dd.setBrand(device.getBrand());
            dd.setModel(device.getModel());
            dd.setSerialNumber(device.getSerialNumber());

            Customer customer = device.getCustomer();
            if (customer != null) {
                CustomerDTO cd = new CustomerDTO();
                cd.setId(customer.getId());
                cd.setFirstName(customer.getFirstName());
                cd.setLastName(customer.getLastName());
                cd.setEmail(customer.getEmail());
                cd.setPhone(customer.getPhone());
                dd.setCustomer(cd);
            }

            dto.setDevice(dd);
        }

        return dto;
    }

    public static PublicTechnicalServiceDTO toPublicDto(TechnicalService s) {
        if (s == null) return null;

        PublicTechnicalServiceDTO dto = new PublicTechnicalServiceDTO();
        dto.setId(s.getId());
        dto.setTicketCode(s.getTicketCode());
        dto.setDescription(s.getDescription());
        dto.setStatus(s.getStatus());
        dto.setEntryDate(s.getEntryDate());
        dto.setEstimatedCost(s.getEstimatedCost());
        dto.setCreatedAt(s.getCreatedAt());
        dto.setReceivedAt(s.getReceivedAt());
        dto.setInProgressAt(s.getInProgressAt());
        dto.setFinalizedAt(s.getFinalizedAt());
        dto.setDeliveredAt(s.getDeliveredAt());
        dto.setInProgressMessage(s.getInProgressMessage());
        dto.setFinalizedMessage(s.getFinalizedMessage());
        dto.setDeliveredMessage(s.getDeliveredMessage());

        Device device = s.getDevice();
        if (device != null) {
            PublicDeviceDTO dd = new PublicDeviceDTO();
            dd.setId(device.getId());
            dd.setType(device.getType());
            dd.setBrand(device.getBrand());
            dd.setModel(device.getModel());
            dd.setSerialNumber(device.getSerialNumber());

            Customer customer = device.getCustomer();
            if (customer != null) {
                PublicCustomerDTO cd = new PublicCustomerDTO();
                cd.setId(customer.getId());
                cd.setFirstName(customer.getFirstName());
                cd.setLastName(customer.getLastName());
                dd.setCustomer(cd);
            }

            dto.setDevice(dd);
        }

        return dto;
    }

}
