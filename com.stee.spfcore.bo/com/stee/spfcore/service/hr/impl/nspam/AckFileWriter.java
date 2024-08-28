package com.stee.spfcore.service.hr.impl.nspam;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import static com.ibm.java.diagnostics.utils.Context.logger;

class AckFileWriter implements IAckFileWriter {

	
	private File outputFile;
	private Logger logger;
	
	public AckFileWriter (File outputFile, Logger logger) {
		this.outputFile = outputFile;
		this.logger = logger;
	}
	
	
	@Override
	public File recordCountNotTally(IData data) {
		
		// Don't need to generate acknowledgement. 
		return null;
	}

	@Override
	public File processStatus(IData data, List<RecordError> errors) {
		
		// Acknowledgement file is different depending on whether there is error
		if (errors.size() == 0) {
			return  generateSuccess (data);
		}
		else {
			return generateFailed (data, errors);
		}
		
	}
	
	
	public File generateSuccess (IData data) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		
		Date date = new Date ();
		
		// Root element
	    Element rootElement = new Element ("FILETRANSFERHEADER");
	    Document doc = new Document (rootElement);	
	    
	    Element recordCountElement = new Element("RECORDCOUNT");
	    rootElement.addContent(recordCountElement);
	    recordCountElement.setText(String.valueOf(data.getRecordCount()));
	    
	    Element processIdElement = new Element("PROCESSID");
	    rootElement.addContent(processIdElement);
	    processIdElement.setText(data.getProcessId());
		
	    Element processingDateElement = new Element ("PROCESSINGDATE");
	    rootElement.addContent(processingDateElement);
	    processingDateElement.setText (dateFormat.format(date));
	    
	    Element processingTimeElement = new Element ("PROCESSINGTIME");
	    rootElement.addContent(processingTimeElement);
	    processingTimeElement.setText(timeFormat.format(date));
	    
	    Element processingStatusElement = new Element ("PROCESSINGSTATUS");
	    rootElement.addContent(processingStatusElement);
	    processingStatusElement.setText("SUCCESS");
	    
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
	
	
	private File generateFailed (IData data, List<RecordError> errors) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		
		Date date = new Date ();
		
		// Root element
	    Element rootElement = new Element ("CORE2NSPAM_SPF_NSF_INFO");
	    Document doc = new Document (rootElement);	
	    
	    Element headerElement = new Element ("FILETRANSFERHEADER");
	    rootElement.addContent(headerElement);
	    
	    Element processingDateElement = new Element ("PROCESSINGDATE");
	    headerElement.addContent(processingDateElement);
	    processingDateElement.setText (dateFormat.format(date));
	    
	    Element processingTimeElement = new Element ("PROCESSINGTIME");
	    headerElement.addContent(processingTimeElement);
	    processingTimeElement.setText(timeFormat.format(date));
	    
	    Element processingStatusElement = new Element ("PROCESSINGSTATUS");
	    headerElement.addContent(processingStatusElement);
	    processingStatusElement.setText("FAILED");
	    
	    Element recordCountElement = new Element("RECORDCOUNT");
	    headerElement.addContent(recordCountElement);
	    recordCountElement.setText(String.valueOf(errors.size()));
	    
	    Element processIdElement = new Element("PROCESSID");
	    headerElement.addContent(processIdElement);
	    processIdElement.setText(data.getProcessId());
	    
	    if (!errors.isEmpty()) {
	    	Element recordsElement = new Element ("RECORDS");
	    	rootElement.addContent(recordsElement);
	    	
	    	for (int i = 0; i < errors.size(); i++) {
	    		RecordError error = errors.get(i);
	    		
	    		Element recordElement = new Element ("RECORD");
	    		recordsElement.addContent(recordElement);
	    		
	    		Element recordNumElement = new Element("RECORD_NUMBER");
	    		recordNumElement.setText(String.valueOf(i + 1));
	    		recordElement.addContent(recordNumElement);
	    		
	    		Element uidElement = new Element("UID");
	    		uidElement.setText(error.getId());
	    		recordElement.addContent(uidElement);
	    		
	    		Element codeElement = new Element("ERROR_CODE");
	    		codeElement.setText(error.getCode());
	    		recordElement.addContent(codeElement);
	    		
	    		Element msgElement = new Element("ERROR_MESSAGE");
	    		msgElement.setText(error.getDesc());
	    		recordElement.addContent(msgElement);
	    	}
	    }
	    
	    XMLOutputter xmlOutput = new XMLOutputter();
			
		xmlOutput.setFormat(Format.getPrettyFormat());
		FileWriter writer = null;
		boolean isValid = true;
	    try {
	    	writer = new FileWriter(outputFile);
			xmlOutput.output(doc, writer);
		} catch (IOException e) {
			isValid = false;
	    	logger.log(Level.SEVERE, "Fail to generate acknowledgement XML file.");
		} finally{
			try {
				if(writer != null)
					writer.close();
			} catch (IOException e) {
				logger.severe(String.valueOf(e));
			}
		}
	    if(!isValid)
	    	return null;
	    
		return outputFile;
	}
	
}
