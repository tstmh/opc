package com.stee.spfcore.service.process;

import com.stee.spfcore.service.process.impl.ProcessService;

public class ProcessServiceFactory {

	private ProcessServiceFactory(){}
	private static IProcessService service = null;
	
	public static synchronized IProcessService getInstance() {
		if (service == null) {
			service = new ProcessService();
		}
		
		return service;
	}
}
