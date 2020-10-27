package de.devtom.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import de.devtom.java.entities.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

}
