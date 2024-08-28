package com.stee.spfcore.vo.sag;

import com.stee.spfcore.model.sag.SAGApplication;

public class SAGApplicationSelect {

	private boolean isSelected;

	private Double Pci;

	private SAGApplication sagApplication;

	private String poName;

	public SAGApplicationSelect() {
		super();
	}

	public SAGApplicationSelect(boolean isSelected, Double Pci, SAGApplication sagApplication, String poName) {
		this.isSelected = isSelected;
		this.Pci = Pci;
		this.sagApplication = sagApplication;
		this.poName = poName;
	}

	public boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected( boolean isSelected) {
		this.isSelected = isSelected;
	}

	public Double getPci() {
		return Pci;
	}

	public void setPci( Double Pci ) {
		this.Pci = Pci;
	}

	public SAGApplication getSAGApplication() {
		return sagApplication;
	}

	public void setSAGApplication( SAGApplication sagApplication ) {
		this.sagApplication = sagApplication;
	}

	public String getPoName() {
		return poName;
	}

	public void setPoName( String poName ) {
		this.poName = poName;
	}
}
