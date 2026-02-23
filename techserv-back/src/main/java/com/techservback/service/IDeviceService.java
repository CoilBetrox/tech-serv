package com.techservback.service;

import java.util.List;
import java.util.Optional;
import com.techservback.repository.model.Device;

public interface IDeviceService {
    Device createDevice(Device device);
    Optional<Device> getDeviceById(Long id);
    List<Device> getAllDevices();
    List<Device> getDevicesByCustomerId(Long customerId);
    Device updateDevice(Long id, Device device);
    void deleteDevice(Long id);
}
