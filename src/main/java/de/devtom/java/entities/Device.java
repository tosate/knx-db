package de.devtom.java.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

import io.swagger.annotations.ApiModelProperty;

@Entity(name = "Device")
@Table(name = "device")
public class Device {
	public static final String TYPE_LIGHTBULB = "Lightbulb";
	public static final String TYPE_DIMMER = "Dimmer";
	public static final String TYPE_CONTACT_SENSOR = "ContactSensor";
	public static final String TYPE_JALOUSIE = "Jalousie";
	public static final String TYPE_MOTION_SENSOR = "MotionSensor";
	public static final String TYPE_POWER_OUTLET = "PowerOutlet";
	public static final String TYPE_ROLLERSHUTTER = "Rollershutter";
	public static final String TYPE_THERMOSTAT = "Thermostat";
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
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "deviceroom")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Room room;
	
	protected Device() {
		
	}
	
	public Device(String label, String deviceType) {
		this.label = label;
		this.deviceType = deviceType;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
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

	public String getNameAffix() {
		return nameAffix;
	}

	public void setNameAffix(String nameAffix) {
		this.nameAffix = nameAffix;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}
}
