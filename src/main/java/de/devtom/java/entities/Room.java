package de.devtom.java.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Room {
	@Id
	private Long roomid;
	private String name;
	private String label;
	private String floor;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="roomproject")
	private Project project;
	@OneToMany(mappedBy="room")
	private List<Device> devices;
}
