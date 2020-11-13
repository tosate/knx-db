package de.devtom.java.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.devtom.java.entities.Project;
import de.devtom.java.repositories.ProjectRepository;

@Service
public class ProjectService {
	@Autowired
	private ProjectRepository projectRepository;
	
	public List<Project> list() {
		return projectRepository.findAll();
	}
	
	public Project save(Project project) {
		return projectRepository.save(project);
	}
	
	public Optional<Project> findById(Long projectid) {
		return projectRepository.findById(projectid);
	}
	
	public Optional<Project> findByName(String name) {
		return projectRepository.findByName(name);
	}
	
	public void delete(Project project) {
		projectRepository.deleteById(project.getProjectid());
	}
	
	public long getNumberOfEntities() {
		return projectRepository.count();
	}
}
