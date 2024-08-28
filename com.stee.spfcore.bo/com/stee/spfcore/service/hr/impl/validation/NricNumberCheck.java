package com.stee.spfcore.service.hr.impl.validation;

public class NricNumberCheck {
	private NricNumberCheck(){}
    private static final int [] MULTIPLES = { 2, 7, 6, 5, 4, 3, 2 };
	private static final String [] ST_LAST_CHAR = {"J","Z","I","H","G","F","E","D","C","B","A"};
	private static final String [] FG_LAST_CHAR = {"X","W","U","T","R","Q","P","N","M","L","K"};
	private static final String [] M_LAST_CHAR = {"K","L","J","N","P","Q","R","T","U","W","X"};
	
    public static boolean isValid (String nric) {
	
    	int total = 0;
    	
    	for (int i = 0; i < MULTIPLES.length; i++) {
    		int num;
    		try {
    			num = Integer.parseInt(nric.substring(i + 1, i + 2));
    		} 
    		catch (NumberFormatException e) {
    			return false;
    		}
    		total = total + (num * MULTIPLES [i]);
    	}
    	
    	if (nric.startsWith("T") || nric.startsWith("G")) {
    		total += 4;
    	}
    	
    	if (nric.startsWith("M")) {
    		total += 3;
    	}
    	
    	int index = total % 11;
    	
    	if (nric.startsWith("S") || nric.startsWith("T")) {
    		return nric.endsWith(ST_LAST_CHAR [index]);
    	}
    	else if (nric.startsWith("F") || nric.startsWith("G")){
    		return nric.endsWith(FG_LAST_CHAR [index]);
    	}
    	else{
    	    int m_index = 10;
			m_index -= index;
    	    return nric.endsWith(M_LAST_CHAR [m_index]);
    	}
    	
    }
}
