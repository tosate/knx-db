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

import de.devtom.java.utils.ServiceUtils;
import io.swagger.annotations.ApiModelProperty;

@Entity(name = "Room")
@Table(name = "room")
public class Room {
	@Id
	@GeneratedValue(generator = "room_id_generator")
	@TableGenerator(name = "room_id_generator", table="sqlite_sequence",
			pkColumnName="name", valueColumnName="seq",
			pkColumnValue="room", allocationSize = 1)
	private Long roomid;
	@NotNull
	@ApiModelProperty(value = "Room name")
	private String name;
	@NotNull
	@ApiModelProperty(value = "Room label")
	private String label;
	private String floor;
	@OneToMany(targetEntity = Device.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "deviceroom", referencedColumnName = "roomid")
	private List<Device> devices;
	
	protected Room() {
		
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

	public Long getRoomid() {
		return roomid;
	}

	public void setRoomid(Long roomid) {
		this.roomid = roomid;
	}

	public void deleteDevice(Device device) {
		this.devices.remove(device);
	}

	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	@Override
	public boolean equals(Object obj) {
		// If the object is compared with itself then return true   
        if (obj == this) { 
            return true; 
        } 
  
        /* Check if o is an instance of Complex or not 
          "null instanceof [type]" also returns false */
        if (!(obj instanceof Room)) { 
            return false; 
        } 
          
        // typecast o to GroupAddress so that we can compare data members
        Room c = (Room)obj;
        
        if(!this.getName().equals(c.getName()) ) {
        	return false;
        }
        
        if(!this.getLabel().equals(c.getLabel())) {
        	return false;
        }
        
        if(!ServiceUtils.compare(this.getFloor(), c.getFloor())) {
        	return false;
        }
        
        return true;
	}
}
