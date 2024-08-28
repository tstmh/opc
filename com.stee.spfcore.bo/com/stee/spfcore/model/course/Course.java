package com.stee.spfcore.model.course;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
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
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table(name = "COURSES", schema = "SPFCORE")
@XStreamAlias("Course")
@Audited
@SequenceDef (name="CourseId_SEQ", schema = "SPFCORE", internetFormat="FEB-%d", intranetFormat="BPM-%d")
public class Course implements Serializable {

	private static final long serialVersionUID = 6234931503682677592L;

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
	
	@Column(name = "\"TITLE_ID\"", length = 10)
	private String titleId;

	@Formula("(select c.NAME from SPFCORE.CATEGORY_SUBCATEGORIES c where c.ID = TITLE_ID)")
	@NotAudited
	@XStreamOmitField
	private String title;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "\"TYPE\"", length = 20)
	private CourseType type;

	@Column(name = "\"IS_EVENT\"")
	private boolean event;
	
	@Column(name = "\"CREATED_BY\"", length = 30)
	private String createdBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "\"CREATED_ON\"")
	private Date createdOn;
	
	@Column(name = "\"OUTLINE\"", length = 5000)
	private String outline;

	@Enumerated(EnumType.STRING)
	@Column(name = "\"UNIT_COST_TYPE\"", length = 15)
	private UnitCostType costUnitType;

	@Column(name = "\"UNIT_COST\"")
	private Double unitCost;

	@Column(name = "\"ORGANIZING_UNIT\"", length = 50)
	private String organizer;

	@Enumerated(EnumType.STRING)
	@Column(name = "\"STATUS\"", length = 30)
	private CourseStatus status;
	
	@Column(name = "\"VENDOR_ID\"")
	private String vendorId;
	
	@Column(name = "\"CONTENT_SET_ID\"")
	private String contentSetId;
	
	@Embedded
	private RegistrationConfig registrationConfig;

	@Temporal(TemporalType.DATE)
	@Column(name = "\"COOLING_PERIOD_END\"")
	private Date coolingPeriodEndDate;
	
	@ElementCollection( fetch = FetchType.EAGER )
	@CollectionTable( name = "COURSE_MEMBER_GROUPS", schema = "SPFCORE", joinColumns = @JoinColumn( name = "COURSE_ID" ) )
	@Column( name = "MEMBER_GROUP_ID" )
	@Fetch( value = FetchMode.SELECT )
	private List<String> memberGroupIds;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "\"COURSE_ID\"")
	@Fetch(value = FetchMode.SELECT)
	@OrderColumn(name = "\"LIST_ORDER\"")
	private List<Slot> slots;

	@Column(name = "\"SURVEY_ID\"")
	private String surveyId;
	
	@Column(name = "\"REQUIRE_CONFIRM\"")
	private boolean confirm;
	
	@Column(name = "\"TAKEOVER_DAYS\"")
	private int takeoverDays;
	
	@Column(name = "\"REMINDER_DAYS\"")
	private int reminderDays;
	
	
	public Course() {
		super();
	}

	public Course(String id, String categoryId, String titleId, CourseType type, boolean event, String createdBy,
			Date createdOn, String outline, UnitCostType costUnitType, Double unitCost, String organizer,
			CourseStatus status, String vendorId, String contentSetId, RegistrationConfig registrationConfig,
			Date coolingPeriodEndDate, List<String> memberGroupIds, List<Slot> slots, String surveyId,
			boolean confirm, int takeoverDays, int reminderDays) {
		
		super();
		this.id = id;
		this.categoryId = categoryId;
		this.titleId = titleId;
		this.type = type;
		this.event = event;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.outline = outline;
		this.costUnitType = costUnitType;
		this.unitCost = unitCost;
		this.organizer = organizer;
		this.status = status;
		this.vendorId = vendorId;
		this.contentSetId = contentSetId;
		this.registrationConfig = registrationConfig;
		this.coolingPeriodEndDate = coolingPeriodEndDate;
		this.memberGroupIds = memberGroupIds;
		this.slots = slots;
		this.surveyId = surveyId;
		this.confirm = confirm;
		this.takeoverDays = takeoverDays;
		this.reminderDays = reminderDays;
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

	public String getTitleId() {
		return titleId;
	}

	public void setTitleId(String titleId) {
		this.titleId = titleId;
	}

	public CourseType getType() {
		return type;
	}

	public void setType(CourseType type) {
		this.type = type;
	}

	public boolean isEvent() {
		return event;
	}

	public void setEvent(boolean event) {
		this.event = event;
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

	public String getOutline() {
		return outline;
	}

	public void setOutline(String outline) {
		this.outline = outline;
	}

	public UnitCostType getCostUnitType() {
		return costUnitType;
	}

	public void setCostUnitType(UnitCostType costUnitType) {
		this.costUnitType = costUnitType;
	}

	public Double getUnitCost() {
		return unitCost;
	}

	public void setUnitCost(Double unitCost) {
		this.unitCost = unitCost;
	}

	public String getOrganizer() {
		return organizer;
	}

	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}

	public CourseStatus getStatus() {
		return status;
	}

	public void setStatus(CourseStatus status) {
		this.status = status;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getContentSetId() {
		return contentSetId;
	}

	public void setContentSetId(String contentSetId) {
		this.contentSetId = contentSetId;
	}

	public RegistrationConfig getRegistrationConfig() {
		return registrationConfig;
	}

	public void setRegistrationConfig(RegistrationConfig registrationConfig) {
		this.registrationConfig = registrationConfig;
	}

	public Date getCoolingPeriodEndDate() {
		return coolingPeriodEndDate;
	}

	public void setCoolingPeriodEndDate(Date coolingPeriodEndDate) {
		this.coolingPeriodEndDate = coolingPeriodEndDate;
	}

	public List<String> getMemberGroupIds() {
		return memberGroupIds;
	}

	public void setMemberGroupIds(List<String> memberGroupIds) {
		this.memberGroupIds = memberGroupIds;
	}

	public List<Slot> getSlots() {
		return slots;
	}

	public void setSlots(List<Slot> slots) {
		this.slots = slots;
	}

	public String getCategory() {
		return category;
	}

	public String getTitle() {
		return title;
	}

	public String getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(String surveyId) {
		this.surveyId = surveyId;
	}

	public boolean isConfirm() {
		return confirm;
	}

	public void setConfirm(boolean confirm) {
		this.confirm = confirm;
	}

	public int getTakeoverDays() {
		return takeoverDays;
	}

	public void setTakeoverDays(int takeoverDays) {
		this.takeoverDays = takeoverDays;
	}

	public int getReminderDays() {
		return reminderDays;
	}

	public void setReminderDays(int reminderDays) {
		this.reminderDays = reminderDays;
	}
	
	
}
