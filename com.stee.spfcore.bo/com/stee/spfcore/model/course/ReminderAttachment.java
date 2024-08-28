package com.stee.spfcore.model.course;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "SLOT_REMINDER_ATTACHMENT", schema = "SPFCORE")
@XStreamAlias("ReminderAttachment")
@Audited
public class ReminderAttachment {
	
	@Id
	@Column(name = "\"DOC_ID\"")
	private String docId;
	
	@Column(name = "\"FILE_NAME\"")
	private String fileName;
	
	@Column(name = "\"FILE_TYPE\"")
	private String fileType;
	
	public ReminderAttachment() {
		super();
	}

	public ReminderAttachment(String docId, String fileName, String fileType) {
		super();
		this.docId = docId;
		this.fileName = fileName;
		this.fileType = fileType;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
}
