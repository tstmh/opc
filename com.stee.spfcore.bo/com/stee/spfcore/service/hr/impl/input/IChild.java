package com.stee.spfcore.service.hr.impl.input;

import java.text.ParseException;
import java.util.Date;

public interface IChild {

	public String getName();
	
	public Date getDateOfBirth () throws ParseException;
	
	public String getId ();
	
	public CodeInfo getIdType ();
}
