package com.stee.spfcore.webapi.model.corporateCard;

public enum CardTypeDetailStatus {

	DRAFT ("Draft"),
	CREATED ("Created");
	
	private String value;

	private CardTypeDetailStatus (String value) {
		this.value = value;
	}
	
	public String toString() {
		return value;
	}
	
	public static CardTypeDetailStatus getCardTypeDetailStatus (String value) {
		CardTypeDetailStatus [] cardTypeDetailStatuses = CardTypeDetailStatus.values();
		
		for (CardTypeDetailStatus cardTypeDetailStatus : cardTypeDetailStatuses) {
			if (cardTypeDetailStatus.value.equals(value)) {
				return cardTypeDetailStatus;
			}
		}
		
		return null;
	}
	
}
