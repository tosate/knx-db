package de.devtom.java.rest.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.devtom.java.entities.Project;
import de.devtom.java.services.ProjectService;
import static de.devtom.java.config.KnxDbApplicationConfiguration.BASE_PATH;

@RestController
@RequestMapping(BASE_PATH)
public class ProjectController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);
	@Autowired
	private ProjectService projectService;
	
	@PostMapping(value = "/project", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Project> createProject(@RequestBody Project project) {
		ResponseEntity<Project> response = null;
		try {
			validateProjectName(project);
			Optional<Project> existingProject = projectService.findByName(project.getName());
			if(existingProject.isPresent()) {
				throw new IllegalArgumentException(String.format("Project with name [%s] already exists!", project.getName()));
			} else {
				response = new ResponseEntity<>(projectService.save(project), HttpStatus.CREATED);
			}
		} catch (IllegalArgumentException e) {
			LOGGER.error("Invalid input: {}", e.getMessage());
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return response;
	}
	
	@GetMapping(value = "/project/{projectid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Project> getProject(@PathVariable Long projectid) {
		ResponseEntity<Project> response = null;
		Optional<Project> project = projectService.findById(projectid);
		if(project.isPresent()) {
			response = new ResponseEntity<>(project.get(), HttpStatus.OK);
		} else {
			LOGGER.error("No project with ID [{}]", projectid);
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	@PutMapping(value = "/project/{projectid}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Project> updateProject(@PathVariable Long projectid, @RequestBody Project project) {
		ResponseEntity<Project> response = null;
		try {
			validateProjectName(project);
			Optional<Project> existingProject = projectService.findById(projectid);
			if(existingProject.isPresent()) {
				existingProject.get().setName(project.getName());
				response = new ResponseEntity<>(projectService.save(existingProject.get()), HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (IllegalArgumentException e) {
			LOGGER.error("Invalid input: " + e.getMessage());
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return response;
	}
	
	@DeleteMapping(value = "/project/{projectid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Project> deleteProject(@PathVariable Long projectid) {
		ResponseEntity<Project> response = null;
		Optional<Project> existingProject = projectService.findById(projectid);
		if(existingProject.isPresent()) {
			projectService.delete(existingProject.get());
			response = new ResponseEntity<Project>(existingProject.get(), HttpStatus.OK);
		} else {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	private void validateProjectName(Project project) {
		if(StringUtils.isEmpty(project.getName())) {
			throw new IllegalArgumentException("Project name cannot be empty");
		}
	}
}
