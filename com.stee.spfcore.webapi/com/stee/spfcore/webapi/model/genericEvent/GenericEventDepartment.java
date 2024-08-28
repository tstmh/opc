package com.stee.spfcore.webapi.model.genericEvent;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Formula;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.stee.spfcore.webapi.model.annotation.GeneratedId;
import com.stee.spfcore.webapi.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "GENERIC_EVENT_DEPARTMENTS", schema = "SPFCORE")
@XStreamAlias("GenericEventDepartment")
@Audited
@SequenceDef (name="GenericEventDepartmentId_SEQ", schema = "SPFCORE", internetFormat="FEB-EA-%d", intranetFormat="BPM-EA-%d")
public class GenericEventDepartment{
	
	@Id
	@GeneratedId
	@Column(name = "\"ID\"")
	private String id;
	
	@Column(name = "\"DEPARTMENT_ID\"")
	private String departmentId;
	
	@Formula("(select c.DESCRIPTION from SPFCORE.CODES c where "
			+ "c.CODE_TYPE = 'UNIT_DEPARTMENT' and c.ID = DEPARTMENT_ID)")
	@NotAudited
	private String department;
	
	@Column(name = "\"IS_MAIN_DEPARTMENT\"")
	private boolean isMainDepartment;
	
	@Column(name="\"UPDATED_BY\"", length=256)
	private String updatedBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name="\"UPDATED_DATE\"")
	private Date updatedOn;

	public GenericEventDepartment() {
		super();
	}

	public GenericEventDepartment(String id, String departmentId,
			boolean isMainDepartment, String updatedBy, Date updatedOn) {
		super();
		this.id = id;
		this.departmentId = departmentId;
		this.isMainDepartment = isMainDepartment;
		this.updatedBy = updatedBy;
		this.updatedOn = updatedOn;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public boolean isMainDepartment() {
		return isMainDepartment;
	}

	public void setMainDepartment(boolean isMainDepartment) {
		this.isMainDepartment = isMainDepartment;
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

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

}
