package com.stee.spfcore.webapi.model.genericEvent;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;

import com.stee.spfcore.webapi.model.annotation.GeneratedId;
import com.stee.spfcore.webapi.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "GENERIC_EVENT_TICKETING_SECTIONS", schema = "SPFCORE")
@XStreamAlias("GenericTicketingSection")
@Audited
@SequenceDef (name="GenericEventTicketingSectionId_SEQ", schema = "SPFCORE", internetFormat="FEB-ETS-%d", intranetFormat="BPM-ETS-%d")

public class GETicketingSection {

	@Id
	@GeneratedId
	@Column(name = "\"ID\"")
	private String id;
	
	@Column(name = "\"NAME\"")
	private String name;
	
	@Column(name = "\"DESCRIPTION\"")
	private String description;
	
	@Column(name = "\"IS_SINGLE_SELECT\"")
	private boolean isSingleSelect;
	
	@Column(name = "\"IS_SHOW_PRICE\"")
	private boolean isShowPrice;
	
	@Column(name = "\"IS_VISIBLE\"")
	private boolean isVisible;
	
	@Column(name = "\"IS_MANDATORY\"")
	private boolean isMandatory;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "\"SECTION_ID\"")
	@Fetch(value = FetchMode.SELECT)
	@OrderColumn(name = "\"LIST_ORDER\"")
	private List<GETicketingOption> ticketingOption;
	
	@Column(name = "\"MAX_QTY\"")
	private int maxQty;
	
	@Column(name = "\"UNIT_PRICE\"")
	private double unitPrice;
	
	@Column(name = "\"AVAILABLE_QTY\"")
	private int availableQty;
	
	// CONSTRUCTOR
	
	public GETicketingSection() 
	{
		super();
	}
	
	public GETicketingSection(String id, String name, String description,
			boolean isSingleSelect, boolean isShowPrice, boolean isVisible,
			boolean isMandatory, List<GETicketingOption> ticketingOption, int maxQty,
			float unitPrice, int availableQty) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.isSingleSelect = isSingleSelect;
		this.isShowPrice = isShowPrice;
		this.isVisible = isVisible;
		this.isMandatory = isMandatory;
		this.ticketingOption = ticketingOption;
		this.maxQty = maxQty;
		this.unitPrice = unitPrice;
		this.availableQty = availableQty;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isSingleSelect() {
		return isSingleSelect;
	}

	public void setSingleSelect(boolean isSingleSelect) {
		this.isSingleSelect = isSingleSelect;
	}

	public boolean isShowPrice() {
		return isShowPrice;
	}

	public void setShowPrice(boolean isShowPrice) {
		this.isShowPrice = isShowPrice;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public boolean isMandatory() {
		return isMandatory;
	}

	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public List<GETicketingOption> getTicketingOption() {
		return ticketingOption;
	}

	public void setTicketingOption(List<GETicketingOption> ticketingOption) {
		this.ticketingOption = ticketingOption;
	}
	
	public int getMaxQty() {
		return maxQty;
	}
	
	public void setMaxQty(int maxQty) {
		this.maxQty = maxQty;
	}
	
	public double getUnitPrice() {
		return unitPrice;
	}
	
	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	public int getAvailableQty() {
		return availableQty;
	}
	
	public void setAvailableQty(int availableQty) {
		this.availableQty = availableQty;
	}
}

