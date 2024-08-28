package com.stee.spfcore.model.userRoleManagement;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import com.stee.spfcore.model.annotation.GeneratedId;
import com.stee.spfcore.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table(name = "\"URM_BRP_GROUP_USER\"", schema = "\"SPFCORE\"")
@XStreamAlias("UrmBrpGroupUser")
@Audited
@SequenceDef (name="URM_BRP_GROUP_USER_SEQ", schema = "SPFCORE", internetFormat="FEB-GU-%d", intranetFormat="BPM-GU-%d")
public class UrmBrpGroupUser {

	@Id
	@GeneratedId
	@Column(name = "\"ID\"")
	private String id;

	@Column(name = "\"BPM_USER_ID\"", length = 100)
	private String bpmUserId;

	@Column(name = "\"BPM_USER_NAME\"", length = 100)
	private String bpmUserName;

	@Column(name = "\"URM_BPM_GROUP_ID\"", length = 100)
	private String urmBpmGroupId;

	@Column(name = "\"STATUS\"", length = 50)
	private String status;

	@Column(name = "\"CREATED_BY\"", length = 50)
	private String created_by;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"CREATED_ON\"")
	private Date created_on;

	@Column(name = "\"REMARKS\"", length = 1000)
	private String remarks;

	public String getbpmUserName()
	{
		return this.bpmUserName;
	}

	public void setBpmUserName(String bpmUserName)
	{
		this.bpmUserName = bpmUserName;
	}

	//	public String getUrmBpmGroupId()
//	{
//		return this.bpmUserName;
//	}
	public String getUrmBpmGroupId()
	{
		return this.urmBpmGroupId;
	}

	public void setUrmBpmGroupId(String urmBpmGroupId)
	{
		this.urmBpmGroupId = urmBpmGroupId;
	}

//	public String getStatus()
//	{
//		return this.bpmUserName;
//	}

	public String getStatus()
	{
		return this.status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getCreatedBy()
	{
		return this.created_by;
	}

	public void setCreatedBy(String created_by)
	{
		this.created_by = created_by;
	}

	public Date getCreatedOn()
	{
		return this.created_on;
	}

	public void setCreatedOn(Date created_on)
	{
		this.created_on = created_on;
	}

	public String getRemarks()
	{
		return this.remarks;
	}

	public void setRemarks(String remarks)
	{
		this.remarks = remarks;
	}

	public void preSave () {

		if (bpmUserName != null) {
			bpmUserName = bpmUserName.toUpperCase();
		}

		if (created_by != null) {
			created_by = created_by.toUpperCase();
		}
	}

}
