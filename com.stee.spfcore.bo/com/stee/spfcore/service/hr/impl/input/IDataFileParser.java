package com.stee.spfcore.service.hr.impl.input;

import java.io.File;

public interface IDataFileParser {

	public IData parse (File inputFile) throws DataException;
	
	
}
