package com.stee.spfcore.service.corporateCard;

import java.util.Date;
import java.util.List;

import com.stee.spfcore.model.corporateCard.BroadcastFrequency;
import com.stee.spfcore.model.corporateCard.CardAutoBroadcast;
import com.stee.spfcore.model.corporateCard.CardBooking;
import com.stee.spfcore.model.corporateCard.CardBookingStatus;
import com.stee.spfcore.model.corporateCard.CardType;
import com.stee.spfcore.model.corporateCard.CardTypeDetail;
import com.stee.spfcore.model.membership.Membership;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.vo.corporateCard.CardBookingSearchCriteria;
import com.stee.spfcore.vo.corporateCard.CardBookingStatusUpdate;
import com.stee.spfcore.vo.corporateCard.CardTypeBookingsResult;
import com.stee.spfcore.vo.corporateCard.CardTypeLessDetail;
import com.stee.spfcore.vo.corporateCard.UpdateCardBookingStatusesResult;

public interface ICorporateCardService {
    public PersonalDetail getPersonal( String nric ) throws CorporateCardException;

    public Membership getMembership( String nric ) throws CorporateCardException;

    public CardTypeDetail getCardTypeDetail( String cardTypeId ) throws CorporateCardException;

    public CardTypeDetail getCardTypeDetail( CardType cardType, String department, String cardTypeName ) throws CorporateCardException;

    public List< CardTypeDetail > getAllCardTypeDetails() throws CorporateCardException;

    public List< CardTypeDetail > getAllCardTypeDetailsForWpo() throws CorporateCardException;

    public List< CardTypeDetail > getAllCardTypeDetailsForUwo( String department ) throws CorporateCardException;

    public List< CardTypeDetail > getAllEligibleCardTypeDetails( boolean isPcwfMember, String department ) throws CorporateCardException;

    public List< CardTypeDetail > getAllCreatedEligibleCardTypeDetails( boolean isPcwfMember, String department ) throws CorporateCardException;
    
    public List< CardTypeLessDetail > getAllCardTypeLessDetails( boolean includeAvailabilityToday ) throws CorporateCardException;

    public List< CardTypeLessDetail > getAllCreatedCardTypeLessDetailsForWpo( boolean includeAvailabilityToday ) throws CorporateCardException;

    public List< CardTypeLessDetail > getAllCreatedCardTypeLessDetailsForUwo( String department, boolean includeAvailabilityToday ) throws CorporateCardException;
    
    public List< CardTypeLessDetail > getAllCardTypeLessDetailsForWpo( boolean includeAvailabilityToday ) throws CorporateCardException;

    public List< CardTypeLessDetail > getAllCardTypeLessDetailsForUwo( String department, boolean includeAvailabilityToday ) throws CorporateCardException;

    public List< CardTypeLessDetail > getAllCardTypeLessDetails( BroadcastFrequency frequency, boolean includeAvailabilityToday ) throws CorporateCardException;

    public List< CardTypeLessDetail > getAllCreatedCardTypeLessDetails( BroadcastFrequency frequency, boolean includeAvailabilityToday ) throws CorporateCardException;
    
    public String addCardTypeDetail( CardTypeDetail cardTypeDetail, String requester ) throws CorporateCardException;

    public String updateCardTypeDetail( CardTypeDetail cardTypeDetail, String requester ) throws CorporateCardException;

    public CardBooking getCardBooking( String cardBookingId ) throws CorporateCardException;

    public List< CardBooking > getCardBookings( Date startDate, Date endDate, List< String > cardTypeIds, List< CardBookingStatus > cardBookingStatuses, String nric ) throws CorporateCardException;

    public int getCardBookingsCount( Date startDate, Date endDate, List< String > cardTypeIds, List< CardBookingStatus > cardBookingStatuses, String nric ) throws CorporateCardException;

    public List< CardBooking > searchCardBookings( CardBookingSearchCriteria searchCriteria ) throws CorporateCardException;

    public CardBooking getCardBookingMoreDetails( String cardBookingId ) throws CorporateCardException;

    public void addCardBooking( CardBooking cardBooking, String requester ) throws CorporateCardException;

    public void updateCardBooking( CardBooking cardBooking, String requester ) throws CorporateCardException;

    public void updateCardBookingRemarks( String cardBookingId, String remarks, String requester ) throws CorporateCardException;

    public CardBooking getApprovedCardBooking( Date startDate, Date endDate, String allocatedSerialNumber ) throws CorporateCardException;

    public CardAutoBroadcast getCardAutoBroadcast( String cardAutoBroadcastId ) throws CorporateCardException;

    public CardAutoBroadcast getCardAutoBroadcast( CardType cardType, String department ) throws CorporateCardException;

    public List< CardAutoBroadcast > getCardAutoBroadcasts( BroadcastFrequency frequency ) throws CorporateCardException;

    public void addOrUpdateCardAutoBroadcast( CardAutoBroadcast cardAutoBroadcast ) throws CorporateCardException;

    public List< CardTypeBookingsResult > processCardBookings( Date processingDate ) throws CorporateCardException;

    public UpdateCardBookingStatusesResult updateCardBookingStatuses( List< CardBookingStatusUpdate > cardbookingsStatusUpdateList ) throws CorporateCardException;

    public void sendCancelCardBookingNotifications( CardBooking cardBooking ) throws CorporateCardException;
    
    public int getNumberOfCardBookingCancellations (String nric) throws CorporateCardException;
    
    public void sendEmailForApprovedBookings(CardBooking cardBooking) throws CorporateCardException;
    
    public void sendBroadcast (List <String> emailList, List <String> smsList, CardTypeDetail cardTypeDetail, String subject, String body, String smsText) throws CorporateCardException;
    
    public void sendEmailForSuccessfulBookings(CardBooking cardBooking) throws CorporateCardException;
    
    public void sendEmailForCancelledBookings (CardBooking cardBooking) throws CorporateCardException;
    
    public void sendEmailForCancelledBooking (CardBooking cardBooking) throws CorporateCardException;
}
