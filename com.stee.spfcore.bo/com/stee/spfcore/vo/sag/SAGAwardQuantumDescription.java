package com.stee.spfcore.vo.sag;

import com.stee.spfcore.model.sag.SAGAwardQuantum;

public class SAGAwardQuantumDescription {
	
	private SAGAwardQuantum awardQuantum;
	
	private String description;
	
	public SAGAwardQuantumDescription () {
		super();	
	}

	public SAGAwardQuantumDescription(SAGAwardQuantum awardQuantum,
			String description) {
		super();
		this.awardQuantum = awardQuantum;
		this.description = description;
	}

	public SAGAwardQuantum getAwardQuantum() {
		return awardQuantum;
	}

	public void setAwardQuantum(SAGAwardQuantum awardQuantum) {
		this.awardQuantum = awardQuantum;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
