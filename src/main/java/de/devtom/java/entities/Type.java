package de.devtom.java.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Type {
	@Id
	private Long typeid;
	private String label;
}
