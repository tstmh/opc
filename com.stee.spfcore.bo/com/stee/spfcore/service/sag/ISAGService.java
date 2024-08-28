package com.stee.spfcore.service.sag;

import java.util.List;
import java.util.Map;

import com.stee.spfcore.model.sag.SAGAnnouncementConfig;
import com.stee.spfcore.model.sag.SAGApplication;
import com.stee.spfcore.model.sag.SAGAwardQuantum;
import com.stee.spfcore.model.sag.SAGBatchFileRecord;
import com.stee.spfcore.model.sag.SAGConfigSetup;
import com.stee.spfcore.model.sag.SAGDateConfigType;
import com.stee.spfcore.model.sag.SAGDonation;
import com.stee.spfcore.model.sag.SAGEventDetail;
import com.stee.spfcore.model.sag.SAGEventRsvp;
import com.stee.spfcore.model.sag.SAGPrivileges;
import com.stee.spfcore.model.sag.SAGTask;
import com.stee.spfcore.model.sag.inputConfig.SAGInputType;
import com.stee.spfcore.model.sag.inputConfig.SAGInputs;
import com.stee.spfcore.model.sag.inputConfig.SAGSubInputs;
import com.stee.spfcore.security.AccessDeniedException;
import com.stee.spfcore.vo.sag.SAGApplicationApprovalsCriteria;
import com.stee.spfcore.vo.sag.SAGApplicationChildDetail;
import com.stee.spfcore.vo.sag.SAGApplicationCriteria;
import com.stee.spfcore.vo.sag.SAGApplicationDetailByApprovals;
import com.stee.spfcore.vo.sag.SAGApplicationResult;
import com.stee.spfcore.vo.sag.SAGApplicationSelectNricList;
import com.stee.spfcore.vo.sag.SAGApplicationsApprovedForAward;
import com.stee.spfcore.vo.sag.SAGApplicationsApprovedForAwardWithPayment;
import com.stee.spfcore.vo.sag.SAGApplicationsCountDetail;
import com.stee.spfcore.vo.sag.SAGApplicationsForAudit;
import com.stee.spfcore.vo.sag.SAGAwardQuantumDescription;
import com.stee.spfcore.vo.sag.SAGEmailContentDetails;
import com.stee.spfcore.vo.sag.SAGInputListIdsDescriptionDetail;
import com.stee.spfcore.vo.sag.SAGPrivilegePersonalDetails;
import com.stee.spfcore.vo.sag.SAGPrivilegeUserDetail;
import com.stee.spfcore.vo.sag.SAGStatusApprovalRecordDetail;

public interface ISAGService {

	/**
	 * Search for saved SAG Application by member nric.
	 * 
	 * @param nric
	 * @return
	 * @throws SAGServiceException
	 * @throws AccessDeniedException
	 */
	public List<SAGApplication> searchSAGApplication( String nric )
			throws SAGServiceException, AccessDeniedException;

	/**
	 * Search for saved SAG Applications by member nric and awardType.
	 * 
	 * @param nric
	 * @param awardType
	 * @return
	 * @throws SAGServiceException
	 * @throws AccessDeniedException
	 */
	public List<SAGApplication> searchSAGApplication( String nric,
			String childNric, String awardType, String financialYear )
			throws SAGServiceException, AccessDeniedException;
	
	public List<SAGApplication> searchSimliarSAGApplication( String childNric, String financialYear, String referenceNumber)
			throws SAGServiceException, AccessDeniedException;

	public List<SAGApplication> searchSAGApplication( String nric,
			String childNric, String awardType, String financialYear,
			boolean isOrderAsc ) throws SAGServiceException,
			AccessDeniedException;

	public List<SAGApplication> searchSAGApplicationsBySubmission( String nric,
			String financialYear ) throws SAGServiceException;

	/**
	 * Get specific SAG Application by Application Reference Number
	 * 
	 * @param referenceNumber
	 * @return
	 * @throws SAGServiceException
	 * @throws AccessDeniedException
	 */
	public SAGApplication getSAGApplication( String referenceNumber )
			throws SAGServiceException, AccessDeniedException;

	/**
	 * Save SAG Application (Add/Update)
	 * 
	 * @param sagApplication
	 * @param requestor
	 * @throws SAGServiceException
	 * @throws AccessDeniedException
	 */
	public void saveSAGApplication( SAGApplication sagApplication,
			String requestor ) throws SAGServiceException,
			AccessDeniedException;

	/**
	 * Save (Add/Update) List of SAG Applications.
	 * 
	 * @param sagApplicationList
	 * @param requestor
	 * @throws SAGServiceException
	 * @throws AccessDeniedException
	 */
	public void saveSAGApplicationList(
			List<SAGApplication> sagApplicationList, String requestor )
			throws SAGServiceException, AccessDeniedException;

	/**
	 * Batch Update SAG Applications. Use at INTRANET only
	 * 
	 * @param sagApplicationList
	 * @param requestor
	 * @throws SAGServiceException
	 * @throws AccessDeniedException
	 */
	public void batchUpdateSAGApplications(
			List<SAGApplication> sagApplicationList, String requestor )
			throws SAGServiceException, AccessDeniedException;

	/**
	 * @param sagApplicationCriteria
	 * @return
	 */
	public List<SAGApplicationResult> searchSAGApplicationByCriteria(
			SAGApplicationCriteria sagApplicationCriteria,
			boolean isOrderByReferenceNumber ) throws SAGServiceException;

	/**
	 * Get Input List (DropDown) by awardType
	 * 
	 * @param awardType
	 * @return
	 * @throws SAGServiceException
	 */
	public List<SAGInputs> getListOfSAGInputs( String awardType )
			throws SAGServiceException;

	/**
	 * Get Input List (DropDown) by awardType and inputType
	 * 
	 * @param awardType
	 * @param inputType
	 * @return
	 * @throws SAGServiceException
	 */
	public List<SAGInputs> getListOfSAGInputByType( String awardType,
			SAGInputType inputType ) throws SAGServiceException;

	/**
	 * Get SAGInput
	 * 
	 * @param awardType
	 * @param inputType
	 * @param inputId
	 * @return
	 * @throws SAGServiceException
	 */
	public SAGInputs getSAGInput( String awardType, SAGInputType inputType,
			String inputId ) throws SAGServiceException;

	/**
	 * Get Sub Input List Dependent on Parent selection (DropDown)
	 * 
	 * @param awardType
	 * @param parentId
	 * @param parentType
	 * @return
	 * @throws SAGServiceException
	 */
	public List<SAGSubInputs> getSubInputListByCriteria( String awardType,
			String parentId, SAGInputType parentType )
			throws SAGServiceException;

	public SAGConfigSetup getConfigSetup( String id )
			throws SAGServiceException;

	public SAGConfigSetup getConfigSetup( String financialYear,
			SAGDateConfigType configType ) throws SAGServiceException;

	public List<SAGConfigSetup> searchSAGConfigSetup( String financialYear )
			throws SAGServiceException;

	public void saveConfigSetup( SAGConfigSetup sagConfigSetup, String requestor )
			throws SAGServiceException;

	public SAGPrivileges getPrivilege( String id ) throws SAGServiceException;

	public List<SAGPrivileges> searchPrivileges( String financialYear,
			String memberNric ) throws SAGServiceException;

	public SAGPrivilegeUserDetail getPrivilegeUserDetail( String id )
			throws SAGServiceException;

	public List<SAGPrivilegeUserDetail> searchPrivilegeUserDetail(
			String financialYear, String memberNric )
			throws SAGServiceException;

	public void savePrivileges( SAGPrivileges sagPrivilege, String requestor )
			throws SAGServiceException;

	public void deletePrivileges( List<String> privilegesIdList,
			String requestor ) throws SAGServiceException;

	public SAGAwardQuantum getAwardQuantum( String id )
			throws SAGServiceException;

	public List<SAGAwardQuantum> searchAwardQuantum( String financialYear,
			String awardType, String subType ) throws SAGServiceException;

	public void saveAwardQuantum( SAGAwardQuantum sagAwardQuantum,
			String requestor ) throws SAGServiceException;

	public SAGEventDetail getEventDetails( String eventId )
			throws SAGServiceException;

	public List<SAGEventDetail> searchEventDetails( String eventId,
			String financialYear ) throws SAGServiceException;

	public void saveEventDetail( SAGEventDetail sagEventDetail, String requestor )
			throws SAGServiceException;

	public SAGEventRsvp getEventRsvp( String id ) throws SAGServiceException;

	public List<SAGEventRsvp> searchEventRsvp( String financialYear,
			String eventId, String seqNumberReference, String attendeeName,
			String attendeeId ) throws SAGServiceException;

	public void saveEventRsvp( SAGEventRsvp sagEventRsvp, String requestor )
			throws SAGServiceException;

	public void deleteEventRsvpList( List<String> rsvpIdList, String requestor )
			throws SAGServiceException;

	// Service to store List of records
	public void saveConfigSetupList( List<SAGConfigSetup> configSetupList,
			String requestor ) throws SAGServiceException;

	public void savePrivilegesList( List<SAGPrivileges> privilegesList,
			String requestor ) throws SAGServiceException;

	public void saveAwardQuantumList( List<SAGAwardQuantum> awardQuantumList,
			String requestor ) throws SAGServiceException;

	public void saveEventRsvpList( List<SAGEventRsvp> eventRsvpList,
			String requestor ) throws SAGServiceException;

	// SAGDonations
	public SAGDonation getDonation( String id ) throws SAGServiceException;

	public List<SAGDonation> searchDonations( String financialYear,
			String organization ) throws SAGServiceException;

	public void saveDonation( SAGDonation sagDonation, String requestor )
			throws SAGServiceException;

	public void deleteDonations( List<String> donationsIdList, String requestor )
			throws SAGServiceException;

	public void saveDonationsList( List<SAGDonation> sagDonationList,
			String requestor ) throws SAGServiceException;

	public List<SAGApplicationChildDetail> searchSAGChildDetails(
			String financialYear ) throws SAGServiceException;

	public void updateApplicationDetailsAfterRSVP(
			List<SAGApplication> sagApplicationsToUpdate, String requestor )
			throws SAGServiceException;

	public List<SAGPrivilegePersonalDetails> searchPersonalForManagePrivileges(
			String name, String nric, String departmentId )
			throws SAGServiceException;

	public List<SAGApplicationDetailByApprovals> searchSAGApplicationsByApprovalCriteria(
			SAGApplicationApprovalsCriteria criteria )
			throws SAGServiceException;

	public List<SAGApplicationsApprovedForAward> searchSAGApplicationsApprovedForAward(
			SAGApplicationCriteria criteria ) throws SAGServiceException;
	
	public List<SAGApplicationsApprovedForAwardWithPayment> searchSAGApplicationsApprovedForAwardWithPayment(
			SAGApplicationCriteria criteria, String paymentMode, List<String> paymentStatusList  ) throws SAGServiceException;

	public List<SAGApplicationsForAudit> searchSAGApplicationsForAudit(
			String financialYear, String applicationStatus )
			throws SAGServiceException;

    public void updateChequeValueDateForSAGApplications(
			List<String> referenceNumberList,
			List<SAGApplicationsApprovedForAward> sagApplicationApprovedForAwardList,
			String requestor ) throws SAGServiceException,
			AccessDeniedException;

	public List<SAGApplication> searchSAGApplicationsByReferenceNumber(
			List<String> referenceNumberList ) throws SAGServiceException,
			AccessDeniedException;

	public SAGApplicationsCountDetail getSAGApplicationsCountForYear(
			String financialYear ) throws SAGServiceException,
			AccessDeniedException;

	public SAGInputListIdsDescriptionDetail getSAGInputListDescriptions(
			SAGInputListIdsDescriptionDetail sagInputListIdsDescriptionDetail )
			throws SAGServiceException, AccessDeniedException;

	public void updateStatusAndApprovalRecordForSAGApplication(
			List<String> referenceNumberList,
			List<SAGStatusApprovalRecordDetail> sagStatusApprovalRecordDetails,
			String requestor ) throws SAGServiceException,
			AccessDeniedException;

	public SAGAnnouncementConfig getSAGAnnouncementConfig( String id )
			throws SAGServiceException;

	public List<SAGAnnouncementConfig> searchSAGAnnouncementConfig(
			String financialYear ) throws SAGServiceException;

	public void saveSAGAnnouncementConfig(
			SAGAnnouncementConfig sagAnnouncementConfig, String requestor )
			throws SAGServiceException, AccessDeniedException;

	public Double getSAGAwardBalanceByYear( String financialYear )
			throws SAGServiceException;

	public List<String> getSAGAwardYears() throws SAGServiceException;

	public SAGEmailContentDetails getSAGEmailContentDetails(
			String financialYear ) throws SAGServiceException;

	public String getMaxSAGSequenceNumber( String financialYear )
			throws SAGServiceException;

	public void saveSAGTask(SAGTask sagTask, String currentUser) throws SAGServiceException;

	public SAGTask getSAGTask(String referenceNumber) throws SAGServiceException;

	public void deleteSAGTask(String referenceNumber, String currentUser) throws SAGServiceException;
	
	public void processTask () throws SAGServiceException;
	
	public void processExpiredApplication () throws SAGServiceException;
	
	public List <SAGApplication> searchSAGApplicationBySN (String financialYear, List <String> seqNo) throws SAGServiceException;
	
	public List <String> searchSAGReferenceNoBySN (String financialYear, List <String> seqNo) throws SAGServiceException;
	
	public List <String> searchSAGSNByReferenceNo (String financialYear, List <String> referenceNoList) throws SAGServiceException;
	
	public void updateEventRsvpWithSN (List<String> febIds, String financialYear ,String seqNo, String requestor) throws SAGServiceException;
	
	public List <SAGAwardQuantumDescription> searchAwardQuantumByFY (String financialYear) throws SAGServiceException;
	
	public List <SAGApplication> getApplicationWithSameChildNric (String financialYear, String childNric, List<String> statuses, String referenceNumber) throws SAGServiceException;
	
	public List <SAGApplication> getSuccessfulApplicationByChildNric (List<String> financialYear, String childNric) throws SAGServiceException;
	
	public SAGEmailContentDetails getSAGEmailContentDetailsNoRsvp (String financialYear) throws SAGServiceException;
	
	public List<SAGApplicationSelectNricList> getDuplicatedFamilyBackground(
			String financialYear ) throws SAGServiceException;
	
	public void updateDuplicatedFamilyBackground (List<SAGApplication> sagApplicationList) throws SAGServiceException;
	
	public SAGApplication searchSAGApplicationFamilyBackground( String nric,
			String childNric, String awardType, String financialYear,
			boolean isOrderAsc ) throws SAGServiceException,
			AccessDeniedException;

	public List<SAGApplicationChildDetail> searchSAGChildDetailsWithYearEducation(
			List<String> financialYear, List<String> educationYear)
			throws SAGServiceException;
	
	public List<SAGBatchFileRecord> searchSAGBatchFileRecordByFinancialYear(String financialYear, List<String> paymentStatusList) throws SAGServiceException;

	public void saveSAGBatchFileRecords( List<SAGBatchFileRecord> sagBatchFileRecordList, String requestor) throws SAGServiceException;

	public void updateSAGApplicationPayment( Map<String, Object> context, SAGApplication sagApplication, String requestor) throws SAGServiceException;
	
	public List<SAGBatchFileRecord> searchSAGBatchFileRecordByReferenceNumber (List<String> referenceNumber) throws SAGServiceException;

	public List<SAGApplication> searchSAGApplicationsByChildNricAndEduLevel(
			String childNric, String awardType, String childNewEduLevel,
			String childHighestEduLevel, String financialYear) throws SAGServiceException,
			AccessDeniedException;

}
