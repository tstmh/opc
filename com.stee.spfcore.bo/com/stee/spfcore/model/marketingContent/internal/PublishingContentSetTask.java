package com.stee.spfcore.model.marketingContent.internal;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "MARKETING_CONTENT_PUBLISHING_CONTENT_SET_TASKS", schema = "SPFCORE")
@Audited
public class PublishingContentSetTask {

	@Id
  @GeneratedValue( strategy = GenerationType.AUTO )
  @Column( name = "\"ID\"" )
  private long id;
	
	@Column(name = "\"CONTENT_SET_ID\"")
	private String contentSetId;
	
	// Will be true if one of the content has been published.
	@Column(name = "\"IS_PUBLISHED\"")
	private boolean published;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "\"TASK_ID\"")
	@Fetch(value = FetchMode.SUBSELECT)
	@OrderColumn(name = "\"LIST_ORDER\"")
	private List<PublishingContent> contents;
	
	@Column(name = "\"IS_CANCELLED\"")
	private boolean cancelled;
	
	@Column(name = "\"SENDER_EMAIL\"", length=256)
	private String senderEmail;
	
	@Column(name = "\"SENDER_EMAIL_PASSWORD\"", length=100)
	private String senderEmailPassword;
	
	
	public PublishingContentSetTask() {
		super();
	}


	public PublishingContentSetTask(long id, String contentSetId, boolean published, List<PublishingContent> contents,
			boolean cancelled, String senderEmail, String senderEmailPassword) {
		super();
		this.id = id;
		this.contentSetId = contentSetId;
		this.published = published;
		this.contents = contents;
		this.cancelled = cancelled;
		this.senderEmail = senderEmail;
		this.senderEmailPassword = senderEmailPassword;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getContentSetId() {
		return contentSetId;
	}


	public void setContentSetId(String contentSetId) {
		this.contentSetId = contentSetId;
	}


	public boolean isPublished() {
		return published;
	}


	public void setPublished(boolean published) {
		this.published = published;
	}


	public List<PublishingContent> getContents() {
		return contents;
	}


	public void setContents(List<PublishingContent> contents) {
		this.contents = contents;
	}


	public boolean isCancelled() {
		return cancelled;
	}


	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}


	public String getSenderEmail() {
		return senderEmail;
	}


	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}


	public String getSenderEmailPassword() {
		return senderEmailPassword;
	}


	public void setSenderEmailPassword(String senderEmailPassword) {
		this.senderEmailPassword = senderEmailPassword;
	}

}
