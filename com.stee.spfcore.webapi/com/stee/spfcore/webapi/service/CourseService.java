package com.stee.spfcore.webapi.service;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stee.spfcore.webapi.dao.CourseDAO;
import com.stee.spfcore.webapi.model.course.Course;
import com.stee.spfcore.webapi.model.course.CourseParticipant;
import com.stee.spfcore.webapi.model.vo.course.UserCourseHistory;

@Service
public class CourseService {

	private CourseDAO courseDAO;
	
	private static final Logger logger = Logger.getLogger(CourseService.class.getName());
	
	@Autowired
	public CourseService ( CourseDAO courseDAO ) {
		this.courseDAO = courseDAO;
	}
	
	@Transactional
	public Course getCourse ( String courseId ) {
		return courseDAO.getCourse(courseId);
	}
	
	@Transactional
	public CourseParticipant getCourseParticipant ( String courseId, String nric ) {
		return courseDAO.getCourseParticipant(courseId, nric);
	}
	
	@Transactional
	public List <CourseParticipant> getCourseParticipants(String nric, Date startDate, Date endDate) {
		return courseDAO.getCourseParticipants(nric, startDate, endDate);
	}
	
	@Transactional
	public List<UserCourseHistory> getUserCourseHistories ( String nric, Date startDate, Date endDate ) {
		return courseDAO.getUserCourseHistories(nric, startDate, endDate);
	}
	
	@Transactional
	public List<Course> getCoursePendingRegistration ( String nric ) {
		return courseDAO.getCoursePendingRegistration(nric);
	}
	
	@Transactional
	public void updateCourseParticipant ( CourseParticipant participant ) {
		courseDAO.updateCourseParticipant(participant);
	}
	
	@Transactional
	public void addCourseParticipant ( CourseParticipant participant ) {
		courseDAO.addCourseParticipant(participant);
	}
	
	@Transactional
	public void deleteCourseParticipant ( CourseParticipant participant ) {
		courseDAO.deleteCourseParticipant(participant);
	}
	
	@Transactional
	public String addCourse ( Course course ) {
		return courseDAO.addCourse(course);
	}
	
	@Transactional
	public void updateCourse ( Course course ) {
		courseDAO.updateCourse(course);
	}


	
}
