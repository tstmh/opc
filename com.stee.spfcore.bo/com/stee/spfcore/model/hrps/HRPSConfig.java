package com.stee.spfcore.model.hrps;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table(name = "HRPS_CONFIG", schema = "SPFCORE")
@XStreamAlias("HRPSConfig")

public class HRPSConfig {
	@Id
	@Column(name = "ID")
	@XStreamOmitField
	private int id = 0;
	
	@Column(name = "INBOUND_HEADER_RECORD_TYPE")
	private String inboundHeaderRecordType = "1";
	
	@Column(name = "INBOUND_HEADER_SOURCE")
	private String inboundHeaderSource = "Minis-MHA-SPFCARE";
	
	@Column(name = "INBOUND_DETAIL_RECORD_TYPE")
	private String inboundDetailRecordType = "2";
	
	@Column(name = "INBOUND_DETAIL_WAGE_TYPE")
	private String inboundDetailWageType = "2338";
	
	@Column(name = "INBOUND_TRAILER_RECORD_TYPE")
	private String inboundTrailerRecordType = "9";
	
	@Column(name = "OUTBOUND_HEADER_RECORD_TYPE")
	private String outboundHeaderRecordType = "1";
	
	@Column(name = "OUTBOUND_HEADER_SOURCE")
	private String outboundHeaderSource = "HRP-WOG";
	
	@Column(name = "OUTBOUND_TRAILER_RECORD_TYPE")
	private String outboundTrailerRecordType = "9";
	
	@Column(name = "INBOUND_FOLDER")
	private String inboundFolder = "c:\\HRPS\\inbound";
	
	@Column(name = "WORKING_FOLDER")
	private String workingFolder = "c:\\HRPS\\working";
	
	@Column(name = "OUTBOUND_FOLDER")
	private String outboundFolder = "c:\\HRPS\\outbound";
	
	@Column(name = "OUTBOUND_POST_FOLDER")
	private String outboundPostFolder = "c:\\HRPS\\post";
	
	@Column(name = "INBOUND_ARCHIVE_FOLDER")
	private String inboundArchiveFolder = "c:\\HRPS\\archive\\inbound";
	
	@Column(name = "OUTBOUND_ARCHIVE_FOLDER")
	private String outboundArchiveFolder = "c:\\HRPS\\archive\\outbound";
	
	@Column(name = "OUTBOUND_POST_ARCHIVE_FOLDER")
	private String outboundPostArchiveFolder = "c:\\HRPS\\archive\\post";
	
	@Column(name = "INBOUND_FILE")
	private String inboundFile = "SPFCAR_HRP_AllceDedn";
	
	@Column(name = "OUTBOUND_FILE")
	private String outboundFile = "HRP_SPFCAR_AllceDedn.Return";
	
	@Column(name = "OUTBOUND_POST_FILE")
	private String outboundPostFile = "HRP_SPFCARE_PostPayroll";
	
	@Column(name = "EMAIL_TO_ADDRESS")
	private String emailToAddress = "core_system@spf.gov.sg";
	
	@Column(name = "SENDER_ADDRESS")
	private String senderAddress = "spf_tech@spf.gov.sg";
	
	@Column(name = "EMAIL_PASSWORD")
	private String emailPassword;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getInboundHeaderRecordType() {
		return inboundHeaderRecordType;
	}

	public void setInboundHeaderRecordType(String inboundHeaderRecordType) {
		this.inboundHeaderRecordType = inboundHeaderRecordType;
	}

	public String getInboundHeaderSource() {
		return inboundHeaderSource;
	}

	public void setInboundHeaderSource(String inboundHeaderSource) {
		this.inboundHeaderSource = inboundHeaderSource;
	}

	public String getInboundDetailRecordType() {
		return inboundDetailRecordType;
	}

	public void setInboundDetailRecordType(String inboundDetailRecordType) {
		this.inboundDetailRecordType = inboundDetailRecordType;
	}

	public String getInboundDetailWageType() {
		return inboundDetailWageType;
	}

	public void setInboundDetailWageType(String inboundDetailWageType) {
		this.inboundDetailWageType = inboundDetailWageType;
	}

	public String getInboundTrailerRecordType() {
		return inboundTrailerRecordType;
	}

	public void setInboundTrailerRecordType(String inboundTrailerRecordType) {
		this.inboundTrailerRecordType = inboundTrailerRecordType;
	}

	public String getOutboundHeaderRecordType() {
		return outboundHeaderRecordType;
	}

	public void setOutboundHeaderRecordType(String outboundHeaderRecordType) {
		this.outboundHeaderRecordType = outboundHeaderRecordType;
	}

	public String getOutboundHeaderSource() {
		return outboundHeaderSource;
	}

	public void setOutboundHeaderSource(String outboundHeaderSource) {
		this.outboundHeaderSource = outboundHeaderSource;
	}

	public String getOutboundTrailerRecordType() {
		return outboundTrailerRecordType;
	}

	public void setOutboundTrailerRecordType(String outboundTrailerRecordType) {
		this.outboundTrailerRecordType = outboundTrailerRecordType;
	}

	public String getInboundFolder() {
		return inboundFolder;
	}

	public void setInboundFolder(String inboundFolder) {
		this.inboundFolder = inboundFolder;
	}

	public String getWorkingFolder() {
		return workingFolder;
	}

	public void setWorkingFolder(String workingFolder) {
		this.workingFolder = workingFolder;
	}

	public String getOutboundFolder() {
		return outboundFolder;
	}

	public void setOutboundFolder(String outboundFolder) {
		this.outboundFolder = outboundFolder;
	}

	public String getInboundArchiveFolder() {
		return inboundArchiveFolder;
	}

	public void setInboundArchiveFolder(String inboundArchiveFolder) {
		this.inboundArchiveFolder = inboundArchiveFolder;
	}

	public String getOutboundArchiveFolder() {
		return outboundArchiveFolder;
	}

	public void setOutboundArchiveFolder(String outboundArchiveFolder) {
		this.outboundArchiveFolder = outboundArchiveFolder;
	}

	public String getInboundFile() {
		return inboundFile;
	}

	public void setInboundFile(String inboundFile) {
		this.inboundFile = inboundFile;
	}

	public String getOutboundFile() {
		return outboundFile;
	}

	public void setOutboundFile(String outboundFile) {
		this.outboundFile = outboundFile;
	}

	public String getOutboundPostFile() {
		return outboundPostFile;
	}

	public void setOutboundPostFile(String outboundPostFile) {
		this.outboundPostFile = outboundPostFile;
	}

	public String getEmailToAddress() {
		return emailToAddress;
	}

	public void setEmailToAddress(String emailToAddress) {
		this.emailToAddress = emailToAddress;
	}

	public String getSenderAddress() {
		return senderAddress;
	}

	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}

	public String getEmailPassword() {
		return emailPassword;
	}

	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}

	public String getOutboundPostFolder() {
		return outboundPostFolder;
	}

	public void setOutboundPostFolder(String outboundPostFolder) {
		this.outboundPostFolder = outboundPostFolder;
	}

	public String getOutboundPostArchiveFolder() {
		return outboundPostArchiveFolder;
	}

	public void setOutboundPostArchiveFolder(String outboundPostArchiveFolder) {
		this.outboundPostArchiveFolder = outboundPostArchiveFolder;
	}

	
	
	
	
}
