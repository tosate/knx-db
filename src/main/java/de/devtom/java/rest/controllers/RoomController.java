package de.devtom.java.rest.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.devtom.java.entities.Project;
import de.devtom.java.entities.Room;
import de.devtom.java.services.ProjectService;
import de.devtom.java.services.RoomService;

@RestController
@RequestMapping("/knx-db")
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
			response = new ResponseEntity<Room>(roomService.save(project, room), HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Invalid input: {}", e.getMessage());
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
