package de.devtom.java.services;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.devtom.java.entities.Project;

@SpringBootTest
public class ProjectServiceUnitTest {
	private static final String PROJECT_NAME = "name";
	@Autowired
	private ProjectService projectService;
	
	@Test
	public void testList() {
		List<Project> projects = projectService.list();
		
		assertEquals(1, projects.size());
	}
	
	@Test
	public void testCreateUpdateDelete() {
		
		Project project = new Project(PROJECT_NAME);

		// Create
		long count = projectService.getNumberOfEntities();
		project = projectService.createProject(project);
		assertEquals(count + 1, projectService.getNumberOfEntities());
		assertEquals(PROJECT_NAME, project.getName());
		
		// Update
		String alteredProjectName = "alteredName";
		project.setName(alteredProjectName);
		project = projectService.createProject(project);
		assertEquals(alteredProjectName, project.getName());
		
		// Delete
		projectService.delete(project);
		assertEquals(count, projectService.getNumberOfEntities());
	}
}
