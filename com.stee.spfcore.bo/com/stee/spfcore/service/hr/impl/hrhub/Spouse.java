package com.stee.spfcore.service.hr.impl.hrhub;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jdom2.Element;

import com.stee.spfcore.service.hr.impl.input.CodeInfo;
import com.stee.spfcore.service.hr.impl.input.ISpouse;


public class Spouse implements ISpouse {
	
	private static final String MARRIAGE_CERT_NO = "MarriageCertNo";
	private static final String ID_TYPE_OF_SPOUSE_DESC = "IDTypeOfSpouseDesc";
	private static final String ID_TYPE_OF_SPOUSE = "IDTypeOfSpouse";
	private static final String DATE_OF_MARRIAGE = "DateOfMarriage";
	private static final String NAME_OF_SPOUSE = "NameOfSpouse";
	private static final String ID_OF_SPOUSE = "IDOfSpouse";

	private String marriageDate;
	private String name;
	private String id;
	private CodeInfo idType;
	private String certificateNumber;
	
	
	private SimpleDateFormat dateFormat;
	
	public Spouse (Element spouseElement) {
		super();
		
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);
		
		this.marriageDate = spouseElement.getChildTextTrim(DATE_OF_MARRIAGE);
		this.name = spouseElement.getChildTextTrim(NAME_OF_SPOUSE);
		this.id = spouseElement.getChildTextTrim(ID_OF_SPOUSE);
		
		this.idType = new CodeInfo (ID_TYPE_OF_SPOUSE, true, spouseElement.getChildTextTrim(ID_TYPE_OF_SPOUSE), 
																spouseElement.getChildTextTrim(ID_TYPE_OF_SPOUSE_DESC));
		
		this.certificateNumber = spouseElement.getChildTextTrim(MARRIAGE_CERT_NO);
	}

	@Override
	public Date getDateOfMarriage () throws ParseException {
		if (marriageDate != null && !marriageDate.isEmpty()) {
			return dateFormat.parse(marriageDate);
		}
		
		return null;
	}

	@Override
	public String getName () {
		return name;
	}

	@Override
	public String getId () {
		return id;
	}

	@Override
	public CodeInfo getIdType () {
		return idType;
	}

	@Override
	public String getCertificateNumber () {
		return certificateNumber;
	}
	
	
}
