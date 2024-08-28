package com.stee.spfcore.model.genericEvent.internal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "GENERIC_EVENT_NOTIFY_APPLICATION_RESULT_TASK", schema = "SPFCORE")
public class NotifyApplicationResultTask {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "\"ID\"")
	private long id;

	@Column(name = "\"EVENT_ID\"")
	private String eventId;

	@Column(name = "\"IS_COMPLETED\"")
	private boolean completed;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"PERFORMED_ON\"")
	private Date performedOn;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"REQUESTED_ON\"")
	private Date requestedOn;

	@Column(name = "\"REQUESTED_BY\"", length = 256)
	private String requestedBy;

	public NotifyApplicationResultTask() {
		super();
	}

	public NotifyApplicationResultTask(String eventId, boolean completed, Date performedOn, Date requestedOn,
			String requestedBy) {
		super();
		this.eventId = eventId;
		this.completed = completed;
		this.performedOn = performedOn;
		this.requestedOn = requestedOn;
		this.requestedBy = requestedBy;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public Date getPerformedOn() {
		return performedOn;
	}

	public void setPerformedOn(Date performedOn) {
		this.performedOn = performedOn;
	}

	public Date getRequestedOn() {
		return requestedOn;
	}

	public void setRequestedOn(Date requestedOn) {
		this.requestedOn = requestedOn;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

}
