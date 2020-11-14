package de.devtom.java.rest.controllers;

import static de.devtom.java.config.KnxDbApplicationConfiguration.BASE_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import javax.persistence.EntityNotFoundException;

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
import de.devtom.java.entities.GroupAddress;
import de.devtom.java.entities.Project;
import de.devtom.java.entities.Room;
import de.devtom.java.services.DeviceService;
import de.devtom.java.services.GroupAddressService;
import de.devtom.java.services.ProjectService;
import de.devtom.java.services.RoomService;

@ContextConfiguration(classes = {GroupAddressController.class})
public class GroupAddressControllerUnitTest extends AbstractControllerUnitTest {
	private static final Long PROJECT_ID = 1l;
	private static final String PROJECT_NAME = "project-name";
	private static final Long ROOM_ID = 1l;
	private static final String ROOM_NAME = "room-name";
	private static final String ROOM_LABEL = "room-label";
	private static final Long DEVICE_ID = 1l;
	private static final String DEVICE_LABEL = "device-label";
	private static final String DEVICE_TYPE = "Lightbulb";
	private static final Long GROUP_ADDRESS_ID = 1l;
	private static final String URI = BASE_PATH + "/projects/" + PROJECT_ID + "/rooms/" + ROOM_ID + "/devices/" + DEVICE_ID + "/group-addresses";
	private static final int ADDR_MAIN_GROUP = 1;
	private static final int ADDR_MIDDLE_GROUP = 1;
	private static final int ADDR_SUB_GROUP = 1;
	@MockBean
	private ProjectService projectService;
	@MockBean
	private RoomService roomService;
	@MockBean
	private DeviceService deviceService;
	@MockBean
	private GroupAddressService groupAddressService;
	
	@Test
	public void testCreateGroupAddress() {
		Project project = new Project(PROJECT_NAME);
		project.setProjectid(PROJECT_ID);
		Room room = new Room(ROOM_NAME, ROOM_LABEL);
		room.setRoomid(ROOM_ID);
		room.setProject(project);
		Device device = new Device(DEVICE_LABEL, DEVICE_TYPE);
		device.setDeviceid(DEVICE_ID);
		device.setRoom(room);
		GroupAddress address = new GroupAddress(ADDR_MAIN_GROUP, ADDR_MIDDLE_GROUP, ADDR_SUB_GROUP);
		GroupAddress savedAddress = new GroupAddress(ADDR_MAIN_GROUP, ADDR_MIDDLE_GROUP, ADDR_SUB_GROUP);
		savedAddress.setGroupAddressId(GROUP_ADDRESS_ID);
		
		try {
			String inputJson = mapToJson(address);
			Mockito.when(projectService.findById(Mockito.anyLong())).thenReturn(project);
			Mockito.when(roomService.findById(Mockito.anyLong())).thenReturn(room);
			Mockito.when(deviceService.findById(Mockito.anyLong())).thenReturn(device);
			Mockito.when(groupAddressService.createGroupAddress(Mockito.any(GroupAddress.class))).thenReturn(savedAddress);
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(URI).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
			Mockito.verify(projectService, Mockito.times(1)).findById(Mockito.anyLong());
			Mockito.verify(roomService, Mockito.times(1)).findById(Mockito.anyLong());
			Mockito.verify(deviceService, Mockito.times(1)).findById(Mockito.anyLong());
			Mockito.verify(groupAddressService, Mockito.times(1)).createGroupAddress(Mockito.any(GroupAddress.class));
			
			validateHttpStatus(HttpStatus.CREATED, mvcResult);
			String content = mvcResult.getResponse().getContentAsString();
			String outputJson = mapToJson(savedAddress);
			assertEquals(outputJson, content);
		} catch (JsonProcessingException e) {
			fail(e.getMessage());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetGroupAddress() {
		Project project = new Project(PROJECT_NAME);
		project.setProjectid(PROJECT_ID);
		Room room = new Room(ROOM_NAME, ROOM_LABEL);
		room.setRoomid(ROOM_ID);
		room.setProject(project);
		Device device = new Device(DEVICE_LABEL, DEVICE_TYPE);
		device.setDeviceid(DEVICE_ID);
		device.setRoom(room);
		GroupAddress address = new GroupAddress(ADDR_MAIN_GROUP, ADDR_MIDDLE_GROUP, ADDR_SUB_GROUP);
		address.setGroupAddressId(GROUP_ADDRESS_ID);
		address.setDevice(device);
		
		try {
			Mockito.when(projectService.findById(Mockito.anyLong())).thenReturn(project);
			Mockito.when(roomService.findById(Mockito.anyLong())).thenReturn(room);
			Mockito.when(deviceService.findById(Mockito.anyLong())).thenReturn(device);
			Mockito.when(groupAddressService.findById(Mockito.anyLong())).thenReturn(address);
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(URI + "/" + GROUP_ADDRESS_ID)).andReturn();
			Mockito.verify(projectService, Mockito.times(1)).findById(Mockito.anyLong());
			Mockito.verify(roomService, Mockito.times(1)).findById(Mockito.anyLong());
			Mockito.verify(deviceService, Mockito.times(1)).findById(Mockito.anyLong());
			Mockito.verify(groupAddressService, Mockito.times(1)).findById(Mockito.anyLong());
			
			validateHttpStatus(HttpStatus.OK, mvcResult);
			String content = mvcResult.getResponse().getContentAsString();
			String outputJson = mapToJson(address);
			assertEquals(outputJson, content);
		} catch (JsonProcessingException e) {
			fail(e.getMessage());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetGroupAddressNotExistent() {
		Project project = new Project(PROJECT_NAME);
		project.setProjectid(PROJECT_ID);
		Room room = new Room(ROOM_NAME, ROOM_LABEL);
		room.setRoomid(ROOM_ID);
		room.setProject(project);
		Device device = new Device(DEVICE_LABEL, DEVICE_TYPE);
		device.setDeviceid(DEVICE_ID);
		device.setRoom(room);
		
		try {
			Mockito.when(projectService.findById(Mockito.anyLong())).thenReturn(project);
			Mockito.when(roomService.findById(Mockito.anyLong())).thenReturn(room);
			Mockito.when(deviceService.findById(Mockito.anyLong())).thenReturn(device);
			Mockito.when(groupAddressService.findById(Mockito.anyLong())).thenThrow(EntityNotFoundException.class);
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(URI + "/" + GROUP_ADDRESS_ID)).andReturn();
			Mockito.verify(projectService, Mockito.times(1)).findById(Mockito.anyLong());
			Mockito.verify(roomService, Mockito.times(1)).findById(Mockito.anyLong());
			Mockito.verify(deviceService, Mockito.times(1)).findById(Mockito.anyLong());
			Mockito.verify(groupAddressService, Mockito.times(1)).findById(Mockito.anyLong());
			
			validateHttpStatus(HttpStatus.NOT_FOUND, mvcResult);
		} catch (JsonProcessingException e) {
			fail(e.getMessage());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testUpdateGroupAddress() {
		Project project = new Project(PROJECT_NAME);
		project.setProjectid(PROJECT_ID);
		Room room = new Room(ROOM_NAME, ROOM_LABEL);
		room.setRoomid(ROOM_ID);
		room.setProject(project);
		Device device = new Device(DEVICE_LABEL, DEVICE_TYPE);
		device.setDeviceid(DEVICE_ID);
		device.setRoom(room);
		GroupAddress address = new GroupAddress(ADDR_MAIN_GROUP, ADDR_MIDDLE_GROUP, ADDR_SUB_GROUP);
		address.setGroupAddressId(GROUP_ADDRESS_ID);
		address.setDevice(device);
		GroupAddress updatedGroupAddress = new GroupAddress(ADDR_MAIN_GROUP, ADDR_MIDDLE_GROUP, 2);
		updatedGroupAddress.setGroupAddressId(GROUP_ADDRESS_ID);
		
		try {
			String inputJson = mapToJson(updatedGroupAddress);
			Mockito.when(projectService.findById(Mockito.anyLong())).thenReturn(project);
			Mockito.when(roomService.findById(Mockito.anyLong())).thenReturn(room);
			Mockito.when(deviceService.findById(Mockito.anyLong())).thenReturn(device);
			Mockito.when(groupAddressService.updateGroupAddress(Mockito.any(GroupAddress.class))).thenReturn(updatedGroupAddress);
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(URI + "/" + GROUP_ADDRESS_ID).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
			Mockito.verify(projectService, Mockito.times(1)).findById(Mockito.anyLong());
			Mockito.verify(roomService, Mockito.times(1)).findById(Mockito.anyLong());
			Mockito.verify(deviceService, Mockito.times(1)).findById(Mockito.anyLong());
			Mockito.verify(groupAddressService, Mockito.times(1)).updateGroupAddress(Mockito.any(GroupAddress.class));
			
			validateHttpStatus(HttpStatus.OK, mvcResult);
			String content = mvcResult.getResponse().getContentAsString();
			String outputJson = mapToJson(updatedGroupAddress);
			assertEquals(outputJson, content);
		} catch (JsonProcessingException e) {
			fail(e.getMessage());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testDeleteGroupAddress() {
		Project project = new Project(PROJECT_NAME);
		project.setProjectid(PROJECT_ID);
		Room room = new Room(ROOM_NAME, ROOM_LABEL);
		room.setRoomid(ROOM_ID);
		room.setProject(project);
		Device device = new Device(DEVICE_LABEL, DEVICE_TYPE);
		device.setDeviceid(DEVICE_ID);
		device.setRoom(room);
		GroupAddress address = new GroupAddress(ADDR_MAIN_GROUP, ADDR_MIDDLE_GROUP, ADDR_SUB_GROUP);
		address.setGroupAddressId(GROUP_ADDRESS_ID);
		address.setDevice(device);
		
		try {
			Mockito.when(projectService.findById(Mockito.anyLong())).thenReturn(project);
			Mockito.when(roomService.findById(Mockito.anyLong())).thenReturn(room);
			Mockito.when(deviceService.findById(Mockito.anyLong())).thenReturn(device);
			Mockito.when(groupAddressService.findById(Mockito.anyLong())).thenReturn(address);
			Mockito.when(groupAddressService.delete(Mockito.any(GroupAddress.class))).thenReturn(true);
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/" + GROUP_ADDRESS_ID)).andReturn();
			Mockito.verify(projectService, Mockito.times(1)).findById(Mockito.anyLong());
			Mockito.verify(roomService, Mockito.times(1)).findById(Mockito.anyLong());
			Mockito.verify(deviceService, Mockito.times(1)).findById(Mockito.anyLong());
			Mockito.verify(groupAddressService, Mockito.times(1)).findById(Mockito.anyLong());
			Mockito.verify(groupAddressService, Mockito.times(1)).delete(Mockito.any(GroupAddress.class));
			
			validateHttpStatus(HttpStatus.OK, mvcResult);
			String content = mvcResult.getResponse().getContentAsString();
			String outputJson = mapToJson(address);
			assertEquals(outputJson, content);
		} catch (JsonProcessingException e) {
			fail(e.getMessage());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
