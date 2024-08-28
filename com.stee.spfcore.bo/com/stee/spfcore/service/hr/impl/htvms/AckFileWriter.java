package com.stee.spfcore.service.hr.impl.htvms;

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
		
		if (errors.isEmpty()) {
			return  generateSuccess (data);
		}
		else {
			return generateFailed (data, errors);
		}
		
	}
	
	
	private File generateFailed (IData data, List<RecordError> errors) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		
		Date date = new Date ();
		
		// Root element
    Element rootElement = new Element ("COREInfoUpdate");
    Document doc = new Document (rootElement);	
		
    
    Element headerElement = new Element ("FILETRANSFERHEADER");
    rootElement.addContent(headerElement);
    
    Element countElement = new Element ("RECORDCOUNT");
    countElement.setText(String.valueOf(errors.size()));
    headerElement.addContent(countElement);
    
    Element processIdElement = new Element ("PROCESSID");
    processIdElement.setText(String.valueOf(data.getProcessId()));
    headerElement.addContent(processIdElement);
    
    Element processingDateElement = new Element ("PROCESSINGDATE");
    processingDateElement.setText(dateFormat.format(date));
    headerElement.addContent(processingDateElement);
    
    Element processingTimeElement = new Element ("PROCESSINGTIME");
    processingTimeElement.setText(timeFormat.format(date));
    headerElement.addContent(processingTimeElement);
    
    Element processingStatusElement = new Element ("PROCESSINGSTATUS");
    processingStatusElement.setText("FAILED");
    headerElement.addContent(processingStatusElement);
    
    Element recordTypeElement = new Element ("RECORD_TYPE");
    recordTypeElement.setText("L");
    headerElement.addContent(recordTypeElement);
    
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
    
    XMLOutputter xmlOutput = new XMLOutputter();
		
	xmlOutput.setFormat(Format.getPrettyFormat());
    FileWriter writer = null;
    boolean isValid = true;
    try {
    		writer = new FileWriter(outputFile);
			xmlOutput.output(doc, writer);
	} 
    catch (IOException e) {
    	logger.log(Level.SEVERE, "Fail to generate acknowledgement XML file.");
    	isValid = false;
	} 
    finally {
    	try {
    		if(writer != null)
    			writer.close();
		} catch (IOException e) {
			logger.severe(String.valueOf(e));
		}
    }
    	if (!isValid) {
    		return null;
    	}
		return outputFile;
	}

	
	private File generateSuccess (IData data) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		
		Date date = new Date ();
		
		// Root element
		Element rootElement = new Element ("COREInfoUpdate");
	    Document doc = new Document (rootElement);	
			
		Element headerElement = new Element ("FILETRANSFERHEADER");
	    rootElement.addContent(headerElement);
			
	    Element countElement = new Element ("RECORDCOUNT");
	    countElement.setText("0");
	    headerElement.addContent(countElement);
	    
	    Element processIdElement = new Element ("PROCESSID");
	    processIdElement.setText(String.valueOf(data.getProcessId()));
	    headerElement.addContent(processIdElement);
	    
	    Element processingDateElement = new Element ("PROCESSINGDATE");
	    processingDateElement.setText(dateFormat.format(date));
	    headerElement.addContent(processingDateElement);
	    
	    Element processingTimeElement = new Element ("PROCESSINGTIME");
	    processingTimeElement.setText(timeFormat.format(date));
	    headerElement.addContent(processingTimeElement);
	    
	    Element processingStatusElement = new Element ("PROCESSINGSTATUS");
	    processingStatusElement.setText("SUCCESS");
	    headerElement.addContent(processingStatusElement);
	    
	    Element recordTypeElement = new Element ("RECORD_TYPE");
	    headerElement.addContent(recordTypeElement);
	    
	    Element recordsElement = new Element ("RECORDS");
	  	rootElement.addContent(recordsElement);
	  	
	    XMLOutputter xmlOutput = new XMLOutputter();
		
		xmlOutput.setFormat(Format.getPrettyFormat());
		FileWriter writer = null;
		boolean isValid = true;
	    try {
	    	writer = new FileWriter(outputFile);
			xmlOutput.output(doc, writer);
		} 
	    catch (IOException e) {
	    	logger.log(Level.SEVERE, "Fail to generate acknowledgement XML file.");
	    	isValid = false;
		} 
	    finally {
	    	try {
	    		if(writer != null)
	    			writer.close();
			} catch (IOException e) {
				logger.log(Level.INFO, "Fail to close FileWriter", e);
			}
	    }
    	if (!isValid) {
    		return null;
    	}
		return outputFile;
  }
}
