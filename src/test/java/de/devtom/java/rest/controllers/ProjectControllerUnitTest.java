package de.devtom.java.rest.controllers;

import static de.devtom.java.config.KnxDbApplicationConfiguration.BASE_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

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

@ContextConfiguration(classes = {ProjectController.class})
public class ProjectControllerUnitTest extends AbstractControllerUnitTest {
	private static final String URI = BASE_PATH + "/projects";
	@MockBean
	private ProjectService projectService;
	@MockBean
	private RoomService roomService;
	@MockBean
	private DeviceService deviceService;
	@MockBean
	private GroupAddressService groupAddressService;
	
	@Test
	public void testCreateProject() {
		Project project = new Project("name");
		Project savedResult = new Project("name");
		savedResult.setProjectid(1l);
		
		try {
			String inputJson = mapToJson(project);
			Mockito.when(projectService.createProject(Mockito.any(Project.class))).thenReturn(savedResult);
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(URI).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
			Mockito.verify(projectService, Mockito.times(1)).createProject(Mockito.any(Project.class));
						
			validateHttpStatus(HttpStatus.CREATED, mvcResult);
			String content = mvcResult.getResponse().getContentAsString();
			String outputJson = mapToJson(savedResult);
			assertEquals(outputJson, content);
		} catch (JsonProcessingException e) {
			fail(e.getMessage());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testCreateProjectEmptyName() {
		try {
			String inputJson = mapToJson(new Project(""));
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(URI).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

			validateHttpStatus(HttpStatus.BAD_REQUEST, mvcResult);
		} catch (JsonProcessingException e) {
			fail(e.getMessage());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetProject() {
		long projectid = 1l;
		Project project = new Project("name");
		project.setProjectid(projectid);
		
		try {
			Mockito.when(projectService.findById(Mockito.anyLong())).thenReturn(project);
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(URI + "/" + projectid)).andReturn();
			Mockito.verify(projectService, Mockito.times(1)).findById(Mockito.anyLong());
			
			validateHttpStatus(HttpStatus.OK, mvcResult);
			String content = mvcResult.getResponse().getContentAsString();
			String outputJson = mapToJson(project);
			assertEquals(outputJson, content);
		} catch (JsonProcessingException e) {
			fail(e.getMessage());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetProjectList() {
		List<Project> projectList = new ArrayList<>();
		Project project1 = new Project("project1");
		project1.setProjectid(1l);
		projectList.add(project1);
		String nameProject2 = "project2";
		Project project2 = new Project(nameProject2);
		project2.setProjectid(2l);
		projectList.add(project2);
		
		try {
			Mockito.when(projectService.list()).thenReturn(projectList);
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(URI + "?name=" + nameProject2)).andReturn();
			Mockito.verify(projectService, Mockito.times(1)).list();
			
			validateHttpStatus(HttpStatus.OK, mvcResult);
			String content = mvcResult.getResponse().getContentAsString();
			projectList.remove(project1);
			String outputJson = mapToJson(projectList);
			assertEquals(outputJson, content);
		} catch (JsonProcessingException e) {
			fail(e.getMessage());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetProjectAsCsv() {
		Project project = new Project("project");
		project.setProjectid(1l);
		List<Room> rooms = new ArrayList<>();
		Room room = new Room("room", "Room Label");
		room.setRoomid(1l);
		room.setFloor("GF");
		room.setProject(project);
		rooms.add(room);
		List<Device> devices = new ArrayList<>();
		Device device1 = new Device("Device1 label", Device.TYPE_LIGHTBULB);
		device1.setDeviceid(1l);
		device1.setNameAffix("affix");
		device1.setRoom(room);
		devices.add(device1);
		List<GroupAddress> groupAddresses = new ArrayList<>();
		GroupAddress ga1 = new GroupAddress(1, 1, 1);
		ga1.setGroupAddressId(1l);
		ga1.setDevice(device1);
		groupAddresses.add(ga1);
		GroupAddress ga2 = new GroupAddress(1, 4, 1);
		ga2.setGroupAddressId(2l);
		ga2.setDevice(device1);
		groupAddresses.add(ga2);
		Device device2 = new Device("Device2 label", Device.TYPE_LIGHTBULB);
		device2.setDeviceid(2l);
		device2.setRoom(room);
		devices.add(device2);
		GroupAddress ga3 = new GroupAddress(1, 1, 2);
		ga3.setGroupAddressId(3l);
		ga3.setDevice(device2);
		groupAddresses.add(ga3);
		GroupAddress ga4 = new GroupAddress(1, 4, 2);
		ga4.setGroupAddressId(4l);
		ga4.setDevice(device2);
		groupAddresses.add(ga4);
		
		try {
			Mockito.when(projectService.findById(Mockito.anyLong())).thenReturn(project);
			Mockito.when(roomService.findByProjectId(Mockito.anyLong())).thenReturn(rooms);
			Mockito.when(deviceService.findByRoomId(Mockito.anyLong())).thenReturn(devices);
			Mockito.when(groupAddressService.findByDeviceId(Mockito.anyLong())).thenReturn(groupAddresses);
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(URI + "/1?format=HomeAssistant").accept("text/comma-separated-values")).andReturn();
			Mockito.verify(projectService, Mockito.times(1)).findById(Mockito.anyLong());
			Mockito.verify(roomService, Mockito.times(1)).findByProjectId(Mockito.anyLong());
			Mockito.verify(deviceService, Mockito.times(1)).findByRoomId(Mockito.anyLong());
			Mockito.verify(groupAddressService, Mockito.times(2)).findByDeviceId(Mockito.anyLong());
			
			validateHttpStatus(HttpStatus.OK, mvcResult);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetProjectNotExistent() {
		try {
			Mockito.when(projectService.findById(Mockito.anyLong())).thenThrow(EntityNotFoundException.class);
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(URI + "/" + 1l)).andReturn();
			Mockito.verify(projectService, Mockito.times(1)).findById(Mockito.anyLong());
			
			validateHttpStatus(HttpStatus.NOT_FOUND, mvcResult);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testUpdateProject() {
		long projectid = 1l;
		Project project = new Project("name");
		project.setProjectid(projectid);
		
		try {
			String inputJson = mapToJson(project);
			Mockito.when(projectService.findById(Mockito.anyLong())).thenReturn(project);
			Mockito.when(projectService.createProject(Mockito.any(Project.class))).thenReturn(project);
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(URI + "/" + projectid).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
			Mockito.verify(projectService, Mockito.times(1)).findById(Mockito.anyLong());
			Mockito.verify(projectService, Mockito.times(1)).createProject(Mockito.any(Project.class));
			
			validateHttpStatus(HttpStatus.OK, mvcResult);
			String content = mvcResult.getResponse().getContentAsString();
			String outputJson = mapToJson(project);
			assertEquals(outputJson, content);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testDeleteProject() {
		long projectid = 1l;
		Project project = new Project("name");
		project.setProjectid(projectid);
		
		try {
			Mockito.when(projectService.findById(Mockito.anyLong())).thenReturn(project);
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/" + projectid)).andReturn();
			Mockito.verify(projectService, Mockito.times(1)).findById(Mockito.anyLong());
			Mockito.verify(projectService, Mockito.times(1)).delete(Mockito.any(Project.class));
			
			validateHttpStatus(HttpStatus.OK, mvcResult);
			String content = mvcResult.getResponse().getContentAsString();
			String outputJson = mapToJson(project);
			assertEquals(outputJson, content);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
