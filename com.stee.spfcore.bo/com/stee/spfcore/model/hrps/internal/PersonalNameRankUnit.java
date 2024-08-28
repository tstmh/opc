package com.stee.spfcore.model.hrps.internal;

import com.stee.spfcore.vo.personnel.PersonalNricName;

public class PersonalNameRankUnit extends PersonalNricName {
	private String rank;
	private String unit;
	
	public PersonalNameRankUnit () {
		super();
	}
	
	public PersonalNameRankUnit(String rank, String unit) {
		super();
		this.rank = rank;
		this.unit = unit;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
}
