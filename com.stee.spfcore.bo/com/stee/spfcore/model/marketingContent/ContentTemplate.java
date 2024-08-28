package com.stee.spfcore.model.marketingContent;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "\"MARKETING_CONTENTS_TEMPLATES\"", schema = "\"SPFCORE\"")
public class ContentTemplate {
	
	@Id
	@Column(name = "\"ID\"", length = 256)
	private String id;
	
	@Column(name = "\"DISPLAY_NAME\"", length = 500)
	private String displayName;
	
	@Column(name = "\"FILE_NAME\"", length = 256)
	private String fileName;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "\"TEMPLATE_ID\"")
	@Fetch(value = FetchMode.SELECT)
	private List<TemplateResource> resources;
	
	public ContentTemplate () {
		super();
	}
	
	public ContentTemplate (String id, String displayName, String fileName, List<TemplateResource> resources) {
		super();
		this.id = id;
		this.displayName = displayName;
		this.fileName = fileName;
		this.resources = resources;
	}
	
	public String getId () {
		return id;
	}
	
	public void setId (String id) {
		this.id = id;
	}
	
	public String getDisplayName () {
		return displayName;
	}
	
	public void setDisplayName (String displayName) {
		this.displayName = displayName;
	}
	
	public String getFileName () {
		return fileName;
	}
	
	public void setFileName (String fileName) {
		this.fileName = fileName;
	}
	
	public List<TemplateResource> getResources () {
		return resources;
	}
	
	public void setResources (List<TemplateResource> resources) {
		this.resources = resources;
	}
	
}
