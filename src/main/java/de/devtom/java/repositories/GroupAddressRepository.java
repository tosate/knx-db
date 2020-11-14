package de.devtom.java.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.devtom.java.entities.GroupAddress;

public interface GroupAddressRepository extends JpaRepository<GroupAddress, Long> {
	List<GroupAddress> findByDeviceDeviceid(Long deviceId);
}
