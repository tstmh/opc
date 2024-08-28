package com.stee.spfcore.model.course.internal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "COURSE_REMINDER_STATUSES", schema = "SPFCORE")
public class ReminderStatus implements Serializable {
	
	private static final long serialVersionUID = 5759683797891509517L;

	@Id
	@Column(name = "\"COURSE_ID\"")
	private String courseId;
	
	@Id
	@Column(name = "\"SLOT_ID\"")
	private String slotId;
	
	@Column(name = "\"HAS_SENT\"")
	private boolean sent;

	public ReminderStatus() {
		super();
	}

	public ReminderStatus(String courseId, String slotId, boolean sent) {
		super();
		this.courseId = courseId;
		this.slotId = slotId;
		this.sent = sent;
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

	public boolean isSent() {
		return sent;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((courseId == null) ? 0 : courseId.hashCode());
		result = prime * result + ((slotId == null) ? 0 : slotId.hashCode());
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
		ReminderStatus other = (ReminderStatus) obj;
		if (courseId == null) {
			if (other.courseId != null)
				return false;
		}
		else if (!courseId.equals(other.courseId))
			return false;
		if (slotId == null) {
			if (other.slotId != null)
				return false;
		}
		else if (!slotId.equals(other.slotId))
			return false;
		return true;
	}
	
}
