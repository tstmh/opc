package com.stee.spfcore.vo.accounting;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

public class PayAdviceUOB implements IPayAdvice {

	public PayAdviceUOBFCH fileControlHeader;
	public PayAdviceUOBBH batchHeader;
	public List<PayAdviceUOBBDcheque> detailRecord;
	public PayAdviceUOBBT batchTrailer;
	
	private static final String NL = System.getProperty("line.separator");
	
	public static final Logger log = Logger.getLogger( PayAdviceUOB.class.getName() );
	
	public PayAdviceUOB() 
	{
		fileControlHeader = new PayAdviceUOBFCH();
		batchHeader = new PayAdviceUOBBH();
		detailRecord = new ArrayList<>();
		batchTrailer = new PayAdviceUOBBT();
	}
	@Override
	public void processRecord()
	{
		// DO NOTHING
	}
	@Override
	public boolean validatePayAdvice()
	{
		// validate data before processing
		if(fileControlHeader.validateData() && batchHeader.validateData() && batchTrailer.validateData())
		{
			boolean isValid = true;
			for(int i = 0; i < detailRecord.size(); i++)
			{
				if(!detailRecord.get(i).validateData())
					isValid = false;
			}
			if(!isValid)
			{
				log.severe("Data validation fail, aborting output");
				return false;
			}
		} else {
			
			return false;
		}
		return true;
	}
	
	/**
	 * Return data content in formated output
	 * @return String
	 */
	@Override
	public String getFormatedData()
	{
		if(!validatePayAdvice())
			log.severe("Data validation fail, aborting output");
		
		StringBuilder output = new StringBuilder();
		output.append(fileControlHeader.print()).append(NL);
		output.append(batchHeader.print()).append(NL);
		for(int i = 0; i < detailRecord.size(); i++)
		{
			output.append(detailRecord.get(i).print()).append(NL);
		}
		output.append(batchTrailer.print());
		
		return output.toString();
	}
	
	@Override
	public String toString()
	{
		String output = StringUtils.center("  UOB Payment Advice  ", 100, "#") + NL+NL;
		output += fileControlHeader;
		output += batchHeader;
		PayAdviceUOBBDcheque d = new PayAdviceUOBBDcheque();
		output += d;
		output += batchTrailer+NL;
		output += String.format("%100s", "").replace(" ","#") + NL;
		return output;
	}
	
	public static void main(String[] args)
	{
		// For TESTING 
		
		PayAdviceUOB uob = new PayAdviceUOB();
		if ( log.isLoggable( Level.INFO ) ) {
			log.info(String.valueOf(uob));
		}
		
		/*************************   Example   **************************************/
		uob.fileControlHeader.recordType.setData(9);
		uob.fileControlHeader.creationTime.setData("010011");
		uob.fileControlHeader.fileName.setData("123");
		uob.fileControlHeader.creationDate.setData("01102018");
		
		PayAdviceUOBBDcheque t = new PayAdviceUOBBDcheque();
		t.recordType.setData(2);
		t.payType.setData("CHQ");
		t.payAmt.setData(100);
		uob.detailRecord.add(t);
		/****************************************************************************/
		
		// Ignore mandatory? Yes
		uob.fileControlHeader.setIgnoreMandatory(true);
		uob.batchHeader.setIgnoreMandatory(true);
		uob.batchTrailer.setIgnoreMandatory(true);
		for(PayAdviceUOBBDcheque c : uob.detailRecord)
		{
			c.setIgnoreMandatory(true);
		}

		log.info(uob.getFormatedData());
	}
}
