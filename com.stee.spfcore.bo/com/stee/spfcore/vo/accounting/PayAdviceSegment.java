package com.stee.spfcore.vo.accounting;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.service.hrps.impl.StringUtil;
import org.apache.commons.lang3.StringUtils;

import com.stee.spfcore.vo.accounting.PayAdviceUtil.Bank;

import static com.ibm.java.diagnostics.utils.Context.logger;

public class PayAdviceSegment implements IPayAdviceSegment  {

	public static final Logger log = Logger.getLogger( PayAdviceSegment.class.getName() );
	private String segName;
	private static final String NL = System.getProperty("line.separator");
	/**
	 * Set to TRUE if you do not want mandatory to affect validation.<br>
	 * Default is FALSE;
	 */
	private boolean ignoreMandatory;
	protected Bank bank;
	
	public PayAdviceSegment(){
		segName = this.getClass().getName(); // will be overloaded by child class name
		ignoreMandatory = false;
		bank = Bank.OTHERS;
	}
	
	public void setIgnoreMandatory(boolean value)
	{
		this.ignoreMandatory = value;
	}
	public boolean isIgnoreMandatory() {
		return ignoreMandatory;
	}
	
	@Override
	public String toString()
	{
		String output = StringUtils.center("   "+segName+"   ",100,"*")+NL;
		
		ArrayList<PayAdviceEntity> entities = getEntities();
		int totalLength = 0;
		for(PayAdviceEntity e : entities)
		{
			output += " > [POS] "+String.format("%5d ",e.position)+"  [LENGTH] "+String.format("%5d ", e.length)+"  [DESC] "+e.description+NL;
			totalLength += e.length;
		}
		output += " > Total Length is "+totalLength+NL+NL;
		return output;
	}
	
	/****************   COPY CODE BELOW TO ALL WHO IMPLEMENTS IPayAdviceSegment INTERFACE   *********************/
	// Only Java 8 allow static method... For now, copy these code to all required classes
	
	/**
	 * Classes inheriting this PayAdviceSegment, this 'getEntities()' method will get the fields of that class
	 */
	@Override
	public ArrayList<PayAdviceEntity> getEntities() {
		ArrayList<PayAdviceEntity> temp = new ArrayList<>();
		Field[] fields = this.getClass().getFields();
		for(int i = 0; i < fields.length; i++)
		{
			try {
				temp.add((PayAdviceEntity)fields[i].get(this));
			} catch (Exception e) {
				logger.severe(String.valueOf(e));
			}
		}
		return temp;
	}
	
	@Override
	public String print()
	{
		StringBuilder output = new StringBuilder();
		ArrayList<PayAdviceEntity> entities = getEntities();
		int toBeProcess = entities.size();
		while(toBeProcess > 0)
		{
			for(int i = 0; i < entities.size(); i++)
			{
				if( (i+1) == entities.get(i).order)
				{
					// Entities are processed based on their order.
					// Duplicates are ignored, only first match will be processed
					// Every processed will append to output
					
					String temp = entities.get(i).toString();
					output.append(temp);
					toBeProcess--;
				}
			}
		}
		return output.toString();
	}
	
	/**
	 * Validate if data is null and string length
	 */
	@Override
	public boolean validateData()
	{
		if (logger.isLoggable(Level.INFO)) {
			String message = StringUtils.center(String.format("   [Validate Data] %s   ", segName), 100, '*') + NL;
			logger.info(message);
		}

		boolean isValid = true;
		ArrayList<PayAdviceEntity> entities = getEntities();
		StringBuilder rowStr = new StringBuilder();
		for(PayAdviceEntity e : entities)
		{
			String prefix = "";
			
			// Check mandatory
			if(e.getDataRaw() == null && e.isMandatory)
			{
				if(!ignoreMandatory)
				{
					isValid = false;
					log.info(String.format("> FAIL for [ %s ] Mandatory data is NULL", e.description));
				} else {
					prefix = "*";
				}
				
			}
			
			// check string output
			String s = e.toString();
			if(s.length() != e.length)
			{
				isValid = false;
				log.info(String.format("> FAIL for [ %s ] length %s instead of %s", e.description, s.length(), e.length));
			}
			
			if(isValid)
			{
				rowStr.append(s);
				log.info(String.format("> %s PASS for [ %s ] length %s", prefix, e.description,s.length()));
			}
		}
		// Check row length to match bank's defined word count
		if(rowStr.length() != PayAdviceUtil.getCharCount(bank))
		{
			isValid = false;
			log.info(String.format(">> FAIL for row length %s instead of %s",rowStr.length(),PayAdviceUtil.getCharCount(bank)));
		}
		
		if(isValid)
		{
			log.info(String.format(">> PASS for char count %s",PayAdviceUtil.getCharCount(bank)));
		}

		return isValid;
	}
}
