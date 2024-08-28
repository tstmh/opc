package com.stee.spfcore.model.survey;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.stee.spfcore.model.annotation.GeneratedId;
import com.stee.spfcore.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"SURVEY_BROADCASTS\"", schema = "\"SPFCORE\"")
@XStreamAlias("SurveyBroadcast")
@Audited
@SequenceDef (name="SurveyBroadcastId_SEQ", schema = "SPFCORE", internetFormat="FEB-%d", intranetFormat="BPM-%d")
public class SurveyBroadcast {

	@Id
	@Column(name = "\"ID\"")
	@GeneratedId
	private String id;

	@Column(name = "\"SUBJECT\"", length = 200)
	private String subject;

	@Column(name = "\"CONTENT\"", length = 4000)
	private String content;

	@Column(name = "\"DAY_OFFSET\"")
	private int offset;

	public SurveyBroadcast() {
		super();
	}

	public SurveyBroadcast(String id, String subject, String content, int offset) {
		super();
		this.id = id;
		this.subject = subject;
		this.content = content;
		this.offset = offset;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

}
