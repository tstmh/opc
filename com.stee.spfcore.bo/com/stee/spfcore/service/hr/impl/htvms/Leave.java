package com.stee.spfcore.service.hr.impl.htvms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jdom2.Element;

import com.stee.spfcore.service.hr.impl.input.CodeInfo;
import com.stee.spfcore.service.hr.impl.input.ILeave;

class Leave implements ILeave {

	private static final String LEAVE_TYPE = "LeaveType";
	private static final String LEAVE_END = "LeaveEnd";
	private static final String LEAVE_START = "LeaveStart";
	
	private SimpleDateFormat dateFormat;
	
	
	private String startDate;
	private String endDate;
	private CodeInfo leaveType;
	
	public Leave (Element leaveElement) {
		
		dateFormat = new SimpleDateFormat("ddMMyyyy");
		dateFormat.setLenient(false);
		
		startDate = leaveElement.getChildTextTrim(LEAVE_START);
		endDate = leaveElement.getChildTextTrim(LEAVE_END);
		leaveType = new CodeInfo(LEAVE_TYPE, true, leaveElement.getChildTextTrim(LEAVE_TYPE), "");
		
	}
	
	
	@Override
	public Date getStartDate() throws ParseException {
		return parseDateString(startDate);
	}

	@Override
	public Date getEndDate() throws ParseException {
		return parseDateString(endDate);
	}

	@Override
	public CodeInfo getLeaveType() {
		return leaveType;
	}
	
	@Override
	public String getDeleteIndicator() {
		return null;
	}
	
	private synchronized Date parseDateString (String dateString) throws ParseException {
		return dateFormat.parse(dateString);
	}
}
