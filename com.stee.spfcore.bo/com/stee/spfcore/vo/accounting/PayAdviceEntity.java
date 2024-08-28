package com.stee.spfcore.vo.accounting;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import com.stee.spfcore.vo.accounting.PayAdviceUtil.entityFormat;

/**
 * Generic entity for Payment Advice record
 * @author Sebastian
 *
 */
public class PayAdviceEntity {

	public static final Logger log = Logger.getLogger( PayAdviceEntity.class.getName() );
		
	public String description;
	public String remarks;
	public String filler;
	public int position;
	public int length;
	/*
	 * Order must always start with 1 for the first item
	 */
	public int order;
	public entityFormat picture;
	public boolean isMandatory;
	
	/**
	 * Generic data container. All type will be converted to String
	 */
	private String data;
	
	public PayAdviceEntity()
	{
		this("",PayAdviceUtil.entityFormat.NUM_RIGHT_ALIGN_FILLER,"0",0,0,"",0,false);
	}
	public PayAdviceEntity(String description, entityFormat picture, String filler, int position, int length, String remarks, int order, boolean isMandatory )
	{
		this.description = description;
		this.picture = picture;
		this.filler = filler;	// Need to have value if picture contains "FILLER"
		this.position = position;
		this.length = length;
		this.remarks = remarks;
		this.order = order;
		this.isMandatory = isMandatory;
		
		if(!PayAdviceUtil.checkValid(validate()))
		{
			log.warning("[PayAdviceUOBFCH] UOB Pay advice is invalid, initiating to default");
			
			// Default is a non-mandatory right align number entity of length 1 with 0 as filler
			 
			this.description = "";
			this.picture = entityFormat.NUM_RIGHT_ALIGN_FILLER;
			this.position = 0;
			this.filler = "0";
			this.length = 1;
			this.remarks = "DEFAULT";
			this.order = 0;
			this.isMandatory = false;
		}
	}
	public String validate()
	{
		if(description == null)
			return PayAdviceUtil.ERROR_ID + "description is null";
		else if(picture == null)
			return PayAdviceUtil.ERROR_ID + "picture is null";
		else if(position < 0)
			return PayAdviceUtil.ERROR_ID + "position is less than 0";
		else if(length < 0)
			return PayAdviceUtil.ERROR_ID + "length is less than 0";
		else if(remarks == null)
			return PayAdviceUtil.ERROR_ID + "remarks is null";
		else if(order < 0)
			return PayAdviceUtil.ERROR_ID + "order is less than 0";
		
		if(description.length() == 0)
			return PayAdviceUtil.ERROR_ID + "description length is 0";
		if(picture == entityFormat.CHAR_LEFT_ALIGN_FILLER 
				|| picture == entityFormat.CHAR_RIGHT_ALIGN_FILLER
				|| picture == entityFormat.NUM_LEFT_ALIGN_FILLER
				|| picture == entityFormat.NUM_RIGHT_ALIGN_FILLER)
		{
			if(filler == null)
				return PayAdviceUtil.ERROR_ID + "required filler is null";
			else if(filler.length() == 0)
				return PayAdviceUtil.ERROR_ID + "required filler is empty";
		}
		
		return PayAdviceUtil.VALID_ID;
	}
	
	public boolean setData(Object data)
	{
		try{
			if(isNumeric())
			{
				if(data instanceof String)
				{
					this.data = (String) data;
				} 
				else if(data instanceof Integer)
				{
					try
					{
						this.data = String.valueOf((int) data);
					}
					catch(Exception e)
					{
						log.warning("[PayAdviceEntity] Error converting Integer ("+data+") for <"+this.description+"> to string data... using default '0'");
						this.data = "0";
					}
				}
				else
				{
					if ( log.isLoggable( Level.INFO ) ) {
						log.warning(String.format("[PayAdviceEntity] data for < %s > is invalid type... using default 0", this.description));
					}
					this.data = "0";
				}
			}
			else
			{
				if(data instanceof Integer)
				{
					try
					{
						this.data = String.valueOf((int)data);
					}
					catch(Exception e)
					{
						log.warning("[PayAdviceEntity] Error converting int ("+data+") for <"+this.description+"> to string data... using default ''");
						this.data = "";
					}
				}
				else if(data instanceof String)
				{
					this.data = (String) data;
				}
				else
				{
					if ( log.isLoggable( Level.INFO ) ) {
						log.warning(String.format("[PayAdviceEntity] data for < %s > is invalid type... using default ''", this.description));
					}
					this.data = "";
				}
			}
		}
		catch(Exception e)
		{
			return false;
		}
		validateData();
		return true;
	}
	public String getData()
	{
		return toString();
	}
	public Object getDataRaw()
	{
		return data;
	}
	@Override
	public String toString()
	{
		/* Stringify data for processing
		 * 
		 * Note: Default filler for char is ' ' and default filler for number is 0
		 */
		String toStringData = this.data;
		if(toStringData == null)
		{
			toStringData = "";
		}
		
		String output = "";
		if(picture == entityFormat.CHAR_LEFT_ALIGN || picture == entityFormat.CHAR_LEFT_ALIGN_FILLER
				|| picture == entityFormat.NUM_LEFT_ALIGN || picture == entityFormat.NUM_LEFT_ALIGN_FILLER)
		{
			output += toStringData;
			if(output.length() != length)
			{
				// need to fill up
				int fillCount = length - output.length();
				if(picture == entityFormat.CHAR_LEFT_ALIGN)
					output += StringUtils.repeat(" ", fillCount);
				else if(picture == entityFormat.NUM_LEFT_ALIGN)
					output += StringUtils.repeat("0", fillCount);
				else
					output += StringUtils.repeat(filler, fillCount);
			}
		} 
		else if(picture == entityFormat.CHAR_RIGHT_ALIGN || picture == entityFormat.CHAR_RIGHT_ALIGN_FILLER
					|| picture == entityFormat.NUM_RIGHT_ALIGN || picture == entityFormat.NUM_RIGHT_ALIGN_FILLER)
		{
			int fillCount = length - toStringData.length();
			if(picture == entityFormat.CHAR_RIGHT_ALIGN)
				output += StringUtils.repeat(" ", fillCount);
			else if(picture == entityFormat.NUM_RIGHT_ALIGN)
				output += StringUtils.repeat("0", fillCount);
			else
				output += StringUtils.repeat(filler, fillCount);
			
			output += toStringData;
		}
		else
		{
			log.warning("Format mismatch, cannot convert data... return empty string");
		}
		
		return output;
	}
	/**
	 * Tells 
	 * @return true if numeric
	 */
	private boolean isNumeric()
	{
		return picture != entityFormat.CHAR_LEFT_ALIGN && picture != entityFormat.CHAR_LEFT_ALIGN_FILLER
				&& picture != entityFormat.CHAR_RIGHT_ALIGN && picture != entityFormat.CHAR_RIGHT_ALIGN_FILLER;
	}
	/**
	 * Overloaded PayAdviceEntity '==' comparator
	 */

	@Override
	public boolean equals(Object o) {
		if (this == o) return true; // Check if the same object
		if (o == null || getClass() != o.getClass()) return false; // Check for null and class type

		PayAdviceEntity temp = (PayAdviceEntity) o; // Safe to cast now
		if (this.data != null ? !this.data.equals(temp.getData()) : temp.getData() != null) return false; // Compare data
		if (!Objects.equals(this.description, temp.description)) return false; // Compare description
		return this.order == temp.order; // Compare order
	}

	@Override
	public int hashCode() {
		int result = (data != null ? data.hashCode() : 0);
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + order;
		return result;
	}

	/**
	 * To validate Data, making sure data conform to length set
	 * @return true if valid
	 */
	private boolean validateData()
	{
		if((data != null) && (data.length() > length))
		{
			if ( log.isLoggable( Level.INFO ) ) {
				log.warning(String.format("[ %s ] data (%s) length (%s) is greater than allowed length (%s).. truncating...", description, data, data.length(), length));
			}
			data = data.substring(0, length);
			return false;
		}
		return true;
	}
}
