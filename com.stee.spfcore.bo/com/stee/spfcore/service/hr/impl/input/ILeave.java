package com.stee.spfcore.service.hr.impl.input;

import java.text.ParseException;
import java.util.Date;


public interface ILeave {

	public CodeInfo getLeaveType ();
	
	public Date getStartDate () throws ParseException;
	
	public Date getEndDate () throws ParseException;
	
	public String getDeleteIndicator ();
	
}
