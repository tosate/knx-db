package de.devtom.java.rest.controllers;

import java.rmi.activation.UnknownObjectException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.devtom.java.entities.Project;
import de.devtom.java.entities.Room;
import de.devtom.java.services.ProjectService;
import de.devtom.java.services.RoomService;

import static de.devtom.java.config.KnxDbApplicationConfiguration.BASE_PATH;

@RestController
@RequestMapping(BASE_PATH)
public class RoomController {
	private static final Logger LOGGER = LoggerFactory.getLogger(RoomController.class);
	
	@Autowired
	private RoomService roomService;
	@Autowired
	private ProjectService projectService;
	
	@PostMapping(value = "/project/{projectid}/room", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Room> createRoom(@PathVariable Long projectid, @RequestBody Room room) {
		ResponseEntity<Room> response = null;
		try {
			validateRoomFields(room);
			Project project = getProject(projectid);
			for(Room existinRoom : project.getRooms()) {
				if(existinRoom.getName().equals(room.getName())) {
					throw new IllegalArgumentException(String.format("Room with name [%s] already esxists.", room.getName()));
				}
			}
			response = new ResponseEntity<Room>(roomService.save(project, room), HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Invalid input: {}", e.getMessage());
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return response;
	}
	
	@GetMapping(value = "/project/{projectid}/room/{roomid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Room> getRoom(@PathVariable Long projectid, @PathVariable Long roomid) {
		ResponseEntity<Room> response = null;
		try {
			Project project = getProject(projectid);
			response = new ResponseEntity<>(retrieveExistingRoom(project, roomid), HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Invalid input: {}", e.getMessage());
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (UnknownObjectException e) {
			LOGGER.error(e.getMessage());
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	private Room retrieveExistingRoom(Project project, Long roomid) throws UnknownObjectException {
		Optional<Room> result = Optional.empty();
		for(Room room : project.getRooms()) {
			if(room.getRoomid() != null && room.getRoomid() == roomid) {
				result = Optional.of(room);
				break;
			}
		}
		if(result.isPresent()) {
			return result.get();
		} else {
			throw new UnknownObjectException(String.format("No room with roomid [%d]", roomid));
		}
	}

	@PutMapping(value = "/project/{projectid}/room/{roomid}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Room> updateRoom(@PathVariable Long projectid, @PathVariable Long roomid, @RequestBody Room room) {
		ResponseEntity<Room> response = null;
		try {
			validateRoomFields(room);
			Project project = getProject(projectid);
			Room existingRoom = retrieveExistingRoom(project, roomid);
			room.setRoomid(existingRoom.getRoomid());
			room.setProject(existingRoom.getProject());
			response = new ResponseEntity<>(roomService.update(room), HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Invalid input: {}", e.getMessage());
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (UnknownObjectException e) {
			LOGGER.error(e.getMessage());
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	@DeleteMapping(value = "/project/{projectid}/room/{roomid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Room> deleteRoom(@PathVariable Long projectid, @PathVariable Long roomid) {
		ResponseEntity<Room> response = null;
		try {
			Project project = getProject(projectid);
			Room existingRoom = retrieveExistingRoom(project, roomid);
			roomService.delete(existingRoom);
			response = new ResponseEntity<>(existingRoom, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Invalid input: {}", e.getMessage());
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (UnknownObjectException e) {
			LOGGER.error(e.getMessage());
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	private Project getProject(Long projectid) {
		Optional<Project> project = projectService.findById(projectid);
		if(project.isPresent()) {
			return project.get();
		} else {
			throw new IllegalArgumentException(String.format("No project with projectid [%d]", projectid));
		}
	}

	private void validateRoomFields(Room room) {
		if(StringUtils.isEmpty(room.getName())) {
			throw new IllegalArgumentException("Room name cannot be empty");
		}
		if(StringUtils.isEmpty(room.getLabel())) {
			throw new IllegalArgumentException("Room label cannot be empty");
		}
		
	}
}
