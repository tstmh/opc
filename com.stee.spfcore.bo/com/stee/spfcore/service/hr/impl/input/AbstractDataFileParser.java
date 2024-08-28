package com.stee.spfcore.service.hr.impl.input;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.stee.spfcore.service.hr.impl.ErrorConstants;

public abstract class AbstractDataFileParser implements IDataFileParser {
	
	protected Logger logger;
	
	protected AbstractDataFileParser (Logger logger) {
		this.logger = logger;
	}
	
	public IData parse (File inputFile) throws DataException {
		
		SAXBuilder builder = new SAXBuilder();
		// Disable external entity resolution
		builder.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		builder.setFeature("http://xml.org/sax/features/external-general-entities", false);
		builder.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

		Document document;
		
		FileInputStream inStream = null;
		try {
			inStream = new FileInputStream(inputFile);
			document = builder.build(inStream);
		} 
		catch (JDOMException | IOException e) {
			logger.severe(String.format("Fail to parse file: %s %s", inputFile.getAbsolutePath(), e));
			throw new DataException(ErrorConstants.INVALID_XML);
		}
		finally{
			try {
				if(inStream != null)
					inStream.close();
			} catch (IOException e) {
				logger.log(Level.INFO, "Fail to close the FileInputStream", e);
			}
		}
		
		return buildData (document);
	}
	
	protected abstract IData buildData (Document document) throws DataException;
	
}
