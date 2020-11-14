package de.devtom.java.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.devtom.java.entities.Project;
import de.devtom.java.repositories.ProjectRepository;

@Service
public class ProjectService extends AbstractService<Project> {
	@Autowired
	private ProjectRepository projectRepository;
	
	public List<Project> list() {
		return projectRepository.findAll();
	}
	
	public Project createProject(Project project) {
		return projectRepository.save(project);
	}
	
	public Project updateProject(Project project) {
		return projectRepository.findById(project.getProjectid()).map(p -> {
			p.setName(project.getName());
			return projectRepository.save(p);
		}).orElseThrow(() -> new EntityNotFoundException(String.format("Project ID [%d] not found", project.getProjectid())));
	}
	
	public Project findById(Long projectid) {
		Optional<Project> project = projectRepository.findById(projectid);
		return handleOptional(project, String.format("Project ID [%d] not found", projectid));
	}
	
	public Project findByName(String name) {
		Optional<Project> project = projectRepository.findByName(name);
		return handleOptional(project, String.format("Project with name [%s] not found", name));
	}
	
	public boolean delete(Project project) {
		return projectRepository.findById(project.getProjectid()).map(p -> {
			projectRepository.delete(p);
			return true;
		}).orElseThrow(() -> new EntityNotFoundException(String.format("Project ID [%d] not found", project.getProjectid())));
	}
	
	public long getNumberOfEntities() {
		return projectRepository.count();
	}
}
