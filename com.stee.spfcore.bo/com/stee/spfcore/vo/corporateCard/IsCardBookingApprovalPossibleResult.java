package com.stee.spfcore.vo.corporateCard;

import java.util.List;

public class IsCardBookingApprovalPossibleResult {
    private boolean isApprovalPossible = false;
    private String notPossibleReason = "";
    private List< String > cardSerialNumbers = null;

    public IsCardBookingApprovalPossibleResult() {
        // DO NOTHING
    }

    public boolean isApprovalPossible() {
        return isApprovalPossible;
    }

    public void setApprovalPossible( boolean isApprovalPossible ) {
        this.isApprovalPossible = isApprovalPossible;
    }

    public String getNotPossibleReason() {
        return notPossibleReason;
    }

    public void setNotPossibleReason( String notPossibleReason ) {
        this.notPossibleReason = notPossibleReason;
    }

    public List< String > getCardSerialNumbers() {
        return cardSerialNumbers;
    }

    public void setCardSerialNumbers( List< String > cardSerialNumbers ) {
        this.cardSerialNumbers = cardSerialNumbers;
    }
}
