package com.stee.spfcore.service.course;

import com.stee.spfcore.service.course.impl.CourseService;

public class CourseServiceFactory {

	private CourseServiceFactory(){}
	private static ICourseService instance;
	
	public static synchronized ICourseService getInstance () {
		
		if (instance == null) {
			instance = createInstance ();
		}
		return instance;
	}
	
	private static ICourseService createInstance () {

		return new CourseService();
	}
	
}
