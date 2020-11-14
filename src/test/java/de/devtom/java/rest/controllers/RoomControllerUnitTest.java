package de.devtom.java.rest.controllers;

import static de.devtom.java.config.KnxDbApplicationConfiguration.BASE_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;

import de.devtom.java.entities.Project;
import de.devtom.java.entities.Room;
import de.devtom.java.services.ProjectService;
import de.devtom.java.services.RoomService;

@ContextConfiguration(classes = {RoomController.class})
public class RoomControllerUnitTest extends AbstractControllerUnitTest {
	private static final Long PROJECT_ID = 1l;
	private static final Long ROOM_ID = 1l;
	private static final String PROJECT_NAME = "project-name";
	private static final String ROOM_NAME = "room-name";
	private static final String ROOM_LABEL = "room-label";
	private static final String URI = BASE_PATH + "/projects/" + PROJECT_ID + "/rooms";
	@MockBean
	private ProjectService projectService;
	@MockBean
	private RoomService roomService;
	
	@Test
	public void testCreateRoom() {
		Project project = new Project(PROJECT_NAME);
		project.setProjectid(PROJECT_ID);
		Room room = new Room(ROOM_NAME, ROOM_LABEL);
		Room savedRoom = new Room(ROOM_NAME, ROOM_LABEL);
		savedRoom.setRoomid(ROOM_ID);
		
		try {
			String inputJson = mapToJson(room);
			Mockito.when(projectService.findById(Mockito.anyLong())).thenReturn(project);
			Mockito.when(roomService.createRoom(Mockito.any(Room.class))).thenReturn(savedRoom);
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(URI).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
			Mockito.verify(projectService, Mockito.times(1)).findById(Mockito.anyLong());
			Mockito.verify(roomService, Mockito.times(1)).createRoom(Mockito.any(Room.class));
			
			validateHttpStatus(HttpStatus.CREATED, mvcResult);
			String content = mvcResult.getResponse().getContentAsString();
			String outputJson = mapToJson(savedRoom);
			assertEquals(outputJson, content);
		} catch (JsonProcessingException e) {
			fail(e.getMessage());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testCreateRoomWithoutLabel() {
		Room room = new Room(ROOM_NAME, null);
		
		try {
			String inputJson = mapToJson(room);
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(URI).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
			
			validateHttpStatus(HttpStatus.BAD_REQUEST, mvcResult);
		} catch (JsonProcessingException e) {
			fail(e.getMessage());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetRoom() {
		Project project = new Project(PROJECT_NAME);
		project.setProjectid(PROJECT_ID);
		Room room = new Room(ROOM_NAME, ROOM_LABEL);
		room.setRoomid(ROOM_ID);
		room.setProject(project);
		
		try {
			Mockito.when(projectService.findById(Mockito.anyLong())).thenReturn(project);
			Mockito.when(roomService.findById(Mockito.anyLong())).thenReturn(room);
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(URI + "/" + ROOM_ID)).andReturn();
			Mockito.verify(projectService, Mockito.times(1)).findById(Mockito.anyLong());
			Mockito.verify(roomService, Mockito.times(1)).findById(Mockito.anyLong());
			
			validateHttpStatus(HttpStatus.OK, mvcResult);
			String content = mvcResult.getResponse().getContentAsString();
			String outputJson = mapToJson(room);
			assertEquals(outputJson, content);
		} catch (JsonProcessingException e) {
			fail(e.getMessage());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testUpdateRoom() {
		Project project = new Project(PROJECT_NAME);
		project.setProjectid(PROJECT_ID);
		Room existingRoom = new Room(ROOM_NAME, ROOM_LABEL);
		existingRoom.setRoomid(ROOM_ID);
		existingRoom.setProject(project);
		Room updatedRoom = new Room("updated-room", "updated-room-label");
		updatedRoom.setRoomid(ROOM_ID);
		
		try {
			String inputJson = mapToJson(updatedRoom);
			Mockito.when(projectService.findById(Mockito.anyLong())).thenReturn(project);
			Mockito.when(roomService.updateRoom(Mockito.any(Room.class))).thenReturn(updatedRoom);
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(URI + "/" + ROOM_ID).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
			Mockito.verify(projectService, Mockito.times(1)).findById(Mockito.anyLong());
			Mockito.verify(roomService, Mockito.times(1)).updateRoom(Mockito.any(Room.class));
			
			validateHttpStatus(HttpStatus.OK, mvcResult);
			String content = mvcResult.getResponse().getContentAsString();
			String outputJson = mapToJson(updatedRoom);
			assertEquals(outputJson, content);
		} catch (JsonProcessingException e) {
			fail(e.getMessage());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testDeleteRoom() {
		Project project = new Project(PROJECT_NAME);
		project.setProjectid(PROJECT_ID);
		Room roomToDelete = new Room(ROOM_NAME, ROOM_LABEL);
		roomToDelete.setRoomid(ROOM_ID);
		roomToDelete.setProject(project);
		
		try {
			Mockito.when(projectService.findById(Mockito.anyLong())).thenReturn(project);
			Mockito.when(roomService.findById(Mockito.anyLong())).thenReturn(roomToDelete);
			Mockito.when(roomService.delete(Mockito.any(Room.class))).thenReturn(true);
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/" + ROOM_ID)).andReturn();
			Mockito.verify(projectService, Mockito.times(1)).findById(Mockito.anyLong());
			Mockito.verify(roomService, Mockito.times(1)).findById(Mockito.anyLong());
			Mockito.verify(roomService, Mockito.times(1)).delete(Mockito.any(Room.class));
			
			validateHttpStatus(HttpStatus.OK, mvcResult);
			String content = mvcResult.getResponse().getContentAsString();
			String outputJson = mapToJson(roomToDelete);
			assertEquals(outputJson, content);
		} catch (JsonProcessingException e) {
			fail(e.getMessage());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
