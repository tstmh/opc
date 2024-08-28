package com.stee.spfcore.webapi.model.sag;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;

import com.stee.spfcore.webapi.model.ApplicationStatus;
import com.stee.spfcore.webapi.model.benefits.ApprovalRecord;
import com.stee.spfcore.webapi.model.benefits.SupportingDocument;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"SAG_APPLICATION\"", schema = "\"SPFCORE\"")
@XStreamAlias("SAGApplication")
@Audited
public class SAGApplication {

	@Id
	@Column(name = "\"REFERENCE_NUMBER\"", length = 50)
	private String referenceNumber;

	@Column(name = "\"FINANCIAL_YEAR\"", length = 10)
	private String financialYear;

	@Column(name = "\"AWARD_TYPE\"", length = 50)
	private String awardType;

	@Enumerated(EnumType.STRING)
	@Column(name = "\"APPLICATION_STATUS\"", length = 50)
	private ApplicationStatus applicationStatus;

	@Column(name = "\"MEMBER_NRIC\"", length = 10)
	private String memberNric;

	@Column(name = "\"GROSS_SALARY\"")
	private Double grossSalary; // Enter only for NON-SPF Officers.

	@Column(name = "\"SPECIAL_ALLOWANCE\"")
	private Double specialAllowance;

	@Column(name = "\"VSC_DUTY_HOURS\"")
	private Double vscDutyHours;

	@Column(name = "\"VSC_SERVICE_YEARS\"")
	private Double vscServiceYears;
	
	@Column(name = "\"VSC_OCCUPATION\"", length = 100)
	private String vscOccupation;
	
	@Column(name = "\"VSC_COMPANY_NAME\"", length = 100)
	private String vscCompanyName;

	@Column(name = "\"CHILD_NRIC\"", length = 50)
	private String childNric;

	@Column(name = "\"CHILD_NAME\"", length = 100)
	private String childName;

	@Column(name = "\"CHILD_NAME_AMENDED\"", length = 100)
	private String childNameAmended;

	@Temporal(TemporalType.DATE)
	@Column(name = "\"CHILD_DOB\"")
	private Date childDateOfBirth;

	@Column(name = "\"CHILD_EMAIL\"", length = 256)
	private String childEmail;

	@Column(name = "\"CHILD_CURR_SCHOOL\"", length = 60)
	private String childCurrentSchool;
	
	@Column(name = "\"CHILD_OTHER_CURR_SCHOOL\"", length = 100)
	private String childOtherCurrentSchool;

	@Column(name = "\"CHILD_HIGHEST_EDU_LVL\"", length = 30)
	private String childHighestEduLevel;

	@Column(name = "\"CHILD_LAST_EXAM_STATUS\"", length = 4)
	private String childLastExamStatus;

	@Column(name = "\"CHILD_NEW_EDU_LVL\"", length = 30)
	private String childNewEduLevel;

	@Column(name = "\"CHILD_PSLE_SCORE\"", length = 10)
	private String childPsleScore;

	@Column(name = "\"CHILD_CGPA\"")
	private Double childCgpa;

	@Column(name = "\"BANK_RECIPIENT_NAME\"", length = 100)
	private String bankRecipientName;

	@Column(name = "\"BANK\"", length = 100)
	private String bank;

	@Column(name = "\"OTHER_BANK\"", length = 100)
	private String otherBank;

	@Column(name = "\"BRANCH_CODE\"")
	private String branchCode;

	@Column(name = "\"ACCOUNT_NO\"")
	private String accountNo;
	
	@Column(name = "\"PAYMENT_ADVICE_EMAIL\"", length = 256)
	private String paymentAdviceEmail;

	@Column(name = "\"SELF_DECLARATION\"")
	private boolean selfDeclarationCheck;

	@Column(name = "\"ALLOW_PRD_TO_EMAIL_CHILD\"")
	private boolean allowChildEmailForPRD;

	@Column(name = "\"IS_VSC_OFFICER\"")
	private boolean vscOfficerCheck;

	@Column(name = "\"VSC_DUTY_HOURS_CHECK\"")
	private boolean vscDutyHoursCheck;

	@Column(name = "\"VSC_SERVICE_YEARS_CHECK\"")
	private boolean vscServiceYearsCheck;

	@Column(name = "\"VSC_SUPP_DOCS_CHECK\"")
	private boolean vscSuppDocsCheck;

	@Column(name = "\"IS_VSC_HEAD_RECOMMENDED\"")
	private boolean vscHeadRecommendation;

	@Temporal(TemporalType.DATE)
	@Column(name = "\"SUBMISSION_DATE\"")
	private Date dateOfSubmission;

	@Column(name = "\"SUBMITTED_BY\"", length = 50)
	private String submittedBy;

	@Column(name = "\"REMARKS\"", length = 2000)
	private String remarks;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "\"REFERENCE_NUMBER\"")
	@Fetch(value = FetchMode.SUBSELECT)
	private List<SAGAcademicResults> academicResults;

//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "\"REFERENCE_NUMBER\"")
	@Fetch(value = FetchMode.SUBSELECT)
	private List<SAGCoCurricularActivity> childCcaDetails;

	@Column(name = "\"MUSIC_ARTS_SPECIAL\"", length = 1000)
	private String childMusicArtsSpecialDetails;

	@Column(name = "\"COMMENDATION_DETAILS\"", length = 1000)
	private String childCommendationDetails;

	//@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER, orphanRemoval = true)
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "\"REFERENCE_NUMBER\"")
	@Fetch(value = FetchMode.SUBSELECT)
	private List<SAGFamilyBackground> childFamBackgroundDetails;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "\"SAG_REFERENCE_NUMBER\"")
	@Fetch(value = FetchMode.SUBSELECT)
	//@Index(name = "INDEX_APPLICATION_SUPPORTING_DOCUMENT_SAG_RER_NUM", columnNames = { "SAG_REFERENCE_NUMBER" })
	private List<SupportingDocument> supportingDocuments;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "\"SAG_REFERENCE_NUMBER\"")
	@Fetch(value = FetchMode.SUBSELECT)
	//@Index(name = "INDEX_APPLICATION_APPROVAL_RECORD_SAG_RER_NUM", columnNames = { "SAG_REFERENCE_NUMBER" })
	private List<ApprovalRecord> approvalRecords;

	@Column(name = "\"SEQUENCE_NUMBER\"", length= 5)
	private String sequenceNumber;

	@Column(name = "\"CHEQUE_NUMBER\"", length = 20)
	private String chequeNumber;

	@Temporal(TemporalType.DATE)
	@Column(name = "\"CHEQUE_UPDATED_DATE\"")
	private Date chequeUpdatedDate;

	@Column(name = "\"CHEQUE_UPDATED_BY\"", length = 100)
	private String chequeUpdatedBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "\"CHEQUE_VALUE_DATE\"")
	private Date chequeValueDate;
	
	@Column(name = "\"ON_BEHALF_REASON\"", length = 100)
	private String onBehalfReason;
	
	@Column(name = "\"ON_BEHALF_OTHERS_REASON\"", length = 100)
	private String onBehalfOthersReason;

	@Column(name = "\"AMOUNT_PAID\"")
	private Double amountPaid;
	
	@Column(name = "\"MERIT_IN_DIPLOMA\"", length = 3)
	private String meritInDiploma;
	
	@Column(name = "\"MERIT_IN_NIT_HNIT\"", length = 3)
	private String meritInNitecHNitec;
	
	@Column(name = "\"MAILING_ADDRESS\"", length = 500)
	private String mailingAddress;

	@Column(name = "\"PREFERRED_PAYMENT_MODE\"", length = 20)
	private String preferredPaymentMode;
	
	@Column(name = "\"PAYNOW_RECIPIENT_NAME\"", length = 140)
	private String paynowRecipientName;
	
	@Column(name = "\"PAYNOW_PROXY_TYPE\"", length = 20)
	private String paynowProxyType;
	
	@Column(name = "\"PAYNOW_PROXY_VALUE\"", length = 20)
	private String paynowProxyValue;
	
	/*@Column(name = "\"NOT_OTHER_RECIPIENT\"")
	private boolean notOtherRecipient;*/
	
	@Column(name = "\"NO_NATIONAL_SERVICE\"")
	private boolean noNationalService;
	
	@Column(name = "\"INCOMPLETE_APP_NOT_CONSIDERED\"")
	private boolean incompleteAppNotConsidered;
	
	@Column(name = "\"NO_DISCREPENCY_FINANCIAL_INFORMATION\"")
	private boolean noDiscrepencyFinancialInformation;
	
	
	public SAGApplication() {
		super();
	}

	public SAGApplication( String financialYear, String awardType,
			ApplicationStatus applicationStatus, String memberNric,
			Double grossSalary, Double specialAllowance, Double vscDutyHours,
			Double vscServiceYears, String vscOccupation,
			String vscCompanyName, String childNric, String childName,
			String childNameAmended, Date childDateOfBirth, String childEmail,
			String childCurrentSchool, String childOtherCurrentSchool,
			String childHighestEduLevel, String childLastExamStatus,
			String childNewEduLevel, String childPsleScore, Double childCgpa,
			String bankRecipientName, String bank, String otherBank,
			String branchCode, String accountNo, String paymentAdviceEmail, boolean selfDeclarationCheck,
			boolean allowChildEmailForPRD, boolean vscOfficerCheck,
			boolean vscDutyHoursCheck, boolean vscServiceYearsCheck,
			boolean vscSuppDocsCheck, boolean vscHeadRecommendation,
			Date dateOfSubmission, String submittedBy, String remarks,
			List<SAGAcademicResults> academicResults,
			List<SAGCoCurricularActivity> childCcaDetails,
			String childMusicArtsSpecialDetails,
			String childCommendationDetails,
			List<SAGFamilyBackground> childFamBackgroundDetails,
			List<SupportingDocument> supportingDocuments,
			List<ApprovalRecord> approvalRecords, String sequenceNumber,
			String chequeNumber, Date chequeUpdatedDate,
			String chequeUpdatedBy, Date chequeValueDate,
			String onBehalfReason, String onBehalfOthersReason,
			Double amountPaid, String meritInDiploma, String meritInNitecHNitec,
			String mailingAddress, String preferredPaymentMode, 
			String paynowRecipientName, String paynowProxyType, String paynowProxyValue,boolean incompleteAppNotConsidered,
			boolean noNationalService, boolean noDiscrepencyFinancialInformation ) {
		super();
		this.financialYear = financialYear;
		this.awardType = awardType;
		this.applicationStatus = applicationStatus;
		this.memberNric = memberNric;
		this.grossSalary = grossSalary;
		this.specialAllowance = specialAllowance;
		this.vscDutyHours = vscDutyHours;
		this.vscServiceYears = vscServiceYears;
		this.vscOccupation = vscOccupation;
		this.vscCompanyName = vscCompanyName;
		this.childNric = childNric;
		this.childName = childName;
		this.childNameAmended = childNameAmended;
		this.childDateOfBirth = childDateOfBirth;
		this.childEmail = childEmail;
		this.childCurrentSchool = childCurrentSchool;
		this.childOtherCurrentSchool = childOtherCurrentSchool;
		this.childHighestEduLevel = childHighestEduLevel;
		this.childLastExamStatus = childLastExamStatus;
		this.childNewEduLevel = childNewEduLevel;
		this.childPsleScore = childPsleScore;
		this.childCgpa = childCgpa;
		this.bankRecipientName = bankRecipientName;
		this.bank = bank;
		this.otherBank = otherBank;
		this.branchCode = branchCode;
		this.accountNo = accountNo;
		this.paymentAdviceEmail = paymentAdviceEmail;
		this.selfDeclarationCheck = selfDeclarationCheck;
		this.allowChildEmailForPRD = allowChildEmailForPRD;
		this.vscOfficerCheck = vscOfficerCheck;
		this.vscDutyHoursCheck = vscDutyHoursCheck;
		this.vscServiceYearsCheck = vscServiceYearsCheck;
		this.vscSuppDocsCheck = vscSuppDocsCheck;
		this.vscHeadRecommendation = vscHeadRecommendation;
		this.dateOfSubmission = dateOfSubmission;
		this.submittedBy = submittedBy;
		this.remarks = remarks;
		this.academicResults = academicResults;
		this.childCcaDetails = childCcaDetails;
		this.childMusicArtsSpecialDetails = childMusicArtsSpecialDetails;
		this.childCommendationDetails = childCommendationDetails;
		this.childFamBackgroundDetails = childFamBackgroundDetails;
		this.supportingDocuments = supportingDocuments;
		this.approvalRecords = approvalRecords;
		this.sequenceNumber = sequenceNumber;
		this.chequeNumber = chequeNumber;
		this.chequeUpdatedDate = chequeUpdatedDate;
		this.chequeUpdatedBy = chequeUpdatedBy;
		this.chequeValueDate = chequeValueDate;
		this.onBehalfReason = onBehalfReason;
		this.onBehalfOthersReason = onBehalfOthersReason;
		this.amountPaid = amountPaid;
		this.meritInDiploma = meritInDiploma;
		this.meritInNitecHNitec = meritInNitecHNitec;
		this.mailingAddress = mailingAddress;
		this.preferredPaymentMode = preferredPaymentMode;
		this.paynowRecipientName = paynowRecipientName;
		this.paynowProxyType = paynowProxyType;
		this.paynowProxyValue = paynowProxyValue; 
		//this.notOtherRecipient = notOtherRecipient;
		this.noNationalService = noNationalService;
		this.incompleteAppNotConsidered = incompleteAppNotConsidered;
		this.noDiscrepencyFinancialInformation = noDiscrepencyFinancialInformation;		
	}
	
	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber( String referenceNumber ) {
		this.referenceNumber = referenceNumber;
	}

	public String getAwardType() {
		return awardType;
	}

	public void setAwardType( String awardType ) {
		this.awardType = awardType;
	}

	public ApplicationStatus getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus( ApplicationStatus applicationStatus ) {
		this.applicationStatus = applicationStatus;
	}

	public String getMemberNric() {
		return memberNric;
	}

	public void setMemberNric( String memberNric ) {
		this.memberNric = memberNric;
	}

	public Double getGrossSalary() {
		return grossSalary;
	}

	public void setGrossSalary( Double grossSalary ) {
		this.grossSalary = grossSalary;
	}

	public Double getSpecialAllowance() {
		return specialAllowance;
	}

	public void setSpecialAllowance( Double specialAllowance ) {
		this.specialAllowance = specialAllowance;
	}

	public Double getVscDutyHours() {
		return vscDutyHours;
	}

	public void setVscDutyHours( Double vscDutyHours ) {
		this.vscDutyHours = vscDutyHours;
	}

	public Double getVscServiceYears() {
		return vscServiceYears;
	}

	public void setVscServiceYears( Double vscServiceYears ) {
		this.vscServiceYears = vscServiceYears;
	}

	public String getChildNric() {
		return childNric;
	}

	public void setChildNric( String childNric ) {
		this.childNric = childNric;
	}

	public String getChildName() {
		return childName;
	}

	public void setChildName( String childName ) {
		this.childName = childName;
	}

	public String getChildNameAmended() {
		return childNameAmended;
	}

	public void setChildNameAmended( String childNameAmended ) {
		this.childNameAmended = childNameAmended;
	}

	public Date getChildDateOfBirth() {
		return childDateOfBirth;
	}

	public void setChildDateOfBirth( Date childDateOfBirth ) {
		this.childDateOfBirth = childDateOfBirth;
	}

	public String getChildEmail() {
		return childEmail;
	}

	public void setChildEmail( String childEmail ) {
		this.childEmail = childEmail;
	}

	public String getChildCurrentSchool() {
		return childCurrentSchool;
	}

	public void setChildCurrentSchool( String childCurrentSchool ) {
		this.childCurrentSchool = childCurrentSchool;
	}

	public String getChildHighestEduLevel() {
		return childHighestEduLevel;
	}

	public void setChildHighestEduLevel( String childHighestEduLevel ) {
		this.childHighestEduLevel = childHighestEduLevel;
	}

	public String getChildLastExamStatus() {
		return childLastExamStatus;
	}

	public void setChildLastExamStatus( String childLastExamStatus ) {
		this.childLastExamStatus = childLastExamStatus;
	}

	public String getChildNewEduLevel() {
		return childNewEduLevel;
	}

	public void setChildNewEduLevel( String childNewEduLevel ) {
		this.childNewEduLevel = childNewEduLevel;
	}

	public String getChildPsleScore() {
		return childPsleScore;
	}

	public void setChildPsleScore( String childPsleScore ) {
		this.childPsleScore = childPsleScore;
	}

	public Double getChildCgpa() {
		return childCgpa;
	}

	public void setChildCgpa( Double childCgpa ) {
		this.childCgpa = childCgpa;
	}

	public String getBankRecipientName() {
		return bankRecipientName;
	}

	public void setBankRecipientName( String bankRecipientName ) {
		this.bankRecipientName = bankRecipientName;
	}

	public String getBank() {
		return bank;
	}

	public void setBank( String bank ) {
		this.bank = bank;
	}

	public String getOtherBank() {
		return otherBank;
	}

	public void setOtherBank( String otherBank ) {
		this.otherBank = otherBank;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode( String branchCode ) {
		this.branchCode = branchCode;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo( String accountNo ) {
		this.accountNo = accountNo;
	}
	
	public String getPaymentAdviceEmail() {
		return paymentAdviceEmail;
	}

	public void setPaymentAdviceEmail( String paymentAdviceEmail ) {
		this.paymentAdviceEmail = paymentAdviceEmail;
	}

	public boolean isSelfDeclarationCheck() {
		return selfDeclarationCheck;
	}

	public void setSelfDeclarationCheck( boolean selfDeclarationCheck ) {
		this.selfDeclarationCheck = selfDeclarationCheck;
	}

	public boolean isAllowChildEmailForPRD() {
		return allowChildEmailForPRD;
	}

	public void setAllowChildEmailForPRD( boolean allowChildEmailForPRD ) {
		this.allowChildEmailForPRD = allowChildEmailForPRD;
	}

	public boolean isVscOfficerCheck() {
		return vscOfficerCheck;
	}

	public void setVscOfficerCheck( boolean vscOfficerCheck ) {
		this.vscOfficerCheck = vscOfficerCheck;
	}

	public boolean isVscDutyHoursCheck() {
		return vscDutyHoursCheck;
	}

	public void setVscDutyHoursCheck( boolean vscDutyHoursCheck ) {
		this.vscDutyHoursCheck = vscDutyHoursCheck;
	}

	public boolean isVscServiceYearsCheck() {
		return vscServiceYearsCheck;
	}

	public void setVscServiceYearsCheck( boolean vscServiceYearsCheck ) {
		this.vscServiceYearsCheck = vscServiceYearsCheck;
	}

	public boolean isVscSuppDocsCheck() {
		return vscSuppDocsCheck;
	}

	public void setVscSuppDocsCheck( boolean vscSuppDocsCheck ) {
		this.vscSuppDocsCheck = vscSuppDocsCheck;
	}

	public boolean isVscHeadRecommendation() {
		return vscHeadRecommendation;
	}

	public void setVscHeadRecommendation( boolean vscHeadRecommendation ) {
		this.vscHeadRecommendation = vscHeadRecommendation;
	}

	public Date getDateOfSubmission() {
		return dateOfSubmission;
	}

	public void setDateOfSubmission( Date dateOfSubmission ) {
		this.dateOfSubmission = dateOfSubmission;
	}

	public String getSubmittedBy() {
		return submittedBy;
	}

	public void setSubmittedBy( String submittedBy ) {
		this.submittedBy = submittedBy;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks( String remarks ) {
		this.remarks = remarks;
	}

	public List<SAGAcademicResults> getAcademicResults() {
		return academicResults;
	}

	public void setAcademicResults( List<SAGAcademicResults> academicResults ) {
		this.academicResults = academicResults;
	}

	public List<SAGCoCurricularActivity> getChildCcaDetails() {
		return childCcaDetails;
	}

	public void setChildCcaDetails(
			List<SAGCoCurricularActivity> childCcaDetails ) {
		this.childCcaDetails = childCcaDetails;
	}

	public String getChildMusicArtsSpecialDetails() {
		return childMusicArtsSpecialDetails;
	}

	public void setChildMusicArtsSpecialDetails(
			String childMusicArtsSpecialDetails ) {
		this.childMusicArtsSpecialDetails = childMusicArtsSpecialDetails;
	}

	public String getChildCommendationDetails() {
		return childCommendationDetails;
	}

	public void setChildCommendationDetails( String childCommendationDetails ) {
		this.childCommendationDetails = childCommendationDetails;
	}

	public List<SAGFamilyBackground> getChildFamBackgroundDetails() {
		return childFamBackgroundDetails;
	}

	public void setChildFamBackgroundDetails(
			List<SAGFamilyBackground> childFamBackgroundDetails ) {
		this.childFamBackgroundDetails = childFamBackgroundDetails;
	}

	public List<SupportingDocument> getSupportingDocuments() {
		return supportingDocuments;
	}

	public void setSupportingDocuments(
			List<SupportingDocument> supportingDocuments ) {
		this.supportingDocuments = supportingDocuments;
	}

	public List<ApprovalRecord> getApprovalRecords() {
		return approvalRecords;
	}

	public void setApprovalRecords( List<ApprovalRecord> approvalRecords ) {
		this.approvalRecords = approvalRecords;
	}

	public String getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear( String financialYear ) {
		this.financialYear = financialYear;
	}

	public String getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber( String sequenceNumber ) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getChequeNumber() {
		return chequeNumber;
	}

	public void setChequeNumber( String chequeNumber ) {
		this.chequeNumber = chequeNumber;
	}

	public Date getChequeUpdatedDate() {
		return chequeUpdatedDate;
	}

	public void setChequeUpdatedDate( Date chequeUpdatedDate ) {
		this.chequeUpdatedDate = chequeUpdatedDate;
	}

	public String getChequeUpdatedBy() {
		return chequeUpdatedBy;
	}

	public void setChequeUpdatedBy( String chequeUpdatedBy ) {
		this.chequeUpdatedBy = chequeUpdatedBy;
	}

	public Double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid( Double amountPaid ) {
		this.amountPaid = amountPaid;
	}

	public String getVscOccupation() {
		return vscOccupation;
	}

	public void setVscOccupation( String vscOccupation ) {
		this.vscOccupation = vscOccupation;
	}

	public String getVscCompanyName() {
		return vscCompanyName;
	}

	public void setVscCompanyName( String vscCompanyName ) {
		this.vscCompanyName = vscCompanyName;
	}

	public Date getChequeValueDate() {
		return chequeValueDate;
	}

	public void setChequeValueDate( Date chequeValueDate ) {
		this.chequeValueDate = chequeValueDate;
	}

	public String getOnBehalfReason() {
		return onBehalfReason;
	}

	public void setOnBehalfReason( String onBehalfReason ) {
		this.onBehalfReason = onBehalfReason;
	}

	public String getOnBehalfOthersReason() {
		return onBehalfOthersReason;
	}

	public void setOnBehalfOthersReason( String onBehalfOthersReason ) {
		this.onBehalfOthersReason = onBehalfOthersReason;
	}

	public String getMeritInDiploma() {
		return meritInDiploma;
	}

	public void setMeritInDiploma( String meritInDiploma ) {
		this.meritInDiploma = meritInDiploma;
	}

	public String getMeritInNitecHNitec() {
		return meritInNitecHNitec;
	}

	public void setMeritInNitecHNitec( String meritInNitecHNitec ) {
		this.meritInNitecHNitec = meritInNitecHNitec;
	}

	public String getChildOtherCurrentSchool() {
		return childOtherCurrentSchool;
	}

	public void setChildOtherCurrentSchool( String childOtherCurrentSchool ) {
		this.childOtherCurrentSchool = childOtherCurrentSchool;
	}

	public String getMailingAddress() {
		return mailingAddress;
	}

	public void setMailingAddress(String mailingAddress) {
		this.mailingAddress = mailingAddress;
	}

	public String getPreferredPaymentMode() {
		return preferredPaymentMode;
	}

	public void setPreferredPaymentMode(String preferredPaymentMode) {
		this.preferredPaymentMode = preferredPaymentMode;
	}
	
	public String getPaynowRecipientName() {
		return paynowRecipientName;
	}

	public void setPaynowRecipientName(String paynowRecipientName) {
		this.paynowRecipientName = paynowRecipientName;
	}

	public String getPaynowProxyType() {
		return paynowProxyType;
	}

	public void setPaynowProxyType(String paynowProxyType) {
		this.paynowProxyType = paynowProxyType;
	}

	public String getPaynowProxyValue() {
		return paynowProxyValue;
	}

	public void setPaynowProxyValue(String paynowProxyValue) {
		this.paynowProxyValue = paynowProxyValue;
	}
	
	/*public boolean getNotOtherRecipient() {
		return notOtherRecipient;
	}

	public void setNotOtherRecipient( boolean notOtherRecipient ) {
		this.notOtherRecipient = notOtherRecipient;
	}*/

	public boolean getNoNationalService() {
		return noNationalService;
	}

	public void setNoNationalService( boolean noNationalService ) {
		this.noNationalService = noNationalService;
	}
	
	public boolean getIncompleteAppNotConsidered() {
		return incompleteAppNotConsidered;
	}

	public void setIncompleteAppNotConsidered( boolean incompleteAppNotConsidered ) {
		this.incompleteAppNotConsidered = incompleteAppNotConsidered;
	}
	
	public boolean getNoDiscrepencyFinancialInformation() {
		return noDiscrepencyFinancialInformation;
	}

	public void setNoDiscrepencyFinancialInformation( boolean noDiscrepencyFinancialInformation ) {
		this.noDiscrepencyFinancialInformation = noDiscrepencyFinancialInformation;
	}
		
	
	public void preSave() {
		if ( referenceNumber != null ) {
			referenceNumber = referenceNumber.toUpperCase();
		}

		if ( memberNric != null ) {
			memberNric = memberNric.toUpperCase();
		}

		if ( accountNo != null ) {
			accountNo = accountNo.toUpperCase();
		}

		if ( bank != null ) {
			bank = bank.toUpperCase();
		}

		if ( bankRecipientName != null ) {
			bankRecipientName = bankRecipientName.toUpperCase();
		}

		if ( branchCode != null ) {
			branchCode = branchCode.toUpperCase();
		}
		
		if ( paymentAdviceEmail != null ) {
			paymentAdviceEmail = paymentAdviceEmail.toUpperCase();
		}

		if ( childCurrentSchool != null ) {
			childCurrentSchool = childCurrentSchool.toUpperCase();
		}

		if ( childEmail != null ) {
			childEmail = childEmail.toUpperCase();
		}

		if ( childHighestEduLevel != null ) {
			childHighestEduLevel = childHighestEduLevel.toUpperCase();
		}

		if ( childLastExamStatus != null ) {
			childLastExamStatus = childLastExamStatus.toUpperCase();
		}

		if ( childName != null ) {
			childName = childName.toUpperCase();
		}

		if ( childNewEduLevel != null ) {
			childNewEduLevel = childNewEduLevel.toUpperCase();
		}

		if ( childNric != null ) {
			childNric = childNric.toUpperCase();
		}

		if ( childPsleScore != null ) {
			childPsleScore = childPsleScore.toUpperCase();
		}

		if ( otherBank != null ) {
			otherBank = otherBank.toUpperCase();
		}

		if ( remarks != null ) {
			remarks = remarks.toUpperCase();
		}

		if ( submittedBy != null ) {
			submittedBy = submittedBy.toUpperCase();
		}

		if ( academicResults != null ) {
			for ( SAGAcademicResults each : academicResults ) {
				each.preSave();
			}
		}

		if ( childCcaDetails != null ) {
			for ( SAGCoCurricularActivity each : childCcaDetails ) {
				each.preSave();
			}
		}

		if ( childMusicArtsSpecialDetails != null ) {
			childMusicArtsSpecialDetails = childMusicArtsSpecialDetails
					.toUpperCase();
		}

		if ( childCommendationDetails != null ) {
			childCommendationDetails = childCommendationDetails.toUpperCase();
		}
		
		if ( mailingAddress != null ) {
			mailingAddress = mailingAddress.toUpperCase();
		}
		
		if ( preferredPaymentMode != null ) {
			preferredPaymentMode = preferredPaymentMode.toUpperCase();
		}
		
		if ( paynowProxyType != null ) {
			paynowProxyType = paynowProxyType.toUpperCase();
		}
		
		if ( paynowProxyValue != null ) {
			paynowProxyValue = paynowProxyValue.toUpperCase();
		}

		if ( childFamBackgroundDetails != null ) {
			for ( SAGFamilyBackground each : childFamBackgroundDetails ) {
				each.preSave();
			}
		}

		if ( approvalRecords != null ) {
			for ( ApprovalRecord each : approvalRecords ) {
				each.preSave();
			}
		}
				
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ( ( referenceNumber == null ) ? 0 : referenceNumber
						.hashCode() );
		return result;
	}

	@Override
	public boolean equals( Object obj ) {
		if ( this == obj )
			return true;
		if ( obj == null )
			return false;
		if ( getClass() != obj.getClass() )
			return false;
		SAGApplication other = (SAGApplication) obj;
		if ( referenceNumber == null ) {
			if ( other.referenceNumber != null )
				return false;
		} else if ( !referenceNumber.equals( other.referenceNumber ) )
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SAGApplication [referenceNumber=" + referenceNumber + ", financialYear=" + financialYear
				+ ", awardType=" + awardType + ", applicationStatus=" + applicationStatus + ", memberNric=" + memberNric
				+ ", grossSalary=" + grossSalary + ", specialAllowance=" + specialAllowance + ", vscDutyHours="
				+ vscDutyHours + ", vscServiceYears=" + vscServiceYears + ", vscOccupation=" + vscOccupation
				+ ", vscCompanyName=" + vscCompanyName + ", childNric=" + childNric + ", childName=" + childName
				+ ", childNameAmended=" + childNameAmended + ", childDateOfBirth=" + childDateOfBirth + ", childEmail="
				+ childEmail + ", childCurrentSchool=" + childCurrentSchool + ", childOtherCurrentSchool="
				+ childOtherCurrentSchool + ", childHighestEduLevel=" + childHighestEduLevel + ", childLastExamStatus="
				+ childLastExamStatus + ", childNewEduLevel=" + childNewEduLevel + ", childPsleScore=" + childPsleScore
				+ ", childCgpa=" + childCgpa + ", bankRecipientName=" + bankRecipientName + ", bank=" + bank
				+ ", otherBank=" + otherBank + ", branchCode=" + branchCode + ", accountNo=" + accountNo
				+ ", paymentAdviceEmail=" + paymentAdviceEmail + ", selfDeclarationCheck=" + selfDeclarationCheck
				+ ", allowChildEmailForPRD=" + allowChildEmailForPRD + ", vscOfficerCheck=" + vscOfficerCheck
				+ ", vscDutyHoursCheck=" + vscDutyHoursCheck + ", vscServiceYearsCheck=" + vscServiceYearsCheck
				+ ", vscSuppDocsCheck=" + vscSuppDocsCheck + ", vscHeadRecommendation=" + vscHeadRecommendation
				+ ", dateOfSubmission=" + dateOfSubmission + ", submittedBy=" + submittedBy + ", remarks=" + remarks
				+ ", academicResults=" + academicResults + ", childCcaDetails=" + childCcaDetails
				+ ", childMusicArtsSpecialDetails=" + childMusicArtsSpecialDetails + ", childCommendationDetails="
				+ childCommendationDetails + ", childFamBackgroundDetails=" + childFamBackgroundDetails
				+ ", supportingDocuments=" + supportingDocuments + ", approvalRecords=" + approvalRecords
				+ ", sequenceNumber=" + sequenceNumber + ", chequeNumber=" + chequeNumber + ", chequeUpdatedDate="
				+ chequeUpdatedDate + ", chequeUpdatedBy=" + chequeUpdatedBy + ", chequeValueDate=" + chequeValueDate
				+ ", onBehalfReason=" + onBehalfReason + ", onBehalfOthersReason=" + onBehalfOthersReason
				+ ", amountPaid=" + amountPaid + ", meritInDiploma=" + meritInDiploma + ", meritInNitecHNitec="
				+ meritInNitecHNitec + ", mailingAddress=" + mailingAddress + ", preferredPaymentMode="
				+ preferredPaymentMode + ", paynowRecipientName=" + paynowRecipientName + ", paynowProxyType="
				+ paynowProxyType + ", paynowProxyValue=" + paynowProxyValue + ", noNationalService="
				+ noNationalService + ", incompleteAppNotConsidered=" + incompleteAppNotConsidered
				+ ", noDiscrepencyFinancialInformation=" + noDiscrepencyFinancialInformation + "]";
	}
	
	

}

