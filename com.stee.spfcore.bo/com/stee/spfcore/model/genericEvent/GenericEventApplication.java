package com.stee.spfcore.model.genericEvent;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Formula;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.stee.spfcore.model.annotation.GeneratedId;
import com.stee.spfcore.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "GENERIC_EVENT_APPLICATIONS", schema = "SPFCORE")
@XStreamAlias("GenericEventApplication")
@Audited
@SequenceDef (name="GenericEventApplicationId_SEQ", schema = "SPFCORE", internetFormat="FEB-EA-%d", intranetFormat="BPM-EA-%d")
public class GenericEventApplication {
	
	@Id
	@GeneratedId
	@Column(name = "\"ID\"")
	private String id;
	
	@Column( name = "\"NRIC\"", length = 10 )
  private String nric;
	
	@Column(name = "\"EVENT_ID\"")
	private String eventId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"REGISTERED_ON\"")
	private Date registeredOn;
	
	@Formula("(select c.NAME from SPFCORE.PERSONAL_DETAILS c where c.NRIC = NRIC)")
	@NotAudited
	private String name;
	
	@Formula("(select c.DESCRIPTION from SPFCORE.CODES c, SPFCORE.PERSONAL_EMPLOYMENT_DETAILS e where "
			+ "c.ID = e.ORGANISATION_OR_DEPARTMENT and c.CODE_TYPE = 'UNIT_DEPARTMENT' and e.NRIC = NRIC)")
	@NotAudited
	private String department;
	
	@Formula("(select c.DESCRIPTION from SPFCORE.CODES c, SPFCORE.PERSONAL_EMPLOYMENT_DETAILS e where "
			+ "c.ID = e.SERVICE_TYPE and c.CODE_TYPE = 'SERVICE_TYPE' and e.NRIC = NRIC)")
	@NotAudited
	private String serviceType;
	
	@Column(name="\"UPDATED_BY\"", length=256)
	private String updatedBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name="\"UPDATED_DATE\"")
	private Date updatedOn;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "\"STATUS\"", length = 15)
	private GEApplicationStatus status;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "\"APPLICATION_ID\"")
	@Fetch(value = FetchMode.SELECT)
	@OrderColumn(name = "\"LIST_ORDER\"")
	private List<GETicketingChoice> choices;
	
	@Column(name = "\"DIETARY_PREFERENCE\"", length = 100)
	private String dietaryPreference;
	
	@Column(name = "\"BRANCH\"", length = 100)
	private String branch;

	public GenericEventApplication() {
		super();
	}

	public GenericEventApplication(String id, String nric, String eventId, Date registeredOn, 
				String updatedBy, Date updatedOn, GEApplicationStatus status, List<GETicketingChoice> choices) {
		super();
		this.id = id;
		this.nric = nric;
		this.eventId = eventId;
		this.registeredOn = registeredOn;
		this.updatedBy = updatedBy;
		this.updatedOn = updatedOn;
		this.status = status;
		this.choices = choices;
	}
	
	public GenericEventApplication(String id, String nric, String eventId,
			Date registeredOn, String updatedBy, Date updatedOn,
			GEApplicationStatus status, List<GETicketingChoice> choices,
			String dietaryPreference, String branch) {
		super();
		this.id = id;
		this.nric = nric;
		this.eventId = eventId;
		this.registeredOn = registeredOn;
		this.updatedBy = updatedBy;
		this.updatedOn = updatedOn;
		this.status = status;
		this.choices = choices;
		this.dietaryPreference = dietaryPreference;
		this.branch = branch;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNric() {
		return nric;
	}

	public void setNric(String nric) {
		this.nric = nric;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public Date getRegisteredOn() {
		return registeredOn;
	}

	public void setRegisteredOn(Date registeredOn) {
		this.registeredOn = registeredOn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public GEApplicationStatus getStatus() {
		return status;
	}

	public void setStatus(GEApplicationStatus status) {
		this.status = status;
	}

	public List<GETicketingChoice> getChoices() {
		return choices;
	}

	public void setChoices(List<GETicketingChoice> choices) {
		this.choices = choices;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getDietaryPreference() {
		return dietaryPreference;
	}

	public void setDietaryPreference(String dietaryPreference) {
		this.dietaryPreference = dietaryPreference;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}
	

}
