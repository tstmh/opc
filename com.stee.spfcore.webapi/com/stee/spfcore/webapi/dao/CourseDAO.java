package com.stee.spfcore.webapi.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.transform.AliasToBeanResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.stee.spfcore.webapi.model.course.Course;
import com.stee.spfcore.webapi.model.course.CourseParticipant;
import com.stee.spfcore.webapi.model.course.CourseStatus;
import com.stee.spfcore.webapi.model.course.ParticipantStatus;
import com.stee.spfcore.webapi.model.userAnnouncement.UserAnnouncementModule;
import com.stee.spfcore.webapi.model.vo.course.UserCourseHistory;

@Repository
public class CourseDAO {
	
	private EntityManager entityManager;
	private static final Logger logger = LoggerFactory.getLogger(CourseDAO.class);
	
	public CourseDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public Course getCourse(String courseId) {
		logger.info("Get Course");
		Session session = entityManager.unwrap(Session.class);
		
		return  session.get(Course.class, courseId);
	}
	

	@SuppressWarnings("unchecked")
	public CourseParticipant getCourseParticipant(String courseId, String nric) {
		logger.info("Get Course Participant");
		Session session = entityManager.unwrap(Session.class);
		
		Query<CourseParticipant> query = session.createQuery("Select c from CourseParticipant c where c.nric = :nric and c.courseId = :courseId" );
		query.setParameter("nric", nric);
		query.setParameter("courseId", courseId);
		
		List<CourseParticipant> list = query.list();
		if (!list.isEmpty()) {
			return (CourseParticipant) list.get(0);
		} else {
			return null;
		}
	}
	
	@SuppressWarnings( "unchecked" )
    public List< UserCourseHistory > getUserCourseHistories( String nric, Date startDate, Date endDate ) {

		Session session = entityManager.unwrap(Session.class);
		logger.info("Get Course Histories");

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

	@SuppressWarnings( "unchecked" )
    public List< Course > getCoursePendingRegistration( String nric ) {
		logger.info("Get Course Pending Registration");
		Session session = entityManager.unwrap(Session.class);

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
	
	public List <CourseParticipant> getCourseParticipants(String nric, Date startDate, Date endDate) {
		logger.info("Get Course Participants");
		Session session = entityManager.unwrap(Session.class);
		StringBuilder queryBuilder = new StringBuilder( "select c from CourseParticipant c where c.nric = :nric " );
        queryBuilder.append( "and c.slotId in (select s.id from Slot s where " );
        queryBuilder.append( "( (year(s.startDate) < :endYear) " );
        queryBuilder.append( "  or (year(s.startDate) = :endYear and month(s.startDate) < :endMonth) " );
        queryBuilder.append( "  or (year(s.startDate) = :endYear and month(s.startDate) = :endMonth and day(s.startDate) <= :endDay) ) " );
        queryBuilder.append( "and ( (year(s.endDate) > :startYear) " );
        queryBuilder.append( "  or (year(s.endDate) = :startYear and month(s.endDate) > :startMonth) " );
        queryBuilder.append( "  or (year(s.endDate) = :startYear and month(s.endDate) = :startMonth and day(s.endDate) >= :startDay) ) )" );
        logger.info(queryBuilder.toString());
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
	
	public void updateCourseParticipant ( CourseParticipant participant ) {
		logger.info("Update Course Participant");
		Session session = entityManager.unwrap(Session.class);
		
	    session.merge( participant );
	    session.flush();
	}
	
	public void addCourseParticipant ( CourseParticipant participant ) {
		logger.info("Add Course Participant");
		Session session = entityManager.unwrap(Session.class);
		
	    session.save( participant );
	    session.flush();
	}
	
	public void deleteCourseParticipant ( CourseParticipant participant ) {
		logger.info("Delete Course Participant");
		Session session = entityManager.unwrap(Session.class);
		
	    session.delete( participant );
	}
	
	public String addCourse ( Course course ) {
		logger.info("Add Course");
		Session session = entityManager.unwrap(Session.class);
		
	    session.save( course );
	    session.flush();
	    
	    return course.getId();
	}
	
	public void updateCourse ( Course course ) {
		logger.info("Update Course");
		Session session = entityManager.unwrap(Session.class);
		
	    session.merge( course );
	    session.flush();
	}


	
}
