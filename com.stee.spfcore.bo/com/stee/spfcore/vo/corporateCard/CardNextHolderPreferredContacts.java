package com.stee.spfcore.vo.corporateCard;

import java.util.ArrayList;
import java.util.List;

import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.vo.personnel.PersonalPreferredContacts;

public class CardNextHolderPreferredContacts extends PersonalPreferredContacts {
    private List< CardLessDetail > cardLessDetails = new ArrayList<>();

    public CardNextHolderPreferredContacts( PersonalDetail detail ) {
        super( detail );
    }

    public List< CardLessDetail > getCardLessDetails() {
        return cardLessDetails;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append( "[" );
        for ( CardLessDetail cardLessDetail : cardLessDetails ) {
            stringBuilder.append( cardLessDetail.toString() );
        }
        stringBuilder.append( "]" );
        return String.format( "[nric=%s, name=%s, contactMode=%s, mobile=%s, email=%s, cardLessDetails=%s]", this.nric, this.name, this.preferredContactMode, this.preferredMobile, this.preferredEmail, stringBuilder.toString() );
    }
}
