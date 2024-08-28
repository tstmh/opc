package com.stee.spfcore.service.course;

import java.util.Date;
import java.util.List;

import com.stee.spfcore.model.category.Category;
import com.stee.spfcore.model.course.Course;
import com.stee.spfcore.model.course.CourseParticipant;
import com.stee.spfcore.model.course.ParticipantStatus;
import com.stee.spfcore.model.course.Slot;
import com.stee.spfcore.model.course.internal.WaitListTask;
import com.stee.spfcore.vo.course.SlotVacancyAllocation;
import com.stee.spfcore.vo.course.UserCourseHistory;
import com.stee.spfcore.vo.course.UserCourseTask;

public interface ICourseService {
	
	public String addCourse (Course course, String requester) throws CourseServiceException;
		
	public void updateCourse (Course course, String requester) throws CourseServiceException;
	
	public Course getCourse (String id) throws CourseServiceException;
	
	public List<Course> getCourses () throws CourseServiceException;
	
	public List<Course> getCourses (String categoryId, String titleId) throws CourseServiceException;
	
	public List<Category> getCourseCategories () throws CourseServiceException;
	
	public void addCourseParticipant (CourseParticipant participant, String requester) throws CourseServiceException;
	
	public void addCourseParticipants (List<CourseParticipant> participants, String requester) throws CourseServiceException;
	
	public void updateCourseParticipant (CourseParticipant participant, String requester) throws CourseServiceException;
	
	public void deleteCourseParticipant (String courseId, String nric, String requester) throws CourseServiceException;
	
	public CourseParticipant getCourseParticipant (String courseId, String nric) throws CourseServiceException;
	
	public List<CourseParticipant> getCourseParticipants (String courseId, String slotId) throws CourseServiceException;
	
	public List<CourseParticipant> getCourseParticipants (String courseId) throws CourseServiceException;
	
	public List<CourseParticipant> getCourseParticipants (String courseId, String slotId, 
									ParticipantStatus status) throws CourseServiceException;
	
	public List<SlotVacancyAllocation> getCourseVacancyAllocation (String courseId) throws CourseServiceException;
	
	public boolean activate (String courseId, String requester) throws CourseServiceException;
	
	public List<CourseParticipant> getCourseParticipants (String nric, Date startDate, Date endDate) throws CourseServiceException;
	
	public List<Course> getCoursePendingRegistration (String nric) throws CourseServiceException;
	
	public List<UserCourseTask> getUserCourseTasks (String nric) throws CourseServiceException;
	
	public void acknowledgeNominatedCourse (CourseParticipant participant, String requester) throws CourseServiceException;
	
	public void registerCourse (CourseParticipant participant, String requester) throws CourseServiceException;
	
	public void reRegisterCourse (CourseParticipant participant, String requester) throws CourseServiceException;
	
	public void withdrawCourse (CourseParticipant participant, String requester) throws CourseServiceException;
	
	public void confirmCourse (CourseParticipant participant, String requester) throws CourseServiceException;
	
	public void cancel (String courseId, String requester) throws CourseServiceException;
	
	/**
	 * Internal method to be called by Timer EJB 
	 */
	public void processTask () throws CourseServiceException;
	
	/**
	 * Internal method to be called by Timer EJB 
	 */
	public void vacancyAllocation () throws CourseServiceException;
	
	/**
	 * Internal method to be called by MessageHandler.
	 */
	public void handleWithdrawal (CourseParticipant participant) throws CourseServiceException;
	
	
	public List<UserCourseHistory> getUserCourseHistories (String nric, Date startDate, Date endDate) throws CourseServiceException;
	
	
	public void requestBroadcastAllocationOutcome (String courseId, String requester) throws CourseServiceException;
	
	public boolean activateWithThread (String courseId, String requester) throws CourseServiceException;
	
	public void addParticipant (CourseParticipant participant, String requester) throws CourseServiceException;
	
	public void updateParticipant(CourseParticipant participant, String requester) throws CourseServiceException;
	
	public void addInformParticipant (CourseParticipant participant, String requester) throws CourseServiceException;
	
	public void updateInformParticipant(CourseParticipant participant, String requester) throws CourseServiceException;
	
	public void processCoolingEndCourses (Date date) throws CourseServiceException;
	
	public List<WaitListTask> getWaitListTaskList (String courseId, String slotId, boolean informed);
	
	public WaitListTask getWaitListTask (String id);
	
	public void saveWaitListTask (WaitListTask task);
	
	public void saveWaitListTask (WaitListTask task, Course course, Slot slot, CourseParticipant participant);

	public Slot getSlot (String id) throws CourseServiceException;
	
	public String getNextSlotSequence() throws CourseServiceException;
}
