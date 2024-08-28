package com.stee.spfcore.model.corporateCard;

import java.util.Comparator;

public class CardBookingLeastUsedComparator implements Comparator< CardBooking > {
    public CardBookingLeastUsedComparator() {
        // DO NOTHING
    }

    @Override
    public int compare( CardBooking c1, CardBooking c2 ) {
        int ct1 = 0;
        int ct2 = 0;
        if ( c1 != null ) {
            ct1 = c1.getNumberOfPreviousApprovedBookings();
        }
        if ( c2 != null ) {
            ct2 = c2.getNumberOfPreviousApprovedBookings();
        }
        return Integer.compare( ct1, ct2 );
    }
}
