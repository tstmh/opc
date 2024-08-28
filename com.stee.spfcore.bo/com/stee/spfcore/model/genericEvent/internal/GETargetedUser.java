package com.stee.spfcore.model.genericEvent.internal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.stee.spfcore.model.annotation.GeneratedId;
import com.stee.spfcore.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "GENERIC_EVENT_TARGETED_USERS", schema = "SPFCORE")
@XStreamAlias("GenericEventTargetedUser")
@Audited
@SequenceDef(name = "GenericEventTargetedUserId_SEQ", schema = "SPFCORE", internetFormat = "FEB-GETU-%d", intranetFormat = "BPM-GETU-%d")
public class GETargetedUser {
	
	@Id
	@GeneratedId
	@Column(name = "\"ID\"")
	private String id;
	
	@Column(name = "\"NRIC\"", length = 10)
	private String nric;
	
	@Column(name = "\"EVENET_ID\"")
	private String eventId;
	
	public GETargetedUser () {
		super();
	}
	
	public GETargetedUser (String id, String nric, String eventId) {
		super();
		this.id = id;
		this.nric = nric;
		this.eventId = eventId;
	}
	
	public String getId () {
		return id;
	}
	
	public void setId (String id) {
		this.id = id;
	}
	
	public String getNric () {
		return nric;
	}
	
	public void setNric (String nric) {
		this.nric = nric;
	}
	
	public String getEventId () {
		return eventId;
	}
	
	public void setEventId (String eventId) {
		this.eventId = eventId;
	}
	
}
