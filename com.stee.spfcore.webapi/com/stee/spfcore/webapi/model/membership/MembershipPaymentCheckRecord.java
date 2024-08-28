package com.stee.spfcore.webapi.model.membership;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Index;

@Entity
@Table( name = "\"MEMBERSHIP_PAYMENT_CHECK_RECORD\"", schema = "\"SPFCORE\"" )
public class MembershipPaymentCheckRecord {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "ID" )
    private long id;

    @Column( name = "CHECKER" )
    private String checker;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "CHECK_DATETIME" )
    @Index( name = "MEMBERSHIP_PAYMENT_CHECK_RECORD_IDX_CHECK_DATETIME" )
    private Date checkDateTime;

    @Column( name = "NRIC" )
    @Index( name = "MEMBERSHIP_PAYMENT_CHECK_RECORD_IDX_NRIC" )
    private String nric;

    @Column( name = "MONTH" )
    private Integer month;

    @Column( name = "YEAR" )
    @Index( name = "MEMBERSHIP_PAYMENT_CHECK_RECORD_IDX_YEAR" )
    private Integer year;

    @Embedded
    @AttributeOverrides( { @AttributeOverride( name = "result", column = @Column( name = "RESULT" ) ), @AttributeOverride( name = "code", column = @Column( name = "RESULT_CODE" ) ),
            @AttributeOverride( name = "description", column = @Column( name = "RESULT_DESCRIPTION" ) ) } )
    private MembershipPaymentCheckResult checkResult;

    public MembershipPaymentCheckRecord() {
    }

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker( String checker ) {
        this.checker = checker;
    }

    public Date getCheckDateTime() {
        return checkDateTime;
    }

    public void setCheckDateTime( Date checkDateTime ) {
        this.checkDateTime = checkDateTime;
    }

    public String getNric() {
        return nric;
    }

    public void setNric( String nric ) {
        this.nric = nric;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth( Integer month ) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear( Integer year ) {
        this.year = year;
    }

    public MembershipPaymentCheckResult getCheckResult() {
        return checkResult;
    }

    public void setCheckResult( MembershipPaymentCheckResult checkResult ) {
        this.checkResult = checkResult;
    }
}
