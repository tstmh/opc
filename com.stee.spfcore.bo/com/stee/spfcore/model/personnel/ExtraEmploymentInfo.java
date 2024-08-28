package com.stee.spfcore.model.personnel;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"PERSONAL_EMPLOYMENT_EXTRA_INFO\"", schema = "\"SPFCORE\"")
@XStreamAlias( "ExtraEmploymentInfo" )
@Audited
public class ExtraEmploymentInfo {
	
	@Id
	@Column(name = "\"NRIC\"", length = 10)
	private String nric;
	
	@Column(name = "\"PRESENT_UNIT\"", length = 50)
	private String presentUnit;
	
	@Column(name = "\"PRESENT_DESIGNATION\"", length = 50)
	private String presentDesignation;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "\"CURRENT_SCHEME_DATE\"")
	private Date currentSchemeDate;
	
	public ExtraEmploymentInfo () {
		super();
	}
	
	public ExtraEmploymentInfo (String nric, String presentUnit, String presentDesignation, Date currentSchemeDate) {
		super();
		this.nric = nric;
		this.presentUnit = presentUnit;
		this.presentDesignation = presentDesignation;
		this.currentSchemeDate = currentSchemeDate;
	}
	
	public String getNric () {
		return nric;
	}
	
	public void setNric (String nric) {
		this.nric = nric;
	}
	
	public String getPresentUnit () {
		return presentUnit;
	}
	
	public void setPresentUnit (String presentUnit) {
		this.presentUnit = presentUnit;
	}
	
	public String getPresentDesignation () {
		return presentDesignation;
	}
	
	public void setPresentDesignation (String presentDesignation) {
		this.presentDesignation = presentDesignation;
	}
	
	public Date getCurrentSchemeDate () {
		return currentSchemeDate;
	}
	
	public void setCurrentSchemeDate (Date currentSchemeDate) {
		this.currentSchemeDate = currentSchemeDate;
	}
}
