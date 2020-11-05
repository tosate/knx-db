package de.devtom.java.rest.controllers;

import static de.devtom.java.config.KnxDbApplicationConfiguration.BASE_PATH;

import java.rmi.activation.UnknownObjectException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.devtom.java.entities.Device;
import de.devtom.java.entities.GroupAddress;
import de.devtom.java.entities.Project;
import de.devtom.java.entities.Room;
import de.devtom.java.services.GroupAddressService;
import de.devtom.java.services.ProjectService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(BASE_PATH)
public class GroupAddressController {
	private static final Logger LOGGER = LoggerFactory.getLogger(GroupAddressController.class);
	
	@Autowired
	private GroupAddressService groupAddressService;
	@Autowired
	private ProjectService projectService;
	
	@ApiOperation(value = "create group address instance")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "New group address instance created", response = GroupAddress.class)
	})
	@PostMapping(value = "/projects/{projectid}/rooms/{roomid}/devices/{deviceid}/group-addresses", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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
	
	@ApiOperation(value = "get group address by groupaddressid")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Group address instance found", response = GroupAddress.class),
			@ApiResponse(code = 400, message = "Parent element not found"),
			@ApiResponse(code = 404, message = "Group address not found")
	})
	@GetMapping(value = "/projects/{projectid}/rooms/{roomid}/devices/{deviceid}/group-addresses/{groupaddressid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GroupAddress> getGroupAddress(@PathVariable Long projectid, @PathVariable Long roomid, @PathVariable Long deviceid, @PathVariable Long groupaddressid) {
		ResponseEntity<GroupAddress> response = null;
		try {
			Project project = getProject(projectid);
			Room room = getRoom(project, roomid);
			Device device = getDevice(room, deviceid);
			response = new ResponseEntity<>(retrieveExistingGroupAddress(device, groupaddressid), HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Invalid input: {}", e.getMessage());
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (UnknownObjectException e) {
			LOGGER.error(e.getMessage());
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	@ApiOperation(value = "Replace existing group address instance")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Group address replaced", response = GroupAddress.class),
			@ApiResponse(code = 400, message = "Parent element not found"),
			@ApiResponse(code = 404, message = "Group address instance to replace not found")
	})
	@PutMapping(value = "/projects/{projectid}/rooms/{roomid}/devices/{deviceid}/group-addresses/{groupaddressid}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GroupAddress> replaceExistingGroupAddress(@PathVariable Long projectid, @PathVariable Long roomid, @PathVariable Long deviceid, @PathVariable Long groupaddressid, @RequestBody GroupAddress groupAddress) {
		ResponseEntity<GroupAddress> response = null;
		
		try {
			Project project = getProject(projectid);
			Room room = getRoom(project, roomid);
			Device device = getDevice(room, deviceid);
			GroupAddress existingGroupAddress = retrieveExistingGroupAddress(device, groupaddressid);
			groupAddress.setGroupAddressId(existingGroupAddress.getGroupAddressId());
			response = new ResponseEntity<GroupAddress>(groupAddressService.update(groupAddress), HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Invalid input: {}", e.getMessage());
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (UnknownObjectException e) {
			LOGGER.error(e.getMessage());
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	@ApiOperation(value = "Delete group address instance")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Group address instance deleted", response = GroupAddress.class),
			@ApiResponse(code = 400, message = "Parent element not found"),
			@ApiResponse(code = 404, message = "Group address to delete not found")
	})
	@DeleteMapping(value = "/projects/{projectid}/rooms/{roomid}/devices/{deviceid}/group-addresses/{groupaddressid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GroupAddress> deleteGroupAddress(@PathVariable Long projectid, @PathVariable Long roomid, @PathVariable Long deviceid, @PathVariable Long groupaddressid) {
		ResponseEntity<GroupAddress> response = null;
		try {
			Project project = getProject(projectid);
			Room room = getRoom(project, roomid);
			Device device = getDevice(room, deviceid);
			GroupAddress existingGrouAddress = retrieveExistingGroupAddress(device, groupaddressid);
			groupAddressService.delete(device, existingGrouAddress);
			response = new ResponseEntity<>(existingGrouAddress, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Invalid input: {}", e.getMessage());
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (UnknownObjectException e) {
			LOGGER.error(e.getMessage());
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	private GroupAddress retrieveExistingGroupAddress(Device device, Long groupaddressid) throws UnknownObjectException {
		Optional<GroupAddress> result = Optional.empty();
		for(GroupAddress address : device.getGroupAddresses()) {
			if(address.getGroupAddressId().equals(groupaddressid)) {
				result = Optional.of(address);
			}
		}
		if(result.isPresent()) {
			return result.get();
		} else {
			throw new UnknownObjectException(String.format("No group address with ID [%d]", groupaddressid));
		}
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
