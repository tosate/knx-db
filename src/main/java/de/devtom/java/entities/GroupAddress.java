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
}
