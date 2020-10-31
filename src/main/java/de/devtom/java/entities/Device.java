package de.devtom.java.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

import io.swagger.annotations.ApiModelProperty;

@Entity
public class Device {
	@Id
	@GeneratedValue(generator = "device_id_generator")
	@TableGenerator(name = "device_id_generator", table="sqlite_sequence",
			pkColumnName="name", valueColumnName="seq",
			pkColumnValue="device")
	private Long deviceid;
	@NotNull
	@ApiModelProperty(value = "Device label")
	private String label;
	@Column(name = "devicetype")
	@NotNull
	@ApiModelProperty(value = "Device type specifier")
	private String deviceType;
	@ManyToOne
	@JoinColumn(name="deviceroom")
	@JsonIgnore
	private Room room;
	@OneToMany(fetch = FetchType.EAGER, mappedBy="device")
	private List<GroupAddress> groupAddresses;
	
	protected Device() {
		
	}
	
	public Device(String label, String deviceType) {
		this.label = label;
		this.deviceType = deviceType;
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

	public Long getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(Long deviceid) {
		this.deviceid = deviceid;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public void addAddress(GroupAddress address) {
		address.setDevice(this);
		this.groupAddresses.add(address);
	}
	
	public void deleteAddress(GroupAddress address) {
		this.groupAddresses.remove(address);
	}
}
