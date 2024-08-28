package com.stee.spfcore.service.hrps.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.model.code.Code;
import com.stee.spfcore.model.code.CodeType;
import com.stee.spfcore.service.code.CodeServiceFactory;
import com.stee.spfcore.service.code.ICodeService;

public class CodeUtil {
	
	private static final Logger LOGGER = Logger.getLogger(CodeUtil.class.getName());
	
	private Map<CodeType, Map<String,String>> codesMap = new HashMap<>();
	
	public CodeUtil () {
		
		ICodeService codeService = CodeServiceFactory.getCodeService();
		
		for (CodeType codeType: CodeType.values()) {
			Map<String,String> mappings = new HashMap<>();
			
			List<Code> codes = codeService.getCodes(codeType);
			
			if (LOGGER.isLoggable(Level.FINE)) {
				LOGGER.fine ("Number of Codes for code type " + codeType + " is " + codes.size());
			}
			
			for (Code code: codes) {
				mappings.put(code.getId(), code.getDescription());
			}
			codesMap.put(codeType, mappings);
		}
	}
	
	public String getDescription(CodeType codeType, String id) {
		
		Map<String,String> mappings = codesMap.get(codeType);
		return mappings.get(id.toUpperCase());
	}	
}
