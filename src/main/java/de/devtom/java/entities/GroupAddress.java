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

import de.devtom.java.utils.ServiceUtils;
import io.swagger.annotations.ApiModelProperty;

@Entity(name = "GroupAddress")
@Table(name = "groupaddress")
public class GroupAddress {
	@Id
	@GeneratedValue(generator = "group_address_id_generator")
	@TableGenerator(name="group_address_id_generator", table="sqlite_sequence",
			pkColumnName="name", valueColumnName="seq",
			pkColumnValue="groupaddress", allocationSize = 1)
	@Column(name = "groupaddressid")
	private Long groupAddressId;
	@Column(name = "maingroup")
	@NotNull
	@ApiModelProperty("KNX main group")
	private int mainGroup;
	@Column(name = "middlegroup")
	@NotNull
	@ApiModelProperty(value = "KNX middle group")
	private int middleGroup;
	@Column(name = "subgroup")
	@NotNull
	@ApiModelProperty(value = "KNX sub group")
	private int subGroup;
	@Column(name = "datatype")
	@ApiModelProperty(value = "KNX data type")
	private String dataType;
	@ApiModelProperty(value = "Function description of this address")
	private String function;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "groupaddressdevice")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Device device;
	
	protected GroupAddress() {
		
	}
	
	public GroupAddress(int mainGroup, int middleGroup, int subGroup) {
		this.mainGroup = mainGroup;
		this.middleGroup = middleGroup;
		this.subGroup = subGroup;
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

	@Override
	public String toString() {
		return String.format("%d/%d/%d", this.mainGroup, this.middleGroup, this.subGroup);
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public void setMainGroup(int mainGroup) {
		this.mainGroup = mainGroup;
	}

	public void setMiddleGroup(int middleGroup) {
		this.middleGroup = middleGroup;
	}

	public void setSubGroup(int subGroup) {
		this.subGroup = subGroup;
	}
}
