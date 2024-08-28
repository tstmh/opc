package com.stee.spfcore.service.hr.impl.nspam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jdom2.Element;

import com.stee.spfcore.service.hr.impl.input.CodeInfo;
import com.stee.spfcore.service.hr.impl.input.ISpouse;

public class Spouse implements ISpouse {

	private static final String MARRIAGE_CERTIFICATE = "MARRIAGE_CERTIFICATE";
	private static final String SPOUSE_ID = "SPOUSE_ID";
	private static final String SPOUSE_NAME = "SPOUSE_NAME";
	private static final String MARRIAGE_DATE = "MARRIAGE_DATE";
	
	private SimpleDateFormat dateFormat;
	
	private String marriageDate;
	private String name;
	private String id;
	private String certificateNumber;
	
	public Spouse (Element spouseElement) {
		
		dateFormat = new SimpleDateFormat ("ddMMyyyy");
		dateFormat.setLenient(false);
		
		marriageDate = spouseElement.getChildTextTrim(MARRIAGE_DATE);
		name = spouseElement.getChildTextTrim(SPOUSE_NAME);
		id = spouseElement.getChildTextTrim(SPOUSE_ID);
		certificateNumber = spouseElement.getChildTextTrim(MARRIAGE_CERTIFICATE);
	}
	
	@Override
	public Date getDateOfMarriage() throws ParseException {
		
		if (marriageDate != null && !marriageDate.isEmpty()) {
			return parseDateString (marriageDate);
		}
		
		return null;
	}

	@Override
	public String getName() {
		return name;
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

	@Override
	public String getCertificateNumber () {
		return certificateNumber;
	}
}
