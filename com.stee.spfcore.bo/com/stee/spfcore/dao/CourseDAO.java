package com.stee.spfcore.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;

import com.stee.spfcore.dao.utils.SequenceUtil;
import com.stee.spfcore.model.blacklist.BlacklistModuleNameConstants;
import com.stee.spfcore.model.category.Category;
import com.stee.spfcore.model.category.SystemCategoryIdConstants;
import com.stee.spfcore.model.course.Course;
import com.stee.spfcore.model.course.CourseParticipant;
import com.stee.spfcore.model.course.CourseStatus;
import com.stee.spfcore.model.course.CourseType;
import com.stee.spfcore.model.course.ParticipantStatus;
import com.stee.spfcore.model.course.Slot;
import com.stee.spfcore.model.course.internal.ApplicantSelectionTaskStatus;
import com.stee.spfcore.model.course.internal.OutcomeBroadcastTaskStatus;
import com.stee.spfcore.model.course.internal.ReminderStatus;
import com.stee.spfcore.model.course.internal.WaitListTask;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.model.userAnnouncement.UserAnnouncementModule;
import com.stee.spfcore.vo.course.UserCourseHistory;
import com.stee.spfcore.vo.course.UserCourseTask;
import com.stee.spfcore.vo.personnel.PersonalNricEmailPhone;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CourseDAO {

	private static final Logger logger = Logger.getLogger( CourseDAO.class.getName() );
	
    private List< String > courseCategoryIds;

    public CourseDAO() {
        courseCategoryIds = new ArrayList<>();
        courseCategoryIds.add( SystemCategoryIdConstants.CAREER_TRANSITION_PROGRAMMES );
        courseCategoryIds.add( SystemCategoryIdConstants.STAFF_Well_BEING_PROGRAMMES );
    }

    public String addCourse( Course course, String requester ) {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getCurrentSession();

        session.save( course );

        session.flush();

        return course.getId();
    }

    public void updateCourse( Course course, String requester ) {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getCurrentSession();

        session.merge( course );

        session.flush();
    }

    public Course getCourse( String id ) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        return ( Course ) session.get( Course.class, id );
    }

    @SuppressWarnings( "unchecked" )
    public List< Course > getCourses() {

        Session session = SessionFactoryUtil.getCurrentSession();

        Query query = session.createQuery( "FROM Course course order by course.registrationConfig.openDate desc" );

        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< Course > getCourses( String categoryId, String titleId ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        Query query = session.createQuery( "FROM Course c where c.categoryId = :categoryId and c.titleId = :titleId" );
        query.setParameter( "categoryId", categoryId );
        query.setParameter( "titleId", titleId );

        return query.list();
    }

    public void addCourseParticipant( CourseParticipant participant, String requester ) {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getCurrentSession();

        session.save( participant );

        session.flush();
    }

    public void addCourseParticipants( List< CourseParticipant > participants, String requester ) {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getCurrentSession();

        for ( int i = 0; i < participants.size(); i++ ) {
            CourseParticipant participant = participants.get( i );
            session.save( participant );

            if ( i % 20 == 0 ) { //20, same as the JDBC batch size
                //flush a batch of inserts and release memory:
                session.flush();
                session.clear();
            }
        }

        session.flush();
    }

    public void updateCourseParticipant( CourseParticipant participant, String requester ) {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getCurrentSession();

        session.merge( participant );

        session.flush();

    }

    public CourseParticipant getCourseParticipant( String courseId, String nric ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        Query query = session.createQuery( "Select c from CourseParticipant c where c.nric = :nric and c.courseId = :courseId" );
        query.setParameter( "nric", nric );
        query.setParameter( "courseId", courseId );

        List< ? > list = query.list();

        if ( !list.isEmpty() ) {
            return ( CourseParticipant ) list.get( 0 );
        }
        else {
            return null;
        }
    }

    public void deleteCourseParticipant( CourseParticipant participant, String requester ) {
        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getCurrentSession();
        session.delete( participant );
    }

    @SuppressWarnings( "unchecked" )
    public List< CourseParticipant > getCourseParticipants( String courseId, String slotId ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        Query query = session.createQuery( "Select c from CourseParticipant c where c.courseId = :courseId and c.slotId = :slotId" );
        query.setParameter( "courseId", courseId );
        query.setParameter( "slotId", slotId );

        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< CourseParticipant > getCourseParticipants( String courseId, String slotId, ParticipantStatus status ) {

        Session session = SessionFactoryUtil.getCurrentSession();

    	StringBuilder builder = new StringBuilder("select c from CourseParticipant c where c.status = :status ");
    	if (courseId != null) {
    		builder.append("and c.courseId = :courseId ");
    	}
    	if (slotId != null) {
    		builder.append("and c.slotId = :slotId ");
    	}
    	
    	builder.append("order by c.appliedOn asc ");

        if ( logger.isLoggable( Level.INFO ) ) {
            logger.info(String.format("getWaitListTaskList: %s", builder.toString()));
        }
        Query query = session.createQuery(builder.toString());
    	if (courseId != null) {
    		query.setParameter("courseId", courseId);
    	}
    	if (slotId != null) {
    		query.setParameter("slotId", slotId);
    	}
    	query.setParameter( "status", status );
    	
    	return query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< CourseParticipant > getCourseParticipants( String courseId, String slotId, ParticipantStatus[] statuses ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        Query query = session.createQuery( "Select c from CourseParticipant c where c.courseId = :courseId " + "and c.slotId = :slotId and c.status in (:statuses)" );
        query.setParameter( "courseId", courseId );
        query.setParameter( "slotId", slotId );
        query.setParameterList( "statuses", statuses );

        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< String > getCourseParticipantNrics( String courseId ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        Query query = session.createQuery( "Select c.nric from CourseParticipant c where c.courseId = :courseId " );
        query.setParameter( "courseId", courseId );

        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< CourseParticipant > getCourseParticipants( String courseId ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        Query query = session.createQuery( "Select c from CourseParticipant c where c.courseId = :courseId" );
        query.setParameter( "courseId", courseId );

        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< PersonalDetail > getNominatedCourseParticipantDetails( String courseId ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        String queryString = "Select distinct p from PersonalDetail p where p.nric in " + "(select c.nric from CourseParticipant c where c.status = :status and c.courseId = :courseId)";

        Query query = session.createQuery( queryString );
        query.setParameter( "courseId", courseId );
        query.setParameter( "status", ParticipantStatus.NOMINATED );

        return query.list();
    }
    
    
    @SuppressWarnings("unchecked")
	public List< PersonalNricEmailPhone > getNominatedCourseParticipants( String courseId ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        String queryString = "select new com.stee.spfcore.vo.personnel.PersonalNricEmailPhone( p ) from PersonalDetail p where p.nric in " + "(select c.nric from CourseParticipant c where c.status = :status and c.courseId = :courseId)";

        Query query = session.createQuery( queryString );
        query.setParameter( "courseId", courseId );
        query.setParameter( "status", ParticipantStatus.NOMINATED );

        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< String > getCourseIds( String titleId, CourseStatus status ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        Query query = session.createQuery( "select c.id from Course c where c.titleId = :titleId and c.status = :status" );
        query.setParameter( "titleId", titleId );
        query.setParameter( "status", status );

        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    /**
     * Query a list of registered course participants that has / has not attended the specified
     * course before and are not in the specified blacklist. 
     */
    public List< CourseParticipant > getRegisteredCourseParticipants( String courseId, String slotId, boolean attendedBefore, List< String > similarCourses, List< String > serviceTypes, List< String > blacklistNrics ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "Select c from CourseParticipant c where c.courseId = :courseId " );
        builder.append( "and c.slotId = :slotId and c.status = :status " );
        builder.append( "and c.nric in (select e.nric from Employment e where e.serviceType in (:serviceTypes)) " );

        if ( !similarCourses.isEmpty() ) {
            if ( attendedBefore ) {
                builder.append( "and c.nric in (select b.nric from CourseParticipant b where b.attended = true and b.courseId in (:similarCourses)) " );
            }
            else {
                builder.append( "and c.nric not in (select b.nric from CourseParticipant b where b.attended = true and b.courseId in (:similarCourses)) " );
            }
        }

        if ( !blacklistNrics.isEmpty() ) {
            builder.append( "and c.nric not in (:blacklistNrics) " );
        }

        builder.append( "order by c.appliedOn asc" );

        Query query = session.createQuery( builder.toString() );

        query.setParameter( "courseId", courseId );
        query.setParameter( "slotId", slotId );
        query.setParameter( "status", ParticipantStatus.REGISTERED );
        query.setParameterList( "serviceTypes", serviceTypes );
        if ( !similarCourses.isEmpty() ) {
            query.setParameterList( "similarCourses", similarCourses );
        }
        if ( !blacklistNrics.isEmpty() ) {
            query.setParameterList( "blacklistNrics", blacklistNrics );
        }

        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    /**
     * Query a list of registered course participants in the specified unit that has not attended the specified
     * course before and are not in the specified blacklist. 
     */
    public List< CourseParticipant > getRegisteredCourseParticipants( String courseId, String slotId, String unitCodeId, List< String > similarCourses, List< String > serviceTypes, List< String > blacklistNrics ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "Select c from CourseParticipant c where c.courseId = :courseId " );
        builder.append( "and c.slotId = :slotId and c.status = :status " );
        builder.append( "and c.nric in (select e.nric from Employment e where e.serviceType in (:serviceTypes) " );
        builder.append( "and e.organisationOrDepartment = :unitId) " );

        if ( !similarCourses.isEmpty() ) {
            builder.append( "and c.nric not in (select b.nric from CourseParticipant b where b.attended = true and b.courseId in (:similarCourses)) " );
        }

        if ( !blacklistNrics.isEmpty() ) {
            builder.append( "and c.nric not in (:blacklistNrics) " );
        }

        builder.append( "order by c.appliedOn asc" );

        Query query = session.createQuery( builder.toString() );

        query.setParameter( "courseId", courseId );
        query.setParameter( "slotId", slotId );
        query.setParameter( "status", ParticipantStatus.REGISTERED );
        query.setParameterList( "serviceTypes", serviceTypes );
        query.setParameter( "unitId", unitCodeId );

        if ( !similarCourses.isEmpty() ) {
            query.setParameterList( "similarCourses", similarCourses );
        }
        if ( !blacklistNrics.isEmpty() ) {
            query.setParameterList( "blacklistNrics", blacklistNrics );
        }

        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    /**
     * Query a list of registered course participants that has attended the specified
     * course before and are not in the specified blacklist. 
     */
    public List< CourseParticipant > getRegisteredCourseParticipants( String courseId, String slotId, List< String > similarCourses, List< String > blacklistNrics ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "Select c from CourseParticipant c where c.courseId = :courseId " );
        builder.append( "and c.slotId = :slotId and c.status = :status " );

        if ( !similarCourses.isEmpty() ) {
            builder.append( "and c.nric in (select b.nric from CourseParticipant b where b.attended = true and b.courseId in (:similarCourses)) " );
        }

        if ( !blacklistNrics.isEmpty() ) {
            builder.append( "and c.nric not in (:blacklistNrics) " );
        }

        builder.append( "order by c.appliedOn asc" );

        Query query = session.createQuery( builder.toString() );

        query.setParameter( "courseId", courseId );
        query.setParameter( "slotId", slotId );
        query.setParameter( "status", ParticipantStatus.REGISTERED );
        if ( !similarCourses.isEmpty() ) {
            query.setParameterList( "similarCourses", similarCourses );
        }
        if ( !blacklistNrics.isEmpty() ) {
            query.setParameterList( "blacklistNrics", blacklistNrics );
        }

        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    /**
     * Query a list of registered course participants that has attended the specified
     * course before and are not in the specified blacklist.
     */
    public List< CourseParticipant > getBlacklistedRegisteredCourseParticipants( String courseId, String slotId ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "Select c from CourseParticipant c, Blacklistee v where " );
        builder.append( "c.nric = v.nric and c.courseId = :courseId " );
        builder.append( "and c.slotId = :slotId and c.status = :status " );
        builder.append( "and v.module like :module " );
        builder.append( "and (v.effectiveDate is not null and v.effectiveDate <= current_date()) " );
        builder.append( "and (v.effectiveDate is not null and v.effectiveDate <= current_date()) " );
        builder.append( "and (v.obsoleteDate is null or v.obsoleteDate > current_date()) " );
        builder.append( "order by v.effectiveDate, c.appliedOn asc" );

        Query query = session.createQuery( builder.toString() );

        query.setParameter( "courseId", courseId );
        query.setParameter( "slotId", slotId );
        query.setParameter( "status", ParticipantStatus.REGISTERED );
        query.setParameter( "module", BlacklistModuleNameConstants.WELFARE_COURSES );

        return ( List< CourseParticipant > ) query.list();
    }

    @SuppressWarnings( "unchecked" )
    /**
     * Query a list of course participants that has attended the specified
     * course before and are not in the specified blacklist.
     */
    public List< CourseParticipant > getBlacklistedCourseParticipants( String courseId, String slotId ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "Select c from CourseParticipant c, Blacklistee v where " );
        builder.append( "c.nric = v.nric and c.courseId = :courseId " );
        builder.append( "and c.slotId = :slotId " );
        builder.append( "and v.module like :module " );
        builder.append( "and (v.effectiveDate is not null and v.effectiveDate <= current_date()) " );
        builder.append( "and (v.effectiveDate is not null and v.effectiveDate <= current_date()) " );
        builder.append( "and (v.obsoleteDate is null or v.obsoleteDate > current_date()) " );
        builder.append( "order by v.effectiveDate, c.appliedOn asc" );

        Query query = session.createQuery( builder.toString() );

        query.setParameter( "courseId", courseId );
        query.setParameter( "slotId", slotId );
        query.setParameter( "module", BlacklistModuleNameConstants.WELFARE_COURSES );

        return ( List< CourseParticipant > ) query.list();
    }
    
    @SuppressWarnings( "unchecked" )
    /**
     * Query a list of course participants that has attended the specified
     * course before and are not in the specified blacklist.
     */
    public List< CourseParticipant > getNonBlacklistedCourseParticipants( String courseId, String slotId, ParticipantStatus status ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "Select c from CourseParticipant c where " );
        builder.append( "c.courseId = :courseId " );
        builder.append( "and c.slotId = :slotId " );
        builder.append( "and c.status = :status " );
        builder.append( "and c.nric not in (select v.nric from Blacklistee v where " );
        builder.append( "v.module like :module " );
        builder.append( "and (v.effectiveDate is not null and v.effectiveDate <= current_date()) " );
        builder.append( "and (v.effectiveDate is not null and v.effectiveDate <= current_date()) " );
        builder.append( "and (v.obsoleteDate is null or v.obsoleteDate > current_date()) ) " );
        builder.append( "order by c.appliedOn asc" );

        Query query = session.createQuery( builder.toString() );

        query.setParameter( "courseId", courseId );
        query.setParameter( "slotId", slotId );
        query.setParameter( "status", status );
        query.setParameter( "module", BlacklistModuleNameConstants.WELFARE_COURSES );

        return ( List< CourseParticipant > ) query.list();
    }
    
    @SuppressWarnings( "unchecked" )
    public List< Category > getCourseCategories() {

        Session session = SessionFactoryUtil.getCurrentSession();

        Query query = session.createQuery( "FROM Category c where c.id = (:ids)" );
        query.setParameterList( "ids", courseCategoryIds );

        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< CourseParticipant > getCourseParticipants( String nric, Date startDate, Date endDate ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder queryBuilder = new StringBuilder( "select c from CourseParticipant c where c.nric = :nric " );
        queryBuilder.append( "and c.slotId in (select s.id from Slot s where " );
        queryBuilder.append( "( (year(s.startDate) < :endYear) " );
        queryBuilder.append( "  or (year(s.startDate) = :endYear and month(s.startDate) < :endMonth) " );
        queryBuilder.append( "  or (year(s.startDate) = :endYear and month(s.startDate) = :endMonth and day(s.startDate) <= :endDay) ) " );
        queryBuilder.append( "and ( (year(s.endDate) > :startYear) " );
        queryBuilder.append( "  or (year(s.endDate) = :startYear and month(s.endDate) > :startMonth) " );
        queryBuilder.append( "  or (year(s.endDate) = :startYear and month(s.endDate) = :startMonth and day(s.endDate) >= :startDay) ) )" );

        Query query = session.createQuery( queryBuilder.toString() );

        query.setParameter( "nric", nric );

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime( startDate );
        query.setParameter( "startYear", cal.get( Calendar.YEAR ) );
        query.setParameter( "startMonth", cal.get( Calendar.MONTH ) + 1 );
        query.setParameter( "startDay", cal.get( Calendar.DATE ) );

        cal.setTime( endDate );
        query.setParameter( "endYear", cal.get( Calendar.YEAR ) );
        query.setParameter( "endMonth", cal.get( Calendar.MONTH ) + 1 );
        query.setParameter( "endDay", cal.get( Calendar.DATE ) );

        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< Course > getCoursePendingRegistration( String nric ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder queryBuilder = new StringBuilder( "select c from Course c where c.registrationConfig.openDate <= :currentDate " );
        queryBuilder.append( "and c.registrationConfig.closeDate >= :currentDate " );
        queryBuilder.append( "and c.status = :status " );
        queryBuilder.append( "and c.id not in (select distinct p.courseId from CourseParticipant p where p.nric = :nric and p.status != :participantStatus) " );
        queryBuilder.append( "and c.id in (select distinct a.referenceId from UserAnnouncement a where a.nric = :nric and a.module = :module) " );

        Query query = session.createQuery( queryBuilder.toString() );

        query.setParameter( "nric", nric );
        query.setParameter( "status", CourseStatus.ACTIVATED );
        query.setParameter( "module", UserAnnouncementModule.COURSE );
        query.setParameter( "participantStatus", ParticipantStatus.WITHDRAWN );
        query.setParameter( "currentDate", new Date() );

        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< UserCourseTask > getUserCourseTasks( String nric ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder queryBuilder = new StringBuilder( "select p.nric as nric, p.courseId as courseId, p.slotId as slotId, cat.name as title, c.type as courseType " );
        queryBuilder.append( "from CourseParticipant p, Course c, SubCategory cat where p.courseId = c.id " );
        queryBuilder.append( "and p.nric = :nric " );
        queryBuilder.append( "and c.titleId = cat.id " );
        queryBuilder.append( "and (p.status = :nominated or p.status = :selected) and c.registrationConfig.openDate <= :currentDate " );
        queryBuilder.append( "and c.registrationConfig.closeDate >= :currentDate" );

        Query query = session.createQuery( queryBuilder.toString() );
        query.setParameter( "nric", nric );
        query.setParameter( "nominated", ParticipantStatus.NOMINATED );
        query.setParameter( "selected", ParticipantStatus.SELECTED );
        query.setParameter( "currentDate", new Date() );

        query.setResultTransformer( new AliasToBeanResultTransformer( UserCourseTask.class ) );

        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< Course > getCoursePendingReminder() {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder queryBuilder = new StringBuilder( "select distinct c from Course as c inner join c.slots as slot " );
        queryBuilder.append( "where c.status = :status and slot.reminderDate <= :currentDate and slot.id not in " );
        queryBuilder.append( "(select rs.slotId from ReminderStatus as rs where rs.sent = true)" );

        Query query = session.createQuery( queryBuilder.toString() );
        query.setParameter( "status", CourseStatus.ACTIVATED );
        query.setParameter( "currentDate", new Date() );

        return query.list();
    }

    public ReminderStatus getReminderStatus( String courseId, String slotId ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        Query query = session.createQuery( "select rs from ReminderStatus as rs where rs.courseId = :courseId and rs.slotId = :slotId" );
        query.setParameter( "courseId", courseId );
        query.setParameter( "slotId", slotId );

        @SuppressWarnings( "unchecked" )
        List< ReminderStatus > list = query.list();

        if ( list.isEmpty() ) {
            return null;
        }
        else {
            return list.get( 0 );
        }
    }

    public void saveReminderStatus( ReminderStatus status ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        session.saveOrUpdate( status );

        session.flush();

    }

    @SuppressWarnings( "unchecked" )
    public List< Course > getCoursesPendingAllocation() {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "select c from Course c where c.type = :type " );
        builder.append( "and c.status = :status and c.registrationConfig.closeDate < :currentDate " );
        builder.append( "and c.id not in (select t.courseId from ApplicantSelectionTaskStatus t)" );

        Query query = session.createQuery( builder.toString() );
        query.setParameter( "type", CourseType.NON_COMPULSORY );
        query.setParameter( "status", CourseStatus.ACTIVATED );
        query.setParameter( "currentDate", new Date() );

        return query.list();
    }

    public void saveApplicantSelectionTaskStatus( ApplicantSelectionTaskStatus status ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        session.saveOrUpdate( status );

        session.flush();

    }

    @SuppressWarnings( "unchecked" )
    public List< UserCourseHistory > getUserCourseHistories( String nric, Date startDate, Date endDate ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder queryBuilder = new StringBuilder( "select category.name as title, slot.startDate as startDate, " );
        queryBuilder.append( "slot.endDate as endDate, participant.status as status, participant.attended as attended, participant.withdrawalReason as withdrawalReason " );
        queryBuilder.append( "from SubCategory category, Course course, Slot slot, CourseParticipant participant " );
        queryBuilder.append( "where participant.courseId = course.id and " );
        queryBuilder.append( "participant.slotId = slot.id and " );
        queryBuilder.append( "course.titleId = category.id and " );
        queryBuilder.append( "participant.nric = :nric and ( " );
        queryBuilder.append( "( (year(slot.startDate) < :endYear) " );
        queryBuilder.append( "  or (year(slot.startDate) = :endYear and month(slot.startDate) < :endMonth) " );
        queryBuilder.append( "  or (year(slot.startDate) = :endYear and month(slot.startDate) = :endMonth and day(slot.startDate) <= :endDay) ) " );
        queryBuilder.append( "and ( (year(slot.endDate) > :startYear) " );
        queryBuilder.append( "  or (year(slot.endDate) = :startYear and month(slot.endDate) > :startMonth) " );
        queryBuilder.append( "  or (year(slot.endDate) = :startYear and month(slot.endDate) = :startMonth and day(slot.endDate) >= :startDay) ) )" );
        queryBuilder.append( " order by slot.startDate" );

        Query query = session.createQuery( queryBuilder.toString() );
        query.setParameter( "nric", nric );

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime( startDate );
        query.setParameter( "startYear", cal.get( Calendar.YEAR ) );
        query.setParameter( "startMonth", cal.get( Calendar.MONTH ) + 1 );
        query.setParameter( "startDay", cal.get( Calendar.DATE ) );

        cal.setTime( endDate );
        query.setParameter( "endYear", cal.get( Calendar.YEAR ) );
        query.setParameter( "endMonth", cal.get( Calendar.MONTH ) + 1 );
        query.setParameter( "endDay", cal.get( Calendar.DATE ) );

        query.setResultTransformer( new AliasToBeanResultTransformer( UserCourseHistory.class ) );

        return query.list();
    }

    public void saveOutcomeBroadcastTaskStatus( OutcomeBroadcastTaskStatus status ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        session.saveOrUpdate( status );

        session.flush();

    }

    @SuppressWarnings( "unchecked" )
    public List< Course > getCoursePendingOutcomeBroadcast( Date cutoffDate ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "select c from Course c where " );
        builder.append( "c.type = :type and " );
        builder.append( "c.status = :status and " );
        builder.append( "c.registrationConfig.closeDate < :currentDate and " );
        builder.append( "c.registrationConfig.autoOutcomeBroadcast = false and " );
        builder.append( "((c.registrationConfig.outcomeBroadcasted = true and c.id not in (select t.courseId from OutcomeBroadcastTaskStatus t)) or " );
        builder.append( "(c.registrationConfig.outcomeBroadcasted = false and c.registrationConfig.closeDate < :takeOverDate))" );

        Query query = session.createQuery( builder.toString() );
        query.setParameter( "type", CourseType.NON_COMPULSORY );
        query.setParameter( "status", CourseStatus.ACTIVATED );

        query.setParameter( "currentDate", new Date() );
        query.setParameter( "takeOverDate", cutoffDate );

        return query.list();

    }
    
    @SuppressWarnings("unchecked")
	public List< Course > getCoursePendingOutcomeBroadcast() {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "select c from Course c where " );
        builder.append( "c.type = :type and " );
        builder.append( "c.status = :status and " );
        builder.append( "c.registrationConfig.closeDate < :currentDate and " );
        builder.append( "c.registrationConfig.autoOutcomeBroadcast = false and " );
        builder.append( "((c.registrationConfig.outcomeBroadcasted = true and c.id not in (select t.courseId from OutcomeBroadcastTaskStatus t)) or " );
        builder.append( "(c.registrationConfig.outcomeBroadcasted = false))" );

        Query query = session.createQuery( builder.toString() );
        query.setParameter( "type", CourseType.NON_COMPULSORY );
        query.setParameter( "status", CourseStatus.ACTIVATED );

        query.setParameter( "currentDate", new Date() );

        return query.list();

    }

    @SuppressWarnings( "unchecked" )
    public List< Course > getCoursePendingOutcomeConfirmation( Date cutoffDate ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "select c from Course c where " );
        builder.append( "c.type = :type and " );
        builder.append( "c.status = :status and " );
        builder.append( "c.registrationConfig.closeDate < :currentDate and " );
        builder.append( "c.registrationConfig.autoOutcomeBroadcast = false and " );
        builder.append( "(c.registrationConfig.outcomeBroadcasted = false and c.registrationConfig.closeDate < :takeOverDate))" );

        Query query = session.createQuery( builder.toString() );
        query.setParameter( "type", CourseType.NON_COMPULSORY );
        query.setParameter( "status", CourseStatus.ACTIVATED );

        query.setParameter( "currentDate", new Date() );
        query.setParameter( "takeOverDate", cutoffDate );

        return query.list();

    }
    
    @SuppressWarnings("unchecked")
	public List< Course > getCoursePendingOutcomeConfirmation() {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "select c from Course c where " );
        builder.append( "c.type = :type and " );
        builder.append( "c.status = :status and " );
        builder.append( "c.registrationConfig.closeDate < :currentDate and " );
        builder.append( "c.registrationConfig.autoOutcomeBroadcast = false and " );
        builder.append( "c.registrationConfig.outcomeBroadcasted = false" );

        Query query = session.createQuery( builder.toString() );
        query.setParameter( "type", CourseType.NON_COMPULSORY );
        query.setParameter( "status", CourseStatus.ACTIVATED );

        query.setParameter( "currentDate", new Date() );

        return query.list();

    }

    @SuppressWarnings( "unchecked" )
    public List< String > getUnitOrderByNumOfApplicants( String courseId, String slotId ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "select e.organisationOrDepartment from Employment e, CourseParticipant p where " );
        builder.append( "e.nric = p.nric and " );
        builder.append( "p.courseId = :courseId and " );
        builder.append( "p.slotId = :slotId and " );
        builder.append( "p.status = :status " );
        builder.append( "group by e.organisationOrDepartment order by count (p.nric) desc" );

        Query query = session.createQuery( builder.toString() );
        query.setParameter( "courseId", courseId );
        query.setParameter( "slotId", slotId );
        query.setParameter( "status", ParticipantStatus.REGISTERED );

        return query.list();
    }
    
    public WaitListTask getWaitListTask (String id) {
    	
    	 Session session = SessionFactoryUtil.getInstance().getCurrentSession();

         return ( WaitListTask ) session.get( WaitListTask.class, id );
    }
    
    @SuppressWarnings("unchecked")
	public List<WaitListTask> getWaitListTaskList (String courseId, String slotId, boolean informed) {
    	
    	Session session = SessionFactoryUtil.getCurrentSession();
    	
    	StringBuilder builder = new StringBuilder("select w from WaitListTask as w where 1=1 ");
    	if (courseId != null) {
    		builder.append("and w.courseId = :courseId ");
    	}
    	if (slotId != null) {
    		builder.append("and w.slotId = :slotId ");
    	}
    	
    	builder.append("and w.informed = :informed ");
        if ( logger.isLoggable( Level.INFO ) ) {
            logger.info(String.format("getWaitListTaskList: %s", builder));
        }
        Query query = session.createQuery(builder.toString());
    	if (courseId != null) {
    		query.setParameter("courseId", courseId);
    	}
    	if (slotId != null) {
    		query.setParameter("slotId", slotId);
    	}
    	query.setParameter("informed", informed);
    	
    	return (List<WaitListTask>)query.list();
    }
    
    public void saveWaitListTask (WaitListTask task) {
    	
    	logger.info("saveWaitListTasks");
    	
    	Session session = SessionFactoryUtil.getCurrentSession();
    	
    	session.saveOrUpdate(task);
    	
    	session.flush();
    }
    
    @SuppressWarnings("unchecked")
	public List<Course> getCoolingEndCourses (Date date) {
    	
    	Session session = SessionFactoryUtil.getCurrentSession();
    	
    	StringBuilder builder = new StringBuilder("select c from Course c where ");
    	builder.append("c.status = :status and ");
    	builder.append("c.type = :type and ");
    	builder.append("c.coolingPeriodEndDate = :coolingPeriodEndDate");
    
    	Query query = session.createQuery(builder.toString());
    	query.setParameter("status", CourseStatus.ACTIVATED);
    	query.setParameter("type", CourseType.NON_COMPULSORY);
    	query.setParameter("coolingPeriodEndDate", date);
    	
    	return (List<Course>)query.list();
    }
    
    public Slot getSlot( String id ) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        return ( Slot ) session.get( Slot.class, id );
    }
    
    public String generateSlotId() {
        return SequenceUtil.getInstance().getNextSequenceValue( Slot.class );
    }
    
}
