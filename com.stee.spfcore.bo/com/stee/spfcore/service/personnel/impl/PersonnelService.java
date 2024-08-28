package com.stee.spfcore.service.personnel.impl;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.dao.HRProcessingInfoDAO;
import com.stee.spfcore.dao.PersonnelDAO;
import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.model.personnel.ChangeRecord;
import com.stee.spfcore.model.personnel.Employment;
import com.stee.spfcore.model.personnel.ExtraEmploymentInfo;
import com.stee.spfcore.model.personnel.HRChangeRecord;
import com.stee.spfcore.model.personnel.Leave;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.model.personnel.internal.HRProcessingInfo;
import com.stee.spfcore.security.AccessDeniedException;
import com.stee.spfcore.service.personnel.IPersonnelService;
import com.stee.spfcore.service.personnel.PersonnelServiceException;
import com.stee.spfcore.utils.ConvertUtil;
import com.stee.spfcore.utils.Util;
import com.stee.spfcore.vo.personnel.PersonalDepartmentWorkEmail;
import com.stee.spfcore.vo.personnel.PersonalNricName;
import com.stee.spfcore.vo.personnel.PersonalOnContinuousLeaveStatus;
import com.stee.spfcore.vo.personnel.PersonalPreferredContacts;
import com.stee.spfcore.vo.personnel.PersonnelCriteria;
import com.stee.spfcore.vo.personnel.SearchPersonnelResult;
import com.stee.spfcore.vo.personnel.SearchPersonnelResults;

public class PersonnelService implements IPersonnelService {

    private static final Logger logger = Logger.getLogger( PersonnelService.class.getName() );

    private PersonnelDAO dao;
    private HRProcessingInfoDAO hrDao;

    public PersonnelService() {
        dao = new PersonnelDAO();
        hrDao = new HRProcessingInfoDAO();
    }

    @Override
    public PersonalDetail getPersonal( String nric ) throws PersonnelServiceException, AccessDeniedException {
        PersonalDetail result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getPersonal( nric );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( AccessDeniedException e ) {
            logger.severe(String.format("Fail to get personal: %s %s", Util.replaceNewLine( nric ), e ));
            SessionFactoryUtil.rollbackTransaction();
        }
        catch ( Exception e ) {
            logger.severe(String.format("Fail to get personal:%s %s",Util.replaceNewLine( nric ), e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public Employment getEmployment( String nric ) throws PersonnelServiceException, AccessDeniedException {

        Employment result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getEmployment( nric );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( AccessDeniedException e ) {
            logger.severe(String.format( "Fail to get employment: %s %s", nric, e ));
            SessionFactoryUtil.rollbackTransaction();
        }
        catch ( Exception e ) {
            logger.severe(String.format("Fail to get employment: %s %s", nric, e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public void addPersonal( PersonalDetail personal, String requester ) throws PersonnelServiceException, AccessDeniedException {
        logger.info( "start BPMPersonnelService.addPersonal()" );
        try {
            SessionFactoryUtil.beginTransaction();

            dao.addPersonal( personal, requester );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( AccessDeniedException e ) {
            logger.severe(String.format("Fail to add personal: %s %s", Util.replaceNewLine( personal.getNric() ), e ));
            SessionFactoryUtil.rollbackTransaction();
        }
        catch ( Exception exception ) {
            logger.severe(String.format("Fail to add personal: %s %s", Util.replaceNewLine( personal.getNric() ), exception ));
            SessionFactoryUtil.rollbackTransaction();
        }

        logger.info( "end BPMPersonnelService.addPersonal()" );
    }

    @Override
    public void updatePersonal( PersonalDetail personal, String requester ) throws PersonnelServiceException, AccessDeniedException {

        try {
            SessionFactoryUtil.beginTransaction();

            dao.updatePersonal( personal, requester );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( AccessDeniedException e ) {
            logger.severe(String.format("Fail to update personal: %s %s", Util.replaceNewLine( personal.getNric() ), e ));
            SessionFactoryUtil.rollbackTransaction();
        }
        catch ( Exception exception ) {
            logger.severe(String.format( "Fail to update personal: %s %s", Util.replaceNewLine( personal.getNric() ), exception ));
            SessionFactoryUtil.rollbackTransaction();
        }
    }

    @Override
    public List< PersonalDetail > searchPersonnel( String name, String nric, String orgOrDeptCode ) throws PersonnelServiceException {
        long startTimeNowMs = System.currentTimeMillis();
        if ( logger.isLoggable( Level.INFO ) ) {
            logger.info(String.format("start personnel search. name=%s, nric=%s, orgOrDeptCode=%s", name, nric, orgOrDeptCode));
        }
        List< PersonalDetail > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.searchPersonnel( name, nric, orgOrDeptCode );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            logger.severe(String.format("Fail to search personnel: %s, %s, %s %s",name, nric, orgOrDeptCode, e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        long endTimeNowMs = System.currentTimeMillis();
        int durationMs = ( int ) ( endTimeNowMs - startTimeNowMs );
        Integer resultCount = ( result == null ) ? null : result.size();
        if ( logger.isLoggable( Level.INFO ) ) {
            logger.info(String.format("end personnel search. result count=%s, duration=%sms", resultCount, durationMs));
        }
        return result;
    }

    @Override
    public SearchPersonnelResults searchPersonnel( String name, String nric, String department, String uwoDepartment, String uwoSubunit, int maxResults ) throws PersonnelServiceException {
        long startTimeNowMs = System.currentTimeMillis();
        if ( logger.isLoggable( Level.INFO ) ) {
            logger.info(String.format("start personnel search. name=%s, nric=%s, department=%s, uwoDepartment=%s, uwoSubunit=%s, maxResults=%s", name, nric, department, uwoDepartment, uwoSubunit, maxResults));
        }
        SearchPersonnelResults results = null;

        try {
            SessionFactoryUtil.beginTransaction();

            if ( name != null && name.isEmpty()) {
                name = null;
            }
            if ( nric != null && nric.isEmpty()) {
                nric = null;
            }
            if ( department != null && department.isEmpty()) {
                department = null;
            }
            if ( uwoDepartment != null && uwoDepartment.isEmpty()) {
                uwoDepartment = null;
            }
            if ( uwoSubunit != null && uwoSubunit.isEmpty()  ) {
                uwoSubunit = null;
            }

            results = dao.searchPersonnel( name, nric, department, uwoDepartment, uwoSubunit, maxResults );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            String errMsg = String.format( "%s", e.getMessage() );
            logger.info( errMsg );
            SessionFactoryUtil.rollbackTransaction();
            throw new PersonnelServiceException( errMsg );
        }

        long endTimeNowMs = System.currentTimeMillis();
        int durationMs = ( int ) ( endTimeNowMs - startTimeNowMs );
        List< SearchPersonnelResult > resultList = ( results == null ) ? null : results.getResultList();
        Integer resultCount = ( resultList == null ) ? null : resultList.size();
        if ( logger.isLoggable( Level.INFO ) ) {
            logger.info(String.format("end personnel search. result count=%s, duration=%sms", resultCount, durationMs));
        }
        return results;
    }

    @Override
    public void saveHRChangeRecord( HRChangeRecord HRChangeRecordSave ) throws PersonnelServiceException {

        try {
            SessionFactoryUtil.beginTransaction();

            dao.saveHRChangeRecord( HRChangeRecordSave );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception exception ) {
            logger.severe(String.format("Fail to save HR change record for: %s %s", HRChangeRecordSave.getNric(), exception ));
            SessionFactoryUtil.rollbackTransaction();
        }

    }

    @Override
    public List< String > getHRUpdatedPersonnel( Date date ) throws PersonnelServiceException {

        List< String > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getHRUpdatedPersonnel( date );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            logger.severe(String.format( "Fail to search HR Change Record for date: %s %s", date, e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public List< Leave > searchPersonalLeavesByCriteria( PersonnelCriteria personnelCriteria ) throws PersonnelServiceException, AccessDeniedException {
        List< Leave > result = null;

        try {

            SessionFactoryUtil.beginTransaction();
            result = dao.searchPersonalLeavesByCriteria( personnelCriteria );
            SessionFactoryUtil.commitTransaction();

        }
        catch ( AccessDeniedException e ) {
            logger.severe(String.format("Fail to Search Personal By Criteria: %s %s " + personnelCriteria, e ));
            SessionFactoryUtil.rollbackTransaction();
        }
        catch ( RuntimeException e ) {
            logger.severe(String.format("Fail to Search Personal By Criteria: %s %s", personnelCriteria.toString(), e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public ChangeRecord getChangeRecord( String nric ) throws PersonnelServiceException {
        throw new UnsupportedOperationException( "Only Internet side support change record." );
    }

    @Override
    public HRProcessingInfo getHRProcessingInfo( String nric ) throws PersonnelServiceException {

        HRProcessingInfo result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = hrDao.getHRProcessingInfo( nric );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            logger.severe(String.format("Fail to get HR Processing Info: %s %s", nric, e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public void saveHRProcessingInfo( HRProcessingInfo info ) throws PersonnelServiceException {
        try {
            SessionFactoryUtil.beginTransaction();

            hrDao.saveProcessingInfo( info );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception exception ) {
            logger.severe(String.format("Fail to save HR change record for: %s %s", info.getNric(), exception ));
            SessionFactoryUtil.rollbackTransaction();
        }
    }

    @Override
    public List< Object[] > getActivePersonnelForHivScreeningDueOfficerList( List< String > departments, List< String > serviceTypes ) throws PersonnelServiceException {
        List< Object[] > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getActivePersonnelForHivScreeningDueOfficerList( departments, serviceTypes );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.severe(String.format("Fail to get active personnel for hiv screening due officer list. %s", e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public List< Integer > getActivePersonnelCountsGroupByDepartment( List< String > departments, List< String > serviceTypes ) throws PersonnelServiceException {
        List< Integer > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getActivePersonnelCountsGroupByDepartment( departments, serviceTypes );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.severe(String.format( "Fail to get active personnel counts. %s", e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    public List< PersonalPreferredContacts > getActivePcwfMemberPreferredContacts() throws PersonnelServiceException {
        List< PersonalPreferredContacts > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getActivePcwfMemberPreferredContacts();

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.severe(String.format( "Fail getActivePcwfMemberPreferredContacts(). %s", e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    public List< PersonalPreferredContacts > getActivePersonnelPreferredContacts( List< String > departments, List< String > serviceTypes ) throws PersonnelServiceException {
        List< PersonalPreferredContacts > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getActivePersonnelPreferredContacts( departments, serviceTypes, null );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.severe(String.format("Fail getActivePersonnelPreferredContacts(). %s", e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    public List< PersonalPreferredContacts > getPersonnelPreferredContacts( List< String > nrics ) throws PersonnelServiceException {
        List< PersonalPreferredContacts > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getPersonnelPreferredContacts( nrics );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.severe(String.format("Fail getPersonnelPreferredContacts(). %s", e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    public List< PersonalDepartmentWorkEmail > getPersonnelOfficeEmails( List< String > nrics ) throws PersonnelServiceException {
        List< PersonalDepartmentWorkEmail > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getPersonnelOfficeEmails( nrics );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.severe(String.format("Fail getPersonnelOfficeEmails(). %s", e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public String getPersonalName( String nric ) throws PersonnelServiceException {
        String result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getPersonalName( nric );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            logger.severe(String.format( "Fail to get name for personal: %s %s", Util.replaceNewLine( nric ), e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }
    @Override
	public List<PersonalNricName> getPersonalName(List<String> nrics) throws PersonnelServiceException {
		List< PersonalNricName > result = null;
		try 
		{
			SessionFactoryUtil.beginTransaction();

            result = dao.getPersonalName(nrics);

            SessionFactoryUtil.commitTransaction();
		} 
		catch(Exception e) 
		{
			logger.severe(String.format( "Fail to get Personnel Name with list size: %s %s", nrics.size(), e ));
            SessionFactoryUtil.rollbackTransaction();
		}
		return result;
	}

    public List< PersonalOnContinuousLeaveStatus > getOnContinuousLeaveStatuses( List< String > nrics, Date startDate, int numberOfDays ) throws PersonnelServiceException {
        List< PersonalOnContinuousLeaveStatus > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getOnContinuousLeaveStatuses( nrics, startDate, numberOfDays );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            String msg = Util.replaceNewLine( String.format( "Fail getOnContinuousLeaveStatuses(). nrics=%s, startDate=%s, numberOfDays=%s", nrics, startDate, numberOfDays ) );
            logger.log( Level.SEVERE, msg, e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    public Boolean isOnFullMonthNoPayLeave( String nric, Date date ) throws PersonnelServiceException {
        boolean isOnNoPayLeave = false;
        try {
            SessionFactoryUtil.beginTransaction();

            isOnNoPayLeave = dao.isOnFullMonthNoPayLeave( nric, date );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            String errMsg = String.format( "isOnFullMonthNoPayLeave() failed. nric=%s, date=%s", Util.replaceNewLine( nric ), ConvertUtil.convertDateToDateString( date ) );
            logger.severe(String.format(errMsg, e ));
            SessionFactoryUtil.rollbackTransaction();
        }
        return isOnNoPayLeave;
    }

    @Override
    public void saveExtraEmploymentInfo( ExtraEmploymentInfo info ) throws PersonnelServiceException {
        try {
            SessionFactoryUtil.beginTransaction();

            dao.saveExtraEmploymentInfo( info );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception exception ) {
            logger.severe(String.format( "Fail to save extra employment info for: %s %s",info.getNric(), exception ));
            SessionFactoryUtil.rollbackTransaction();
        }
    }

    @Override
    public ExtraEmploymentInfo getExtraEmploymentInfo( String nric ) throws PersonnelServiceException, AccessDeniedException {
        ExtraEmploymentInfo result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getExtraEmploymentInfo( nric );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( AccessDeniedException e ) {
            logger.severe(String.format("Fail to get extra employment info:%s %s", nric, e) );
            SessionFactoryUtil.rollbackTransaction();
        }
        catch ( Exception e ) {
            logger.severe(String.format("Fail to get extra employment info: %s %s", nric, e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }
}
