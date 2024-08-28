package com.stee.spfcore.service.sag.impl;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.stee.spfcore.model.ApplicationStatus;
import com.stee.spfcore.model.internal.SAGAwardType;
import com.stee.spfcore.model.sag.SAGApplication;
import com.stee.spfcore.model.sag.SAGEventDetail;
import com.stee.spfcore.model.sag.SAGEventRsvp;
import com.stee.spfcore.model.sag.SAGFamilyBackground;
import com.stee.spfcore.service.personnel.IPersonnelService;
import com.stee.spfcore.service.personnel.PersonnelServiceException;
import com.stee.spfcore.service.personnel.PersonnelServiceFactory;
import com.stee.spfcore.service.sag.ISAGService;
import com.stee.spfcore.service.sag.SAGServiceFactory;
import com.stee.spfcore.utils.ConvertUtil;
import com.stee.spfcore.vo.sag.SAGRsvpTemplate;
import com.stee.spfcore.vo.sag.SAGBatchUploadErrorDetails;

public class SAGBatchRsvpHelper {

	private static final Logger logger = Logger
			.getLogger( SAGBatchRsvpHelper.class.getName() );

	private static final String NO_CONTENT = "-";
	private static final String EMPTY = "";
	private static final String YES = "Y";
	private static final String NO = "N";
	private static final String RSVP_SUBMITTED = "Submitted";
	private static final String regexName = "^[A-Za-z,/.@()\\-\' ]+$";
	private static final String regexNricPassportOthers = "^[A-Za-z0-9/\\-' ]+$";
	private static final String maxCharactersPrefix = "Max. of ";
	private static final String maxCharactersSuffix = " characters is allowed for the field ";
	private static final String alphaCommaSpaceMsg = "Only alphabet, comma, space, forward slash, hyphen, apostrophe, full stop, @ and () are allowed for the field ";

	private static final String EXCEPTION_GENERIC = "Unexpected Exception: Please contact administrator for assistance.";
	private static final String EXCEPTION_RSVP_UPDATE = "Exception Occurred while updating SPF-Lee Foundation Study Award RSVP";
	private static final String EXCEPTION_RECIPIENT_AMEND_NAME_UPDATE = "Exception Occurred while updating SAG Application(s) for Recipient's Amended Name";

	private IPersonnelService personnelService = null;
	private ISAGService sagService = null;

	private List<SAGEventRsvp> eventRsvpListToSaveUpdate = null;
	private List<String> eventRsvpIdsToDelete = null;
	private List<SAGApplication> sagApplicationsToUpdateForChildNameAmend = null;

	public SAGBatchRsvpHelper() {
		personnelService = PersonnelServiceFactory.getPersonnelService();
		sagService = SAGServiceFactory.getSagService();
	}

	public ISAGService getSagService() {
		return sagService;
	}

	public static String getExceptionGeneric() {
		return EXCEPTION_GENERIC;
	}

	public static String getExceptionRsvpUpdate() {
		return EXCEPTION_RSVP_UPDATE;
	}

	public static String getExceptionRecipientAmendNameUpdate() {
		return EXCEPTION_RECIPIENT_AMEND_NAME_UPDATE;
	}

	public List<SAGRsvpTemplate> buildRsvpTemplate( String nric, List<SAGApplication> applicationsBySubmission )
	{
		logger.log( Level.INFO, "Entered buildRsvpTemplate" );
		if ( null == nric || nric.isEmpty() || null == applicationsBySubmission || applicationsBySubmission.isEmpty() ) {
			logger.log( Level.WARNING, "No Login user credential and/or No Successful On-Behalf SAG applications " );
			return null;
		}
		List<SAGRsvpTemplate> rsvpTemplateList = new ArrayList<SAGRsvpTemplate>();
		Map<String, List<SAGApplication>> successfulApplicationsMap = getSuccessfulApplicationsForOnBehalfRsvpList(nric, applicationsBySubmission );

		if ( null != successfulApplicationsMap && !successfulApplicationsMap.isEmpty() )
		{
			logger.log( Level.INFO, "successfulApplicationsMap: " + successfulApplicationsMap.size() );
			for ( String memberNric : successfulApplicationsMap.keySet() )
			{
				String memberName = "";

				try
				{
					memberName = personnelService.getPersonalName( memberNric );
				}
				catch ( PersonnelServiceException e )
				{
					memberName = "";
					logger.log( Level.WARNING, "Failed to get Personal Name for: " + memberNric );
				}

				List<SAGApplication> successfulApplicationList = successfulApplicationsMap.get( memberNric );
				List<String> membersReadList = new ArrayList<String>();

				buildApplicantRsvpTemplate( rsvpTemplateList, membersReadList,memberNric, memberName );
				logger.log( Level.INFO, "rsvpTemplateList --- "+ rsvpTemplateList.size() );
				List<SAGRsvpTemplate> familyInfoRsvpTemplateList = new ArrayList<SAGRsvpTemplate>();
				logger.log( Level.INFO,"No. of successful Application List --- " + successfulApplicationList.size() );
				for ( SAGApplication each : successfulApplicationList )
				{
					buildRecipientRsvpTemplate( rsvpTemplateList, membersReadList, each, memberNric );
					List<SAGFamilyBackground> familyInfoList = each.getChildFamBackgroundDetails();
					buildFamilyMemberRsvpTemplate( familyInfoRsvpTemplateList,membersReadList, familyInfoList, memberNric );
				}
				logger.log( Level.INFO,"rsvpTemplateList After reading Successful Applications--- "+ rsvpTemplateList.size() );
				rsvpTemplateList.addAll( familyInfoRsvpTemplateList );
				logger.log( Level.INFO,"rsvpTemplateList After Adding familyInfo details--- "+ rsvpTemplateList.size() );
				logger.log(Level.INFO, "rsvpTemplateList (final) for memberNric: "+ memberNric + " --- "+ rsvpTemplateList.toString() );
			}
		}

		logger.log( Level.INFO,"rsvpTemplateList (final): size = " + rsvpTemplateList.size()+ " , details = " + rsvpTemplateList.toString() );
		return rsvpTemplateList;
	}

	private void buildRecipientRsvpTemplate(
			List<SAGRsvpTemplate> rsvpTemplateList,
			List<String> membersReadList, SAGApplication sagApplication,
			String memberNric ) {
		if ( null != sagApplication ) {
			SAGRsvpTemplate recipientTemplate = new SAGRsvpTemplate();
			recipientTemplate.setApplicantId( memberNric );
			recipientTemplate.setAttendeeId( sagApplication.getChildNric() );
			recipientTemplate.setAttendeeName( sagApplication.getChildName() );
			recipientTemplate.setAttendeeIsRecipient( YES );
			recipientTemplate.setAttendance( YES );
			recipientTemplate.setReferenceNumber( sagApplication
					.getReferenceNumber() );
			recipientTemplate.setRecipientSequenceNumber( sagApplication
					.getSequenceNumber() );
			recipientTemplate.setRecipientAmendedName( EMPTY );
			recipientTemplate.setReasonForAbsence( EMPTY );
			recipientTemplate.setVegMealRequired( NO );

			membersReadList.add( sagApplication.getChildNric() );
			rsvpTemplateList.add( recipientTemplate );
		}
	}

	private void buildApplicantRsvpTemplate(
			List<SAGRsvpTemplate> rsvpTemplateList,
			List<String> membersReadList, String memberNric, String memberName ) {
		if ( null != memberNric && !memberNric.isEmpty() ) {
			SAGRsvpTemplate recipientTemplate = new SAGRsvpTemplate();
			recipientTemplate.setApplicantId( memberNric );
			recipientTemplate.setAttendeeId( memberNric );
			recipientTemplate.setAttendeeName( memberName );
			recipientTemplate.setAttendeeIsRecipient( NO );
			recipientTemplate.setAttendance( YES );
			recipientTemplate.setReferenceNumber( NO_CONTENT );
			recipientTemplate.setRecipientSequenceNumber( NO_CONTENT );
			recipientTemplate.setRecipientAmendedName( EMPTY );
			recipientTemplate.setReasonForAbsence( EMPTY );
			recipientTemplate.setVegMealRequired( NO );

			membersReadList.add( memberNric );
			rsvpTemplateList.add( recipientTemplate );
		}
	}

	private void buildFamilyMemberRsvpTemplate(
			List<SAGRsvpTemplate> familyInfoRsvpTemplateList,
			List<String> membersReadList,
			List<SAGFamilyBackground> familyInfoList, String memberNric ) {
		if ( null != familyInfoList && !familyInfoList.isEmpty() ) {
			for ( SAGFamilyBackground eachFamInfo : familyInfoList ) {
				String familyMemberNric = eachFamInfo.getIdNo();
				if ( null != membersReadList
						&& !membersReadList.contains( familyMemberNric ) ) {
					SAGRsvpTemplate recipientTemplate = new SAGRsvpTemplate();
					recipientTemplate.setApplicantId( memberNric );
					recipientTemplate.setAttendeeId( familyMemberNric );
					recipientTemplate.setAttendeeName( eachFamInfo.getName() );
					recipientTemplate.setAttendeeIsRecipient( NO );
					recipientTemplate.setAttendance( NO );
					recipientTemplate.setReferenceNumber( NO_CONTENT );
					recipientTemplate.setRecipientSequenceNumber( NO_CONTENT );
					recipientTemplate.setRecipientAmendedName( EMPTY );
					recipientTemplate.setReasonForAbsence( EMPTY );
					recipientTemplate.setVegMealRequired( NO );

					membersReadList.add( familyMemberNric );
					familyInfoRsvpTemplateList.add( recipientTemplate );
				}
			}
		}
	}

	private Map<String, List<SAGApplication>> getSuccessfulApplicationsForOnBehalfRsvpList(String nric, List<SAGApplication> applicationsBySubmission ) {
		logger.log( Level.INFO,
				"Get Successful Applications mapped by Applicant Nric." );
		Map<String, List<SAGApplication>> successfulApplicationsMap = null;
		if ( null != applicationsBySubmission
				&& !applicationsBySubmission.isEmpty() ) {
			successfulApplicationsMap = new HashMap<String, List<SAGApplication>>();
			for ( SAGApplication each : applicationsBySubmission )
			{
				// add to list if on-behalf successful application
				if ( !nric.equals( each.getMemberNric() ) && !each.getAwardType().equals(SAGAwardType.STUDY_GRANT.toString() ) && ApplicationStatus.SUCCESSFUL == each.getApplicationStatus() )
				{
					List<SAGApplication> applicationsByApplicant = null;
					if ( successfulApplicationsMap.containsKey( each
							.getMemberNric() ) )
					{
						applicationsByApplicant = successfulApplicationsMap
								.get( each.getMemberNric() );
					}
					else
					{
						applicationsByApplicant = new ArrayList<SAGApplication>();
					}
					applicationsByApplicant.add( each );
					successfulApplicationsMap.put( each.getMemberNric(),
							applicationsByApplicant );
				}
			}
			logger.log(
					Level.INFO,
					("Exiting getSuccessfulApplicationsForOnBehalfRsvpList --- successfulApplicationsMap: "
							+ successfulApplicationsMap.size()).replace('\n','_').replace('\r','_'));
		}


		return successfulApplicationsMap;
	}

	private String getSeqNumberReference( List<SAGApplication> applicationList ) {
		String seqNumberReference = "";
		String delimiter = "";
		List<String> seqNumberRefList = new ArrayList<String>();
		for ( SAGApplication each : applicationList ) {
			if ( null != each.getSequenceNumber() ) {
				seqNumberRefList.add( each.getSequenceNumber() );
			}
		}
		if ( null != seqNumberRefList ) {
			Collections.sort( seqNumberRefList );
			for ( String eachInt : seqNumberRefList ) {
				seqNumberReference = seqNumberReference + delimiter + eachInt;
				delimiter = ",";
			}
		}

		return seqNumberReference;
	}

	public String uploadBatchRsvpResponses( String nric,
											List<SAGApplication> applicationsBySubmission,
											Map<String, List<SAGRsvpTemplate>> rsvpResponseListMap )
			throws Exception {
		logger.log( Level.INFO, "Entered uploadBatchRsvpResponses" );
		if ( null == nric || nric.isEmpty() || null == applicationsBySubmission
				|| applicationsBySubmission.isEmpty()
				|| null == rsvpResponseListMap || rsvpResponseListMap.isEmpty() ) {
			logger.log(
					Level.WARNING,
					"No Login user credential / No Successful On-Behalf SAG applications / No Rsvp Responses" );
			return "No Data available for upload.";
		}

		this.eventRsvpListToSaveUpdate = new ArrayList<SAGEventRsvp>();
		this.eventRsvpIdsToDelete = new ArrayList<String>();
		this.sagApplicationsToUpdateForChildNameAmend = new ArrayList<SAGApplication>();

		String financialYear = ConvertUtil.convertToFinancialYearString();
		Map<String, List<SAGApplication>> successfulApplicationsMap = getSuccessfulApplicationsForOnBehalfRsvpList(
				nric, applicationsBySubmission );

		// Get Event Detail
		String sagEventId = "";
		List<SAGEventDetail> eventDetailList = sagService.searchEventDetails(
				null, financialYear );

		if ( null != eventDetailList && !eventDetailList.isEmpty() ) {
			SAGEventDetail sagEventDetail = eventDetailList.get( 0 );

			logger.log( Level.INFO,
					"sagEventDetail --- " + sagEventDetail.getId() );

			if(sagEventDetail != null)
				sagEventId = sagEventDetail.getId();
		}

		// Get RSVP Master List for the FinancialYear and event.
		List<SAGEventRsvp> eventRsvpMasterList = sagService.searchEventRsvp(
				financialYear, sagEventId, "", "", "" );
		List<SAGBatchUploadErrorDetails> unprocessedList = new ArrayList<SAGBatchUploadErrorDetails>();
		List<String> getUniqueMemberId = new ArrayList<String>();

		for ( String applicantId : rsvpResponseListMap.keySet() ) {

			getUniqueMemberId.add(applicantId);
			List<SAGBatchUploadErrorDetails> updated = updateSAGApplicationAndRsvp( nric, applicantId,
					sagEventId, financialYear,
					successfulApplicationsMap.get( applicantId ),
					rsvpResponseListMap.get( applicantId ), eventRsvpMasterList );
			{
				logger.log( Level.INFO, "list updated: " + updated);
				if (!updated.isEmpty())
				{
					for ( SAGBatchUploadErrorDetails eachId : updated )
					{
						unprocessedList.add(eachId);
					}
				}
			}
		}

		//Update SAG Event Rsvp List.
		try {
			if( null != this.eventRsvpListToSaveUpdate && !this.eventRsvpListToSaveUpdate.isEmpty() ) {
				//create a new method to loop over eventRsvpListToSaveUpdate and
				//re-enter the updated order.
				for (int uniqueMemberCount=0; uniqueMemberCount < getUniqueMemberId.size(); uniqueMemberCount++)
				{
					int newOrder = 0;
					for (int uniqueMemberListCount=0; uniqueMemberListCount< rsvpResponseListMap.get(getUniqueMemberId.get(uniqueMemberCount)).size(); uniqueMemberListCount++)
					{
						for ( int eventRsvpListCount=0; eventRsvpListCount < eventRsvpListToSaveUpdate.size(); eventRsvpListCount++ )
						{
							if (eventRsvpListToSaveUpdate.get(eventRsvpListCount).getAttendeeId().equals(rsvpResponseListMap.get(getUniqueMemberId.get(uniqueMemberCount)).get(uniqueMemberListCount).getAttendeeId()))
							{
								eventRsvpListToSaveUpdate.get(eventRsvpListCount).setOrder(++newOrder);
							}
						}
					}
				}
				sagService.saveEventRsvpList( this.eventRsvpListToSaveUpdate, nric );
			}

			//Delete SAg Event Rsvp(s).
			if ( this.eventRsvpIdsToDelete != null
					&& !this.eventRsvpIdsToDelete.isEmpty() ) {
				sagService.deleteEventRsvpList( this.eventRsvpIdsToDelete, nric );
			}
		} catch ( Exception e ) {
			// TODO Auto-generated catch block
			logger.log( Level.SEVERE, "Exception Occurred while updating SPF-Lee Foundation Study Award RSVP: "+ e.getMessage() +" --- "+e);
			throw new Exception(EXCEPTION_RSVP_UPDATE, e);
		}

		try {
			// Update SAGApplication(s) for Child Name Amend.
			if( null != this.sagApplicationsToUpdateForChildNameAmend && !this.sagApplicationsToUpdateForChildNameAmend.isEmpty() ) {
				sagService.updateApplicationDetailsAfterRSVP( this.sagApplicationsToUpdateForChildNameAmend, nric );
			}
		} catch ( Exception e ) {
			// TODO Auto-generated catch block
			logger.log( Level.SEVERE, "Exception Occurred while updating SAG Application(s) for Recipient's Amended Name: "+ e.getMessage() +" --- "+e);
			throw new Exception(EXCEPTION_RECIPIENT_AMEND_NAME_UPDATE, e);
		}

		logger.log( Level.INFO,
				"unprocessed Ids: " + unprocessedList.toString() );
		String result = "SPF-Lee Foundation RSVP uploaded successfully.";

		if ( !unprocessedList.isEmpty() )
		{
			result = result
					+ "<br>Details for the following applicant(s) were not processed: <table border='1'><th width='6%'>Row No.</th><th width='10%'>Applicant ID</th><th width='10%'>Attendee ID</th><th width='74%'>Description</th>";

			for ( SAGBatchUploadErrorDetails eachId : unprocessedList ) {
				result = result + "<tr><td>" + eachId.getRowNumber() + "</td><td>" + eachId.getApplicantId() + "</td><td>" + eachId.getAttendeeId() + "</td><td>" + eachId.getErrorMessage() + "</td>";
			}
			result = result + "</table>";
		}

		logger.log( Level.INFO, "result: " + result );
		return result;
	}

	List<String> attendeeCountObj = new ArrayList<String>();
	List<String> familyMemberCountObj = new ArrayList<String>();
	List<String> familyMemberValidateCountObj = new ArrayList<String>();
	@SuppressWarnings( "unused" )
	private List<SAGBatchUploadErrorDetails> updateSAGApplicationAndRsvp( String currentUserNric,
																		  String memberNric, String eventId, String financialYear,
																		  List<SAGApplication> applicationList,
																		  List<SAGRsvpTemplate> rsvpTemplateList,
																		  List<SAGEventRsvp> eventRsvpMasterList ) throws Exception
	{

		List<SAGBatchUploadErrorDetails> errorMessageObj = new ArrayList<SAGBatchUploadErrorDetails>();
		if ( null == applicationList || applicationList.isEmpty() || null == rsvpTemplateList || rsvpTemplateList.isEmpty() )
		{
			logger.log( Level.INFO, "Applicant ID No: " + memberNric + " - Applicant does not have any successful Scholastic Achievement Award / Study Award applications for year " + financialYear );
			SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
			errorMessageAttributes.setRowNumber(rsvpTemplateList.get(0).getRowNumber());
			errorMessageAttributes.setApplicantId(rsvpTemplateList.get(0).getApplicantId());
			errorMessageAttributes.setAttendeeId(rsvpTemplateList.get(0).getAttendeeId());
			errorMessageAttributes.setErrorMessage("Applicant does not have any successful Scholastic Achievement Award / Study Award applications for year " + financialYear);
			errorMessageObj.add(errorMessageAttributes);
			return errorMessageObj;
		}

		String seqNumberRef = getSeqNumberReference( applicationList );
		logger.log( Level.INFO, "seqNumberRef ---- " + seqNumberRef );

		// Get rsvpList (if present) wrt applicationList SeqNumberRef.
		List<SAGEventRsvp> eventRsvpList = getEventRsvpListBySeqReference( eventRsvpMasterList, seqNumberRef );

		if ( null == eventRsvpList )
		{
			eventRsvpList = new ArrayList<SAGEventRsvp>();
		}

		List<String> updateRsvpIdList = new ArrayList<String>();
		List<String> rsvpToDeleteList = new ArrayList<String>();
		List<SAGEventRsvp> newRsvpList = new ArrayList<SAGEventRsvp>();

		List<SAGRsvpTemplate> rsvpTemplateListOriginal = null;
		SAGBatchRsvpHelper rsvpHelper = new SAGBatchRsvpHelper();

		rsvpTemplateListOriginal = rsvpHelper.buildRsvpTemplate( currentUserNric, applicationList );
		int validCount = 0;
		boolean validateCheckAwardRecipient = false;
		for ( SAGRsvpTemplate each : rsvpTemplateList )
		{
			boolean validateApplicantID = false;


			boolean validationApplicant = true;
			boolean validationAttendee = true;
			boolean validationName = true;
			boolean validationAwardRecipient = true;
			boolean validationSerialNo = true;
			boolean validationRefNo = true;
			boolean validationRecipientName = true;
			boolean validationAttendance = true;
			boolean validationReason = true;
			boolean validationVegetarianMeal = true;

			String attendeeId = each.getAttendeeId();

			String applicantId = each.getApplicantId();

			if (!Objects.equals(applicantId, ""))
			{
				for (int applicantIdCount = 0; applicantIdCount < rsvpTemplateList.size(); applicantIdCount++)
				{
					if (rsvpTemplateList.get(applicantIdCount).getApplicantId().equals(applicantId))
					{
						validateApplicantID = true;
					}
				}
			}

			int recordMatchCount = 0;
			for (SAGRsvpTemplate eachOriginal : rsvpTemplateListOriginal)
			{
				//check for new inserted record if one record of applicant and attendee ID matches with the list that are generated.
				if (each.getApplicantId().equals(eachOriginal.getApplicantId()) && each.getAttendeeId().equals(eachOriginal.getAttendeeId()))
				{
					if (!each.getAttendeeIsRecipient().equalsIgnoreCase(eachOriginal.getAttendeeIsRecipient()))
					{
						validationAwardRecipient = false;
						if (eachOriginal.getAttendeeIsRecipient().equalsIgnoreCase(YES))
						{
							SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
							errorMessageAttributes.setRowNumber(each.getRowNumber());
							errorMessageAttributes.setApplicantId(applicantId);
							errorMessageAttributes.setAttendeeId(attendeeId);
							errorMessageAttributes.setErrorMessage("Only 'Y' is accepted for the field 'Is Attendee an Award Recipient?' when the attendee is the award recipient");
							errorMessageObj.add(errorMessageAttributes);
						}
						else
						{
							SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
							errorMessageAttributes.setRowNumber(each.getRowNumber());
							errorMessageAttributes.setApplicantId(applicantId);
							errorMessageAttributes.setAttendeeId(attendeeId);
							errorMessageAttributes.setErrorMessage("Only 'N' is accepted for the field 'Is Attendee an Award Recipient?' when the attendee is not the award recipient");
							errorMessageObj.add(errorMessageAttributes);
						}
					}
					else
					{
						// Check if all award recipient can't attend
						if (each.getAttendeeIsRecipient().equalsIgnoreCase(YES))
						{
							if(each.getAttendance().equalsIgnoreCase(YES))
							{
								validateCheckAwardRecipient = true;
							}
						}

						//check Amend Recipient's Name
						if (each.getRecipientAmendedName().length() > 0)
						{
							if (each.getRecipientAmendedName().length() > 100)
							{
								if (each.getApplicantId().equals(each.getAttendeeId()))
								{
									validationRecipientName = false;
									SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
									errorMessageAttributes.setRowNumber(each.getRowNumber());
									errorMessageAttributes.setApplicantId(applicantId);
									errorMessageAttributes.setAttendeeId(attendeeId);
									errorMessageAttributes.setErrorMessage("Only the award recipient would amend the name for the field 'Amend Recipient Name if applicable. Otherwise this field should be blank");
									errorMessageObj.add(errorMessageAttributes);
								}
								else
								{
									validationRecipientName = false;
									SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
									errorMessageAttributes.setRowNumber(each.getRowNumber());
									errorMessageAttributes.setApplicantId(applicantId);
									errorMessageAttributes.setAttendeeId(attendeeId);
									errorMessageAttributes.setErrorMessage(maxCharactersPrefix + "100" + maxCharactersSuffix +  "'Amend Recipient Name'");
									errorMessageObj.add(errorMessageAttributes);
								}
							}
							else
							{
								if (!each.getAttendeeIsRecipient().equalsIgnoreCase(YES))
								{
									validationRecipientName = false;
									SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
									errorMessageAttributes.setRowNumber(each.getRowNumber());
									errorMessageAttributes.setApplicantId(applicantId);
									errorMessageAttributes.setAttendeeId(attendeeId);
									errorMessageAttributes.setErrorMessage("Only the award recipient would amend the name for the field 'Amend Recipient Name if applicable. Otherwise this field should be blank");
									errorMessageObj.add(errorMessageAttributes);
								}
								else
								{
									Pattern pattern = Pattern.compile(regexName);
									if (!pattern.matcher(each.getRecipientAmendedName()).matches())
									{
										validationRecipientName = false;
										SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
										errorMessageAttributes.setRowNumber(each.getRowNumber());
										errorMessageAttributes.setApplicantId(applicantId);
										errorMessageAttributes.setAttendeeId(attendeeId);
										errorMessageAttributes.setErrorMessage(alphaCommaSpaceMsg + "'Amend Recipient Name'");
										errorMessageObj.add(errorMessageAttributes);
									}
								}
							}
						}
					}

					//check for name consistency
					if (!each.getAttendeeName().equals(eachOriginal.getAttendeeName()))
					{
						validationName = false;
						SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
						errorMessageAttributes.setRowNumber(each.getRowNumber());
						errorMessageAttributes.setApplicantId(applicantId);
						errorMessageAttributes.setAttendeeId(attendeeId);
						errorMessageAttributes.setErrorMessage("Attendee name is incorrect for the field 'Attendee Name'");
						errorMessageObj.add(errorMessageAttributes);
					}

					//check for serial no. consistency
					if (!each.getRecipientSequenceNumber().equals(eachOriginal.getRecipientSequenceNumber()))
					{
						if (eachOriginal.getAttendeeIsRecipient().equalsIgnoreCase(YES))
						{
							validationSerialNo = false;
							SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
							errorMessageAttributes.setRowNumber(each.getRowNumber());
							errorMessageAttributes.setApplicantId(applicantId);
							errorMessageAttributes.setAttendeeId(attendeeId);
							errorMessageAttributes.setErrorMessage("Serial no. is incorrect for the field 'Recipient's Serial No.'");
							errorMessageObj.add(errorMessageAttributes);
						}
						else
						{
							if (each.getRecipientSequenceNumber().length() > 0)
							{
								String recipientSequenceNumber = each.getRecipientSequenceNumber().replaceAll("\\s", "");
								if (!recipientSequenceNumber.equals(EMPTY))
								{
									if (!each.getRecipientSequenceNumber().equals(NO_CONTENT))
									{
										validationSerialNo = false;
										SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
										errorMessageAttributes.setRowNumber(each.getRowNumber());
										errorMessageAttributes.setApplicantId(applicantId);
										errorMessageAttributes.setAttendeeId(attendeeId);
										errorMessageAttributes.setErrorMessage("Only the award recipient would have a serial no. for field 'Recipient's Serial No.'. Otherwise this field should be blank.");
										errorMessageObj.add(errorMessageAttributes);
									}
								}
							}
						}
					}

					//check for reference no. consistency
					if (!each.getReferenceNumber().equals(eachOriginal.getReferenceNumber()))
					{
						if (eachOriginal.getAttendeeIsRecipient().equalsIgnoreCase(YES))
						{
							validationRefNo = false;
							SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
							errorMessageAttributes.setRowNumber(each.getRowNumber());
							errorMessageAttributes.setApplicantId(applicantId);
							errorMessageAttributes.setAttendeeId(attendeeId);
							errorMessageAttributes.setErrorMessage("Application reference no. is incorrect for the field 'Recipient's Application Reference No.'");
							errorMessageObj.add(errorMessageAttributes);
						}
						else
						{
							if (each.getReferenceNumber().length() > 0)
							{
								String referenceNumber = each.getReferenceNumber().replaceAll("\\s", "");
								if (!referenceNumber.equals(EMPTY))
								{
									if (!each.getReferenceNumber().equals(NO_CONTENT))
									{
										validationRefNo = false;
										SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
										errorMessageAttributes.setRowNumber(each.getRowNumber());
										errorMessageAttributes.setApplicantId(applicantId);
										errorMessageAttributes.setAttendeeId(attendeeId);
										errorMessageAttributes.setErrorMessage("Only the award recipient would have an application reference no. for field 'Recipient's Application Reference No.'. Otherwise this field should be blank.");
										errorMessageObj.add(errorMessageAttributes);
									}
								}
							}
						}
					}

					recordMatchCount++;
				}
			}

			//Manually insert record validation
			if (recordMatchCount == 0)
			{
				// check for attendance is recipient to accept only NO
				if (!each.getAttendeeIsRecipient().equalsIgnoreCase(NO))
				{
					validationAwardRecipient = false;
					SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
					errorMessageAttributes.setRowNumber(each.getRowNumber());
					errorMessageAttributes.setApplicantId(applicantId);
					errorMessageAttributes.setAttendeeId(attendeeId);
					errorMessageAttributes.setErrorMessage("Only 'N' is accepted for the field 'Is Attendee an Award Recipient?' when the attendee is not the award recipient");
					errorMessageObj.add(errorMessageAttributes);
				}

				// check for serial number to only accept hyphen and blank
				if (each.getRecipientSequenceNumber().length() > 0)
				{
					String recipientSequenceNumber = each.getRecipientSequenceNumber().replaceAll("\\s", "");
					if (!recipientSequenceNumber.equals(EMPTY))
					{
						if (!each.getRecipientSequenceNumber().equals(NO_CONTENT))
						{
							validationSerialNo = false;
							SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
							errorMessageAttributes.setRowNumber(each.getRowNumber());
							errorMessageAttributes.setApplicantId(applicantId);
							errorMessageAttributes.setAttendeeId(attendeeId);
							errorMessageAttributes.setErrorMessage("Only the award recipient would have a serial no. for field 'Recipient's Serial No.'. Otherwise this field should be blank");
							errorMessageObj.add(errorMessageAttributes);
						}
					}
				}

				// check for reference number to only accept hyphen and blank
				if (each.getReferenceNumber().length() > 0)
				{
					String referenceNumber = each.getReferenceNumber().replaceAll("\\s", "");
					if (!referenceNumber.equals(EMPTY))
					{
						if (!each.getReferenceNumber().equals(NO_CONTENT))
						{
							validationRefNo = false;
							SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
							errorMessageAttributes.setRowNumber(each.getRowNumber());
							errorMessageAttributes.setApplicantId(applicantId);
							errorMessageAttributes.setAttendeeId(attendeeId);
							errorMessageAttributes.setErrorMessage("Only the award recipient would have an application reference no. for field ‘Recipient's Application Reference No.’. Otherwise this field should be blank");
							errorMessageObj.add(errorMessageAttributes);
						}
					}
				}

				if (each.getRecipientAmendedName().length() > 0)
				{
					validationRecipientName = false;
					SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
					errorMessageAttributes.setRowNumber(each.getRowNumber());
					errorMessageAttributes.setApplicantId(applicantId);
					errorMessageAttributes.setAttendeeId(attendeeId);
					errorMessageAttributes.setErrorMessage("Only the award recipient would amend the name for field 'Amend Recipient Name' if applicable. Otherwise this field should be blank");
					errorMessageObj.add(errorMessageAttributes);
				}
			}

			// Validate Attendee Nric
			if (each.getAttendeeId().length() > 0)
			{
				if (each.getAttendeeId().length() > 50)
				{
					validationAttendee = false;
					SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
					errorMessageAttributes.setRowNumber(each.getRowNumber());
					errorMessageAttributes.setApplicantId(applicantId);
					errorMessageAttributes.setAttendeeId(attendeeId);
					errorMessageAttributes.setErrorMessage(maxCharactersPrefix + "50" + maxCharactersSuffix + "'Attendee's NRIC'");
					errorMessageObj.add(errorMessageAttributes);
				}
				else
				{
					Pattern pattern = Pattern.compile(regexNricPassportOthers);
					if (!pattern.matcher(each.getAttendeeId()).matches())
					{
						validationAttendee = false;
						SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
						errorMessageAttributes.setRowNumber(each.getRowNumber());
						errorMessageAttributes.setApplicantId(applicantId);
						errorMessageAttributes.setAttendeeId(attendeeId);
						errorMessageAttributes.setErrorMessage("Only alphanumeric, forward slash and hyphen are allowed for the field 'Attendee's NRIC'");
						errorMessageObj.add(errorMessageAttributes);
					}
				}
			}
			else
			{
				validationAttendee = false;
				SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
				errorMessageAttributes.setRowNumber(each.getRowNumber());
				errorMessageAttributes.setApplicantId(applicantId);
				errorMessageAttributes.setAttendeeId(attendeeId);
				errorMessageAttributes.setErrorMessage("Field 'Attendee NRIC' cannot be blank");
				errorMessageObj.add(errorMessageAttributes);
			}

			// validate Attendee Name
			if (each.getAttendeeName().length() > 0)
			{
				if (each.getAttendeeName().length() > 100)
				{
					validationName = false;
					SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
					errorMessageAttributes.setRowNumber(each.getRowNumber());
					errorMessageAttributes.setApplicantId(applicantId);
					errorMessageAttributes.setAttendeeId(attendeeId);
					errorMessageAttributes.setErrorMessage(maxCharactersPrefix + "100" + maxCharactersSuffix + "'Attendee Name'");
					errorMessageObj.add(errorMessageAttributes);
				}
				else
				{
					Pattern pattern = Pattern.compile(regexName);
					if (!pattern.matcher(each.getAttendeeName()).matches())
					{
						validationName = false;
						SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
						errorMessageAttributes.setRowNumber(each.getRowNumber());
						errorMessageAttributes.setApplicantId(applicantId);
						errorMessageAttributes.setAttendeeId(attendeeId);
						errorMessageAttributes.setErrorMessage(alphaCommaSpaceMsg + "'Attendee Name'");
						errorMessageObj.add(errorMessageAttributes);
					}
				}
			}
			else
			{
				validationName = false;
				SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
				errorMessageAttributes.setRowNumber(each.getRowNumber());
				errorMessageAttributes.setApplicantId(applicantId);
				errorMessageAttributes.setAttendeeId(attendeeId);
				errorMessageAttributes.setErrorMessage("Field 'Attendee Name' cannot be blank");
				errorMessageObj.add(errorMessageAttributes);
			}

			// Attendance Column validation
			if (each.getAttendance().length() > 0)
			{
				if (each.getAttendance().length() > 5)
				{
					validationAttendance = false;
					SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
					errorMessageAttributes.setRowNumber(each.getRowNumber());
					errorMessageAttributes.setApplicantId(applicantId);
					errorMessageAttributes.setAttendeeId(attendeeId);
					errorMessageAttributes.setErrorMessage("Only 'Y' or 'N' is accepted for the field 'Attendance'");
					errorMessageObj.add(errorMessageAttributes);
				}
				else
				{
					if (!(each.getAttendance().equalsIgnoreCase( YES ) || each.getAttendance().equalsIgnoreCase( NO )))
					{
						validationAttendance = false;
						SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
						errorMessageAttributes.setRowNumber(each.getRowNumber());
						errorMessageAttributes.setApplicantId(applicantId);
						errorMessageAttributes.setAttendeeId(attendeeId);
						errorMessageAttributes.setErrorMessage("Only 'Y' or 'N' is accepted for the field 'Attendance'");
						errorMessageObj.add(errorMessageAttributes);
					}
				}
			}
			else
			{
				validationAttendance = false;
				SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
				errorMessageAttributes.setRowNumber(each.getRowNumber());
				errorMessageAttributes.setApplicantId(applicantId);
				errorMessageAttributes.setAttendeeId(attendeeId);
				errorMessageAttributes.setErrorMessage("Field 'Attendance' cannot be blank");
				errorMessageObj.add(errorMessageAttributes);
			}

			// Reason for Not Attending column validation
			if ((each.getAttendeeIsRecipient().equalsIgnoreCase(NO) && (each.getApplicantId().equalsIgnoreCase(each.getAttendeeId()))) || each.getAttendeeIsRecipient().equalsIgnoreCase(YES))
			{
				if (each.getAttendance().equalsIgnoreCase(NO) && each.getReasonForAbsence().isEmpty())
				{
					validationReason = false;
					SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
					errorMessageAttributes.setRowNumber(each.getRowNumber());
					errorMessageAttributes.setApplicantId(applicantId);
					errorMessageAttributes.setAttendeeId(attendeeId);
					errorMessageAttributes.setErrorMessage("Field 'Reason for not Attending' is required when attendee is the applicant / award recipient and not attending the ceremony");
					errorMessageObj.add(errorMessageAttributes);
				}
				else
				{
					if (each.getAttendance().equalsIgnoreCase(YES))
					{
						if (each.getReasonForAbsence().length() > 255)
						{
							validationReason = false;
							SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
							errorMessageAttributes.setRowNumber(each.getRowNumber());
							errorMessageAttributes.setApplicantId(applicantId);
							errorMessageAttributes.setAttendeeId(attendeeId);
							errorMessageAttributes.setErrorMessage(maxCharactersPrefix + "255" + maxCharactersSuffix + "'Reason for Not Attending'");
							errorMessageObj.add(errorMessageAttributes);
						}
					}

					if (each.getAttendance().equalsIgnoreCase(NO))
					{
						if (each.getReasonForAbsence().length() > 255)
						{
							validationReason = false;
							SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
							errorMessageAttributes.setRowNumber(each.getRowNumber());
							errorMessageAttributes.setApplicantId(applicantId);
							errorMessageAttributes.setAttendeeId(attendeeId);
							errorMessageAttributes.setErrorMessage(maxCharactersPrefix + "255" + maxCharactersSuffix + "'Reason for Not Attending'");
							errorMessageObj.add(errorMessageAttributes);
						}
					}
				}
			}
			else
			{
				if (each.getAttendance().equalsIgnoreCase(NO))
				{
					if (each.getReasonForAbsence().length() > 255)
					{
						validationReason = false;
						SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
						errorMessageAttributes.setRowNumber(each.getRowNumber());
						errorMessageAttributes.setApplicantId(applicantId);
						errorMessageAttributes.setAttendeeId(attendeeId);
						errorMessageAttributes.setErrorMessage(maxCharactersPrefix + "255" + maxCharactersSuffix + "'Reason for Not Attending'");
						errorMessageObj.add(errorMessageAttributes);
					}
				}
			}


			if (each.getAttendeeIsRecipient().equalsIgnoreCase(YES) && each.getAttendance().equalsIgnoreCase(YES) && each.getReasonForAbsence().length() > 0)
			{
				each.setReasonForAbsence(EMPTY);
			}

			// Vegetarian Meal Required Column validation
			if (each.getVegMealRequired().length() > 0)
			{
				if (each.getAttendance().equalsIgnoreCase(YES) || each.getAttendance().equalsIgnoreCase(NO))
				{
					if (each.getAttendance().equalsIgnoreCase(YES))
					{
						if ( !(each.getVegMealRequired().equalsIgnoreCase( YES ) || each.getVegMealRequired().equalsIgnoreCase( NO )))
						{
							validationVegetarianMeal = false;
							SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
							errorMessageAttributes.setRowNumber(each.getRowNumber());
							errorMessageAttributes.setApplicantId(applicantId);
							errorMessageAttributes.setAttendeeId(attendeeId);
							errorMessageAttributes.setErrorMessage("Only 'Y' or 'N' is accepted for the field 'Vegetarian Meal Required?'");
							errorMessageObj.add(errorMessageAttributes);
						}
					}
					else
					{
						if (!each.getVegMealRequired().equalsIgnoreCase(NO))
						{
							validationVegetarianMeal = false;
							SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
							errorMessageAttributes.setRowNumber(each.getRowNumber());
							errorMessageAttributes.setApplicantId(applicantId);
							errorMessageAttributes.setAttendeeId(attendeeId);
							errorMessageAttributes.setErrorMessage("Only 'N' is accepted for field 'Vegetarian Meal Required?' when field 'Attendance' is 'N'");
							errorMessageObj.add(errorMessageAttributes);
						}
					}
				}
			}
			else
			{
				validationVegetarianMeal = false;
				SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
				errorMessageAttributes.setRowNumber(each.getRowNumber());
				errorMessageAttributes.setApplicantId(applicantId);
				errorMessageAttributes.setAttendeeId(attendeeId);
				errorMessageAttributes.setErrorMessage("Field 'Vegetarian Meal Required?' cannot be blank");
				errorMessageObj.add(errorMessageAttributes);
			}

			if (validationApplicant && validationAttendee && validationName && validationAwardRecipient && validationSerialNo && validationRefNo && validationRecipientName && validationAttendance && validationReason && validationVegetarianMeal)
			{
				validCount++;
			}
		}

		if (validateCheckAwardRecipient)
		{
			int validateCheckFamilyMemberCount = 0;
			for ( SAGRsvpTemplate each : rsvpTemplateList )
			{
				if (each.getAttendeeIsRecipient().equalsIgnoreCase(NO) && each.getAttendance().equalsIgnoreCase(YES))
				{
					validateCheckFamilyMemberCount++;
				}
			}

			if (validateCheckFamilyMemberCount > 2)
			{
				for ( SAGRsvpTemplate each : rsvpTemplateList )
				{
					if (each.getAttendeeIsRecipient().equalsIgnoreCase(NO) && each.getAttendance().equalsIgnoreCase(YES))
					{
						SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
						errorMessageAttributes.setRowNumber(each.getRowNumber());
						errorMessageAttributes.setApplicantId(each.getApplicantId());
						errorMessageAttributes.setAttendeeId(each.getAttendeeId());
						errorMessageAttributes.setErrorMessage("Only 2 family members (including applicant) are invited to the ceremony");
						errorMessageObj.add(errorMessageAttributes);
					}
				}
				validCount = 0;
			}
		}
		else
		{
			// check if applicant and all attendee and family member can't attend
			int validateCheckAttendance = 0;
			for ( SAGRsvpTemplate each : rsvpTemplateList )
			{
				if (each.getAttendance().equalsIgnoreCase(NO))
				{
					validateCheckAttendance++;
				}
			}

			if (!(validateCheckAttendance == rsvpTemplateList.size()))
			{
				for ( SAGRsvpTemplate each : rsvpTemplateList )
				{
					if (each.getAttendeeIsRecipient().equalsIgnoreCase(NO))
					{
						if (each.getApplicantId().equals(each.getAttendeeId()))
						{
							if (each.getAttendance().equalsIgnoreCase(YES))
							{
								SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
								errorMessageAttributes.setRowNumber(each.getRowNumber());
								errorMessageAttributes.setApplicantId(each.getApplicantId());
								errorMessageAttributes.setAttendeeId(each.getAttendeeId());
								errorMessageAttributes.setErrorMessage("Applicant must be accompanied by award recipient to attend the ceremony");
								errorMessageObj.add(errorMessageAttributes);
							}
						}
						else
						{
							if (each.getAttendance().equalsIgnoreCase(YES))
							{
								SAGBatchUploadErrorDetails errorMessageAttributes = new SAGBatchUploadErrorDetails();
								errorMessageAttributes.setRowNumber(each.getRowNumber());
								errorMessageAttributes.setApplicantId(each.getApplicantId());
								errorMessageAttributes.setAttendeeId(each.getAttendeeId());
								errorMessageAttributes.setErrorMessage("Family member must be accompanied by award recipient to attend the ceremony");
								errorMessageObj.add(errorMessageAttributes);
							}
						}
					}
				}
				validCount = 0;
			}
		}

		if (validCount == rsvpTemplateList.size())
		{
			for ( SAGRsvpTemplate toBeSave : rsvpTemplateList )
			{
				int index = rsvpTemplateList.indexOf( toBeSave );
				boolean isRsvpUpdated = false;
				boolean isApplicant = false;
				boolean isRsvp = false;

				boolean isRecipient = ( toBeSave.getAttendeeIsRecipient().equalsIgnoreCase( YES ) ? true : false );
				isApplicant = ( toBeSave.getAttendeeId().equalsIgnoreCase( toBeSave.getApplicantId() ) ? true : false );

				for ( SAGEventRsvp eachRsvp : eventRsvpList )
				{
					// set rsvp
					if (toBeSave.getAttendance().equalsIgnoreCase( YES ))
					{
						isRsvp = true;
					}
					else
					{
						isRsvp = false;
					}

					if ( toBeSave.getAttendeeId().equals( eachRsvp.getAttendeeId() )  )
					{
						// update rsvp object -
						// Iff is Applicant OR isRecipient OR is Family Member
						// attending. else skip.
						updateRsvpIdList.add( eachRsvp.getId() );
						updateRsvpObject( currentUserNric, toBeSave.getApplicantId(), eachRsvp, toBeSave, financialYear, eventId, seqNumberRef, RSVP_SUBMITTED, index + 1 );
						isRsvpUpdated = true;
						break;
					}
					else
					{
						isRsvpUpdated = false;
					}
				}

				// if not existing RSVP. add to save new.
				if ( !isRsvpUpdated )
				{
					// Add new rsvp Object
					// Iff is Applicant OR isRecipient OR is Family Member
					// attending. else skip.
					SAGEventRsvp eventRsvp = new SAGEventRsvp();
					updateRsvpObject( currentUserNric, toBeSave.getApplicantId(), eventRsvp, toBeSave, financialYear, eventId, seqNumberRef, RSVP_SUBMITTED, index + 1 );
					newRsvpList.add( eventRsvp );
				}

				if ( isRecipient )
				{
					// update sagApplication for ChildNameAmend. Add to sagApplicationsToUpdateForChildNameAmend.
					updateSAGApplicationForChildNameAmend( isRecipient, toBeSave, applicationList );
				}
			}
		}

		// Create the list of rsvp ids to delete.
		if ( null != updateRsvpIdList && !updateRsvpIdList.isEmpty() )
		{
			Iterator<SAGEventRsvp> itr = eventRsvpList.iterator();
			while ( itr.hasNext() )
			{
				SAGEventRsvp eventRsvp = (SAGEventRsvp) itr.next();
				if ( null != eventRsvp.getId() && !eventRsvp.getId().isEmpty() && !updateRsvpIdList.contains( eventRsvp.getId() ) )
				{
					rsvpToDeleteList.add( eventRsvp.getId() );
					itr.remove();
				}
			}
		}

		// Append the new rsvp List.
		eventRsvpList.addAll( newRsvpList );

		logger.info( String.format("eventRsvpList to update (size): %s" , eventRsvpList.size() ));
		logger.info( String.format( "rsvp(s) to delete (size): %s" , rsvpToDeleteList.size() ));
		this.eventRsvpListToSaveUpdate.addAll( eventRsvpList );
		this.eventRsvpIdsToDelete.addAll( rsvpToDeleteList );

		logger.log( Level.INFO, "just before return: " + errorMessageObj);
		return errorMessageObj;
	}

	private SAGEventRsvp updateRsvpObject( String currentUserNric,
										   String applicantNric, SAGEventRsvp eventRsvp,
										   SAGRsvpTemplate rsvpResponseTemplate, String financialYear,
										   String eventId, String seqNumberRef, String rsvpStatus,
										   Integer order ) {

		if ( null != eventRsvp && null != rsvpResponseTemplate ) {
			eventRsvp.setAttendeeId( rsvpResponseTemplate.getAttendeeId() );

			eventRsvp.setIsAttendeeApplicant( ( rsvpResponseTemplate
					.getApplicantId().equalsIgnoreCase(
							rsvpResponseTemplate.getAttendeeId() ) ? true
					: false ) );

			eventRsvp.setIsAttendeeChild( rsvpResponseTemplate
					.getAttendeeIsRecipient() != null
					&& rsvpResponseTemplate.getAttendeeIsRecipient()
					.equalsIgnoreCase( YES ) ? true : false );

			String attendeeName = rsvpResponseTemplate.getAttendeeName();
			if ( eventRsvp.getIsAttendeeChild() ) {
				// update the name with amended Name (if Present) for the
				// Recipient.
				attendeeName = rsvpResponseTemplate.getRecipientAmendedName() != null
						&& !rsvpResponseTemplate.getRecipientAmendedName()
						.isEmpty() ? rsvpResponseTemplate
						.getRecipientAmendedName() : rsvpResponseTemplate
						.getAttendeeName();
			}
			eventRsvp.setAttendeeName( attendeeName );

			eventRsvp.setRsvp( rsvpResponseTemplate.getAttendance() != null
					&& rsvpResponseTemplate.getAttendance().equalsIgnoreCase(
					YES ) ? true : false );

			eventRsvp.setIsVegetarian( rsvpResponseTemplate
					.getVegMealRequired() != null
					&& rsvpResponseTemplate.getVegMealRequired()
					.equalsIgnoreCase( YES ) ? true : false );

			//Default to blank
			eventRsvp.setReasonForAbsence( "" );
			if ( !eventRsvp.getRsvp() ) {
				eventRsvp.setReasonForAbsence( rsvpResponseTemplate
						.getReasonForAbsence() );
			}

			// Set Order.
			eventRsvp.setOrder( order );

			eventRsvp.setFinancialYear( financialYear );
			eventRsvp.setEventId( eventId );
			eventRsvp.setSequenceNumberReference( seqNumberRef );

			eventRsvp.setRsvpSubmissionStatus( rsvpStatus );
			eventRsvp.setRsvpSubmittedBy( currentUserNric );
			eventRsvp.setDateOfRsvpSubmission( new Date() );
		}

		return eventRsvp;
	}

	private List<SAGEventRsvp> getEventRsvpListBySeqReference(
			List<SAGEventRsvp> eventRsvpMasterList, String sequenceNumberRef ) {
		List<SAGEventRsvp> eventRsvpList = new ArrayList<SAGEventRsvp>();

		if ( null != eventRsvpMasterList && null != sequenceNumberRef
				&& !eventRsvpMasterList.isEmpty()
				&& !sequenceNumberRef.isEmpty() ) {
			for ( SAGEventRsvp eachRsvp : eventRsvpMasterList ) {
				if ( sequenceNumberRef.equals( eachRsvp
						.getSequenceNumberReference() ) ) {
					eventRsvpList.add( eachRsvp );
				}
			}
		}

		return eventRsvpList;
	}

	private void updateSAGApplicationForChildNameAmend( boolean isRecipient,
														SAGRsvpTemplate recipientRsvpResponse,
														List<SAGApplication> applicationList ) {

		if ( null != recipientRsvpResponse && null != applicationList
				&& !applicationList.isEmpty() ) {
			String referenceNumber = recipientRsvpResponse.getReferenceNumber();
			String amendName = recipientRsvpResponse.getRecipientAmendedName();

			if ( null != referenceNumber && null != amendName
					&& !amendName.isEmpty() ) {
				for ( SAGApplication eachApp : applicationList ) {
					if ( referenceNumber.equalsIgnoreCase( eachApp
							.getReferenceNumber() ) ) {
						// update amendName.
						eachApp.setChildNameAmended( amendName );
						eachApp.setChildName( amendName );
						this.sagApplicationsToUpdateForChildNameAmend
								.add( eachApp );
					}
				}
			}
		}
	}
}
