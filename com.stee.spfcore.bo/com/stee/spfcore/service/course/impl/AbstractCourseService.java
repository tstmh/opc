package com.stee.spfcore.service.course.impl;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.dao.CourseDAO;
import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.model.category.Category;
import com.stee.spfcore.model.course.Course;
import com.stee.spfcore.model.course.CourseParticipant;
import com.stee.spfcore.model.course.ParticipantStatus;
import com.stee.spfcore.model.course.Slot;
import com.stee.spfcore.model.course.internal.WaitListTask;
import com.stee.spfcore.service.course.CourseServiceException;
import com.stee.spfcore.service.course.ICourseService;
import com.stee.spfcore.vo.course.UserCourseTask;

public abstract class AbstractCourseService implements ICourseService {

	protected static final Logger logger = Logger.getLogger(AbstractCourseService.class.getName());

	protected CourseDAO dao;
	
	protected AbstractCourseService() {
		dao = new CourseDAO();
	}

	@Override
	public Course getCourse(String id) throws CourseServiceException {

		Course result = null;

		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getCourse(id);

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get Course", e);
			SessionFactoryUtil.rollbackTransaction();
		}

		return result;
	}

	@Override
	public List<Course> getCourses() throws CourseServiceException {

		List<Course> result = null;

		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getCourses();

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get Courses", e);
			SessionFactoryUtil.rollbackTransaction();
		}

		return result;
	}

	@Override
	public List<Course> getCourses(String categoryId, String titleId) throws CourseServiceException {

		List<Course> result = null;

		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getCourses(categoryId, titleId);

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get Courses of specified SubCategoryID", e);
			SessionFactoryUtil.rollbackTransaction();
		}

		return result;
	}

	@Override
	public List<Category> getCourseCategories() throws CourseServiceException {
		
		List<Category> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getCourseCategories();

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get Categories specified to Course module", e);
			SessionFactoryUtil.rollbackTransaction();
		}

		return result;
	}

	@Override
	public CourseParticipant getCourseParticipant(String courseId, String nric) throws CourseServiceException {
		
		CourseParticipant result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getCourseParticipant(courseId, nric);

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get course participant", e);
			SessionFactoryUtil.rollbackTransaction();
		}

		return result;
	}

	@Override
	public List<CourseParticipant> getCourseParticipants(String courseId, String slotId) throws CourseServiceException {
		
		List<CourseParticipant> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getCourseParticipants(courseId, slotId);

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get course participants", e);
			SessionFactoryUtil.rollbackTransaction();
		}

		return result;
	}

	@Override
	public List<CourseParticipant> getCourseParticipants(String courseId, String slotId, ParticipantStatus status)
			throws CourseServiceException {
		
		List<CourseParticipant> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getCourseParticipants(courseId, slotId, status);

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get course participants", e);
			SessionFactoryUtil.rollbackTransaction();
		}

		return result;
	}

	@Override
	public List<CourseParticipant> getCourseParticipants(String courseId) throws CourseServiceException {
		List<CourseParticipant> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getCourseParticipants(courseId);

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get course participants", e);
			SessionFactoryUtil.rollbackTransaction();
		}

		return result;
	}

	@Override
	public List<CourseParticipant> getCourseParticipants(String nric, Date startDate, Date endDate)
			throws CourseServiceException {
		
		List<CourseParticipant> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getCourseParticipants(nric, startDate, endDate);

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get course participants", e);
			SessionFactoryUtil.rollbackTransaction();
		}

		return result;
	}

	@Override
	public List<Course> getCoursePendingRegistration(String nric) throws CourseServiceException {
		
		List<Course> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getCoursePendingRegistration (nric);

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get course pending registration", e);
			SessionFactoryUtil.rollbackTransaction();
		}

		return result;
	}

	@Override
	public List<UserCourseTask> getUserCourseTasks(String nric) throws CourseServiceException {
		List<UserCourseTask> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getUserCourseTasks(nric);

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get user course tasks", e);
			SessionFactoryUtil.rollbackTransaction();
		}

		return result;
	}
	
	@Override
	public List<WaitListTask> getWaitListTaskList (String courseId, String slotId, boolean informed) {
		List<WaitListTask> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getWaitListTaskList(courseId, slotId, informed);

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get waitlist task list ", e);
			SessionFactoryUtil.rollbackTransaction();
		}

		return result;
	}
	
	@Override
	public WaitListTask getWaitListTask (String id) {
		WaitListTask result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getWaitListTask(id);

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get waitlist task ", e);
			SessionFactoryUtil.rollbackTransaction();
		}

		return result;
	}
	
	@Override
	public void saveWaitListTask (WaitListTask task) {
		
		try {
			SessionFactoryUtil.beginTransaction();
			dao.saveWaitListTask(task);

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to save waitlist task ", e);
			SessionFactoryUtil.rollbackTransaction();
		}

	}
	
	@Override
	public Slot getSlot (String id) throws CourseServiceException {

		Slot result = null;

		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getSlot(id);

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get Slot", e);
			SessionFactoryUtil.rollbackTransaction();
		}

		return result;
	}
	
	public String getNextSlotSequence() throws CourseServiceException {
		
		String result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.generateSlotId();
			SessionFactoryUtil.commitTransaction();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get slot sequence", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		return result;
	}
	
}
