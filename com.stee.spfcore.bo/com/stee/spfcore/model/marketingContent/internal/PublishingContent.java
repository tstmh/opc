package com.stee.spfcore.model.marketingContent.internal;

import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "MARKETING_CONTENT_PUBLISHING_CONTENT_STATES", schema = "SPFCORE")
@Audited
public class PublishingContent {
	
	@Id
  @GeneratedValue( strategy = GenerationType.AUTO )
  @Column( name = "\"ID\"" )
  private long id;
	
	@Column(name = "\"CONTENT_ID\"")
	private String contentId;
	
	// Whether the content has been transferred to FEB
	@Column(name = "\"IS_TRANSFERRED\"")
	private boolean transferred;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"PUBLISH_DATE\"")
	private Date publishDate;
	
	// Whether email has been sent
	@Column(name = "\"IS_NOTIFICATION_SENT\"")
	private boolean notificationSent;

	@ElementCollection( fetch = FetchType.EAGER )
  @CollectionTable( name = "MARKETING_CONTENT_PUBLISH_EMAILS", schema = "SPFCORE", joinColumns = @JoinColumn( name = "TASK_ID" ) )
  @Column( name = "PUBLISH_EMAIL", length=256 )
  @Fetch( value = FetchMode.SELECT )
	private List<String> emails;
	
	@ElementCollection( fetch = FetchType.EAGER )
  @CollectionTable( name = "MARKETING_CONTENT_PUBLISH_PHONES", schema = "SPFCORE", joinColumns = @JoinColumn( name = "TASK_ID" ) )
  @Column( name = "PUBLISH_PHONE" )
  @Fetch( value = FetchMode.SELECT )
	private List<String> phones;
	
	
	public PublishingContent() {
		super();
	}

	public PublishingContent(long id, String contentId, boolean transferred, Date publishDate,
			boolean notificationSent, List<String> emails, List<String> phones) {
		super();
		this.id = id;
		this.contentId = contentId;
		this.transferred = transferred;
		this.publishDate = publishDate;
		this.notificationSent = notificationSent;
		this.emails = emails;
		this.phones = phones;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public boolean isTransferred() {
		return transferred;
	}

	public void setTransferred(boolean transferred) {
		this.transferred = transferred;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public boolean isNotificationSent() {
		return notificationSent;
	}

	public void setNotificationSent (boolean notificationSent) {
		this.notificationSent = notificationSent;
	}

	public List<String> getEmails() {
		return emails;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}

	public List<String> getPhones() {
		return phones;
	}

	public void setPhones(List<String> phones) {
		this.phones = phones;
	}
}
