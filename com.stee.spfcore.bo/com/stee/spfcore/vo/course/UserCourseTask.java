package com.stee.spfcore.vo.course;

import com.stee.spfcore.model.course.CourseType;

public class UserCourseTask {

	private String nric;

	private String courseId;

	private String slotId;

	private String title;

	private CourseType courseType;

	public UserCourseTask() {
		super();
	}

	public UserCourseTask(String nric, String courseId, String slotId, String title, CourseType courseType) {
		super();
		this.nric = nric;
		this.courseId = courseId;
		this.slotId = slotId;
		this.title = title;
		this.courseType = courseType;
	}

	public String getNric() {
		return nric;
	}

	public void setNric(String nric) {
		this.nric = nric;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getSlotId() {
		return slotId;
	}

	public void setSlotId(String slotId) {
		this.slotId = slotId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public CourseType getCourseType() {
		return courseType;
	}

	public void setCourseType(CourseType courseType) {
		this.courseType = courseType;
	}

}
