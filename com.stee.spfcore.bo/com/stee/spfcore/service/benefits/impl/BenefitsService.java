package com.stee.spfcore.service.benefits.impl;

import com.ibm.websphere.security.auth.WSSubject;
import com.stee.spfcore.dao.BenefitsDAO;
import com.stee.spfcore.dao.ProcessingInfoDAO;
import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.model.ActivationStatus;
import com.stee.spfcore.model.ApplicationStatus;
import com.stee.spfcore.model.benefits.*;
import com.stee.spfcore.model.code.Code;
import com.stee.spfcore.model.code.CodeType;
import com.stee.spfcore.model.internal.ApplicationType;
import com.stee.spfcore.model.internal.ProcessingInfo;
import com.stee.spfcore.model.membership.Membership;
import com.stee.spfcore.model.membership.MembershipType;
import com.stee.spfcore.model.personnel.Child;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.model.personnel.Spouse;
import com.stee.spfcore.security.AccessDeniedException;
import com.stee.spfcore.service.benefits.BenefitsServiceException;
import com.stee.spfcore.service.benefits.IBenefitsService;
import com.stee.spfcore.service.code.CodeServiceFactory;
import com.stee.spfcore.service.code.ICodeService;
import com.stee.spfcore.service.configuration.IFileHandlingConfig;
import com.stee.spfcore.service.configuration.ServiceConfig;
import com.stee.spfcore.service.membership.IMembershipService;
import com.stee.spfcore.service.membership.MembershipServiceException;
import com.stee.spfcore.service.membership.MembershipServiceFactory;
import com.stee.spfcore.service.personnel.IPersonnelService;
import com.stee.spfcore.service.personnel.PersonnelServiceException;
import com.stee.spfcore.service.personnel.PersonnelServiceFactory;
import com.stee.spfcore.service.process.IProcessService;
import com.stee.spfcore.service.process.ProcessServiceException;
import com.stee.spfcore.service.process.ProcessServiceFactory;
import com.stee.spfcore.utils.ReferenceNumberGenerator;
import com.stee.spfcore.utils.Util;
import com.stee.spfcore.utils.zip.ZipUtil;
import com.stee.spfcore.vo.benefits.*;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BenefitsService implements IBenefitsService {

    private static final Logger logger = Logger.getLogger( BenefitsService.class.getName() );

    private BenefitsDAO dao;
    private ProcessingInfoDAO infoDao;
    private IPersonnelService personnelService;
    private IMembershipService membershipService;
    private ICodeService codeService;

    private File workingFolder;
    private File ftpFolder;
    
    Date todayDate = new Date();
    Calendar todayCalendar = Calendar.getInstance();
    int currentYear = todayCalendar.get(Calendar.YEAR);
    int currentMonth = todayCalendar.get(Calendar.MONTH);
    int currentDate = todayCalendar.get(Calendar.DAY_OF_MONTH);

    public BenefitsService() {
        dao = new BenefitsDAO();
        infoDao = new ProcessingInfoDAO();
        this.personnelService = PersonnelServiceFactory.getPersonnelService();
        membershipService = MembershipServiceFactory.getMembershipService();
        this.codeService = CodeServiceFactory.getCodeService();
                
        IFileHandlingConfig config = ServiceConfig.getInstance().getFileHandlingConfig();

        workingFolder = new File( config.workingFolder() );
        if ( !workingFolder.exists() && !workingFolder.mkdirs()) {
            logger.warning( "Fail to create working folder:" + workingFolder.getAbsolutePath() );
        }

        ftpFolder = new File( config.ftpFolder() );
        if ( !ftpFolder.exists() && !ftpFolder.mkdirs() ) {
            logger.warning( "Fail to create FTP folder:" + ftpFolder.getAbsolutePath() );
        }
    }

    @Override
    public List< BereavementGrant > searchBereavementGrant( String nric ) throws BenefitsServiceException, AccessDeniedException {
        List< BereavementGrant > result = null;
        try {
            SessionFactoryUtil.beginTransaction();
            result = dao.searchBereavementGrant( nric );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( AccessDeniedException ade ) {
            logger.warning(String.format("Fail to searchBereavementGrant by nric:%s %s", nric, ade ));
            SessionFactoryUtil.rollbackTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Fail to searchBereavementGrant by nric:%s %s", nric, e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public List< BereavementGrant > searchBereavementGrantForLoginID( String nric ) throws BenefitsServiceException, AccessDeniedException {
        List< BereavementGrant > result = null;
        try {
            SessionFactoryUtil.beginTransaction();
            result = dao.searchBereavementGrantForLoginID( nric );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( AccessDeniedException ade ) {
            logger.warning(String.format("Fail to searchBereavementGrant by nric:%s %s", nric, ade ));
            SessionFactoryUtil.rollbackTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Fail to searchBereavementGrant by nric:%s %s", nric, e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }
    
    @Override
    public BereavementGrant getBereavementGrant( String referenceNumber ) throws BenefitsServiceException, AccessDeniedException {
        BereavementGrant result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getBereavementGrant( referenceNumber );
            SessionFactoryUtil.commitTransaction();

        }
        catch ( AccessDeniedException ade ) {
            logger.warning(String.format( "Fail to get BereavementGrant: %s %s", Util.replaceNewLine(referenceNumber), ade ));
            SessionFactoryUtil.rollbackTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format( "Fail to get BereavementGrant: %s %s", Util.replaceNewLine(referenceNumber), e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public void addBereavementGrant( BereavementGrant bereavementGrant, String requester ) throws BenefitsServiceException, AccessDeniedException {
        try {
            SessionFactoryUtil.beginTransaction();

            // Generate Unique Application Reference Number
            ReferenceNumberGenerator referenceNumberGenerator = ReferenceNumberGenerator.getInstance();
            String uniqueId = referenceNumberGenerator.getUniqueId( ApplicationType.BEREAVEMENT_GRANT );

            bereavementGrant.setReferenceNumber( uniqueId );
            dao.saveBereavementGrant( bereavementGrant, requester );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( AccessDeniedException ade ) {
            logger.warning(String.format( "Fail to update BereavementGrant: %s %s", Util.replaceNewLine( bereavementGrant.getReferenceNumber() ), ade ));
            SessionFactoryUtil.rollbackTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format( "Fail to update BereavementGrant: %s %s", Util.replaceNewLine( bereavementGrant.getReferenceNumber() ), e ));
            SessionFactoryUtil.rollbackTransaction();
        }
    }

    @Override
    public void updateBereavementGrant( BereavementGrant bereavementGrant, String requester ) throws BenefitsServiceException, AccessDeniedException {

        try {
            SessionFactoryUtil.beginTransaction();

            dao.saveBereavementGrant( bereavementGrant, requester );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( AccessDeniedException ade ) {
            logger.warning(String.format( "Fail to update BereavementGrant: %s %s", Util.replaceNewLine( bereavementGrant.getReferenceNumber() ), ade ));
            SessionFactoryUtil.rollbackTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format( "Fail to update BereavementGrant: %s %s", Util.replaceNewLine( bereavementGrant.getReferenceNumber() ), e ));
            SessionFactoryUtil.rollbackTransaction();
        }
    }

    @Override
    public List< NewBornGift > searchNewBorn( String nric ) throws BenefitsServiceException, AccessDeniedException {

        List< NewBornGift > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.searchNewBorn( nric );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( AccessDeniedException ade ) {
            logger.warning(String.format("Fail to search new born: %s %s", nric, ade ));
            SessionFactoryUtil.rollbackTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Fail to search new born: %s %s", nric, e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }
    
    @Override
    public List< NewBornGift > searchNewBornForLoginID( String nric ) throws BenefitsServiceException, AccessDeniedException {

        List< NewBornGift > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.searchNewBornForLoginID( nric );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( AccessDeniedException ade ) {
            logger.log( Level.WARNING, "Fail to search new born:" + nric, ade );
            SessionFactoryUtil.rollbackTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Fail to search new born: %s %s",nric, e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public NewBornGift getNewBorn( String referenceNumber ) throws BenefitsServiceException, AccessDeniedException {

        NewBornGift result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getNewBorn( referenceNumber );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( AccessDeniedException ade ) {
            logger.log( Level.WARNING, "Fail to get new born:" + Util.replaceNewLine( referenceNumber ), ade );
            SessionFactoryUtil.rollbackTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.WARNING, "Fail to get new born:" + Util.replaceNewLine( referenceNumber ), e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public void addNewBorn( NewBornGift newBornGift, String requester ) throws BenefitsServiceException, AccessDeniedException {
        try {
            SessionFactoryUtil.beginTransaction();

            // Generate Unique Application Reference Number
            ReferenceNumberGenerator referenceNumberGenerator = ReferenceNumberGenerator.getInstance();
            String uniqueId = referenceNumberGenerator.getUniqueId( ApplicationType.NEWBORN_GIFT );

            newBornGift.setReferenceNumber( uniqueId );
            dao.saveNewBorn( newBornGift, requester );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( AccessDeniedException ade ) {
            logger.log( Level.WARNING, "Fail to update new born:" + Util.replaceNewLine( newBornGift.getReferenceNumber() ), ade );
            SessionFactoryUtil.rollbackTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.WARNING, "Fail to update new born:" + Util.replaceNewLine( newBornGift.getReferenceNumber() ), e );
            SessionFactoryUtil.rollbackTransaction();
        }
    }

    @Override
    public void updateNewBorn( NewBornGift newBornGift, String requester ) throws BenefitsServiceException, AccessDeniedException {

        try {
            SessionFactoryUtil.beginTransaction();

            dao.saveNewBorn( newBornGift, requester );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( AccessDeniedException ade ) {
            logger.log( Level.WARNING, "Fail to update new born:" + Util.replaceNewLine( newBornGift.getReferenceNumber() ), ade );
            SessionFactoryUtil.rollbackTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.WARNING, "Fail to update new born:" + Util.replaceNewLine( newBornGift.getReferenceNumber() ), e );
            SessionFactoryUtil.rollbackTransaction();
        }
    }

    @Override
    public List< WeddingGift > searchWeddingGift( String nric ) throws BenefitsServiceException, AccessDeniedException {

        List< WeddingGift > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.searchWeddingGift( nric );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( AccessDeniedException ade ) {
            logger.log( Level.WARNING, "Fail to searchWeddingGift:" + nric, ade );
            SessionFactoryUtil.rollbackTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format( "Fail to searchWeddingGift: %s %s", nric, e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public List< WeddingGift > searchWeddingGiftForLoginID( String nric ) throws BenefitsServiceException, AccessDeniedException {

        List< WeddingGift > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.searchWeddingGiftForLoginID( nric );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( AccessDeniedException ade ) {
            logger.log( Level.WARNING, "Fail to searchWeddingGift:" + nric, ade );
            SessionFactoryUtil.rollbackTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format( "Fail to searchWeddingGift: %s %s",nric, e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }
    
    @Override
    public WeddingGift getWeddingGift( String referenceNumber ) throws BenefitsServiceException, AccessDeniedException {

        WeddingGift result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getWeddingGift( referenceNumber );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( AccessDeniedException ade ) {
            logger.log( Level.WARNING, "Fail to get Wedding Gift:" + Util.replaceNewLine(referenceNumber), ade );
            SessionFactoryUtil.rollbackTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.WARNING, "Fail to get Wedding Gift:" + Util.replaceNewLine(referenceNumber), e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public void addWeddingGift( WeddingGift weddingGift, String requester ) throws BenefitsServiceException, AccessDeniedException {
        try {
            SessionFactoryUtil.beginTransaction();

            // Generate Unique Application Reference Number
            ReferenceNumberGenerator referenceNumberGenerator = ReferenceNumberGenerator.getInstance();
            String uniqueId = referenceNumberGenerator.getUniqueId( ApplicationType.WEDDING_GIFT );

            weddingGift.setReferenceNumber( uniqueId );
            dao.saveWeddingGift( weddingGift, requester );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( AccessDeniedException ade ) {
            logger.log( Level.WARNING, "Fail to update wedding gift:" + Util.replaceNewLine( weddingGift.getReferenceNumber() ), ade );
            SessionFactoryUtil.rollbackTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.WARNING, "Fail to update wedding gift:" + Util.replaceNewLine( weddingGift.getReferenceNumber() ), e );
            SessionFactoryUtil.rollbackTransaction();
        }
    }

    @Override
    public void updateWeddingGift( WeddingGift weddingGift, String requester ) throws BenefitsServiceException, AccessDeniedException {

        try {
            SessionFactoryUtil.beginTransaction();

            dao.saveWeddingGift( weddingGift, requester );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( AccessDeniedException ade ) {
            logger.log( Level.WARNING, "Fail to update Wedding Gift:" + Util.replaceNewLine( weddingGift.getReferenceNumber() ), ade );
            SessionFactoryUtil.rollbackTransaction();
        }
        catch ( Exception exception ) {
            logger.log( Level.WARNING, "Fail to update Wedding Gift:" + Util.replaceNewLine( weddingGift.getReferenceNumber() ), exception );
            SessionFactoryUtil.rollbackTransaction();
        }
    }

    /**
     * The file transfer process via SFTP is as follows:
     * <ol>
     * <li>In the Internet side, EJB timer service will trigger method of same name in FEBBenefitsService to create a zip file with the reference number as filename, which contains all the supporting documents for the application. The
     * document in the zip is named using the FEB_ID.</li>
     * <li>Windows script that is scheduled to trigger at regular interval will encrypt the zip file using SLIFT and move the file into SFTP server.</li>
     * <li>Windows script at the Intranet side will then move the file from SFTP server into local filesystem, and decrypt the file.</li>
     * <li>EJB timer service will trigger this method which will unzip the zip files and rename the files to using the id of the SupportingDocument as filename, and trigger the ProcessService to add document.</li>
     * </ol>
     * 
     * @throws BenefitsServiceException
     * @throws AccessDeniedException
     */
    @Override
    public void processSupportingDocuments() throws BenefitsServiceException, AccessDeniedException {

        List< ProcessingInfo > list = null;

        try {
            SessionFactoryUtil.beginTransaction();

            list = infoDao.getAllPendingUpload();

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception exception ) {
            logger.log( Level.WARNING, "Fail to retrieve pending file transfer record", exception );
            SessionFactoryUtil.rollbackTransaction();
        }

        IProcessService processService = ProcessServiceFactory.getInstance();

        try {
            for ( ProcessingInfo info : list ) {
                if ( info.getType() == ApplicationType.BEREAVEMENT_GRANT ) {
                    BereavementGrant grant = getBereavementGrant( info.getReferenceNumber() );

                    if ( prepareForFileUpload( info) ) {
                        // 4) Invoke BPM
                        processService.applyBereavementGrant( grant );

                        // 5) Update ProcessingInfo.
                        updateProcessInfo( info );
                    }
                }
                else if ( info.getType() == ApplicationType.NEWBORN_GIFT ) {
                    NewBornGift gift = getNewBorn( info.getReferenceNumber() );
                    if ( prepareForFileUpload( info) ) {
                        // 4) Invoke BPM
                        processService.applyNewBornGift( gift );

                        // 5) Update ProcessingInfo.
                        updateProcessInfo( info );
                    }
                }
                else if ( info.getType() == ApplicationType.WEDDING_GIFT ) {
                    WeddingGift gift = getWeddingGift( info.getReferenceNumber() );
                    if ( prepareForFileUpload( info) ) {
                        // 4) Invoke BPM
                        processService.applyWeddingGift( gift );

                        // 5) Update ProcessingInfo.
                        updateProcessInfo( info );
                    }
                }
            }
        }
        catch ( ProcessServiceException e ) {
            logger.log( Level.WARNING, "Fail to upload file to BPM process", e );
        }
    }

    private void updateProcessInfo( ProcessingInfo info ) throws BenefitsServiceException {

        info.setBpmFileUploaded( true );
        info.setUpdatedOn( new Date() );
        try {
            SessionFactoryUtil.beginTransaction();

            infoDao.updateProcessingInfo( info );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception exception ) {
            logger.log( Level.WARNING, "Fail to update processing info", exception );
            SessionFactoryUtil.rollbackTransaction();
        }
    }

    private boolean prepareForFileUpload( ProcessingInfo info ) throws BenefitsServiceException {

        // 1) Move the zip file from ftp folder to working folder.
        File zipFile = new File( ftpFolder, info.getReferenceNumber() + ".zip" );
        if ( !zipFile.exists() ) {
            logger.info( "File has not arrive:" + zipFile.getAbsolutePath() );
            //Doesn't exists. File has not arrive from FTP yet.
            return false;
        }

        File unzipFile = new File( workingFolder, info.getReferenceNumber() + ".zip" );
        // Clean up
        if (unzipFile.exists()) {
            try {
                Files.delete(Paths.get(unzipFile.toURI()));
                if ( logger.isLoggable( Level.INFO ) ) {
                    logger.info(String.format("Unzip file deleted successfully: %s", unzipFile.getAbsolutePath()));
                }
            } catch (IOException e) {
                logger.warning(String.format("Failed to delete unzip file: %s %s" , unzipFile.getAbsolutePath(), e));
            }
        }

        if ( !zipFile.renameTo( unzipFile ) ) {
            logger.severe( "Fail to transfer file to working folder:" + zipFile.getAbsolutePath() );
            //Move file to the working folder not successful.
            return false;
        }

        // 2) Unzip the zip file.
        try {
            ZipUtil.unzip( workingFolder.getAbsolutePath(), unzipFile.getAbsolutePath() );
        }
        catch ( ZipException e ) {
            logger.log( Level.WARNING, "Fail to unzip file for " + Util.replaceNewLine( info.getReferenceNumber() ), e );
        }

        return true;
    }

    @Override
    public List< NewBornGiftDetails > searchNewBornByApprovalsCriteria( BenefitsCriteria benefitsCriteria ) throws BenefitsServiceException {
        List< NewBornGiftDetails > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.searchNewBornByApprovalsCriteria( benefitsCriteria );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.WARNING, "Fail to search new born by Approvals Criteria:" + benefitsCriteria.toString(), e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public List< WeddingGiftDetails > searchWeddingByApprovalsCriteria( BenefitsCriteria benefitsCriteria ) throws BenefitsServiceException {
        List< WeddingGiftDetails > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.searchWeddingByApprovalsCriteria( benefitsCriteria );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.WARNING, "Fail to searchWeddingGift by Approvals Criteria:" + benefitsCriteria.toString(), e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public List< BereavementGrantDetails > searchBereavementGrantByApprovalsCriteria( BenefitsCriteria benefitsCriteria ) throws BenefitsServiceException {
        List< BereavementGrantDetails > result = null;
        try {
            SessionFactoryUtil.beginTransaction();
            result = dao.searchBereavementGrantByApprovalsCriteria( benefitsCriteria );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.WARNING, "Fail to searchBereavementGrant by ApprovalRecords Criteria:" + benefitsCriteria.toString(), e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

	@Override
    public List< DashboardStatus> RetrieveDashboardStatus() throws BenefitsServiceException {
    	List< DashboardStatus > result = null;
        try {
        	
        	SessionFactoryUtil.beginTransaction();

        	result = dao.getDashBoardStatus();

            SessionFactoryUtil.commitTransaction();
        	
        	
        }
        catch ( Exception e ) {
            logger.log( Level.WARNING, "Fail to retrieveStatus:", e );
        }

        return result;
    }
    
    @Override
    public GrantBudget getGrantBudget( Long id ) throws BenefitsServiceException {
        GrantBudget result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getGrantBudget( id );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.warning(String.format("Fail to get Grant Budget record by id: %s %s",  id, e ));
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public List< GrantBudget > searchGrantBudgetByCriteria( GrantBudgetCriteria budgetCriteria ) throws BenefitsServiceException {
        List< GrantBudget > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.searchGrantBudgetByCriteria( budgetCriteria );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.WARNING, "Fail to searchGrantBudgetByCriteria:" + budgetCriteria.toString(), e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public void addGrantBudget( GrantBudget grantBudget, String requester ) throws BenefitsServiceException {
        try {
            SessionFactoryUtil.beginTransaction();

            dao.addGrantBudget( grantBudget, requester );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.WARNING, "Fail to add grant budget:" + grantBudget.getGrantType() + "," + grantBudget.getGrantSubType(), e );
            SessionFactoryUtil.rollbackTransaction();
        }

    }

    @Override
    public void updateGrantBudget( GrantBudget grantBudget, String requester ) throws BenefitsServiceException {
        try {
            SessionFactoryUtil.beginTransaction();

            dao.updateGrantBudget( grantBudget, requester );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception exception ) {
            logger.log( Level.WARNING, "Fail to update Grant Budget:" + grantBudget.getGrantType() + "," + grantBudget.getGrantSubType(), exception );
            SessionFactoryUtil.rollbackTransaction();
        }

    }

    @Override
    public void saveBereavementGrant( BereavementGrant bereavementGrant, String requester ) throws BenefitsServiceException {
        throw new UnsupportedOperationException( "Only Internet side is allow to call Save bereavement grant." );
    }

    @Override
    public void saveNewBorn( NewBornGift newBornGift, String requester ) throws BenefitsServiceException {
        throw new UnsupportedOperationException( "Only Internet side is allow to call Save new born Gift." );
    }

    @Override
    public void saveWeddingGift( WeddingGift weddingGift, String requester ) throws BenefitsServiceException {
        throw new UnsupportedOperationException( "Only Internet side is allow to call Save wedding Gift." );
    }

    @Override
    public Double getTotalBereavementGrantAmountPaid( BenefitsCriteria benefitsCriteria ) throws BenefitsServiceException {
        Double result = null;
        try {
            SessionFactoryUtil.beginTransaction();
            result = dao.getTotalBereavementGrantPaidByCriteria( benefitsCriteria );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.WARNING, "Fail to get Total Bereavement Grant Amount Paid:" + benefitsCriteria.toString(), e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public Double getTotalNewBornGrantAmountPaid( BenefitsCriteria benefitsCriteria ) throws BenefitsServiceException {
        Double result = null;
        try {
            SessionFactoryUtil.beginTransaction();
            result = dao.getTotalNewBornGiftGrantPaidByCriteria( benefitsCriteria );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.WARNING, "Fail to get Total New Born Grant Amount Paid:" + benefitsCriteria.toString(), e );
            SessionFactoryUtil.rollbackTransaction();

        }

        return result;
    }

    @Override
    public Double getTotalWeddingGrantAmountPaid( BenefitsCriteria benefitsCriteria ) throws BenefitsServiceException {
        Double result = null;
        try {
            SessionFactoryUtil.beginTransaction();
            result = dao.getTotalWeddingGiftGrantPaidByCriteria( benefitsCriteria );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.WARNING, "Fail to get Total Wedding Gift Grant Amount Paid:" + benefitsCriteria.toString(), e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public Long searchCountBereavementGrant( String deceasedIdType, String deceasedNric, String deathCertificateNumber, String referenceNumber ) throws BenefitsServiceException {
        throw new UnsupportedOperationException( "Only Internet side is allow to call Search count of Bereavement Grant." );
    }

    @Override
    public List< BereavementGrantDetails > searchBereavementGrant( BenefitsCriteria benefitsCriteria ) throws BenefitsServiceException {
        List< BereavementGrantDetails > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.searchBereavementGrant( benefitsCriteria );

            SessionFactoryUtil.commitTransaction();

        }
        catch ( Exception e ) {
            logger.log( Level.WARNING, "Fail to searchBereavementGrant by benefitsCriteria:" + benefitsCriteria.toString(), e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public List< NewBornGiftDetails > searchNewBorn( BenefitsCriteria benefitsCriteria ) throws BenefitsServiceException {
        List< NewBornGiftDetails > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.searchNewBorn( benefitsCriteria );

            SessionFactoryUtil.commitTransaction();

        }
        catch ( Exception e ) {
            logger.log( Level.WARNING, "Fail to search New Born details by benefitsCriteria:" + benefitsCriteria.toString(), e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public List< WeddingGiftDetails > searchWeddingGift( BenefitsCriteria benefitsCriteria ) throws BenefitsServiceException {
        List< WeddingGiftDetails > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.searchWeddingGift( benefitsCriteria );

            SessionFactoryUtil.commitTransaction();

        }
        catch ( Exception e ) {
            logger.log( Level.WARNING, "Fail to search Wedding Gift by benefitsCriteria:" + benefitsCriteria.toString(), e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public WeightMgmtSubsidy getWeightMgmtSubsidy( String referenceNumber ) throws BenefitsServiceException {
        throw new UnsupportedOperationException( "Only Internet side is allow to call Get Weight Mgmt Subsidy." );
    }

    @Override
    public List< WeightMgmtSubsidy > searchWeightMgmtSubsidyByNric( String nric, Calendar today ) throws BenefitsServiceException {
        throw new UnsupportedOperationException( "Only Internet side is allow to call Search Weight Mgmt Subsidy By Nric." );
    }

    @Override
    public void updateWeightMgmtSubsidy( WeightMgmtSubsidy weightMgmtSubsidy, String requester ) throws BenefitsServiceException {
        throw new UnsupportedOperationException( "Only Internet side is allow to call Update Weight Mgmt Subsidy." );

    }

    @Override
    public void addWeightMgmtSubsidy( WeightMgmtSubsidy weightMgmtSubsidy, String requester ) throws BenefitsServiceException {
        throw new UnsupportedOperationException( "Only Internet side is allow to call Add Weight Mgmt Subsidy." );

    }

    @Override
    public List< HealthCareProvider > getHealthCareProviderList() throws BenefitsServiceException {
        throw new UnsupportedOperationException( "Only Internet side is allow to call Get HealthCare Provider List." );
    }

    @Override
    public HealthCareProvider getHealthCareProviderByServiceProvider( String serviceProvider ) throws BenefitsServiceException {
        throw new UnsupportedOperationException( "Only Internet side is allow to call Get HealthCare Provider By ServiceProvider." );
    }

    @Override
    public List< String > getBereavementGrantRelationships() throws BenefitsServiceException {
        List< String > results = null;

        try {
            SessionFactoryUtil.beginTransaction();
            results = dao.getBereavementGrantRelationships();
            SessionFactoryUtil.commitTransaction();

        }
        catch ( Exception e ) {
            logger.log( Level.SEVERE, "failed getBereavementGrantRelationships()", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return results;
    }
    
    @Override
    public void saveBenefitsProcess(BenefitsProcess benefitsProcess) throws BenefitsServiceException {

        try {
        	if(!SessionFactoryUtil.isTransactionActive())
			{
				SessionFactoryUtil.beginTransaction();
				
				logger.log(Level.INFO,"saveBenefitsProcess is transaction active after:  %s", SessionFactoryUtil.isTransactionActive());
			}
			else
			{
				logger.log(Level.INFO, "close session");
				SessionFactoryUtil.getCurrentSession().close();
				logger.log(Level.INFO, "begin transaction");
				SessionFactoryUtil.getCurrentSession().beginTransaction();
				logger.log(Level.INFO, "after begin transaction");
				logger.log(Level.INFO,"saveBenefitsProcess is transaction active after recommit:  %s", SessionFactoryUtil.isTransactionActive());
			}
            logger.log(Level.INFO, "benefits: "+benefitsProcess);
            dao.saveBenefitsProcess(benefitsProcess);
            SessionFactoryUtil.commitTransaction();

        }
        catch ( Exception e ) {
            logger.log( Level.SEVERE, "failed saveBenefitsProcess()", e );
            SessionFactoryUtil.rollbackTransaction();
        }

    }
    
    public List<BenefitsProcess> retrieveBenefitsProcess(Integer bpmId) throws BenefitsServiceException {

        try {
        	if(!SessionFactoryUtil.isTransactionActive())
			{
				SessionFactoryUtil.beginTransaction();
				
				logger.log(Level.INFO,"retrieveBenefitsProcess is transaction active after: %s", SessionFactoryUtil.isTransactionActive());
			}
			else
			{
				logger.log(Level.INFO, "close session");
				SessionFactoryUtil.getCurrentSession().close();
				logger.log(Level.INFO, "begin transaction");
				SessionFactoryUtil.getCurrentSession().beginTransaction();
				logger.log(Level.INFO, "after begin transaction");
				logger.log(Level.INFO,"retrieveBenefitsProcess is transaction active after recommit:  %s", SessionFactoryUtil.isTransactionActive());
			}
            List<BenefitsProcess> benefitsProcess = null;
            benefitsProcess = dao.retrieveBenefitsProcess(bpmId);
            SessionFactoryUtil.commitTransaction();
            return benefitsProcess;

        }
        catch ( Exception e ) {
            logger.log( Level.SEVERE, "failed saveBenefitsProcess()", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return Collections.emptyList();
    }
        
    public boolean hasActiveBenefitsProcess(String creationPeriod, String status, Date startDate, Date endDate) throws BenefitsServiceException {

    	try {
    		if(!SessionFactoryUtil.isTransactionActive())
			{
				SessionFactoryUtil.beginTransaction();
				
				logger.log(Level.INFO,"hasActiveBenefitsProcess is transaction active after:  %s", SessionFactoryUtil.isTransactionActive());
			}
			else
			{
				logger.log(Level.INFO, "close session");
				SessionFactoryUtil.getCurrentSession().close();
				logger.log(Level.INFO, "begin transaction");
				SessionFactoryUtil.getCurrentSession().beginTransaction();
				logger.log(Level.INFO, "after begin transaction");
				logger.log(Level.INFO,"hasActiveBenefitsProcess is transaction active after recommit:  %s", SessionFactoryUtil.isTransactionActive());
			}
            List<BenefitsProcess> benefitsProcess = null;
            benefitsProcess = dao.hasActiveBenefitsProcess(creationPeriod, status);
            logger.info("number of active records: " +benefitsProcess.size());
            SessionFactoryUtil.commitTransaction();

            boolean hasActiveBenefits = false;
            if(benefitsProcess.size()>0)
            {
            	hasActiveBenefits = true;
            }
            return hasActiveBenefits;

        }
        catch ( Exception e ) {
            logger.log( Level.SEVERE, "failed hasActiveBenefitsProcess()", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return false;
    }
    
    public boolean validateNRIC(String nric){
    	String nricSTList ="JZIHGFEDCBA";
    	String nricFGList ="XWUTRQPNMLK";
    	boolean isValidNRIC = true; 
    	if(nric.length() == 9){
    		logger.log(Level.INFO, "check nric validity");
    		char first = nric.charAt(0);
    		char last = nric.charAt(8);
    		
    	    if(nric.matches("[A-Za-z]\\d{7}[A-Za-z]")){
    	    	int t = ((nric.charAt(1))*2) + ((nric.charAt(2))*7) + ((nric.charAt(3))*6);
    	    	t = t + ((nric.charAt(4))*5) + ((nric.charAt(5))*4) + ((nric.charAt(6))*3);
    	    	t = t + ((nric.charAt(7))*2);
    	    	
    	    	if(first == 'G' || first == 'T'){
    	    		
    	    		t = t + 4;
    	    	}
    	    	
    	    	int r = t%11;
    	    	
    	    	if(first == 'S' || first == 'T'){
    	    		
    	    		if(nricSTList.charAt(r) == last){
    	    			logger.log(Level.INFO, "chekIsValidNRIC: no error");
    	    		}
    	    	} else if(first == 'F' || first == 'G'){
    	    		if(nricFGList.charAt(r) == last){
    	    			logger.log(Level.INFO, "chekIsValidNRIC: no error");
                    }
    	    	} else{
    	    		logger.log(Level.INFO, "chekIsValidNRIC: error");
    	    		isValidNRIC = false;
    	    	}
    	    }
    	} else{
    		isValidNRIC = false;
    	}
    	
    	return isValidNRIC;
    }
    
    public boolean hasPreviousWeddingRecord(List<WeddingGift> resultList){
    			
		logger.log(Level.INFO, "Number of existing Wedding Records: " + resultList.size());
		
		int rejectCount = 0;
		boolean hasPreviousRecord = true;
		
		if(resultList.size() == 0){
			
			logger.log(Level.WARNING, "No existing Wedding Records found, fulfill first criteria");
			hasPreviousRecord = false;
			
		} else{
			
			logger.log(Level.WARNING, "Existing Record found");
			
			for(int j=0; j < resultList.size(); j++){
				
				logger.log(Level.WARNING, "Application Status" + resultList.get(j).getApplicationStatus());
				
				if(resultList.get(j).getApplicationStatus() == ApplicationStatus.REJECTED){
					
					rejectCount++;
                    if ( logger.isLoggable( Level.INFO ) ) {
                        logger.info(String.format("rejCount:  %s", rejectCount));
                        logger.log(Level.INFO, "Previous record rejected, can apply again");
                    }
				} else{
                    logger.log(Level.INFO, "Previous record not rejected cannot apply for new");
					break;
				}
			}
			
			if(rejectCount == resultList.size()){
				hasPreviousRecord = false;
			}
		}
    	
    	return hasPreviousRecord;
    }
    
    public boolean hasPreviousNewbornRecord(List<NewBornGift> resultList){
		
		logger.log(Level.INFO, "Number of existing Newborn Records: " + resultList.size());
		
		int rejectCount = 0;
		boolean hasPreviousRecord = true;
		
		if(resultList.size() == 0){
			
			logger.log(Level.WARNING, "No existing Newborn Records found, fulfill first criteria");
			hasPreviousRecord = false;
			
		} else{
			
			logger.log(Level.WARNING, "Existing Record found");
			
			for(int j=0; j < resultList.size(); j++){
				
				logger.log(Level.WARNING, "Application Status" + resultList.get(j).getApplicationStatus());
				
				if(resultList.get(j).getApplicationStatus() == ApplicationStatus.REJECTED){
					
					rejectCount++;
                    if ( logger.isLoggable( Level.INFO ) ) {
                        logger.info(String.format("rejCount:  %s", rejectCount));
                        logger.log(Level.INFO, "Previous record rejected, can apply again");
                    }
				} else{
                    logger.log(Level.INFO, "Previous record not rejected cannot apply for new");
					break;
				}
			}
			
			if(rejectCount == resultList.size()){
				hasPreviousRecord = false;
			}
		}
    	
    	return hasPreviousRecord;
    }
    
    public boolean hasSpouseApplication(PersonalDetail personal, List<Spouse> spouses){
    	boolean hasSpouseApplication = false;
    	    	
    	if(spouses.size() > 0)
		{
			for(int k=0; k<spouses.size(); k++){

                Date spouseDateOfMarriage = spouses.get(k).getDateOfMarriage();
                Calendar domCalendar = Calendar.getInstance();
                domCalendar.setTime(spouseDateOfMarriage);

				if(spouseDateOfMarriage == null){
					hasSpouseApplication = false;
					logger.log(Level.INFO, "there is no date of marriage");
				} else{
					logger.log(Level.INFO, "date of marriage not null");

					if(spouseDateOfMarriage.compareTo(personal.getEmployment().getDateOfAppointment()) < 0){
						logger.log(Level.INFO, "date of marriage less than date of appointment");
						hasSpouseApplication = false;
					} else{

						if(currentYear - domCalendar.get(Calendar.YEAR) == 0){
							logger.log(Level.INFO, "year of marriage same as this year");
							hasSpouseApplication = true;																					
						} else if (currentYear - domCalendar.get(Calendar.YEAR) == 1){
							
							logger.log(Level.INFO, "year of marriage 1 year ago");
							
							if(currentMonth < domCalendar.get(Calendar.MONTH)){
								logger.log(Level.INFO, "month of marriage after this month");
								hasSpouseApplication = true; 
							}else if(currentMonth == domCalendar.get(Calendar.MONTH)){

								if(currentDate < domCalendar.get(Calendar.DAY_OF_MONTH)){
									logger.info("date of marriage after today\'s date");
									hasSpouseApplication = true; 
								} else{
		    						logger.log(Level.INFO, "date of marriage 1 year or more ago");
		    					}
							} else{
	    						logger.log(Level.INFO, "date of marriage 1 year or more ago");
	    					}
						} else{
							logger.log(Level.INFO, "date of marriage 1 year or more ago");
						}						
					}
				} //end if-else personal.getSpouses().get(k).getDateOfMarriage() == null
			}
		}
    	
    	return hasSpouseApplication;
    }
    
    public boolean hasApplicableNewborn(PersonalDetail personal, List<Child> children){

    	boolean isSuccessful = false;
    	boolean isValid = true;

    	for(int m = 0; m<children.size(); m++){
    		isSuccessful = false;

            Date childDateOfBirth = children.get(m).getDateOfBirth();
            Calendar dobCalendar = Calendar.getInstance();
            dobCalendar.setTime(childDateOfBirth);

    		if( childDateOfBirth == null || children.get(m).getNric() == null || children.get(m).getName() == "" || children.get(m).getName() == null || children.get(m).getIdType() == null || children.get(m).getIdType() == ""){
                isSuccessful = false;
    		} else{
    			if( childDateOfBirth.compareTo(personal.getEmployment().getDateOfAppointment()) < 0) {
    				logger.log(Level.INFO, "date of birth less than date of appointment");
                    isSuccessful = false;
    			} else{
    				if(currentYear - dobCalendar.get(Calendar.YEAR) == 0){

    					logger.log(Level.INFO, "birth year same as this year");
    					isSuccessful = true;
    				} else if (currentYear - dobCalendar.get(Calendar.YEAR) == 1){

    					logger.log(Level.INFO, "year of birth is 1 year ago");
    					
    					if(currentMonth <  dobCalendar.get(Calendar.MONTH)){
    						logger.log(Level.INFO, "birth month after this month");
    						isSuccessful = true; 
    						
    					}else if(currentMonth ==  dobCalendar.get(Calendar.MONTH)){

    						if(currentDate < dobCalendar.get(Calendar.DAY_OF_MONTH)){
    							logger.log(Level.INFO, "birth day after today");
    							isSuccessful = true; 
    						} else{
    							logger.log(Level.INFO, "date of birth 1 year or more ago");
    						}
    					} else{
    						logger.log(Level.INFO, "date of birth 1 year or more ago");
    					}

    				} else{
    					logger.log(Level.INFO, "date of birth 1 year or more ago");
    					isSuccessful = false;
    				}

    				logger.log(Level.INFO, "idtype" + children.get(m).getIdType());
    				logger.log(Level.INFO, "childNric" + children.get(m).getNric());
    				if(!(children.get(m).getIdType().equals("NR")) && !(children.get(m).getIdType().equals("FI")) && !(children.get(m).getIdType().equals("OT")) && !(children.get(m).getIdType().equals("PA"))){
    					logger.log(Level.INFO, "idType not NR, FI, OT, PA");
    					isSuccessful = false;
    				} else{
    					if(children.get(m).getIdType().equals("NR") || children.get(m).getIdType().equals("FI")){

    						isValid=validateNRIC(children.get(m).getNric());
                            if ( logger.isLoggable( Level.INFO ) ) {
                                logger.info(String.format("nricValid: %s", isValid));
                            }
    						if(isValid){
    							isSuccessful = true; 
    						}
    					} else{
    						isSuccessful = false; 
    					}
    				}
    			}
    		}
    	}
    	
    	return isSuccessful;
    }
    
    public boolean saveWeddingDetails(WeddingGift weddingApplications, PersonalDetail personal, List<Spouse> spouses){
    	
    	boolean isSuccessful = true;
    	
    	Date todaysDate = new Date();
    	
    	if(spouses.size() > 0)
		{
			for(int k=0; k<spouses.size(); k++){
				
				if(personal.getSpouses().get(k).getNric() != "" && personal.getSpouses().get(k).getName() != ""){
					
			    	weddingApplications.setMemberNric(personal.getNric());
					weddingApplications.setSpouseName(spouses.get(k).getName());
					weddingApplications.setDateOfMarriage(spouses.get(k).getDateOfMarriage());
					weddingApplications.setSpouseIdType("NR");
					weddingApplications.setCertificateNumber(spouses.get(k).getCertificateNumber());
					weddingApplications.setBank("");
					weddingApplications.setBranchCode("");
					weddingApplications.setAccountNo("");
					weddingApplications.setDateOfSubmission(todaysDate);
					weddingApplications.setApplicationStatus(ApplicationStatus.PENDING);
					weddingApplications.setOtherBank("");
					weddingApplications.setAmountPaid(0.0);
					weddingApplications.setSubmittedBy("System");
					weddingApplications.setSpouseNric(spouses.get(k).getNric());
					weddingApplications.setPaymentDate(null);
					weddingApplications.setRemark("");																						
					weddingApplications.setSupportingDocuments(new ArrayList<SupportingDocument>());
					
					weddingApplications.setApprovalRecords(new ArrayList<ApprovalRecord>());																						
					ApprovalRecord currentApprovalRecord = new ApprovalRecord();
					currentApprovalRecord.setOfficerLevel("-");
					currentApprovalRecord.setOfficerNric("System");
					currentApprovalRecord.setOfficerName("System");
					currentApprovalRecord.setOfficerAction("Supported");
					currentApprovalRecord.setOfficerComments("-");
					currentApprovalRecord.setDateOfCompletion(todaysDate);
	
					weddingApplications.getApprovalRecords().add(0, currentApprovalRecord);
				} else{
					isSuccessful = false;
				}
			}
		}
    	
    	return isSuccessful;
    }
    
    public boolean saveNewbornDetails(NewBornGift newbornApplications, PersonalDetail personal, List<Child> children){
    	
    	boolean isSuccessful = true;
    	
    	for(int m = 0; m<children.size(); m++){
    		
	    	newbornApplications.setMemberNric(personal.getNric());
			newbornApplications.setChildName(children.get(m).getName());
			newbornApplications.setChildIdType(children.get(m).getIdType());
			newbornApplications.setChildDateOfBirth(children.get(m).getDateOfBirth());
			newbornApplications.setBirthCertificateNumber(children.get(m).getNric());
			newbornApplications.setBank("");
			newbornApplications.setBranchCode("");
			newbornApplications.setAccountNo("");
			newbornApplications.setDateOfSubmission(todayDate);
			newbornApplications.setApplicationStatus(ApplicationStatus.PENDING);
			newbornApplications.setOtherBank("");
			newbornApplications.setAmountPaid(0.0);
			newbornApplications.setSubmittedBy("System");
			newbornApplications.setPaymentDate(null);
			newbornApplications.setRemark("");
			newbornApplications.setSupportingDocuments(new ArrayList<SupportingDocument>());
			
			newbornApplications.setApprovalRecords(new ArrayList<ApprovalRecord>());
			ApprovalRecord currentApprovalRecord = new ApprovalRecord();
			currentApprovalRecord.setOfficerLevel("-"); 
			currentApprovalRecord.setOfficerNric("System");
			currentApprovalRecord.setOfficerName("System");
			currentApprovalRecord.setOfficerAction("Supported");
			currentApprovalRecord.setOfficerComments("-");
			currentApprovalRecord.setDateOfCompletion(todayDate);
			
			newbornApplications.getApprovalRecords().add(0, currentApprovalRecord);
			
    	}
    	
    	return isSuccessful;
    }
            
    public boolean autoCreateApplicationsFromHRSystem(){
    	logger.log(Level.INFO,"autoCreateApplicationsFromHRSystem");
    	
    	WeddingGift weddingApplications = new WeddingGift();
            	
    	try {

			List<String> nricList = personnelService.getHRUpdatedPersonnel(new Date()); //(new Date()); format yyyy-MM-dd
            if ( logger.isLoggable( Level.INFO ) ) {
                logger.info(String.format("hr list record:  %s", nricList.size()));
            }
			if(!nricList.isEmpty()){
				
				for (int i=0; i<nricList.size(); i++){

                    if ( logger.isLoggable( Level.INFO ) ) {
                        logger.info(String.format("Checking for NRIC:  %s", nricList.get(i)));
                        logger.log(Level.INFO, "Getting Personal Details");
                    }
					
					try {
						PersonalDetail personal = personnelService.getPersonal(nricList.get(i));
						
						if(personal != null){							
							logger.log(Level.INFO, personal.toString());
							
							Membership membership;
							logger.log(Level.INFO, "Getting Membership Details");
							logger.log(Level.INFO, "Get Membership Details START--- ");
							
							try {
								
								membership = membershipService.getMembership(personal.getNric());
								logger.log(Level.INFO, membership.toString());
								
								if(membership != null){
									Code unitDepartment = codeService.getCode(CodeType.UNIT_DEPARTMENT, personal.getEmployment().getOrganisationOrDepartment());
									logger.log(Level.INFO, unitDepartment.toString());
									logger.log(Level.INFO, "code -----------------------------" + unitDepartment.getType());
									logger.log(Level.INFO, "type -------------------------------" + unitDepartment.getId());
									logger.log(Level.INFO, "getCode for UNIT_DEPARTMENT --- " + unitDepartment.getId());
									
										Code subunit = codeService.getCode(CodeType.SUB_UNIT, personal.getEmployment().getSubunit());
																				
										if(unitDepartment != null){											
											logger.log(Level.INFO, personal.getNric() +": membership details and department found");

											boolean hasEnoughMembership = false; 
											boolean isEligible = false;
											
											logger.log(Level.INFO, "Membership Status: " + membership.getMembershipStatus());
											logger.log(Level.INFO, "Membership Type: " + membership.getMembershipType());
											
											if(membership.getMembershipStatus() == ActivationStatus.ACTIVE && membership.getMembershipType() == MembershipType.ORDINARY){
												logger.log(Level.INFO, "has correct membership status and membership type");
												logger.log(Level.INFO, "Service Type: " + personal.getEmployment().getServiceType());
												
												if(personal.getEmployment().getServiceType().equals("385") || personal.getEmployment().getServiceType().equals("389") || personal.getEmployment().getServiceType().equals("412") || personal.getEmployment().getServiceType().equals("111") || personal.getEmployment().getServiceType().equals("222")){
											        
													logger.log(Level.INFO, "Personnel is SPF Officer");
											        hasEnoughMembership = true;
											        
											    } else if(personal.getEmployment().getServiceType().equals("338") || personal.getEmployment().getServiceType().equals("000")){
													logger.log(Level.INFO, "Personnel is Police Civilian, Checking for Effective Date");
													
													if((membership.getEffectiveDate().getYear()+1) < (currentYear)){
														hasEnoughMembership = true;
														logger.log(Level.INFO, "effective year+1 less than today date");

                                                        if ( logger.isLoggable( Level.INFO ) ) {
                                                            logger.info(String.format("has enough membership %s", hasEnoughMembership));
                                                        }
													} else if ((membership.getEffectiveDate().getYear()+1 == currentYear) && ((membership.getEffectiveDate().getMonth() < currentMonth) ||
                                                            ((membership.getEffectiveDate().getMonth() == currentMonth) &&
                                                                    (membership.getEffectiveDate().getDate() <= currentDate || membership.getEffectiveDate().getDate() == currentDate+1)))){
															hasEnoughMembership = true;
													} //end if-else membership.getEffectiveDate
													
													if(!hasEnoughMembership){
											            logger.log(Level.INFO, "Effective Date not more than 12 months");
													} //end if hasEnoughMembership
												} else { 
													hasEnoughMembership = false;
												} //end if-else personal service type

												if(hasEnoughMembership){
													logger.log(Level.INFO, "has valid membership");
													logger.log(Level.INFO, "Department: " + unitDepartment.getDescription());
													
													if(unitDepartment.getDescription().toUpperCase().equals("EXTERNAL AGENCIES")){
														
														logger.log(Level.INFO, "Check if ISD Member");
														
														if(subunit == null){															
															logger.log(Level.INFO, "Subunit is empty, do not create application");
														}else if(subunit.getDescription().equals("MHA")){															
															logger.log(Level.INFO, "subunit: "+ subunit.getDescription());
															logger.log(Level.INFO, "ISD member: do not create application");
														}else{
															logger.log(Level.INFO, "Subunit: " + subunit.getDescription());	
															isEligible = true;
														}

													} else{
														isEligible = true;														
													}
                                                    if ( logger.isLoggable( Level.INFO ) ) {
                                                        logger.info(String.format("is eligible %s", isEligible));
                                                    }

													if(isEligible){
														try {

															List<WeddingGift> weddingResultList = searchWeddingGift(personal.getNric());

															boolean hasPreviousRecord = hasPreviousWeddingRecord(weddingResultList);

                                                            if ( logger.isLoggable( Level.INFO ) ) {
                                                                logger.info(String.format("hasPreviousRecord %s", hasPreviousRecord));
                                                            }

															if (!hasPreviousRecord){
																//has spouse application
																boolean hasSpouseApplication = hasSpouseApplication(personal, personal.getSpouses());

																if(hasSpouseApplication){
																	logger.log(Level.INFO, "Save wedding details");

																	hasSpouseApplication = saveWeddingDetails(weddingApplications, personal, personal.getSpouses());

																}
                                                                if ( logger.isLoggable( Level.INFO ) ) {
                                                                    logger.info(String.format("hasApplicableWedding %s", hasSpouseApplication));
                                                                }

																if(hasSpouseApplication){
																	//create wedding application
																	logger.log(Level.INFO, "Create wedding application");
																	addWeddingGift(weddingApplications, WSSubject.getCallerPrincipal()); 
																}

															} //end hasPreviousRecord == false

															//check for applicable newborn 
															logger.log(Level.INFO, "check applicable newborn");
															NewBornGift newbornApplications = new NewBornGift();
															boolean isSuccessful = false;
															boolean hasApplicableNewborn = false;

															hasApplicableNewborn = hasApplicableNewborn(personal, personal.getChildren());
                                                            if ( logger.isLoggable( Level.INFO ) ) {
                                                                logger.info(String.format("hasApplicableNewborn %s", hasApplicableNewborn));
                                                            }
															if(hasApplicableNewborn){
																isSuccessful = saveNewbornDetails(newbornApplications, personal, personal.getChildren());

															}


                                                            if ( logger.isLoggable( Level.INFO ) ) {
                                                                logger.info(String.format("isSuccessful %s", isSuccessful));
                                                            }
															boolean hasPreviousNewbornRecord = false;
															if(isSuccessful){
																List<NewBornGift> newbornResultList = searchNewBorn(personal.getNric());
																if(newbornResultList.size() != 0){
																	hasPreviousNewbornRecord = hasPreviousNewbornRecord(newbornResultList);
                                                                    if ( logger.isLoggable( Level.INFO ) ) {
                                                                        logger.info(String.format("hasPreviousNewbornRecord %s", hasPreviousNewbornRecord));
                                                                    }
																}

																if(!hasPreviousNewbornRecord){
																	addNewBorn(newbornApplications, WSSubject.getCallerPrincipal());
																}
															}


														} catch (BenefitsServiceException e) {
															logger.log(Level.INFO, "no wedding result");
														} catch (NullPointerException e){
															logger.log(Level.INFO, "null");
														}
													}
												}

											} else{
												logger.log(Level.INFO, "not correct membership type");
											}
											
										} //end if getMembershipStatus

								} //end if membership != null

							} catch (MembershipServiceException e) {
								logger.log(Level.INFO, "no membership");
							} 

						} else{
							logger.log(Level.INFO, "no personal details"); // end if personal != null
						}
												
					} catch (AccessDeniedException e) {
						logger.log(Level.INFO, "no personal details"); 
					} //end try catch
				} //end for loop
								
			} // end if
			
		} catch (PersonnelServiceException e) {
			logger.log(Level.INFO, "no personal details"); 
		} catch (IndexOutOfBoundsException i) {
			logger.log(Level.INFO, "List empty");
		}
    	return true;
    }
}
