package de.devtom.java.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.devtom.java.entities.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
	Optional<Room> findById(Long roomid);
	Optional<Room> findByName(String name);
}
