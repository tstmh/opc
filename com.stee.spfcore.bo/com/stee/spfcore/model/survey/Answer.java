package com.stee.spfcore.model.survey;

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

import com.stee.spfcore.model.annotation.GeneratedId;
import com.stee.spfcore.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"SURVEY_ANSWERS\"", schema = "\"SPFCORE\"")
@XStreamAlias("SurveyAnswer")
@Audited
@SequenceDef (name="SurveyAnswerId_SEQ", schema = "SPFCORE", internetFormat="FEB-%d", intranetFormat="BPM-%d")
public class Answer implements Serializable {

	private static final long serialVersionUID = -1788903849890158262L;
	
	@Id
	@GeneratedId
	@Column(name = "\"ID\"")
	private String id;
	
	@Column(name = "\"QUESTION_ID\"")
	private String questionId;
	
	@Column(name = "\"TEXT_VALUE\"", length=2000)
	private String textValue;
	
	@Column(name = "\"INT_VALUE\"")
	private int intValue;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "\"ANSWER_ID\"")
	@Fetch(value = FetchMode.SUBSELECT)
	@OrderColumn(name = "\"LIST_ORDER\"")
	private List<Choice> choices;

	public Answer() {
		super();
		choices = new ArrayList<>();
	}

	public Answer(String id, String questionId, String textValue, int intValue, List<Choice> choices) {
		super();
		this.id = id;
		this.questionId = questionId;
		this.textValue = textValue;
		this.intValue = intValue;
		this.choices = choices;
		
		if (this.choices == null) {
			this.choices = new ArrayList<>();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getTextValue() {
		return textValue;
	}

	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}

	public int getIntValue() {
		return intValue;
	}

	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}

	public List<Choice> getChoices() {
		return choices;
	}

	public void setChoices(List<Choice> choices) {
		this.choices = choices;
	}
	
	@Override
	public String toString() {
		return "Answer [id=" + id + ", questionId=" + questionId
				+ ", textValue=" + textValue + ", intValue=" + intValue
				+ ", choices=" + choices + "]";
	}
}
