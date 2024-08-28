package com.stee.spfcore.service.hr.impl.input;

import java.util.List;

public interface IData {

	public String getProcessId ();
	
	public int getRecordCount();
	
	public List<IRecord> getRecordList ();
}
