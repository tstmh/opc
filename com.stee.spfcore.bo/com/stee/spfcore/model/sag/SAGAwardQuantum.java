package com.stee.spfcore.model.sag;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.stee.spfcore.model.annotation.GeneratedId;
import com.stee.spfcore.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"SAG_AWARD_QUANTUM\"", schema = "\"SPFCORE\"")
@XStreamAlias("SAGAwardQuantum")
@Audited
@SequenceDef (name="SAGAwardQuantum_SEQ", schema = "SPFCORE", internetFormat="FEB-%d", intranetFormat="BPM-%d")
public class SAGAwardQuantum {

	@Id
	@GeneratedId
    @Column( name = "\"ID\"" )
	private String id;
	
	@Column( name = "\"FINANCIAL_YEAR\"", length=10 )
	private String financialYear;
	
	@Column( name = "\"AWARD_TYPE\"", length=10 )
	private String awardType;
	
	@Column( name = "\"SUB_TYPE\"", length=10 )
	private String subType;
	
	@Column( name = "\"AMOUNT\"" )
	private Double amount;

	public SAGAwardQuantum() {
		super();
	}

	public SAGAwardQuantum( String financialYear, String awardType,
			String subType, Double amount ) {
		super();
		this.financialYear = financialYear;
		this.awardType = awardType;
		this.subType = subType;
		this.amount = amount;
	}

	public String getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear( String financialYear ) {
		this.financialYear = financialYear;
	}

	public String getAwardType() {
		return awardType;
	}

	public void setAwardType( String awardType ) {
		this.awardType = awardType;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType( String subType ) {
		this.subType = subType;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount( Double amount ) {
		this.amount = amount;
	}

	public String getId() {
		return id;
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
		SAGAwardQuantum other = (SAGAwardQuantum) obj;
		if ( id == null ) {
			if ( other.id != null )
				return false;
		} else if ( !id.equals( other.id ) )
			return false;
		return true;
	}
}
