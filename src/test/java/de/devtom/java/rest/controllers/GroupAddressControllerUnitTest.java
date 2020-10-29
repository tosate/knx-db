package de.devtom.java.rest.controllers;

import static de.devtom.java.config.KnxDbApplicationConfiguration.BASE_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Optional;

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
import de.devtom.java.services.GroupAddressService;
import de.devtom.java.services.ProjectService;

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
	private static final String URI = BASE_PATH + "/project/" + PROJECT_ID + "/room/" + ROOM_ID + "/device/" + DEVICE_ID + "/group-address";
	private static final int ADDR_MAIN_GROUP = 1;
	private static final int ADDR_MIDDLE_GROUP = 1;
	private static final int ADDR_SUB_GROUP = 1;
	@MockBean
	private ProjectService projectService;
	@MockBean
	private GroupAddressService groupAddressService;
	
	@Test
	public void testCreateGroupAddress() {
		Project project = new Project(PROJECT_NAME);
		project.setProjectid(PROJECT_ID);
		Room room = new Room(ROOM_NAME, ROOM_LABEL);
		room.setRoomid(ROOM_ID);
		project.addRoom(room);
		Device device = new Device(DEVICE_LABEL, DEVICE_TYPE);
		device.setDeviceid(DEVICE_ID);
		room.addDevice(device);
		GroupAddress address = new GroupAddress(ADDR_MAIN_GROUP, ADDR_MIDDLE_GROUP, ADDR_SUB_GROUP);
		GroupAddress savedAddress = new GroupAddress(ADDR_MAIN_GROUP, ADDR_MIDDLE_GROUP, ADDR_SUB_GROUP);
		savedAddress.setGroupAddressId(GROUP_ADDRESS_ID);
		
		try {
			String inputJson = mapToJson(address);
			Mockito.when(projectService.findById(Mockito.anyLong())).thenReturn(Optional.of(project));
			Mockito.when(groupAddressService.save(Mockito.any(Device.class), Mockito.any(GroupAddress.class))).thenReturn(savedAddress);
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(URI).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
			Mockito.verify(projectService, Mockito.times(1)).findById(Mockito.anyLong());
			Mockito.verify(groupAddressService, Mockito.times(1)).save(Mockito.any(Device.class), Mockito.any(GroupAddress.class));
			
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
		project.addRoom(room);
		Device device = new Device(DEVICE_LABEL, DEVICE_TYPE);
		device.setDeviceid(DEVICE_ID);
		room.addDevice(device);
		GroupAddress address = new GroupAddress(ADDR_MAIN_GROUP, ADDR_MIDDLE_GROUP, ADDR_SUB_GROUP);
		address.setGroupAddressId(GROUP_ADDRESS_ID);
		device.addAddress(address);
		
		try {
			Mockito.when(projectService.findById(Mockito.anyLong())).thenReturn(Optional.of(project));
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(URI + "/" + GROUP_ADDRESS_ID)).andReturn();
			Mockito.verify(projectService, Mockito.times(1)).findById(Mockito.anyLong());
			
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
