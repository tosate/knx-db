package de.devtom.java.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.devtom.java.entities.Room;
import de.devtom.java.repositories.RoomRepository;

@Service
public class RoomService extends AbstractService<Room> {
	@Autowired
	private RoomRepository roomRepository;
	
	public List<Room> list() {
		return roomRepository.findAll();
	}
	
	public Room createRoom(Room room) {
		return roomRepository.save(room);
	}
	
	public Room updateRoom(Room room) {
		return roomRepository.findById(room.getRoomid()).map(r -> {
			r.setName(room.getName());
			r.setLabel(room.getLabel());
			r.setProject(room.getProject());
			r.setFloor(room.getFloor());
			return roomRepository.save(r);
		}).orElseThrow(() -> new EntityNotFoundException(String.format("Room ID [%d] not found", room.getRoomid())));
	}
	
	public Room findById(Long roomid) {
		Optional<Room> room = roomRepository.findById(roomid);
		return handleOptional(room, String.format("Room ID [%d] not found", roomid));
	}
	
	public boolean delete(Room room) {
		return roomRepository.findById(room.getRoomid()).map(r -> {
			roomRepository.deleteById(room.getRoomid());
			return true;
		}).orElseThrow(() -> new EntityNotFoundException(String.format("Room ID [%d] not found", room.getRoomid())));
	}
	
	public long getNumberOfEntities() {
		return roomRepository.count();
	}
	
	public List<Room> findByProjectId(Long projectId) {
		return roomRepository.findByProjectProjectid(projectId);
	}
}
