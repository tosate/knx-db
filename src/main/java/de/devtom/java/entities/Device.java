package de.devtom.java.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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

@Entity(name = "Device")
@Table(name = "device")
public class Device {
	@Id
	@GeneratedValue(generator = "device_id_generator")
	@TableGenerator(name = "device_id_generator", table="sqlite_sequence",
			pkColumnName="name", valueColumnName="seq",
			pkColumnValue="device", allocationSize = 1)
	private Long deviceid;
	@NotNull
	@ApiModelProperty(value = "Device label")
	private String label;
	@Column(name = "nameaffix")
	@ApiModelProperty(value = "Device name affix")
	private String nameAffix;
	@Column(name = "devicetype")
	@NotNull
	@ApiModelProperty(value = "Device type specifier")
	private String deviceType;
	@OneToMany(targetEntity = GroupAddress.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "groupaddressdevice", referencedColumnName = "deviceid")
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
		this.groupAddresses.add(address);
	}
	
	public void deleteAddress(GroupAddress address) {
		this.groupAddresses.remove(address);
	}

	public void setGroupAddresses(List<GroupAddress> groupAddresses) {
		this.groupAddresses = groupAddresses;
	}

	public String getNameAffix() {
		return nameAffix;
	}

	public void setNameAffix(String nameAffix) {
		this.nameAffix = nameAffix;
	}
}
