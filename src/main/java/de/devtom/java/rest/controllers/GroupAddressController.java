package de.devtom.java.rest.controllers;

import static de.devtom.java.config.KnxDbApplicationConfiguration.BASE_PATH;

import javax.persistence.EntityNotFoundException;

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
import de.devtom.java.services.DeviceService;
import de.devtom.java.services.GroupAddressService;
import de.devtom.java.services.ProjectService;
import de.devtom.java.services.RoomService;
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
	private DeviceService deviceService;
	@Autowired
	private RoomService roomService;
	@Autowired
	private ProjectService projectService;
	
	@ApiOperation(value = "create group address instance")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "New group address instance created", response = GroupAddress.class),
			@ApiResponse(code = 404, message = "Entity not found")
	})
	@PostMapping(value = "/projects/{projectid}/rooms/{roomid}/devices/{deviceid}/group-addresses", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createGroupAddress(@PathVariable Long projectid, @PathVariable Long roomid, @PathVariable Long deviceid, @RequestBody GroupAddress groupAddress) {
		ResponseEntity<?> response = null;
		try {
			projectService.findById(projectid);
			roomService.findById(roomid);
			Device device = deviceService.findById(deviceid);
			groupAddress.setDevice(device);
			response = new ResponseEntity<GroupAddress>(groupAddressService.createGroupAddress(groupAddress), HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Invalid input: {}", e.getMessage());
			response = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (EntityNotFoundException e) {
			LOGGER.error(e.getMessage());
			response= new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
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
	public ResponseEntity<?> getGroupAddress(@PathVariable Long projectid, @PathVariable Long roomid, @PathVariable Long deviceid, @PathVariable Long groupaddressid) {
		ResponseEntity<?> response = null;
		try {
			projectService.findById(projectid);
			roomService.findById(roomid);
			deviceService.findById(deviceid);
			response = new ResponseEntity<GroupAddress>(groupAddressService.findById(groupaddressid), HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Invalid input: {}", e.getMessage());
			response = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (EntityNotFoundException e) {
			LOGGER.error(e.getMessage());
			response= new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
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
	public ResponseEntity<?> replaceExistingGroupAddress(@PathVariable Long projectid, @PathVariable Long roomid, @PathVariable Long deviceid, @PathVariable Long groupaddressid, @RequestBody GroupAddress groupAddress) {
		ResponseEntity<?> response = null;
		
		try {
			projectService.findById(projectid);
			roomService.findById(roomid);
			Device device = deviceService.findById(deviceid);
			groupAddress.setDevice(device);
			response = new ResponseEntity<GroupAddress>(groupAddressService.updateGroupAddress(groupAddress), HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Invalid input: {}", e.getMessage());
			response = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (EntityNotFoundException e) {
			LOGGER.error(e.getMessage());
			response= new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
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
	public ResponseEntity<?> deleteGroupAddress(@PathVariable Long projectid, @PathVariable Long roomid, @PathVariable Long deviceid, @PathVariable Long groupaddressid) {
		ResponseEntity<?> response = null;
		try {
			projectService.findById(projectid);
			roomService.findById(roomid);
			deviceService.findById(deviceid);
			GroupAddress existingGrouAddress = groupAddressService.findById(groupaddressid);
			groupAddressService.delete(existingGrouAddress);
			response = new ResponseEntity<GroupAddress>(existingGrouAddress, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Invalid input: {}", e.getMessage());
			response = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (EntityNotFoundException e) {
			LOGGER.error(e.getMessage());
			response= new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
}
