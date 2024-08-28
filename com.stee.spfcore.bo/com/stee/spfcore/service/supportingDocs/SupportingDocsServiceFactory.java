package com.stee.spfcore.service.supportingDocs;

import com.stee.spfcore.service.supportingDocs.impl.SupportingDocsService;

public class SupportingDocsServiceFactory {

	private SupportingDocsServiceFactory(){}
	private static ISupportingDocsService supportingDocsService;

	public static synchronized ISupportingDocsService getSagService() {
		if(supportingDocsService == null){
			supportingDocsService = createSupportingDocsService();
		}
		return supportingDocsService;
	}
	
	private static ISupportingDocsService createSupportingDocsService() {

		return new SupportingDocsService();
		
	}
}
