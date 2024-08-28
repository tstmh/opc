package com.stee.spfcore.service.hr.impl.htvms;

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

	private static final String NUM_OF_REC = "NumOfRec";
	private static final String PROCESS_ID = "ProcessID";
	private static final String CORE = "CORE";
	private static final String CORE_UPDATE_DETAILS = "COREUpdateDetails";
	
	
	private String processId = null;
	private int recordCount = 0;
	private List<IRecord> recordList;
	
	public Data (Document doc, CodeMappingUtil codeMappingUtil) throws DataException {
		
		Element rootElement = doc.getRootElement();
		
		processHeader (rootElement);
		
		Element recordElement = rootElement.getChild(CORE_UPDATE_DETAILS);
		if (recordElement == null) {
			throw new DataException(ErrorConstants.INVALID_XML);
		}
		
		recordList = new ArrayList<>();
		List<Element> detailsElements = recordElement.getChildren(CORE);
		for (Element detailElement : detailsElements) {
			recordList.add(new Record(detailElement, codeMappingUtil));
		}
	}
	
	private void processHeader (Element element) throws DataException {
		
		Element pIdElement = element.getChild(PROCESS_ID);
		if (pIdElement != null) {
			processId = pIdElement.getTextTrim();
		}
		else {
			throw new DataException(ErrorConstants.PROCESS_ID_NOT_DEFINE);
		}
		
		Element countElement = element.getChild(NUM_OF_REC);
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
