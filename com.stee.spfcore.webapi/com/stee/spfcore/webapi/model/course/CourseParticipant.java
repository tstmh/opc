package com.stee.spfcore.webapi.model.course;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"COURSE_PARTICIPANTS\"", schema = "\"SPFCORE\"")
@XStreamAlias("CourseParticipant")
@Audited
public class CourseParticipant implements Serializable {

	private static final long serialVersionUID = 455307583516039622L;

	@Id
	@Column( name = "\"NRIC\"", length = 10 )
	private String nric;
	
	@Id
	@Column(name = "\"COURSE_ID\"")
	private String courseId;

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
	
	@Formula("(select c.DESCRIPTION from SPFCORE.CODES c, SPFCORE.PERSONAL_EMPLOYMENT_DETAILS e where "
			+ "c.ID = e.RANK_OR_GRADE and c.CODE_TYPE = 'RANK' and e.NRIC = NRIC)")
	@NotAudited
	private String rank;
	
	@Formula("(select c.DESCRIPTION from SPFCORE.CODES c, SPFCORE.PERSONAL_DETAILS e where "
			+ "c.ID = e.GENDER and c.CODE_TYPE = 'GENDERS' and e.NRIC = NRIC)")
	@NotAudited
	private String gender;
	
	@Formula("(select c.ADDRESS from SPFCORE.PERSONAL_EMAIL_DETAILS c where c.PERSONAL_NRIC = NRIC "
			+ "and c.PREFER = 1)")
	@NotAudited
	private String email;
	
	@Formula("(select c.PHONE_NUMBER from SPFCORE.PERSONAL_PHONE_DETAILS c where c.PERSONAL_NRIC = NRIC "
			+ "and c.PREFER = 1)")
	@NotAudited
	private String phoneNumber;
	
	@Formula("(select c.DATE_OF_BIRTH from SPFCORE.PERSONAL_DETAILS c where c.NRIC = NRIC)")
	@NotAudited
	private Date dateOfBirth;
	
	@Transient
	private int age;
	
	@Column(name = "\"SLOT_ID\"")
	private String slotId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "\"STATUS\"", length = 15)
	private ParticipantStatus status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"APPLIED_ON\"")
	private Date appliedOn;
	
	@Column(name = "\"DIETARY_PREFERENCE\"", length = 100)
	private String dietaryPreference;
	
	@Column(name = "\"VEHICLE_NUMBER\"", length = 10)
	private String vehicleNumber;
	
	@Column(name="\"UPDATED_BY\"", length=256)
	private String updatedBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name="\"UPDATED_DATE\"")
	private Date updatedOn;
	
	@Column(name="\"ATTENDED\"")
	private boolean attended;
	
	@Column(name = "\"WITHDRAWAL_REASON\"", length = 1000)
	private String withdrawalReason;
		
	@Column(name="\"ACCEPT_WAITLIST\"")
	private boolean acceptWaitlist;
	
	@Embedded
	private Supervisor supervisor;
	
	@Column(name="\"ATTENDING\"")
	private boolean attending;

	public CourseParticipant() {
		super();
	}

	public CourseParticipant(String nric, String courseId, String slotId,
			ParticipantStatus status, Date appliedOn, String dietaryPreference, String vehicleNumber, String updatedBy,
			Date updatedOn, boolean attended, boolean acceptWaitlist, Supervisor supervisor, boolean attending) {
		super();
		this.nric = nric;
		this.courseId = courseId;
		this.slotId = slotId;
		this.status = status;
		this.appliedOn = appliedOn;
		this.dietaryPreference = dietaryPreference;
		this.vehicleNumber = vehicleNumber;
		this.updatedBy = updatedBy;
		this.updatedOn = updatedOn;
		this.attended = attended;
		this.acceptWaitlist = acceptWaitlist;
		this.supervisor = supervisor;
		this.attending = attending;
	}

	public String getNric() {
		return nric;
	}

	public void setNric(String nric) {
		this.nric = nric;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
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

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}
	
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getSlotId() {
		return slotId;
	}

	public void setSlotId(String slotId) {
		this.slotId = slotId;
	}

	public ParticipantStatus getStatus() {
		return status;
	}

	public void setStatus(ParticipantStatus status) {
		this.status = status;
	}

	public Date getAppliedOn() {
		return appliedOn;
	}

	public void setAppliedOn(Date appliedOn) {
		this.appliedOn = appliedOn;
	}

	public String getDietaryPreference() {
		return dietaryPreference;
	}

	public void setDietaryPreference(String dietaryPreference) {
		this.dietaryPreference = dietaryPreference;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
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

	public boolean isAttended() {
		return attended;
	}

	public void setAttended(boolean attended) {
		this.attended = attended;
	}
	
	public boolean isAcceptWaitlist() {
		return acceptWaitlist;
	}

	public void setAcceptWaitlist(boolean acceptWaitlist) {
		this.acceptWaitlist = acceptWaitlist;
	}

	public Supervisor getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(Supervisor supervisor) {
		this.supervisor = supervisor;
	}

	public String getWithdrawalReason () {
		return withdrawalReason;
	}
	
	public void setWithdrawalReason (String withdrawalReason) {
		this.withdrawalReason = withdrawalReason;
	}
	
	public boolean isAttending() {
		return attending;
	}

	public void setAttending(boolean attending) {
		this.attending = attending;
	}

	@PostLoad
	protected void calculateAge() {
		Calendar today = Calendar.getInstance();
		Calendar birthDate = Calendar.getInstance();
		
		if (dateOfBirth != null) {
			birthDate.setTime(dateOfBirth);
			int todayYear = today.get(Calendar.YEAR);
			int birthDateYear = birthDate.get(Calendar.YEAR);
			int todayMonth = today.get(Calendar.MONTH);
			int birthDateMonth = birthDate.get(Calendar.MONTH);
			int todayDayOfYear = today.get(Calendar.DAY_OF_YEAR);
			int birthDateDayOfYear = birthDate.get(Calendar.DAY_OF_YEAR);
			int todayDayOfMonth = today.get(Calendar.DAY_OF_MONTH);
			int birthDateDayOfMonth = birthDate.get(Calendar.DAY_OF_MONTH);
			age = todayYear - birthDateYear;
			
			//If birth date is greater than todays date (after 2 days adjustment of leap year)
			if ((birthDateDayOfYear - todayDayOfYear > 3) || (birthDateMonth > todayMonth)) {
				age--;
			//If birth date and todays date are of same month and birth day of month is greater than todays day of month
			} else if ((birthDateMonth == todayMonth) && (birthDateDayOfMonth > todayDayOfMonth)) {
				age--;
			}
		} else {
			age = 0;
		}
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((courseId == null) ? 0 : courseId.hashCode());
		result = prime * result + ((nric == null) ? 0 : nric.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CourseParticipant other = (CourseParticipant) obj;
		if (courseId == null) {
			if (other.courseId != null)
				return false;
		}
		else if (!courseId.equals(other.courseId))
			return false;
		if (nric == null) {
			if (other.nric != null)
				return false;
		}
		else if (!nric.equals(other.nric))
			return false;
		return true;
	}
}
