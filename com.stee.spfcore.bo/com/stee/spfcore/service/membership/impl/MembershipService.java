package com.stee.spfcore.service.membership.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.dao.MembershipDAO;
import com.stee.spfcore.dao.PersonnelDAO;
import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.model.membership.DiscrepancyRecord;
import com.stee.spfcore.model.membership.Membership;
import com.stee.spfcore.model.membership.MembershipFees;
import com.stee.spfcore.model.membership.MembershipPaymentCheckRecord;
import com.stee.spfcore.model.membership.MembershipPaymentCheckResult;
import com.stee.spfcore.model.membership.PaymentDataSource;
import com.stee.spfcore.model.membership.PaymentHistory;
import com.stee.spfcore.model.membership.ReconciliationReport;
import com.stee.spfcore.model.membership.StatusChangeRecord;
import com.stee.spfcore.model.personnel.LeaveType;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.security.AccessDeniedException;
import com.stee.spfcore.service.membership.IMembershipService;
import com.stee.spfcore.service.membership.MembershipServiceException;
import com.stee.spfcore.utils.ConvertUtil;
import com.stee.spfcore.utils.Util;
import com.stee.spfcore.vo.membership.MembershipCriteria;
import com.stee.spfcore.vo.membership.MembershipExtend;
import com.stee.spfcore.vo.membership.MembershipReport;
import com.stee.spfcore.vo.membership.PaymentRecordCriteria;

public class MembershipService implements IMembershipService {

    private static final Logger logger = Logger.getLogger( MembershipService.class.getName() );

    private MembershipDAO dao;
    private PersonnelDAO personnelDAO;

    public MembershipService() {
        dao = new MembershipDAO();
        personnelDAO = new PersonnelDAO();
    }

    @Override
    public Membership getMembership( String nric ) throws MembershipServiceException, AccessDeniedException {
        Membership result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getMembership( nric );

            SessionFactoryUtil.commitTransaction();
        }
        catch (AccessDeniedException | RuntimeException e ) {
            logger.severe(String.format("Fail to get membership:%s %s", Util.replaceNewLine( nric ), e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public void addMembership( Membership membership, String requester ) throws MembershipServiceException, AccessDeniedException {

        try {
            SessionFactoryUtil.beginTransaction();

            dao.addMembership( membership, requester );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( AccessDeniedException e ) {
            logger.severe(String.format("Fail to add membership:%s %s", membership.getNric(), e ));
            SessionFactoryUtil.rollbackTransaction();
        }
        catch ( Exception exception ) {
            logger.severe(String.format("Fail to add membership: %s %s", membership.getNric(), exception ));
            SessionFactoryUtil.rollbackTransaction();
        }
    }

    @Override
    public void updateMembership( Membership membership, String requester ) throws MembershipServiceException, AccessDeniedException {

        try {
            SessionFactoryUtil.beginTransaction();

            dao.updateMembership( membership, requester );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception exception ) {
            logger.severe(String.format( "Fail to update membership: %s %s", Util.replaceNewLine( membership.getNric() ), exception ));
            SessionFactoryUtil.rollbackTransaction();
        }
    }

    @Override
    public void applyMembership( Membership membership, String requester ) throws MembershipServiceException {
        throw new UnsupportedOperationException( "Only Internet side is allow to make request to apply membership." );
    }

    @Override
    public void withdrawMembership( Membership membership, String requester ) throws MembershipServiceException {
        throw new UnsupportedOperationException( "Only Internet side is allow to make request to withdraw membership." );
    }

    @Override
    public void submitInsuranceNomination( Membership membership, String requester ) throws MembershipServiceException {
        throw new UnsupportedOperationException( "Only Internet side is allow to make request to submit insurance nomination." );
    }

    @Override
    public List< PersonalDetail > searchMembersByCriteria( MembershipCriteria membershipCriteria ) throws MembershipServiceException {

        List< PersonalDetail > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.searchMembersByCritieria( membershipCriteria );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            logger.severe(String.format( "Fail to get members by Search Criteria: %s %s", membershipCriteria.toString(), e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public List< MembershipReport > searchMemberReportByCriteria( MembershipCriteria membershipCriteria ) throws MembershipServiceException {
        List< MembershipReport > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.searchMemberReportByCritieria( membershipCriteria );

            if ( result != null && !result.isEmpty() ) {
                for ( MembershipReport each : result ) {
                    PersonalDetail personalDetail = personnelDAO.getPersonal( each.getNric() );
                    if ( personalDetail != null ) {
                        each.setEmailContacts( personalDetail.getEmailContacts() );
                        each.setPhoneContacts( personalDetail.getPhoneContacts() );
                    }
                }
            }

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.severe(String.format("Fail to get member Reports by Search Criteria: %s %s", membershipCriteria.toString(), e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public List< Membership > searchPaymentRecordByCriteria( PaymentRecordCriteria paymentRecordCriteria ) throws MembershipServiceException {

        List< Membership > result = null;
        try {

            SessionFactoryUtil.beginTransaction();
            result = dao.searchPaymentRecordByAccrualMonth( paymentRecordCriteria );
            SessionFactoryUtil.commitTransaction();

        }
        catch ( RuntimeException e ) {
            logger.severe(String.format( "Fail to get membership payment record by accrualMonth: %s %s" , paymentRecordCriteria.toString(), e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public Integer searchCountMembershipNominee( String memberNric, String nric ) throws MembershipServiceException {
        Integer result = null;

        try {

            SessionFactoryUtil.beginTransaction();
            result = dao.searchCountMembershipNominee( memberNric, nric );
            SessionFactoryUtil.commitTransaction();

        }
        catch ( RuntimeException e ) {
            logger.severe(String.format("Fail to get Membership Nominee by memberNric: %s nric: %s, %s", memberNric, nric, e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public List< MembershipFees > searchMembershipFees( String rankType, String rank, Date date ) throws MembershipServiceException {
        List< MembershipFees > result = null;

        try {

            SessionFactoryUtil.beginTransaction();
            result = dao.searchMembershipFees( rankType, rank, date );
            SessionFactoryUtil.commitTransaction();

        }
        catch ( RuntimeException e ) {
            String errMsg = String.format( "searchMembershipFees() failed. rankType=%s, rank=%s, date=%s", rankType, rank, ConvertUtil.convertDateToDateString( date ) );
            logger.severe(String.format( errMsg, e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public void updateMembershipFees( MembershipFees membershipFees, String requester ) throws MembershipServiceException {
        try {

            SessionFactoryUtil.beginTransaction();
            dao.updateMembershipFees( membershipFees, requester );
            SessionFactoryUtil.commitTransaction();

        }
        catch ( Exception exception ) {
            logger.severe(String.format("Fail to update membershipFees: %s %s", membershipFees.toString(), exception ));
            SessionFactoryUtil.rollbackTransaction();
        }
    }

    @Override
    public List< PersonalDetail > searchMembersByCriteria2( MembershipCriteria membershipCriteria, Boolean hasInsuranceCoverage ) throws MembershipServiceException {

        List< PersonalDetail > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.searchMembersByCritieria2( membershipCriteria, hasInsuranceCoverage );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            logger.severe(String.format("Fail to get members by Search Criteria2: %s %s", membershipCriteria.toString(), e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;

    }

    @Override
    public List< StatusChangeRecord > getStatusChangeRecords( String nric, int month, int year ) throws MembershipServiceException {

        List< StatusChangeRecord > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getStatusChangeRecords( nric, month, year );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            logger.severe(String.format("Fail to get membership status change records: %s %s", nric, e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public List< StatusChangeRecord > getStatusChangeRecords( int month, int year ) throws MembershipServiceException {

        List< StatusChangeRecord > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getStatusChangeRecords( month, year );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            logger.severe(String.format("Fail to get membership status change records for month: %s, year=%s %s", month, year, e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public void addStatusChangeRecord( StatusChangeRecord statusChangeRecordAdd ) throws MembershipServiceException {

        try {
            SessionFactoryUtil.beginTransaction();

            dao.addStatusChangeRecord( statusChangeRecordAdd );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception exception ) {
            logger.severe(String.format("Fail to add membership status change record:%s %s", statusChangeRecordAdd.getNric(), exception ));
            SessionFactoryUtil.rollbackTransaction();
        }

    }

    @Override
    public void updateStatusChangeRecord( StatusChangeRecord statusChangeRecordUpdate ) throws MembershipServiceException {

        try {
            SessionFactoryUtil.beginTransaction();

            dao.updateStatusChangeRecord( statusChangeRecordUpdate );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception exception ) {
            logger.severe(String.format("Fail to update membership status change record: %s %s",statusChangeRecordUpdate.getNric(), exception ));
            SessionFactoryUtil.rollbackTransaction();
        }
    }

    @Override
    public void deleteStatusChangeRecord( long id ) throws MembershipServiceException {

        try {
            SessionFactoryUtil.beginTransaction();

            dao.deleteStatusChangeRecord( id );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception exception ) {
            logger.severe(String.format("Fail to delete membership status change record: %s %s", id, exception ));
            SessionFactoryUtil.rollbackTransaction();
        }

    }

    @Override
    public DiscrepancyRecord getDiscrepancyRecord( String nric, Integer month, Integer year ) throws MembershipServiceException {

        DiscrepancyRecord result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getDiscrepancyRecord( nric, month, year );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            logger.severe(String.format("Fail to get DiscrepancyRecord for: nric=%s, month=%s, year=%s %s", nric, month, year, e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public List< DiscrepancyRecord > getDiscrepancyRecords( Integer month, Integer year, boolean includeZeroBalance ) throws MembershipServiceException {

        List< DiscrepancyRecord > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getDiscrepancyRecords( month, year, includeZeroBalance );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            logger.severe(String.format("Fail to get DiscrepancyRecords for: month=%s, year=%s %s", month , year, e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public List< DiscrepancyRecord > getDiscrepancyRecords( String nric, boolean includeZeroBalance ) throws MembershipServiceException {

        List< DiscrepancyRecord > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getDiscrepancyRecords( nric, includeZeroBalance );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            logger.severe(String.format("Fail to get DiscrepancyRecords for: nric=%s, includeZeroBalance=%s %s", nric, includeZeroBalance, e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;

    }

    @Override
    public void saveDiscrepancyRecord( DiscrepancyRecord discrepancyRecordSave ) throws MembershipServiceException {
        try {
            SessionFactoryUtil.beginTransaction();

            dao.saveDiscrepancyRecord( discrepancyRecordSave );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception exception ) {
            logger.severe(String.format("Fail to save DiscrepancyRecord: nric= %s, month=%s, year=%s %s",discrepancyRecordSave.getNric(), discrepancyRecordSave.getMonth(), discrepancyRecordSave.getYear(), exception ));
            SessionFactoryUtil.rollbackTransaction();
        }
    }

    @Override
    public void deleteDiscrepancyRecord( String nric, Integer month, Integer year ) throws MembershipServiceException {
        try {
            SessionFactoryUtil.beginTransaction();

            dao.deleteDiscrepancyRecords( nric, month, year );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception exception ) {
            logger.severe(String.format("Fail to delete DiscrepancyRecord: nric=%s, month=%s, year=%s %s", nric, month, year, exception ));
            SessionFactoryUtil.rollbackTransaction();
        }
    }

    @Override
    public int deleteDiscrepancyRecords( Integer month, Integer year ) throws MembershipServiceException {
        int count = 0;
        try {
            SessionFactoryUtil.beginTransaction();

            count = dao.deleteDiscrepancyRecords( null, month, year );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception exception ) {
            logger.severe(String.format("Fail to delete DiscrepancyRecords: month=%s, year=%s %s" , month ,year, exception ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return count;
    }

    @Override
    public int deletePaymentHistories( Integer month, Integer year, PaymentDataSource source ) throws MembershipServiceException {

        int count = 0;
        try {
            SessionFactoryUtil.beginTransaction();

            count = dao.deletePaymentHistories( month, year, source );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception exception ) {
            logger.log( Level.SEVERE, "Fail to delete Payment History: month=" + month + ", year=" + year + ", source=" + source, exception );
            SessionFactoryUtil.rollbackTransaction();
        }

        return count;
    }

    @Override
    public List< DiscrepancyRecord > computeNonCurrentMonthDiscrepancyRecordsForReconciliationReport() throws MembershipServiceException {
        List< DiscrepancyRecord > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.computeNonCurrentMonthDiscrepancyRecordsForReconciliationReport();

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            String errMsg = "Fail to get discrepancy records of non current month for reconciliation report.";
            logger.log( Level.SEVERE, errMsg );
            SessionFactoryUtil.rollbackTransaction();
            throw new MembershipServiceException( errMsg );
        }

        return result;
    }

    public ReconciliationReport getLatestReconciliationReport( int year, int month ) throws MembershipServiceException {
        ReconciliationReport result = null;
        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getLatestReconciliationReport( year, month );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.SEVERE, "getLatestReconciliationReport() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }
        return result;
    }

    public ReconciliationReport getReconciliationReport( int year, int month, Date revisionTimeStamp ) throws MembershipServiceException {
        ReconciliationReport result = null;
        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getReconciliationReport( year, month, revisionTimeStamp );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.SEVERE, "getReconciliationReport() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }
        return result;
    }

    public List< Date > getReconciliationReportRevisionTimestamps( int year, int month ) throws MembershipServiceException {
        List< Date > result = null;
        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getReconciliationReportRevisionTimestamps( year, month );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.SEVERE, "getReconciliationReportRevisionTimestamps() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }
        return result;
    }

    public void addReconciliationReport( ReconciliationReport report, String requester ) throws MembershipServiceException {
        try {
            SessionFactoryUtil.beginTransaction();

            dao.addReconciliationReport( report, requester );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.SEVERE, "addReconciliationReport() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }
    }

    public Boolean checkMembershipPayment( String checker, String nric, Integer month, Integer year, boolean writeCheckRecordToDb ) throws MembershipServiceException {
        List< String > nricList = new ArrayList<>();
        nricList.add( nric );
        List< Boolean > results = this.checkMembershipPayments( checker, nricList, month, year, writeCheckRecordToDb );
        Boolean result = null;
        if ( results != null && !results.isEmpty()) {
            result = results.get( 0 );
        }
        return result;
    }

    public List< Boolean > checkMembershipPayments( String checker, List< String > nricList, Integer month, Integer year, boolean writeCheckRecordToDb ) throws MembershipServiceException {
        List< Boolean > results = null;

        Integer nricListCount = ( nricList == null ) ? null : nricList.size();
        if (nricListCount!=null && logger.isLoggable( Level.INFO )) {
            logger.log(Level.INFO, String.format("checkMembershipPayments() started. nricListCount=%s, month=%s, year=%s", nricListCount, month, year));
        }

        if ( nricList != null ) {
            results = new ArrayList<>(nricListCount);
            Calendar calendar = Calendar.getInstance();
            int curMonth = calendar.get( Calendar.MONTH ) + 1;
            int curYear = calendar.get( Calendar.YEAR );
            Integer curIndex = null;
            String curNric = null;

            try {
                SessionFactoryUtil.beginTransaction();

                for ( String nric : nricList ) {
                    int count = results.size();
                    curIndex = count + 1;
                    curNric = nric;

                    if ( curIndex % 200 == 0 ) {
                        SessionFactoryUtil.commitTransaction();
                        SessionFactoryUtil.beginTransaction();
                    }

                    MembershipPaymentCheckRecord membershipPaymentCheckRecord = new MembershipPaymentCheckRecord();
                    membershipPaymentCheckRecord.setChecker( checker );
                    membershipPaymentCheckRecord.setCheckDateTime( calendar.getTime() );
                    membershipPaymentCheckRecord.setMonth( month );
                    membershipPaymentCheckRecord.setYear( year );
                    membershipPaymentCheckRecord.setNric( nric );

                    // check future date
                    if (( year > curYear ) || ( year == curYear && month > curMonth)){
                        membershipPaymentCheckRecord.setCheckResult( MembershipPaymentCheckResult.FUTURE_DATE );
                        results.add( membershipPaymentCheckRecord.getCheckResult().getResult() );
                        if ( writeCheckRecordToDb ) {
                            dao.addMembershipPaymentCheckRecord( membershipPaymentCheckRecord );
                        }
                        continue;
                    }

                    // fetch discrepancy record of nric
                    DiscrepancyRecord discrepancyRecord = dao.getDiscrepancyRecord( nric, month, year );
                    if ( discrepancyRecord == null ) {
                        membershipPaymentCheckRecord.setCheckResult( MembershipPaymentCheckResult.NO_DISCREPANCY );
                        results.add( membershipPaymentCheckRecord.getCheckResult().getResult() );
                        if ( writeCheckRecordToDb ) {
                            dao.addMembershipPaymentCheckRecord( membershipPaymentCheckRecord );
                        }
                        continue;
                    }

                    Double fee = discrepancyRecord.getFee();
                    if ( ( fee == null ) || ( fee <= 0.0 ) ) {
                        List< PaymentHistory > paymentHistories = discrepancyRecord.getPaymentHistories();
                        if ( ( paymentHistories != null ) && ( paymentHistories.size() > 0 ) ) {
                            // have payment history
                            boolean hasPaid = false;
                            for ( PaymentHistory paymentHistory : paymentHistories ) {
                                Double amount = paymentHistory.getAmount();
                                if ( amount == null || amount >= 0) {
                                    hasPaid = true;
                                    break;
                                }
                            }
                            if ( hasPaid )
                                membershipPaymentCheckRecord.setCheckResult( MembershipPaymentCheckResult.POSITIVE_AMT_COLLECTED );
                            else
                                membershipPaymentCheckRecord.setCheckResult( MembershipPaymentCheckResult.OWE_MONEY );
                        }
                        else {
                            // no payment history
                            membershipPaymentCheckRecord.setCheckResult( MembershipPaymentCheckResult.RANK_NOT_COVERED_NO_PAYMENT_OR_RANK_COVERED_FEE_ZERO );
                        }
                    }
                    else {
                        // got fee > 0
                        Double balance = discrepancyRecord.getBalance();
                        if ( ( balance != null ) && ( balance < 0 ) ) {
                            List< PaymentHistory > paymentHistories = discrepancyRecord.getPaymentHistories();
                            if ( ( paymentHistories != null ) && ( paymentHistories.size() > 0 ) ) {
                                // have payment history
                                for ( PaymentHistory paymentHistory : paymentHistories ) {
                                    Double amount = paymentHistory.getAmount();
                                    if ( ( amount != null ) && ( amount > 0 ) ) {
                                        membershipPaymentCheckRecord.setCheckResult( MembershipPaymentCheckResult.HAVE_POSITIVE_PAYMENT );
                                    }
                                }
                                membershipPaymentCheckRecord.setCheckResult( MembershipPaymentCheckResult.NO_POSITIVE_PAYMENT );
                            }
                            else {
                                // no payment history
                                membershipPaymentCheckRecord.setCheckResult( MembershipPaymentCheckResult.OWE_N_BALANCE );
                            }
                        }
                        else {
                            // balance >= 0
                            membershipPaymentCheckRecord.setCheckResult( MembershipPaymentCheckResult.REQD_TO_PAY_N_BALANCE );
                        }
                    }

                    results.add( membershipPaymentCheckRecord.getCheckResult().getResult() );
                    if ( writeCheckRecordToDb ) {
                        dao.addMembershipPaymentCheckRecord( membershipPaymentCheckRecord );
                    }
                }

                SessionFactoryUtil.commitTransaction();
            }
            catch ( Exception e ) {
                String errMsg = String.format( "checkMembershipPayments() failed. curIndex=%s, curNric=%s", curIndex, curNric );
                logger.log( Level.SEVERE, errMsg, e );
                // instead of rollback, commit all successful check records 
                SessionFactoryUtil.commitTransaction();
            }
        }

        if (nricListCount!=null && logger.isLoggable( Level.INFO )) {
            logger.log(Level.INFO, String.format("checkMembershipPayments() ended. nricListCount=%s, month=%s, year=%s", nricListCount, month, year));
        }
        return results;
    }

    public List< MembershipExtend > searchMembershipOnFullMonthNoPayLeave( Date date ) throws MembershipServiceException {
        List< MembershipExtend > result = null;
        try {

            SessionFactoryUtil.beginTransaction();
            result = dao.searchMembershipExtends( null, LeaveType.NO_PAY_LEAVE_CODE_IDS, date );
            SessionFactoryUtil.commitTransaction();

        }
        catch ( Exception exception ) {
            String errMsg = String.format( "searchMembershipOnFullMonthNoPayLeave() failed. date=%s", ConvertUtil.convertDateToDateString( date ) );
            logger.log( Level.SEVERE, errMsg, exception );
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }
}
