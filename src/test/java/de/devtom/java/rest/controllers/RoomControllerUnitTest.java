package de.devtom.java.rest.controllers;

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

import de.devtom.java.entities.Project;
import de.devtom.java.entities.Room;
import de.devtom.java.services.ProjectService;
import de.devtom.java.services.RoomService;

@ContextConfiguration(classes = {RoomController.class})
public class RoomControllerUnitTest extends AbstractControllerUnitTest {
	@MockBean
	private ProjectService projectService;
	@MockBean
	private RoomService roomSevice;
	
	@Test
	public void testCreateRoom() {
		Long projectid = 1l;
		Project project = new Project("project-name");
		project.setProjectid(projectid);
		Room room = new Room("room-name", "room-label");
		Room savedRoom = new Room("room-name", "room-label");
		savedRoom.setRoomid(1l);
		savedRoom.setProject(project);
		
		try {
			String inputJson = mapToJson(room);
			Mockito.when(projectService.findById(Mockito.anyLong())).thenReturn(Optional.of(project));
			Mockito.when(roomSevice.save(Mockito.any(Project.class), Mockito.any(Room.class))).thenReturn(savedRoom);
			String uri = BASE_PATH + "/project/" + projectid + "/room";
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
			Mockito.verify(projectService, Mockito.times(1)).findById(Mockito.anyLong());
			Mockito.verify(roomSevice, Mockito.times(1)).save(Mockito.any(Project.class), Mockito.any(Room.class));
			
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
	public void testCreateRoomAlreadyExisting() {
		Long projectid = 1l;
		Project project = new Project("project-name");
		project.setProjectid(projectid);
		Room room = new Room("room-name", "room-label");
		Long roomid = 1l;
		room.setRoomid(roomid);
		project.addRoom(room);
		
		try {
			String inputJson = mapToJson(new Room("room-name", "room-label"));
			Mockito.when(projectService.findById(Mockito.anyLong())).thenReturn(Optional.of(project));
			String uri = BASE_PATH + "/project/" + projectid + "/room";
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
			Mockito.verify(projectService, Mockito.times(1)).findById(Mockito.anyLong());
			
			validateHttpStatus(HttpStatus.BAD_REQUEST, mvcResult);
			
		} catch (JsonProcessingException e) {
			fail(e.getMessage());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
