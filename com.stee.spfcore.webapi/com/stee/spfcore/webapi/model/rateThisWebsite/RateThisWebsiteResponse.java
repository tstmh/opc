package com.stee.spfcore.webapi.model.rateThisWebsite;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.stee.spfcore.webapi.model.annotation.GeneratedId;
import com.stee.spfcore.webapi.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"RATETHISWEBSITE_RESPONSES\"", schema = "\"SPFCORE\"")
@XStreamAlias("RateThisWebsiteResponse")
@Audited
@SequenceDef (name="RateThisWebsiteResponseId_SEQ", schema = "SPFCORE", internetFormat="FEB-%d", intranetFormat="BPM-%d")

public class RateThisWebsiteResponse implements Serializable
{
	private static final long serialVersionUID = 1459306056424410153L;

	@Id
	@GeneratedId
	@Column(name = "\"ID\"")
	private String id;
	
	@Column(name = "\"QUESTION_1\"")
	private String question1;
	
	@Column(name = "\"QUESTION_2\"")
	private String question2;
	
	@Column(name = "\"QUESTION_3\"")
	private String question3;
	
	@Column(name = "\"COMMENT\"")
	private String comment;
	
	@Column(name = "\"EMAIL_SENDED\"")
	private boolean emailSended;
	
	@Temporal( TemporalType.TIMESTAMP )
	@Column( name = "UPDATE_TIMESTAMP" )
	private Date updateDateTime;
	
	public RateThisWebsiteResponse()
	{
		super();
	}
	
	public RateThisWebsiteResponse(String id, String question1, String question2, String question3, String comment, Boolean emailSended)
	{
		super();
		this.id = id;
		this.question1 = question1;
		this.question2 = question2;
		this.question3 = question3;
		this.comment = comment;
		this.emailSended = emailSended;
	}

	public String getId() 
	{
		return id;
	}

	public void setId(String id) 
	{
		this.id = id;
	}

	public String getQuestion1() 
	{
		return question1;
	}

	public void setQuestion1(String question1) 
	{
		this.question1 = question1;
	}

	public String getQuestion2() 
	{
		return question2;
	}

	public void setQuestion2(String question2) 
	{
		this.question2 = question2;
	}

	public String getQuestion3() 
	{
		return question3;
	}

	public void setQuestion3(String question3) 
	{
		this.question3 = question3;
	}

	public String getComment() 
	{
		return comment;
	}

	public void setComment(String comment) 
	{
		this.comment = comment;
	}

	public boolean isEmailSended() {
		return emailSended;
	}

	public void setEmailSended(boolean emailSended) {
		this.emailSended = emailSended;
	}
	
	public Date getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime( Date updateDateTime ) {
        this.updateDateTime = updateDateTime;
    }
	
	@Override
	public String toString() {
		return "RateThisWebsiteResponse [id=" + id + ", question1=" + question1
				+ ", question2=" + question2 + ", question3=" + question3
				+ ", comment=" + comment + ", emailSended=" + emailSended + "]";
	}
}

