package de.devtom.java.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class GroupAddress {
	@Id
	private Long groupAddressId;
	private int mainGroup;
	private int middleGroup;
	private int subGroup;
	private String dataType;
	private String function;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="addressdevice")
	private Device device;
}
