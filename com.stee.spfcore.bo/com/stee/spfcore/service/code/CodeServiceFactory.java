package com.stee.spfcore.service.code;

import com.stee.spfcore.service.code.impl.CodeService;

public class CodeServiceFactory {

	private CodeServiceFactory(){}
	private static ICodeService codeService;
	
	
	public static synchronized ICodeService getCodeService () {
		if (codeService == null) {
			codeService = createCodeService ();
		}
		
		return codeService;
	}
	
	private static ICodeService createCodeService () {
		return new CodeService();
	} 
	
}
