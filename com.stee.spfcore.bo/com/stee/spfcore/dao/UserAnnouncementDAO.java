package com.stee.spfcore.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.stee.spfcore.model.userAnnouncement.UserAnnouncement;
import com.stee.spfcore.model.userAnnouncement.UserAnnouncementModule;

public class UserAnnouncementDAO {

    public void addUserAnnouncements( List< UserAnnouncement > announcements, String requester ) {
        SessionFactoryUtil.setUser( requester );
        Session session = SessionFactoryUtil.getCurrentSession();
        for ( UserAnnouncement announcement : announcements ) {
            session.save( announcement );
        }
        session.flush();
        session.clear();
    }

    public String addUserAnnouncement( UserAnnouncement announcement, String requester ) {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getCurrentSession();

        session.save( announcement );

        session.flush();

        return announcement.getId();
    }

    public void updateUserAnnouncement( UserAnnouncement userAnnouncement, String requester ) {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getCurrentSession();

        session.merge( userAnnouncement );

        session.flush();
    }

    public UserAnnouncement getUserAnnouncement( String id ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        return ( UserAnnouncement ) session.get( UserAnnouncement.class, id );
    }

    public UserAnnouncement getUserAnnouncement( UserAnnouncementModule module, String referenceId, String nric, String contentId ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        String queryString = "select u from UserAnnouncement u where u.module = :module and u.referenceId = :referenceId and " + "u.nric = :nric and u.contentId = :contentId";

        Query query = session.createQuery( queryString );
        query.setParameter( "module", module );
        query.setParameter( "referenceId", referenceId );
        query.setParameter( "nric", nric );
        query.setParameter( "contentId", contentId );

        List< ? > list = query.list();

        if ( list.isEmpty() ) {
            return null;
        }
        else {
            return ( UserAnnouncement ) list.get( 0 );
        }
    }

    @SuppressWarnings( "unchecked" )
    public List< UserAnnouncement > getUserAnnouncements( String nric, int maxAge ) {

        Date startDate = new Date();

        GregorianCalendar cal = new GregorianCalendar();
        cal.add( Calendar.DATE, -( maxAge ) );
        Date endDate = cal.getTime();

        Session session = SessionFactoryUtil.getCurrentSession();

        session.clear();
        Query query = session.createQuery( "From UserAnnouncement u where u.title is not null and u.nric = :nric and u.publishDate <= :startDate and u.publishDate > :endDate order by u.publishDate desc" );

        query.setParameter( "nric", nric );
        query.setParameter( "startDate", startDate );
        query.setParameter( "endDate", endDate );

        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< UserAnnouncement > getNonPublishedUserAnnouncements( UserAnnouncementModule module, String referenceId ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        Query query = session.createQuery( "From UserAnnouncement u where u.referenceId = :referenceId " + "and u.module = :module and u.publishDate > current_timestamp()" );

        query.setParameter( "referenceId", referenceId );
        query.setParameter( "module", module );

        return query.list();
    }

    public void deleteUserAnnouncement( UserAnnouncement userAnnouncement, String requester ) {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getCurrentSession();

        session.delete( userAnnouncement );
    }

    @SuppressWarnings( "unchecked" )
    public List< UserAnnouncement > getNonPublishedUserAnnouncements( UserAnnouncementModule module, String referenceId, String nric ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        Query query = session.createQuery( "From UserAnnouncement u where u.referenceId = :referenceId " + "and u.nric = :nric and u.module = :module and u.publishDate > current_timestamp()" );

        query.setParameter( "referenceId", referenceId );
        query.setParameter( "nric", nric );
        query.setParameter( "module", module );

        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< String > getUserAnnouncementNrics( UserAnnouncementModule module, String referenceId, String contentId ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        Query query = session.createQuery( "select u.nric From UserAnnouncement u where u.referenceId = :referenceId " + "and u.contentId = :contentId and u.module = :module" );

        query.setParameter( "referenceId", referenceId );
        query.setParameter( "contentId", contentId );
        query.setParameter( "module", module );

        return query.list();
    }

    public long getUserAnnouncementCount( UserAnnouncementModule module, String referenceId, String nric ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        Query query = session.createQuery( "select count (u) From UserAnnouncement u where u.referenceId = :referenceId " + "and u.nric = :nric and u.module = :module" );

        query.setParameter( "referenceId", referenceId );
        query.setParameter( "nric", nric );
        query.setParameter( "module", module );

        return ( long ) query.uniqueResult();
    }

}
