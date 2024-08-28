package com.stee.spfcore.service.hr.impl.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.model.code.Code;
import com.stee.spfcore.model.code.CodeMapping;
import com.stee.spfcore.model.code.CodeType;
import com.stee.spfcore.model.code.ExternalSystemType;
import com.stee.spfcore.service.code.CodeServiceException;
import com.stee.spfcore.service.code.CodeServiceFactory;
import com.stee.spfcore.service.code.ICodeService;
import com.stee.spfcore.service.hr.impl.input.CodeInfo;

public class CodeMappingUtil {

	private static final Logger LOGGER = Logger.getLogger(CodeMappingUtil.class.getName());
	
	private Map<CodeType, Map<String,String>> codeMappings = new HashMap<>();
	
	public CodeMappingUtil (ExternalSystemType systemType, Logger logger) {
		
		ICodeService codeService = CodeServiceFactory.getCodeService();
		
		for (CodeType codeType : CodeType.values()) {
			Map<String,String> mappings = new HashMap<>();
			codeMappings.put(codeType, mappings);
			
			try {
				List<CodeMapping> codeMappings = codeService.getCodeMappings(systemType, codeType);
				
				if (LOGGER.isLoggable(Level.FINE)) {
					LOGGER.fine ("Number of Code Mapping for code type " + codeType + " for external system " + systemType + " is " + codeMappings.size());
				}
				
				for (CodeMapping mapping : codeMappings) {
					mappings.put(mapping.getExternalId().toUpperCase(), mapping.getInternalId());
				}
			} 
			catch (CodeServiceException e) {
				logger.log(Level.SEVERE, "Unable to retrieve code mapping for type:" + codeType, e);
			}
		}
	}
	
	private String getInternalCode (CodeType codeType, String externalId) {
		
		Map<String,String> mappings = codeMappings.get(codeType);
		return mappings.get(externalId.toUpperCase());
	}	
	
	
	public void convertToInternalCode (CodeInfo codeInfo, CodeType codeType, Set<CodeInfo> nonMatchingCodes) {
		
		String externalId = codeInfo.getValue();
		if (externalId != null && !externalId.isEmpty()) {
			String internalId = getInternalCode(codeType, externalId);
			if (internalId == null) {
				//no match found
				nonMatchingCodes.add(codeInfo);
			}
			else {
				ICodeService codeService = CodeServiceFactory.getCodeService();
				
				Code code = codeService.getCode(codeType, internalId);
				
				if (code == null) {
					//no match found
					nonMatchingCodes.add(codeInfo);
				}
				else {
					codeInfo.setValue(internalId);
					codeInfo.setDescription(code.getDescription());
				}
			}
		}
	}
}
