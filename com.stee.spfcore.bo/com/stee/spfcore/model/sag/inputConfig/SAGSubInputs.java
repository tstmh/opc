package com.stee.spfcore.model.sag.inputConfig;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table(name = "SAG_SUB_INPUTS", schema = "SPFCORE")
@XStreamAlias("SAGSubInputs")
@Audited
public class SAGSubInputs {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	@XStreamOmitField
	private long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "PARENT_INPUT_TYPE", length = 50)
	private SAGInputType parentInputType;
	
	@Column(name = "AWARD_TYPE", length = 5)
	private String awardType;
	
	@Column(name = "PARENT_INPUT_ID", length = 100)
	private String parentInputId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "INPUT_TYPE", length = 50)
	private SAGInputType subInputType;
	
	@Column(name = "INPUT_ID", length = 100)
	private String inputId;
	
	@Column(name = "INPUT_DESCRIPTION", length = 2000)
	private String inputDescription;
	
	@Column(name = "IS_REQUIRED")
	private boolean isRequired = false;
	
	@Column(name = "LIST_ORDER")
	private Integer order;

	public SAGSubInputs() {
		super();
	}

	public SAGSubInputs( SAGInputType parentInputType, String awardType,
			String parentInputId, SAGInputType subInputType, String inputId,
			String inputDescription, Integer order ) {
		super();
		this.parentInputType = parentInputType;
		this.awardType = awardType;
		this.parentInputId = parentInputId;
		this.subInputType = subInputType;
		this.inputId = inputId;
		this.inputDescription = inputDescription;
		this.order = order;
	}

	public long getId() {
		return id;
	}

	public SAGInputType getParentInputType() {
		return parentInputType;
	}

	public String getAwardType() {
		return awardType;
	}

	public String getParentInputId() {
		return parentInputId;
	}

	public SAGInputType getSubInputType() {
		return subInputType;
	}

	public String getInputId() {
		return inputId;
	}

	public String getInputDescription() {
		return inputDescription;
	}

	public Integer getOrder() {
		return order;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) ( id ^ ( id >>> 32 ) );
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
		SAGSubInputs other = (SAGSubInputs) obj;
		return id == other.id;
	}
	
}
