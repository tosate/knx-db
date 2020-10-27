package de.devtom.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import de.devtom.java.entities.Device;

public interface DeviceRepository extends JpaRepository<Device, Long> {

}
