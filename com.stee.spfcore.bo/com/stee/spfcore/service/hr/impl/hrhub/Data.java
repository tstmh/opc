package com.stee.spfcore.service.hr.impl.hrhub;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;

import com.stee.spfcore.service.hr.impl.ErrorConstants;
import com.stee.spfcore.service.hr.impl.input.DataException;
import com.stee.spfcore.service.hr.impl.input.IData;
import com.stee.spfcore.service.hr.impl.input.IRecord;
import com.stee.spfcore.service.hr.impl.util.CodeMappingUtil;

class Data implements IData {

	private static final String TOTAL_RECORD_COUNT = "TotalRecordCount";
	private static final String PROCESS_ID = "ProcessID";
	private static final String PERSON_DETAILS = "PersonDetails";
	private static final String HEADER = "Header";
	
	private String processId = null;
	private int recordCount = 0;
	private List<IRecord> recordList;
	
	public Data (Document doc, CodeMappingUtil codeMappingUtil) throws DataException {
		
		Element rootElement = doc.getRootElement();
		Element headerElement = rootElement.getChild(HEADER);
		if (headerElement != null) {
			processHeader(headerElement);
		}
		else {
			throw new DataException(ErrorConstants.INVALID_XML);
		}
		
		recordList = new ArrayList<>();
		
		List<Element> detailsElements = rootElement.getChildren(PERSON_DETAILS);
		for (Element detailElement : detailsElements) {
			recordList.add(new Record (detailElement, codeMappingUtil));
		}
	}
	
	private void processHeader (Element headerElement) throws DataException {
		Element pIdElement = headerElement.getChild(PROCESS_ID);
		if (pIdElement != null) {
			processId = pIdElement.getTextTrim();
		}
		else {
			throw new DataException(ErrorConstants.PROCESS_ID_NOT_DEFINE);
		}
		
		Element countElement = headerElement.getChild(TOTAL_RECORD_COUNT);
		if (countElement != null) {
			try {
				recordCount = Integer.parseInt(countElement.getTextTrim());
			} 
			catch (NumberFormatException e) {
				throw new DataException(ErrorConstants.INVALID_RECORD_COUNT);
			}
		}
		else {
			throw new DataException(ErrorConstants.RECORD_COUNT_NOT_DEFINE);
		}
	}
	
	@Override
	public String getProcessId () {
		return processId;
	}

	@Override
	public int getRecordCount() {
		return recordCount;
	}

	@Override
	public List<IRecord> getRecordList() {
		return recordList;
	}
	
	
}
