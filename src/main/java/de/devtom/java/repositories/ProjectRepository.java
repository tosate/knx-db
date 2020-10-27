package de.devtom.java.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.devtom.java.entities.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
	Optional<Project> findById(Long projectid);
	Optional<Project> findByName(String name);
}
