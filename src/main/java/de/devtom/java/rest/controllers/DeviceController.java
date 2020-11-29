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

import de.devtom.java.entities.Device;
import de.devtom.java.entities.Room;
import de.devtom.java.services.DeviceService;
import de.devtom.java.services.ProjectService;
import de.devtom.java.services.RoomService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(BASE_PATH)
public class DeviceController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DeviceController.class);
	
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private RoomService roomService;
	@Autowired
	private ProjectService projectService;
	
	@ApiOperation(value = "create device instance")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "New device instance created", response = Device.class),
			@ApiResponse(code = 400, message = "Empty mandatory fields, device alredy exists or parent elements not found"),
			@ApiResponse(code = 404, message = "Entity not found")
	})
	@PostMapping(value = "/projects/{projectid}/rooms/{roomid}/devices", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createDevice(
			@ApiParam(value = "Project ID")
			@PathVariable(name = "projectid", required = true)
			Long projectid,
			@ApiParam(value = "Room ID")
			@PathVariable(name = "roomid", required = true)
			Long roomid,
			@RequestBody
			Device device) {
		ResponseEntity<?> response = null;
		try {
			validateDeviceFields(device);
			projectService.findById(projectid);
			Room room = roomService.findById(roomid);
			device.setRoom(room);
			response = new ResponseEntity<Device>(deviceService.createDevice(device), HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Invalid input: {}", e.getMessage());
			response = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (EntityNotFoundException e) {
			LOGGER.error(e.getMessage());
			response= new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
		return response;
	}

	@ApiOperation(value = "get device by deviceid")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Device instance found", response = Device.class),
			@ApiResponse(code = 400, message = "Parent element not found"),
			@ApiResponse(code = 404, message = "Device instance not found")
	})
	@GetMapping(value = "/projects/{projectid}/rooms/{roomid}/devices/{deviceid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getDevice(
			@ApiParam(value = "Project ID")
			@PathVariable(name = "projectid", required = true)
			Long projectid,
			@ApiParam(value = "Room ID")
			@PathVariable(name = "roomid", required = true)
			Long roomid,
			@ApiParam("Device ID")
			@PathVariable(name = "deviceid", required = true)
			Long deviceid) {
		ResponseEntity<?> response = null;
		try {
			projectService.findById(projectid);
			roomService.findById(roomid);
			response = new ResponseEntity<>(deviceService.findById(deviceid), HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Invalid input: {}", e.getMessage());
			response = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (EntityNotFoundException e) {
			LOGGER.error(e.getMessage());
			response= new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	@ApiOperation(value = "get device list")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Device list for room", response = List.class),
			@ApiResponse(code = 404, message = "Entity not found")
	})
	@GetMapping(value = "/projects/{projectid}/rooms/{roomid}/devices", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getDeviceList(
			@ApiParam(value = "Project ID")
			@PathVariable(name = "projectid", required = true)
			Long projectid,
			@ApiParam(value = "Room ID")
			@PathVariable(name = "roomid", required = true)
			Long roomid) {
		ResponseEntity<?> response = null;
		try {
			projectService.findById(projectid);
			roomService.findById(roomid);
			response = new ResponseEntity<List<Device>>(deviceService.findByRoomId(roomid), HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			LOGGER.error(e.getMessage());
			response= new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	@ApiOperation(value = "Replace existing device instance")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Device instance replaced"),
			@ApiResponse(code = 400, message = "Parent element not found"),
			@ApiResponse(code = 404, message = "Device instance to replace not found") 
	})
	@PutMapping(value = "/projects/{projectid}/rooms/{roomid}/devices/{deviceid}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> replaceExistingDevice(
			@ApiParam(value = "Project ID")
			@PathVariable(name = "projectid", required = true)
			Long projectid,
			@ApiParam(value = "Room ID")
			@PathVariable(name = "roomid", required = true)
			Long roomid,
			@ApiParam(value = "Device ID")
			@PathVariable(name = "deviceid", required = true)
			Long deviceid,
			@RequestBody Device device) {
		ResponseEntity<?> response = null;
		try {
			validateDeviceFields(device);
			projectService.findById(projectid);
			Room room = roomService.findById(roomid);
			device.setRoom(room);
			response = new ResponseEntity<Device>(deviceService.updateDevice(device), HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Invalid input: {}", e.getMessage());
			response = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (EntityNotFoundException e) {
			LOGGER.error(e.getMessage());
			response= new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	@ApiOperation(value = "Delete device instance")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Device instance deleted", response = Device.class),
			@ApiResponse(code = 400, message = "Parent element not found"),
			@ApiResponse(code = 404, message = "Device to delete not found")
	})
	@DeleteMapping(value = "/projects/{projectid}/rooms/{roomid}/devices/{deviceid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteDevice(
			@ApiParam(value = "Project ID")
			@PathVariable(name = "projectid", required = true)
			Long projectid,
			@ApiParam(value = "Room ID")
			@PathVariable(name = "roomid", required = true)
			Long roomid,
			@ApiParam(value = "Device ID")
			@PathVariable(name = "deviceid", required = true)
			Long deviceid) {
		ResponseEntity<?> response = null;
		try {
			projectService.findById(projectid);
			roomService.findById(roomid);
			Device existingDevice = deviceService.findById(deviceid);
			deviceService.delete(existingDevice);
			response = new ResponseEntity<Device>(existingDevice, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Invalid input: {}", e.getMessage());
			response = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (EntityNotFoundException e) {
			LOGGER.error(e.getMessage());
			response= new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
		return response;
	}

	private void validateDeviceFields(Device device) {
		if(StringUtils.isEmpty(device.getLabel())) {
			throw new IllegalArgumentException("Device label cannot be empty");
		}
		if(StringUtils.isEmpty(device.getDeviceType())) {
			throw new IllegalArgumentException("Device type cannot be empty");
		}
	}
}
