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
@Table(name = "\"SAG_PRIVILEGES\"", schema = "\"SPFCORE\"")
@XStreamAlias("SAGPrivileges")
@Audited
@SequenceDef (name="SAGPrivileges_SEQ", schema = "SPFCORE", internetFormat="FEB-%d", intranetFormat="BPM-%d")
public class SAGPrivileges {
	
	@Id
	@GeneratedId
    @Column( name = "\"ID\"" )
	private String id;
	
	@Column(name="\"FINANCIAL_YEAR\"", length=10)
	private String financialYear;
	
	@Column(name="\"MEMBER_NRIC\"", length=10)
	private String memberNric;
	
	@Column(name="\"COMMENTS\"", length=255)
	private String comments;

	public SAGPrivileges() {
		super();
	}

	public SAGPrivileges( String financialYear, String memberNric,
			String comments ) {
		super();
		this.financialYear = financialYear;
		this.memberNric = memberNric;
		this.comments = comments;
	}

	public String getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear( String financialYear ) {
		this.financialYear = financialYear;
	}

	public String getMemberNric() {
		return memberNric;
	}

	public void setMemberNric( String memberNric ) {
		this.memberNric = memberNric;
	}

	public String getComments() {
		return comments;
	}

	public void setComments( String comments ) {
		this.comments = comments;
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
		SAGPrivileges other = (SAGPrivileges) obj;
		if ( id == null ) {
			if ( other.id != null )
				return false;
		} else if ( !id.equals( other.id ) )
			return false;
		return true;
	}
}
