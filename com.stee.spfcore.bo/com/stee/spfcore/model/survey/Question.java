package com.stee.spfcore.model.survey;

import java.io.Serializable;
import java.util.ArrayList;
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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;

import com.stee.spfcore.model.annotation.GeneratedId;
import com.stee.spfcore.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"SURVEY_QUESTIONS\"", schema = "\"SPFCORE\"")
@XStreamAlias("SurveyQuestion")
@Audited
@SequenceDef (name="SurveyQuestionId_SEQ", schema = "SPFCORE", internetFormat="FEB-%d", intranetFormat="BPM-%d")
public class Question implements Serializable {

	private static final long serialVersionUID = 6195922640393635302L;

	@Id
	@GeneratedId
	@Column(name = "\"ID\"")
	private String id;

	@Column(name = "\"CONTENT\"", length = 200)
	private String content;

	@Enumerated(EnumType.STRING)
	@Column(name = "\"TYPE\"", length = 20)
	private QuestionType type;

	@Column(name = "\"MIN_VALUE\"")
	private Integer minValue;

	@Column(name = "\"MAX_VALUE\"")
	private Integer maxValue;

	@Column(name = "\"TEXT_LENGTH\"")
	private Integer maxLength;

	@Column(name = "\"SLIDER_STEP_WIDTH\"")
	private Integer stepWidth;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "\"QUESTION_ID\"")
	@Fetch(value = FetchMode.SUBSELECT)
	@OrderColumn(name = "\"LIST_ORDER\"")
	private List<Option> options;

	public Question() {
		super();
		options = new ArrayList<>();
	}

	public Question(String id, String content, QuestionType type, int minValue, int maxValue, int maxLength, int stepWidth,
			List<Option> options) {
		super();
		this.id = id;
		this.content = content;
		this.type = type;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.maxLength = maxLength;
		this.stepWidth = stepWidth;
		this.options = options;
		
		if (this.options == null) {
			this.options = new ArrayList<>();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public QuestionType getType() {
		return type;
	}

	public void setType(QuestionType type) {
		this.type = type;
	}

	public int getMinValue() {
		if (minValue == null)
		{
			minValue = 0;
		}
		return minValue;
	}

	public void setMinValue(Integer minValue) {
		this.minValue = minValue;
	}

	public int getMaxValue() {
		if (maxValue == null)
		{
			maxValue = 0;
		}
		return maxValue;
	}

	public void setMaxValue(Integer maxValue) {
		this.maxValue = maxValue;
	}

	public int getMaxLength() {
		if (maxLength == null)
		{
			maxLength = 0;
		}
		return maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	public int getStepWidth() {
		if (stepWidth == null)
		{
			stepWidth = 0;
		}
		return stepWidth;
	}

	public void setStepWidth(Integer stepWidth) {
		this.stepWidth = stepWidth;
	}

	public List<Option> getOptions() {
		return options;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
	}
}
