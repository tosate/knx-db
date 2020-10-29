package de.devtom.java.rest.controllers;

import static de.devtom.java.config.KnxDbApplicationConfiguration.BASE_PATH;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.devtom.java.entities.Device;
import de.devtom.java.entities.GroupAddress;
import de.devtom.java.entities.Project;
import de.devtom.java.entities.Room;
import de.devtom.java.services.GroupAddressService;
import de.devtom.java.services.ProjectService;

@RestController
@RequestMapping(BASE_PATH)
public class GroupAddressController {
	private static final Logger LOGGER = LoggerFactory.getLogger(GroupAddressController.class);
	
	@Autowired
	private GroupAddressService groupAddressService;
	@Autowired
	private ProjectService projectService;
	
	@PostMapping(value = "/project/{projectid}/room/{roomid}/device/{deviceid}/group-address", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GroupAddress> createGroupAddress(@PathVariable Long projectid, @PathVariable Long roomid, @PathVariable Long deviceid, @RequestBody GroupAddress groupAddress) {
		ResponseEntity<GroupAddress> response = null;
		try {
			Project project = getProject(projectid);
			Room room = getRoom(project, roomid);
			Device device = getDevice(room, deviceid);
			for(GroupAddress existingAddress : device.getGroupAddresses()) {
				if(existingAddress.equals(groupAddress)) {
					throw new IllegalArgumentException(String.format("Group address %d/%d/%d already exists.", groupAddress.getMainGroup(), groupAddress.getMiddleGroup(), groupAddress.getSubGroup()));
				}
			}
			response = new ResponseEntity<>(groupAddressService.save(device, groupAddress), HttpStatus.CREATED);
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
	
	private Room getRoom(Project project, Long roomid) {
		for(Room room : project.getRooms()) {
			if(room.getRoomid() != null && room.getRoomid().equals(roomid)) {
				return room;
			}
		}
		
		throw new IllegalArgumentException(String.format("Project [%s] contains no room with room ID [%d]", project.getName(), roomid));
	}
	
	private Device getDevice(Room room, Long deviceid) {
		for(Device device : room.getDevices()) {
			if(device.getDeviceid() != null && device.getDeviceid().equals(deviceid)) {
				return device;
			}
		}
		
		throw new IllegalArgumentException(String.format("Room [%s] contains no device with ID [%d]", room.getName(), deviceid));
	}
}
