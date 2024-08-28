package com.stee.spfcore.model.sag;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.stee.spfcore.model.annotation.GeneratedId;
import com.stee.spfcore.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/*
 * Storing the task id of bpm for the purpose of opening the task from the Search
 * 
 * */
@Entity
@Table(name = "\"SAG_Task\"", schema = "\"SPFCORE\"")
@XStreamAlias("SAGTask")
@Audited
@SequenceDef (name="SAGTask_SEQ", schema = "SPFCORE", internetFormat="FEB-%d", intranetFormat="BPM-%d")
public class SAGTask {
	@Id
	@GeneratedId
    @Column( name = "\"ID\"" )
	private String id;
	
	@Column(name = "\"REFERENCE_NUMBER\"", length = 50)
	private String referenceNumber;
	
	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	@Column(name = "\"Task_ID\"", length = 50)
	private String taskId;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	@Override
	public String toString() {
		return "SAGTask [id=" + id + ", referenceNumber=" + referenceNumber
				+ ", taskId=" + taskId + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ( ( referenceNumber == null ) ? 0 : referenceNumber
						.hashCode() );
		return result;
	}

	@Override
	public boolean equals( Object obj ) {
		if ( this == obj )
			return true;
		if ( obj == null )
			return false;
		if ( getClass() != obj.getClass() )
			return false;
		SAGTask other = (SAGTask) obj;
		if ( referenceNumber == null ) {
			if ( other.referenceNumber != null )
				return false;
		} else if ( !referenceNumber.equals( other.referenceNumber ) )
			return false;
		return true;
	}
}
