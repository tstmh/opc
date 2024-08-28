package com.stee.spfcore.model.benefits;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.stee.spfcore.model.annotation.GeneratedId;
import com.stee.spfcore.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table(name = "\"BENEFITS_PROCESS\"", schema = "\"SPFCORE\"")
@XStreamAlias("BenefitsProcess")
@Audited
@SequenceDef (name="BENEFITS_PROCESS_SEQ", schema = "SPFCORE", internetFormat="FEB-BP-%d", intranetFormat="BPM-BP-%d")
public class BenefitsProcess {
	
	@Id
	@GeneratedId
	@Column(name = "\"ID\"")
	private String id;
	
	@Column(name="\"BPM_PROCESS_ID\"")
	private Integer bpmProcessId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"CREATED_DATE\"")
	private Date createdDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"UPDATED_DATE\"")
	private Date updatedDate;
	
	@Column(name = "\"CREATION_PERIOD\"", length = 20)
	private String creationPeriod;
	
	@Column(name = "\"NAME\"", length = 200)
	private String name;
	
	@Column(name = "\"STATUS\"", length = 50)
	private String status;
	
	public BenefitsProcess(){
		super();
	}
	
	public BenefitsProcess(Integer bpmProcessId,
			Date createdDate, Date updatedDate, String creationPeriod, String name, String status) {
		super();
		this.bpmProcessId = bpmProcessId;
		this.createdDate= createdDate;
		this.updatedDate = updatedDate;
		this.creationPeriod = creationPeriod;
		this.name = name;
		this.status = status;
	}

	public Integer getBpmProcessId() {
		return bpmProcessId;
	}

	public void setBpmProcessId(Integer bpmProcessId) {
		this.bpmProcessId = bpmProcessId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getCreationPeriod() {
		return creationPeriod;
	}

	public void setCreationPeriod(String creationPeriod) {
		this.creationPeriod = creationPeriod;
	}

}
