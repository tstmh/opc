package com.stee.spfcore.service.hr.impl.nspam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jdom2.Element;

import com.stee.spfcore.service.hr.impl.input.CodeInfo;
import com.stee.spfcore.service.hr.impl.input.ILeave;

class Leave implements ILeave {

	private static final String TYPE_OF_LEAVE_DESC = "TYPE_OF_LEAVE_DESC";
	private static final String TYPE_OF_LEAVE_CODE = "TYPE_OF_LEAVE_CODE";
	private static final String LEAVE_END_DATE = "LEAVE_END_DATE";
	private static final String LEAVE_START_DATE = "LEAVE_START_DATE";
	
	private SimpleDateFormat dateFormat;
	
	private String startDate;
	private String endDate;
	private CodeInfo leaveType;
	
	public Leave (Element leaveElement) {
		
		dateFormat = new SimpleDateFormat ("ddMMyyyy");
		dateFormat.setLenient(false);
		
		startDate = leaveElement.getChildTextTrim(LEAVE_START_DATE);
		endDate = leaveElement.getChildTextTrim(LEAVE_END_DATE);
		leaveType = new CodeInfo (TYPE_OF_LEAVE_CODE, true, leaveElement.getChildTextTrim(TYPE_OF_LEAVE_CODE),
				leaveElement.getChildTextTrim(TYPE_OF_LEAVE_DESC));
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
