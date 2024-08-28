package com.stee.spfcore.webapi.model.marketingContent.internal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "MARKETING_CONTENT_BINARY_FILES", schema = "SPFCORE")
@XStreamAlias("MarketingContentBinaryFile")
public class BinaryFile {

	@Id
	@Column(name = "\"DOC_ID\"")
	private String docId;
	
	@Column(name = "\"NAME\"", length=255)
	private String name;
	
	@Column(name = "\"CONTENT_TYPE\"", length=255)
	private String contentType;
	
	@Column(name = "\"CONTENT_ID\"")
	private String contentId;
	
	@Column(name = "\"IS_ATTACHMENT\"")
	private boolean attachment;
	
	@Column(name = "FILE_CONTENT", nullable = false, length=4194304)
	@Lob
  private byte [] content;

	public BinaryFile() {
		super();
	}

	public BinaryFile(String docId, String name, String contentType, String contentId, boolean attachment, byte[] content) {
		super();
		this.docId = docId;
		this.name = name;
		this.contentType = contentType;
		this.content = content;
		this.contentId = contentId;
		this.attachment = attachment;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public boolean isAttachment() {
		return attachment;
	}

	public void setAttachment(boolean attachment) {
		this.attachment = attachment;
	}
	
}


