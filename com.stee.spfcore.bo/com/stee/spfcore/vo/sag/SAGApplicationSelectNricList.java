package com.stee.spfcore.vo.sag;

import java.util.List;

public class SAGApplicationSelectNricList {

	private String memberNric;
	
	private String memberName;
	
	private List<SAGApplicationSelect> sagApplicationSelect;
	
	public SAGApplicationSelectNricList() {
		super();
	}
	
	public SAGApplicationSelectNricList(String memberNric, String memberName, List<SAGApplicationSelect> sagApplicationSelect ) {
		this.memberNric = memberNric;
		this.memberName = memberName;
		this.sagApplicationSelect = sagApplicationSelect;
		
	}
	
	public String getMemberNric() {
		return memberNric;
	}
	

	public void setMemberNric( String memberNric ) {
		this.memberNric = memberNric;
	}
	
	public String getMemberName() {
		return memberName;
	}
	
	public void setMemberName( String memberName ) {
		this.memberName = memberName;
	}
	
	public List<SAGApplicationSelect> getSAGApplicationSelect() {
		return sagApplicationSelect;
	}

	public void setSAGApplicationSelect( List<SAGApplicationSelect> sagApplicationSelect ) {
		this.sagApplicationSelect = sagApplicationSelect;
	}
}
