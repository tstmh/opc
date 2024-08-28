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
@Table(name = "GENERIC_EVENT_TICKETING_CHOICES", schema = "SPFCORE")
@XStreamAlias("GenericTicketingChoice")
@Audited
@SequenceDef (name="GenericEventTicketingChoicesId_SEQ", schema = "SPFCORE", internetFormat="FEB-CTO-%d", intranetFormat="BPM-CTO-%d")
public class GETicketingChoice {

	@Id
	@GeneratedId
	@Column(name = "\"ID\"")
	private String id;
	
	@Column(name = "\"OPTION_ID\"")
	private String optionId;
	
	@Column(name = "\"INT_VALUE\"")
	private int value;

	public GETicketingChoice() {
		super();
	}

	public GETicketingChoice(String id, String optionId, int value) {
		super();
		this.id = id;
		this.optionId = optionId;
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOptionId() {
		return optionId;
	}

	public void setOptionId(String optionId) {
		this.optionId = optionId;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
