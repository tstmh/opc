package com.stee.spfcore.model.internal;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "\"APPLICATION_PROCESSING_INFO\"", schema = "\"SPFCORE\"")
public class ProcessingInfo implements Serializable {

	private static final long serialVersionUID = -6445083481438080959L;

	@Id
	@Column(name = "\"REFERENCE_NUMBER\"", length = 50)
	private String referenceNumber;
	
	@Enumerated(EnumType.STRING)
	@Column(name="\"APPLICATION_TYPE\"", length=40)
	private ApplicationType type;
	
	@Column(name="\"FEB_APP_ID\"", length=36)
	private String febAppId;
	
	@Column(name="\"FEB_FORM_ID\"", length=36)
	private String febFormId;
	
	@Column(name="\"FEB_RECORD_ID\"", length=36)
	private String febRecordId;
	
	/**
	 * Track whether the file has been transferred to BPM via SFTP
	 */
	@Column(name="\"FEB_FILE_TRANSFERRED\"")
	private boolean febFileTransferred = false;
	
	@Column(name="\"BPM_BPD_ID\"", length=50)
	private String bpmBPDId;
	
	@Column(name="\"BPM_BRANCH_ID\"", length=50)
	private String bpmBranchId;
	
	@Column(name="\"BPM_PROCESS_INSTANCE_ID\"", length=50)
	private String bpmProcessInstanceId;
	
	/**
	 * Track whether the file has been uploaded to BPM content repository
	 */
	@Column(name="\"BPM_FILE_UPLOADED\"")
	private boolean bpmFileUploaded = false;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="\"UPDATED_DATE\"")
	private Date updatedOn;
	
	public ProcessingInfo () {
		super ();
	}


	public String getReferenceNumber() {
		return referenceNumber;
	}


	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}


	public ApplicationType getType() {
		return type;
	}


	public void setType(ApplicationType type) {
		this.type = type;
	}


	public String getFebAppId() {
		return febAppId;
	}


	public void setFebAppId(String febAppId) {
		this.febAppId = febAppId;
	}


	public String getFebFormId() {
		return febFormId;
	}


	public void setFebFormId(String febFormId) {
		this.febFormId = febFormId;
	}


	public String getFebRecordId() {
		return febRecordId;
	}


	public void setFebRecordId(String febRecordId) {
		this.febRecordId = febRecordId;
	}


	public boolean isFebFileTransferred() {
		return febFileTransferred;
	}


	public void setFebFileTransferred(boolean febFileTransferred) {
		this.febFileTransferred = febFileTransferred;
	}


	public String getBpmBPDId() {
		return bpmBPDId;
	}


	public void setBpmBPDId(String bpmBPDId) {
		this.bpmBPDId = bpmBPDId;
	}


	public String getBpmBranchId() {
		return bpmBranchId;
	}


	public void setBpmBranchId(String bpmBranchId) {
		this.bpmBranchId = bpmBranchId;
	}


	public String getBpmProcessInstanceId() {
		return bpmProcessInstanceId;
	}


	public void setBpmProcessInstanceId(String bpmProcessInstanceId) {
		this.bpmProcessInstanceId = bpmProcessInstanceId;
	}


	public boolean isBpmFileUploaded() {
		return bpmFileUploaded;
	}


	public void setBpmFileUploaded(boolean bpmFileUploaded) {
		this.bpmFileUploaded = bpmFileUploaded;
	}
	
	public Date getUpdatedOn() {
		return updatedOn;
	}


	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((referenceNumber == null) ? 0 : referenceNumber.hashCode());
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
		ProcessingInfo other = (ProcessingInfo) obj;
		if (referenceNumber == null) {
			if (other.referenceNumber != null)
				return false;
		} else if (!referenceNumber.equals(other.referenceNumber))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "ProcessingInfo [referenceNumber=" + referenceNumber + ", type=" + type + ", febAppId=" + febAppId
				+ ", febFormId=" + febFormId + ", febRecordId=" + febRecordId + ", febFileTransferred=" + febFileTransferred
				+ ", bpmBPDId=" + bpmBPDId + ", bpmBranchId=" + bpmBranchId + ", bpmProcessInstanceId=" + bpmProcessInstanceId
				+ ", bpmFileUploaded=" + bpmFileUploaded + ", updatedOn=" + updatedOn + "]";
	}
	
}
