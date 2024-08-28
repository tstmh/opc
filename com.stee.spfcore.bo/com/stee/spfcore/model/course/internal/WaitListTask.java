package com.stee.spfcore.model.course.internal;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Formula;
import org.hibernate.envers.Audited; 
import org.hibernate.envers.NotAudited;

import com.stee.spfcore.model.annotation.GeneratedId;
import com.stee.spfcore.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "COURSE_WAIT_LIST_TASK", schema = "SPFCORE")
@XStreamAlias("CourseWaitListTask")
@Audited
@SequenceDef(name = "CourseWaitListTask_SEQ", schema = "SPFCORE", internetFormat = "FEB-%d", intranetFormat = "BPM-%d")

public class WaitListTask {
	
	@Id
	@GeneratedId
	@Column(name = "\"ID\"")
	private String id;
	
	@Column(name = "\"COURSE_ID\"")
	private String courseId;
	
	@Column(name = "\"SLOT_ID\"")
	private String slotId;
	
	@Column(name = "\"WITHDRAWN_NRIC\"")
	private String withdrawnNirc;
	
	@Column(name = "\"SELECTED_NRIC\"")
	private String selectedNric;
	
	@Formula("(select c.NAME from SPFCORE.PERSONAL_DETAILS c where c.NRIC = WITHDRAWN_NRIC)")
	@NotAudited
	private String withdrawnName;
	
	@Formula("(select c.NAME from SPFCORE.PERSONAL_DETAILS c where c.NRIC = SELECTED_NRIC)")
	@NotAudited
	private String selectedName;
	
	@Formula("(select c.DESCRIPTION from SPFCORE.CODES c, SPFCORE.PERSONAL_EMPLOYMENT_DETAILS e where "
			+ "c.ID = e.ORGANISATION_OR_DEPARTMENT and c.CODE_TYPE = 'UNIT_DEPARTMENT' and e.NRIC = WITHDRAWN_NRIC)")
	@NotAudited
	private String withdrawnDepartment;
	
	@Formula("(select c.DESCRIPTION from SPFCORE.CODES c, SPFCORE.PERSONAL_EMPLOYMENT_DETAILS e where "
			+ "c.ID = e.ORGANISATION_OR_DEPARTMENT and c.CODE_TYPE = 'UNIT_DEPARTMENT' and e.NRIC = SELECTED_NRIC)")
	@NotAudited
	private String selectedDepartment;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"WITHDRAWN_DATE_TIME\"")
	private Date withdrawnDateTime;
	
	@Column(name = "\"HAS_INFORMED\"")
	private boolean informed;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "\"UPDATED_ON\"")
	private Date updatedOn;
	
	@Column(name = "\"UPDATED_BY\"")
	private String updatedBy;
	
	public WaitListTask () {
		super();
	}

	public WaitListTask(String id, String courseId, String slotId,
			String withdrawnNirc, String selectedNric, String withdrawnName, String selectedName, String withdrawnDepartment, String selectedDepartment, Date withdrawnDateTime,
			boolean informed, Date updatedOn, String updatedBy) {
		super();
		this.id = id;
		this.courseId = courseId;
		this.slotId = slotId;
		this.withdrawnNirc = withdrawnNirc;
		this.selectedNric = selectedNric;
		this.withdrawnName = withdrawnName;
		this.selectedName = selectedName;
		this.withdrawnDepartment = withdrawnDepartment;
		this.selectedDepartment = selectedDepartment;
		this.withdrawnDateTime = withdrawnDateTime;
		this.informed = informed;
		this.updatedOn = updatedOn;
		this.updatedBy = updatedBy;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getSlotId() {
		return slotId;
	}

	public void setSlotId(String slotId) {
		this.slotId = slotId;
	}

	public String getWithdrawnNirc() {
		return withdrawnNirc;
	}

	public void setWithdrawnNirc(String withdrawnNirc) {
		this.withdrawnNirc = withdrawnNirc;
	}

	public String getSelectedNric() {
		return selectedNric;
	}

	public void setSelectedNric(String selectedNric) {
		this.selectedNric = selectedNric;
	}
	
	public String getWithdrawnName() {
		return withdrawnName;
	}

	public void setWithdrawnName(String withdrawnName) {
		this.withdrawnName = withdrawnName;
	}

	public String getSelectedName() {
		return selectedName;
	}

	public void setSelectedName(String selectedName) {
		this.selectedName = selectedName;
	}
	
	public Date getWithdrawnDateTime() {
		return withdrawnDateTime;
	}

	public void setWithdrawnDateTime(Date withdrawnDateTime) {
		this.withdrawnDateTime = withdrawnDateTime;
	}

	public boolean isInformed() {
		return informed;
	}

	public void setInformed(boolean informed) {
		this.informed = informed;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
}
