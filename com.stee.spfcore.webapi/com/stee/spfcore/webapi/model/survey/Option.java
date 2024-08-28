package com.stee.spfcore.webapi.model.survey;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.stee.spfcore.webapi.model.annotation.GeneratedId;
import com.stee.spfcore.webapi.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"SURVEY_QUESTION_OPTIONS\"", schema = "\"SPFCORE\"")
@XStreamAlias("SurveyOption")
@Audited
@SequenceDef (name="SurveyOptionId_SEQ", schema = "SPFCORE", internetFormat="FEB-%d", intranetFormat="BPM-%d")
public class Option {

	@Id
	@GeneratedId
	@Column(name = "\"ID\"")
	private String id;
	
	@Column(name = "\"NAME\"", length = 100)
	private String name;

	@Column(name = "\"VALUE\"")
	private Integer value;

	public Option() {
		super();
	}

	public Option(String id, String name, Integer value) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
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

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
}
