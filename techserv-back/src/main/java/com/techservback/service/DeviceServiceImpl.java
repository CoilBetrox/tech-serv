package com.techservback.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.techservback.repository.IDeviceRepository;
import com.techservback.repository.model.Device;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DeviceServiceImpl implements IDeviceService {

    final private IDeviceRepository deviceRepository;

    public DeviceServiceImpl(IDeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    @Transactional
    public Device createDevice(Device device) {
        log.info("Creating device: {} - {}", device.getBrand(), device.getModel());
        return deviceRepository.save(device);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Device> getDeviceById(Long id) {
        return deviceRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Device> getAllDevices() {
        return (List<Device>) deviceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Device> getDevicesByCustomerId(Long customerId) {
        log.info("Getting devices for customer: {}", customerId);
        return deviceRepository.findByCustomerId(customerId);
    }

    @Override
    @Transactional
    public Device updateDevice(Long id, Device device) {
        Device existingDevice = deviceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Device not found"));
        
        existingDevice.setType(device.getType());
        existingDevice.setBrand(device.getBrand());
        existingDevice.setModel(device.getModel());
        existingDevice.setSerialNumber(device.getSerialNumber());
        
        return deviceRepository.save(existingDevice);
    }

    @Override
    @Transactional
    public void deleteDevice(Long id) {
        deviceRepository.deleteById(id);
    }
}
