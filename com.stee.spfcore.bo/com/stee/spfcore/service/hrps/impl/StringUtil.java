package com.stee.spfcore.service.hrps.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {

	private StringUtil(){}
	public static String convertAmountToString (Double amount) {
		return ((String.valueOf((amount*100))).split("\\."))[0];
	}
	
	public static String getHeaderDateTimeString(Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		return simpleDateFormat.format(date);
	}
	
	public static String getDetailDateString(Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		return simpleDateFormat.format(date);
	}
	
	public static String getFileDateTimeString(Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyMMdd.HHmmss.SSS");
		return simpleDateFormat.format(date);
	}
	
	public static String rightPadSpaces (String data, int totalLength) {
		data = (data == null) ? "" : data;
		
		if (data.length() >= totalLength) {
			return data.substring(0, totalLength);
		}
		return StringUtils.rightPad(data, totalLength, " ");
	}
	
	public static String leftPadZeros(String data, int totalLength) {
		data = (data == null) ? "" : data;
		
		if (data.length() >= totalLength) {
			return data.substring(0, totalLength);
		}
		return StringUtils.leftPad(data, totalLength, "0");
	}
	
	public static String convertAmountToSignedString(Double amount, int totalLength) {
		StringBuilder str = new StringBuilder();
		
		if (amount > 0) {
			str.append(" ");
			str.append(leftPadZeros(convertAmountToString(amount),totalLength-1));
		} else {
			amount = amount * -1; //Convert to positive amount
			str.append("-");
			str.append(leftPadZeros(convertAmountToString(amount),totalLength-1));
		}
		return str.toString();
	}
	
	public static int getLastDayOfMonth (Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		return cal.getActualMaximum(Calendar.DATE);
	}
	
	public static String getBenefitsTypeFromId (String id) {
		String benefitType;
		if (id == null) {
			benefitType = "";
		} else {
			benefitType = (id.length() < 2) ? id : id.substring(0, 2);
		}
		return benefitType;
	}
	
	public static Date convertPayrollMonthToDate (String payrollMonth) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		return simpleDateFormat.parse(payrollMonth);
	}
	
	
}
