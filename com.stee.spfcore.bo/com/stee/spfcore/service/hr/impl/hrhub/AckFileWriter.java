package com.stee.spfcore.service.hr.impl.hrhub;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.stee.spfcore.service.hr.impl.IAckFileWriter;
import com.stee.spfcore.service.hr.impl.RecordError;
import com.stee.spfcore.service.hr.impl.input.IData;

class AckFileWriter implements IAckFileWriter {

	private static final String ERROR_DESC = "ErrorDesc";
	private static final String NRIC = "NRIC";
	private static final String ERROR_DETAILS = "ErrorDetails";
	private static final String HEADER_ERROR_DESC = "HeaderErrorDesc";
	private static final String TOTAL_ERROR_COUNT = "TotalErrorCount";
	private static final String PROCESS_ID = "ProcessID";
	private static final String HEADER = "Header";
	private static final String PERSONNEL_INFO = "PersonnelInfo";
	private static final String RECORD_COUNT_DOES_NOT_TALLY = "Record count does not tally";

	private static final List<RecordError> EMPTY_LIST = new ArrayList<>();
	
	private File outputFile;
	private Logger logger;
	
	
	public AckFileWriter (File outputFile, Logger logger) {
		this.outputFile = outputFile;
		this.logger = logger;
	}
	
	@Override
	public File recordCountNotTally (IData data) {
		return generateOutput (data.getProcessId(), RECORD_COUNT_DOES_NOT_TALLY, EMPTY_LIST);
	}
	
	@Override
	public File processStatus(IData data, List<RecordError> errors) {
		return generateOutput (data.getProcessId(), null, errors);
	}
	
	
	private File generateOutput (String processId, String errorDesc, List<RecordError> errors) {
		
		// Root element
	    Element rootElement = new Element (PERSONNEL_INFO);
	    Document doc = new Document (rootElement);	
		
	    //Header 
		Element headerElement = new Element(HEADER);
		rootElement.addContent(headerElement);
		
		//ProcessID
		Element processIdElement = new Element(PROCESS_ID);
		processIdElement.setText(processId);
		headerElement.addContent(processIdElement);
		
		//TotalErrorCount
		Element totalCountElement = new Element(TOTAL_ERROR_COUNT);
		totalCountElement.setText(String.valueOf(errors.size()));
		headerElement.addContent(totalCountElement);
		
		//HeaderErrorDesc
		Element errorDescElement = new Element(HEADER_ERROR_DESC);
		
		if (errorDesc != null) {
			errorDescElement.setText(errorDesc);
		}
		headerElement.addContent(errorDescElement);
		
		// Error Details
		for (RecordError error : errors) {
			Element detailsElement = new Element (ERROR_DETAILS);
			
			//NRIC
			Element nricElement = new Element(NRIC);
			nricElement.setText(error.getId());
			detailsElement.addContent(nricElement);
			
			//ErrorDesc
			Element descElement = new Element (ERROR_DESC);
			descElement.setText(error.getDesc());
			detailsElement.addContent(descElement);
			
			rootElement.addContent(detailsElement);
		}
		
		XMLOutputter xmlOutput = new XMLOutputter();
		
		xmlOutput.setFormat(Format.getPrettyFormat());
    
		FileWriter writer = null;
	    try {
	    	writer = new FileWriter(outputFile);
	    	xmlOutput.output(doc, writer);
		} 
	    catch (IOException e) {
	    	logger.log(Level.SEVERE, "Fail to generate acknowledgement XML file.");
	    	return null;
		}
	    finally{
	    	try {
	    		if(writer != null)
	    			writer.close();
			} catch (IOException e) {
				logger.log(Level.INFO, "Fail to close the FileWriter", e);
			}
	    }
    
		return outputFile;
	}
	
}
