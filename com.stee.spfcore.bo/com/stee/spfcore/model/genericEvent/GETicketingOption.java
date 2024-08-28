package com.stee.spfcore.model.genericEvent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.stee.spfcore.model.annotation.GeneratedId;
import com.stee.spfcore.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "GENERIC_EVENT_TICKETING_OPTIONS", schema = "SPFCORE")
@XStreamAlias("GenericTicketingOption")
@Audited
@SequenceDef (name="GenericEventTicketingOptionsId_SEQ", schema = "SPFCORE", internetFormat="FEB-ETO-%d", intranetFormat="BPM-ETO-%d")
public class GETicketingOption {
	
	@Id
	@GeneratedId
	@Column(name = "\"ID\"")
	private String id;
	
	@Column(name = "\"TITLE\"", length=100)
	private String title;
	
	@Column(name = "\"MAX\"")
	private int max;
	
	@Column(name = "\"MIN\"")
	private int min;
	
	@Column(name = "\"UNIT_COST\"")
	private Double unitCost;

	public GETicketingOption() {
		super();
	}

	public GETicketingOption(String id, String title, int max, int min, Double unitCost) {
		super();
		this.id = id;
		this.title = title;
		this.max = max;
		this.min = min;
		this.unitCost = unitCost;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public Double getUnitCost() {
		return unitCost;
	}

	public void setUnitCost(Double unitCost) {
		this.unitCost = unitCost;
	}
}
