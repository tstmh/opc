package com.stee.spfcore.vo.marketingContent;

public class ContentTemplateInfo {
	
	private String id;
	
	private String displayName;
	
	public ContentTemplateInfo (String id, String displayName) {
		super();
		this.id = id;
		this.displayName = displayName;
	}
	
	public ContentTemplateInfo () {
		super();
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
	
}
