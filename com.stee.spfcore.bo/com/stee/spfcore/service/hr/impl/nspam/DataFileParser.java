package com.stee.spfcore.service.hr.impl.nspam;

import java.util.logging.Logger;

import org.jdom2.Document;

import com.stee.spfcore.service.hr.impl.input.AbstractDataFileParser;
import com.stee.spfcore.service.hr.impl.input.DataException;
import com.stee.spfcore.service.hr.impl.input.IData;
import com.stee.spfcore.service.hr.impl.util.CodeMappingUtil;

public class DataFileParser extends AbstractDataFileParser {

	private CodeMappingUtil codeMappingUtil;
	
	protected DataFileParser (Logger logger, CodeMappingUtil codeMappingUtil) {
		super (logger);
		this.codeMappingUtil = codeMappingUtil;
	}
	
	@Override
	protected IData buildData(Document document) throws DataException {
		return new Data(document, codeMappingUtil);
	}
}