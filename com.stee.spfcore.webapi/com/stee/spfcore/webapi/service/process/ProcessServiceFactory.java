package com.stee.spfcore.webapi.service.process;

import com.stee.spfcore.webapi.service.process.impl.ProcessService;

public class ProcessServiceFactory {

	private static IProcessService service = null;
	
	public synchronized static IProcessService getInstance() {
		if (service == null) {
			service = new ProcessService();
		}
		
		return service;
	}
}
