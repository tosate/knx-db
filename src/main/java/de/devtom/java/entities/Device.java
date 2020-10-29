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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Device {
	@Id
	@GeneratedValue(generator = "sqlite")
	@TableGenerator(name = "sqlite", table="sqlite_sequence",
			pkColumnName="name", valueColumnName="seq",
			pkColumnValue="device")
	private Long deviceid;
	private String label;
	@ManyToOne
	@JoinColumn(name="devicetype")
	private Type deviceType;
	@ManyToOne
	@JoinColumn(name="deviceroom")
	@JsonIgnore
	private Room room;
	@OneToMany(fetch = FetchType.EAGER, mappedBy="device", cascade = CascadeType.ALL)
	private List<GroupAddress> groupAddresses;
	
	public Device() {
		
	}
	
	public Device(String label) {
		this.label = label;
		this.groupAddresses = new ArrayList<>();
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<GroupAddress> getGroupAddresses() {
		return groupAddresses;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Room getRoom() {
		return room;
	}

	public void setDeviceType(Type deviceType) {
		this.deviceType = deviceType;
	}

	public Long getDeviceid() {
		return deviceid;
	}
}
