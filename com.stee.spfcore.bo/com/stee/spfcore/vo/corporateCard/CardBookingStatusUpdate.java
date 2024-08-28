package com.stee.spfcore.vo.corporateCard;

import com.stee.spfcore.model.corporateCard.CardBookingStatus;

public class CardBookingStatusUpdate {
    private String cardBookingId;
    private CardBookingStatus newStatus;

    public CardBookingStatusUpdate() {
    }
    
    public CardBookingStatusUpdate( String cardBookingId, CardBookingStatus updatedStatus ) {
        this.cardBookingId = cardBookingId;
        this.newStatus = updatedStatus;
    }

    public String getCardBookingId() {
        return cardBookingId;
    }

    public void setCardBookingId( String cardBookingId ) {
        this.cardBookingId = cardBookingId;
    }

    public CardBookingStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus( CardBookingStatus newStatus ) {
        this.newStatus = newStatus;
    }
}
