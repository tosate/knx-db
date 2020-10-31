package de.devtom.java.rest.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
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
import de.devtom.java.services.ProjectService;
import static de.devtom.java.config.KnxDbApplicationConfiguration.BASE_PATH;

@ContextConfiguration(classes = {ProjectController.class})
public class ProjectControllerUnitTest extends AbstractControllerUnitTest {
	private static final String URI = BASE_PATH + "/projects";
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
	
	@Test
	public void testUpdateProject() {
		long projectid = 1l;
		Project project = new Project("name");
		project.setProjectid(projectid);
		
		try {
			String inputJson = mapToJson(project);
			Mockito.when(projectService.findById(Mockito.anyLong())).thenReturn(Optional.of(project));
			Mockito.when(projectService.save(Mockito.any(Project.class))).thenReturn(project);
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(URI + "/" + projectid).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
			Mockito.verify(projectService, Mockito.times(1)).findById(Mockito.anyLong());
			Mockito.verify(projectService, Mockito.times(1)).save(Mockito.any(Project.class));
			
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
			Mockito.when(projectService.findById(Mockito.anyLong())).thenReturn(Optional.of(project));
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
