package com.stee.spfcore.webapi.model.vo.corporateCard;

import java.util.ArrayList;
import java.util.List;

import com.stee.spfcore.webapi.model.personnel.PersonalDetail;
import com.stee.spfcore.webapi.model.vo.personnel.PersonalPreferredContacts;

public class CardPreviousHolderPreferredContacts extends PersonalPreferredContacts {
    private List< CardLessDetail > cardLessDetails = new ArrayList< CardLessDetail >();

    public CardPreviousHolderPreferredContacts( PersonalDetail detail ) {
        super( detail );
    }

    public List< CardLessDetail > getCardLessDetails() {
        return cardLessDetails;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append( "[" );
        int i = 0;
        for ( CardLessDetail cardLessDetail : cardLessDetails ) {
            if ( i > 0 ) {
                stringBuilder.append( "," );
            }
            stringBuilder.append( cardLessDetail.toString() );
        }
        stringBuilder.append( "]" );
        return String.format( "[nric=%s, name=%s, contactMode=%s, mobile=%s, email=%s, cardLessDetails=%s]", this.nric, this.name, this.preferredContactMode, this.preferredMobile, this.preferredEmail, stringBuilder.toString() );
    }
}
