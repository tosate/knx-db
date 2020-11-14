package de.devtom.java.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.devtom.java.entities.Device;

public interface DeviceRepository extends JpaRepository<Device, Long> {
	List<Device> findByRoomRoomid(Long roomId);
}
