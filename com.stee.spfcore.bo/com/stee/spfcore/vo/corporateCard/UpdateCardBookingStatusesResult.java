package com.stee.spfcore.vo.corporateCard;

import java.util.List;

import com.stee.spfcore.model.corporateCard.CardBooking;

public class UpdateCardBookingStatusesResult {
    private List< String > errMsgs;
    private List< CardBooking > approvedCardBookings;
    private List< CardBooking > rejectedCardBookings;

    public UpdateCardBookingStatusesResult() {
        // DO NOTHING
    }

    public List< String > getErrMsgs() {
        return errMsgs;
    }

    public void setErrMsgs( List< String > errMsgs ) {
        this.errMsgs = errMsgs;
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
