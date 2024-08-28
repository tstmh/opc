package com.stee.spfcore.webapi.model.genericEvent;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Formula;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.stee.spfcore.webapi.model.annotation.GeneratedId;
import com.stee.spfcore.webapi.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table(name = "GENERIC_EVENT_DETAILS", schema = "SPFCORE")
@XStreamAlias("GenericEventDetail")
@Audited
@SequenceDef (name="GenericEventDetailId_SEQ", schema = "SPFCORE", internetFormat="FEB-ED-%d", intranetFormat="BPM-ED-%d")
public class GenericEventDetail {

	@Id
	@GeneratedId
	@Column(name = "\"ID\"")
	private String id;
	
	@Column(name = "\"CATEGORY_ID\"", length = 10)
	private String categoryId;

	@Formula("(select c.NAME from SPFCORE.CATEGORY_CATEGORIES c where c.ID = CATEGORY_ID)")
	@NotAudited
	@XStreamOmitField
	private String category;
	
	@Column(name = "\"TITLE\"", length=100)
	private String title;
	
	@Column( name = "\"DESCRIPTION\"", length = 2000 )
	private String description;
	
	@Column(name = "\"CREATED_BY\"", length = 30)
	private String createdBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "\"CREATED_ON\"")
	private Date createdOn;
	
	@Column(name = "\"ANNOUNCEMENT_ID\"")
	private String announcementId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "\"STATUS\"", length = 30)
	private GenericEventStatus status;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "\"REGISTRATION_START_DATE\"")
	private Date registrationStartDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "\"REGISTRATION_END_DATE\"")
	private Date registrationEndDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"EVENT_START_DATE\"")
	private Date eventStartDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"EVENT_END_DATE\"")
	private Date eventEndDate;
	
	@Column(name = "\"HAS_TICKETING\"")
	private boolean hasTicketing;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "\"EVENT_ID\"")
	@Fetch(value = FetchMode.SELECT)
	@OrderColumn(name = "\"LIST_ORDER\"")
	private List<GETicketingSection> ticketingSections;
	
	@Column(name = "\"VENDOR_ID\"")
	private String vendorId;
	
	@Column(name = "\"SURVEY_ID\"")
	private String surveyId;
	
	@Formula("(select ta.IS_PUBLISHED from SPFCORE.MARKETING_CONTENT_PUBLISHING_CONTENT_SET_TASKS as ta, SPFCORE.ANNOUNCEMENTS as ann " + 
			"where ta.CONTENT_SET_ID = ann.CONTENT_SET_ID and ann.ID = ANNOUNCEMENT_ID)")
	@NotAudited
	@XStreamOmitField
	private Boolean published; 
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "\"EVENT_ID\"")
	@Fetch(value = FetchMode.SELECT)
	private List<GenericEventDepartment> departments;
	
	@Column (name = "SENDER_EMAIL_ADDRESS", length = 256)
	private String senderEmailAddress;
	
	@Column (name = "SENDER_EMAIL_PASSWORD", length = 100)
	private String senderEmailPassword;
	
	@ElementCollection( fetch = FetchType.EAGER )
	@CollectionTable( name = "EVENT_DIETARY_OPTIONS", schema = "SPFCORE", joinColumns = @JoinColumn( name = "EVENT_ID" ) )
	@Column( name = "DIETARY_OPTION" )
	@Fetch( value = FetchMode.SELECT )
	private List<String> dietaryOptions;
	
	@ElementCollection( fetch = FetchType.EAGER )
	@CollectionTable( name = "EVENT_BRANCH_OPTIONS", schema = "SPFCORE", joinColumns = @JoinColumn( name = "EVENT_ID" ) )
	@Column( name = "BRANCH_OPTION" )
	@Fetch( value = FetchMode.SELECT )
	private List<String> branchOptions;
	
	@PostLoad
	protected void onLoad() {
		if (published == null) {
			published = Boolean.FALSE;
		}
	}
	
	public GenericEventDetail() {
		super();
	}

	public GenericEventDetail(String id, String categoryId, String category,
			String title, String description, String createdBy, Date createdOn,
			String announcementId, GenericEventStatus status,
			Date registrationStartDate, Date registrationEndDate,
			Date eventStartDate, Date eventEndDate, boolean hasTicketing,
			List<GETicketingSection> ticketingSections, String vendorId,
			String surveyId, Boolean published,
			List<GenericEventDepartment> departments,
			String senderEmailAddress, String senderEmailPassword, List<String>dietaryOptions, List<String>branchOptions) {
		super();
		this.id = id;
		this.categoryId = categoryId;
		this.category = category;
		this.title = title;
		this.description = description;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.announcementId = announcementId;
		this.status = status;
		this.registrationStartDate = registrationStartDate;
		this.registrationEndDate = registrationEndDate;
		this.eventStartDate = eventStartDate;
		this.eventEndDate = eventEndDate;
		this.hasTicketing = hasTicketing;
		this.ticketingSections = ticketingSections;
		this.vendorId = vendorId;
		this.surveyId = surveyId;
		this.published = published;
		this.departments = departments;
		this.senderEmailAddress = senderEmailAddress;
		this.senderEmailPassword = senderEmailPassword;
		this.dietaryOptions = dietaryOptions;
		this.branchOptions = branchOptions;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getAnnouncementId() {
		return announcementId;
	}

	public void setAnnouncementId(String announcementId) {
		this.announcementId = announcementId;
	}

	public GenericEventStatus getStatus() {
		return status;
	}

	public void setStatus(GenericEventStatus status) {
		this.status = status;
	}

	public Date getRegistrationStartDate() {
		return registrationStartDate;
	}

	public void setRegistrationStartDate(Date registrationStartDate) {
		this.registrationStartDate = registrationStartDate;
	}

	public Date getRegistrationEndDate() {
		return registrationEndDate;
	}

	public void setRegistrationEndDate(Date registrationEndDate) {
		this.registrationEndDate = registrationEndDate;
	}

	public Date getEventStartDate() {
		return eventStartDate;
	}

	public void setEventStartDate(Date eventStartDate) {
		this.eventStartDate = eventStartDate;
	}

	public Date getEventEndDate() {
		return eventEndDate;
	}

	public void setEventEndDate(Date eventEndDate) {
		this.eventEndDate = eventEndDate;
	}

	public boolean isHasTicketing() {
		return hasTicketing;
	}

	public void setHasTicketing(boolean hasTicketing) {
		this.hasTicketing = hasTicketing;
	}

	public List<GETicketingSection> getTicketingSections() {
		return ticketingSections;
	}

	public void setTicketingOptions(List<GETicketingSection> ticketingSections) {
		this.ticketingSections = ticketingSections;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(String surveyId) {
		this.surveyId = surveyId;
	}
	
	public Boolean isPublished () {
		return published;
	}

	public List<GenericEventDepartment> getDepartments() {
		return departments;
	}

	public void setDepartments(List<GenericEventDepartment> departments) {
		this.departments = departments;
	}

	public String getSenderEmailAddress() {
		return senderEmailAddress;
	}

	public void setSenderEmailAddress(String senderEmailAddress) {
		this.senderEmailAddress = senderEmailAddress;
	}

	public String getSenderEmailPassword() {
		return senderEmailPassword;
	}

	public void setSenderEmailPassword(String senderEmailPassword) {
		this.senderEmailPassword = senderEmailPassword;
	}

	public List<String> getDietaryOptions() {
		return dietaryOptions;
	}

	public void setDietaryOptions(List<String> dietaryOptions) {
		this.dietaryOptions = dietaryOptions;
	}

	public List<String> getBranchOptions() {
		return branchOptions;
	}

	public void setBranchOptions(List<String> branchOptions) {
		this.branchOptions = branchOptions;
	}
	
}
