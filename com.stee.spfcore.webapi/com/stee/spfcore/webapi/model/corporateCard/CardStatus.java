package com.stee.spfcore.webapi.model.corporateCard;

public enum CardStatus {

	AUTO_POPULATE( "Auto-Populate" ), MISSING( "Missing" ), DAMAGED( "Damaged" ), LOST( "Lost" ), NONE( "None" );

    private String cardStatusText;

    private CardStatus( String cardStatusText ) {
        this.cardStatusText = cardStatusText;
    }

    public static CardStatus get( String cardStatusText ) {
        CardStatus cardStatus = NONE;
        if ( cardStatusText != null ) {
            for ( CardStatus tempCardStatus : CardStatus.values() ) {
                if ( cardStatusText.equals( tempCardStatus.cardStatusText ) ) {
                    cardStatus = tempCardStatus;
                    break;
                }
            }
        }
        return cardStatus;
    }

    public String toString() {
        return this.cardStatusText;
    }
}
