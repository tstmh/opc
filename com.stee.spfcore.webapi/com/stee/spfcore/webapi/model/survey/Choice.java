package com.stee.spfcore.webapi.model.survey;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.stee.spfcore.webapi.model.annotation.GeneratedId;
import com.stee.spfcore.webapi.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table(name = "\"SURVEY_USER_CHOICES\"", schema = "\"SPFCORE\"")
@XStreamAlias("SurveyChoice")
@Audited
@SequenceDef (name="SurveyChoiceId_SEQ", schema = "SPFCORE", internetFormat="FEB-%d", intranetFormat="BPM-%d")
public class Choice implements Serializable {

	private static final long serialVersionUID = 5600250389747014009L;

	@Id
	@GeneratedId
	@Column(name = "\"ID\"")
	private String id;
	
	@Column(name = "\"OPTION_ID\"")
	private String optionId;
	
	@Formula("(select c.NAME from SPFCORE.SURVEY_QUESTION_OPTIONS c where c.ID = OPTION_ID)")
	@NotAudited
	@XStreamOmitField
	private String name;

	public Choice() {
		super();
	}

	public Choice(String id, String optionId) {
		super();
		this.id = id;
		this.optionId = optionId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOptionId() {
		return optionId;
	}

	public void setOptionId(String optionId) {
		this.optionId = optionId;
	}

	public String getName() {
		return name;
	}
}
