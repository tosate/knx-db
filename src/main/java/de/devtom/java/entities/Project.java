package de.devtom.java.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.sun.istack.NotNull;

import io.swagger.annotations.ApiModelProperty;

@Entity(name = "Project")
@Table(name = "project")
public class Project {
	// https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/
	// https://www.callicoder.com/hibernate-spring-boot-jpa-one-to-many-mapping-example/
	@Id
	@GeneratedValue(generator = "project_id_generator")
	@TableGenerator(name="project_id_generator", table="sqlite_sequence",
			pkColumnName="name", valueColumnName="seq",
			pkColumnValue="project", allocationSize = 1)
	private Long projectid;
	@NotNull
	@ApiModelProperty(value = "Project name")
	private String name;

	protected Project() {

	}
	public Project(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getProjectid() {
		return projectid;
	}
	public void setProjectid(Long projectid) {
		this.projectid = projectid;
	}
}
