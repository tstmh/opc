package com.stee.spfcore.model.accounting;

import java.util.HashMap;

public class ReturnCode {
	
	private HashMap<String, String> returnCodeHashMap = new HashMap<>();
	
	public ReturnCode() {
		returnCodeHashMap.put("1010","Invalid Receiving Account Number");
		returnCodeHashMap.put("1041","DDA has been terminated");
		returnCodeHashMap.put("1042","Invalid Originating Account Number");
		returnCodeHashMap.put("1051","Refer to receiving party");
		returnCodeHashMap.put("1100","Invalid receiving BIC code");
		returnCodeHashMap.put("1160","Receiving account closed");
		returnCodeHashMap.put("1161","Refer to receiving party");
		returnCodeHashMap.put("1169","Refer to receiving party");
		returnCodeHashMap.put("1170","Refer to receiving party");
		returnCodeHashMap.put("1172","Refer to receiving party");
		returnCodeHashMap.put("1202","Refer to receiving party");
		returnCodeHashMap.put("1207","Amount exceeded limit");
		returnCodeHashMap.put("1208","Refer to receiving party");
		returnCodeHashMap.put("1209","Refer to receiving party");
		returnCodeHashMap.put("1219","Cancelled by receiving party");
		returnCodeHashMap.put("1237","DDA expired");
		returnCodeHashMap.put("1243","No such DDA");
		returnCodeHashMap.put("1252","Duplicate DDA");
		returnCodeHashMap.put("1261","Refer to receiving party");
		returnCodeHashMap.put("1262","Invalid BIC");
		returnCodeHashMap.put("1267","Refer to receiving party");
		returnCodeHashMap.put("OTHERS","Please contact bank for assistance");
		
		//PayNow Return Codes - 3 digits with 1 trailing space
		returnCodeHashMap.put("601", "Please contact bank for assistance");
		returnCodeHashMap.put("602", "Please contact bank for assistance");
		returnCodeHashMap.put("650", "Please contact bank for assistance");
		returnCodeHashMap.put("801", "Payee is not registered for this service");
		returnCodeHashMap.put("802", "Please contact bank for assistance");
		returnCodeHashMap.put("809", "Payee is not registered for this service");
		returnCodeHashMap.put("999", "Please contact bank for assistance");
	}
	
	public String getReturnCode(String code){
		return returnCodeHashMap.get(code);
	}
}
