package com.stee.spfcore.webapi.model.marketing;


public enum SortOrder {
	
	ASCENDING ("Ascending"),
	DESCENDING ("Descending");
	
	private String order;
	
	private SortOrder (String order) {
		this.order = order;
	}
	
	public String toString () {
		return order;
	}
	
	public static SortOrder getSortOrder (String order) {
		
		SortOrder [] sortOrders = SortOrder.values();
		
		for (SortOrder sortOrder : sortOrders) {
			if (sortOrder.order.equals(order)) {
				return sortOrder;
			}
		}
		return null;
	}
	
}


