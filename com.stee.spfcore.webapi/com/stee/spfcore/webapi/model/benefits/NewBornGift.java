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
@Table(name="\"NEWBORN_GIFT_APPLICATION\"", schema="\"SPFCORE\"")
@XStreamAlias("NewBormGift")
@Audited
public class NewBornGift {
	
	@Id
	@Column(name="\"REFERENCE_NUMBER\"", length=50)
	private String referenceNumber;
	
	@Column(name="\"MEMBER_NRIC\"", length=10)
	private String memberNric;
	
	@Enumerated(EnumType.STRING)
	@Column(name="\"APPLICATION_STATUS\"", length=10)
	private ApplicationStatus applicationStatus;
	
	@Temporal(TemporalType.DATE)
	@Column(name="\"SUBMISSION_DATE\"")
	private Date dateOfSubmission;
	
	@Column(name="\"CHILD_NRIC\"", length=50)
	private String childNric;
	
	@Column(name = "\"CHILD_ID_TYPE\"", length = 50)
	private String childIdType;
	
	@Column(name="\"CHILD_NAME\"", length=100)
	private String childName;
	
	@Temporal(TemporalType.DATE)
	@Column(name="\"CHILD_DATE_OF_BIRTH\"")
	private Date childDateOfBirth;
	
	@Column(name="\"BIRTH_CERTIFICATE_NO\"")
	private String birthCertificateNumber;
	
	@Column(name="\"REMARKS\"", length=2000)
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
	@JoinColumn(name="\"NEW_BORN_REFERENCE_NUMBER\"")
	@Fetch(value = FetchMode.SUBSELECT)
	//@Index(name="INDEX_APPLICATION_SUPPORTING_DOCUMENT_NEW_BORN_RER_NUM", columnNames={"NEW_BORN_REFERENCE_NUMBER"})
	private List<SupportingDocument> supportingDocuments;

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
	@JoinColumn(name="\"NEW_BORN_REFERENCE_NUMBER\"")
	@Fetch(value = FetchMode.SUBSELECT)
	//@Index(name="INDEX_APPLICATION_APPROVAL_RECORD_NEW_BORN_RER_NUM", columnNames={"NEW_BORN_REFERENCE_NUMBER"})
	private List<ApprovalRecord> approvalRecords;

	@Column(name = "\"SUBMITTED_BY\"", length = 50)
	private String submittedBy;
	
	@Temporal(TemporalType.DATE)
    @Column(name="\"COMPLETION_DATE\"")
    private Date completionDate;

	public NewBornGift() {
		super();
	}
	
	public NewBornGift(String referenceNumber, String memberNric,
            ApplicationStatus applicationStatus, Date dateOfSubmission,
            String childNric, String childIdType, String childName, Date childDateOfBirth,
            String birthCertificateNumber, String remark, String bank,
            String otherBank, String branchCode, String accountNo,
            Double amountPaid, Date paymentDate,
            List<SupportingDocument> supportingDocuments,
            List<ApprovalRecord> approvalRecords, String submittedBy,
            Date completionDate) {
        super();
        this.referenceNumber = referenceNumber;
        this.memberNric = memberNric;
        this.applicationStatus = applicationStatus;
        this.dateOfSubmission = dateOfSubmission;
        this.childNric = childNric;
        this.childIdType = childIdType;
        this.childName = childName;
        this.childDateOfBirth = childDateOfBirth;
        this.birthCertificateNumber = birthCertificateNumber;
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

	public String getChildNric() {
		return childNric;
	}

	public void setChildNric(String childNric) {
		this.childNric = childNric;
	}

	public String getChildIdType() {
		return childIdType;
	}

	public void setChildIdType(String childIdType) {
		this.childIdType = childIdType;
	}
	
	public String getChildName() {
		return childName;
	}

	public void setChildName(String childName) {
		this.childName = childName;
	}

	public Date getChildDateOfBirth() {
		return childDateOfBirth;
	}

	public void setChildDateOfBirth(Date childDateOfBirth) {
		this.childDateOfBirth = childDateOfBirth;
	}

	public String getBirthCertificateNumber() {
		return birthCertificateNumber;
	}

	public void setBirthCertificateNumber(String birthCertificateNumber) {
		this.birthCertificateNumber = birthCertificateNumber;
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
		
		if (childName != null) {
			childName = childName.toUpperCase();
		}
		
		if (childNric != null) {
			childNric = childNric.toUpperCase();
		}
		
		if (memberNric != null) {
			memberNric = memberNric.toUpperCase();
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
		NewBornGift other = (NewBornGift) obj;
		if (referenceNumber == null) {
			if (other.referenceNumber != null)
				return false;
		} else if (!referenceNumber.equals(other.referenceNumber))
			return false;
		return true;
	}

}
