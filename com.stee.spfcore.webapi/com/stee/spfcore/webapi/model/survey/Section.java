package com.stee.spfcore.webapi.model.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;

import com.stee.spfcore.webapi.model.annotation.GeneratedId;
import com.stee.spfcore.webapi.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"SURVEY_SECTIONS\"", schema = "\"SPFCORE\"")
@XStreamAlias("SurveySection")
@Audited
@SequenceDef (name="SurveySectionId_SEQ", schema = "SPFCORE", internetFormat="FEB-%d", intranetFormat="BPM-%d")
public class Section implements Serializable {

	private static final long serialVersionUID = 3891977278073574914L;

	@Id
	@Column(name = "\"ID\"")
	@GeneratedId
	private String id;
	
	@Column(name = "\"TITLE\"", length = 200)
	private String title;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "\"SECTION_ID\"")
	@Fetch(value = FetchMode.SUBSELECT)
	@OrderColumn(name = "\"LIST_ORDER\"")
	private List<Question> questions;

	public Section() {
		super();
		this.questions = new ArrayList<Question>();
	}

	public Section(String id, String title, List<Question> questions) {
		super();
		this.id = id;
		this.title = title;
		this.questions = questions;
		
		if (this.questions == null) {
			this.questions = new ArrayList<Question>();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
	
}
