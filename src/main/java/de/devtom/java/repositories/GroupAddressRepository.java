package de.devtom.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import de.devtom.java.entities.GroupAddress;

public interface GroupAddressRepository extends JpaRepository<GroupAddress, Long> {

}
