package com.stee.spfcore.webapi.dao;

import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stee.spfcore.webapi.model.calendar.PublicHoliday;
import com.stee.spfcore.webapi.model.corporateCard.CardBooking;
import com.stee.spfcore.webapi.model.corporateCard.CardBookingStatus;
import com.stee.spfcore.webapi.model.corporateCard.CardType;
import com.stee.spfcore.webapi.model.corporateCard.CardTypeDetail;
import com.stee.spfcore.webapi.model.corporateCard.CardTypeDetailStatus;
import com.stee.spfcore.webapi.model.membership.Membership;
import com.stee.spfcore.webapi.model.personnel.PersonalDetail;

@Repository
public class CorporateCardDAO {
	
	private EntityManager entityManager;
	private static final Logger logger = LoggerFactory.getLogger(CorporateCardDAO.class);
	
	@Autowired
	public CorporateCardDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public CardBooking getCardBooking ( String cardBookingId ){
		logger.info("Get Card Booking");
		Session session = entityManager.unwrap(Session.class);
		
		return (CardBooking) session.get(CardBooking.class, cardBookingId);
	}
	
	public CardTypeDetail getCardTypeDetail ( String cardTypeId ){
		logger.info("Get Card Type Detail");
		Session session = entityManager.unwrap(Session.class);
		
		return (CardTypeDetail) session.get(CardTypeDetail.class, cardTypeId);
	}
	
	public Membership getMembership ( String nric ) {
		logger.info("Get Membership");
		Session session = entityManager.unwrap(Session.class);
		
		return (Membership) session.get(Membership.class, nric);
	}
		
	@SuppressWarnings("unchecked")
	public List< CardBooking > getCardBookings( Date startDate, Date endDate, List< String > cardTypeIds, List< CardBookingStatus > cardBookingStatuses, String nric ) {
		logger.info("Get Card Bookings");
		Session session = entityManager.unwrap(Session.class);
		
        StringBuilder queryString = new StringBuilder();
        queryString.append( "select cb from CardBooking cb where 1=1 " );
        if ( startDate != null ) {
            queryString.append( "and :startDate <= cb.dateStartEnd.end " );
        }
        if ( endDate != null ) {
            queryString.append( "and cb.dateStartEnd.start <= :endDate " );
        }
        if ( cardTypeIds != null ) {
            if ( cardTypeIds.size() > 0 ) {
                queryString.append( "and cb.cardTypeId in :cardTypeIds " );
            }
        }
        if ( cardBookingStatuses != null ) {
            if ( cardBookingStatuses.size() > 0 ) {
                queryString.append( "and cb.status in :statuses " );
            }
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
        if ( cardTypeIds != null ) {
            if ( cardTypeIds.size() > 0 ) {
                query.setParameterList( "cardTypeIds", cardTypeIds );
            }
        }
        if ( cardBookingStatuses != null ) {
            if ( cardBookingStatuses.size() > 0 ) {
                query.setParameterList( "statuses", cardBookingStatuses );
            }
        }
        if ( nric != null ) {
            query.setParameter( "nric", nric );
        }
        
        return ( List< CardBooking > ) query.list();
    }
	
	@SuppressWarnings("unchecked")
	public CardBooking getApprovedCardBooking( Date startDate, Date endDate, String allocatedSerialNumber ) {
		logger.info("Get Approved Card Booking");
		Session session = entityManager.unwrap(Session.class);
		
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

	@SuppressWarnings( "unchecked" )
    public List< CardTypeDetail > getAllCreatedPcwfGroupCardTypeDetails( boolean includePcwfCards, String department, String coShareDepartment ) {
		logger.info("Get All Created PCWF Group Card Type Details");
		Session session = entityManager.unwrap(Session.class);
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
	
	@SuppressWarnings("unchecked")
	public List<PublicHoliday> getPublicHolidays( Date date ) {
		logger.info("Get Public Holidays");
		Session session = entityManager.unwrap(Session.class);

        StringBuilder builder = new StringBuilder("select rec from PublicHoliday rec where rec.startDate <= :date and :date <= rec.endDate");
        Query query = session.createQuery(builder.toString());
        query.setParameter("date", date);
        return (List<PublicHoliday>) query.list();
	}

	@SuppressWarnings("unchecked")
	public int getNumberOfCardBookingCancellations ( String nric, Date startDate, Date endDate ){
		logger.info("Get Number of Card Booking Cancellations");
		Session session = entityManager.unwrap(Session.class);
		
		StringBuilder queryString = new StringBuilder();
		
		queryString.append( "select cb from CardBooking cb where cb.nric = :nric and cb.status = :status "
				+ "and updateDateTime between :startDate and :endDate" );
		
		Query<CardBooking> query = session.createQuery( queryString.toString() );
		
		query.setParameter( "nric", nric);
		query.setParameter( "status", CardBookingStatus.CANCELLED);
		query.setParameter( "startDate", startDate);
		query.setParameter( "endDate", endDate);
		
		List<CardBooking> results = query.list();
		return results.size();
	}
	
	public String addCardBooking ( CardBooking cardBooking  ) {
		logger.info("Add Card Booking");
		Session session = entityManager.unwrap(Session.class);
		
	    session.save( cardBooking );
	    session.flush();
	    
	    return cardBooking.getId();
	}
	
	public void updateCardBooking ( CardBooking cardBooking  ) {
		logger.info("Update Card Booking");
		Session session = entityManager.unwrap(Session.class);
		
	    session.merge( cardBooking );
	    session.flush();
	}
	
	public String addCardTypeDetail ( CardTypeDetail cardTypeDetail  ) {
		logger.info("Add Card Type Detail");
		Session session = entityManager.unwrap(Session.class);
		
	    session.save( cardTypeDetail );
	    session.flush();
	    
	    return cardTypeDetail.getId();
	}
	
	public void updateCardTypeDetail ( CardTypeDetail cardTypeDetail  ) {
		logger.info("Update Card Type Detail");
		Session session = entityManager.unwrap(Session.class);
		
	    session.merge( cardTypeDetail );
	    session.flush();
	}

	public PersonalDetail getPersonal(String nric) {
		logger.info("Get Personal Detail");
		Session session = entityManager.unwrap(Session.class);
		
		return (PersonalDetail)session.get(PersonalDetail.class, nric);
	}
		
}
