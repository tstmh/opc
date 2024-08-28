package com.stee.spfcore.vo.marketingContent;


public class BinaryAttachment {
	
	private String docId;
	
	private String name;
	
	private String contentType;

	public BinaryAttachment() {
		super();
	}

	public BinaryAttachment(String docId, String name, String contentType) {
		super();
		this.docId = docId;
		this.name = name;
		this.contentType = contentType;
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
	
}
