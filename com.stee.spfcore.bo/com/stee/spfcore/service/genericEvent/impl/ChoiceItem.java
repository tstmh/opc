package com.stee.spfcore.service.genericEvent.impl;

public class ChoiceItem {

	private String name;
	private int quantity;
	private String cost;
	
	public ChoiceItem () {
	}
	
	public ChoiceItem (String name, int quantity, String cost) {
		this.name = name;
		this.quantity = quantity;
		this.cost = cost;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
}
