package com.stee.spfcore.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Table( name = "Event_Location_Department_Session_Config", schema = "SPFCORE" )
@XStreamAlias( "EventLocationDepartmentSessionConfig" )
@Audited
public class EventLocationDepartmentSessionConfig {
	@Id
    @GenericGenerator( name = "EventLocationDepartmentSessionGenerator", strategy = "com.stee.spfcore.dao.utils.StringIdHibernateGenerator" )
    @GeneratedValue( generator = "EventLocationDepartmentSessionGenerator" )
    @Column( name = "ID" )
    @XStreamOmitField
    private String id;
	
	@Column( name = "DEPARTMENT" )
	private String department;

	@Column( name = "NUM_MEMBERS" )
	private int numberOfMembers;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public int getNumberOfMembers() {
		return numberOfMembers;
	}

	public void setNumberOfMembers(int numberOfMembers) {
		this.numberOfMembers = numberOfMembers;
	}

	@Override
	public String toString() {
		return "EventLocationDepartmentSessionConfig [id=" + id
				+ ", department=" + department + ", numberOfMembers="
				+ numberOfMembers + "]";
	}
	
	
}
