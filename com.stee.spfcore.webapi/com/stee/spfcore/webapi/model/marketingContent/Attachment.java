package com.stee.spfcore.webapi.model.marketingContent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "MARKETING_CONTENT_ATTACHMENTS", schema = "SPFCORE")
@XStreamAlias("MarketingContentAttachment")
@Audited
public class Attachment {

	@Id
	@Column(name = "\"DOC_ID\"")
	private String docId;
	
	@Column(name = "\"FILE_NAME\"")
	private String fileName;
	
	@Column(name = "\"FILE_TYPE\"")
	private String fileType;
	
	public Attachment() {
		super();
	}

	public Attachment(String docId, String fileName, String fileType) {
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

