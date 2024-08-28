package com.stee.spfcore.webapi.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.stee.spfcore.webapi.model.userAnnouncement.UserAnnouncement;

@Repository
public class UserAnnouncementDAO {
	private static final Logger logger = LoggerFactory.getLogger(UserAnnouncementDAO.class);
	private EntityManager entityManager;
	private int maxUserAnnouncementAge = 90;
	
	public UserAnnouncementDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
    @SuppressWarnings( "unchecked" )
    public List< UserAnnouncement > getUserAnnouncements( String nric) {
        Date startDate = new Date();

        GregorianCalendar cal = new GregorianCalendar();
        cal.add( Calendar.DATE, -( maxUserAnnouncementAge ) );
        Date endDate = cal.getTime();

        Session session = entityManager.unwrap(Session.class);

        session.clear();
        Query query = session.createQuery( "From UserAnnouncement u where u.title is not null and u.nric = :nric and u.publishDate <= :startDate and u.publishDate > :endDate order by u.publishDate desc" );

        query.setParameter( "nric", nric );
        query.setParameter( "startDate", startDate );
        query.setParameter( "endDate", endDate );

        return query.list();
    }
	
	
}
