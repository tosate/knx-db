package de.devtom.java.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Project {
	@Id
	private Long projectid;
	private String name;
	@OneToMany(mappedBy="project")
	private List<Room> rooms;
	public Project() {
		
	}
	public Project(String name) {
		this.name = name;
		this.rooms = new ArrayList<>();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void addRoom(Room room) {
		this.rooms.add(room);
	}
	public Long getProjectid() {
		return projectid;
	}
	public void setProjectid(Long projectid) {
		this.projectid = projectid;
	}
}
