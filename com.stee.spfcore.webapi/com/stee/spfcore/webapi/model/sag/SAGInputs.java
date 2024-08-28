package com.stee.spfcore.webapi.model.sag;

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
@Table(name = "SAG_INPUTS", schema = "SPFCORE")
@XStreamAlias("SAGInputs")
@Audited
public class SAGInputs {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	@XStreamOmitField
	private long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "INPUT_TYPE", length=50)
	private SAGInputType sagInputType;
	
	@Column(name = "AWARD_TYPE", length=5)
	private String awardType;
	
	@Column(name = "INPUT_ID", length=100)
	private String inputId;
	
	@Column(name = "INPUT_DESCRIPTION", length=2000)
	private String inputDescription;
	
	@Column(name = "LIST_ORDER")
	private Integer order;

	public SAGInputs() {
		super();
	}

	public SAGInputs( SAGInputType sagInputType, String awardType,
			String inputId, String inputDescription, Integer order ) {
		super();
		this.sagInputType = sagInputType;
		this.awardType = awardType;
		this.inputId = inputId;
		this.inputDescription = inputDescription;
		this.order = order;
	}

	public long getId() {
		return id;
	}

	public SAGInputType getSagInputType() {
		return sagInputType;
	}

	public String getAwardType() {
		return awardType;
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
		SAGInputs other = (SAGInputs) obj;
		if ( id != other.id )
			return false;
		return true;
	}
	
}

