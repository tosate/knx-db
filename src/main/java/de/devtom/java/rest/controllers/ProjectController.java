package de.devtom.java.rest.controllers;

import static de.devtom.java.config.KnxDbApplicationConfiguration.BASE_PATH;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.devtom.java.entities.Device;
import de.devtom.java.entities.GroupAddress;
import de.devtom.java.entities.Project;
import de.devtom.java.entities.Room;
import de.devtom.java.output.csv.AbstractCsvGenerator;
import de.devtom.java.output.csv.HomeAssistantCsvGenerator;
import de.devtom.java.output.csv.HomebridgeCsvGenerator;
import de.devtom.java.services.DeviceService;
import de.devtom.java.services.GroupAddressService;
import de.devtom.java.services.ProjectService;
import de.devtom.java.services.RoomService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(BASE_PATH)
public class ProjectController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);
	public static final String MEDIA_TYPE_CSV_STRING = "text/csv";
	private static final MediaType MEDIA_TYPE_CSV = new MediaType("text", "csv");
	@Autowired
	private ProjectService projectService;
	@Autowired
	private RoomService roomService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private GroupAddressService groupAddressService;
	
	@ApiOperation(value = "create a project instance")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "New project instance created", response = Project.class),
			@ApiResponse(code = 400, message = "Empty mandatory field or project already exists")
	})
	@PostMapping(value = "/projects", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Project> createProject(@RequestBody Project project) {
		ResponseEntity<Project> response = null;
		try {
			validateProjectName(project);
			response = new ResponseEntity<>(projectService.createProject(project), HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Invalid input: {}", e.getMessage());
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return response;
	}
	
	@ApiOperation(value = "Get project instance by projectid. Provide the format parameter for CSV output")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Project instance found", response = Project.class),
			@ApiResponse(code = 404, message = "Project instance no found")
	})
	@GetMapping(value = "/projects/{projectid}", produces = {MediaType.APPLICATION_JSON_VALUE,MEDIA_TYPE_CSV_STRING})
	public ResponseEntity<?> getProject(
			@ApiParam(value = "Project ID")
			@PathVariable(name = "projectid", required = true)
			Long projectid,
			@ApiParam(value = "Optional parameter to define CSV output format")
			@RequestParam(name = "format", required = false)
			String format) {
		ResponseEntity<?> response = null;
		try {
			Project project = projectService.findById(projectid);
			if(!StringUtils.isEmpty(format)) {
				final HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.setContentType(MEDIA_TYPE_CSV);
				response = new ResponseEntity<String>(generateCsv(project, format), httpHeaders, HttpStatus.OK);
			} else {
				final HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.setContentType(MediaType.APPLICATION_JSON);
				response = new ResponseEntity<Project>(project, httpHeaders, HttpStatus.OK);
			}
		} catch(EntityNotFoundException e) {
			LOGGER.error(e.getMessage());
			response = new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			LOGGER.error("CSV rendering failed: " + e.getMessage());
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return response;
	}
	
	private String generateCsv(Project project, String format) throws IOException {
		String result = null;
		if(!StringUtils.isEmpty(format)) {
			List<Room> rooms = roomService.findByProjectId(project.getProjectid());
			List<Device> devices = new ArrayList<>();
			for(Room room : rooms) {
				devices.addAll(deviceService.findByRoomId(room.getRoomid()));
			}
			List<GroupAddress> groupAddresses = new ArrayList<>();
			for(Device device : devices) {
				groupAddresses.addAll(groupAddressService.findByDeviceId(device.getDeviceid()));
			}
			
			AbstractCsvGenerator generator = null;
			switch(format) {
			case "HomeAssistant":
				generator = new HomeAssistantCsvGenerator();
				result = generator.generateCsv(project, rooms, devices, groupAddresses);
				break;
			case "Homebridge":
				generator = new HomebridgeCsvGenerator();
				result = generator.generateCsv(project, rooms, devices, groupAddresses);
				break;
			default:
				result = String.format("Unsupported format [%s]", format);
			}
		}
		
		return result;
	}
	
	@ApiOperation(value = "Get project list. A single project can be retrieve by name overa query parameter")
	@ApiResponse(code = 200, message = "List of projects", response = List.class)
	@GetMapping(value = "/projects", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Project>> getProjectList(
			@ApiParam(value = "Optional name filter value")
			@RequestParam(required = false)
			String name) {
		ResponseEntity<List<Project>> response = null;
		if(StringUtils.isEmpty(name)) {
			response = new ResponseEntity<>(projectService.list(), HttpStatus.OK);
		} else {
			List<Project> result = new ArrayList<>();
			for(Project project : projectService.list()) {
				if(project.getName().equals(name)) {
					result.add(project);
				}
			}
			response = new ResponseEntity<>(result, HttpStatus.OK);
		}
		
		return response;
	}
	
	@ApiOperation(value = "Replace existing project instance")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Project instance replaced", response = Project.class),
		@ApiResponse(code = 400, message = "Empty mandatory field"),
		@ApiResponse(code = 404, message = "Project instance to replace not found")
	})
	@PutMapping(value = "/projects/{projectid}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> replaceExistingProject(
			@ApiParam(value = "Project ID")
			@PathVariable(name = "projectid", required = true)
			Long projectid,
			@RequestBody
			Project project) {
		ResponseEntity<?> response = null;
		try {
			validateProjectName(project);
			Project existingProject = projectService.findById(projectid);
			response = new ResponseEntity<Project>(projectService.createProject(existingProject), HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Invalid input: " + e.getMessage());
			response = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch(EntityNotFoundException e) {
			LOGGER.error(e.getMessage());
			response = new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	@ApiOperation(value = "Delete project instance")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Project instance deleted", response = Project.class),
			@ApiResponse(code = 404, message = "Project to delete not found")
	})
	@DeleteMapping(value = "/projects/{projectid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteProject(
			@ApiParam(value = "Project ID")
			@PathVariable(name = "projectid", required = true)
			Long projectid) {
		ResponseEntity<?> response = null;
		try {
			Project existingProject = projectService.findById(projectid);
			projectService.delete(existingProject);
			response = new ResponseEntity<Project>(existingProject, HttpStatus.OK);
		}  catch(EntityNotFoundException e) {
			LOGGER.error(e.getMessage());
			response = new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	private void validateProjectName(Project project) {
		if(StringUtils.isEmpty(project.getName())) {
			throw new IllegalArgumentException("Project name cannot be empty");
		}
	}
}
