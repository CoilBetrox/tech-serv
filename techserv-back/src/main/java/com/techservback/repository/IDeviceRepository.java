package com.techservback.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

import com.techservback.repository.model.Device;

public interface IDeviceRepository extends CrudRepository<Device, Long> {
    List<Device> findByCustomerId(Long customerId);
}
