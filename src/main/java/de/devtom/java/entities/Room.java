package de.devtom.java.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

@Entity
public class Room {
	@Id
	@GeneratedValue(generator = "sqlite")
	@TableGenerator(name = "sqlite", table="sqlite_sequence",
			pkColumnName="name", valueColumnName="seq",
			pkColumnValue="room")
	private Long roomid;
	private String name;
	private String label;
	private String floor;
	@ManyToOne
	@JoinColumn(name="roomproject")
	private Project project;
	@OneToMany(fetch = FetchType.EAGER, mappedBy="room", cascade = CascadeType.ALL)
	private List<Device> devices;
	
	public Room() {
		
	}
	
	public Room(String name, String label) {
		this.name = name;
		this.label = label;
		this.devices = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}
	
	public void addDevice(Device device) {
		this.devices.add(device);
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Project getProject() {
		return project;
	}
}
