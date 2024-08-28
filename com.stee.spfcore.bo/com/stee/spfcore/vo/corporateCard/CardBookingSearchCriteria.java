package com.stee.spfcore.vo.corporateCard;

import java.util.Date;
import java.util.List;

public class CardBookingSearchCriteria {
    private Date bookingStartDateStart;
    private Date bookingStartDateEnd;
    private String applicantName;
    private String applicantIdNo;
    private List< String > cardTypeIds;
    private int maxResults;

    public CardBookingSearchCriteria() {
        // DO NOTHING
    }

    public Date getBookingStartDateStart() {
        return bookingStartDateStart;
    }

    public void setBookingStartDateStart( Date bookingStartDateStart ) {
        this.bookingStartDateStart = bookingStartDateStart;
    }

    public Date getBookingStartDateEnd() {
        return bookingStartDateEnd;
    }

    public void setBookingStartDateEnd( Date bookingStartDateEnd ) {
        this.bookingStartDateEnd = bookingStartDateEnd;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName( String applicantName ) {
        this.applicantName = applicantName;
    }

    public String getApplicantIdNo() {
        return applicantIdNo;
    }

    public void setApplicantIdNo( String applicantIdNo ) {
        this.applicantIdNo = applicantIdNo;
    }

    public List< String > getCardTypeIds() {
        return cardTypeIds;
    }

    public void setCardTypeIds( List< String > cardTypeIds ) {
        this.cardTypeIds = cardTypeIds;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults( int maxResults ) {
        this.maxResults = maxResults;
    }
}
