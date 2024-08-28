package com.stee.spfcore.model.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;

import com.stee.spfcore.model.annotation.GeneratedId;
import com.stee.spfcore.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"SURVEYS\"", schema = "\"SPFCORE\"")
@XStreamAlias("Survey")
@Audited
@SequenceDef (name="SurveyId_SEQ", schema = "SPFCORE", internetFormat="FEB-%d", intranetFormat="BPM-%d")
public class Survey implements Serializable {

	private static final long serialVersionUID = 448323852961871411L;
	
	@Id
	@GeneratedId
	@Column(name = "\"ID\"")
	private String id;
	
	@Column(name = "\"NAME\"", length = 200)
	private String name;

	@Column(name = "\"DESCRIPTION\"", length = 2000)
	private String description;
	
	@Column(name = "\"MODULE\"", length = 100)
	private String module;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "\"SURVEY_ID\"")
	@Fetch(value = FetchMode.SUBSELECT)
	@OrderColumn(name = "\"LIST_ORDER\"")
	private List<Section> sections;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "\"SURVEY_ID\"")
	@Fetch(value = FetchMode.SUBSELECT)
	@OrderColumn(name = "\"LIST_ORDER\"")
	private List<SurveyBroadcast> broadcasts;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "\"STATUS\"", length = 30)
	private SurveyStatus status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"PUBLISH_DATE\"")
	private Date publishDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"END_DATE\"")
	private Date endDate;
	
	public Survey() {
		super();
		this.sections = new ArrayList<>();
	}

	public Survey(String id, String name, String description, String module, List<Section> sections, List<SurveyBroadcast> broadcasts) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.module = module;
		this.sections = sections;
		this.broadcasts = broadcasts;
		
		// Due to bug in Hibernate, the sections cannot be null.
		// Null will cause exception when merge with DB.
		// https://hibernate.atlassian.net/browse/HHH-7726
		if (this.sections == null) {
			this.sections = new ArrayList<>();
		}
		
		if (this.broadcasts == null) {
			this.broadcasts = new ArrayList<>();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public List<Section> getSections() {
		return sections;
	}

	public void setSections(List<Section> sections) {
		this.sections = sections;
	}

	public List<SurveyBroadcast> getBroadcasts() {
		return broadcasts;
	}

	public void setBroadcasts(List<SurveyBroadcast> broadcasts) {
		this.broadcasts = broadcasts;
	}

	public SurveyStatus getStatus() {
		return status;
	}

	public void setStatus(SurveyStatus status) {
		this.status = status;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}
