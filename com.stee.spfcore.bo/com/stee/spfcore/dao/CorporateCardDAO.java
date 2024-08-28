package com.stee.spfcore.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.stee.spfcore.model.corporateCard.BroadcastFrequency;
import com.stee.spfcore.model.corporateCard.CardAutoBroadcast;
import com.stee.spfcore.model.corporateCard.CardBooking;
import com.stee.spfcore.model.corporateCard.CardBookingStatus;
import com.stee.spfcore.model.corporateCard.CardType;
import com.stee.spfcore.model.corporateCard.CardTypeDetail;
import com.stee.spfcore.model.corporateCard.CardTypeDetailStatus;
import com.stee.spfcore.model.membership.Membership;
import com.stee.spfcore.model.personnel.PersonalDetail;

public class CorporateCardDAO {
    public PersonalDetail getPersonal( String nric ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        return ( PersonalDetail ) session.get( PersonalDetail.class, nric );
    }

    public Membership getMembership( String nric ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        return ( Membership ) session.get( Membership.class, nric );
    }

    public CardTypeDetail getCardTypeDetail( String cardTypeId ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        return ( CardTypeDetail ) session.get( CardTypeDetail.class, cardTypeId );
    }

    @SuppressWarnings( "unchecked" )
    public CardTypeDetail getCardTypeDetail( CardType cardType, String department, String cardTypeName ) {
        if ( cardType == CardType.PCWF ) {
            department = null;
        }

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        queryString.append( "select coc from CardTypeDetail coc where 1=1 " );
        if ( cardType != null ) {
            queryString.append( "and coc.type = :cardType " );
        }
        if ( department != null ) {
            queryString.append( "and coc.department = :department " );
        }
        if ( cardTypeName != null ) {
            queryString.append( "and coc.name = :cardTypeName " );
        }
        Query query = session.createQuery( queryString.toString() );
        if ( cardType != null ) {
            query.setParameter( "cardType", cardType );
        }
        if ( department != null ) {
            query.setParameter( "department", department );
        }
        if ( cardTypeName != null ) {
            query.setParameter( "cardTypeName", cardTypeName );
        }

        List< CardTypeDetail > results = ( List< CardTypeDetail > ) query.list();
        CardTypeDetail result = null;
        if ( results != null && results.size() > 0  ) {
            result = results.get( 0 );
        }
        return result;
    }

    @SuppressWarnings( "unchecked" )
    public List< CardTypeDetail > getCardTypeDetails( List< String > cardTypeIds ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        queryString.append( "select coc from CardTypeDetail coc where 1=1 " );
        if ( cardTypeIds != null ) {
            queryString.append( "and coc.id in :cardTypeIds " );
        }
        Query query = session.createQuery( queryString.toString() );
        if ( cardTypeIds != null ) {
            query.setParameterList( "cardTypeIds", cardTypeIds );
        }
        return ( List< CardTypeDetail > ) query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< CardTypeDetail > getAllPcwfCardTypeDetails() {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        queryString.append( "select coc from CardTypeDetail coc where ( coc.type = :pcwfCardType ) " );
        queryString.append( "order by coc.type asc, coc.name asc" );
        Query query = session.createQuery( queryString.toString() );
        query.setParameter( "pcwfCardType", CardType.PCWF );
        return ( List< CardTypeDetail > ) query.list();
    }
    
    @SuppressWarnings( "unchecked" )
    public List< CardTypeDetail > getAllPcwfCreatedCardTypeDetails() {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        queryString.append( "select coc from CardTypeDetail coc where ( coc.type = :pcwfCardType ) and ( coc.status = :status ) " );
        queryString.append( "order by coc.type asc, coc.name asc" );
        Query query = session.createQuery( queryString.toString() );
        query.setParameter( "pcwfCardType", CardType.PCWF );
        query.setParameter("status", CardTypeDetailStatus.CREATED);
        return ( List< CardTypeDetail > ) query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< CardTypeDetail > getAllGroupCardTypeDetails( List< String > departments ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        queryString.append( "select coc from CardTypeDetail coc where ( coc.type = :pcwfCardType ) " );
        if ( departments != null ) {
            queryString.append( "and ( coc.department in (:departments) ) " );
        }
        queryString.append( "order by coc.type asc, coc.name asc" );
        Query query = session.createQuery( queryString.toString() );
        query.setParameter( "pcwfCardType", CardType.GROUP );
        if ( departments != null ) {
            query.setParameterList( "departments", departments );
        }
        return ( List< CardTypeDetail > ) query.list();
    }
    
    @SuppressWarnings( "unchecked" )
    public List< CardTypeDetail > getAllGroupCreatedCardTypeDetails( List< String > departments ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        queryString.append( "select coc from CardTypeDetail coc where ( coc.type = :pcwfCardType )  and ( coc.status = :status ) " );
        if ( departments != null ) {
            queryString.append( "and ( coc.department in (:departments) ) " );
        }
        queryString.append( "order by coc.type asc, coc.name asc" );
        Query query = session.createQuery( queryString.toString() );
        query.setParameter( "pcwfCardType", CardType.GROUP );
        if ( departments != null ) {
            query.setParameterList( "departments", departments );
        }
        query.setParameter("status", CardTypeDetailStatus.CREATED);
        return ( List< CardTypeDetail > ) query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< CardTypeDetail > getAllPcwfGroupCardTypeDetails( boolean includePcwfCards, String department, String coShareDepartment ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        queryString.append( "select coc from CardTypeDetail coc where " );
        if ( includePcwfCards ) {
            queryString.append( "( coc.type = :pcwfCardType ) or " );
        }
        if ( department != null ) {
            queryString.append( "( coc.type = :groupCardType and coc.department = :department ) or " );
        }
        else {
            queryString.append( "( coc.type = :groupCardType ) or " );
        }
        if ( coShareDepartment != null ) {
            queryString.append( "( coc.type = :groupCardType and :coShareDepartment member of coc.coShareDepartments )" );
        }
        else {
            queryString.append( "( coc.type = :groupCardType ) " );
        }
        queryString.append( "order by coc.type asc, coc.name asc" );
        Query query = session.createQuery( queryString.toString() );
        if ( includePcwfCards ) {
            query.setParameter( "pcwfCardType", CardType.PCWF );
        }
        query.setParameter( "groupCardType", CardType.GROUP );
        if ( department != null ) {
            query.setParameter( "department", department );
        }
        if ( coShareDepartment != null ) {
            query.setParameter( "coShareDepartment", coShareDepartment );
        }
        return ( List< CardTypeDetail > ) query.list();
    }
    
    @SuppressWarnings( "unchecked" )
    public List< CardTypeDetail > getAllCreatedPcwfGroupCardTypeDetails( boolean includePcwfCards, String department, String coShareDepartment ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        //To retrieve only CREATED card type details
        queryString.append( "select coc from CardTypeDetail coc where " );
        if ( includePcwfCards ) {
            queryString.append( "( coc.type = :pcwfCardType and coc.status = :status) or " );
        }
        if ( department != null ) {
            queryString.append( "( coc.type = :groupCardType and coc.department = :department and coc.status = :status ) or " );
        }
        else {
            queryString.append( "( coc.type = :groupCardType and coc.status = :status ) or " );
        }
        if ( coShareDepartment != null ) {
            queryString.append( "( coc.type = :groupCardType and :coShareDepartment member of coc.coShareDepartments and coc.status = :status )" );
        }
        else {
            queryString.append( "( coc.type = :groupCardType and coc.status = :status ) " );
        }
        
        queryString.append( "order by coc.type asc, coc.name asc" );
        Query query = session.createQuery( queryString.toString() );
        if ( includePcwfCards ) {
            query.setParameter( "pcwfCardType", CardType.PCWF );
        }
        query.setParameter( "groupCardType", CardType.GROUP );
        if ( department != null ) {
            query.setParameter( "department", department );
        }
        if ( coShareDepartment != null ) {
            query.setParameter( "coShareDepartment", coShareDepartment );
        }
        
        query.setParameter("status", CardTypeDetailStatus.CREATED);
        return ( List< CardTypeDetail > ) query.list();
    }

    public void addCardTypeDetail( CardTypeDetail cardTypeDetail, String requester ) {
        if ( cardTypeDetail.getType() == CardType.PCWF ) {
            cardTypeDetail.setDepartment( null );
        }
        SessionFactoryUtil.setUser( requester );
        cardTypeDetail.setUpdater( requester );
        cardTypeDetail.setUpdateDateTime( new Date() );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        session.save( cardTypeDetail );
        session.flush();
    }

    public void updateCardTypeDetail( CardTypeDetail cardTypeDetail, String requester ) {
        if ( cardTypeDetail.getType() == CardType.PCWF ) {
            cardTypeDetail.setDepartment( null );
        }
        SessionFactoryUtil.setUser( requester );
        cardTypeDetail.setUpdater( requester );
        cardTypeDetail.setUpdateDateTime( new Date() );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        session.merge( cardTypeDetail );
        session.flush();
    }

    public CardBooking getCardBooking( String cardBookingId ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        return ( CardBooking ) session.get( CardBooking.class, cardBookingId );
    }

    @SuppressWarnings( "unchecked" )
    public List< CardBooking > getCardBookings( Date bookingStartDateStart, Date bookingStartDateEnd, String applicantIdNo, List< String > cardTypeIds, List< CardBookingStatus > cardBookingStatuses, int maxResults ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        queryString.append( "select cb from CardBooking cb where 1=1 " );
        if ( bookingStartDateStart != null ) {
            queryString.append( "and :bookingStartDateStart <= cb.dateStartEnd.start " );
        }
        if ( bookingStartDateEnd != null ) {
            queryString.append( "and cb.dateStartEnd.start <= :bookingStartDateEnd " );
        }
        if ( cardTypeIds != null && !cardTypeIds.isEmpty()) {
            queryString.append( "and cb.cardTypeId in :cardTypeIds " );
        }
        if ( applicantIdNo != null ) {
            queryString.append( "and cb.nric like :applicantIdNo " );
        }
        if ( cardBookingStatuses != null && cardBookingStatuses.size() > 0) {
                queryString.append( "and cb.status in :statuses " );
        }

        Query query = session.createQuery( queryString.toString() );
        if ( bookingStartDateStart != null ) {
            query.setParameter( "bookingStartDateStart", bookingStartDateStart );
        }
        if ( bookingStartDateEnd != null ) {
            query.setParameter( "bookingStartDateEnd", bookingStartDateEnd );
        }
        if ( cardTypeIds != null && !cardTypeIds.isEmpty()) {
                query.setParameterList( "cardTypeIds", cardTypeIds );
        }
        if ( applicantIdNo != null ) {
            String tmpApplicantIdNo = "%" + applicantIdNo.toUpperCase() + "%";
            query.setParameter( "applicantIdNo", tmpApplicantIdNo );
        }
        if ( cardBookingStatuses != null &&cardBookingStatuses.size() > 0 ) {
                query.setParameterList( "statuses", cardBookingStatuses );
        }
        if ( maxResults > 0 ) {
            query.setMaxResults( maxResults );
        }
        return ( List< CardBooking > ) query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< CardBooking > getCardBookings( Date startDate, Date endDate, List< String > cardTypeIds, List< CardBookingStatus > cardBookingStatuses, String nric ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        queryString.append( "select cb from CardBooking cb where 1=1 " );
        if ( startDate != null ) {
            queryString.append( "and :startDate <= cb.dateStartEnd.end " );
        }
        if ( endDate != null ) {
            queryString.append( "and cb.dateStartEnd.start <= :endDate " );
        }
        if ( cardTypeIds != null && !cardTypeIds.isEmpty()) {
                queryString.append( "and cb.cardTypeId in :cardTypeIds " );
        }
        if ( cardBookingStatuses != null && cardBookingStatuses.size() > 0) {
                queryString.append( "and cb.status in :statuses " );
        }
        if ( nric != null ) {
            queryString.append( "and cb.nric = :nric " );
        }
        Query query = session.createQuery( queryString.toString() );
        if ( startDate != null ) {
            query.setParameter( "startDate", startDate );
        }
        if ( endDate != null ) {
            query.setParameter( "endDate", endDate );
        }
        if ( cardTypeIds != null && !cardTypeIds.isEmpty() ) {
                query.setParameterList( "cardTypeIds", cardTypeIds );
        }
        if ( cardBookingStatuses != null && cardBookingStatuses.size() > 0) {
                query.setParameterList( "statuses", cardBookingStatuses );
        }
        if ( nric != null ) {
            query.setParameter( "nric", nric );
        }
        return ( List< CardBooking > ) query.list();
    }

    public int getCardBookingsCount( Date startDate, Date endDate, List< String > cardTypeIds, List< CardBookingStatus > cardBookingStatuses, String nric ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        queryString.append( "select count(cb) from CardBooking cb where 1=1 " );
        if ( startDate != null ) {
            queryString.append( "and :startDate <= cb.dateStartEnd.end " );
        }
        if ( endDate != null ) {
            queryString.append( "and cb.dateStartEnd.start <= :endDate " );
        }
        if ( cardTypeIds != null && !cardTypeIds.isEmpty()) {
                queryString.append( "and cb.cardTypeId in :cardTypeIds " );
        }
        if ( cardBookingStatuses != null && cardBookingStatuses.size() > 0 ) {
                queryString.append( "and cb.status in :statuses " );
        }
        if ( nric != null ) {
            queryString.append( "and cb.nric = :nric " );
        }
        Query query = session.createQuery( queryString.toString() );
        if ( startDate != null ) {
            query.setParameter( "startDate", startDate );
        }
        if ( endDate != null ) {
            query.setParameter( "endDate", endDate );
        }
        if ( cardTypeIds != null && !cardTypeIds.isEmpty() ) {
                query.setParameterList( "cardTypeIds", cardTypeIds );
        }
        if ( cardBookingStatuses != null && cardBookingStatuses.size() > 0) {
                query.setParameterList( "statuses", cardBookingStatuses );
        }
        if ( nric != null ) {
            query.setParameter( "nric", nric );
        }
        int count = 0;
        List< ? > results = query.list();
        if ( !results.isEmpty() ) {
            Object result = results.get( 0 );
            if ( result instanceof Long ) {
                count = ( ( Long ) result ).intValue();
            }
            else if ( result instanceof Integer ) {
                count = ( ( Integer ) result ).intValue();
            }
        }
        return count;
    }

    public void addCardBooking( CardBooking cardBooking, String requester ) {
        SessionFactoryUtil.setUser( requester );
        cardBooking.setUpdater( requester );
        cardBooking.setUpdateDateTime( new Date() );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        session.save( cardBooking );
        session.flush();
    }

    public void updateCardBooking( CardBooking cardBooking, String requester ) {
        SessionFactoryUtil.setUser( requester );
        cardBooking.setUpdater( requester );
        cardBooking.setUpdateDateTime( new Date() );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        session.merge( cardBooking );
        session.flush();
    }

    @SuppressWarnings( "unchecked" )
    public CardBooking getApprovedCardBooking( Date startDate, Date endDate, String allocatedSerialNumber ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        queryString.append( "select cb from CardBooking cb where cb.status = :status " );
        if ( startDate != null ) {
            queryString.append( "and cb.dateStartEnd.start = :startDate " );
        }
        if ( endDate != null ) {
            queryString.append( "and cb.dateStartEnd.end = :endDate " );
        }
        if ( allocatedSerialNumber != null ) {
            queryString.append( "and :allocatedSerialNumber member of cb.allocatedCardSerialNumbers" );
        }
        Query query = session.createQuery( queryString.toString() );
        query.setParameter( "status", CardBookingStatus.APPROVED );
        if ( startDate != null ) {
            query.setParameter( "startDate", startDate );
        }
        if ( endDate != null ) {
            query.setParameter( "endDate", endDate );
        }
        if ( allocatedSerialNumber != null ) {
            query.setParameter( "allocatedSerialNumber", allocatedSerialNumber );
        }
        CardBooking result = null;
        List< CardBooking > results = ( List< CardBooking > ) query.list();
        if ( results.size() > 0 ) {
            result = results.get( 0 );
        }
        return result;
    }

    public CardAutoBroadcast getCardAutoBroadcast( String cardAutoBroadcastId ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        return ( CardAutoBroadcast ) session.get( CardAutoBroadcast.class, cardAutoBroadcastId );
    }

    @SuppressWarnings( "unchecked" )
    public CardAutoBroadcast getCardAutoBroadcast( CardType cardType, String department ) {
        if ( cardType == CardType.PCWF ) {
            department = null;
        }

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        queryString.append( "select cab from CardAutoBroadcast cab where 1=1 " );
        if ( cardType != null ) {
            queryString.append( "and cab.type = :cardType " );
        }
        if ( department != null ) {
            queryString.append( "and cab.department = :department " );
        }
        Query query = session.createQuery( queryString.toString() );
        if ( cardType != null ) {
            query.setParameter( "cardType", cardType );
        }
        if ( department != null ) {
            query.setParameter( "department", department );
        }

        List< CardAutoBroadcast > results = ( List< CardAutoBroadcast > ) query.list();
        CardAutoBroadcast result = null;
        if ( results != null && results.size() > 0) {
                result = results.get( 0 );
        }
        return result;
    }

    @SuppressWarnings( "unchecked" )
    public List< CardAutoBroadcast > getCardAutoBroadcasts( BroadcastFrequency frequency ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        queryString.append( "select cab from CardAutoBroadcast cab where cab.active = :active " );
        if ( frequency != null ) {
            queryString.append( "and cab.frequency = :frequency " );
        }
        Query query = session.createQuery( queryString.toString() );
        query.setParameter( "active", Boolean.TRUE );
        if ( frequency != null ) {
            query.setParameter( "frequency", frequency );
        }

        return ( List< CardAutoBroadcast > ) query.list();
    }

    public void addOrUpdateCardAutoBroadcast( CardAutoBroadcast cardAutoBroadcast ) {
        CardType cardType = cardAutoBroadcast.getType();
        if ( cardType == CardType.PCWF ) {
            cardAutoBroadcast.setDepartment( null );
        }
        String department = cardAutoBroadcast.getDepartment();
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        CardAutoBroadcast currentCardAutoBroadcast = this.getCardAutoBroadcast( cardType, department );
        if ( currentCardAutoBroadcast != null ) {
            String id = currentCardAutoBroadcast.getId();
            cardAutoBroadcast.setId( id );
            session.merge( cardAutoBroadcast );
        }
        else {
            session.save( cardAutoBroadcast );
        }
        session.flush();
    }
    
    @SuppressWarnings( "unchecked" )
    public int getNumberOfCardBookingCancellations(String nric, Calendar startDateCalendar, Calendar todayDateCalendar ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        Date startDate;
        startDate = startDateCalendar.getTime();
        Date todayDate;
        todayDate = todayDateCalendar.getTime();
        queryString.append( "select cb from CardBooking cb where cb.nric = :nric and cb.status = :status and updateDateTime between :startDate and :todayDate" );
        Query query = session.createQuery( queryString.toString() );
        query.setParameter( "nric", nric );
        query.setParameter( "status", CardBookingStatus.CANCELLED );
        query.setParameter( "startDate", startDate );
        query.setParameter( "todayDate", todayDate );
        List< CardBooking > results = ( List< CardBooking > ) query.list();
        return results.size();
    }
}
