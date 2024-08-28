package com.stee.spfcore.vo.sag;

public class SAGApplicationFamilyBackground {

	private String memberNric;

	private String referenceNumber;

	private double Pci;

	public SAGApplicationFamilyBackground() {
		super();
	}

	public SAGApplicationFamilyBackground(String memberNric, String referenceNumber, float Pci) {
		this.memberNric = memberNric;
		this.referenceNumber = referenceNumber;
		this.Pci = Pci;
	}

	public String getMemberNric() {
		return memberNric;
	}

	public void setMemberNric( String memberNric ) {
		this.memberNric = memberNric;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber( String referenceNumber ) {
		this.referenceNumber = referenceNumber;
	}

	public double getPci() {
		return Pci;
	}

	public void setPci( double Pci ) {
		this.Pci = Pci;
	}
}
