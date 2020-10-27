package de.devtom.java.rest.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;

import de.devtom.java.entities.Project;
import de.devtom.java.services.ProjectService;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {ProjectController.class})
@WebMvcTest
public class ProjectControllerUnitTest extends AbstractControllerUnitTest {
	private static final String URI = "/spring-rest/project";
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ProjectService projectService;
	
	@Test
	public void testCreateProject() {
		Project project = new Project("name");
		Project savedResult = new Project("name");
		savedResult.setProjectid(1l);
		
		try {
			String inputJson = mapToJson(project);
			Mockito.when(projectService.findByName(Mockito.anyString())).thenReturn(Optional.empty());
			Mockito.when(projectService.save(Mockito.any(Project.class))).thenReturn(savedResult);
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(URI).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
			Mockito.verify(projectService, Mockito.times(1)).findByName(Mockito.anyString());
			Mockito.verify(projectService, Mockito.times(1)).save(Mockito.any(Project.class));
						
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
			String inputJson = mapToJson(new Project());
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(URI).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

			validateHttpStatus(HttpStatus.BAD_REQUEST, mvcResult);
		} catch (JsonProcessingException e) {
			fail(e.getMessage());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testCreateProjectAlreadyExisting() {
		Project project = new Project("name");
		try {
			String inputJson = mapToJson(project);
			Mockito.when(projectService.findByName(Mockito.anyString())).thenReturn(Optional.of(project));
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(URI).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
			Mockito.verify(projectService, Mockito.times(1)).findByName(Mockito.anyString());
			
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
			Mockito.when(projectService.findById(Mockito.anyLong())).thenReturn(Optional.of(project));
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
	public void testGetProjectNotExistent() {
		try {
			Mockito.when(projectService.findById(Mockito.anyLong())).thenReturn(Optional.empty());
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(URI + "/" + 1l)).andReturn();
			Mockito.verify(projectService, Mockito.times(1)).findById(Mockito.anyLong());
			
			validateHttpStatus(HttpStatus.NOT_FOUND, mvcResult);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
