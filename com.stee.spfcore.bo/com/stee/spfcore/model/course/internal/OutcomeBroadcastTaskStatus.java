package com.stee.spfcore.model.course.internal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "COURSE_SELECTION_OUTCOME_BROADCAST_TASK_STATUS", schema = "SPFCORE")
public class OutcomeBroadcastTaskStatus {
	
	@Id
	@Column(name = "\"COURSE_ID\"")
	private String courseId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"PERFORMED_ON\"")
	private Date performedOn;
	
	public OutcomeBroadcastTaskStatus () {
		super();
	}
	
	public OutcomeBroadcastTaskStatus (String courseId, Date performedOn) {
		super();
		this.courseId = courseId;
		this.performedOn = performedOn;
	}
	
	public String getCourseId () {
		return courseId;
	}
	
	public void setCourseId (String courseId) {
		this.courseId = courseId;
	}
	
	public Date getPerformedOn () {
		return performedOn;
	}
	
	public void setPerformedOn (Date performedOn) {
		this.performedOn = performedOn;
	}
}
