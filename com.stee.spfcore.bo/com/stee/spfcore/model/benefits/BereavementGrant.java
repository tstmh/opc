package com.stee.spfcore.model.benefits;

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
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import com.stee.spfcore.model.ApplicationStatus;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"BEREAVEMENT_GRANT_APPLICATION\"", schema = "\"SPFCORE\"")
@XStreamAlias("BereavementGrant")
@Audited
public class BereavementGrant {

	@Id
	@Column(name = "\"REFERENCE_NUMBER\"", length = 50)
	private String referenceNumber;

	@Column(name = "\"MEMBER_NRIC\"", length = 10)
	private String nric;

	@Enumerated(EnumType.STRING)
	@Column(name = "\"APPLICATION_STATUS\"", length = 10)
	private ApplicationStatus applicationStatus;

	@Temporal(TemporalType.DATE)
	@Column(name = "\"SUBMISSION_DATE\"")
	private Date dateOfSubmission;
	
	@Column(name="\"DECEASED_NAME\"")
	private String nameOfDeceased;
	
	@Column(name="\"DECEASED_RELATION\"")
	private String relationship;
	
	@Temporal(TemporalType.DATE)
	@Column(name="\"DECEASE_DATE\"")
	private Date deceaseDate;
	
	@Column(name="\"DECEASED_ID_TYPE\"")
	private String deceasedIdType;
	
	@Column(name="\"DECEASED_NRIC\"", length = 50)
	private String deceasedNric;
	
	@Column(name="\"HAVE_DEATH_CERTIFICATE\"")
	private boolean haveDeathCertificate;
	
	@Column(name="\"DEATH_CERTIFICATE_NO\"")
	private String deathCertificateNumber;
	
	@Column(name="\"RSN_NO_CERTIFICATE\"", length = 1000)
	private String reasonForNoCertificate;
	
	@Column(name="\"AMT_PAID\"")
	private Double amountPaid;
	
	@Temporal(TemporalType.DATE)
	@Column(name="\"PAYMENT_DATE\"")
	private Date paymentDate;

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

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
	@JoinColumn(name="\"BEREAVEMENT_REFERENCE_NUMBER\"")
	@Fetch(value = FetchMode.SUBSELECT)
	@Index(name="INDEX_APPLICATION_SUPPORTING_DOCUMENT_BEREAVEMENT_RER_NUM", columnNames={"BEREAVEMENT_REFERENCE_NUMBER"})
	private List<SupportingDocument> supportingDocuments;

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
	@JoinColumn(name="\"BEREAVEMENT_REFERENCE_NUMBER\"")
	@Fetch(value = FetchMode.SUBSELECT)
	@Index(name="INDEX_APPLICATION_APPROVAL_RECORD_BEREAVEMENT_RER_NUM", columnNames={"BEREAVEMENT_REFERENCE_NUMBER"})
	private List<ApprovalRecord> approvalRecords;
	
	@Column(name = "\"SUBMITTED_BY\"", length = 50)
	private String submittedBy;
	
	@Column(name = "\"CHEQUE_NUMBER\"", length = 20)
	private String chequeNumber;
	
	@Temporal(TemporalType.DATE)
    @Column(name="\"COMPLETION_DATE\"")
	private Date completionDate;

	public BereavementGrant() {
		super();
	}

	public BereavementGrant(String referenceNumber, String nric,
            ApplicationStatus applicationStatus, Date dateOfSubmission,
            String nameOfDeceased, String relationship, Date deceaseDate,
            String deceasedIdType, String deceasedNric,
            boolean haveDeathCertificate, String deathCertificateNumber,
            String reasonForNoCertificate, Double amountPaid, Date paymentDate,
            String remark, String bank, String otherBank, String branchCode,
            String accountNo, List<SupportingDocument> supportingDocuments,
            List<ApprovalRecord> approvalRecords, String submittedBy,
            String chequeNumber, Date completionDate) {
        super();
        this.referenceNumber = referenceNumber;
        this.nric = nric;
        this.applicationStatus = applicationStatus;
        this.dateOfSubmission = dateOfSubmission;
        this.nameOfDeceased = nameOfDeceased;
        this.relationship = relationship;
        this.deceaseDate = deceaseDate;
        this.deceasedIdType = deceasedIdType;
        this.deceasedNric = deceasedNric;
        this.haveDeathCertificate = haveDeathCertificate;
        this.deathCertificateNumber = deathCertificateNumber;
        this.reasonForNoCertificate = reasonForNoCertificate;
        this.amountPaid = amountPaid;
        this.paymentDate = paymentDate;
        this.remark = remark;
        this.bank = bank;
        this.otherBank = otherBank;
        this.branchCode = branchCode;
        this.accountNo = accountNo;
        this.supportingDocuments = supportingDocuments;
        this.approvalRecords = approvalRecords;
        this.submittedBy = submittedBy;
        this.chequeNumber = chequeNumber;
        this.completionDate = completionDate;
    }


    public String getNric() {
		return nric;
	}

	public void setNric(String nric) {
		this.nric = nric;
	}

	public String getReferenceNumber() {
		return referenceNumber;
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

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getNameOfDeceased() {
		return nameOfDeceased;
	}

	public void setNameOfDeceased(String nameOfDeceased) {
		this.nameOfDeceased = nameOfDeceased;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public Date getDeceaseDate() {
		return deceaseDate;
	}

	public void setDeceaseDate(Date deceaseDate) {
		this.deceaseDate = deceaseDate;
	}

	public String getDeceasedIdType() {
		return deceasedIdType;
	}

	public void setDeceasedIdType(String deceasedIdType) {
		this.deceasedIdType = deceasedIdType;
	}

	public String getDeceasedNric() {
		return deceasedNric;
	}

	public void setDeceasedNric(String deceasedNric) {
		this.deceasedNric = deceasedNric;
	}

	public boolean isHaveDeathCertificate() {
		return haveDeathCertificate;
	}

	public void setHaveDeathCertificate(boolean haveDeathCertificate) {
		this.haveDeathCertificate = haveDeathCertificate;
	}

	public String getDeathCertificateNumber() {
		return deathCertificateNumber;
	}

	public void setDeathCertificateNumber(String deathCertificateNumber) {
		this.deathCertificateNumber = deathCertificateNumber;
	}

	public String getReasonForNoCertificate() {
		return reasonForNoCertificate;
	}

	public void setReasonForNoCertificate(String reasonForNoCertificate) {
		this.reasonForNoCertificate = reasonForNoCertificate;
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

	
	public String getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(String chequeNumber) {
        this.chequeNumber = chequeNumber;
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
		
		if (nric != null) {
			nric = nric.toUpperCase();
		}
		
		if (deathCertificateNumber != null) {
			deathCertificateNumber = deathCertificateNumber.toUpperCase();
		}
		
		if (deceasedNric != null) {
			deceasedNric = deceasedNric.toUpperCase();
		}
		
		if (nameOfDeceased != null) {
			nameOfDeceased = nameOfDeceased.toUpperCase();
		}
		
		if (submittedBy != null) {
			submittedBy = submittedBy.toUpperCase();
		}
		
		if (chequeNumber != null) {
		    chequeNumber = chequeNumber.toUpperCase();
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
		BereavementGrant other = (BereavementGrant) obj;
		if (referenceNumber == null) {
			if (other.referenceNumber != null)
				return false;
		} else if (!referenceNumber.equals(other.referenceNumber))
			return false;
		return true;
	}

}
