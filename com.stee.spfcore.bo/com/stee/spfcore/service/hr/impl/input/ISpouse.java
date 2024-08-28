package com.stee.spfcore.service.hr.impl.input;

import java.text.ParseException;
import java.util.Date;

public interface ISpouse {

	public Date getDateOfMarriage () throws ParseException;
	
	public String getName ();
	
	public String getId ();
	
	public CodeInfo getIdType ();
	
	public String getCertificateNumber ();
}
