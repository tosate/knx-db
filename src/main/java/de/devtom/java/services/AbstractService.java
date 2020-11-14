package de.devtom.java.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

public abstract class AbstractService<T> {
	protected T handleOptional(Optional<T> entity, String errorMessage) {
		if(entity.isPresent()) {
			return entity.get();
		} else {
			throw new EntityNotFoundException(errorMessage);
		}
	}
}
