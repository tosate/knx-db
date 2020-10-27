package de.devtom.java.services;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import de.devtom.java.entities.Project;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectServiceUnitTest {
	@Autowired
	private ProjectService projectService;
	
	@Test
	public void whenApplicationStarts_thenHibernateCreatesInitialRecords() {
		List<Project> projects = projectService.list();
		
		Assert.assertEquals(projects.size(), 1);
	}
}
