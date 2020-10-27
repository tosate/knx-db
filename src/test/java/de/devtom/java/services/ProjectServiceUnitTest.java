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
	public void whenApplicationStarts_thenHibernateCreatesInitialRecords() {
		List<Project> projects = projectService.list();
		
		assertEquals(projects.size(), 1);
	}
}
