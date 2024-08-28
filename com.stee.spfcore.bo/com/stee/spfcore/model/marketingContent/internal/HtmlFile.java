package com.stee.spfcore.model.marketingContent.internal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "MARKETING_CONTENT_HTML_FILES", schema = "SPFCORE")
@XStreamAlias("MarketingContentHtmlFile")
public class HtmlFile {

	@Id
	@Column(name = "\"CONTENT_ID\"")
	private String contentId;

	@Column(name = "\"HTML_CONTENT\"", length = 32000)
	private String htmlContent;

	public HtmlFile() {
		super();
	}

	public HtmlFile(String contentId, String htmlContent) {
		super();
		this.contentId = contentId;
		this.htmlContent = htmlContent;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}

}
