package com.stee.spfcore.webapi.model.calendar;

import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import com.stee.spfcore.webapi.model.Module;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
@Entity
@Table(name = "CALENDAR_MARKETING_EVENTS", schema = "SPFCORE")
@XStreamAlias("MarketingEvent")
@Audited
public class MarketingEvent {

	@Id
	@GenericGenerator(name = "MarketingEventIdGenerator", strategy = "com.stee.spfcore.webapi.dao.utils.StringIdHibernateGenerator")
	@GeneratedValue(generator = "MarketingEventIdGenerator")
	@Column(name = "\"ID\"")
	@XStreamOmitField
	private String id;
	
	@Column( name = "\"NRIC\"", length = 10 )
  private String nric;
	
	@Column( name = "\"NAME\"", length = 255 )
  private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "\"MODULE\"", length = 30)
  private Module module;

	@Column( name = "\"REFERENCE_ID\"", length = 255 )
	private String referenceId;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "\"START_DATE\"")
	private Date startDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "\"END_DATE\"")
	private Date endDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "\"CLOSING_DATE\"")
	private Date closingDate;
	
	@Column(name = "\"IS_REGISTERED\"")
	private boolean registered;
	
	@Column(name = "\"EXCLUDE_WEEKEND\"")
	private boolean excludeWeekend;
	
	@ElementCollection( fetch = FetchType.EAGER )
  @CollectionTable( name = "CALENDAR_MARKETING_EVENT_EXCLUDE_DATES", schema = "SPFCORE", joinColumns = @JoinColumn( name = "EXCLUDE_DATE_EVENT_ID" ) )
  @Temporal( TemporalType.DATE )
  @Column( name = "EXCLUDE_DATE" )
  @Fetch( value = FetchMode.SELECT )
	private List<Date> excludeDates;

	public MarketingEvent() {
		super();
	}

	public MarketingEvent(String id, String nric, String name, Module module, String referenceId, Date startDate,
			Date endDate, Date closingDate, boolean registered, boolean excludeWeekend, List<Date> excludeDates) {
		super();
		this.id = id;
		this.nric = nric;
		this.name = name;
		this.module = module;
		this.referenceId = referenceId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.closingDate = closingDate;
		this.registered = registered;
		this.excludeWeekend = excludeWeekend;
		this.excludeDates = excludeDates;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getClosingDate() {
		return closingDate;
	}

	public void setClosingDate(Date closingDate) {
		this.closingDate = closingDate;
	}

	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	public boolean isExcludeWeekend() {
		return excludeWeekend;
	}

	public void setExcludeWeekend(boolean excludeWeekend) {
		this.excludeWeekend = excludeWeekend;
	}

	public List<Date> getExcludeDates() {
		return excludeDates;
	}

	public void setExcludeDates(List<Date> excludeDates) {
		this.excludeDates = excludeDates;
	}
}
