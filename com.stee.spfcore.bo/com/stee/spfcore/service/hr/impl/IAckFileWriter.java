package com.stee.spfcore.service.hr.impl;

import java.io.File;
import java.util.List;

import com.stee.spfcore.service.hr.impl.input.IData;

public interface IAckFileWriter {

	public File recordCountNotTally (IData data);
	
	public File processStatus (IData data, List<RecordError> errors);
}
