package com.stee.spfcore.model.corporateCard;

import java.util.Comparator;
import java.util.Date;

public class CardBookingFcfsComparator implements Comparator< CardBooking > {
    public CardBookingFcfsComparator() {
        // DO NOTHING
    }

    @Override
    public int compare( CardBooking c1, CardBooking c2 ) {
        long ct1 = 0L;
        long ct2 = 0L;
        if ( c1 != null ) {
            Date d1 = c1.getUpdateDateTime();
            if ( d1 != null ) {
                ct1 = d1.getTime();
            }
        }
        if ( c2 != null ) {
            Date d2 = c2.getUpdateDateTime();
            if ( d2 != null ) {
                ct2 = d2.getTime();
            }
        }
        return Long.compare( ct1, ct2 );
    }
}
