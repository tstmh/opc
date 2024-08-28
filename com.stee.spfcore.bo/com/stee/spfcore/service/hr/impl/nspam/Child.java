package com.stee.spfcore.service.hr.impl.nspam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jdom2.Element;

import com.stee.spfcore.service.hr.impl.input.CodeInfo;
import com.stee.spfcore.service.hr.impl.input.IChild;

public class Child implements IChild {

	private static final String CHILD_ID = "CHILD_ID";
	private static final String CHILD_DOB = "CHILD_DOB";
	private static final String CHILD_NAME = "CHILD_NAME";
	
	private SimpleDateFormat dateFormat;
	
	private String name;
	private String dateOfBirth;
	private String id;
	
	
	public Child (Element childElement) {
		
		dateFormat = new SimpleDateFormat ("ddMMyyyy");
		dateFormat.setLenient(false);
		
		name = childElement.getChildTextTrim(CHILD_NAME);
		dateOfBirth = childElement.getChildTextTrim(CHILD_DOB);
		id = childElement.getChildTextTrim(CHILD_ID);
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public Date getDateOfBirth() throws ParseException {
		if (dateOfBirth != null && !dateOfBirth.isEmpty()) {
			return parseDateString(dateOfBirth);
		}
		return null;
	}

	@Override
	public String getId() {
		return id;
	}

	private synchronized Date parseDateString (String dateString) throws ParseException {
		return dateFormat.parse(dateString);
	}

	@Override
	public CodeInfo getIdType () {
		return null;
	}
}
