package de.devtom.java.services;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.devtom.java.entities.Project;

@SpringBootTest
public class ProjectServiceUnitTest {
	@Autowired
	private ProjectService projectService;
	
	@Test
	public void testList() {
		List<Project> projects = projectService.list();
		
		assertEquals(1, projects.size());
	}
	
	@Test
	public void testCreateUpdateDelete() {
		Project project = new Project("name");
		
		// Create
		int count = projectService.list().size();
		Project savedProject = projectService.save(project);;
		assertEquals(count + 1, projectService.list().size());
		assertEquals(project.getName(), savedProject.getName());
		
		// Update
		savedProject.setName("alteredName");
		Project updatedProject = projectService.save(savedProject);
		assertEquals(savedProject.getProjectid(), updatedProject.getProjectid());
		assertEquals(savedProject.getName(), updatedProject.getName());
		
		// Delete
		projectService.delete(savedProject);
		assertEquals(count, projectService.list().size());
	}
}
