package com.stee.spfcore.service.membership;

import java.util.Date;
import java.util.List;

import com.stee.spfcore.model.membership.DiscrepancyRecord;
import com.stee.spfcore.model.membership.Membership;
import com.stee.spfcore.model.membership.MembershipFees;
import com.stee.spfcore.model.membership.PaymentDataSource;
import com.stee.spfcore.model.membership.ReconciliationReport;
import com.stee.spfcore.model.membership.StatusChangeRecord;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.security.AccessDeniedException;
import com.stee.spfcore.vo.membership.MembershipCriteria;
import com.stee.spfcore.vo.membership.MembershipExtend;
import com.stee.spfcore.vo.membership.MembershipReport;
import com.stee.spfcore.vo.membership.PaymentRecordCriteria;

public interface IMembershipService {

    /**
     * Get the membership by nric.
     * 
     * @param nric
     * @return the membership detail. Null if not found
     * @throws MembershipServiceException
     *             exception while retrieving the data.
     */
    public Membership getMembership( String nric ) throws MembershipServiceException, AccessDeniedException;

    /**
     * Add new membership detail.
     * 
     * @param membership
     *            the detail to be added.
     * @throws MembershipServiceException
     *             exception while trying to add the detail.
     */
    public void addMembership( Membership membership, String requester ) throws MembershipServiceException, AccessDeniedException;

    /**
     * Update membership detail
     * 
     * @param membership
     * @throws MembershipServiceException
     */
    public void updateMembership( Membership membership, String requester ) throws MembershipServiceException, AccessDeniedException;

    /**
     * Apply for membership
     * 
     * @param membership
     * @throws MembershipServiceException
     */
    public void applyMembership( Membership membership, String requester ) throws MembershipServiceException, AccessDeniedException;

    /**
     * Withdraw Membership
     * Can only be invoked in the Internet side.
     * Invocation from Intranet side will throws UnsupportedOperationException.
     * 
     * @param membership
     * @throws MembershipServiceException
     */
    public void withdrawMembership( Membership membership, String requester ) throws MembershipServiceException, AccessDeniedException;

    /**
     * Submit insurance nomination
     * 
     * @param membership
     * @throws MembershipServiceException
     */
    public void submitInsuranceNomination( Membership membership, String requester ) throws MembershipServiceException, AccessDeniedException;

    /**
     * Search for All Members(PersonalDetail) with respect to search Criteria.
     * Common Search Service for Membership Module.
     * 
     * @param membershipCriteria
     * @return
     * @throws MembershipServiceException
     */
    public List< PersonalDetail > searchMembersByCriteria( MembershipCriteria membershipCriteria ) throws MembershipServiceException;

    /**
     * Search for Membership report by Criteria.
     * 
     * @param membershipCriteria
     * @return
     * @throws MembershipServiceException
     */
    public List< MembershipReport > searchMemberReportByCriteria( MembershipCriteria membershipCriteria ) throws MembershipServiceException;

    /**
     * 
     * @Title: searchPaymentRecordByCriteria
     * @Description:
     * @param @param accrualMonth
     * @param @param balance
     * @param @return
     * @param @throws MembershipServiceException
     * @return List<Membership>
     * @throws
     */
    public List< Membership > searchPaymentRecordByCriteria( PaymentRecordCriteria paymentRecordCriteria ) throws MembershipServiceException;

    /**
     * 
     * @Title: searchMembershipNominee
     * @Description: search MembershipNominee By NRIC and MEMBER_NRIC
     * @param @param membership
     * @param @return
     * @return Membership
     * @throws
     */
    public Integer searchCountMembershipNominee( String memberNric, String nric ) throws MembershipServiceException;

    public List< MembershipFees > searchMembershipFees( String rankType, String rank, Date date ) throws MembershipServiceException;

    /**
     * 
     * @Title: updateMembershipFees
     * @Description: Update MembershipFees
     * @param @param membershipFees
     * @return void
     * @throws
     */
    public void updateMembershipFees( MembershipFees membershipFees, String requester ) throws MembershipServiceException;

    /**
     * 
     * @Title: searchMembersByCriteria2
     * @Description: Search Members By Criteria2
     * @param @param membershipCriteria
     * @param @param hasInsuranceCoverage
     * @param @return
     * @return List<PersonalDetail>
     * @throws
     */
    public List< PersonalDetail > searchMembersByCriteria2( MembershipCriteria membershipCriteria, Boolean hasInsuranceCoverage ) throws MembershipServiceException;

    /**
     * Return a list of StatusChangeRecord for a specific member on the specific month and year, order by the date.
     * 
     * @param nric
     * @param month
     * @param year
     * @return
     * @throws MembershipServiceException
     */
    public List< StatusChangeRecord > getStatusChangeRecords( String nric, int month, int year ) throws MembershipServiceException;

    /**
     * Return a list of StatusChangeRecord for the specific month and year, order by the date.
     * 
     * @param nric
     * @param month
     * @param year
     * @return
     * @throws MembershipServiceException
     */
    public List< StatusChangeRecord > getStatusChangeRecords( int month, int year ) throws MembershipServiceException;

    /**
     * Add new StatusChangeRecord
     * 
     * @param record
     * @throws MembershipServiceException
     */
    public void addStatusChangeRecord( StatusChangeRecord record ) throws MembershipServiceException;

    /**
     * Update StatusChangeRecord
     * 
     * @param record
     * @throws MembershipServiceException
     */
    public void updateStatusChangeRecord( StatusChangeRecord record ) throws MembershipServiceException;

    /**
     * Delete StatusChangeRecord
     * 
     * @param id
     * @throws MembershipServiceException
     */
    public void deleteStatusChangeRecord( long id ) throws MembershipServiceException;

    /**
     * Get specific member's DiscrepancyRecord for a given month and year
     * 
     * @param nric
     * @param month
     * @param year
     * @return
     * @throws MembershipServiceException
     */
    public DiscrepancyRecord getDiscrepancyRecord( String nric, Integer month, Integer year ) throws MembershipServiceException;

    /**
     * Get a list of DiscrepancyRecord of a given month and year.
     * If includeZeroBalance is true, it will return all records including those with zero balance.
     * 
     * @param month
     * @param year
     * @param includeZeroBalance
     * @return
     * @throws MembershipServiceException
     */
    public List< DiscrepancyRecord > getDiscrepancyRecords( Integer month, Integer year, boolean includeZeroBalance ) throws MembershipServiceException;

    /**
     * Get a list of DiscrepancyRecord for a given member
     * If includeZeroBalance is true, it will return all records including those with zero balance.
     * 
     * @param nric
     * @param includeZeroBalance
     * @return
     * @throws MembershipServiceException
     */
    public List< DiscrepancyRecord > getDiscrepancyRecords( String nric, boolean includeZeroBalance ) throws MembershipServiceException;

    /**
     * Add or update the DiscrepancyRecord.
     * 
     * @param record
     * @throws MembershipServiceException
     */
    public void saveDiscrepancyRecord( DiscrepancyRecord record ) throws MembershipServiceException;

    /**
     * Delete a specific member's discrepancy record.
     * 
     * @param nric
     * @param month
     * @param year
     * @throws MembershipServiceException
     */
    public void deleteDiscrepancyRecord( String nric, Integer month, Integer year ) throws MembershipServiceException;

    /**
     * Delete all DiscrepancyRecords of the specified month and year
     * 
     * @param month
     * @param year
     * @return number of record deleted
     * @throws MembershipServiceException
     */
    public int deleteDiscrepancyRecords( Integer month, Integer year ) throws MembershipServiceException;

    /**
     * Delete all Payment Histories of the specified month, year, and source
     * 
     * @param month
     * @param year
     * @param source
     * @return number of record deleted
     * @throws MembershipServiceException
     */
    public int deletePaymentHistories( Integer month, Integer year, PaymentDataSource source ) throws MembershipServiceException;

    /**
     * Get list of DiscrepancyRecord (with transient field populated) of non current month for reconciliation report.
     */
    public List< DiscrepancyRecord > computeNonCurrentMonthDiscrepancyRecordsForReconciliationReport() throws MembershipServiceException;

    public ReconciliationReport getLatestReconciliationReport( int year, int month ) throws MembershipServiceException;

    public ReconciliationReport getReconciliationReport( int year, int month, Date revisionTimeStamp ) throws MembershipServiceException;

    public List< Date > getReconciliationReportRevisionTimestamps( int year, int month ) throws MembershipServiceException;

    public void addReconciliationReport( ReconciliationReport report, String requester ) throws MembershipServiceException;

    public Boolean checkMembershipPayment( String checker, String nric, Integer month, Integer year, boolean writeCheckRecordToDb ) throws MembershipServiceException;

    public List< Boolean > checkMembershipPayments( String checker, List< String > nricList, Integer month, Integer year, boolean writeCheckRecordToDb ) throws MembershipServiceException;

    public List< MembershipExtend > searchMembershipOnFullMonthNoPayLeave( Date date ) throws MembershipServiceException;
}
