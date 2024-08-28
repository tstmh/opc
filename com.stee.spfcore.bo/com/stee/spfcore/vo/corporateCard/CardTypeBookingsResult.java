package com.stee.spfcore.vo.corporateCard;

import java.util.List;

import com.stee.spfcore.model.corporateCard.AllocationRule;
import com.stee.spfcore.model.corporateCard.CardBooking;
import com.stee.spfcore.model.corporateCard.CardTypeDetail;

public class CardTypeBookingsResult extends CardTypeLessDetail {
    private AllocationRule allocationRule;
    private List< CardBooking > pendingCardBookings;
    private List< CardBooking > approvedCardBookings;
    private List< CardBooking > rejectedCardBookings;

    public CardTypeBookingsResult() {
    }

    public CardTypeBookingsResult( CardTypeDetail cardTypeDetail, String departmentDescription ) {
        super( cardTypeDetail, departmentDescription );
        this.allocationRule = ( cardTypeDetail != null ) ? cardTypeDetail.getAllocationRule() : null;
    }

    public AllocationRule getAllocationRule() {
        return allocationRule;
    }

    public void setAllocationRule( AllocationRule allocationRule ) {
        this.allocationRule = allocationRule;
    }

    public List< CardBooking > getPendingCardBookings() {
        return pendingCardBookings;
    }

    public void setPendingCardBookings( List< CardBooking > pendingCardBookings ) {
        this.pendingCardBookings = pendingCardBookings;
    }

    public List< CardBooking > getApprovedCardBookings() {
        return approvedCardBookings;
    }

    public void setApprovedCardBookings( List< CardBooking > approvedCardBookings ) {
        this.approvedCardBookings = approvedCardBookings;
    }

    public List< CardBooking > getRejectedCardBookings() {
        return rejectedCardBookings;
    }

    public void setRejectedCardBookings( List< CardBooking > rejectedCardBookings ) {
        this.rejectedCardBookings = rejectedCardBookings;
    }
}
