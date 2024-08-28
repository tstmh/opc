package com.stee.spfcore.webapi.model.globalParameters;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import com.stee.spfcore.webapi.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table( name = "GLOBAL_PARAMETERS", schema = "SPFCORE" )
@XStreamAlias( "GlobalParameters" )
@Audited
@SequenceDef (name="GLOBAL_PARAMETERS_SEQ", schema = "SPFCORE", internetFormat="GLOBAL_PARAMETERS_SEQ_%d", intranetFormat="GLOBAL_PARAMETERS_SEQ_%d")
public class GlobalParameters {
	
    @Id
    //@GenericGenerator( name = "GlobalParametersGenerator", strategy = "com.stee.spfcore.dao.utils.StringIdHibernateGenerator" )
    //@GeneratedValue( generator = "GlobalParametersGenerator" )
    @Column( name = "ID" )
    private String id;

    @Column( name = "CC_COLLECTION_DAYS" )
    private Integer corporateCardCollectionPeriodDays = 1;

    @Column( name = "CC_BOOK_RESULTS_NOTIFICATION_DAYS" )
    private Integer corporateCardBookingResultsNotificationPeriodDays = 3;

    @Column( name = "CC_FCFS_APPROVAL_PROCESS_DAYS" )
    private Integer corporateCardFcfsApprovalProcessingPeriodDays = 3;
    
    @Column (name = "CC_UNIT_OR_PCWF")
    private String corporateCardUnitOrPcwf = "";
    
    @Column (name = "CC_SUBUNIT")
    private String corporateCardSubunit = "";
    
    @Column (name = "CC_ALLOWABLE_CANCELLED_BOOKINGS")
    private Integer corporateCardAllowableCancelledBookings = 3;
    
	@Column (name = "SENDER_EMAIL", length = 256)
	private String senderEmail;
	
	@Column (name = "SENDER_EMAIL_PASSWORD", length = 100)
	private String senderEmailPassword;
	
	@Column (name = "EMAIL_SIGN_OFF", length = 256)
	private String emailSignOff = "";

	public GlobalParameters() {
		
    }

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCorporateCardUnitOrPcwf() {
		return corporateCardUnitOrPcwf;
	}

	public void setCorporateCardUnitOrPcwf(String corporateCardUnitOrPcwf) {
		this.corporateCardUnitOrPcwf = corporateCardUnitOrPcwf;
	}

	public Integer getCorporateCardCollectionPeriodDays() {
        return corporateCardCollectionPeriodDays;
    }

    public void setCorporateCardCollectionPeriodDays( Integer corporateCardCollectionPeriodDays ) {
        this.corporateCardCollectionPeriodDays = corporateCardCollectionPeriodDays;
    }

    public Integer getCorporateCardBookingResultsNotificationPeriodDays() {
        return corporateCardBookingResultsNotificationPeriodDays;
    }

    public void setCorporateCardBookingResultsNotificationPeriodDays( Integer corporateCardBookingResultsNotificationPeriodDays ) {
        this.corporateCardBookingResultsNotificationPeriodDays = corporateCardBookingResultsNotificationPeriodDays;
    }

    public Integer getCorporateCardFcfsApprovalProcessingPeriodDays() {
        return corporateCardFcfsApprovalProcessingPeriodDays;
    }

    public void setCorporateCardFcfsApprovalProcessingPeriodDays( Integer corporateCardFcfsApprovalProcessingPeriodDays ) {
        this.corporateCardFcfsApprovalProcessingPeriodDays = corporateCardFcfsApprovalProcessingPeriodDays;
    }
    
    public String getCorporateCardUnitOrPCWF() {
		return corporateCardUnitOrPcwf;
	}

	public void setCorporateCardUnitOrPCWF(String corporateCardUnitOrPcwf) {
		this.corporateCardUnitOrPcwf = corporateCardUnitOrPcwf;
	}
	
	public String getCorporateCardSubunit() {
		return corporateCardSubunit;
	}

	public void setCorporateCardSubunit(String corporateCardSubunit) {
		this.corporateCardSubunit = corporateCardSubunit;
	}
	
	public Integer getCorporateCardAllowableCancelledBookings() {
		
		return corporateCardAllowableCancelledBookings;
	}

	public void setCorporateCardAllowableCancelledBookings(Integer corporateCardAllowableCancelledBookings) {
		this.corporateCardAllowableCancelledBookings = corporateCardAllowableCancelledBookings;
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public String getSenderEmailPassword() {
		return senderEmailPassword;
	}

	public void setSenderEmailPassword(String senderEmailPassword) {
		this.senderEmailPassword = senderEmailPassword;
	}

	public String getEmailSignOff() {
		return emailSignOff;
	}

	public void setEmailSignOff(String emailSignOff) {
		this.emailSignOff = emailSignOff;
	}
	
	
}
