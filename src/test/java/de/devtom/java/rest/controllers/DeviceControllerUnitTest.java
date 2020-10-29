package de.devtom.java.rest.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;

import de.devtom.java.entities.Device;
import de.devtom.java.entities.Project;
import de.devtom.java.entities.Room;
import de.devtom.java.services.DeviceService;
import de.devtom.java.services.ProjectService;

import static de.devtom.java.config.KnxDbApplicationConfiguration.BASE_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Optional;

@ContextConfiguration(classes = {DeviceController.class})
public class DeviceControllerUnitTest extends AbstractControllerUnitTest {
	private static final Long PROJECT_ID = 1l;
	private static final String PROJECT_NAME = "project-name";
	private static final Long ROOM_ID = 1l;
	private static final String ROOM_NAME = "room-name";
	private static final String ROOM_LABEL = "room-label";
	private static final Long DEVICE_ID = 1l;
	private static final String DEVICE_LABEL = "device-label";
	private static final String DEVICE_TYPE = "Lightbuld";
	private static final String URI = BASE_PATH + "/project/" + PROJECT_ID + "/room/" + ROOM_ID + "/device";
	@MockBean
	private ProjectService projectService;
	@MockBean
	private DeviceService deviceService;
	
	@Test
	public void testCreateDevice() {
		Project project = new Project(PROJECT_NAME);
		project.setProjectid(PROJECT_ID);
		Room room = new Room(ROOM_NAME, ROOM_LABEL);
		room.setRoomid(ROOM_ID);
		project.addRoom(room);
		Device device = new Device(DEVICE_LABEL, DEVICE_TYPE);
		Device savedDevice = new Device(DEVICE_LABEL, DEVICE_TYPE);
		savedDevice.setDeviceid(DEVICE_ID);
		
		try {
			String inputJson = mapToJson(device);
			Mockito.when(projectService.findById(Mockito.anyLong())).thenReturn(Optional.of(project));
			Mockito.when(deviceService.save(Mockito.any(Room.class), Mockito.any(Device.class))).thenReturn(savedDevice);
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(URI).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
			Mockito.verify(projectService, Mockito.times(1)).findById(Mockito.anyLong());
			Mockito.verify(deviceService, Mockito.times(1)).save(Mockito.any(Room.class), Mockito.any(Device.class));
			
			validateHttpStatus(HttpStatus.CREATED, mvcResult);
			String content = mvcResult.getResponse().getContentAsString();
			String outputJson = mapToJson(savedDevice);
			assertEquals(outputJson, content);
		} catch (JsonProcessingException e) {
			fail(e.getMessage());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testCreateDeviceEmptyType() {
		Project project = new Project(PROJECT_NAME);
		project.setProjectid(PROJECT_ID);
		Room room = new Room(ROOM_NAME, ROOM_LABEL);
		room.setRoomid(ROOM_ID);
		project.addRoom(room);
		Device device = new Device(DEVICE_LABEL, "");
		
		try {
			String inputJString = mapToJson(device);
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(URI).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJString)).andReturn();
			
			validateHttpStatus(HttpStatus.BAD_REQUEST, mvcResult);
		} catch (JsonProcessingException e) {
			fail(e.getMessage());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetDevice() {
		Project project = new Project(PROJECT_NAME);
		project.setProjectid(PROJECT_ID);
		Room room = new Room(ROOM_NAME, ROOM_LABEL);
		room.setRoomid(ROOM_ID);
		project.addRoom(room);
		Device device = new Device(DEVICE_LABEL, DEVICE_TYPE);
		device.setDeviceid(DEVICE_ID);
		room.addDevice(device);
		
		try {
			Mockito.when(projectService.findById(Mockito.anyLong())).thenReturn(Optional.of(project));
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(URI + "/" + DEVICE_ID)).andReturn();
			Mockito.verify(projectService, Mockito.times(1)).findById(Mockito.anyLong());
			
			validateHttpStatus(HttpStatus.OK, mvcResult);
			String content = mvcResult.getResponse().getContentAsString();
			String outputJson = mapToJson(device);
			assertEquals(outputJson, content);
		} catch (JsonProcessingException e) {
			fail(e.getMessage());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetDeviceNotExistent() {
		Project project = new Project(PROJECT_NAME);
		project.setProjectid(PROJECT_ID);
		Room room = new Room(ROOM_NAME, ROOM_LABEL);
		room.setRoomid(ROOM_ID);
		project.addRoom(room);
		
		try {
			Mockito.when(projectService.findById(Mockito.anyLong())).thenReturn(Optional.of(project));
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(URI + "/" + DEVICE_ID)).andReturn();
			Mockito.verify(projectService, Mockito.times(1)).findById(Mockito.anyLong());
			
			validateHttpStatus(HttpStatus.NOT_FOUND, mvcResult);
		} catch (JsonProcessingException e) {
			fail(e.getMessage());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testUpdateDevice() {
		Project project = new Project(PROJECT_NAME);
		project.setProjectid(PROJECT_ID);
		Room room = new Room(ROOM_NAME, ROOM_LABEL);
		room.setRoomid(ROOM_ID);
		project.addRoom(room);
		Device device = new Device(DEVICE_LABEL, DEVICE_TYPE);
		device.setDeviceid(DEVICE_ID);
		room.addDevice(device);
		Device updatedDevice = new Device("new-label", DEVICE_TYPE);
		updatedDevice.setDeviceid(DEVICE_ID);
		
		try {
			String inputJString = mapToJson(updatedDevice);
			Mockito.when(projectService.findById(Mockito.anyLong())).thenReturn(Optional.of(project));
			Mockito.when(deviceService.update(Mockito.any(Device.class))).thenReturn(updatedDevice);
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(URI + "/" + DEVICE_ID).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJString)).andReturn();
			Mockito.verify(projectService, Mockito.times(1)).findById(Mockito.anyLong());
			Mockito.verify(deviceService, Mockito.times(1)).update(Mockito.any(Device.class));
			
			validateHttpStatus(HttpStatus.OK, mvcResult);
			String content = mvcResult.getResponse().getContentAsString();
			String outputJson = mapToJson(updatedDevice);
			assertEquals(outputJson, content);
		} catch (JsonProcessingException e) {
			fail(e.getMessage());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testDeleteDevice() {
		Project project = new Project(PROJECT_NAME);
		project.setProjectid(PROJECT_ID);
		Room room = new Room(ROOM_NAME, ROOM_LABEL);
		room.setRoomid(ROOM_ID);
		project.addRoom(room);
		Device device = new Device(DEVICE_LABEL, DEVICE_TYPE);
		device.setDeviceid(DEVICE_ID);
		room.addDevice(device);
		
		try {
			Mockito.when(projectService.findById(Mockito.anyLong())).thenReturn(Optional.of(project));
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/" + DEVICE_ID)).andReturn();
			Mockito.verify(projectService, Mockito.times(1)).findById(Mockito.anyLong());
			Mockito.verify(deviceService, Mockito.times(1)).delete(Mockito.any(Device.class));
			
			validateHttpStatus(HttpStatus.OK, mvcResult);
			String content = mvcResult.getResponse().getContentAsString();
			String outputJson = mapToJson(device);
			assertEquals(outputJson, content);
		} catch (JsonProcessingException e) {
			fail(e.getMessage());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
