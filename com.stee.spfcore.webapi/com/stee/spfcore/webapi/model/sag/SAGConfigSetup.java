package com.stee.spfcore.webapi.model.sag;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.stee.spfcore.webapi.model.annotation.GeneratedId;
import com.stee.spfcore.webapi.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"SAG_CONFIG_SETUP\"", schema = "\"SPFCORE\"")
@XStreamAlias("SAGConfigSetup")
@Audited
@SequenceDef (name="SAGConfigSetup_SEQ", schema = "SPFCORE", internetFormat="FEB-%d", intranetFormat="BPM-%d")
public class SAGConfigSetup {

	@Id
	@GeneratedId
    @Column( name = "\"ID\"" )
	private String id;
	
	@Column(name="\"FINANCIAL_YEAR\"", length=10)
	private String financialYear;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "\"CONFIG_TYPE\"", length = 50)
	private SAGDateConfigType configType;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "\"CONFIGURED_DATE\"")
	private Date configuredDate;
	
	@Column(name = "\"CONFIGURED_TIME_IN_DAYS\"")
	private Integer configuredTimeInDays;
	
	@Column(name="\"UPDATED_BY\"", length=100)
	private String updatedBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "\"UPDATED_DATE\"")
	private Date updatedDate;
	
	@Column(name="\"BOOLEAN_VALUE\"")
	private boolean booleanValue;
	
	@Column(name="\"INT_VALUE\"")
	private Integer intValue;
	
	@Column(name="\"STRING_VALUE\"")
	private String stringValue;
	
	public SAGConfigSetup() {
		super();
	}

	public SAGConfigSetup( String financialYear, SAGDateConfigType configType,
			Date configuredDate, Integer configuredTimeInDays,
			String updatedBy, Date updatedDate, boolean booleanValue, Integer intValue, String stringValue ) {
		super();
		this.financialYear = financialYear;
		this.configType = configType;
		this.configuredDate = configuredDate;
		this.configuredTimeInDays = configuredTimeInDays;
		this.updatedBy = updatedBy;
		this.updatedDate = updatedDate;
		this.booleanValue = booleanValue;
		this.intValue = intValue;
		this.stringValue = stringValue;
		
	}

	public String getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear( String financialYear ) {
		this.financialYear = financialYear;
	}

	public SAGDateConfigType getConfigType() {
		return configType;
	}

	public void setConfigType( SAGDateConfigType configType ) {
		this.configType = configType;
	}

	public Date getConfiguredDate() {
		return configuredDate;
	}

	public void setConfiguredDate( Date configuredDate ) {
		this.configuredDate = configuredDate;
	}

	public Integer getConfiguredTimeInDays() {
		return configuredTimeInDays;
	}

	public void setConfiguredTimeInDays( Integer configuredTimeInDays ) {
		this.configuredTimeInDays = configuredTimeInDays;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy( String updatedBy ) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate( Date updatedDate ) {
		this.updatedDate = updatedDate;
	}

	public String getId() {
		return id;
	}

	public boolean isBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	public Integer getIntValue() {
		return intValue;
	}

	public void setIntValue( Integer intValue ) {
		this.intValue = intValue;
	}
	
	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue( String stringValue ) {
		this.stringValue = stringValue;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
		return result;
	}

	@Override
	public boolean equals( Object obj ) {
		if ( this == obj )
			return true;
		if ( obj == null )
			return false;
		if ( getClass() != obj.getClass() )
			return false;
		SAGConfigSetup other = (SAGConfigSetup) obj;
		if ( id == null ) {
			if ( other.id != null )
				return false;
		} else if ( !id.equals( other.id ) )
			return false;
		return true;
	}

}
