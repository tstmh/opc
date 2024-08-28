package com.stee.spfcore.vo.accounting;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PayAdviceUtil {
	
	public static final Logger log = Logger.getLogger( PayAdviceUtil.class.getName() );
	
	/**
	 * Define the type of data and alignment used by the entity.<br><br>
	 * Example belows show a length 7 entity (where _ refers to a space)<br>
	 * Filler means there is a character filling up a space instead of 'space'<br><br>
	 * e.g.
	 * <br>&nbsp&nbsp&nbsp&nbsp0000123 = NUM_RIGHT_ALIGN_FILLER
	 * <br>&nbsp&nbsp&nbsp&nbsp123____ = NUM_LEFT_ALIGN
	 * 
	 * @author Sebastian
	 *
	 */
	public enum entityFormat {
		NUM_LEFT_ALIGN,
		NUM_RIGHT_ALIGN,
		NUM_LEFT_ALIGN_FILLER,
		NUM_RIGHT_ALIGN_FILLER,
		CHAR_LEFT_ALIGN,
		CHAR_RIGHT_ALIGN,
		CHAR_LEFT_ALIGN_FILLER,
		CHAR_RIGHT_ALIGN_FILLER
	}
	
	public enum Bank {
		UOB,
		POSB,
		STANDARD_CHARTED,
		OCBC,
		OTHERS
	}
	
	public static final String ERROR_ID = "[ERROR]";
	public static final String VALID_ID = "[SUCCESS]";
	/**
	 * Character count limit per row
	 *
	public static final int UOB_COLCOUNT = 900;
	*/
	public Map<Bank,Integer> bankRowCount;
	
	public PayAdviceUtil(){
		bankRowCount = new HashMap<>();
		bankRowCount.put(Bank.UOB, 900);
	}
	
	public static int getCharCount(Bank bank)
	{
		PayAdviceUtil util = new PayAdviceUtil();
		Integer rowCount = util.bankRowCount.get(bank);
		if(rowCount == null)
			rowCount = 0;
		return rowCount;
	}
	
	/**
	 * Process log response for error id
	 * @param response
	 * @return
	 */
	public static boolean checkValid(String response)
	{
		boolean isValid = true;
		if(response.indexOf(ERROR_ID) != -1)
		{
			if ( log.isLoggable( Level.WARNING ) ) {
				log.warning("--> " + response.substring(response.indexOf(ERROR_ID)));
			}
			isValid = false;
		}
		return isValid;
	}
}
