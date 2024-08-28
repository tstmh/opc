package com.stee.spfcore.model;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"USER_PROCESSING_DETAILS\"", schema = "\"SPFCORE\"")
@XStreamAlias("UserProcessingDetails")
public class UserProcessingDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="\"ID\"")
	private long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "\"OPERATION_CODE\"", length = 50)
	private OperationCode operationCode;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "\"MESSAGE_CREATED\"")
	private Date messageCreated;

    @Column(name = "NRIC")
    private String nric;
    
    @Column(name = "NAME")
    private String name;
    
    @Enumerated(EnumType.STRING)
	@Column(name = "\"PROCESS_FLAG\"", length = 50)
	private ProcessFlag processFlag;

    @Temporal(TemporalType.DATE)
    @Column(name = "PROCESS_DATE_TIME")
    private Date processDateTime;

	public UserProcessingDetails() {
		super();
	}

	public UserProcessingDetails(OperationCode operationCode, Date messageCreated, String nric, String name,
			ProcessFlag processFlag, Date processDateTime) {
		super();
		this.operationCode = operationCode;
		this.messageCreated = messageCreated;
		this.nric = nric;
		this.name = name;
		this.processFlag = processFlag;
		this.processDateTime = processDateTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public OperationCode getOperationCode() {
		return operationCode;
	}
	
	public void setOperationCode(OperationCode operationCode) {
		this.operationCode = operationCode;
	}

	public Date getMessageCreated() {
		return messageCreated;
	}

	public void setMessageCreated(Date messageCreated) {
		this.messageCreated = messageCreated;
	}

	public String getNric() {
		return nric;
	}

	public void setNric(String nric) {
		this.nric = nric;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ProcessFlag getProcessFlag() {
		return processFlag;
	}

	public void setProcessFlag(ProcessFlag processFlag) {
		this.processFlag = processFlag;
	}

	public Date getProcessDateTime() {
		return processDateTime;
	}

	public void setProcessDateTime(Date processDateTime) {
		this.processDateTime = processDateTime;
	}

	@Override
	public String toString() {
		return "UserProcessingDetails [id=" + id + ", operationCode="
				+ operationCode + ", messageCreated=" + messageCreated
				+ ", nric=" + nric + ", name=" + name + ", processFlag="
				+ processFlag + ", processDateTime=" + processDateTime + "]";
	}	

}
