package com.stee.spfcore.vo.genericEvent;

public class UserChoice {

	private String optionId;
	private String title;
	private int quantity;
	private Double cost;

	public UserChoice() {
		super();
	}

	public UserChoice(String optionId, String title, int quantity, Double cost) {
		super();
		this.optionId = optionId;
		this.title = title;
		this.quantity = quantity;
		this.cost = cost;
	}

	public String getOptionId() {
		return optionId;
	}

	public void setOptionId(String optionId) {
		this.optionId = optionId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

}
