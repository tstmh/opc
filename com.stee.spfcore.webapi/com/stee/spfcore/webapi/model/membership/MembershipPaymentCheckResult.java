package com.stee.spfcore.webapi.model.membership;

import javax.persistence.Embeddable;

@Embeddable
public class MembershipPaymentCheckResult {
    public final static MembershipPaymentCheckResult FUTURE_DATE = new MembershipPaymentCheckResult( Boolean.TRUE, 100, "Future date." );
    public final static MembershipPaymentCheckResult NO_DISCREPANCY = new MembershipPaymentCheckResult( Boolean.FALSE, 200, "PNSF or CPO haven't run." );
    public final static MembershipPaymentCheckResult REQD_TO_PAY_N_BALANCE = new MembershipPaymentCheckResult( Boolean.TRUE, 300, "Required to pay and balanced." );
    public final static MembershipPaymentCheckResult OWE_N_BALANCE = new MembershipPaymentCheckResult( Boolean.FALSE, 400, "Owe and never pay." );
    public final static MembershipPaymentCheckResult HAVE_POSITIVE_PAYMENT = new MembershipPaymentCheckResult( Boolean.TRUE, 500, "Have positive payment." );
    public final static MembershipPaymentCheckResult NO_POSITIVE_PAYMENT = new MembershipPaymentCheckResult( Boolean.FALSE, 600, "No positive payment." );
    public final static MembershipPaymentCheckResult RANK_NOT_COVERED_NO_PAYMENT_OR_RANK_COVERED_FEE_ZERO = new MembershipPaymentCheckResult( Boolean.TRUE, 700, "Rank not covered and no payment / Rank covered of fee $0." );
    public final static MembershipPaymentCheckResult POSITIVE_AMT_COLLECTED = new MembershipPaymentCheckResult( Boolean.TRUE, 800, "System say no need pay and +ve amount collected." );
    public final static MembershipPaymentCheckResult OWE_MONEY = new MembershipPaymentCheckResult( Boolean.FALSE, 900, "Should pay and officer say you owe money." );

    private Boolean result;
    private Integer code;
    private String description;

    public MembershipPaymentCheckResult() {
    }

    public MembershipPaymentCheckResult( Boolean result, Integer code, String description ) {
        this.result = result;
        this.code = code;
        this.description = description;
    }

    public Boolean getResult() {
        return result;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
