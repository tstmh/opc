package com.stee.spfcore.model.corporateCard;

public enum CardType {
    GROUP( "Group Card" ), PCWF( "PCWF Card" ), NONE( "None" );

    private String cardTypeText;

    private CardType( String cardTypeText ) {
        this.cardTypeText = cardTypeText;
    }

    public static CardType get( String cardTypeText ) {
        CardType cardType = NONE;
        if ( cardTypeText != null ) {
            for ( CardType tempCardType : CardType.values() ) {
                if ( cardTypeText.equals( tempCardType.cardTypeText ) ) {
                    cardType = tempCardType;
                    break;
                }
            }
        }
        return cardType;
    }
    @Override
    public String toString() {
        return this.cardTypeText;
    }
}
