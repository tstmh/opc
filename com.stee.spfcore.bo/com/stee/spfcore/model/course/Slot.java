package com.stee.spfcore.model.course;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
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
import org.hibernate.envers.Audited;

import com.stee.spfcore.model.annotation.GeneratedId;
import com.stee.spfcore.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "COURSE_SLOTS", schema = "SPFCORE")
@XStreamAlias("CourseSlot")
@Audited
@SequenceDef (name="CourseSlotId_SEQ", schema = "SPFCORE", internetFormat="FEB-%d", intranetFormat="BPM-%d")
public class Slot {

	@Id
	@GeneratedId
	@Column(name = "\"ID\"")
	private String id;
	
	@Column(name = "\"SLOT_NUM\"")
	private int slotNum;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"START_DATE_TIME\"")
	private Date startDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"END_DATE_TIME\"")
	private Date endDate;
	
	@Column(name = "\"MAX_CLASS_SIZE\"")
	private int maxClassSize;
	
	@Column(name = "\"MIN_CLASS_SIZE\"")
	private int minClassSize;
	
	@Column(name = "\"VENUE\"", length=200)
	private String venue;
	
	@Column(name = "\"FULL_DAY_EVENT\"")
	private boolean fullDay;
	
	@Column(name = "\"EXCLUDE_WEEKEND\"")
	private boolean excludeWeekend;
	
	@ElementCollection( fetch = FetchType.EAGER )
  @CollectionTable( name = "COURSE_SLOTS_EXCLUDE_DATES", schema = "SPFCORE", joinColumns = @JoinColumn( name = "EXCLUDE_DATE_SLOT_ID" ) )
  @Temporal( TemporalType.DATE )
  @Column( name = "EXCLUDE_DATE" )
  @Fetch( value = FetchMode.SELECT )
	private List<Date> excludeDates;
	
	@Embedded
	private Trainer trainer;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"REMINDER_DATE\"")
	private Date reminderDate;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "\"SLOT_ID\"")
	@Fetch(value = FetchMode.SELECT)
	@OrderColumn(name = "\"LIST_ORDER\"")
	private List<ReminderAttachment> attachments; 
	
	@Column(name = "\"EMAIL_BODY\"", length = 5000)
	private String emailBody;
	
	@Column(name = "\"EMAIL_SUBJECT\"", length = 255)
	private String emailSubject;
	
	@Column(name = "\"SMS_TEXT\"", length = 255)
	private String smsText;
	
	public Slot() {
		super();
	}

	public Slot(String id, int slotNum, Date startDate, Date endDate, int maxClassSize, int minClassSize, String venue,
			boolean fullDay, boolean excludeWeekend, List<Date> excludeDates, Trainer trainer, Date reminderDate,
			List<ReminderAttachment> attachments, String emailBody, String emailSubject, String smsText) {
		super();
		this.id = id;
		this.slotNum = slotNum;
		this.startDate = startDate;
		this.endDate = endDate;
		this.maxClassSize = maxClassSize;
		this.minClassSize = minClassSize;
		this.venue = venue;
		this.fullDay = fullDay;
		this.excludeWeekend = excludeWeekend;
		this.excludeDates = excludeDates;
		this.trainer = trainer;
		this.reminderDate = reminderDate;
		this.attachments = attachments;
		this.emailBody = emailBody;
		this.emailSubject = emailSubject;
		this.smsText = smsText;

	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public int getSlotNum() {
		return slotNum;
	}


	public void setSlotNum(int slotNum) {
		this.slotNum = slotNum;
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


	public int getMaxClassSize() {
		return maxClassSize;
	}


	public void setMaxClassSize(int maxClassSize) {
		this.maxClassSize = maxClassSize;
	}


	public int getMinClassSize() {
		return minClassSize;
	}


	public void setMinClassSize(int minClassSize) {
		this.minClassSize = minClassSize;
	}


	public String getVenue() {
		return venue;
	}


	public void setVenue(String venue) {
		this.venue = venue;
	}


	public boolean isFullDay() {
		return fullDay;
	}


	public void setFullDay(boolean fullDay) {
		this.fullDay = fullDay;
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


	public Trainer getTrainer() {
		return trainer;
	}


	public void setTrainer(Trainer trainer) {
		this.trainer = trainer;
	}


	public Date getReminderDate() {
		return reminderDate;
	}


	public void setReminderDate(Date reminderDate) {
		this.reminderDate = reminderDate;
	}


	public List<ReminderAttachment> getAttachments() {
		return attachments;
	}


	public void setAttachments(List<ReminderAttachment> attachments) {
		this.attachments = attachments;
	}


	public String getEmailBody() {
		return emailBody;
	}


	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}


	public String getEmailSubject() {
		return emailSubject;
	}


	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}


	public String getSmsText() {
		return smsText;
	}


	public void setSmsText(String smsText) {
		this.smsText = smsText;
	}
	
	
	
}
