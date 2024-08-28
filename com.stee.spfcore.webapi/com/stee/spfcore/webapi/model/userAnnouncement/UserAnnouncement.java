package com.stee.spfcore.webapi.model.userAnnouncement;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Formula;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.stee.spfcore.webapi.model.annotation.GeneratedId;
import com.stee.spfcore.webapi.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table(name = "USER_ANNOUNCEMENTS", schema = "SPFCORE")
@XStreamAlias("UserAnnouncement")
@Audited
@SequenceDef(name = "USER_ANNOUNCEMENT_SEQ", schema = "SPFCORE", internetFormat = "FEB-%d", intranetFormat = "BPM-%d")
public class UserAnnouncement {

	@Id
	@GeneratedId
	@Column(name = "\"ID\"")
	private String id;

	@Column(name = "\"NRIC\"", length = 10)
	private String nric;

	@Enumerated(EnumType.STRING)
	@Column(name = "\"MODULE\"", length = 20)
	private UserAnnouncementModule module;
	
	@Column(name = "\"REFERENCE_ID\"")
	private String referenceId;

	@Column(name = "\"TITLE\"")
	private String title;

	@Column(name = "\"CONTENT_ID\"")
	private String contentId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"PUBLISH_DATE\"")
	private Date publishDate;
	
	@Formula("(select count(*) from SPFCORE.MARKETING_CONTENT_USER_VIEW_RECORDS c where c.NRIC = NRIC and c.CONTENT_ID = CONTENT_ID)")
	@NotAudited
	@XStreamOmitField
	private int viewCount;

	public UserAnnouncement() {
		super();
	}

	public UserAnnouncement(String id, String nric, UserAnnouncementModule module, String referenceId, String title, String contentId,
			Date publishDate) {
		super();
		this.id = id;
		this.nric = nric;
		this.module = module;
		this.referenceId = referenceId;
		this.title = title;
		this.contentId = contentId;
		this.publishDate = publishDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNric() {
		return nric;
	}

	public void setNric(String nric) {
		this.nric = nric;
	}

	public UserAnnouncementModule getModule() {
		return module;
	}

	public void setModule(UserAnnouncementModule module) {
		this.module = module;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}
	
	
}
