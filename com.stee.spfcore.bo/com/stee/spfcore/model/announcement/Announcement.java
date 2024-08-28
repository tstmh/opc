package com.stee.spfcore.model.announcement;

import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Formula;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.stee.spfcore.model.annotation.GeneratedId;
import com.stee.spfcore.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table(name = "ANNOUNCEMENTS", schema = "SPFCORE")
@XStreamAlias("Announcement")
@Audited
@SequenceDef (name="ANNOUNCEMENT_SEQ", schema = "SPFCORE", internetFormat="FEB-%d", intranetFormat="BPM-%d")
public class Announcement {
	
	@Id
	@GeneratedId
	@Column(name = "\"ID\"")
	private String id;
	
	@Enumerated(EnumType.STRING)
	@Column(name="\"STATE\"", length=20)
	private AnnouncementState state;
	
	@Formula("(select ta.IS_PUBLISHED from SPFCORE.MARKETING_CONTENT_PUBLISHING_CONTENT_SET_TASKS as ta where ta.CONTENT_SET_ID = CONTENT_SET_ID)")
	@NotAudited
	@XStreamOmitField
	private Boolean published; 
	
	@Column(name = "\"NAME\"")
	private String name;
	
	@Column(name = "\"MODULE\"")
	private String module;
	
	@ElementCollection( fetch = FetchType.EAGER )
  @CollectionTable( name = "ANNOUNCEMENT_MEMBERGROUP_IDS", schema = "SPFCORE", joinColumns = @JoinColumn( name = "ANNOUNCEMENT_ID" ) )
  @Column( name = "MEMBER_GROUP_ID" )
  @Fetch( value = FetchMode.SELECT )
	private List<String> memberGroupIds;
	
	@ElementCollection( fetch = FetchType.EAGER )
  @CollectionTable( name = "ANNOUNCEMENT_EXEMPTED_BLACKLIST_MODULES", schema = "SPFCORE", joinColumns = @JoinColumn( name = "ANNOUNCEMENT_ID" ) )
  @Column( name = "MODULE_NAME" )
  @Fetch( value = FetchMode.SELECT )
	private List<String> exemptedModules;
	
	@ElementCollection ( fetch = FetchType.EAGER )
	@CollectionTable ( name = "ANNOUNCEMENT_EXCLUSION_LIST", schema = "SPFCORE", joinColumns = @JoinColumn ( name = "ANNOUNCEMENT_ID" ) )
	@Column( name = "EXCLUSION_ID" )
	@Fetch( value = FetchMode.SELECT )
	private List<String> exclusionIds;
	
	@Column(name = "\"CONTENT_SET_ID\"")
	private String contentSetId;
	
	@Column(name = "\"SEND_BY_OFFICE_EMAIL\"")
	private boolean sendByOfficeEmail;
	
	@Column(name = "\"SEND_BY_PREFERRED_CONTACT\"")
	private boolean sendByPreferredContact;
	
	@Column(name = "\"UPDATED_BY\"", length = 30)
	private String updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "\"UPDATED_DATE\"")
	private Date updatedOn;

	@Column(name = "\"SENDER_EMAIL\"", length=256)
	private String senderEmail;
	
	@PostLoad
  protected void onLoad() {
		if (published == null) {
			published = Boolean.FALSE;
		}
	}
	
	public Announcement() {
		super();
	}


	public Announcement(String id, AnnouncementState state, boolean published, String name, String module, List<String> memberGroupIds,
			List<String> exemptedModules, String contentSetId, boolean sendByOfficeEmail, boolean sendByPreferredContact,
			String updatedBy, Date updatedOn, String senderEmail, List<String> exclusionIds) {
		super();
		this.id = id;
		this.state = state;
		this.published = published;
		this.name = name;
		this.module = module;
		this.memberGroupIds = memberGroupIds;
		this.exemptedModules = exemptedModules;
		this.contentSetId = contentSetId;
		this.sendByOfficeEmail = sendByOfficeEmail;
		this.sendByPreferredContact = sendByPreferredContact;
		this.updatedBy = updatedBy;
		this.updatedOn = updatedOn;
		this.senderEmail = senderEmail;
		this.exclusionIds = exclusionIds;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public AnnouncementState getState() {
		return state;
	}


	public void setState(AnnouncementState state) {
		this.state = state;
	}


	public boolean isPublished() {
		return published;
	}


	public void setPublished(boolean published) {
		this.published = published;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public List<String> getMemberGroupIds() {
		return memberGroupIds;
	}


	public void setMemberGroupIds(List<String> memberGroupIds) {
		this.memberGroupIds = memberGroupIds;
	}


	public List<String> getExemptedModules() {
		return exemptedModules;
	}


	public void setExemptedModules(List<String> exemptedModules) {
		this.exemptedModules = exemptedModules;
	}


	public String getContentSetId() {
		return contentSetId;
	}


	public void setContentSetId(String contentSetId) {
		this.contentSetId = contentSetId;
	}


	public boolean isSendByOfficeEmail() {
		return sendByOfficeEmail;
	}


	public void setSendByOfficeEmail(boolean sendByOfficeEmail) {
		this.sendByOfficeEmail = sendByOfficeEmail;
	}


	public boolean isSendByPreferredContact () {
		return sendByPreferredContact;
	}


	public void setSendByPreferredContact (boolean sendByPreferredContact) {
		this.sendByPreferredContact = sendByPreferredContact;
	}


	public String getUpdatedBy() {
		return updatedBy;
	}


	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}


	public Date getUpdatedOn() {
		return updatedOn;
	}


	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}


	public String getSenderEmail() {
		return senderEmail;
	}


	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	
	public String getModule () {
		return module;
	}

	
	public void setModule (String module) {
		this.module = module;
	}

	public List<String> getExclusionIds() {
		return exclusionIds;
	}

	public void setExclusionIds(List<String> exclusionIds) {
		this.exclusionIds = exclusionIds;
	}
	
}
