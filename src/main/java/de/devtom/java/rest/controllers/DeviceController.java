package de.devtom.java.rest.controllers;

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
import de.devtom.java.entities.Project;
import de.devtom.java.entities.Room;
import de.devtom.java.services.DeviceService;
import de.devtom.java.services.ProjectService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import static de.devtom.java.config.KnxDbApplicationConfiguration.BASE_PATH;

import java.rmi.activation.UnknownObjectException;
import java.util.Optional;

@RestController
@RequestMapping(BASE_PATH)
public class DeviceController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DeviceController.class);
	
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private ProjectService projectService;
	
	@ApiOperation(value = "create device instance")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "New device instance created", response = Device.class),
			@ApiResponse(code = 400, message = "Empty mandatory fields, device alredy exists or parent elements not found")
	})
	@PostMapping(value = "/projects/{projectid}/rooms/{roomid}/devices", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Device> createDevice(@PathVariable Long projectid, @PathVariable Long roomid, @RequestBody Device device) {
		ResponseEntity<Device> response = null;
		try {
			validateDeviceFields(device);
			Project project = getProject(projectid);
			Room room = getRoom(project, roomid);
			for(Device existingDevice : room.getDevices()) {
				if(existingDevice.getLabel().equals(device.getLabel())) {
					throw new IllegalArgumentException(String.format("Device with label [%s] already exists.", device.getLabel()));
				}
			}
			response = new ResponseEntity<>(deviceService.save(room, device), HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Invalid input: {}", e.getMessage());
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
	public ResponseEntity<Device> getDevice(@PathVariable Long projectid, @PathVariable Long roomid, @PathVariable Long deviceid) {
		ResponseEntity<Device> response = null;
		try {
			Project project = getProject(projectid);
			Room room = getRoom(project, roomid);
			response = new ResponseEntity<>(retrieveExistingDevice(room, deviceid), HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Invalid input: {}", e.getMessage());
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (UnknownObjectException e) {
			LOGGER.error(e.getMessage());
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
	public ResponseEntity<Device> replaceExistingDevice(@PathVariable Long projectid, @PathVariable Long roomid, @PathVariable Long deviceid, @RequestBody Device device) {
		ResponseEntity<Device> response = null;
		try {
			validateDeviceFields(device);
			Project project = getProject(projectid);
			Room room = getRoom(project, roomid);
			Device existingDevice = retrieveExistingDevice(room, deviceid);
			device.setDeviceid(existingDevice.getDeviceid());
			response = new ResponseEntity<>(deviceService.update(device), HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Invalid input: {}", e.getMessage());
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (UnknownObjectException e) {
			LOGGER.error(e.getMessage());
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
	public ResponseEntity<Device> deleteDevice(@PathVariable Long projectid, @PathVariable Long roomid, @PathVariable Long deviceid) {
		ResponseEntity<Device> response = null;
		try {
			Project project = getProject(projectid);
			Room room = getRoom(project, roomid);
			Device existingDevice = retrieveExistingDevice(room, deviceid);
			deviceService.delete(room, existingDevice);
			response = new ResponseEntity<>(existingDevice, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Invalid input: {}", e.getMessage());
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (UnknownObjectException e) {
			LOGGER.error(e.getMessage());
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	private Device retrieveExistingDevice(Room room, Long deviceid) throws UnknownObjectException {
		Optional<Device> result = Optional.empty();
		for(Device device : room.getDevices()) {
			if(device.getDeviceid() != null && device.getDeviceid() == deviceid) {
				result = Optional.of(device);
				break;
			}
		}
		if(result.isPresent()) {
			return result.get();
		} else {
			throw new UnknownObjectException(String.format("No device with deviceid [%d]", deviceid));
		}
	}

	private Room getRoom(Project project, Long roomid) {
		for(Room room : project.getRooms()) {
			if(room.getRoomid().equals(roomid)) {
				return room;
			}
		}
		
		throw new IllegalArgumentException(String.format("Project [%s] contains no room with room ID [%d]", project.getName(), roomid));
	}

	private Project getProject(Long projectid) {
		Optional<Project> project = projectService.findById(projectid);
		if(project.isPresent()) {
			return project.get();
		} else {
			throw new IllegalArgumentException(String.format("No project with projectid [%d]", projectid));
		}
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
