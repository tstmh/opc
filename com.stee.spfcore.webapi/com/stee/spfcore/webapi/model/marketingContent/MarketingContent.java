package com.stee.spfcore.webapi.model.marketingContent;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Formula;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table(name = "MARKETING_CONTENTS", schema = "SPFCORE")
@XStreamAlias("MarketingContent")
@Audited
@NamedNativeQuery(name = "findContentIdsInSetNative", query = "select ID from SPFCORE.MARKETING_CONTENTS where SET_ID = :contentSetId")
public class MarketingContent {
	
	@Id
	@Column(name = "\"ID\"")
	private String id;
	
	@Column(name = "\"TITLE\"")
	private String title;
	
	@Column(name = "\"MODULE\"", length = 200)
	private String module;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "\"CONTENT_ID\"")
	@Fetch(value = FetchMode.SELECT)
	@OrderColumn(name = "\"LIST_ORDER\"")
	private List<Attachment> attachments;
	
	@Column(name = "\"HTML_CONTENT\"", length = 32000)
	private String htmlContent;
	
	@Column(name = "\"DATE_OFFSET\"")
	private int offset;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"PUBLISH_DATE\"")
	private Date publishDate;
	
	@Formula("(select ta.IS_NOTIFICATION_SENT from SPFCORE.MARKETING_CONTENT_PUBLISHING_CONTENT_STATES as ta where ta.CONTENT_ID = ID)")
	@NotAudited
	@XStreamOmitField
	private Boolean published;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "\"OFFSET_MODE\"", length = 10)
	private OffsetMode offsetMode;
	
	@Column(name = "\"UPDATED_BY\"", length = 30)
	private String updatedBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "\"UPDATED_DATE\"")
	private Date updatedOn;
	
	@Column(name = "\"REMARKS\"", length = 1000)
	private String remarks;
	
	@Column(name = "\"IS_SMS_CONTENT_AVAILABLE\"")
	private boolean smsContentAvailable;
	
	@Column(name = "\"SMS_CONTENT\"", length = 1000)
	private String smsContent;
	
	@Column(name = "\"IS_TEMPLATE_BASED\"")
	private boolean templateBased;
	
	@Column(name = "\"TEMPLATE_ID\"", length = 256)
	private String templateId;
	
	@Column(name = "\"TEMPLATE_HEADER\"", length = 1000)
	private String templateHeader;
	
	@Column(name = "\"TEMPLATE_TITLE\"", length = 1000)
	private String templateTitle;
	
	@Column(name = "\"TEMPLATE_BODY\"", length = 5000)
	private String templateBody;
	
	@PostLoad
	protected void onLoad () {
		if (published == null) {
			published = Boolean.FALSE;
		}
	}
	
	public MarketingContent () {
		super();
	}
	
	public MarketingContent (String id, String title, String module, List<Attachment> attachments, String htmlContent, int offset, Date publishDate, Boolean published,
			OffsetMode offsetMode, String updatedBy, Date updatedOn, String remarks, boolean smsContentAvailable, String smsContent, boolean templateBased, String templateId,
			String templateHeader, String templateTitle, String templateBody) {
		super();
		this.id = id;
		this.title = title;
		this.module = module;
		this.attachments = attachments;
		this.htmlContent = htmlContent;
		this.offset = offset;
		this.publishDate = publishDate;
		this.published = published;
		this.offsetMode = offsetMode;
		this.updatedBy = updatedBy;
		this.updatedOn = updatedOn;
		this.remarks = remarks;
		this.smsContentAvailable = smsContentAvailable;
		this.smsContent = smsContent;
		this.templateBased = templateBased;
		this.templateId = templateId;
		this.templateHeader = templateHeader;
		this.templateTitle = templateTitle;
		this.templateBody = templateBody;
	}
	
	public String getId () {
		return id;
	}
	
	public void setId (String id) {
		this.id = id;
	}
	
	public String getTitle () {
		return title;
	}
	
	public void setTitle (String title) {
		this.title = title;
	}
	
	public String getModule () {
		return module;
	}
	
	public void setModule (String module) {
		this.module = module;
	}
	
	public List<Attachment> getAttachments () {
		return attachments;
	}
	
	public void setAttachments (List<Attachment> attachments) {
		this.attachments = attachments;
	}
	
	public String getHtmlContent () {
		return htmlContent;
	}
	
	public void setHtmlContent (String htmlContent) {
		this.htmlContent = htmlContent;
	}
	
	public int getOffset () {
		return offset;
	}
	
	public void setOffset (int offset) {
		this.offset = offset;
	}
	
	public Date getPublishDate () {
		return publishDate;
	}
	
	public void setPublishDate (Date publishDate) {
		this.publishDate = publishDate;
	}
	
	public OffsetMode getOffsetMode () {
		return offsetMode;
	}
	
	public void setOffsetMode (OffsetMode offsetMode) {
		this.offsetMode = offsetMode;
	}
	
	public String getUpdatedBy () {
		return updatedBy;
	}
	
	public void setUpdatedBy (String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	public Date getUpdatedOn () {
		return updatedOn;
	}
	
	public void setUpdatedOn (Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	
	public String getRemarks () {
		return remarks;
	}
	
	public void setRemarks (String remarks) {
		this.remarks = remarks;
	}
	
	public boolean isSmsContentAvailable () {
		return smsContentAvailable;
	}
	
	public void setSmsContentAvailable (boolean smsContentAvailable) {
		this.smsContentAvailable = smsContentAvailable;
	}
	
	public String getSmsContent () {
		return smsContent;
	}
	
	public void setSmsContent (String smsContent) {
		this.smsContent = smsContent;
	}
	
	public Date getActualPublishDate () {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(publishDate);
		
		if (offsetMode == OffsetMode.AFTER) {
			cal.add(Calendar.DATE, offset);
		}
		else {
			cal.add(Calendar.DATE, -(offset));
		}
		
		return cal.getTime();
	}
	
	public Boolean getPublished () {
		return published;
	}
	
	public void setPublished (Boolean published) {
		this.published = published;
	}
	
	public boolean isTemplateBased () {
		return templateBased;
	}
	
	public void setTemplateBased (boolean templateBased) {
		this.templateBased = templateBased;
	}
	
	public String getTemplateId () {
		return templateId;
	}
	
	public void setTemplateId (String templateId) {
		this.templateId = templateId;
	}
	
	public String getTemplateHeader () {
		return templateHeader;
	}
	
	public void setTemplateHeader (String templateHeader) {
		this.templateHeader = templateHeader;
	}
	
	public String getTemplateTitle () {
		return templateTitle;
	}
	
	public void setTemplateTitle (String templateTitle) {
		this.templateTitle = templateTitle;
	}
	
	public String getTemplateBody () {
		return templateBody;
	}
	
	public void setTemplateBody (String templateBody) {
		this.templateBody = templateBody;
	}
	
}

