package com.stee.spfcore.webapi.rest;

import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stee.spfcore.webapi.model.UserSessionUtil;
import com.stee.spfcore.webapi.model.course.Course;
import com.stee.spfcore.webapi.model.course.CourseParticipant;
import com.stee.spfcore.webapi.model.vo.course.UserCourseHistory;
import com.stee.spfcore.webapi.service.CourseService;
import com.stee.spfcore.webapi.utils.ConvertUtil;

@RestController
@RequestMapping("/course")
public class CourseController {

	private CourseService service;
	
	private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
	
	@Autowired
	public CourseController(CourseService service) {
		this.service = service;
	}
	
	@GetMapping("/getCourse")
	public Course getCourse(String courseId) {
		Course course = service.getCourse(courseId);
		return course;
	}
	
	@GetMapping("/getCourseParticipant")
	public CourseParticipant getCourseParticipant(@RequestParam String courseId, String nric) {
		CourseParticipant courseParticipant = service.getCourseParticipant(courseId, nric); 
		return courseParticipant;	
	}
	
	@GetMapping("/getCourseParticipants")
	public List <CourseParticipant> getCourseParticipants(@RequestParam String nric, String startDate, String endDate) {
		Date newStartDate = null;
		if ( startDate != null && !startDate.trim().isEmpty() ) {
			newStartDate = ConvertUtil.convertFebDateControlStringToDate( startDate );
		}

		Date newEndDate = null;
		if ( endDate != null && !endDate.trim().isEmpty() ) {
			newEndDate = ConvertUtil.convertFebDateControlStringToDate( endDate );
		}
		
		List <CourseParticipant> courseParticipants = service.getCourseParticipants(nric, newStartDate, newEndDate); 
		return courseParticipants;	
	}
	
	@GetMapping("/getUserCourseHistories")
	public List<UserCourseHistory> getUserCourseHistories(@RequestParam String nric, String startDate, String endDate ) {
		
		Date newStartDate = null;
		if ( startDate != null && !startDate.trim().isEmpty() ) {
			newStartDate = ConvertUtil.convertFebDateControlStringToDate( startDate );
		}

		Date newEndDate = null;
		if ( endDate != null && !endDate.trim().isEmpty() ) {
			newEndDate = ConvertUtil.convertFebDateControlStringToDate( endDate );
		}
		
		List<UserCourseHistory> userCourseHistories = service.getUserCourseHistories(nric, newStartDate, newEndDate); 
		return userCourseHistories;	
	}
	
	@GetMapping("/getCoursePendingRegistration")
	public List<Course> getCoursePendingRegistration(@RequestParam String nric) {
		List<Course> course = service.getCoursePendingRegistration(nric);
		return course;
	}
	
	@PostMapping("/addCourse")
	public String addCourse(@RequestHeader("requester") String requester, @RequestBody Course course) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		logger.info("addCourse complete ");
		return service.addCourse(course);
	}
	
	@PostMapping("/updateCourse")
	public void updateCourse(@RequestHeader("requester") String requester, @RequestBody Course course) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		logger.info("updateCourse complete ");
		service.updateCourse(course);
	}
	
	@PostMapping("/addCourseParticipant")
	public void addCourseParticipant(@RequestHeader("requester") String requester, @RequestBody CourseParticipant participant) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.addCourseParticipant(participant);
		logger.info("addCourseParticipant complete ");
	}
	
	@PostMapping("/updateCourseParticipant")
	public void updateCourseParticipant(@RequestHeader("requester") String requester, @RequestBody CourseParticipant participant) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.updateCourseParticipant(participant);
		logger.info("updateCourseParticipant complete ");
	}
	
	@PostMapping("/deleteCourseParticipant")
	public void deleteCourseParticipant(@RequestHeader("requester") String requester, @RequestBody CourseParticipant participant) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.deleteCourseParticipant(participant);
		logger.info("deleteCourseParticipant complete ");
	}
	
}
