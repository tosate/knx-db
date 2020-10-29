package de.devtom.java.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.devtom.java.utils.ServiceUtils;

@Entity
@Table(name = "groupaddress")
public class GroupAddress {
	@Id
	@GeneratedValue(generator = "sqlite")
	@TableGenerator(name="sqlite", table="sqlite_sequence",
			pkColumnName="name", valueColumnName="seq",
			pkColumnValue="groupaddress")
	@Column(name = "groupaddressid")
	private Long groupAddressId;
	@Column(name = "maingroup")
	private int mainGroup;
	@Column(name = "middlegroup")
	private int middleGroup;
	@Column(name = "subgroup")
	private int subGroup;
	@Column(name = "datatype")
	private String dataType;
	private String function;
	@ManyToOne
	@JoinColumn(name="groupaddressdevice")
	@JsonIgnore
	private Device device;
	
	protected GroupAddress() {
		
	}
	
	public GroupAddress(int mainGroup, int middleGroup, int subGroup) {
		this.mainGroup = mainGroup;
		this.middleGroup = middleGroup;
		this.subGroup = subGroup;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public Device getDevice() {
		return device;
	}

	public Long getGroupAddressId() {
		return groupAddressId;
	}

	public int getMainGroup() {
		return mainGroup;
	}

	public int getMiddleGroup() {
		return middleGroup;
	}

	public int getSubGroup() {
		return subGroup;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	@Override
	public boolean equals(Object obj) {
		// If the object is compared with itself then return true   
        if (obj == this) { 
            return true; 
        } 
  
        /* Check if o is an instance of Complex or not 
          "null instanceof [type]" also returns false */
        if (!(obj instanceof GroupAddress)) { 
            return false; 
        } 
          
        // typecast o to GroupAddress so that we can compare data members
        GroupAddress c = (GroupAddress)obj;
        
        if(this.getMainGroup() != c.getMainGroup()) {
        	return false;
        }
        if(this.getMiddleGroup() != c.getMiddleGroup()) {
        	return false;
        }
        if(this.getSubGroup() != c.getSubGroup()) {
        	return false;
        }
        if(!ServiceUtils.compare(this.getDataType(), c.getDataType())) {
        	return false;
        }
        if(!ServiceUtils.compare(this.getFunction(), c.getFunction())) {
        	return false;
        }
        
        return true;
	}

	public void setGroupAddressId(Long groupAddressId) {
		this.groupAddressId = groupAddressId;
	}
}
