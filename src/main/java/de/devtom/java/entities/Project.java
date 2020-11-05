package de.devtom.java.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.sun.istack.NotNull;

import io.swagger.annotations.ApiModelProperty;

@Entity(name = "Project")
@Table(name = "project")
public class Project {
	@Id
	@GeneratedValue(generator = "project_id_generator")
	@TableGenerator(name="project_id_generator", table="sqlite_sequence",
			pkColumnName="name", valueColumnName="seq",
			pkColumnValue="project", allocationSize = 1)
	private Long projectid;
	@NotNull
	@ApiModelProperty(value = "Project name")
	private String name;
	@OneToMany(targetEntity = Room.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "roomproject", referencedColumnName = "projectid")
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
	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}
}
