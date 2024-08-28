package com.stee.spfcore.service.hr.impl.nspam;

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

	private static final String RECORDCOUNT2 = "RECORDCOUNT";
	private static final String PROCESSINGDATE = "PROCESSINGDATE";
	private static final String RECORD = "RECORD";
	private static final String RECORDS = "RECORDS";
	private static final String FILETRANSFERHEADER = "FILETRANSFERHEADER";
	
	private String processId = null;
	private int recordCount = 0;
	private List<IRecord> recordList;
	
	public Data (Document doc, CodeMappingUtil codeMappingUtil) throws DataException {
		
		Element rootElement = doc.getRootElement();
		
		Element headerElement = rootElement.getChild(FILETRANSFERHEADER);
		if (headerElement != null) {
			processHeader(headerElement);
		}
		else {
			throw new DataException(ErrorConstants.INVALID_XML);
		}
		
		Element recordElement = rootElement.getChild(RECORDS);
		if (recordElement == null) {
			throw new DataException(ErrorConstants.INVALID_XML);
		}
		
		recordList = new ArrayList<>();
		
		List<Element> detailsElements = recordElement.getChildren(RECORD);
		for (Element detailElement : detailsElements) {
			recordList.add(new Record(detailElement, codeMappingUtil));
		}
	}
	
	
	private void processHeader (Element headerElement) throws DataException {
		Element pIdElement = headerElement.getChild (PROCESSINGDATE);
		if (pIdElement != null) {
			processId = pIdElement.getTextTrim();
		}
		else {
			throw new DataException(ErrorConstants.PROCESS_ID_NOT_DEFINE);
		}
		
		Element countElement = headerElement.getChild(RECORDCOUNT2);
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
