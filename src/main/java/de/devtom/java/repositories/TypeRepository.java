package de.devtom.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import de.devtom.java.entities.Type;

public interface TypeRepository extends JpaRepository<Type, Long> {

}
