package com.stee.spfcore.webapi.model.benefits;

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
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"WEDDING_GIFT_APPLICATION\"", schema = "\"SPFCORE\"")
@XStreamAlias("WeddingGift")
@Audited
public class WeddingGift {

	@Id
	@Column(name = "\"REFERENCE_NUMBER\"", length = 50)
	private String referenceNumber;

	@Column(name = "\"MEMBER_NRIC\"", length = 10)
	private String memberNric;

	@Enumerated(EnumType.STRING)
	@Column(name = "\"APPLICATION_STATUS\"", length = 10)
	private ApplicationStatus applicationStatus;

	@Temporal(TemporalType.DATE)
	@Column(name = "\"SUBMISSION_DATE\"")
	private Date dateOfSubmission;

	@Column(name = "\"SPOUSE_NRIC\"", length = 50)
	private String spouseNric;

	@Column(name = "\"SPOUSE_ID_TYPE\"", length = 50)
	private String spouseIdType;
	
	@Column(name = "\"SPOUSE_NAME\"", length = 100)
	private String spouseName;

	@Temporal(TemporalType.DATE)
	@Column(name = "\"DATE_OF_MARRIAGE\"")
	private Date dateOfMarriage;

	@Column(name = "\"CERTIFICATE_NO\"", length = 50)
	private String certificateNumber;

	@Column(name = "\"REMARKS\"", length = 2000)
	private String remark;
	
	@Column(name="\"BANK\"", length=100)
	private String bank;
	
	@Column(name="\"OTHER_BANK\"", length=100)
	private String otherBank;
	
	@Column(name="\"BRANCH_CODE\"")
	private String branchCode;
	
	@Column(name="\"ACCOUNT_NO\"")
	private String accountNo;
	
	@Column(name="\"AMT_PAID\"")
	private Double amountPaid;
	
	@Temporal(TemporalType.DATE)
	@Column(name="\"PAYMENT_DATE\"")
	private Date paymentDate;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
	@JoinColumn(name="\"WEDDING_REFERENCE_NUMBER\"")
	@Fetch(value = FetchMode.SUBSELECT)
	//@Index(name="INDEX_APPLICATION_SUPPORTING_DOCUMENT_WEDDING_RER_NUM", columnNames={"WEDDING_REFERENCE_NUMBER"})
	private List<SupportingDocument> supportingDocuments;

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
	@JoinColumn(name="\"WEDDING_REFERENCE_NUMBER\"")
	@Fetch(value = FetchMode.SUBSELECT)
	//@Index(name="INDEX_APPLICATION_APPROVAL_RECORD_WEDDING_RER_NUM", columnNames={"WEDDING_REFERENCE_NUMBER"})
	private List<ApprovalRecord> approvalRecords;

	@Column(name = "\"SUBMITTED_BY\"", length = 50)
	private String submittedBy;
	
	@Temporal(TemporalType.DATE)
    @Column(name="\"COMPLETION_DATE\"")
    private Date completionDate;
	
	public WeddingGift() {
		super();
	}

	public WeddingGift(String referenceNumber, String memberNric,
            ApplicationStatus applicationStatus, Date dateOfSubmission,
            String spouseNric, String spouseIdType, String spouseName,
            Date dateOfMarriage, String certificateNumber, String remark,
            String bank, String otherBank, String branchCode, String accountNo,
            Double amountPaid, Date paymentDate,
            List<SupportingDocument> supportingDocuments,
            List<ApprovalRecord> approvalRecords, String submittedBy,
            Date completionDate) {
        super();
        this.referenceNumber = referenceNumber;
        this.memberNric = memberNric;
        this.applicationStatus = applicationStatus;
        this.dateOfSubmission = dateOfSubmission;
        this.spouseNric = spouseNric;
        this.spouseIdType = spouseIdType;
        this.spouseName = spouseName;
        this.dateOfMarriage = dateOfMarriage;
        this.certificateNumber = certificateNumber;
        this.remark = remark;
        this.bank = bank;
        this.otherBank = otherBank;
        this.branchCode = branchCode;
        this.accountNo = accountNo;
        this.amountPaid = amountPaid;
        this.paymentDate = paymentDate;
        this.supportingDocuments = supportingDocuments;
        this.approvalRecords = approvalRecords;
        this.submittedBy = submittedBy;
        this.completionDate = completionDate;
    }



    public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public ApplicationStatus getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus(ApplicationStatus applicationStatus) {
		this.applicationStatus = applicationStatus;
	}

	public Date getDateOfSubmission() {
		return dateOfSubmission;
	}

	public void setDateOfSubmission(Date dateOfSubmission) {
		this.dateOfSubmission = dateOfSubmission;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMemberNric() {
		return memberNric;
	}

	public void setMemberNric(String memberNric) {
		this.memberNric = memberNric;
	}

	public String getSpouseNric() {
		return spouseNric;
	}

	public void setSpouseNric(String spouseNric) {
		this.spouseNric = spouseNric;
	}

	public String getSpouseIdType() {
		return spouseIdType;
	}

	public void setSpouseIdType(String spouseIdType) {
		this.spouseIdType = spouseIdType;
	}

	public String getSpouseName() {
		return spouseName;
	}

	public void setSpouseName(String spouseName) {
		this.spouseName = spouseName;
	}

	public Date getDateOfMarriage() {
		return dateOfMarriage;
	}

	public void setDateOfMarriage(Date dateOfMarriage) {
		this.dateOfMarriage = dateOfMarriage;
	}

	public String getCertificateNumber() {
		return certificateNumber;
	}

	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getOtherBank() {
		return otherBank;
	}

	public void setOtherBank(String otherBank) {
		this.otherBank = otherBank;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public Double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public List<SupportingDocument> getSupportingDocuments() {
		return supportingDocuments;
	}

	public void setSupportingDocuments(List<SupportingDocument> supportingDocuments) {
		this.supportingDocuments = supportingDocuments;
	}

	public List<ApprovalRecord> getApprovalRecords() {
		return approvalRecords;
	}

	public void setApprovalRecords(List<ApprovalRecord> approvalRecords) {
		this.approvalRecords = approvalRecords;
	}

	public String getSubmittedBy() {
		return submittedBy;
	}

	public void setSubmittedBy(String submittedBy) {
		this.submittedBy = submittedBy;
	}
	
	public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public void preSave () {
		
		if (accountNo != null) {
			accountNo = accountNo.toUpperCase();
		}
		
		if (certificateNumber != null) {
			certificateNumber = certificateNumber.toUpperCase();
		}
		
		if (memberNric != null) {
			memberNric = memberNric.toUpperCase();
		}
		
		if (spouseName !=  null) {
			spouseName = spouseName.toUpperCase();
		}
		
		if (spouseNric != null) {
			spouseNric = spouseNric.toUpperCase();
		}
		
		if (submittedBy != null) {
			submittedBy = submittedBy.toUpperCase();
		}
		
		if (approvalRecords != null) {
			for (ApprovalRecord record : approvalRecords) {
				record.preSave ();
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((referenceNumber == null) ? 0 : referenceNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WeddingGift other = (WeddingGift) obj;
		if (referenceNumber == null) {
			if (other.referenceNumber != null)
				return false;
		} else if (!referenceNumber.equals(other.referenceNumber))
			return false;
		return true;
	}

}
