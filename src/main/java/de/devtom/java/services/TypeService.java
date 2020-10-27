package de.devtom.java.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.devtom.java.entities.Type;
import de.devtom.java.repositories.TypeRepository;

@Service
public class TypeService {
	@Autowired
	private TypeRepository typeRepository;
	
	public List<Type> list() {
		return typeRepository.findAll();
	}
}
