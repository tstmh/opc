package com.stee.spfcore.webapi.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.stee.spfcore.webapi.model.rateThisWebsite.RateThisWebsiteResponse;

@Repository
public class RateThisWebsiteDAO {
	private EntityManager entityManager;

	public static final Logger LOGGER = Logger
			.getLogger(RateThisWebsiteDAO.class.getName());
	
	public RateThisWebsiteDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public void addRating(RateThisWebsiteResponse ratingReponse) {
		Session session = entityManager.unwrap(Session.class);
		session.save( ratingReponse );
		session.flush();
	}
	
	public void updateRating(RateThisWebsiteResponse ratingReponse) {
		Session session = entityManager.unwrap(Session.class);
		session.merge(ratingReponse);
		session.flush();
	}

	// returns all ratings for a given day
	@SuppressWarnings( "unchecked" )
	public List <RateThisWebsiteResponse> getRatingsForTheDay(Date ratingDate) {
		Session session = entityManager.unwrap(Session.class);
		if (ratingDate != null) {
			// Create date 00h00
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(ratingDate);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			
			Date minDate = new Date(calendar.getTimeInMillis());
			
			calendar.add(Calendar.DAY_OF_MONTH, 1);

			Date maxDate = new Date(calendar.getTimeInMillis());
		  
		    Criteria criteria = session
					.createCriteria(RateThisWebsiteResponse.class);
		    criteria.add( Restrictions.ge("updateDateTime", minDate) );
		    criteria.add( Restrictions.lt("updateDateTime", maxDate) ); 
		    
			return (List <RateThisWebsiteResponse>) criteria.list();
		}
		else{
			LOGGER.log(Level.INFO, "getRatingsForTheDay ratingDate is null");
			
		}
		return null;
	}
}
