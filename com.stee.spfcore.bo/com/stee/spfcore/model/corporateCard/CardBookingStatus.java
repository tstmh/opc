package com.stee.spfcore.model.corporateCard;

public enum CardBookingStatus {
    PENDING( "Pending" ), APPROVED( "Approved" ), REJECTED( "Rejected" ), CANCELLED( "Cancelled" ), NONE( "None" );

    private String cardBookingStatusText;

    private CardBookingStatus( String cardBookingStatusText ) {
        this.cardBookingStatusText = cardBookingStatusText;
    }

    public static CardBookingStatus get( String cardBookingStatusText ) {
        CardBookingStatus cardBookingStatus = NONE;
        if ( cardBookingStatusText != null ) {
            for ( CardBookingStatus tempCardBookingStatus : CardBookingStatus.values() ) {
                if ( cardBookingStatusText.equals( tempCardBookingStatus.cardBookingStatusText ) ) {
                    cardBookingStatus = tempCardBookingStatus;
                    break;
                }
            }
        }
        return cardBookingStatus;
    }

    @Override
    public String toString() {
        return this.cardBookingStatusText;
    }
}
