package com.stee.spfcore.service.benefits;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.stee.spfcore.model.benefits.BenefitsProcess;
import com.stee.spfcore.model.benefits.BereavementGrant;
import com.stee.spfcore.model.benefits.GrantBudget;
import com.stee.spfcore.model.benefits.HealthCareProvider;
import com.stee.spfcore.model.benefits.NewBornGift;
import com.stee.spfcore.model.benefits.WeddingGift;
import com.stee.spfcore.model.benefits.WeightMgmtSubsidy;
import com.stee.spfcore.security.AccessDeniedException;
import com.stee.spfcore.vo.benefits.BenefitsCriteria;
import com.stee.spfcore.vo.benefits.BereavementGrantDetails;
import com.stee.spfcore.vo.benefits.DashboardStatus;
import com.stee.spfcore.vo.benefits.GrantBudgetCriteria;
import com.stee.spfcore.vo.benefits.NewBornGiftDetails;
import com.stee.spfcore.vo.benefits.WeddingGiftDetails;

public interface IBenefitsService {

    /**
     * Search the bereavement grant application by nric.
     * 
     * @param nric
     * @return
     * @throws BenefitsServiceException
     */
    public List< BereavementGrant > searchBereavementGrant( String nric ) throws BenefitsServiceException, AccessDeniedException;

    /**
     * Get the bereavement grant application by login ID.
     * 
     * @param nric
     * @return BereavementGrant or null if detail not found.
     * @throws BenefitsServiceException
     *             Exception while retrieving bereavement grant application.
     */
    public List< BereavementGrant > searchBereavementGrantForLoginID( String nric ) throws BenefitsServiceException, AccessDeniedException;

    /**
     * Get the bereavement grant application by reference number.
     * 
     * @param nric
     * @return BereavementGrant or null if detail not found.
     * @throws BenefitsServiceException
     *             Exception while retrieving bereavement grant application.
     */
    
    public BereavementGrant getBereavementGrant( String referenceNumber ) throws BenefitsServiceException, AccessDeniedException;

    /**
     * Add new bereavement grant application.
     * 
     * @param BereavementGrant
     *            the bereavement grant application detail to be added.
     * @throws BenefitsServiceException
     *             Exception while adding the bereavement grant application.
     */
    public void addBereavementGrant( BereavementGrant bereavementGrant, String requester ) throws BenefitsServiceException, AccessDeniedException;

    /**
     * Updating bereavement grant application detail
     * 
     * @param BereavementGrant
     *            the bereavement grant application detail to be updated.
     * @throws BenefitsServiceException
     *             Exception while updating the bereavement grant application
     *             detail.
     */
    public void updateBereavementGrant( BereavementGrant bereavementGrant, String requester ) throws BenefitsServiceException, AccessDeniedException;

    /**
     * Get the new born application by nric.
     * 
     * @param nric
     * @return NewBorn or null if detail not found.
     * @throws BenefitsServiceException
     *             Exception while retrieving new born application.
     */

    /**
     * Search wedding gift application by nric.
     * 
     * @param nric
     * @return List of WeddingGift.
     * @throws BenefitsServiceException
     *             Exception while retrieving wedding gift application.
     */
    public List< WeddingGift > searchWeddingGift( String nric ) throws BenefitsServiceException, AccessDeniedException;
    
    /**
     * Search wedding gift application by nric.
     * 
     * @param nric
     * @return List of WeddingGift.
     * @throws BenefitsServiceException
     *             Exception while retrieving wedding gift application.
     */
    public List< WeddingGift > searchWeddingGiftForLoginID( String nric ) throws BenefitsServiceException, AccessDeniedException;
    

    /**
     * Get the wedding gift application by reference number
     * 
     * @param referenceNumber
     * @return WeddingGift or null
     * @throws BenefitsServiceException
     */
    public WeddingGift getWeddingGift( String referenceNumber ) throws BenefitsServiceException, AccessDeniedException;

    /**
     * Add new wedding gift application.
     * 
     * @param weddingGift
     *            the wedding gift application detail to be added.
     * @throws BenefitsServiceException
     *             Exception while adding the wedding gift application.
     */
    public void addWeddingGift( WeddingGift weddingGift, String requester ) throws BenefitsServiceException, AccessDeniedException;

    /**
     * Updating wedding gift application detail
     * 
     * @param weddingGift
     *            the wedding gift application detail to be updated.
     * @throws BenefitsServiceException
     *             Exception while updating the wedding gift application detail.
     */
    public void updateWeddingGift( WeddingGift weddingGift, String requester ) throws BenefitsServiceException, AccessDeniedException;

    /**
     * Search new born application by nric.
     * 
     * @param referenceNumber
     * @return NewBorn or null if detail not found.
     * @throws BenefitsServiceException
     *             Exception while retrieving new born application.
     */
    public List< NewBornGift > searchNewBorn( String nric ) throws BenefitsServiceException, AccessDeniedException;
    
    /**
     * Search new born application by login ID.
     * 
     * @param referenceNumber
     * @return NewBorn or null if detail not found.
     * @throws BenefitsServiceException
     *             Exception while retrieving new born application.
     */
    public List< NewBornGift > searchNewBornForLoginID( String nric ) throws BenefitsServiceException, AccessDeniedException;

    /**
     * Get the new born application by reference number.
     * 
     * @param referenceNumber
     * @return NewBorn or null if detail not found.
     * @throws BenefitsServiceException
     *             Exception while retrieving new born application.
     */
    public NewBornGift getNewBorn( String referenceNumber ) throws BenefitsServiceException, AccessDeniedException;

    /**
     * Add new new born application.
     * 
     * @param newBorn
     *            the new born application detail to be added.
     * @throws BenefitsServiceException
     *             Exception while adding the new born application.
     */
    public void addNewBorn( NewBornGift newBornGift, String requester ) throws BenefitsServiceException, AccessDeniedException;

    /**
     * Updating new born application detail
     * 
     * @param newBorn
     *            the new born application detail to be updated.
     * @throws BenefitsServiceException
     *             Exception while updating the new born application detail.
     */
    public void updateNewBorn( NewBornGift newBornGift, String requester ) throws BenefitsServiceException, AccessDeniedException;

    /**
     * For Internet side:
     * <ol>
     * <li>Download files for application that need to be transfer to Intranet side from FEB into a folder with reference number as folder name.</li>
     * <li>Zip the folder into a zip file with reference number as file name.</li>
     * <li>Move the zip file into the SFTP transfer folder.</li>
     * </ol>
     * For Intranet side:
     * <ol>
     * <li>Move the zip file from the SFTP transfer folder into working folder.</li>
     * <li>Unzip the zip file</li>
     * <li>Upload the files in the unzip folder into BPM.</li>
     * </ol>
     */
    public void processSupportingDocuments() throws BenefitsServiceException, AccessDeniedException;

    /**
     * Search for New Born Gift Applications by Latest Approval Records
     * Criteria.
     * 
     * @param benefitsCriteria
     * @return
     * @throws BenefitsServiceException
     */
    public List< NewBornGiftDetails > searchNewBornByApprovalsCriteria( BenefitsCriteria benefitsCriteria ) throws BenefitsServiceException;

    /**
     * Search for Wedding Gift Applications by Latest Approval Records Criteria.
     * 
     * @param benefitsCriteria
     * @return
     * @throws BenefitsServiceException
     */
    public List< WeddingGiftDetails > searchWeddingByApprovalsCriteria( BenefitsCriteria benefitsCriteria ) throws BenefitsServiceException;

    /**
     * Search for Bereavement Grant Applications by Latest Approval Records
     * Criteria.
     * 
     * @param benefitsCriteria
     * @return
     * @throws BenefitsServiceException
     */
    public List< BereavementGrantDetails > searchBereavementGrantByApprovalsCriteria( BenefitsCriteria benefitsCriteria ) throws BenefitsServiceException;

    /**
     * Get Grant Budget by Id.
     * 
     * @param id
     * @return
     * @throws BenefitsServiceException
     */
    public GrantBudget getGrantBudget( Long id ) throws BenefitsServiceException;

    /**
     * Search Grant Budget Records by Criteria.
     * 
     * @param budgetCriteria
     * @return
     * @throws BenefitsServiceException
     */
    public List< GrantBudget > searchGrantBudgetByCriteria( GrantBudgetCriteria budgetCriteria ) throws BenefitsServiceException;

    /**
     * Add Grant Budget Record.
     * 
     * @param grantBudget
     * @throws BenefitsServiceException
     */
    public void addGrantBudget( GrantBudget grantBudget, String requester ) throws BenefitsServiceException;

    /**
     * Update Existing Grant Budget Record.
     * 
     * @param grantBudget
     * @throws BenefitsServiceException
     */
    public void updateGrantBudget( GrantBudget grantBudget, String requester ) throws BenefitsServiceException;

    public void saveBereavementGrant( BereavementGrant bereavementGrant, String requester ) throws BenefitsServiceException, AccessDeniedException;

    public void saveNewBorn( NewBornGift newBornGift, String requester ) throws BenefitsServiceException, AccessDeniedException;

    public void saveWeddingGift( WeddingGift weddingGift, String requester ) throws BenefitsServiceException, AccessDeniedException;

    public Double getTotalBereavementGrantAmountPaid( BenefitsCriteria benefitsCriteria ) throws BenefitsServiceException;

    public Double getTotalNewBornGrantAmountPaid( BenefitsCriteria benefitsCriteria ) throws BenefitsServiceException;

    public Double getTotalWeddingGrantAmountPaid( BenefitsCriteria benefitsCriteria ) throws BenefitsServiceException;

    /**
     * 
     * @Title: searchCountBereavementGrant
     * @Description: Search count of bereavement grant application
     * @param @param benefitsCriteria
     * @param @return
     * @param @throws BenefitsServiceException
     * @return Long
     * @throws
     */
    public Long searchCountBereavementGrant( String deceasedIdType, String deceasedNric, String deathCertificateNumber, String referenceNumber ) throws BenefitsServiceException;

    /**
     * 
     * @Title: searchBereavementGrant
     * @Description: Search Bereavement Grant by BenefitsCriteria.
     * @param @param benefitsCriteria
     * @param @return
     * @return List<CommonBenefitsDetails>
     * @throws
     */
    public List< BereavementGrantDetails > searchBereavementGrant( BenefitsCriteria benefitsCriteria ) throws BenefitsServiceException;

    /**
     * 
     * @Title: searchNewBorn
     * @Description: Search New Born by BenefitsCriteria.
     * @param @param benefitxsCriteria
     * @param @return
     * @return List<NewBornGiftDetails>
     * @throws
     */
    public List< NewBornGiftDetails > searchNewBorn( BenefitsCriteria benefitsCriteria ) throws BenefitsServiceException;

    /**
     * 
     * @Title: searchWeddingGift
     * @Description: Search Wedding gift by BenefitsCriteria.
     * @param @param benefitsCriteria
     * @param @return
     * @return List<WeddingGiftDetails>
     * @throws
     */
    public List< WeddingGiftDetails > searchWeddingGift( BenefitsCriteria benefitsCriteria ) throws BenefitsServiceException;

    /**
     * 
     * @Title: searchWeddingGift
     * @Description: Search Wedding gift by BenefitsCriteria.
     * @param @param benefitsCriteria
     * @param @return
     * @return List<WeddingGiftDetails>
     * @throws
     */
    public List< DashboardStatus > RetrieveDashboardStatus() throws BenefitsServiceException;
    
    /**
     * 
     * @Title: getWeightMgmtSubsidy
     * @Description: get Weight Management Subsidy By ReferenceNumber
     * @param @param referenceNumber
     * @param @return
     * @return WeightMgmtSubsidy
     * @throws
     */
    public WeightMgmtSubsidy getWeightMgmtSubsidy( String referenceNumber ) throws BenefitsServiceException, AccessDeniedException;

    /**
     * 
     * @Title: searchWeightMgmtSubsidyByNricAndYear
     * @Description: search Weight Management Subsidy By Nric And Year
     * @param @param nric
     * @param @param string
     * @param @return
     * @return List<WeightMgmtSubsidy>
     * @throws
     */
    public List< WeightMgmtSubsidy > searchWeightMgmtSubsidyByNric( String nric, Calendar today ) throws BenefitsServiceException, AccessDeniedException;

    /**
     * 
     * @Title: updateWeightMgmtSubsidy
     * @Description: Update Weight Mgmt Subsidy
     * @param @param weightMgmtSubsidy
     * @return void
     * @throws
     */
    public void updateWeightMgmtSubsidy( WeightMgmtSubsidy weightMgmtSubsidy, String requester ) throws BenefitsServiceException, AccessDeniedException;

    /**
     * 
     * @Title: addWeightMgmtSubsidy
     * @Description: Add Weight Mgmt Subsidy
     * @param @param weightMgmtSubsidy
     * @return void
     * @throws
     */
    public void addWeightMgmtSubsidy( WeightMgmtSubsidy weightMgmtSubsidy, String requester ) throws BenefitsServiceException, AccessDeniedException;

    /**
     * 
     * @Title: GetHealthCareProviderList
     * @Description: Get HealthCare Provider List
     * @param
     * @return List<HealthCareProvider>
     * @throws
     */
    public List< HealthCareProvider > getHealthCareProviderList() throws BenefitsServiceException;

    /**
     * 
     * @Title: GetHealthCareProviderByServiceProvider
     * @Description: Get HealthCareProviderByServiceProvider
     * @param @return
     * @param @throws BenefitsServiceException
     * @return HealthCareProvider
     * @throws
     */
    public HealthCareProvider getHealthCareProviderByServiceProvider( String serviceProvider ) throws BenefitsServiceException;

    public List< String > getBereavementGrantRelationships() throws BenefitsServiceException;
    
    /**
     * @param benefitsProcess
     * @throws BenefitsServiceException
     */
    public void saveBenefitsProcess(BenefitsProcess benefitsProcess) throws BenefitsServiceException;
    
    public List<BenefitsProcess>retrieveBenefitsProcess(Integer bpmProcessId) throws BenefitsServiceException;
    
    public boolean hasActiveBenefitsProcess(String creationPeriod, String status, Date startDate, Date endDate) throws BenefitsServiceException;
    
    public boolean autoCreateApplicationsFromHRSystem ();

}
