package com.stee.spfcore.model.sag;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.stee.spfcore.model.annotation.GeneratedId;
import com.stee.spfcore.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"SAG_FAMILY_BACKGROUND\"", schema = "\"SPFCORE\"")
@XStreamAlias("SAGFamilyBackground")
@Audited
@SequenceDef (name="SAGFamilyBackground_SEQ", schema = "SPFCORE", internetFormat="FEB-%d", intranetFormat="BPM-%d")
public class SAGFamilyBackground {

	@Id
	@GeneratedId
    @Column( name = "\"ID\"" )
	private String id;

	@Column(name = "\"NAME\"", length = 100)
	private String name;

	@Column(name = "\"RELATIONSHIP\"", length = 50)
	private String relationship;

	@Column(name = "\"ID_TYPE\"", length = 30)
	private String idType;

	@Column(name = "\"ID_NO\"", length = 50)
	private String idNo;

	@Temporal(TemporalType.DATE)
	@Column(name = "\"DATE_OF_BIRTH\"")
	private Date dateofBirth;

	@Column(name = "\"OCCUPATION\"", length = 100)
	private String occupation;

	@Column(name = "\"GROSS_SALARY\"")
	private Double grossSalary;

	@Column(name = "\"SPECIAL_ALLOWANCE\"")
	private Double specialAllowance;
	
	@Column(name = "\"OTHER_RELATIONSHIP\"", length = 50)
	private String otherRelationship;

	public SAGFamilyBackground() {
		super();
	}

	public SAGFamilyBackground( String name, String relationship,
			String idType, String idNo, Date dateofBirth, String occupation,
			Double grossSalary, Double specialAllowance, String otherRelationship) {
		super();
		this.name = name;
		this.relationship = relationship;
		this.idType = idType;
		this.idNo = idNo;
		this.dateofBirth = dateofBirth;
		this.occupation = occupation;
		this.grossSalary = grossSalary;
		this.specialAllowance = specialAllowance;
		this.otherRelationship = otherRelationship;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship( String relationship ) {
		this.relationship = relationship;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType( String idType ) {
		this.idType = idType;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo( String idNo ) {
		this.idNo = idNo;
	}

	public Date getDateofBirth() {
		return dateofBirth;
	}

	public void setDateofBirth( Date dateofBirth ) {
		this.dateofBirth = dateofBirth;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation( String occupation ) {
		this.occupation = occupation;
	}

	public Double getGrossSalary() {
		return grossSalary;
	}

	public void setGrossSalary( Double grossSalary ) {
		this.grossSalary = grossSalary;
	}

	public Double getSpecialAllowance() {
		return specialAllowance;
	}

	public void setSpecialAllowance( Double specialAllowance ) {
		this.specialAllowance = specialAllowance;
	}

	public String getId() {
		return id;
	}

	public String getOtherRelationship() {
		return otherRelationship;
	}

	public void setOtherRelationship(String otherRelationship) {
		this.otherRelationship = otherRelationship;
	}

	public void preSave() {
		if ( name != null ) {
			name = name.toUpperCase();
		}

		if ( relationship != null ) {
			relationship = relationship.toUpperCase();
		}
		
		if ( otherRelationship != null ) {
			otherRelationship = otherRelationship.toUpperCase();
		}

		if ( idType != null ) {
			idType = idType.toUpperCase();
		}

		if ( idNo != null ) {
			idNo = idNo.toUpperCase();
		}

		if ( occupation != null ) {
			occupation = occupation.toUpperCase();
		}
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
		SAGFamilyBackground other = (SAGFamilyBackground) obj;
		if ( id == null ) 
		{
			if ( other.id != null )
				return false;
		}
		else if ( !id.equals( other.id ) )
			return false;
		return true;
	}

}
