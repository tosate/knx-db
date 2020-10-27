package de.devtom.java.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Device {
	@Id
	private Long deviceid;
	private String label;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="devicetype")
	private Type deviceType;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="deviceroom")
	private Room room;
	@OneToMany(mappedBy="device")
	private List<GroupAddress> groupAddresses;
}
