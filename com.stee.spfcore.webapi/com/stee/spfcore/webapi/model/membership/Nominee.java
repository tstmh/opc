package com.stee.spfcore.webapi.model.membership;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table(name="\"INSURANCE_NOMINEE_DETAILS\"", schema="\"SPFCORE\"")
@XStreamAlias("Nominee")
@Audited
public class Nominee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="\"ID\"")
	@XStreamOmitField
	private long id;
	
	@Column(name="\"ID_TYPE\"")
	private String idType;
	
	@Column(name="\"NRIC\"", length=50)
	private String nric;
	
	@Column(name="\"NAME\"", length=255)
	private String name;
	
	@Column(name="\"RELATIONSHIP\"", length=50)
	private String relationship;
	
	@Column(name="\"PERCENTAGE\"")
	private double percentage;
	
	@Column(name="\"OTHER_RELATION\"", length=50)
	private String otherRelation;

	public Nominee() {
		super();
	}

	public Nominee(String idType, String nric, String name,
			String relationship, double percentage, String otherRelation) {
		super();
		this.idType = idType;
		this.nric = nric;
		this.name = name;
		this.relationship = relationship;
		this.percentage = percentage;
		this.otherRelation = otherRelation;
	}


	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getNric() {
		return nric;
	}

	public void setNric(String nric) {
		this.nric = nric;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public String getOtherRelation() {
		return otherRelation;
	}

	public void setOtherRelation(String otherRelation) {
		this.otherRelation = otherRelation;
	}

	public void preSave () {
		
		if (name != null) {
			name = name.toUpperCase();
		}
		
		if (nric != null) {
			nric = nric.toUpperCase();
		}
		
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Nominee other = (Nominee) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
