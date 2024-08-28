package com.stee.spfcore.vo.corporateCard;

import java.util.Date;

import com.stee.spfcore.model.DateStartEnd;
import com.stee.spfcore.model.corporateCard.CardBooking;
import com.stee.spfcore.utils.ConvertUtil;

public class CardLessDetail {
    private String serialNumber;
    private Date bookingStartDate;
    private Date bookingEndDate;

    public CardLessDetail( String serialNumber, CardBooking booking ) {
        this.serialNumber = serialNumber;
        this.bookingStartDate = this.deriveBookingStartDate( booking );
        this.bookingEndDate = this.deriveBookingEndDate( booking );
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber( String serialNumber ) {
        this.serialNumber = serialNumber;
    }

    public Date getBookingStartDate() {
        return bookingStartDate;
    }

    public void setBookingStartDate( Date bookingStartDate ) {
        this.bookingStartDate = bookingStartDate;
    }

    public Date getBookingEndDate() {
        return bookingEndDate;
    }

    public void setBookingEndDate( Date bookingEndDate ) {
        this.bookingEndDate = bookingEndDate;
    }

    private Date deriveBookingStartDate( CardBooking booking ) {
        Date bookingDateStart = null;
        if ( booking != null ) {
            DateStartEnd dateStartEnd = booking.getDateStartEnd();
            if ( dateStartEnd != null ) {
                bookingDateStart = dateStartEnd.getStart();
            }
        }
        return bookingDateStart;
    }

    private Date deriveBookingEndDate( CardBooking booking ) {
        Date bookingDateEnd = null;
        if ( booking != null ) {
            DateStartEnd dateStartEnd = booking.getDateStartEnd();
            if ( dateStartEnd != null ) {
                bookingDateEnd = dateStartEnd.getEnd();
            }
        }
        return bookingDateEnd;
    }

    public String toString() {
        String startDate = ConvertUtil.convertDateToDateString( this.bookingStartDate );
        String endDate = ConvertUtil.convertDateToDateString( this.bookingEndDate );
        return String.format( "[serialNumber=%s, startDate=%s, endDate=%s]", this.serialNumber, startDate, endDate );
    }
}
