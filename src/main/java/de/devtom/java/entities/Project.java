package de.devtom.java.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

@Entity
public class Project {
	@Id
	@GeneratedValue(generator = "sqlite")
	@TableGenerator(name="sqlite", table="sqlite_sequence",
			pkColumnName="name", valueColumnName="seq",
			pkColumnValue="project")
	private Long projectid;
	private String name;
	@OneToMany(fetch = FetchType.EAGER, mappedBy="project")
	private List<Room> rooms;
	protected Project() {
		
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
		room.setProject(this);
		this.rooms.add(room);
	}
	public void deleteRoom(Room room) {
		this.rooms.remove(room);
	}
	public Long getProjectid() {
		return projectid;
	}
	public void setProjectid(Long projectid) {
		this.projectid = projectid;
	}
	public List<Room> getRooms() {
		return rooms;
	}
}
