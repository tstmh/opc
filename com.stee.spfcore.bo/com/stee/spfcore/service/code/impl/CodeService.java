package com.stee.spfcore.service.code.impl;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.dao.CodeDAO;
import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.model.code.Code;
import com.stee.spfcore.model.code.CodeMapping;
import com.stee.spfcore.model.code.CodeType;
import com.stee.spfcore.model.code.ExternalSystemType;
import com.stee.spfcore.service.code.CodeServiceException;
import com.stee.spfcore.service.code.ICodeService;
import com.stee.spfcore.utils.Util;
import com.stee.spfcore.vo.code.CodeNamedValuePair;
import com.stee.spfcore.vo.code.CodeTypeIdPair;
import com.stee.spfcore.vo.code.SearchResult;

public class CodeService implements ICodeService {

    private static final Logger logger = Logger.getLogger( CodeService.class.getName() );

    private CodeDAO dao;

    public CodeService() {
        dao = new CodeDAO();
    }

    @Override
    public List< Code > getCodes( CodeType type ) {

        List< Code > result = null;
        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getCodes( type );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            logger.log( Level.SEVERE, "Fail to get codes of type:" + type, e );
            SessionFactoryUtil.rollbackTransaction();
        }
        return result;
    }

    @Override
    public List< Code > getCodes( CodeType type, boolean includeDisabled ) {
        List< Code > result = null;
        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getCodes( type, includeDisabled );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            logger.log( Level.SEVERE, "Fail to get codes of type:" + type, e );
            SessionFactoryUtil.rollbackTransaction();
        }
        return result;
    }

    public List< CodeNamedValuePair > getCodeNamedValuePairs( CodeType type ) {
        List< CodeNamedValuePair > result = null;
        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getCodeNamedValuePairs( type );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.SEVERE, "Fail to get code named value pairs of type:" + type, e );
            SessionFactoryUtil.rollbackTransaction();
        }
        return result;
    }

    public List< CodeNamedValuePair > getCodeNamedValuePairs( CodeType type, boolean includeDisabled ) {
        List< CodeNamedValuePair > result = null;
        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getCodeNamedValuePairs( type, includeDisabled );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.SEVERE, "Fail to get code named value pairs of type:" + type, e );
            SessionFactoryUtil.rollbackTransaction();
        }
        return result;
    }

    public List< CodeNamedValuePair > getCodeNamedValuePairs( CodeType type, boolean includeDisabled, boolean sortByValue ) {
        List< CodeNamedValuePair > result = null;
        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getCodeNamedValuePairs( type, includeDisabled, sortByValue );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.SEVERE, "Fail to get code named value pairs of type:" + type, e );
            SessionFactoryUtil.rollbackTransaction();
        }
        return result;
    }

    @Override
    public Code getCode( CodeType type, String id ) {
        Code result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getCode( type, id );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            logger.log( Level.SEVERE, "Fail to get codes of type:" + type + " and id:" + Util.replaceNewLine( id ), e );
            SessionFactoryUtil.rollbackTransaction();
        }
        return result;
    }

    @Override
    public void addCode( Code code, String requester ) throws CodeServiceException {

        try {
            SessionFactoryUtil.beginTransaction();

            dao.addCode( code, requester );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception exception ) {
            logger.log( Level.SEVERE, "Fail to add code:" + code, exception );
            SessionFactoryUtil.rollbackTransaction();
        }
    }

    @Override
    public void updateCode( Code code, String requester ) throws CodeServiceException {

        try {
            SessionFactoryUtil.beginTransaction();

            dao.updateCode( code, requester );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception exception ) {
            logger.log( Level.SEVERE, "Fail to add code:" + Util.replaceNewLine( code.getId() ), exception );
            SessionFactoryUtil.rollbackTransaction();
        }
    }

    @Override
    public void enableDisableCodes() throws CodeServiceException {
        try {
            SessionFactoryUtil.beginTransaction();
            Date now = new Date();

            // Enable the Codes having Effective Date as Today.
            boolean toEnable = true;
            dao.updateCodeStatus( toEnable, now, SYSTEM_USER );

            // Disable the Codes having Obsolete Date as Today.
            toEnable = false;
            dao.updateCodeStatus( toEnable, now, SYSTEM_USER );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception exception ) {
            logger.log( Level.SEVERE, "Fail to enable or disable Codes: ", exception );
            SessionFactoryUtil.rollbackTransaction();
        }
    }

    @Override
    public void deleteCode( CodeType type, String id, String requester ) throws CodeServiceException {

        try {
            SessionFactoryUtil.beginTransaction();

            Code code = dao.getCode( type, id );

            dao.deleteCode( code, requester );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception exception ) {
            logger.log( Level.SEVERE, "Fail to delete Code", exception );
            SessionFactoryUtil.rollbackTransaction();
        }
    }

    @Override
    public List< CodeMapping > getCodeMappings( ExternalSystemType extSysType, CodeType codeType ) throws CodeServiceException {
        List< CodeMapping > result = null;
        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getCodeMappings( extSysType, codeType );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            logger.log( Level.SEVERE, "Fail to get code mappings of type:" + codeType, e );
            SessionFactoryUtil.rollbackTransaction();
        }
        return result;
    }

    @Override
    public CodeMapping getCodeMapping( ExternalSystemType extSysType, CodeType codeType, String extId ) throws CodeServiceException {
        CodeMapping result = null;
        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getCodeMapping( extSysType, codeType, extId );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            logger.log( Level.SEVERE, "Fail to get code mapping of type: " + codeType + " and id: " + extId, e );
            SessionFactoryUtil.rollbackTransaction();
        }
        return result;
    }

    @Override
    public void addCodeMapping( CodeMapping codeMapping, String requester ) throws CodeServiceException {
        try {
            SessionFactoryUtil.beginTransaction();

            dao.addCodeMapping( codeMapping, requester );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception exception ) {
            logger.log( Level.SEVERE, "Fail to add code mapping:" + codeMapping, exception );
            SessionFactoryUtil.rollbackTransaction();
        }
    }

    @Override
    public void updateCodeMapping( CodeMapping codeMapping, String requester ) throws CodeServiceException {
        try {
            SessionFactoryUtil.beginTransaction();

            dao.updateCodeMapping( codeMapping, requester );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception exception ) {
            logger.log( Level.SEVERE, "Fail to update code mapping:" + codeMapping, exception );
            SessionFactoryUtil.rollbackTransaction();
        }
    }

    @Override
    public void deleteCodeMapping( CodeMapping codeMapping, String requester ) throws CodeServiceException {
        try {
            SessionFactoryUtil.beginTransaction();

            // Hibernate can't removing a detached instance
            codeMapping = dao.getCodeMapping( codeMapping.getSystemType(), codeMapping.getCodeType(), codeMapping.getExternalId() );
            dao.deleteCodeMapping( codeMapping, requester );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception exception ) {
            logger.log( Level.SEVERE, "Fail to delete code mapping:" + codeMapping, exception );
            SessionFactoryUtil.rollbackTransaction();
        }

    }

    @Override
    public List< CodeMapping > getCodeMappings( ExternalSystemType extSysType, CodeType codeType, boolean includeDisabled ) throws CodeServiceException {
        List< CodeMapping > result = null;
        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getCodeMappings( extSysType, codeType, includeDisabled );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            logger.log( Level.SEVERE, "Fail to get code mappings of type:" + codeType, e );
            SessionFactoryUtil.rollbackTransaction();
        }
        return result;
    }

    @Override
    public List< Code > searchCodes( CodeType type, String id, String description, boolean includeDisabled ) throws CodeServiceException {

        List< Code > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.searchCodes( type, id, description, includeDisabled );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            logger.log( Level.SEVERE, "Fail to search code", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public List< CodeNamedValuePair > getInternalIdList( CodeType type, String keyword, boolean includeDisabled ) throws CodeServiceException {

        List< CodeNamedValuePair > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getInternalIdList( type, keyword, includeDisabled );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            logger.log( Level.SEVERE, "Fail to get internal id list", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public SearchResult searchCodeMappings( ExternalSystemType systemType, CodeType codeType, String externalId, boolean includeDisabled, int pageNum, int pageSize ) throws CodeServiceException {

        SearchResult result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.searchCodeMappings( systemType, codeType, externalId, includeDisabled, pageNum, pageSize );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            logger.log( Level.SEVERE, "Fail to search code mapping", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public List< Code > searchCodes( List< CodeTypeIdPair > codeTypeIdPairs ) throws CodeServiceException {

        List< Code > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.searchCodes( codeTypeIdPairs );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            logger.log( Level.SEVERE, "Fail to search code", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }
}
