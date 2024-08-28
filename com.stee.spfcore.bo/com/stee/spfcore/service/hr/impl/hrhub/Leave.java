package com.stee.spfcore.service.hr.impl.hrhub;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jdom2.Element;

import com.stee.spfcore.service.hr.impl.input.CodeInfo;
import com.stee.spfcore.service.hr.impl.input.ILeave;

class Leave implements ILeave {

	private static final String TYPE_OF_LEAVE_DESCRIPTION = "TypeOfLeaveDescription";
	private static final String DELETE_INDICATOR = "DeleteIndicator";
	private static final String TYPE_OF_LEAVE_CODE = "TypeOfLeaveCode";
	private static final String END_DATE_OF_LEAVE = "EndDateOfLeave";
	private static final String START_DATE_OF_LEAVE = "StartDateOfLeave";
	
	private SimpleDateFormat dateFormat;
	
	private String startDate;
	private String endDate;
	private CodeInfo leaveType;
	private String indicator;
	
	public Leave (Element leaveElement) {
		
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);
		
		startDate = leaveElement.getChildTextTrim(START_DATE_OF_LEAVE);
		endDate = leaveElement.getChildTextTrim(END_DATE_OF_LEAVE);
		leaveType = new CodeInfo(TYPE_OF_LEAVE_CODE, true, leaveElement.getChildTextTrim(TYPE_OF_LEAVE_CODE), 
				leaveElement.getChildTextTrim(TYPE_OF_LEAVE_DESCRIPTION));
		indicator = leaveElement.getChildTextTrim(DELETE_INDICATOR);
	}
	
	@Override
	public Date getStartDate() throws ParseException {
		return parseDateString (startDate);
	}

	@Override
	public Date getEndDate() throws ParseException {
		return parseDateString (endDate);
	}

	@Override
	public CodeInfo getLeaveType() {
		return leaveType;
	}
	
	@Override
	public String getDeleteIndicator() {
		return indicator;
	}

	private synchronized Date parseDateString (String dateString) throws ParseException {
		return dateFormat.parse(dateString);
	}
}
