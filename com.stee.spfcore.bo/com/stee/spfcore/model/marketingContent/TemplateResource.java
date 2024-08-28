package com.stee.spfcore.model.marketingContent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "\"MARKETING_CONTENTS_TEMPLATE_RESOURCES\"", schema = "\"SPFCORE\"")
public class TemplateResource {
	
	@Id
	@Column(name = "\"ID\"", length = 256)
	private String id;
	
	@Column(name = "\"FILE_NAME\"", length = 255)
	private String fileName;
	
	@Column(name = "\"CONTENT_TYPE\"", length = 255)
	private String contentType;
	
	public TemplateResource () {
		super();
	}
	
	public TemplateResource (String id, String fileName, String contentType) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.contentType = contentType;
	}
	
	public String getId () {
		return id;
	}
	
	public void setId (String id) {
		this.id = id;
	}
	
	public String getFileName () {
		return fileName;
	}
	
	public void setFileName (String fileName) {
		this.fileName = fileName;
	}
	
	public String getContentType () {
		return contentType;
	}
	
	public void setContentType (String contentType) {
		this.contentType = contentType;
	}
	
}
