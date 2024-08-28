package com.stee.spfcore.model.userRoleManagement;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
@Table(name = "\"URM_BPM_GROUP\"", schema = "\"SPFCORE\"")
@XStreamAlias("UrmBpmGroup")
@Audited
@SequenceDef (name="URM_BPM_GROUP_SEQ", schema = "SPFCORE", internetFormat="FEB-UG-%d", intranetFormat="BPM-UG-%d")
public class UrmBpmGroup {

	@Id
	@GeneratedId
	@Column(name = "\"ID\"")
	private String id;

	@Column(name = "\"BPM_GROUP_ID\"", length = 100)
	private String bpmGroupId;

	@Column(name = "\"NAME\"", length = 200)
	private String name;

	@Column(name = "\"STATUS\"", length = 50)
	private String status;

	@Column(name = "\"CREATED_BY\"", length = 50)
	private String created_by;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"CREATED_ON\"")
	private Date created_on;

	@Column(name = "\"REMARKS\"", length = 1000)
	private String remarks;

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
	@JoinColumn(name="\"URM_BPM_GROUP_ID\"")
	@Fetch(value = FetchMode.SUBSELECT)
	private List<UrmBrpGroupUser> UrmBrpGroupUser;

	public UrmBpmGroup() {
		super();
	}

	public UrmBpmGroup(String bpmGroupId, String name, String status, String created_by, Date created_on, String remarks,List<UrmBrpGroupUser> UrmBrpGroupUser) {
		super();
		this.bpmGroupId = bpmGroupId;
		this.name = name;
		this.status = status;
		this.created_by = created_by;
		this.created_on = created_on;
		this.remarks = remarks;
		this.UrmBrpGroupUser = UrmBrpGroupUser;
	}

	public String getBpmGroupId()
	{
		return this.bpmGroupId;
	}

	public void setBpmGroupId(String bpmGroupId)
	{
		this.bpmGroupId = bpmGroupId;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

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

	public List<UrmBrpGroupUser> getUrmBrpGroupUser()
	{
		return this.UrmBrpGroupUser;
	}

	public void setUrmBrpGroupUser(List<UrmBrpGroupUser> UrmBrpGroupUser)
	{
		this.UrmBrpGroupUser = UrmBrpGroupUser;
	}

	public void preSave () {

		if (created_by != null) {
			created_by = created_by.toUpperCase();
		}

		if (UrmBrpGroupUser != null) {
			for (UrmBrpGroupUser UrmBrpGroupUsers : UrmBrpGroupUser) {
				UrmBrpGroupUsers.preSave ();
			}
		}
	}
}
