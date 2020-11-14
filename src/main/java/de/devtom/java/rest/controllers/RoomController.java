package de.devtom.java.rest.controllers;

import static de.devtom.java.config.KnxDbApplicationConfiguration.BASE_PATH;

import java.util.List;

import javax.persistence.EntityNotFoundException;

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
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(BASE_PATH)
public class RoomController {
	private static final Logger LOGGER = LoggerFactory.getLogger(RoomController.class);
	
	@Autowired
	private RoomService roomService;
	@Autowired
	private ProjectService projectService;
	
	@ApiOperation(value = "create a room instance")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "New room instance created", response = Room.class),
			@ApiResponse(code = 400, message = "Empty mandatory fields, room already exists or project not found"),
			@ApiResponse(code = 404, message = "Entity not found")
	})
	@PostMapping(value = "/projects/{projectid}/rooms", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createRoom(@PathVariable Long projectid, @RequestBody Room room) {
		ResponseEntity<?> response = null;
		try {
			validateRoomFields(room);
			Project project = projectService.findById(projectid);
			room.setProject(project);
			response = new ResponseEntity<Room>(roomService.createRoom(room), HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Invalid input: {}", e.getMessage());
			response = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (EntityNotFoundException e) {
			LOGGER.error(e.getMessage());
			response= new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	@ApiOperation(value = "get room by roomid")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Room instance found", response = Room.class),
			@ApiResponse(code = 400, message = "Parent project not found"),
			@ApiResponse(code = 404, message = "Room instance not found")
	})
	@GetMapping(value = "/projects/{projectid}/rooms/{roomid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getRoom(@PathVariable Long projectid, @PathVariable Long roomid) {
		ResponseEntity<?> response = null;
		try {
			projectService.findById(projectid);
			response = new ResponseEntity<Room>(roomService.findById(roomid), HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Invalid input: {}", e.getMessage());
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (EntityNotFoundException e) {
			LOGGER.error(e.getMessage());
			response= new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	@ApiOperation(value = "get room list")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message= "Room list for project", response = List.class),
			@ApiResponse(code = 404, message = "Project not found")
	})
	@GetMapping(value = "/projects/{projectid}/rooms", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getRoomList(@PathVariable Long projectid) {
		ResponseEntity<?> response = null;
		try {
			projectService.findById(projectid);
			response = new ResponseEntity<List<Room>>(roomService.findByProjectId(projectid), HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			LOGGER.error(e.getMessage());
			response= new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
		return response;
	}

	@ApiOperation(value = "Replace existing room instace")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Room instance replaced", response = Room.class),
			@ApiResponse(code = 400, message = "Parent project not found"),
			@ApiResponse(code = 404, message = "Room instance to replace not found")
	})
	@PutMapping(value = "/projects/{projectid}/rooms/{roomid}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> replaceExistingRoom(@PathVariable Long projectid, @PathVariable Long roomid, @RequestBody Room room) {
		ResponseEntity<?> response = null;
		try {
			validateRoomFields(room);
			Project project = projectService.findById(projectid);
			room.setProject(project);
			response = new ResponseEntity<Room>(roomService.updateRoom(room), HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Invalid input: {}", e.getMessage());
			response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		} catch (EntityNotFoundException e) {
			LOGGER.error(e.getMessage());
			response= new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	@ApiOperation(value = "Delete room instace")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Room instance deleted", response = Room.class),
			@ApiResponse(code = 400, message = "Parent project not found"),
			@ApiResponse(code = 404, message = "Room to delete not found")
	})
	@DeleteMapping(value = "/projects/{projectid}/rooms/{roomid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteRoom(@PathVariable Long projectid, @PathVariable Long roomid) {
		ResponseEntity<?> response = null;
		try {
			projectService.findById(projectid);
			Room existingRoom = roomService.findById(roomid);
			roomService.delete(existingRoom);
			response = new ResponseEntity<>(existingRoom, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Invalid input: {}", e.getMessage());
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (EntityNotFoundException e) {
			LOGGER.error(e.getMessage());
			response= new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
		return response;
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
