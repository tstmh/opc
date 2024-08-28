package com.stee.spfcore.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table( name = "Event_Location_Session_Config", schema = "SPFCORE" )
@XStreamAlias( "EventLocationSessionConfig" )
@Audited
public class EventLocationSessionConfig {
	@Id
    @GenericGenerator( name = "EventLocationSessionConfigGenerator", strategy = "com.stee.spfcore.dao.utils.StringIdHibernateGenerator" )
    @GeneratedValue( generator = "EventLocationSessionConfigGenerator" )
    @Column( name = "ID" )
    @XStreamOmitField
    private String id;
	
	@Column( name = "LOCATION", length = 32 )
	private String location;
	
	@Column( name = "NUM_SESSIONS" )
	private int numberOfSessions;
	
	@OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true )
    @JoinTable( name = "HHS_EVENT_LOCATION_DEPARTMENT_SESSION_CONFIG_JOINTS", schema = "SPFCORE", joinColumns = @JoinColumn( name = "EVENT_LOCATION_SESSION_CONFIG_ID" ), inverseJoinColumns = @JoinColumn( name = "EVENT_LOCATION_DEPARTMENT_SESSION_CONFIG_ID" ) )
	private List< EventLocationDepartmentSessionConfig > eventLocationDepartmentSessionConfigList;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getNumberOfSessions() {
		return numberOfSessions;
	}

	public void setNumberOfSessions(int numberOfSessions) {
		this.numberOfSessions = numberOfSessions;
	}

	public List<EventLocationDepartmentSessionConfig> getEventLocationDepartmentSessionConfigList() {
		return eventLocationDepartmentSessionConfigList;
	}

	public void setEventLocationDepartmentSessionConfigList(
			List<EventLocationDepartmentSessionConfig> eventLocationDepartmentSessionConfigList) {
		 if ( this.eventLocationDepartmentSessionConfigList == null ) {
             this.eventLocationDepartmentSessionConfigList = new ArrayList<>();
         }
         this.eventLocationDepartmentSessionConfigList.clear();
         if ( eventLocationDepartmentSessionConfigList != null ) {
             this.eventLocationDepartmentSessionConfigList.addAll( eventLocationDepartmentSessionConfigList );
         }
	}

	@Override
	public String toString() {
		return "EventLocationSessionConfig [id=" + id + ", location="
				+ location + ", numberOfSessions=" + numberOfSessions
				+ ", eventLocationDepartmentSessionConfigList="
				+ eventLocationDepartmentSessionConfigList + "]";
	}

}
