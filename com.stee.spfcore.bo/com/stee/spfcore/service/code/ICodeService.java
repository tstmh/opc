package com.stee.spfcore.service.code;

import java.util.List;

import com.stee.spfcore.model.code.Code;
import com.stee.spfcore.model.code.CodeMapping;
import com.stee.spfcore.model.code.CodeType;
import com.stee.spfcore.model.code.ExternalSystemType;
import com.stee.spfcore.vo.code.CodeNamedValuePair;
import com.stee.spfcore.vo.code.CodeTypeIdPair;
import com.stee.spfcore.vo.code.SearchResult;

public interface ICodeService {

    public static final String SYSTEM_USER = "System";

    public List< Code > getCodes( CodeType type );

    public List< Code > getCodes( CodeType type, boolean includeDisabled );

    public List< CodeNamedValuePair > getCodeNamedValuePairs( CodeType type );

    public List< CodeNamedValuePair > getCodeNamedValuePairs( CodeType type, boolean includeDisabled );
    
    public List< CodeNamedValuePair > getCodeNamedValuePairs( CodeType type, boolean includeDisabled, boolean sortByValue );

    public Code getCode( CodeType type, String id );

    public void addCode( Code code, String requester ) throws CodeServiceException;

    public void updateCode( Code code, String requester ) throws CodeServiceException;

    public void enableDisableCodes() throws CodeServiceException;

    public void deleteCode( CodeType type, String id, String requester ) throws CodeServiceException;

    public List< CodeMapping > getCodeMappings( ExternalSystemType extSysType, CodeType codeType ) throws CodeServiceException;

    public List< CodeMapping > getCodeMappings( ExternalSystemType extSysType, CodeType codeType, boolean includeDisabled ) throws CodeServiceException;

    public CodeMapping getCodeMapping( ExternalSystemType extSysType, CodeType codeType, String extId ) throws CodeServiceException;

    public void addCodeMapping( CodeMapping codeMapping, String requester ) throws CodeServiceException;

    public void updateCodeMapping( CodeMapping codeMapping, String requester ) throws CodeServiceException;

    public void deleteCodeMapping( CodeMapping codeMapping, String requester ) throws CodeServiceException;
    
    public List<Code> searchCodes (CodeType type, String id, String description, boolean includeDisabled ) throws CodeServiceException;
    
    public List<CodeNamedValuePair> getInternalIdList (CodeType type, String keyword, boolean includeDisabled) throws CodeServiceException;
    
    public SearchResult searchCodeMappings (ExternalSystemType systemType, CodeType codeType, String externalId, 
				boolean includeDisabled, int pageNum, int pageSize) throws CodeServiceException;
    
    public List<Code> searchCodes ( List<CodeTypeIdPair> codeTypeIdPairs ) throws CodeServiceException;
}
