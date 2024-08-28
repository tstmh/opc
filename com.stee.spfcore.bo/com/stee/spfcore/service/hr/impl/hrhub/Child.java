package com.stee.spfcore.service.hr.impl.hrhub;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jdom2.Element;

import com.stee.spfcore.service.hr.impl.input.CodeInfo;
import com.stee.spfcore.service.hr.impl.input.IChild;

public class Child implements IChild {

	
	private static final String ID_TYPE_OF_CHILD_DESC = "IDTypeOfChildDesc";
	private static final String ID_TYPE_OF_CHILD = "IDTypeOfChild";
	private static final String ID_OF_CHILD = "IDOfChild";
	private static final String DATE_OF_BIRTH_OF_CHILD = "DateOfBirthOfChild";
	private static final String NAME_OF_CHILD = "NameOfChild";

	private SimpleDateFormat dateFormat; 
	
	private String name;
	private String dateOfBirth;
	private String id;
	private CodeInfo idType;
	
	public Child (Element childElement) {
		
		dateFormat = new SimpleDateFormat ("dd/MM/yyyy");
		dateFormat.setLenient(false);
		
		name = childElement.getChildTextTrim(NAME_OF_CHILD);
		dateOfBirth = childElement.getChildTextTrim(DATE_OF_BIRTH_OF_CHILD);
		id = childElement.getChildTextTrim(ID_OF_CHILD);
		idType = new CodeInfo(ID_TYPE_OF_CHILD, true, childElement.getChildTextTrim(ID_TYPE_OF_CHILD), 
																childElement.getChildTextTrim(ID_TYPE_OF_CHILD_DESC));
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
		return idType;
	}
}
