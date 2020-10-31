package de.devtom.java.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.devtom.java.entities.Project;
import de.devtom.java.entities.Room;
import de.devtom.java.repositories.RoomRepository;

@Service
public class RoomService {
	@Autowired
	private RoomRepository roomRepository;
	
	public List<Room> list() {
		return roomRepository.findAll();
	}
	
	public Room save(Project parentProject, Room room) {
		parentProject.addRoom(room);
		return roomRepository.save(room);
	}
	
	public Room replace(Room room) {
		return roomRepository.save(room);
	}
	
	public Optional<Room> findById(Long roomid) {
		return roomRepository.findById(roomid);
	}
	
	public void delete(Room room) {
		// remove item from the collection maintained by its parent
		room.getProject().deleteRoom(room);
		// explicitly delete the record in the database
		roomRepository.deleteById(room.getRoomid());
	}
}
