package com.stee.spfcore.service.sag.impl;

import com.stee.spfcore.dao.PersonnelDAO;
import com.stee.spfcore.dao.SAGApplicationDAO;
import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.dao.TemplateDAO;
import com.stee.spfcore.model.ApplicationStatus;
import com.stee.spfcore.model.benefits.ApprovalRecord;
import com.stee.spfcore.model.internal.ApplicationType;
import com.stee.spfcore.model.internal.SAGAwardType;
import com.stee.spfcore.model.sag.*;
import com.stee.spfcore.model.sag.inputConfig.SAGInputType;
import com.stee.spfcore.model.sag.inputConfig.SAGInputs;
import com.stee.spfcore.model.sag.inputConfig.SAGSubInputs;
import com.stee.spfcore.notification.BatchElectronicMail;
import com.stee.spfcore.security.AccessDeniedException;
import com.stee.spfcore.service.configuration.IMailRecipientConfig;
import com.stee.spfcore.service.configuration.IMailSenderConfig;
import com.stee.spfcore.service.configuration.ServiceConfig;
import com.stee.spfcore.service.notification.INotificationService;
import com.stee.spfcore.service.notification.NotificationServiceException;
import com.stee.spfcore.service.notification.NotificationServiceFactory;
import com.stee.spfcore.service.sag.ISAGService;
import com.stee.spfcore.service.sag.SAGServiceException;
import com.stee.spfcore.utils.*;
import com.stee.spfcore.utils.template.TemplateUtil;
import com.stee.spfcore.vo.sag.*;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SAGService implements ISAGService {

    private static final Logger logger = Logger.getLogger( SAGService.class.getName() );

	private static final String NOTIFY_PO_AMENDED_SAG_EMAIL_SUBJECT = "SAG-E014_Subject.vm";
	private static final String NOTIFY_PO_AMENDED_SAG_EMAIL_BODY = "SAG-E014_Body.vm";
    
    private static final String CATEGORY_ID = "SAG";
    
    private SAGApplicationDAO sagApplicationDAO;
    private PersonnelDAO personnelDAO;

    public SAGService() {
        sagApplicationDAO = new SAGApplicationDAO();
        personnelDAO = new PersonnelDAO();
    }

    @Override
    public List< SAGApplication > searchSAGApplication( String nric ) throws SAGServiceException, AccessDeniedException {
        List< SAGApplication > sagApplicationList = null;

        logger.log( Level.INFO, "Search SAGApplications by member Nric" );

        try {
            SessionFactoryUtil.beginTransaction();

            sagApplicationList = sagApplicationDAO.searchSAGApplications( nric );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( AccessDeniedException ade ) {
            logger.log( Level.WARNING, "Access Denied to search SAGApplications: " + nric, ade );
            SessionFactoryUtil.rollbackTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to search SAGApplications %s %s", nric, e ));

            SessionFactoryUtil.rollbackTransaction();
        }

        return sagApplicationList;
    }

    @Override
    public List< SAGApplication > searchSAGApplication( String nric, String childNric, String awardType, String financialYear ) throws SAGServiceException, AccessDeniedException {
        return searchSAGApplication( nric, childNric, awardType, financialYear, true );
    }
    
	@Override
	public List<SAGApplication> searchSimliarSAGApplication( String childNric, String financialYear, String referenceNumber) throws SAGServiceException,
			AccessDeniedException {
		List<SAGApplication> sagApplicationList = null;

		logger.log( Level.INFO,
				"Search SAGApplications by awardType and by member Nric" );

		try {
			SessionFactoryUtil.beginTransaction();

			sagApplicationList = sagApplicationDAO.searchSimliarSAGApplication( childNric, financialYear, referenceNumber );

			SessionFactoryUtil.commitTransaction();
		} catch ( AccessDeniedException ade ) {
			logger.log( Level.WARNING,
					"Access Denied to search SAGApplications", ade );
			SessionFactoryUtil.rollbackTransaction();
		} catch ( Exception e ) {
			logger.log( Level.WARNING, "Failed to search SAGApplications", e );

			SessionFactoryUtil.rollbackTransaction();
		}

		return sagApplicationList;
	}
	
	@Override
	public List<SAGApplication> searchSAGApplicationsByChildNricAndEduLevel( String childNric, String awardType, String childNewEduLevel, String childHighestEduLevel, String financialYear) throws SAGServiceException,
			AccessDeniedException {
		List<SAGApplication> sagApplicationList = null;

		logger.log( Level.INFO,
				"Search SAGApplications by child Nric and by education level" );

		try {
			SessionFactoryUtil.beginTransaction();

			sagApplicationList = sagApplicationDAO.searchSAGApplicationsByChildNricAndEduLevel( childNric, awardType, childNewEduLevel, childHighestEduLevel, financialYear);

			SessionFactoryUtil.commitTransaction();
		} catch ( Exception e ) {
			logger.log( Level.WARNING, "Failed to search SAGApplications by child Nric and by education level", e );

			SessionFactoryUtil.rollbackTransaction();
		}

		return sagApplicationList;
	}

    @Override
    public List< SAGApplication > searchSAGApplication( String nric, String childNric, String awardType, String financialYear, boolean isOrderAsc ) throws SAGServiceException, AccessDeniedException {
        List< SAGApplication > sagApplicationList = null;

        logger.log( Level.INFO, "Search SAGApplications by awardType and by member Nric" );

        try {
            SessionFactoryUtil.beginTransaction();

            sagApplicationList = sagApplicationDAO.searchSAGApplications( nric, childNric, awardType, financialYear, isOrderAsc );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( AccessDeniedException ade ) {
            logger.log( Level.WARNING, "Access Denied to search SAGApplications: " + awardType + " --- " + nric, ade );
            SessionFactoryUtil.rollbackTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to search SAGApplications %s --- %s %s", awardType, nric, e ));

            SessionFactoryUtil.rollbackTransaction();
        }

        return sagApplicationList;
    }

    public List< SAGApplication > searchSAGApplicationsBySubmission( String nric, String financialYear ) throws SAGServiceException {
        List< SAGApplication > sagApplicationList = null;

        logger.log( Level.INFO, "Search SAGApplications by Submissions" );

        try {
            SessionFactoryUtil.beginTransaction();

            sagApplicationList = sagApplicationDAO.searchSAGApplicationsBySubmission( nric, financialYear );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to searchSAGApplicationsBySubmission --- %s %s", nric, e ));

            SessionFactoryUtil.rollbackTransaction();
        }

        return sagApplicationList;
    }

    @Override
    public SAGApplication getSAGApplication( String referenceNumber ) throws SAGServiceException, AccessDeniedException {
        SAGApplication sagApplication = null;
        logger.log( Level.INFO, "Get SAGApplication by Reference Number" );

        try {
            SessionFactoryUtil.beginTransaction();

            sagApplication = sagApplicationDAO.getSAGApplication( referenceNumber );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( AccessDeniedException ade ) {
            logger.log( Level.WARNING, "Access Denied to get SAGApplication by referenceNumber: " + Util.replaceNewLine( referenceNumber ), ade );
            SessionFactoryUtil.rollbackTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.WARNING, "Failed to get SAGApplication: " + Util.replaceNewLine( referenceNumber ), e );

            SessionFactoryUtil.rollbackTransaction();
        }

        return sagApplication;
    }

    @Override
    public void saveSAGApplication( SAGApplication sagApplication, String requestor ) throws SAGServiceException, AccessDeniedException {
        logger.log( Level.INFO, "Save (Add/Update) SAG Application" );

        try {

            if ( sagApplication != null ) {

                List< SAGApplication > sagApplicationList = new ArrayList<>();
                sagApplicationList.add( sagApplication );

                saveSAGApplicationList( sagApplicationList, requestor );
            }

        }
        catch ( AccessDeniedException ade ) {
            logger.log( Level.WARNING, "Access Denied to Save SAGApplication: " + Util.replaceNewLine( sagApplication.getReferenceNumber() ), ade );
            SessionFactoryUtil.rollbackTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.WARNING, "Failed to Save SAGApplication: " + Util.replaceNewLine( sagApplication.getReferenceNumber() ), e );

            SessionFactoryUtil.rollbackTransaction();
        }

    }

    /**
     * Return Appropriate ApplicationType for SAG Reference Generator by
     * AwardType.
     * 
     * @param awardType
     * @return
     */
    private ApplicationType setSAGAppTypeForRefGenerator( String awardType ) {

        if ( awardType != null ) {
            SAGAwardType sagAwardType = SAGAwardType.getSAGAwardType( awardType );

            if ( sagAwardType != null ) {
                if ( SAGAwardType.SCHOLASTIC_ACHIEVEMENT_AWARD.equals( sagAwardType ) ) {
                    // return SAA AppType
                    return ApplicationType.SCHOLASTIC_ACHIEVEMENT_AWARD;
                }
                else if ( SAGAwardType.STUDY_AWARD.equals( sagAwardType ) ) {
                    // return SA AppType
                    return ApplicationType.STUDY_AWARD;
                }
                else if ( SAGAwardType.STUDY_GRANT.equals( sagAwardType ) ) {
                    // return SG AppType
                    return ApplicationType.STUDY_GRANT;
                }
                else {
                    return ApplicationType.OTHERS;
                }
            }
        }

        return ApplicationType.OTHERS;
    }

    @Override
    public void saveSAGApplicationList( List< SAGApplication > sagApplicationList, String requestor ) throws SAGServiceException, AccessDeniedException {
        int index = 0;
        try {
            SessionFactoryUtil.beginTransaction();

            if ( sagApplicationList != null ) {
                for ( SAGApplication sagApplication : sagApplicationList ) {

                    index = sagApplicationList.indexOf( sagApplication );

                    if ( logger.isLoggable( Level.INFO ) ) {
                        logger.info(String.format("Saving SAG Application [ %s ]", index));
                    }
                    ApplicationType sagAppType = setSAGAppTypeForRefGenerator( sagApplication.getAwardType() );
                    // Generate Reference Number.
                    boolean isNewSave = false;
                    if ( sagApplication.getReferenceNumber() == null || ( sagApplication.getReferenceNumber() != null && sagApplication.getReferenceNumber().isEmpty() ) ) {
                        logger.log( Level.INFO, "Generate Reference Number for SAG Application" );
                        isNewSave = true;
                        ReferenceNumberGenerator generator = ReferenceNumberGenerator.getInstance();
                        String uniqueId = generator.getUniqueId( sagAppType );

                        sagApplication.setReferenceNumber( uniqueId );
                    }

                    sagApplicationDAO.saveSAGApplication( sagApplication, requestor, isNewSave );
                }
            }

            SessionFactoryUtil.commitTransaction();

        }
        catch ( AccessDeniedException ade ) {
            logger.log( Level.WARNING, "Access Denied to Save SAGApplication(s): " + Util.replaceNewLine( sagApplicationList.get( index ).getReferenceNumber() ), ade );
            SessionFactoryUtil.rollbackTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.WARNING, "Failed to Save SAGApplication(s): " + Util.replaceNewLine( sagApplicationList.get( index ).getReferenceNumber() ), e );

            SessionFactoryUtil.rollbackTransaction();
        }
    }

    @Override
    public void batchUpdateSAGApplications( List< SAGApplication > sagApplicationList, String requestor ) throws SAGServiceException, AccessDeniedException {
        Integer sagApplicationCount = ( sagApplicationList == null ) ? null : sagApplicationList.size();
        if ( logger.isLoggable( Level.INFO ) ) {
            logger.info(String.format("start batchUpdateSAGApplications(), sag application count=%s", sagApplicationCount));
        }
        try {
            SessionFactoryUtil.beginTransaction();

            if ( sagApplicationList != null ) {
            	logger.log( Level.INFO, "sagApplicationList" + sagApplicationList.size());
                List< List< SAGApplication >> batchList = this.splitIntoBatches( sagApplicationList, 100 );
                logger.log( Level.INFO, "batchList" + batchList.size());
                int totalCount = 0;
                for ( List< SAGApplication > tempList : batchList ) {
                    sagApplicationDAO.batchUpdateSAGApplication( tempList, requestor, 100 );
                    logger.info( String.format( "batch count=%s, total=%s", tempList.size(), totalCount ) );
                    totalCount += tempList.size();
                }
            }

            SessionFactoryUtil.commitTransaction();

        }
        catch ( AccessDeniedException ade ) {
            logger.log( Level.WARNING, "Access Denied to batch Update SAGApplication(s): ", ade );
            SessionFactoryUtil.rollbackTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to batch Update SAGApplication(s): %s", e ));
            SessionFactoryUtil.rollbackTransaction();
        }
        logger.info( "end batchUpdateSAGApplications()" );
    }

    @Override
    public List< SAGApplicationResult > searchSAGApplicationByCriteria( SAGApplicationCriteria sagApplicationCriteria, boolean isOrderByReferenceNumber ) throws SAGServiceException {
        List< SAGApplicationResult > sagResults = null;

        try {
            SessionFactoryUtil.beginTransaction();

            sagResults = sagApplicationDAO.searchSAGApplicationByCriteria( sagApplicationCriteria, isOrderByReferenceNumber );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Fail to search SAGApplications by Criteria: %s %s", sagApplicationCriteria.toString(), e ));
        }

        return sagResults;
    }

    public List< SAGInputs > getListOfSAGInputs( String awardType ) throws SAGServiceException {
        List< SAGInputs > sagInputList = null;
        logger.log( Level.INFO, "Get SAGInputs (DropDown List) by AwardType" );

        try {
            SessionFactoryUtil.beginTransaction();

            sagInputList = sagApplicationDAO.getListOfSAGInputs( awardType );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to get SAGInputs List by awardType: %s %s", awardType, e ));

            SessionFactoryUtil.rollbackTransaction();
        }

        return sagInputList;
    }

    @Override
    public List< SAGInputs > getListOfSAGInputByType( String awardType, SAGInputType inputType ) throws SAGServiceException {
        List< SAGInputs > sagInputList = null;
        logger.log( Level.INFO, "Get SAGInputs (DropDown List) by AwardType/InputType" );

        try {
            SessionFactoryUtil.beginTransaction();

            sagInputList = sagApplicationDAO.getListOfSAGInputByType( awardType, inputType );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to get SAGInputs List by awardType/InputType: %s, %s, %s", awardType, ( null != inputType ? inputType.toString() : null ), e ));

            SessionFactoryUtil.rollbackTransaction();
        }

        return sagInputList;
    }

    @Override
    public SAGInputs getSAGInput( String awardType, SAGInputType inputType, String inputId ) throws SAGServiceException {
        SAGInputs sagInput = null;
        logger.log( Level.INFO, "Get SAGInput" );

        try {
            SessionFactoryUtil.beginTransaction();

            sagInput = sagApplicationDAO.getSAGInput( awardType, inputType, inputId );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to get SAGInput: %s, %s, %s %s", awardType, ( null != inputType ? inputType.toString() : null ), inputId, e ));

            SessionFactoryUtil.rollbackTransaction();
        }

        return sagInput;
    }

    @Override
    public List< SAGSubInputs > getSubInputListByCriteria( String awardType, String parentId, SAGInputType parentType ) throws SAGServiceException {
        List< SAGSubInputs > sagSubInputList = null;
        logger.log( Level.INFO, "Get SAGSubInputs (DropDown List) by AwardType/parentId/parentType" );

        try {
            SessionFactoryUtil.beginTransaction();

            sagSubInputList = sagApplicationDAO.getSubInputListByCriteria( awardType, parentId, parentType );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to get SAGSubInputs List by AwardType/parentId/parentType: %s, %s, %s, %s", awardType, parentId, ( null != parentType ? parentType.toString() : null ), e ));

            SessionFactoryUtil.rollbackTransaction();
        }

        return sagSubInputList;
    }

    @Override
    public SAGConfigSetup getConfigSetup( String id ) throws SAGServiceException {
        SAGConfigSetup sagConfigSetup = null;
        logger.log( Level.INFO, "Get SAG Config Setup by Id" );
        try {
            SessionFactoryUtil.beginTransaction();

            sagConfigSetup = sagApplicationDAO.getConfigSetup( id );
            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to get SAG Config Setup by Id: %s %s", id, e ));

            SessionFactoryUtil.rollbackTransaction();
        }
        return sagConfigSetup;
    }

    @Override
    public SAGConfigSetup getConfigSetup( String financialYear, SAGDateConfigType configType ) throws SAGServiceException {
        SAGConfigSetup sagConfigSetup = null;
        logger.log( Level.INFO, "Get SAG Config Setup by financialYear and ConfigType" );
        try {
            SessionFactoryUtil.beginTransaction();

            sagConfigSetup = sagApplicationDAO.getConfigSetup( financialYear, configType );
            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to get SAG Config Setup by financialYear and ConfigType: %s, %s %s",financialYear, configType.toString(), e ));

            SessionFactoryUtil.rollbackTransaction();
        }
        return sagConfigSetup;
    }

    @Override
    public List< SAGConfigSetup > searchSAGConfigSetup( String financialYear ) throws SAGServiceException {
        List< SAGConfigSetup > configSetupList = null;
        logger.log( Level.INFO, "Search SAG Config Setup by year" );
        try {
            SessionFactoryUtil.beginTransaction();

            configSetupList = sagApplicationDAO.searchConfigSetup( financialYear );
            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to search SAG Config Setup by year: %s %s", financialYear, e ));

            SessionFactoryUtil.rollbackTransaction();
        }
        return configSetupList;
    }

    @Override
    public void saveConfigSetup( SAGConfigSetup sagConfigSetup, String requestor ) throws SAGServiceException {
        logger.log( Level.INFO, "Save SAG ConfigSetup" );
        try {
            if ( null != sagConfigSetup ) {
                List< SAGConfigSetup > sagConfigSetupList = new ArrayList<>();
                sagConfigSetupList.add( sagConfigSetup );
                saveConfigSetupList( sagConfigSetupList, requestor );
            }
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to Save SAG ConfigSetup: %s %s", sagConfigSetup.getFinancialYear(), e ));

            SessionFactoryUtil.rollbackTransaction();
        }

    }

    @Override
    public SAGPrivileges getPrivilege( String id ) throws SAGServiceException {
        SAGPrivileges sagPrivilege = null;
        logger.log( Level.INFO, "Get SAG Privileges by Id" );
        try {
            SessionFactoryUtil.beginTransaction();

            sagPrivilege = sagApplicationDAO.getSAGPrivilege( id );
            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to get SAG Privileges by Id: %s %s", id, e ));

            SessionFactoryUtil.rollbackTransaction();
        }
        return sagPrivilege;
    }

    @Override
    public List< SAGPrivileges > searchPrivileges( String financialYear, String memberNric ) throws SAGServiceException {
        List< SAGPrivileges > sagPrivilegesList = null;
        logger.log( Level.INFO, "Search SAG Privileges by year/memberNric" );
        try {
            SessionFactoryUtil.beginTransaction();

            sagPrivilegesList = sagApplicationDAO.searchSAGPrivileges( financialYear, memberNric );
            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to search SAG Privileges by year/memberNric: %s, %s %s",financialYear, memberNric, e ));

            SessionFactoryUtil.rollbackTransaction();
        }
        return sagPrivilegesList;
    }

    @Override
    public SAGPrivilegeUserDetail getPrivilegeUserDetail( String id ) throws SAGServiceException {
        SAGPrivilegeUserDetail sagPrivilegeUserDetail = null;
        logger.log( Level.INFO, "Get SAG Privileges User Detail by Id" );
        try {
            SessionFactoryUtil.beginTransaction();

            sagPrivilegeUserDetail = sagApplicationDAO.getSAGPrivilegeUserDetail(id);
            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to get SAG Privileges User Detail by Id: %s %s", id, e ));

            SessionFactoryUtil.rollbackTransaction();
        }
        return sagPrivilegeUserDetail;
    }

    @Override
    public List< SAGPrivilegeUserDetail > searchPrivilegeUserDetail( String financialYear, String memberNric ) throws SAGServiceException {
        List< SAGPrivilegeUserDetail > sagPrivilegeUserDetailList = null;
        logger.log( Level.INFO, "Search SAG Privileges User Detail by year/memberNric" );
        try {
            SessionFactoryUtil.beginTransaction();

            sagPrivilegeUserDetailList = sagApplicationDAO.searchSAGPrivilegeUserDetail( financialYear, memberNric );
            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to search SAG Privileges User Detail by year/memberNric: %s, %s %s", financialYear, memberNric, e ));

            SessionFactoryUtil.rollbackTransaction();
        }
        return sagPrivilegeUserDetailList;
    }

    @Override
    public void savePrivileges( SAGPrivileges sagPrivilege, String requestor ) throws SAGServiceException {
        logger.log( Level.INFO, "Save SAG Privileges" );
        try {
            if ( null != sagPrivilege ) {
                List< SAGPrivileges > sagPrivilegesList = new ArrayList<>();
                sagPrivilegesList.add( sagPrivilege );
                savePrivilegesList( sagPrivilegesList, requestor );
            }
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to save SAG Privileges: %s %s", sagPrivilege.getFinancialYear(), e ));

            SessionFactoryUtil.rollbackTransaction();
        }

    }

    @Override
    public SAGAwardQuantum getAwardQuantum( String id ) throws SAGServiceException {
        SAGAwardQuantum sagAwardQuantum = null;
        logger.log( Level.INFO, "Get SAG Award Quantum by Id" );
        try {
            SessionFactoryUtil.beginTransaction();

            sagAwardQuantum = sagApplicationDAO.getAwardQuantum( id );
            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format( "Failed to get SAG Award Quantum by Id: %s %s", id, e ));

            SessionFactoryUtil.rollbackTransaction();
        }
        return sagAwardQuantum;
    }

    @Override
    public List< SAGAwardQuantum > searchAwardQuantum( String financialYear, String awardType, String subType ) throws SAGServiceException {
        List< SAGAwardQuantum > sagAwardQuantumList = null;
        logger.log( Level.INFO, "Search SAG Award Quantum by year/awardType/subType" );
        try {
            SessionFactoryUtil.beginTransaction();

            sagAwardQuantumList = sagApplicationDAO.searchAwardQuantum( financialYear, awardType, subType );
            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to search SAG Award Quantum by year/memberNric: %s, %s, %s %s", financialYear, awardType,subType, e) );

            SessionFactoryUtil.rollbackTransaction();
        }
        return sagAwardQuantumList;
    }

    @Override
    public void saveAwardQuantum( SAGAwardQuantum sagAwardQuantum, String requestor ) throws SAGServiceException {
        logger.log( Level.INFO, "Save SAG Award Quantum" );
        try {
            if ( null != sagAwardQuantum ) {
                List< SAGAwardQuantum > awardQuantumList = new ArrayList<>();
                awardQuantumList.add( sagAwardQuantum );
                saveAwardQuantumList( awardQuantumList, requestor );
            }
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to save SAG Award Quantum: %s %s", sagAwardQuantum.getFinancialYear(), e ));

            SessionFactoryUtil.rollbackTransaction();
        }

    }

    @Override
    public SAGEventDetail getEventDetails( String eventId ) throws SAGServiceException {
        SAGEventDetail sagEventDetail = null;
        logger.log( Level.INFO, "Get SAG Event Detail by Id" );
        try {
            SessionFactoryUtil.beginTransaction();

            sagEventDetail = sagApplicationDAO.getSAGEventDetail( eventId );
            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to get SAG Event Detail by Id: %s %s", eventId, e ));

            SessionFactoryUtil.rollbackTransaction();
        }
        return sagEventDetail;
    }

    @Override
    public List< SAGEventDetail > searchEventDetails( String eventId, String financialYear ) throws SAGServiceException {
        List< SAGEventDetail > sagEventDetailList = null;
        logger.log( Level.INFO, "Search SAG Event Detail by year/eventId" );
        try {
            SessionFactoryUtil.beginTransaction();

            sagEventDetailList = sagApplicationDAO.searchEventDetail( eventId, financialYear );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to search SAG Event Detail by year/eventId: %s, %s %s", financialYear, eventId, e ));

            SessionFactoryUtil.rollbackTransaction();
        }
        return sagEventDetailList;
    }

    @Override
    public void saveEventDetail( SAGEventDetail sagEventDetail, String requestor ) throws SAGServiceException {
        logger.log( Level.INFO, "Save SAG Event Detail" );
        try {
            SessionFactoryUtil.beginTransaction();
            sagApplicationDAO.saveSAGEventDetail( sagEventDetail, requestor );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to save SAG Event Detail: %s %s", sagEventDetail.getFinancialYear(), e ));

            SessionFactoryUtil.rollbackTransaction();
        }

    }

    @Override
    public SAGEventRsvp getEventRsvp( String id ) throws SAGServiceException {
        SAGEventRsvp sagEventRsvp = null;
        logger.log( Level.INFO, "Get SAG Event Rsvp by Id" );
        try {
            SessionFactoryUtil.beginTransaction();

            sagEventRsvp = sagApplicationDAO.getEventRsvp( id );
            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format( "Failed to get SAG Event Rsvp by Id: %s %s", id, e ));

            SessionFactoryUtil.rollbackTransaction();
        }
        return sagEventRsvp;
    }

    @Override
    public List< SAGEventRsvp > searchEventRsvp( String financialYear, String eventId, String seqNumberReference, String attendeeName, String attendeeId ) throws SAGServiceException {
        List< SAGEventRsvp > sagEventRsvpList = null;
        logger.log( Level.INFO, "Search SAG Event Rsvp by criteria" );
        try {
            SessionFactoryUtil.beginTransaction();

            sagEventRsvpList = sagApplicationDAO.searchEventRsvp( financialYear, eventId, seqNumberReference, attendeeName, attendeeId );
            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to search SAG Event Rsvp by criteria: %s, %s, %s, %s, %s, %s", financialYear, eventId, seqNumberReference, attendeeName, attendeeId, e ));

            SessionFactoryUtil.rollbackTransaction();
        }
        return sagEventRsvpList;
    }

    @Override
    public void saveEventRsvp( SAGEventRsvp sagEventRsvp, String requestor ) throws SAGServiceException {
        logger.log( Level.INFO, "Save SAG Event Rsvp" );
        try {
            if ( null != sagEventRsvp ) {
                List< SAGEventRsvp > eventRsvpList = new ArrayList<>();
                eventRsvpList.add( sagEventRsvp );
                saveEventRsvpList( eventRsvpList, requestor );
            }
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to save SAG Event Rsvp: %s %s", sagEventRsvp.getFinancialYear(), e ));

            SessionFactoryUtil.rollbackTransaction();
        }

    }

    @Override
    public void deleteEventRsvpList( List< String > rsvpIdList, String requestor ) throws SAGServiceException {
        logger.log( Level.INFO, "Delete SAG Event Rsvp(s) by Id" );
        try {
            SessionFactoryUtil.beginTransaction();

            sagApplicationDAO.deleteEventRsvp( rsvpIdList, requestor );
            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to Delete SAG Event Rsvp(s) by Id: %s %s", rsvpIdList, e ));

            SessionFactoryUtil.rollbackTransaction();
        }
    }

    @Override
    public void saveConfigSetupList( List< SAGConfigSetup > configSetupList, String requestor ) throws SAGServiceException {
        int index = 0;
        try {
            SessionFactoryUtil.beginTransaction();

            if ( configSetupList != null ) {
                for ( SAGConfigSetup sagConfigSetup : configSetupList ) {
                	logger.info("config setup: "+sagConfigSetup);
                    index = configSetupList.indexOf( sagConfigSetup );
                    if ( logger.isLoggable( Level.INFO ) ) {
                        logger.info(String.format("Saving sag config Setup [ %s ]", index));
                    }
                    sagApplicationDAO.saveSAGConfigSetup( sagConfigSetup, requestor );
                }
            }

            SessionFactoryUtil.commitTransaction();

        }
        catch ( Exception e ) {
            if (configSetupList != null) {
                logger.warning(String.format( "Failed to Save SAG Config Setup: %s --- %s %s", index, configSetupList.get( index ).getConfigType().toString(), e ));
            }

            SessionFactoryUtil.rollbackTransaction();
        }
    }

    @Override
    public void savePrivilegesList( List< SAGPrivileges > privilegesList, String requestor ) throws SAGServiceException {
        int index = 0;
        try {
            SessionFactoryUtil.beginTransaction();

            if ( privilegesList != null ) {
                for ( SAGPrivileges sagPrivilege : privilegesList ) {

                    index = privilegesList.indexOf( sagPrivilege );
                    if ( logger.isLoggable( Level.INFO ) ) {
                        logger.info(String.format("Saving sag Privilege [ %s ]", index));
                    }
                    sagApplicationDAO.saveSAGPrivilege( sagPrivilege, requestor );
                }
            }

            SessionFactoryUtil.commitTransaction();

        }
        catch ( Exception e ) {
            if (privilegesList != null) {
                logger.warning(String.format( "Failed to Save SAGPrivileges: %s --- %s %s",index, privilegesList.get( index ).getMemberNric(), e ));
            }

            SessionFactoryUtil.rollbackTransaction();
        }
    }

    @Override
    public void saveAwardQuantumList( List< SAGAwardQuantum > awardQuantumList, String requestor ) throws SAGServiceException {
        int index = 0;
        try {
            SessionFactoryUtil.beginTransaction();

            if ( awardQuantumList != null ) {
                for ( SAGAwardQuantum sagAwardQuantum : awardQuantumList ) {

                    index = awardQuantumList.indexOf( sagAwardQuantum );

                    if ( logger.isLoggable( Level.INFO ) ) {
                        logger.info(String.format("Saving sag Award Quantum [ %s ]", index));
                    }
                    sagApplicationDAO.saveAwardQuantum( sagAwardQuantum, requestor );
                }
            }

            SessionFactoryUtil.commitTransaction();

        }
        catch ( Exception e ) {
            if (awardQuantumList != null) {
                logger.warning(String.format("Failed to Save SAGPrivileges: %s --- %s --- %s %s", index, awardQuantumList.get( index ).getAwardType(), awardQuantumList.get( index ).getSubType(), e ));
            }

            SessionFactoryUtil.rollbackTransaction();
        }
    }

    @Override
    public void saveEventRsvpList( List< SAGEventRsvp > eventRsvpList, String requestor ) throws SAGServiceException {
        int index = 0;
        try {
            SessionFactoryUtil.beginTransaction();

            if ( eventRsvpList != null ) {
                for ( SAGEventRsvp sagEventRsvp : eventRsvpList ) {

                    index = eventRsvpList.indexOf( sagEventRsvp );

                    if ( logger.isLoggable( Level.INFO ) ) {
                        logger.info(String.format("Saving SAG Event Rsvp [ %s ]", index));
                    }
                    sagApplicationDAO.saveEventRsvp( sagEventRsvp, requestor );
                }

            }

            SessionFactoryUtil.commitTransaction();

        }
        catch ( Exception e ) {
            if (eventRsvpList != null) {
                logger.warning(String.format("Failed to Save SAG Event Rsvp: %s --- %s %s", index ,eventRsvpList.get( index ).getAttendeeId(), e ));
            }

            SessionFactoryUtil.rollbackTransaction();
        }
    }

    @Override
    public SAGDonation getDonation( String id ) throws SAGServiceException {
        SAGDonation sagDonation = null;
        logger.log( Level.INFO, "Get SAG Donation by Id" );
        try {
            SessionFactoryUtil.beginTransaction();

            sagDonation = sagApplicationDAO.getDonation( id );
            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format( "Failed to get SAG Donation by Id: %s %s", id, e ));

            SessionFactoryUtil.rollbackTransaction();
        }
        return sagDonation;
    }

    @Override
    public List< SAGDonation > searchDonations( String financialYear, String organization ) throws SAGServiceException {
        List< SAGDonation > sagDonationsList = null;
        logger.log( Level.INFO, "Search SAG Donations by financialYear/Organization" );
        try {
            SessionFactoryUtil.beginTransaction();

            sagDonationsList = sagApplicationDAO.searchDonationList( financialYear, organization );
            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to search SAG Donations by financialYear/Organization: %s, %s %s ", financialYear, organization, e ));

            SessionFactoryUtil.rollbackTransaction();
        }
        return sagDonationsList;
    }

    @Override
    public void saveDonation( SAGDonation sagDonation, String requestor ) throws SAGServiceException {
        logger.log( Level.INFO, "Save SAG Donations" );
        try {
            if ( null != sagDonation ) {
                List< SAGDonation > donationsList = new ArrayList<>();
                donationsList.add( sagDonation );
                saveDonationsList( donationsList, requestor );
            }
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to save SAG Donations: %s %s",sagDonation.getFinancialYear(), e ));

            SessionFactoryUtil.rollbackTransaction();
        }

    }

    @Override
    public void deleteDonations( List< String > donationsIdList, String requestor ) throws SAGServiceException {
        logger.log( Level.INFO, "Delete SAG Donations(s) by Id" );
        try {
            SessionFactoryUtil.beginTransaction();

            sagApplicationDAO.deleteDonations( donationsIdList, requestor );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Delete SAG Donations(s) by Id: %s %s", donationsIdList, e ));

            SessionFactoryUtil.rollbackTransaction();
        }
    }

    @Override
    public void saveDonationsList( List< SAGDonation > sagDonationList, String requestor ) throws SAGServiceException {
        int index = 0;
        try {
            SessionFactoryUtil.beginTransaction();

            if ( sagDonationList != null ) {
                for ( SAGDonation sagDonation : sagDonationList ) {

                    index = sagDonationList.indexOf( sagDonation );

                    if ( logger.isLoggable( Level.INFO ) ) {
                        logger.info(String.format("Saving SAG Donations [ %s ]", index));
                    }
                    sagApplicationDAO.saveDonation( sagDonation, requestor );
                }
            }

            SessionFactoryUtil.commitTransaction();

        }
        catch ( Exception e ) {
            if (sagDonationList != null) {
                logger.warning(String.format("Failed to Save SAG Donations: %s --- %s --- %s %s", index, sagDonationList.get( index ).getFinancialYear(), sagDonationList.get( index ).getOrganization(), e ));
            }

            SessionFactoryUtil.rollbackTransaction();
        }
    }

    @Override
    public List< SAGApplicationChildDetail > searchSAGChildDetails( String financialYear ) throws SAGServiceException {
        List< SAGApplicationChildDetail > sagChildDetails = null;
        logger.log( Level.INFO, "Search SAGApplication Child Details by financialYear" );
        try {
            SessionFactoryUtil.beginTransaction();

            sagChildDetails = sagApplicationDAO.searchChildDetails( financialYear );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to search SAGApplication Child Details by financialYear: %s %s", financialYear, e ));

            SessionFactoryUtil.rollbackTransaction();
        }
        return sagChildDetails;
    }

    @Override
    public void updateApplicationDetailsAfterRSVP( List< SAGApplication > sagApplicationsToUpdate, String requestor ) throws SAGServiceException {
        throw new UnsupportedOperationException( "Only Internet side is allowed to update SAGApplicationDetails After SVP" );

    }

    @Override
    public List< SAGPrivilegePersonalDetails > searchPersonalForManagePrivileges( String name, String nric, String departmentId ) throws SAGServiceException {
        List< SAGPrivilegePersonalDetails > sagPrivilegePersonalDetailList = null;
        logger.log( Level.INFO, "Search SAGPrivilegePersonalDetails by name/nric/departmentId" );
        try {
            SessionFactoryUtil.beginTransaction();

            sagPrivilegePersonalDetailList = sagApplicationDAO.searchPersonalForManagePrivileges( name, nric, departmentId );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to search SAGPrivilegePersonalDetails : %s, %s, %s %s", name, nric, departmentId, e ));

            SessionFactoryUtil.rollbackTransaction();
        }
        return sagPrivilegePersonalDetailList;
    }

    @Override
    public List< SAGApplicationDetailByApprovals > searchSAGApplicationsByApprovalCriteria( SAGApplicationApprovalsCriteria criteria ) throws SAGServiceException {
        List< SAGApplicationDetailByApprovals > sagApplicationByApprovalsList = null;
        logger.log( Level.INFO, "Search SAGApplicationDetailByApprovals by approvalsCriteria" );
        try {
            SessionFactoryUtil.beginTransaction();

            sagApplicationByApprovalsList = sagApplicationDAO.searchSAGApplicationsByApprovalCriteria( criteria );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to search SAGApplicationDetailByApprovals by approvalsCriteria: %s %s", criteria.toString(), e ));

            SessionFactoryUtil.rollbackTransaction();
        }
        return sagApplicationByApprovalsList;
    }

    @Override
    public List< SAGApplicationsApprovedForAward > searchSAGApplicationsApprovedForAward( SAGApplicationCriteria criteria ) throws SAGServiceException {
        List< SAGApplicationsApprovedForAward > sagApplicationsApprovedList = null;
        logger.log( Level.INFO, "search SAGApplications Approved For Award by criteria" );
        try {
            SessionFactoryUtil.beginTransaction();

            sagApplicationsApprovedList = sagApplicationDAO.searchSAGApplicationsApprovedForAward( criteria );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to search SAGApplications Approved For Award by criteria: %s %s", criteria.toString(), e ));

            SessionFactoryUtil.rollbackTransaction();
        }
        return sagApplicationsApprovedList;
    }
    
    @Override
    public List< SAGApplicationsApprovedForAwardWithPayment > searchSAGApplicationsApprovedForAwardWithPayment
    ( SAGApplicationCriteria criteria, String paymentMode, List<String> paymentStatusList ) throws SAGServiceException {
        List< SAGApplicationsApprovedForAwardWithPayment > sagApplicationsApprovedList = null;
        logger.log( Level.INFO, "search SAGApplications Approved For Award by criteria and payment mode" );
        try {
            SessionFactoryUtil.beginTransaction();

            sagApplicationsApprovedList = sagApplicationDAO.searchSAGApplicationsApprovedForAwardWithPayment(criteria, 
            		paymentMode, paymentStatusList);

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to search SAGApplications Approved For Award by criteria, payment mode and status %s" , e ));

            SessionFactoryUtil.rollbackTransaction();
        }
        return sagApplicationsApprovedList;
    }

    @Override
    public List< SAGApplicationsForAudit > searchSAGApplicationsForAudit( String financialYear, String applicationStatus ) throws SAGServiceException {
        List< SAGApplicationsForAudit > sagApplicationsForAuditList = null;
        logger.log( Level.INFO, "search SAGApplications For Audit by FinancialYear" );
        try {
            SessionFactoryUtil.beginTransaction();

            sagApplicationsForAuditList = sagApplicationDAO.searchSAGApplicationsForAudit( financialYear, applicationStatus );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to search SAGApplications For Audit by FinancialYear: %s %s", financialYear, e ));

            SessionFactoryUtil.rollbackTransaction();
        }
        return sagApplicationsForAuditList;
    }

    @Override
    public void updateChequeValueDateForSAGApplications( List< String > referenceNumberList, List< SAGApplicationsApprovedForAward > sagApplicationApprovedForAwardList, String requestor ) throws SAGServiceException, AccessDeniedException {

        if ( null != referenceNumberList && !referenceNumberList.isEmpty() ) {
            List< SAGApplication > sagApplicationsToUpdate = searchSAGApplicationsByReferenceNumber( referenceNumberList );

            if ( null != sagApplicationsToUpdate && null != sagApplicationApprovedForAwardList && !sagApplicationsToUpdate.isEmpty() && !sagApplicationApprovedForAwardList.isEmpty() ) {
                for ( SAGApplication eachApplication : sagApplicationsToUpdate ) {
                    String referenceNumber = eachApplication.getReferenceNumber();

                    for ( SAGApplicationsApprovedForAward each : sagApplicationApprovedForAwardList ) {
                        if ( referenceNumber.equals( each.getReferenceNumber() ) ) {
                            eachApplication.setChequeValueDate( each.getChequeValueDate() );
                            break;
                        }
                    }
                }
                batchUpdateSAGApplications( sagApplicationsToUpdate, requestor );
            }
        }
    }

    public List< SAGApplication > searchSAGApplicationsByReferenceNumber( List< String > referenceNumberList ) throws SAGServiceException, AccessDeniedException {

        List< SAGApplication > sagApplicationList = null;
        logger.log( Level.INFO, "search SAGApplications by ReferenceNumbers" );
        try {
            SessionFactoryUtil.beginTransaction();

            sagApplicationList = sagApplicationDAO.searchSAGApplicationsByReferenceNumber( referenceNumberList );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to search SAGApplications by ReferenceNumbers: %s %s", referenceNumberList.toString(), e ));

            SessionFactoryUtil.rollbackTransaction();
        }

        return sagApplicationList;
    }

    @Override
    public void deletePrivileges( List< String > privilegesIdList, String requestor ) throws SAGServiceException {
        logger.log( Level.INFO, "Delete SAG Donations(s) by Id" );
        try {
            SessionFactoryUtil.beginTransaction();

            sagApplicationDAO.deletePrivileges( privilegesIdList, requestor );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to Delete SAG Privilege(s) by Id: %s %s", privilegesIdList, e ));

            SessionFactoryUtil.rollbackTransaction();
        }

    }

    @Override
    public SAGApplicationsCountDetail getSAGApplicationsCountForYear( String financialYear ) throws SAGServiceException, AccessDeniedException {
        logger.log( Level.INFO, "Get SAG Applications By financialYear" );
        List< SAGApplication > sagApplications = null;
        SAGApplicationsCountDetail applicationCountDetail = null;
        try {
            SessionFactoryUtil.beginTransaction();

            sagApplications = sagApplicationDAO.searchSAGApplicationsByFinancialYear( financialYear );

            SessionFactoryUtil.commitTransaction();

        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to Search SAGApplications by FinancialYear: %s %s" , financialYear, e ));

            SessionFactoryUtil.rollbackTransaction();
        }

        try {
            logger.log( Level.INFO, "generate SAGApplications Count Detail" );

            applicationCountDetail = getSAGApplicationsCount( sagApplications );
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to generate SAGApplications Count Detail: %s %s", financialYear, e ));

            SessionFactoryUtil.rollbackTransaction();
        }

        return applicationCountDetail;
    }

    private SAGApplicationsCountDetail getSAGApplicationsCount( List< SAGApplication > sagApplications ) throws Exception {
        SAGApplicationsCountDetail applicationCountDetail = new SAGApplicationsCountDetail();
        SAGApplicationCountByStatus saaCountByStatus = new SAGApplicationCountByStatus();
        SAGApplicationCountByStatus saCountByStatus = new SAGApplicationCountByStatus();
        SAGApplicationCountByStatus sgCountByStatus = new SAGApplicationCountByStatus();
        int totalCount = 0;
        int saaTotalCount = 0;
        int saTotalCount = 0;
        int sgTotalCount = 0;

        if ( null != sagApplications ) {
            totalCount = sagApplications.size();

            for ( SAGApplication each : sagApplications ) {
                SAGAwardType currentAwardType = SAGAwardType.getSAGAwardType( each.getAwardType() );
                if ( SAGAwardType.SCHOLASTIC_ACHIEVEMENT_AWARD == currentAwardType ) {
                    saaTotalCount = saaTotalCount + 1;
                    updateCountMap( each, saaCountByStatus );
                }
                else if ( SAGAwardType.STUDY_AWARD == currentAwardType ) {
                    saTotalCount = saTotalCount + 1;
                    updateCountMap( each, saCountByStatus );
                }
                else if ( SAGAwardType.STUDY_GRANT == currentAwardType ) {
                    sgTotalCount = sgTotalCount + 1;
                    updateCountMap( each, sgCountByStatus );
                }
            }
        }

        // Set Count Details
        applicationCountDetail.setTotalCount( totalCount );
        applicationCountDetail.setSaaTotalCount( saaTotalCount );
        applicationCountDetail.setSaTotalCount( saTotalCount );
        applicationCountDetail.setSgTotalCount( sgTotalCount );
        applicationCountDetail.setSaaCountByStatus( saaCountByStatus );
        applicationCountDetail.setSaCountByStatus( saCountByStatus );
        applicationCountDetail.setSgCountByStatus( sgCountByStatus );

        return applicationCountDetail;
    }

    private SAGApplicationCountByStatus updateCountMap( SAGApplication sagApplication, SAGApplicationCountByStatus countByTypeStatus ) throws Exception {
        if ( null != sagApplication ) {

            ApplicationStatus status = sagApplication.getApplicationStatus();

            if ( ApplicationStatus.PENDING == status ) {
                countByTypeStatus.setPendingCount( countByTypeStatus.getPendingCount() + 1 );
            }
            else if ( ApplicationStatus.SUPPORTED == status ) {
                countByTypeStatus.setSupportedCount( countByTypeStatus.getSupportedCount() + 1 );
            }
            else if ( ApplicationStatus.SUCCESSFUL == status ) {
                countByTypeStatus.setSuccessfulCount( countByTypeStatus.getSuccessfulCount() + 1 );
            }
            else if ( ApplicationStatus.REJECTED == status ) {
                countByTypeStatus.setRejectedCount( countByTypeStatus.getRejectedCount() + 1 );
            }
            else if ( ApplicationStatus.WITHDRAWN == status ) {
                countByTypeStatus.setWithdrawnCount( countByTypeStatus.getWithdrawnCount() + 1 );
            }
            else if ( ApplicationStatus.AMENDED == status ) {
                countByTypeStatus.setAmendedCount( countByTypeStatus.getAmendedCount() + 1 );
            }
            else if ( ApplicationStatus.UNSUCCESSFUL == status ) {
                countByTypeStatus.setUnsuccessfulCount( countByTypeStatus.getUnsuccessfulCount() + 1 );
            }
            else if ( ApplicationStatus.PENDING_APPROVAL == status ) {
                countByTypeStatus.setBlacklistedCount( countByTypeStatus.getBlacklistedCount() + 1 );
            }
            else if ( ApplicationStatus.REVERTED == status ) {
                countByTypeStatus.setRevertedCount( countByTypeStatus.getRevertedCount() + 1 );
            }
            else if ( ApplicationStatus.RECOMMENDED == status ) {
                countByTypeStatus.setRecommendedCount( countByTypeStatus.getRecommendedCount() + 1 );
            }
            else if ( ApplicationStatus.NOT_RECOMMENDED == status ) {
                countByTypeStatus.setNotRecommendedCount( countByTypeStatus.getNotRecommendedCount() + 1 );
            }
            else if ( ApplicationStatus.VERIFIED == status ) {
                countByTypeStatus.setVerifiedCount( countByTypeStatus.getVerifiedCount() + 1 );
            }
            else if ( ApplicationStatus.REVERTED_TO_PO == status ) {
                countByTypeStatus.setRoutedToPOCount( countByTypeStatus.getRoutedToPOCount() + 1 );
            }

        }

        return countByTypeStatus;
    }

    @Override
    public SAGInputListIdsDescriptionDetail getSAGInputListDescriptions( SAGInputListIdsDescriptionDetail sagInputListIdsDescriptionDetail ) throws SAGServiceException, AccessDeniedException {
        logger.log( Level.INFO, "set SAG inputs Description for application by awardType" );
        try {
            if ( null != sagInputListIdsDescriptionDetail ) {
                List< SAGInputs > sagInputList = getListOfSAGInputs( sagInputListIdsDescriptionDetail.getAwardType() );

                if ( null != sagInputList && !sagInputList.isEmpty() ) {
                    boolean highestEduLevelDescSet = initInputDescriptionFlag( sagInputListIdsDescriptionDetail.getHighestEduLevelInput() );
                    boolean newEduLevelDescSet = initInputDescriptionFlag( sagInputListIdsDescriptionDetail.getNewEduLevelInput() );
                    boolean currSchoolDescSet = initInputDescriptionFlag( sagInputListIdsDescriptionDetail.getCurrentSchoolInput() );
                    boolean academicSubjectInputDescSet = initInputDescriptionFlag( sagInputListIdsDescriptionDetail.getAcademicSubjectInput() );
                    boolean academicGradeInputDescSet = initInputDescriptionFlag( sagInputListIdsDescriptionDetail.getAcademicGradeInput() );

                    for ( SAGInputs eachInput : sagInputList ) {

                        if ( highestEduLevelDescSet && newEduLevelDescSet && currSchoolDescSet && academicSubjectInputDescSet && academicGradeInputDescSet ) {
                            break;
                        }

                        String currInputId = eachInput.getInputId();
                        String currInputDescription = eachInput.getInputDescription();
                        SAGInputType currInputType = eachInput.getSagInputType();

                        if ( !highestEduLevelDescSet ) {
                            String highestEdulLevelDesc = null;
                            highestEdulLevelDesc = getInputDescription( currInputId, currInputType, currInputDescription, sagInputListIdsDescriptionDetail.getHighestEduLevelInput(), SAGInputType.HIGHEST_EDU_LEVEL );
                            sagInputListIdsDescriptionDetail.setHighestEduLevelInputDescription( highestEdulLevelDesc );
                            if ( null != highestEdulLevelDesc && !highestEdulLevelDesc.isEmpty() ) {
                                highestEduLevelDescSet = true;
                                continue;
                            }
                        }

                        // NEW_EDU_LEVEL
                        if ( !newEduLevelDescSet ) {
                            String newEdulLevelDesc = null;
                            newEdulLevelDesc = getInputDescription( currInputId, currInputType, currInputDescription, sagInputListIdsDescriptionDetail.getNewEduLevelInput(), SAGInputType.NEW_EDU_LEVEL );
                            sagInputListIdsDescriptionDetail.setNewEduLevelInputDescription( newEdulLevelDesc );
                            if ( null != newEdulLevelDesc && !newEdulLevelDesc.isEmpty() ) {
                                newEduLevelDescSet = true;
                                continue;
                            }
                        }

                        // CURR_SCHOOL
                        if ( !currSchoolDescSet ) {
                            String currentSchoolDesc = null;
                            currentSchoolDesc = getInputDescription( currInputId, currInputType, currInputDescription, sagInputListIdsDescriptionDetail.getCurrentSchoolInput(), SAGInputType.CURRENT_SCHOOL_INST );
                            sagInputListIdsDescriptionDetail.setCurrentSchoolInputDescription( currentSchoolDesc );
                            if ( null != currentSchoolDesc && !currentSchoolDesc.isEmpty() ) {
                                currSchoolDescSet = true;
                                continue;
                            }
                        }

                        // Academic_Subject
                        if ( !academicSubjectInputDescSet ) {
                            String academicSubjectDesc = null;
                            academicSubjectDesc = getInputDescription( currInputId, currInputType, currInputDescription, sagInputListIdsDescriptionDetail.getAcademicSubjectInput(), SAGInputType.ACADEMIC_RESULT_SUBJECT );

                            sagInputListIdsDescriptionDetail.setAcademicSubjectInputDescription( academicSubjectDesc );
                            if ( null != academicSubjectDesc && !academicSubjectDesc.isEmpty() ) {
                                academicSubjectInputDescSet = true;
                                continue;
                            }
                        }

                        // Academic_Grade
                        if ( !academicGradeInputDescSet ) {
                            String academicGradeDesc = null;
                            academicGradeDesc = getInputDescription( currInputId, currInputType, currInputDescription, sagInputListIdsDescriptionDetail.getAcademicGradeInput(), SAGInputType.ACADEMIC_RESULT_GRADE );

                            sagInputListIdsDescriptionDetail.setAcademicGradeInputDescription( academicGradeDesc );
                            if ( null != academicGradeDesc && !academicGradeDesc.isEmpty() ) {
                                academicGradeInputDescSet = true;
                                continue;
                            }
                        }
                    }

                    // Doc_Type List
                    List< String > docTypeDescriptionList = new ArrayList<>();
                    List< String > docTypeInputList = sagInputListIdsDescriptionDetail.getDocTypeInputList();
                    if ( logger.isLoggable( Level.INFO ) ) {
                        logger.info(String.format("docTypeInputList size = %s", docTypeInputList.size()));
                    }
                    if ( null != docTypeInputList && !docTypeInputList.isEmpty() ) {
                        for ( String eachDocType : docTypeInputList ) {
                            for ( SAGInputs eachInput : sagInputList ) {
                                if ( eachDocType.equals( eachInput.getInputId() ) && eachInput.getSagInputType() == SAGInputType.DOC_TYPE ) {
                                    docTypeDescriptionList.add( eachInput.getInputDescription() );
                                    break;
                                }
                            }
                        }
                        sagInputListIdsDescriptionDetail.setDocTypeInputDescriptionList( docTypeDescriptionList );
                    }
                }
            }
        }
        catch ( Exception e ) {
            logger.warning(String.format( "Failed to set SAG inputs Description for application by awardType: %s %s", sagInputListIdsDescriptionDetail.toString(), e ));

            SessionFactoryUtil.rollbackTransaction();
        }
        return sagInputListIdsDescriptionDetail;
    }

    private String getInputDescription( String currInputId, SAGInputType currInputType, String currInputDescription, String inputIdToCompare, SAGInputType inputTypeToCompare ) {
        String descriptionToSet = null;
        if (( null != inputIdToCompare && !inputIdToCompare.isEmpty() ) && ( inputIdToCompare.equals( currInputId ) && inputTypeToCompare == currInputType )) {
            descriptionToSet = currInputDescription;
        }

        return descriptionToSet;
    }

    private boolean initInputDescriptionFlag( String inputId ) {
        return null == inputId || inputId.isEmpty();
    }

    @Override
    public void updateStatusAndApprovalRecordForSAGApplication( List< String > referenceNumberList, List< SAGStatusApprovalRecordDetail > sagStatusApprovalRecordDetails, String requestor ) throws SAGServiceException, AccessDeniedException {
        logger.log( Level.INFO, "update Batch SAG Application Status and ApprovalRecord" );
        if ( null != referenceNumberList && !referenceNumberList.isEmpty() ) {
            List< SAGApplication > sagApplicationsToUpdate = searchSAGApplicationsByReferenceNumber( referenceNumberList );

            if ( null != sagApplicationsToUpdate && null != sagStatusApprovalRecordDetails && !sagApplicationsToUpdate.isEmpty() && !sagStatusApprovalRecordDetails.isEmpty() ) {
                for ( SAGApplication eachApplication : sagApplicationsToUpdate ) {
                    String referenceNumber = eachApplication.getReferenceNumber();

                    for ( SAGStatusApprovalRecordDetail each : sagStatusApprovalRecordDetails ) {
                        if ( referenceNumber.equals( each.getReferenceNumber() ) ) {
                            if ( null != each.getApplicationStatus() || each.getApplicationStatus() != ApplicationStatus.NONE ) {

                                // set Application status only when its not null
                                // OR not NONE
                                eachApplication.setApplicationStatus( each.getApplicationStatus() );

                                // Default sequenceNumber value set to BLANK.
                                eachApplication.setSequenceNumber( "" );
                                if ( null != each.getSequenceNumber() && !each.getSequenceNumber().isEmpty() ) {
                                    // Set Sequence Number for Successful
                                    // Applications
                                    eachApplication.setSequenceNumber( each.getSequenceNumber() );
                                }

                                if ( null != each.getRemarks() && !each.getRemarks().isEmpty() ) {
                                    // Set Remarks from WPO for each
                                    // application.
                                    eachApplication.setRemarks( each.getRemarks() );
                                }
                            }

                            // Add new ApprovalRecord Entry for the application.
                            List< ApprovalRecord > approvalRecords = eachApplication.getApprovalRecords();
                            if ( null == approvalRecords ) {
                                approvalRecords = new ArrayList<>();
                            }
                            approvalRecords.add( each.getApprovalRecord() );
                            eachApplication.setApprovalRecords( approvalRecords );
                            break;
                        }
                    }
                }
                batchUpdateSAGApplications( sagApplicationsToUpdate, requestor );
            }
        }
    }

    @Override
    public SAGAnnouncementConfig getSAGAnnouncementConfig( String id ) throws SAGServiceException {
        SAGAnnouncementConfig sagAnnouncementConfig = null;
        logger.log( Level.INFO, "Get SAGAnnouncementConfig by id" );

        try {
            SessionFactoryUtil.beginTransaction();

            sagAnnouncementConfig = sagApplicationDAO.getSAGAnnouncementConfig( id );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to get SAGAnnouncementConfig by id: %s %s", id, e ));

            SessionFactoryUtil.rollbackTransaction();
        }

        return sagAnnouncementConfig;
    }

    @Override
    public List< SAGAnnouncementConfig > searchSAGAnnouncementConfig( String financialYear ) throws SAGServiceException {
        List< SAGAnnouncementConfig > sagAnnouncementConfigList = null;

        logger.log( Level.INFO, "Search SAGAnnouncementConfigs by financialYear" );

        try {
            SessionFactoryUtil.beginTransaction();

            sagAnnouncementConfigList = sagApplicationDAO.searchSAGAnnouncementConfigByYear( financialYear );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to search SAGAnnouncementConfigs %s %s", financialYear, e ));

            SessionFactoryUtil.rollbackTransaction();
        }

        return sagAnnouncementConfigList;
    }

    @Override
    public void saveSAGAnnouncementConfig( SAGAnnouncementConfig sagAnnouncementConfig, String requestor ) throws SAGServiceException, AccessDeniedException {
        logger.log( Level.INFO, "Save (Add/Update) SAG Application" );

        try {
            SessionFactoryUtil.beginTransaction();
            if ( sagAnnouncementConfig != null ) {

                sagApplicationDAO.saveSAGAnnouncementConfig( sagAnnouncementConfig, requestor );
            }
            SessionFactoryUtil.commitTransaction();

        }
        catch ( Exception e ) {
            if (sagAnnouncementConfig != null) {
                logger.warning(String.format( "Failed to Save SAGAnnouncementConfig: %s %s", sagAnnouncementConfig.getFinancialYear(), e ));
            }

            SessionFactoryUtil.rollbackTransaction();
        }
    }

    @Override
    public Double getSAGAwardBalanceByYear( String financialYear ) throws SAGServiceException {
        Double balance = null;
        logger.log( Level.INFO, "Get SAG Award(s) balance by Year" );

        try {
            SessionFactoryUtil.beginTransaction();

            balance = sagApplicationDAO.getSAGAwardBalanceByYear( financialYear );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to Get SAG Award(s) balance by Year: %s %s", financialYear, e ));

            SessionFactoryUtil.rollbackTransaction();
        }

        return balance;
    }

    @Override
    public List< String > getSAGAwardYears() throws SAGServiceException {
        List< String > yearsList = new ArrayList<>();

        logger.log( Level.INFO, "Get SAG Award(s) Years" );

        try {
            SessionFactoryUtil.beginTransaction();

            yearsList = sagApplicationDAO.getSAGAwardYears();

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.WARNING, "Failed to Get SAG Award(s) Years: ", e );

            SessionFactoryUtil.rollbackTransaction();
        }

        return yearsList;
    }
    
    @Override
    public SAGEmailContentDetails getSAGEmailContentDetailsNoRsvp( String financialYear ) throws SAGServiceException {
        logger.log( Level.INFO, "Get Email Contents to send to applicant(s) for No RSVP" );
        SAGEmailContentDetails emailContentDetails = new SAGEmailContentDetails();
        try {
        	
            emailContentDetails.setApplicantEmailDetails(new ArrayList<>() );

            SAGApplicationCriteria criteria = new SAGApplicationCriteria();
            criteria.setFinancialYear( financialYear );
            criteria.setRecommendOrSuccessfulApplications( ApplicationStatus.SUCCESSFUL.toString() );

            List< SAGApplicationResult > sagApplicationResults = searchSAGApplicationByCriteria( criteria, false );

            if ( null != sagApplicationResults && !sagApplicationResults.isEmpty() ) {
                Map< String, List< SAGApplicationResult >> sagApplicationResultsMap = convertResultListToMap( sagApplicationResults );

                for ( String submittedBy : sagApplicationResultsMap.keySet() ) {
                    SAGApplicantEmailContentDetail applicantEmailContent = new SAGApplicantEmailContentDetail();
                    applicantEmailContent.setSubmittedById( submittedBy );
                    if ( sagApplicationResultsMap.get( submittedBy ) != null ) {

                        boolean isContactsDetailsUpdated = false;
                        // initialize applicationDetailForEmail List
                        applicantEmailContent.setApplicationDetailForEmailList(new ArrayList<>() );

                        for ( SAGApplicationResult eachResult : sagApplicationResultsMap.get( submittedBy ) ) {
                            // Set SubmittedBy Name Only Once
                            if ( !isContactsDetailsUpdated ) {
                                updateContactDetails( applicantEmailContent, eachResult );
                                isContactsDetailsUpdated = true;
                            }
                            
                            // Get all application by submittedBy
                            if ( submittedBy.equals( eachResult.getSubmittedBy() ) ) {
                                SAGApplicationDetailForEmail appDetailForEmail = new SAGApplicationDetailForEmail();
                                appDetailForEmail.setReferenceNumber( eachResult.getReferenceNumber() );
                                appDetailForEmail.setChildName( eachResult.getChildName() );
                                appDetailForEmail.setApplicationStatus( eachResult.getApplicationStatus() );
                                appDetailForEmail.setAwardType( eachResult.getAwardType() );
                                appDetailForEmail.setAwardTypeDescription( ( SAGAwardType.getSAGAwardType( eachResult.getAwardType() ) ).name().replaceAll( "_", " " ) );
                                appDetailForEmail.setApplicantId(eachResult.getApplicantId());
                                // Added Sequence Number for Email Content.
                                appDetailForEmail.setSequenceNumber( eachResult.getSequenceNumber() );
                                ( applicantEmailContent.getApplicationDetailForEmailList() ).add( appDetailForEmail );
                            }
                        }
                    }
                    emailContentDetails.getApplicantEmailDetails().add( applicantEmailContent );
                }
            }
        }
        catch ( Exception e ) {
            logger.log( Level.WARNING, "Failed to Get Email Content for SAG Award(s) recipients No RSVP: ", e );
        }
        return emailContentDetails;
    }
    
    public SAGEmailContentDetails getSAGEmailContentDetails( String financialYear ) throws SAGServiceException {
        logger.log( Level.INFO, "Get Email Contents to send to applicant(s)" );
        SAGEmailContentDetails emailContentDetails = new SAGEmailContentDetails();
        try {

            SAGConfigSetup rsvpCutOffConfig = getConfigSetup( financialYear, SAGDateConfigType.RSVP_CUT_OFF );
            if ( null != rsvpCutOffConfig ) {
                emailContentDetails.setRsvpCutOffDate( rsvpCutOffConfig.getConfiguredDate() );
            }
            else if ( logger.isLoggable( Level.INFO ) ) {
                logger.warning(String.format("No cut-Off date for RSVP SAG Award Ceremony configured for this year ( %s )", financialYear ));
            }

            List< SAGEventDetail > chequeCollectionList = searchEventDetails( null, financialYear );
            if ( null != chequeCollectionList && !chequeCollectionList.isEmpty() ) {
                for ( SAGEventDetail chequeCollection : chequeCollectionList ) {
                    emailContentDetails.setChequeCollectionStartDate( chequeCollection.getChequeCollection().getStart() );
                    emailContentDetails.setChequeCollectionEndDate( chequeCollection.getChequeCollection().getEnd() );
                    emailContentDetails.setChequeCollectionLocation( chequeCollection.getChequeCollectionLocation() );
                }
            }
            else if ( logger.isLoggable( Level.INFO ) ) {
                logger.warning(String.format("No cheque Collection date for RSVP SAG Award Ceremony configured for this year ( %s )", financialYear));
            }

            emailContentDetails.setApplicantEmailDetails(new ArrayList<SAGApplicantEmailContentDetail>() );

            SAGApplicationCriteria criteria = new SAGApplicationCriteria();
            criteria.setFinancialYear( financialYear );
            criteria.setRecommendOrSuccessfulApplications( ApplicationStatus.SUCCESSFUL.toString() );

            List< SAGApplicationResult > sagApplicationResults = searchSAGApplicationByCriteria( criteria, false );

            if ( null != sagApplicationResults && !sagApplicationResults.isEmpty() ) {
                Map< String, List< SAGApplicationResult >> sagApplicationResultsMap = convertResultListToMap( sagApplicationResults );

                for ( String submittedBy : sagApplicationResultsMap.keySet() ) {
                    SAGApplicantEmailContentDetail applicantEmailContent = new SAGApplicantEmailContentDetail();
                    applicantEmailContent.setSubmittedById( submittedBy );
                    if ( sagApplicationResultsMap.get( submittedBy ) != null ) {

                        boolean isContactsDetailsUpdated = false;
                        // initialize applicationDetailForEmail List
                        applicantEmailContent.setApplicationDetailForEmailList(new ArrayList<>() );

                        for ( SAGApplicationResult eachResult : sagApplicationResultsMap.get( submittedBy ) ) {
                            // Set SubmittedBy Name Only Once
                            if ( !isContactsDetailsUpdated ) {
                                updateContactDetails( applicantEmailContent, eachResult );
                                isContactsDetailsUpdated = true;
                            }

                            if ( submittedBy.equals( eachResult.getApplicantId() ) ) {
                                // submitted by applicant
                                SAGApplicationDetailForEmail appDetailForEmail = new SAGApplicationDetailForEmail();
                                appDetailForEmail.setReferenceNumber( eachResult.getReferenceNumber() );
                                appDetailForEmail.setChildName( eachResult.getChildName() );
                                appDetailForEmail.setApplicationStatus( eachResult.getApplicationStatus() );
                                appDetailForEmail.setAwardType( eachResult.getAwardType() );
                                appDetailForEmail.setAwardTypeDescription( ( SAGAwardType.getSAGAwardType( eachResult.getAwardType() ) ).name().replaceAll( "_", " " ) );
                                // Added Sequence Number for Email Content.
                                appDetailForEmail.setSequenceNumber( eachResult.getSequenceNumber() );
                                appDetailForEmail.setApplicantId( eachResult.getApplicantId() );
                                ( applicantEmailContent.getApplicationDetailForEmailList() ).add( appDetailForEmail );
                            }
                            else {
                                // on Behalf
                                if ( applicantEmailContent.getOnBehalfDetailForEmail() == null ) {
                                    applicantEmailContent.setOnBehalfDetailForEmail( new SAGOnBehalfDetailForEmail() );
                                }
                                updateOnBehalfDetailForEmail( applicantEmailContent.getOnBehalfDetailForEmail(), eachResult );
                            }
                        }
                    }
                    emailContentDetails.getApplicantEmailDetails().add( applicantEmailContent );
                }
            }
        }
        catch ( Exception e ) {
            logger.log( Level.WARNING, "Failed to Get Email Content for SAG Award(s) recipients: ", e );
        }
        return emailContentDetails;
    }

    
    private Map< String, List< SAGApplicationResult >> convertResultListToMap( List< SAGApplicationResult > sagApplicationResults ) {
        Map< String, List< SAGApplicationResult >> sagApplicationResultsMap = new HashMap<>();
        if ( null != sagApplicationResults && !sagApplicationResults.isEmpty() ) {
            for ( SAGApplicationResult each : sagApplicationResults ) {
                String submittedBy = each.getSubmittedBy();
                List< SAGApplicationResult > mapList = null;
                if ( sagApplicationResultsMap.containsKey( submittedBy ) ) {
                    mapList = sagApplicationResultsMap.get( submittedBy );
                }
                else {
                    mapList = new ArrayList<>();
                }
                mapList.add( each );
                sagApplicationResultsMap.put( submittedBy, mapList );
            }
        }

        return sagApplicationResultsMap;
    }

    private void updateOnBehalfDetailForEmail( SAGOnBehalfDetailForEmail onBehalfDetailForEmail, SAGApplicationResult appResult ) {
        if ( null != onBehalfDetailForEmail && null != appResult ) {
            onBehalfDetailForEmail.setTotalCount( onBehalfDetailForEmail.getTotalCount() + 1 );

            if ( SAGAwardType.SCHOLASTIC_ACHIEVEMENT_AWARD == SAGAwardType.getSAGAwardType( appResult.getAwardType() ) ) {
                if ( ApplicationStatus.SUCCESSFUL == appResult.getApplicationStatus() ) {
                    onBehalfDetailForEmail.setSuccessfulSaaCount( onBehalfDetailForEmail.getSuccessfulSaaCount() + 1 );
                }
                else {
                    onBehalfDetailForEmail.setUnsuccessfulSaaCount( onBehalfDetailForEmail.getUnsuccessfulSaaCount() + 1 );
                }
            }
            else if ( SAGAwardType.STUDY_AWARD == SAGAwardType.getSAGAwardType( appResult.getAwardType() ) ) {
                if ( ApplicationStatus.SUCCESSFUL == appResult.getApplicationStatus() ) {
                    onBehalfDetailForEmail.setSuccessfulSaCount( onBehalfDetailForEmail.getSuccessfulSaCount() + 1 );
                }
                else {
                    onBehalfDetailForEmail.setUnsuccessfulSaCount( onBehalfDetailForEmail.getUnsuccessfulSaCount() + 1 );
                }
            }
            else if ( SAGAwardType.STUDY_GRANT == SAGAwardType.getSAGAwardType( appResult.getAwardType() ) ) {
                if ( ApplicationStatus.SUCCESSFUL == appResult.getApplicationStatus() ) {
                    onBehalfDetailForEmail.setSuccessfulSgCount( onBehalfDetailForEmail.getSuccessfulSgCount() + 1 );
                }
                else {
                    onBehalfDetailForEmail.setUnsuccessfulSgCount( onBehalfDetailForEmail.getUnsuccessfulSgCount() + 1 );
                }
            }
        }
    }

    private void updateContactDetails( SAGApplicantEmailContentDetail applicantEmailContent, SAGApplicationResult result ) {
        if ( null != applicantEmailContent && null != result ) {
            applicantEmailContent.setSubmittedByName( result.getSubmittedByName() );

            applicantEmailContent.setPreferredContactMode( result.getSubmittedByPrefContactMode() );

            applicantEmailContent.setPreferredEmail( result.getSubmittedByPrefEmail() );
            
            applicantEmailContent.setPaymentAdviceEmail( result.getSubmittedByPaymentAdviceEmail());

            applicantEmailContent.setWorkEmail( result.getSubmittedByWorkEmail() );

            applicantEmailContent.setPreferredContactNumber( result.getSubmittedByPrefContactNumber() );
        }
    }

    @Override
    public String getMaxSAGSequenceNumber( String financialYear ) throws SAGServiceException {

        logger.log( Level.INFO, "Get Max SAG Sequence Number " );
        String maxSequenceNumber = "";
        try {
            SessionFactoryUtil.beginTransaction();

            maxSequenceNumber = sagApplicationDAO.getMaxSAGSequenceNumber( financialYear );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.WARNING, "Failed to Get Max SAG Sequence Number: ", e );

            SessionFactoryUtil.rollbackTransaction();
        }

        return maxSequenceNumber;
    }

    private List< List< SAGApplication >> splitIntoBatches( List< SAGApplication > sagApplicationList, int batchSize ) {
        List< List< SAGApplication >> batchList = new ArrayList<>();
        logger.log(Level.INFO, "sagApplicationList.size()" + sagApplicationList.size());
        List<SAGApplication> tempList = new ArrayList<>();
        for ( SAGApplication sagApplication : sagApplicationList )
        {
            if(tempList.size() >= batchSize)
            {
                batchList.add( tempList );
                tempList = new ArrayList<>();

            }
            tempList.add( sagApplication );
        }

        if(!tempList.isEmpty()){
            batchList.add( tempList );
        }

        return batchList;
    }

    @Override
	public void saveSAGTask(SAGTask sagTask, String currentUser)
			throws SAGServiceException {
    	logger.log( Level.INFO, "Save (Add/Update) SAG Task" );

        try {

            if ( sagTask != null ) {
            	SessionFactoryUtil.beginTransaction();

            	sagApplicationDAO.saveSAGTask(sagTask, currentUser);

                SessionFactoryUtil.commitTransaction();
            }

        }
        catch ( Exception e ) {
            logger.log( Level.WARNING, "Failed to Save SAG Task: " + Util.replaceNewLine( sagTask.getReferenceNumber() ), e );

            SessionFactoryUtil.rollbackTransaction();
        }
		
	}

	@Override
	public SAGTask getSAGTask(String referenceNumber)
			throws SAGServiceException {
		SAGTask sagTask = null;
        logger.log( Level.INFO, "Get SAG Task by Reference Number" );

        try {
            SessionFactoryUtil.beginTransaction();

            sagTask = sagApplicationDAO.getSAGTask( referenceNumber );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.WARNING, "Failed to get SAG Task: " + Util.replaceNewLine( referenceNumber ), e );

            SessionFactoryUtil.rollbackTransaction();
        }

        return sagTask;
	}

	@Override
	public void deleteSAGTask(String referenceNumber, String currentUser ) throws SAGServiceException {
		logger.log( Level.INFO, "Delete SAG Task" );
		SAGTask sagTask;
        try {

            if ( referenceNumber != null ) {
            	SessionFactoryUtil.beginTransaction();

            	sagTask = sagApplicationDAO.getSAGTask(referenceNumber);
            	sagApplicationDAO.deleteSAGTask(sagTask, currentUser);

                SessionFactoryUtil.commitTransaction();
            }

        }
        catch ( Exception e ) {
            logger.log( Level.WARNING, "Failed to Delete SAG Task: " + Util.replaceNewLine( referenceNumber ), e );

            SessionFactoryUtil.rollbackTransaction();
        }
	}

	@Override
	public void processTask() throws SAGServiceException {
        // This method is intentionally left empty
	}

	@Override
	public void processExpiredApplication() throws SAGServiceException {
		throw new UnsupportedOperationException("Only Internet side is allowed to process Expired SAG Application" );
	}
	
	public List <SAGApplication> searchSAGApplicationBySN (String financialYear, List<String> seqNo) throws SAGServiceException{
		SessionFactoryUtil.beginTransaction();
		
		List <SAGApplication> sagApplications = null;
		try {
			sagApplications = sagApplicationDAO.searchSAGApplicationBySN(financialYear, seqNo);
		
			SessionFactoryUtil.commitTransaction();
		} catch (Exception e) {
			SessionFactoryUtil.rollbackTransaction();
			throw new SAGServiceException("Failed to get SAGApplicationBySN", e);
		}
		return sagApplications;
	}
	
	public List<String> searchSAGReferenceNoBySN (String financialYear, List<String> seqNo) throws SAGServiceException {
		
		SessionFactoryUtil.beginTransaction();
		
		List <String> referenceNoList = null;
		try {
			referenceNoList = sagApplicationDAO.searchSAGReferenceNoBySN(financialYear, seqNo);
		
			SessionFactoryUtil.commitTransaction();
		} catch (Exception e) {
			SessionFactoryUtil.rollbackTransaction();
			throw new SAGServiceException("Failed to get reference number by Seq No.", e);
			
		}
		return referenceNoList;
	}
	
	public List<String> searchSAGSNByReferenceNo (String financialYear, List <String> referenceNoList) throws SAGServiceException {
		
		SessionFactoryUtil.beginTransaction();
		
		List <String> seqNoList = null;
		
		try {
		seqNoList = sagApplicationDAO.searchSAGSNByReferenceNo(financialYear, referenceNoList);
		
		SessionFactoryUtil.commitTransaction();
		} catch (Exception e){
			SessionFactoryUtil.rollbackTransaction();
			throw new SAGServiceException("Failed to get sequnce number by reference number", e);
		}
		
		return seqNoList;
	}
	
	public void updateEventRsvpWithSN (List<String> febIds, String financialYear ,String seqNo, String requestor) throws SAGServiceException {
		
		SessionFactoryUtil.beginTransaction();
		
		List <SAGEventRsvp> eventRsvpList = null;
		try {
			eventRsvpList = sagApplicationDAO.getSAGEventRsvpListByIdList(febIds);
			
			for (SAGEventRsvp eventRsvp: eventRsvpList) {
				if (seqNo != null && (seqNo.length() > 0)) {
                    eventRsvp.setSequenceNumberReference(seqNo);
                    sagApplicationDAO.saveEventRsvp(eventRsvp, requestor);
				}
			}
			
			SessionFactoryUtil.commitTransaction();
		} catch (Exception e){
			SessionFactoryUtil.rollbackTransaction();
			throw new SAGServiceException("Failed to update SAGEventRsvp", e);
		}
	}

	@Override
	public List<SAGAwardQuantumDescription> searchAwardQuantumByFY(String financialYear) throws SAGServiceException {
		SessionFactoryUtil.beginTransaction();
		
		List<SAGAwardQuantumDescription> awardQuantumList = null;
		
		try{
			awardQuantumList = sagApplicationDAO.searchAwardQuantumByFY(financialYear);
			SessionFactoryUtil.commitTransaction();
		} catch(Exception e) {
			SessionFactoryUtil.rollbackTransaction();
			throw new SAGServiceException("Fail to get award quantum by FY", e);
		}
		return awardQuantumList;
	}

	@Override
	public List<SAGApplication> getApplicationWithSameChildNric(String financialYear, String childNric, List<String> statuses,String referenceNumber) throws SAGServiceException {
		SessionFactoryUtil.beginTransaction();
		
		List <SAGApplication> sagApplications = null;
		
		try {
			sagApplications = sagApplicationDAO.getApplicationWithSameChildNric(financialYear, childNric, statuses, referenceNumber);
		
			SessionFactoryUtil.commitTransaction();
		} catch (Exception e) {
			SessionFactoryUtil.rollbackTransaction();
			throw new SAGServiceException("Fail to get application with same child nric", e);
		}
		
		return sagApplications;
	}

	@Override
	public List<SAGApplication> getSuccessfulApplicationByChildNric(List<String> financialYear, String childNric)throws SAGServiceException {
		SessionFactoryUtil.beginTransaction();
		
		List<SAGApplication> sagApplications = null;
		
		try {
			sagApplications = sagApplicationDAO.getSuccessfulApplicationByChildNric(financialYear, childNric);
			
			SessionFactoryUtil.commitTransaction();
		} catch (Exception e) {
			SessionFactoryUtil.rollbackTransaction();
			throw new SAGServiceException("Fail to get successful application by child nric", e);
			
		}
		return sagApplications;
	}

    @Override
    public List<SAGApplicationSelectNricList> getDuplicatedFamilyBackground(
            String financialYear) throws SAGServiceException {
        SessionFactoryUtil.beginTransaction();
        logger.info(String.format("financial Year: %s ",financialYear));

        SAGApplication sagApplication = new SAGApplication();
        List<SAGApplication> SAGApplications = new ArrayList<SAGApplication>();
        List<SAGApplicationFamilyBackground> sagApplicationFamilyBackground = new ArrayList<SAGApplicationFamilyBackground>();
        List<SAGApplicationFamilyBackground> dupSAGApplicationFamilyBackground = new ArrayList<SAGApplicationFamilyBackground>();
        List<SAGApplicationSelectNricList> sagApplicationSelectNricList = new ArrayList<SAGApplicationSelectNricList>();

        try {
            sagApplicationFamilyBackground = sagApplicationDAO.getDuplicatedFamilyBackground(financialYear);

            double salarySum = -1;
            String currentMember = "";
            List<String> duplicateMemberList = new ArrayList<String>();
            int isDup = 0;
            logger.info("size of sagApplicationFamilyBackground: "+ sagApplicationFamilyBackground.size());
            for(int i=0; i<sagApplicationFamilyBackground.size(); i++)
            {
                if(!currentMember.equals(sagApplicationFamilyBackground.get(i).getMemberNric()) )
                {

                    salarySum = sagApplicationFamilyBackground.get(i).getPci();
                    currentMember = sagApplicationFamilyBackground.get(i).getMemberNric();
                    isDup = 0;
                }
                else if(currentMember.equals(sagApplicationFamilyBackground.get(i).getMemberNric())&& salarySum != sagApplicationFamilyBackground.get(i).getPci())
                {
                    if(isDup == 0)
                    {
                        duplicateMemberList.add(currentMember);

                        isDup = 1;
                    }
                }
            }

            for(int i=0; i< duplicateMemberList.size(); i++)
            {
                for(int j=0; j<sagApplicationFamilyBackground.size(); j++)
                {
                    if(sagApplicationFamilyBackground.get(j).getMemberNric().equals(duplicateMemberList.get(i)))
                    {
                        sagApplication = sagApplicationDAO.getSAGApplication(sagApplicationFamilyBackground.get(j).getReferenceNumber());

                        dupSAGApplicationFamilyBackground.add(sagApplicationFamilyBackground.get(j));

                        SAGApplications.add(sagApplication);
                    }
                }

            }
            logger.info("SAGApplication list: "+SAGApplications.size());
            logger.info("dupSAGApplicationFamilyBackground list: "+dupSAGApplicationFamilyBackground.size());

            List<SAGApplicationSelect> sagApplicationSelect = new ArrayList<SAGApplicationSelect>();
            String currentNric = "";
            int numFamily = 1;
            String poName = "";
            Date currentDate = new Date();
            for(SAGApplication app:SAGApplications)
            {
                numFamily = 1;
                SAGApplicationSelect sagApplicationSelectTemp = new SAGApplicationSelect();
                sagApplicationSelectTemp.setIsSelected(false);
                sagApplicationSelectTemp.setSAGApplication(app);
                for(int m=0; m<app.getApprovalRecords().size(); m++)
                {
                    logger.info("officer level: "+app.getApprovalRecords().get(m).getOfficerLevel());
                    if(m==0)
                    {
                        currentDate = app.getApprovalRecords().get(m).getDateOfCompletion();
                    }
                    if(app.getApprovalRecords().get(m).getOfficerLevel().equals("PO"))
                    {
                        poName = app.getApprovalRecords().get(m).getOfficerName();
                    }
                }
                sagApplicationSelectTemp.setPoName(poName);

                for (SAGApplicationFamilyBackground dup: dupSAGApplicationFamilyBackground){
                    if (dup.getReferenceNumber().equals(app.getReferenceNumber())){
                        sagApplicationSelectTemp.setPci(dup.getPci());
                        break;
                    }
                }
                sagApplicationSelect.add(sagApplicationSelectTemp);
            }
            currentNric = "";
            int listCount = -1;
            int list2 = 0;
            logger.info("complete initialization");
            List<SAGApplicationSelect> sagApplicationSelectTempList = new ArrayList<SAGApplicationSelect>();
            SAGApplicationSelectNricList sagApplicationSelectNricListTemp = new SAGApplicationSelectNricList();
            String personalName = "";
            for(int i=0; i<sagApplicationSelect.size(); i++)
            {
                if(!currentNric.equals(sagApplicationSelect.get(i).getSAGApplication().getMemberNric()))
                {
                    logger.info(String.format("current Nric: %s ",currentNric));
                    logger.info("sag application Nric: "+sagApplicationSelect.get(i).getSAGApplication().getMemberNric());
                    if(i!=0)
                    {
                        logger.info("add to list");
                        sagApplicationSelectNricListTemp.setSAGApplicationSelect(sagApplicationSelectTempList);
                        sagApplicationSelectNricList.add(sagApplicationSelectNricListTemp);
                    }
                    currentNric = sagApplicationSelect.get(i).getSAGApplication().getMemberNric();
                    list2 = 0;
                    listCount++;
                    sagApplicationSelectNricListTemp = new SAGApplicationSelectNricList();
                    sagApplicationSelectNricListTemp.setMemberNric(sagApplicationSelect.get(i).getSAGApplication().getMemberNric());
                    personalName = personnelDAO.getPersonalName(sagApplicationSelect.get(i).getSAGApplication().getMemberNric());
                    sagApplicationSelectNricListTemp.setMemberName(personalName);
                    sagApplicationSelectTempList = new ArrayList<SAGApplicationSelect>();
                    sagApplicationSelectTempList.add(sagApplicationSelect.get(i));

                    list2++;
                }
                else if(currentNric.equals(sagApplicationSelect.get(i).getSAGApplication().getMemberNric()))
                {
                    logger.info(String.format("same nric: %s ",currentNric));
                    logger.info("same application nric: "+sagApplicationSelect.get(i).getSAGApplication().getMemberNric());
                    sagApplicationSelectTempList.add(sagApplicationSelect.get(i));
                    list2++;
                    if(i+1 == sagApplicationSelect.size())
                    {
                        logger.info("add the last record into the list: ");
                        sagApplicationSelectNricListTemp.setSAGApplicationSelect(sagApplicationSelectTempList);
                        sagApplicationSelectNricList.add(sagApplicationSelectNricListTemp);
                    }
                }

            }

            SessionFactoryUtil.commitTransaction();
        } catch (Exception e) {
            SessionFactoryUtil.rollbackTransaction();
            throw new SAGServiceException("Fail to get duplicatedFamilyBackground", e);

        }
        return sagApplicationSelectNricList;
    }


	@Override
	public void updateDuplicatedFamilyBackground(
		List<SAGApplication> sagApplicationList) throws SAGServiceException {
		SessionFactoryUtil.beginTransaction();
		try {
			logger.info("List of SAG Applications: "+sagApplicationList);
			List<SAGApplication> sagMemberApplications;
			for(int i=0; i<sagApplicationList.size(); i++)
			{
				logger.info("member nric is: "+sagApplicationList.get(i).getMemberNric());
				logger.info("family background info is: "+sagApplicationList.get(i).getChildFamBackgroundDetails());
				try {
                    sagMemberApplications = sagApplicationDAO.searchSAGApplications(sagApplicationList.get(i).getMemberNric());
					logger.info("Return list: "+sagMemberApplications);
					for(int j=0; j<sagMemberApplications.size(); j++)
					{
                        sagMemberApplications.get(j).setChildFamBackgroundDetails(sagApplicationList.get(i).getChildFamBackgroundDetails());
						sagApplicationDAO.saveSAGApplication(sagMemberApplications.get(j), "celladmin", true);
						logger.info("set finish");
					}
				} catch (AccessDeniedException e) {
					e.printStackTrace();
				}
			}
			SessionFactoryUtil.commitTransaction();
		} catch (Exception e) {
			SessionFactoryUtil.rollbackTransaction();
			throw new SAGServiceException("Fail to get duplicatedFamilyBackground", e);
			
		}
	}

	@Override
	public SAGApplication searchSAGApplicationFamilyBackground(
			String nric, String childNric, String awardType,
			String financialYear, boolean isOrderAsc)
			throws SAGServiceException, AccessDeniedException {
		return null;
	}
	
	@Override
    public List< SAGApplicationChildDetail > searchSAGChildDetailsWithYearEducation( List<String> financialYear, List<String> educationYear ) throws SAGServiceException {
        List< SAGApplicationChildDetail > sagChildDetails = null;
        logger.log( Level.INFO, "Search SAGApplication Child Details by financialYear and educationLevel" );
        try {
            SessionFactoryUtil.beginTransaction();

            sagChildDetails = sagApplicationDAO.searchPrdChildDetails(financialYear, educationYear);

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Failed to search SAGApplication Child Details by financialYear and educationLevel: %s, %s %s", financialYear, educationYear, e ));

            SessionFactoryUtil.rollbackTransaction();
        }
        return sagChildDetails;
    }
	
	@Override
	public List<SAGBatchFileRecord> searchSAGBatchFileRecordByFinancialYear(String financialYear, List<String> paymentStatusList) throws SAGServiceException {
		SessionFactoryUtil.beginTransaction();
		
		List<SAGBatchFileRecord> batchFileRecordList = null;
		
		try{
			batchFileRecordList = sagApplicationDAO.searchSAGBatchFileRecordByFY(financialYear, paymentStatusList);
			
			logger.log( Level.INFO, "search SAG Batch File Records by Financial Year >> List size: " + batchFileRecordList.size() );
			
			SessionFactoryUtil.commitTransaction();
		} catch(Exception e) {
			SessionFactoryUtil.rollbackTransaction();
			throw new SAGServiceException("Fail to get batch file record by FY", e);
		}
		return batchFileRecordList;
	}
	
	@Override
	public void saveSAGBatchFileRecords (List<SAGBatchFileRecord> sagBatchFileRecordList, String requestor) throws SAGServiceException {
		
		if (sagBatchFileRecordList != null && !sagBatchFileRecordList.isEmpty()){
			try{
				SessionFactoryUtil.beginTransaction();
				
				logger.log( Level.INFO, "Saving SAG BatchFileRecords List");
				
				sagApplicationDAO.saveSAGBatchFileRecordList(sagBatchFileRecordList, requestor);
				
				SessionFactoryUtil.commitTransaction();
			} catch(Exception e) {
				SessionFactoryUtil.rollbackTransaction();
				throw new SAGServiceException("Fail to save batch file record list", e);
			}
		}
	}
	
	@Override
	public void updateSAGApplicationPayment( Map<String, Object> context, SAGApplication sagApplication, String requestor ) throws SAGServiceException {
		logger.log( Level.INFO, "Update SAG Application Payment Information" );

		try {
			SessionFactoryUtil.beginTransaction();
			 
			sagApplicationDAO.saveSAGApplication( sagApplication, requestor, false );
			
			//sender email
			IMailSenderConfig mailSenderConfig = ServiceConfig.getInstance().getMailSenderConfig(CATEGORY_ID);
			String senderEmail = mailSenderConfig.senderAddress();
			String senderPassword = mailSenderConfig.senderPassword();
			
			String encryptionKey = EnvironmentUtils.getEncryptionKey();
			
			if (encryptionKey != null && !encryptionKey.isEmpty()) {
				try {
					Encipher encipher = new Encipher(encryptionKey);
					senderPassword = encipher.decrypt(senderPassword);
				}
				catch (Exception e) {
					logger.info("Error while decrypting the configured password");
				}
			}
			
			//retrieve group email list from config of group "SPFCORE_Welfare Planning Processing Officer Admin"
			IMailRecipientConfig recipientConfig = ServiceConfig.getInstance().getMailRecipientConfig(CATEGORY_ID);
			
			List<String> toAddress = retrieveGroupEmails (recipientConfig.toRecipientGroups());
			
			//send email notification 
			sendEmail(context, senderEmail, senderPassword, toAddress, NOTIFY_PO_AMENDED_SAG_EMAIL_SUBJECT, NOTIFY_PO_AMENDED_SAG_EMAIL_BODY);
			
			SessionFactoryUtil.commitTransaction();

		} catch ( Exception e ) {
			logger.log(Level.WARNING, "Failed to Update SAGApplication Payment Information: " + Util.replaceNewLine( sagApplication.getReferenceNumber() ), e );

			SessionFactoryUtil.rollbackTransaction();
		}
	}
	
	private List<String> retrieveGroupEmails (List<String> groups) {
		
		List<String> users = UserGroupUtil.getUsersInGroups(groups);
		if (users == null || users.isEmpty()) {
			return Collections.emptyList();
		}
		
		List<String> addresses = personnelDAO.getOfficeEmailAddress(users);
		
		if (addresses == null || addresses.isEmpty()) {
			return Collections.emptyList();
		}
		
		return addresses;
	}
	
	private boolean sendEmail( Map<String, Object> context, String senderAddress, String senderPassword, List< String > recipients, String subjectTemplate, String bodyTemplate ) {
		
		TemplateUtil templateUtil = TemplateUtil.getInstance();

		String subject = templateUtil.format(subjectTemplate, context);
		String body = templateUtil.format(bodyTemplate, context);
		
        BatchElectronicMail mail = new BatchElectronicMail();
        mail.setHtmlContent( true );
        mail.setSubject( subject );
        mail.setUserAddress( senderAddress );
        mail.setToRecipients( recipients );
        mail.setText( body );
        mail.setUserPassword( senderPassword );

        INotificationService notificationService = NotificationServiceFactory.getInstance();
        try {
            notificationService.send( mail );
            return true;
        }
        catch ( NotificationServiceException e ) {
            logger.log( Level.SEVERE, "Fail to send email to " + recipients, e );
            return false;
        }
    }

	@Override
	public List<SAGBatchFileRecord> searchSAGBatchFileRecordByReferenceNumber(List<String> referenceNumberList) throws SAGServiceException {
		SessionFactoryUtil.beginTransaction();
		
		List<SAGBatchFileRecord> batchFileRecordList = null;
		
		try{
			batchFileRecordList = sagApplicationDAO.searchSAGBatchFileRecordByReferenceNumber(referenceNumberList);
			
			logger.log( Level.INFO, "search SAG Batch File Records by Reference Number >> List size: " + batchFileRecordList.size() );
			
			SessionFactoryUtil.commitTransaction();
		} catch(Exception e) {
			SessionFactoryUtil.rollbackTransaction();
			throw new SAGServiceException("Fail to get batch file record by referenceNumber", e);
		}
		return batchFileRecordList;
	}
  
}