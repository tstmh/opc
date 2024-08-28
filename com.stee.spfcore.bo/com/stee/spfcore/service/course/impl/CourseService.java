package com.stee.spfcore.service.course.impl;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import com.stee.spfcore.dao.CodeDAO;
import com.stee.spfcore.dao.MarketingContentDAO;
import com.stee.spfcore.dao.MarketingDAO;
import com.stee.spfcore.dao.PersonnelDAO;
import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.model.code.Code;
import com.stee.spfcore.model.code.CodeType;
import com.stee.spfcore.model.course.Course;
import com.stee.spfcore.model.course.CourseParticipant;
import com.stee.spfcore.model.course.CourseStatus;
import com.stee.spfcore.model.course.CourseType;
import com.stee.spfcore.model.course.ParticipantStatus;
import com.stee.spfcore.model.course.Slot;
import com.stee.spfcore.model.course.internal.WaitListTask;
import com.stee.spfcore.model.marketingContent.MarketingContent;
import com.stee.spfcore.model.marketingContent.MarketingContentSet;
import com.stee.spfcore.model.personnel.ContactLabel;
import com.stee.spfcore.model.personnel.ContactMode;
import com.stee.spfcore.model.personnel.Email;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.model.personnel.Phone;
import com.stee.spfcore.vo.personnel.PersonalNricEmailPhone;
import com.stee.spfcore.model.userAnnouncement.UserAnnouncement;
import com.stee.spfcore.model.userAnnouncement.UserAnnouncementModule;
import com.stee.spfcore.security.AccessDeniedException;
import com.stee.spfcore.service.configuration.IMailSenderConfig;
import com.stee.spfcore.service.configuration.IMarketingContentConfig;
import com.stee.spfcore.service.configuration.ServiceConfig;
import com.stee.spfcore.service.course.CourseServiceException;
import com.stee.spfcore.service.marketingContent.IMarketingContentService;
import com.stee.spfcore.service.marketingContent.MarketingContentException;
import com.stee.spfcore.service.marketingContent.MarketingContentServiceFactory;
import com.stee.spfcore.service.marketingContent.impl.ECMUtil;
import com.stee.spfcore.service.marketingContent.impl.EmailUtil;
import com.stee.spfcore.service.marketingContent.impl.SmsUtil;
import com.stee.spfcore.service.userAnnouncement.IUserAnnouncementService;
import com.stee.spfcore.service.userAnnouncement.UserAnnouncementServiceException;
import com.stee.spfcore.service.userAnnouncement.UserAnnouncementServiceFactory;
import com.stee.spfcore.utils.DateUtils;
import com.stee.spfcore.utils.Encipher;
import com.stee.spfcore.utils.EnvironmentUtils;
import com.stee.spfcore.utils.Util;
import com.stee.spfcore.vo.course.SlotVacancyAllocation;
import com.stee.spfcore.vo.course.UnitAllocation;
import com.stee.spfcore.vo.course.UserCourseHistory;

public class CourseService extends AbstractCourseService {

	private MarketingContentDAO marketingContentDAO;
	private IMarketingContentService marketingContentService;
	private MarketingDAO marketingDAO;
	private EmailSmsHelper emailSmsHelper;
	private PersonnelDAO personnelDAO;
	private VacancyAllocationHandler vacancyAllocationHandler;
	private IUserAnnouncementService userAnnouncementService;
	private ECMUtil ecmUtil;
	private EmailUtil emailUtil;

    public CourseService() {
		super();
		
		try {
			ecmUtil = new ECMUtil();
		}
		catch (UnsupportedEncodingException e) {
			logger.log(Level.SEVERE, "Fail to construct ECMUtil.", e);
		}
		
		this.marketingContentDAO = new MarketingContentDAO();
		this.marketingContentService = MarketingContentServiceFactory.getInstance();
		this.marketingDAO = new MarketingDAO();
		this.emailSmsHelper = new EmailSmsHelper();
		this.personnelDAO = new PersonnelDAO();
		this.vacancyAllocationHandler = new VacancyAllocationHandler();
		this.userAnnouncementService = UserAnnouncementServiceFactory.getInstance();
		
		IMarketingContentConfig config = ServiceConfig.getInstance().getMarketingContentConfig();
		this.emailUtil = new EmailUtil(config.ecmContentURL(), ecmUtil);
        SmsUtil smsUtil = new SmsUtil();
	}

	@Override
	public String addCourse(Course course, String requester) throws CourseServiceException {

		String id = null;

		try {
			SessionFactoryUtil.beginTransaction();

			logger.log(Level.INFO, "course>>>>>>>>>>>>> in java code", course.getId().toString());
			logger.log(Level.INFO, "course>>>>>>>>>>>>> in java code", course.getCategory().toString());
			logger.log(Level.INFO, "course>>>>>>>>>>>>> in java code", course.getCategoryId().toString());
			logger.log(Level.INFO, "course>>>>>>>>>>>>> in java code", course.getType().toString());

			course.setId(null);
			dao.addCourse(course, requester);
			id = course.getId();

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to add course", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new CourseServiceException("Fail to add course", e);
		}

		return id;
	}

	@Override
	public void updateCourse(Course course, String requester) throws CourseServiceException {
		try {
			SessionFactoryUtil.beginTransaction();
			
			Course orgCourse = dao.getCourse(course.getId());
			
			boolean activated = (orgCourse.getStatus() == CourseStatus.ACTIVATED);
			
			// If already activated, need to validate date change and update 
			// the publishing
			if (activated) {
				validate (course, orgCourse);

				
				updateMarketing (course, requester);
			}
			
			dao.updateCourse(course, requester);

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to update course", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new CourseServiceException("Fail to update course", e);
		}
	}

	@Override
	public void addCourseParticipant(CourseParticipant participant, String requester) throws CourseServiceException {
		try {
			SessionFactoryUtil.beginTransaction();
			dao.addCourseParticipant(participant, requester);

			if (participant.getStatus() == ParticipantStatus.NOMINATED) {
				Course course = dao.getCourse(participant.getCourseId());
				if (course.getType() != CourseType.COMPULSORY) {
					throw new CourseServiceException ("Invalid course type");
				}
				
				if (course.getStatus() == CourseStatus.ACTIVATED) {
					MarketingContentSet contentSet = marketingContentDAO.getMarketingContentSet(course.getContentSetId());
					
					Set<String> targetedEmails = new HashSet<>();
					Set<String> targetedMobiles = new HashSet<>();
					
					PersonalDetail personalDetail = personnelDAO.getPersonal(participant.getNric());
					
					CourseUtil.retrieveParticipantContacts(true, personalDetail, targetedEmails, targetedMobiles);
					
					Date now = new Date ();
					for (MarketingContent content : contentSet.getContents()) {
						
						//Only need to handle content that yet to publish
						if (now.before(content.getActualPublishDate())) {
							
							marketingContentService.appendContentRecipients(content.getId(), new ArrayList<>(targetedEmails),
									new ArrayList<>(targetedMobiles), requester);
							
							userAnnouncementService.createUserAnnouncement(UserAnnouncementModule.COURSE, course.getId(), personalDetail, content, requester);
						}
					}
				}
			}
			
			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to add course participant", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new CourseServiceException("Fail to add course participant", e);
		}
	}

	@Override
	public void addCourseParticipants(List<CourseParticipant> participants, String requester)
			throws CourseServiceException {
		try {
			SessionFactoryUtil.beginTransaction();
			
			String courseId = getCourseId (participants);
			
			dao.addCourseParticipants(participants, requester);
			
			// Only need to perform the following for Activated Compulsory Course and participant
			// status of Nominated
			Course course = dao.getCourse(courseId);
			if (course.getStatus() == CourseStatus.ACTIVATED && course.getType() == CourseType.COMPULSORY) {
				
				MarketingContentSet contentSet = marketingContentDAO.getMarketingContentSet(course.getContentSetId());
				Date now = new Date ();
				
				List<MarketingContent> notPublishContents = new ArrayList<>();
				
				for (MarketingContent content : contentSet.getContents()) {
					// If the content has not be publish yet.
					if (now.before(content.getActualPublishDate())) {
						notPublishContents.add(content);
					}
				}
				
				if (!notPublishContents.isEmpty()) {
					for (CourseParticipant participant : participants) {
					
						if (participant.getStatus() != ParticipantStatus.NOMINATED) {
							continue;
						}
					
						Set<String> targetedEmails = new HashSet<>();
						Set<String> targetedMobiles = new HashSet<>();
					
						PersonalDetail personalDetail = personnelDAO.getPersonal(participant.getNric());
					
						CourseUtil.retrieveParticipantContacts(true, personalDetail, targetedEmails, targetedMobiles);
						
						for (MarketingContent content : notPublishContents) {
							
							if (!targetedEmails.isEmpty() || !targetedMobiles.isEmpty()) {
								marketingContentService.appendContentRecipients(content.getId(), new ArrayList<>(targetedEmails),
										new ArrayList<>(targetedMobiles), requester);
							}
						
							userAnnouncementService.createUserAnnouncement(UserAnnouncementModule.COURSE, course.getId(), personalDetail, content, requester);
						}
					}
				}	
			}
			
			SessionFactoryUtil.commitTransaction();
		}
		catch (MarketingContentException | UserAnnouncementServiceException | AccessDeniedException e) {
			logger.log(Level.SEVERE, "Fail to add course participants", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new CourseServiceException("Fail to add course participants", e);
		}
	}

	
	private String getCourseId (List<CourseParticipant> participants) throws CourseServiceException {
		String courseId = "";
		
		for (CourseParticipant participant : participants) {
			if (Objects.equals(courseId, "")) {
				courseId = participant.getCourseId();
			}
			if ((courseId != null) && (!courseId.equals(participant.getCourseId()))){
				throw new CourseServiceException ("All participant need to be for same course");
			}
		}
		
		return courseId;
	}
	
	
	@Override
	public void updateCourseParticipant(CourseParticipant participant, String requester) throws CourseServiceException {
		try {
			SessionFactoryUtil.beginTransaction();
			dao.updateCourseParticipant(participant, requester);

			Course course = dao.getCourse(participant.getCourseId());
			if (course.getStatus() == CourseStatus.ACTIVATED && course.getType() == CourseType.COMPULSORY) {
				
				Set<String> targetedEmails = new HashSet<>();
				Set<String> targetedMobiles = new HashSet<>();
				
				PersonalDetail personalDetail = personnelDAO.getPersonal(participant.getNric());
				
				CourseUtil.retrieveParticipantContacts(true, personalDetail, targetedEmails, targetedMobiles);
				
				Date now = new Date ();
				
				// Need to add UserAnnouncement for content yet to publish.
				MarketingContentSet contentSet = marketingContentDAO.getMarketingContentSet(course.getContentSetId());
				
				for (MarketingContent content : contentSet.getContents()) {
					if (now.before(content.getActualPublishDate())) {
						
						// Only Nominated Participant will receive broadcast
						if (participant.getStatus() == ParticipantStatus.NOMINATED) {
					
							marketingContentService.appendContentRecipients(content.getId(), new ArrayList<>(targetedEmails),
									new ArrayList<>(targetedMobiles), requester);
							
							userAnnouncementService.createUserAnnouncement(UserAnnouncementModule.COURSE, course.getId(), personalDetail, content, requester);
						}
						else {
							
							marketingContentService.removeContentRecipients(content.getId(), new ArrayList<>(targetedEmails),
									new ArrayList<>(targetedMobiles), requester);
							
							userAnnouncementService.removeUserAnnouncement(UserAnnouncementModule.COURSE, course.getId(), personalDetail.getNric(), content.getId(), requester);
							
						}
					}
				}
			}
			
			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to update course participant", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new CourseServiceException("Fail to update course participant", e);
		}
	}

	
	@Override
	public void deleteCourseParticipant(String courseId, String nric, String requester) throws CourseServiceException {
		
		try {
			SessionFactoryUtil.beginTransaction();
			CourseParticipant participant = dao.getCourseParticipant(courseId, nric);
			dao.deleteCourseParticipant(participant, requester);
			
			
			// Only Nominated participant is targeted for Broadcast.
			// So, need to remove those that has yet to publish.
			if (participant.getStatus() == ParticipantStatus.NOMINATED) {
				
				Course course = dao.getCourse(courseId);
				
				if (course.getStatus() == CourseStatus.ACTIVATED && course.getType() == CourseType.COMPULSORY) {
					
					Set<String> targetedEmails = new HashSet<>();
					Set<String> targetedMobiles = new HashSet<>();
					
					PersonalDetail personalDetail = personnelDAO.getPersonal(participant.getNric());
					
					CourseUtil.retrieveParticipantContacts(true, personalDetail, targetedEmails, targetedMobiles);
					
					Date now = new Date ();
					
					// Need to add UserAnnouncement for content yet to publish.
					MarketingContentSet contentSet = marketingContentDAO.getMarketingContentSet(course.getContentSetId());
					
					for (MarketingContent content : contentSet.getContents()) {
						if (now.before(content.getActualPublishDate())) {
							
							marketingContentService.removeContentRecipients(content.getId(), new ArrayList<>(targetedEmails),
									new ArrayList<>(targetedMobiles), requester);
							
							userAnnouncementService.removeUserAnnouncement(UserAnnouncementModule.COURSE, course.getId(), personalDetail.getNric(), content.getId(), requester);
						}
					}
				}
			}
			
			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to delete course participant", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new CourseServiceException("Fail to delete course participant", e);
		}
	}

	@Override
	public List<SlotVacancyAllocation> getCourseVacancyAllocation(String courseId) throws CourseServiceException {
		
		List<SlotVacancyAllocation> result = new ArrayList<>();
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			Map<String,String> codeMap = getUnitCodes ();
			
			Course course = dao.getCourse(courseId);
			
			List<String> memberGroups = course.getMemberGroupIds();
			
			Set<PersonalDetail> personalDetails = getMemberInGroups (memberGroups);
			
			int totalOfficer = personalDetails.size();
			
			Map<String, List<PersonalDetail>> unitOfficers = splitByUnit (personalDetails);
			
			for (Slot slot : course.getSlots()) {
				SlotVacancyAllocation allocation = new SlotVacancyAllocation();
				allocation.setSlotId(slot.getId());
				allocation.setMaxClassSize(slot.getMaxClassSize());
				allocation.setMinClassSize(slot.getMinClassSize());
				allocation.setTotalOffice(totalOfficer);
				
				List<UnitAllocation> unitAllocations = new ArrayList<>();
				allocation.setUnitAllocations(unitAllocations);
				
				for (String unitCodeId: unitOfficers.keySet()) {
					UnitAllocation unitAllocation = new UnitAllocation();
					unitAllocation.setUnitCodeId(unitCodeId);
					unitAllocation.setUnitName(codeMap.get(unitCodeId));
					
					List<PersonalDetail> list = unitOfficers.get(unitCodeId);
					float unitTotalOfficer = list.size();
					unitAllocation.setTotalOffice(list.size());
					
					unitAllocation.setAllocated((unitTotalOfficer / totalOfficer) * slot.getMaxClassSize());
					
					unitAllocations.add(unitAllocation);
				}
				
				result.add(allocation);
			}
			
			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to compute course vacancy allocation", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new CourseServiceException("Fail to compute course vacancy allocation", e);
		}
		
		return result;
	}
	
	
	private Map<String,String> getUnitCodes () {
		
		CodeDAO codeDAO = new CodeDAO();
		
		List<Code> codes = codeDAO.getCodes(CodeType.UNIT_DEPARTMENT);
		
		Map<String,String> codeMap = new HashMap<>();
		
		for (Code code : codes) {
			codeMap.put(code.getId(), code.getDescription());
		}
		
		return codeMap;
	}
	
	
	private Set<PersonalDetail> getMemberInGroups (List<String> memberGroups) {
		
		Set<PersonalDetail> set = new HashSet<>();
		MarketingDAO dao = new MarketingDAO();
		
		for (String groupId : memberGroups) {
			List<PersonalDetail> list = dao.getPersonnelInGroup(groupId);
			set.addAll(list);
		}
		
		return set;
	}
	
	private Map<String,List<PersonalDetail>> splitByUnit (Set<PersonalDetail> personalDetails) {
		
		Map<String, List<PersonalDetail>> unitOfficers = new HashMap<>();
		
		for (PersonalDetail personalDetail : personalDetails) {
			String unitCodeId = personalDetail.getEmployment().getOrganisationOrDepartment();
			List<PersonalDetail> list = unitOfficers.get(unitCodeId);
			if (list == null) {
				list = new ArrayList<>();
				unitOfficers.put(unitCodeId, list);
			}
			list.add(personalDetail);
		}
		
		return unitOfficers;
	}

	@Override
	public boolean activate(String courseId, String requester) throws CourseServiceException {
		
		boolean result = false;
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			Course course = dao.getCourse(courseId);
			
			if (course.getStatus() != CourseStatus.DRAFT) {
				throw new CourseServiceException ("Only draft Course can be activated.");
			}
			
			// Need to have at least one slot
			if (course.getSlots() == null || course.getSlots().isEmpty()) {
				throw new CourseServiceException ("No slot defined for the course.");
			}
			
			MarketingContentSet contentSet = marketingContentDAO.getMarketingContentSet(course.getContentSetId());
			if (contentSet == null) {
				throw new CourseServiceException("Content is not configurated for this course.");
			}
			
			boolean isDateOk = validateCourseDate (course);
			
			// if course date ok, then check content date
			if (isDateOk) {
				isDateOk = validateContentDate (contentSet);
			}
			
			if (isDateOk) {
				course.setStatus(CourseStatus.ACTIVATED);
				dao.updateCourse(course, requester);
				
				Set<String> targetedEmails = new HashSet<>();
				Set<String> targetedMobiles = new HashSet<>();
				
				IMailSenderConfig mailSenderConfig = ServiceConfig.getInstance().getMailSenderConfig(course.getCategoryId());
				Set<PersonalDetail> personalDetails = new HashSet<>();
				
				if (course.getType() == CourseType.COMPULSORY) {
					personalDetails.addAll(dao.getNominatedCourseParticipantDetails (course.getId()));
					
					// Compulsory will send to office email and preferred contact
					retrieveTargetedUserContacts (true, personalDetails, targetedEmails, targetedMobiles);
				}
				else if (course.getType() == CourseType.NON_COMPULSORY) {
					List<String> memberGroupIds = course.getMemberGroupIds();
					for (String memberGroupId : memberGroupIds) {
						personalDetails.addAll(marketingDAO.getPersonnelInGroup(memberGroupId));
					}
					
					// Non compulsory will send to preferred contact only
					retrieveTargetedUserContacts (false, personalDetails, targetedEmails, targetedMobiles);
				}
				
				// Publish the content set.
				String password = mailSenderConfig.senderPassword();
				
				String encryptionKey = EnvironmentUtils.getEncryptionKey();
				
				if (encryptionKey != null && !encryptionKey.isEmpty()) {
					try {
						Encipher encipher = new Encipher(encryptionKey);
                        encipher.decrypt(password);
                    }
					catch (Exception e) {
						logger.info("Error while decrypting the configured password");
					}
				}
				marketingContentService.publishContentSet(contentSet.getId(), new ArrayList<>(targetedEmails),
						new ArrayList<>(targetedMobiles), mailSenderConfig.senderAddress(),
									mailSenderConfig.senderPassword(), requester);
				
				userAnnouncementService.createUserAnnouncements(UserAnnouncementModule.COURSE, course.getId(), personalDetails, contentSet, requester);
				
				result = true;
			}
			
			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to activate course:" + courseId, e);
			SessionFactoryUtil.rollbackTransaction();
			throw new CourseServiceException("Fail to activate course:" + courseId, e);
		}
		
		return result;
	}
	
	/**
	 * Check dates to ensure no past dates and dates are in correct sequence
	 *  Registration open date should be earlier than close date
	 *  Cooling period end date should be later than close date and before earlest slot start date (for non-compulsory only)
	 *  Slot start date should be after close date.
	 *  Reminder date should be after close date and before start date 
	 * 
	 * @param course
	 * @return
	 */
	private boolean validateCourseDate (Course course) {
		
		Date now = new Date ();
		
		// Registration dates
		Date openDate = course.getRegistrationConfig().getOpenDate();
		Date closeDate = course.getRegistrationConfig().getCloseDate();
		
		if (openDate == null) {
			logger.log(Level.SEVERE, "Missing registration start date for course:" + Util.replaceNewLine(course.getId()));
			return false;
		}
		
		if (DateUtils.isBeforeDay(openDate, now)) {
			logger.log(Level.SEVERE, "Registration start date is past date for course:" + Util.replaceNewLine(course.getId()));
			return false;
		}
		
		if (closeDate == null) {
			logger.log(Level.SEVERE, "Missing registration close date for course:" + Util.replaceNewLine(course.getId()));
			return false;
		}
		
		if (DateUtils.isBeforeDay(closeDate, now)) {
			logger.log(Level.SEVERE, "Registration close date is past date for course:" + Util.replaceNewLine(course.getId()));
			return false;
		}
		
		if (DateUtils.isBeforeDay(closeDate, openDate)) {
			logger.log(Level.SEVERE, "Registration close date is earlier than start date for course:" + Util.replaceNewLine(course.getId()));
			return false;
		}
		
		Date earlestSlotStartDate = new Date();
		// Validate slot dates
		List<Slot> slotList = course.getSlots();
		if (slotList != null){
			for (int i=0; i < slotList.size(); i++){
				Slot slot = slotList.get(i);
				Date slotStartDate = slot.getStartDate();
				Date slotEndDate = slot.getEndDate();
				Date reminderDate = slot.getReminderDate();
				
				if (slotStartDate == null) {
					logger.log(Level.SEVERE, "Missing slot start date for slot:" + Util.replaceNewLine(slot.getId()));
					return false;
				}
				
				if (DateUtils.isBeforeDay(slotStartDate, closeDate)) {
					logger.log(Level.SEVERE, "Slot start date is earlier than registration close date for slot:" + Util.replaceNewLine(slot.getId()));
					return false;
				}
				
				if (slotEndDate == null) {
					logger.log(Level.SEVERE, "Missing slot end date for slot:" + Util.replaceNewLine(slot.getId()));
					return false;
				}
				
				if (slotEndDate.before(slotStartDate)) {
					logger.log(Level.SEVERE, "Slot end date is earlier than slot start date for slot:" + Util.replaceNewLine(slot.getId()));
					return false;
				}
				
				// Assume can don't have reminder date
				if (reminderDate != null && DateUtils.isAfterDay(reminderDate, slotStartDate)) {
					logger.log(Level.SEVERE, "Reminder date is after slot start date for slot:" + Util.replaceNewLine(slot.getId()));
					return false;
				}
				
				// Find earliest slot start date
				if ((i == 0) || (earlestSlotStartDate.after(slotStartDate))){
					earlestSlotStartDate = slotStartDate;
				}
			}
		}
		
		// Cooling period (For non-compulsory only)
		if (course.getType() == CourseType.NON_COMPULSORY) {
			Date coolingEndDate = course.getCoolingPeriodEndDate();
			if (coolingEndDate == null) {
				logger.log(Level.SEVERE, "Missing cooling period end date for course:" + Util.replaceNewLine(course.getId()));
				return false;
			}
			
			if (DateUtils.isBeforeDay(coolingEndDate, closeDate)) {
				logger.log(Level.SEVERE, "Cooling period end date is earlier than registration end date for course:" + Util.replaceNewLine(course.getId()));
				return false;
			}
			
			if (DateUtils.isAfterDay(coolingEndDate, earlestSlotStartDate)) {
				logger.log(Level.SEVERE, "Cooling period end date is after earlest slot start date for course:" + Util.replaceNewLine(course.getId()));
				return false;
			}
		}
		
		return true;
	}
	
	private boolean validateContentDate (MarketingContentSet contentSet) {
		
		Date now = new Date ();
		boolean result = true;
		
		String earliestContentID = "";
		Date earliestPublishDate = new Date();
		
		for (int i=0; i< contentSet.getContents().size(); i++){
			MarketingContent content = contentSet.getContents().get(i);
			
			Date publishDate = content.getActualPublishDate();
			
			if (now.after(publishDate)) {
				result = false;
				logger.log(Level.SEVERE, "Marketing content publishing date is past date:" +  Util.replaceNewLine( content.getId() ));
				
				break;
			}
			
			if (i == 0 || earliestPublishDate.after(publishDate)){
				earliestPublishDate = publishDate;
				earliestContentID = content.getId();
				
				logger.log(Level.INFO, "Replace earliest publish date and contentID :" +  earliestPublishDate + " " + earliestContentID);
			}
		}
		
		logger.log(Level.INFO, "Final earliest publish date and contentID :" +  earliestPublishDate + " " + earliestContentID);
		
		long dateDiffinMillies = earliestPublishDate.getTime() - now.getTime();	
		long dateDiffinMin = TimeUnit.MINUTES.convert(dateDiffinMillies, TimeUnit.MILLISECONDS);
		logger.log(Level.INFO, "earliest publish date difference in minutes from now :" +  Util.replaceNewLine( Long.toString(dateDiffinMin) ));
		
		if (dateDiffinMin < 60){ //less than 3 hours
			result = false;
			logger.log(Level.SEVERE, "Marketing content publishing date/time is too close (within 1 hour) to activation date/time:" +  Util.replaceNewLine( earliestContentID ));
		}
		
		return result;
	}
	

	private void retrieveTargetedUserContacts (boolean includeOffice, Set<PersonalDetail> personalDetails, Set<String> targetedEmails, Set<String> targetedMobiles) {
		
		for (PersonalDetail personalDetail : personalDetails) {
			
			// Retrieve office contacts
			if (includeOffice) {
				
				// If more than one office contacts, then first one will be selected.
				for (Email email : personalDetail.getEmailContacts()) {
					if (email.getLabel() == ContactLabel.WORK && email.getAddress() != null) {
						targetedEmails.add(email.getAddress().trim().toLowerCase());
						break;
					}
				}
			}
			
			// if personal preferred mode of contact is email, select the preferred email
			if (personalDetail.getPreferredContactMode() == ContactMode.EMAIL) {
						
				String preferredEmail = null;
						
				// Only interested in email other than office that is
				// configured as preferred.
				for (Email email : personalDetail.getEmailContacts()) {
					if (email.isPrefer() && email.getAddress() != null) {
						preferredEmail = email.getAddress().trim().toLowerCase();
						break;
					}
				}
				
				// As targetedEmails is a Set, duplicate will be removed
				if (preferredEmail != null) {
					targetedEmails.add(preferredEmail);
				}
			}
			else if (personalDetail.getPreferredContactMode() == ContactMode.SMS){
						
				// Select first phone number that is set to preferred.
				for (Phone phone : personalDetail.getPhoneContacts()) {
					if (phone.isPrefer()) {
						targetedMobiles.add(phone.getNumber());
						break;
					}
				}
			}
		}
	}
	
	private void getTargetedUserContacts (boolean includeOffice, Set<PersonalNricEmailPhone> personalDetails, Set<String> targetedEmails, Set<String> targetedMobiles) {
		
		for (PersonalNricEmailPhone personalDetail : personalDetails) {
			
			// Retrieve office contacts
			if (includeOffice) {
				
				// If more than one office contacts, then first one will be selected.
				for (Email email : personalDetail.getEmails()) {
					if (email.getLabel() == ContactLabel.WORK && email.getAddress() != null) {
						targetedEmails.add(email.getAddress().trim().toLowerCase());
						break;
					}
				}
			}
			
			// if personal preferred mode of contact is email, select the preferred email
			if (personalDetail.getPreferredContactMode() == ContactMode.EMAIL) {
						
				String preferredEmail = null;
						
				// Only interested in email other than office that is
				// configured as preferred.
				for (Email email : personalDetail.getEmails()) {
					if (email.isPrefer() && email.getAddress() != null) {
						preferredEmail = email.getAddress().trim().toLowerCase();
						break;
					}
				}
				
				// As targetedEmails is a Set, duplicate will be removed
				if (preferredEmail != null) {
					targetedEmails.add(preferredEmail);
				}
			}
			else if (personalDetail.getPreferredContactMode() == ContactMode.SMS){
						
				// Select first phone number that is set to preferred.
				for (Phone phone : personalDetail.getPhones()) {
					if (phone.isPrefer()) {
						targetedMobiles.add(phone.getNumber());
						break;
					}
				}
			}
		}
	}
	
	
	private void validate (Course newCourse, Course orgCourse) throws CourseServiceException {
		
		if (orgCourse.getStatus() != newCourse.getStatus()) {
			logger.log(Level.SEVERE, "Cannot modify course status");
			throw new CourseServiceException ("Cannot modify course status");
		}
		
		if (!orgCourse.getCategoryId().equals(newCourse.getCategoryId())) {
			logger.log(Level.SEVERE, "Cannot modify course category after activated");
			throw new CourseServiceException ("Cannot modify course category after activated");
		}
		
		if (orgCourse.getType() != newCourse.getType()) {
			logger.log(Level.SEVERE, "Cannot modify course type after activated");
			throw new CourseServiceException ("Cannot modify course type after activated");
		}
				
		//Ensure modified dates are in correct sequence and cannot be earlier than original date
		
		// Registration dates
		Date openDate = newCourse.getRegistrationConfig().getOpenDate();
		Date closeDate = newCourse.getRegistrationConfig().getCloseDate();
			
		if (openDate == null) {
			logger.log(Level.SEVERE, "Missing registration start date");
			throw new CourseServiceException ("Missing registration start date");
		}
		
		if (DateUtils.isBeforeDay(openDate, orgCourse.getRegistrationConfig().getOpenDate())) {
			logger.log(Level.SEVERE, "Registration start date cannot be modified to be earlier than original");
			throw new CourseServiceException ("Registration start date cannot be modified to be earlier than original");
		}
			
		if (closeDate == null) {
			logger.log(Level.SEVERE, "Missing registration close date");
			throw new CourseServiceException ("Missing registration close date");
		}
			
		if (DateUtils.isBeforeDay(closeDate, orgCourse.getRegistrationConfig().getCloseDate())) {
			logger.log(Level.SEVERE, "Registration close date cannot be modified to be earlier than original");
			throw new CourseServiceException ("Registration close date cannot be modified to be earlier than original");
		}
			
		if (DateUtils.isBeforeDay(closeDate, openDate)) {
			logger.log(Level.SEVERE, "Registration close date is earlier than start date");
			throw new CourseServiceException ("Registration close date is earlier than start date");
		}
			
		Date earlestSlotStartDate = new Date();
		// Validate slot dates
		List<Slot> slotList = newCourse.getSlots();
		if (slotList != null) {
			for (int i=0; i< slotList.size(); i++){
				Slot slot = slotList.get(i);
				
				Date slotStartDate = slot.getStartDate();
				Date slotEndDate = slot.getEndDate();
				Date reminderDate = slot.getReminderDate();
					
				if (slotStartDate == null) {
					logger.log(Level.SEVERE, "Missing slot start date");
					throw new CourseServiceException ("Missing slot start date");
				}
					
				if (DateUtils.isBeforeDay(slotStartDate, closeDate)) {
					logger.log(Level.SEVERE, "Slot start date is earlier than registration close date");
					throw new CourseServiceException ("Slot start date is earlier than registration close date");
				}
				
				if (slotEndDate == null) {
					logger.log(Level.SEVERE, "Missing slot end date");
					throw new CourseServiceException ("Missing slot end date");
				}
					
				if (slotEndDate.before(slotStartDate)) {
					logger.log(Level.SEVERE, "Slot end date is earlier than slot start date");
					throw new CourseServiceException ("Slot end date is earlier than slot start date");
				}
					
				// Assume can don't have reminder date
				if (reminderDate != null && DateUtils.isAfterDay(reminderDate, slotStartDate)) {
					logger.log(Level.SEVERE, "Reminder date is after slot start date");
					throw new CourseServiceException ("Reminder date is after slot start date");
				}
					
				// Find earlest slot start date
				if ((i == 0) || (earlestSlotStartDate.after(slotStartDate)))
					earlestSlotStartDate = slotStartDate;
			}
		}
			
		// Cooling period (For non-compulsory only)
		if (orgCourse.getType() == CourseType.NON_COMPULSORY) {
			Date coolingEndDate = orgCourse.getCoolingPeriodEndDate();
			if (coolingEndDate == null) {
				logger.log(Level.SEVERE, "Missing cooling period end date");
				throw new CourseServiceException ("Missing cooling period end date");
			}
				
			if (DateUtils.isBeforeDay(coolingEndDate, closeDate)) {
				logger.log(Level.SEVERE, "Cooling period end date is earlier than registration end date");
				throw new CourseServiceException ("Cooling period end date is earlier than registration end date");
			}
				
			if (DateUtils.isAfterDay(coolingEndDate, earlestSlotStartDate)) {
				logger.log(Level.SEVERE, "Cooling period end date is after earlest slot start date");
				throw new CourseServiceException ("Cooling period end date is after earlest slot start date");
			}
		}
	}
	
	
	private void updatePublishing (Course course, String requester) throws MarketingContentException, UserAnnouncementServiceException {
		
		Set<PersonalDetail> personalDetails = new HashSet<>();
		
		if (course.getType() == CourseType.COMPULSORY) {
			personalDetails.addAll(dao.getNominatedCourseParticipantDetails (course.getId()));
		}
		else if (course.getType() == CourseType.NON_COMPULSORY) {
			
			// List of user that already has participant created.
			// For non compulsory course, it only mean that the user already 
			// registered. So don't need to send to the user.
			List<String> participantNricList = dao.getCourseParticipantNrics (course.getId());
			
			List<String> memberGroupIds = course.getMemberGroupIds();
			for (String memberGroupId : memberGroupIds) {
				List<PersonalDetail> personalInGroup = marketingDAO.getPersonnelInGroup(memberGroupId);
				
				for (PersonalDetail personalDetail : personalInGroup) {
					
					// Ignore those that has CourseParticipant (For non compulsory)
					if (!participantNricList.contains(personalDetail.getNric())) {
						personalDetails.add (personalDetail);
					}
				}
			}
		}
		
		MarketingContentSet contentSet = marketingContentDAO.getMarketingContentSet(course.getContentSetId());
		
		// delete UserAnnouncement that don't have corresponding content id in content set and has yet to publish
		// Content been deleted. Marketing Content side will handle the deleting of the task.
		List<String> contentIds = new ArrayList<>();
		for (MarketingContent content : contentSet.getContents()) {
			contentIds.add(content.getId());
		}
		
		List<UserAnnouncement> pendingAnnouncements = userAnnouncementService.getNonPublishedUserAnnouncements(UserAnnouncementModule.COURSE,
																											course.getId());
		
		for (UserAnnouncement announcement : pendingAnnouncements) {
			if (!contentIds.contains(announcement.getContentId())) {
				userAnnouncementService.deleteUserAnnouncement(announcement, requester);
			}
		}
		
		// Retrieve the contacts for personal
		Set<String> targetedEmails = new HashSet<>();

		Set<String> targetedMobiles = new HashSet<>();
		
		if (course.getType() == CourseType.COMPULSORY) {
			// Compulsory will send to office email and preferred contact
			retrieveTargetedUserContacts (true, personalDetails, targetedEmails, targetedMobiles);
		}
		else if (course.getType() == CourseType.NON_COMPULSORY) {
			// Non compulsory will send to preferred contact only
			retrieveTargetedUserContacts (false, personalDetails, targetedEmails, targetedMobiles);
		}
		
		
		// Add UserAnnouncement that doesn't exists for content that has yet publish
		// Republish the marketing content.
		// Handle the case where new content added or MemberGroup change
		Date now = new Date ();
		for (MarketingContent content : contentSet.getContents()) {
			if (now.before(content.getActualPublishDate())) {
				List<String> userAnnoucementNrics = userAnnouncementService.getUserAnnouncementNrics(UserAnnouncementModule.COURSE, course.getId(), content.getId());
				
				for (PersonalDetail personalDetail : personalDetails) {
					
					if (!userAnnoucementNrics.contains(personalDetail.getNric())) {
						userAnnouncementService.createUserAnnouncement(UserAnnouncementModule.COURSE, course.getId(), personalDetail, content, requester);
					}
				}
				
				// Republish the content.
				marketingContentService.appendContentRecipients (content.getId(), new ArrayList<>(targetedEmails),
						new ArrayList<>(targetedMobiles), requester);
				
			}
		}
	}
	
	private void updateMarketing (Course course, String requester) throws MarketingContentException, UserAnnouncementServiceException {
		
		logger.log(Level.INFO, "[WFC] Update Marketing");
		Set<PersonalNricEmailPhone> personalDetails = new HashSet<>();
		
		if (course.getType() == CourseType.COMPULSORY) {
			personalDetails.addAll(dao.getNominatedCourseParticipants(course.getId()));
		}
		else if (course.getType() == CourseType.NON_COMPULSORY) {
			
			//Consolidate Targeted Personnel
			List<String> memberGroupIds = course.getMemberGroupIds();
			for (String memberGroupId : memberGroupIds) {
				List<PersonalNricEmailPhone> personalInGroup = marketingDAO.getPersonnelNricEmailPhoneInGroup(memberGroupId);
				personalDetails.addAll(personalInGroup);
			}
		}
		
		MarketingContentSet contentSet = marketingContentDAO.getMarketingContentSet(course.getContentSetId());
		
		// Retrieve the contacts for personal
		Set<String> targetedEmails = new HashSet<>();
		Set<String> targetedMobiles = new HashSet<>();
		
		if (course.getType() == CourseType.COMPULSORY) {
			// Compulsory will send to office email and preferred contact
			getTargetedUserContacts (true, personalDetails, targetedEmails, targetedMobiles);
		}
		else if (course.getType() == CourseType.NON_COMPULSORY) {
			// Non compulsory will send to preferred contact only
			getTargetedUserContacts (false, personalDetails, targetedEmails, targetedMobiles);
		}
	
		//Will not add / delete UserAnnouncements for new or deleted
		//content after course is activated to prevent thread hang.
		//Method is resource intensive if target group is big
		Date now = new Date ();
		for (MarketingContent content : contentSet.getContents()) {
			if (now.before(content.getActualPublishDate())) {
				// Republish the content.
				marketingContentService.appendContentRecipients (content.getId(), new ArrayList<>(targetedEmails),
						new ArrayList<>(targetedMobiles), requester);
			}
		}
	}

	@Override
	public void acknowledgeNominatedCourse(CourseParticipant participant, String requester) throws CourseServiceException {
		throw new UnsupportedOperationException ("Internet side only operation.");
	}

	@Override
	public void registerCourse(CourseParticipant participant, String requester) throws CourseServiceException {
		throw new UnsupportedOperationException ("Internet side only operation.");
	}

	@Override
	public void reRegisterCourse(CourseParticipant participant, String requester) throws CourseServiceException {
		throw new UnsupportedOperationException ("Internet side only operation.");
	}

	@Override
	public void withdrawCourse(CourseParticipant participant, String requester) throws CourseServiceException {
		throw new UnsupportedOperationException ("Internet side only operation.");
	}

	@Override
	public void confirmCourse(CourseParticipant participant, String requester) throws CourseServiceException {
		throw new UnsupportedOperationException ("Internet side only operation.");
	}

	@Override
	public void cancel(String courseId, String requester) throws CourseServiceException {
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			Course course = dao.getCourse(courseId);
			
			if (course.getStatus() != CourseStatus.ACTIVATED) {
				throw new CourseServiceException ("Only activated Course can be cancelled.");
			}
			
			userAnnouncementService.removePendingUserAnnouncements(UserAnnouncementModule.COURSE, courseId, requester);
			
			// Cancel publishing
			marketingContentService.cancelPublishContentSet(course.getContentSetId(), requester);
			
			// Update course status to cancelled
			course.setStatus(CourseStatus.CANCELLED);
			dao.updateCourse(course, requester);
			
			
			// Check if registration started. 
			// Only need to inform of cancellation if course started.
			if (CourseUtil.isRegistrationStarted(course)) {
				// Inform participant of the cancellation
				emailSmsHelper.informOfCancellation (course);
			}
			
			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to activate course:" + courseId, e);
			SessionFactoryUtil.rollbackTransaction();
			throw new CourseServiceException("Fail to activate course:" + courseId, e);
		}
	}

	
	@Override
	public void processTask () throws CourseServiceException {
		
		processSendReminderTask ();
	
	}

	public void processSendReminderTask() {
		
		List<Course> courses = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			courses = dao.getCoursePendingReminder ();
			
			for (Course course : courses) {
				emailSmsHelper.sendCourseReminder(course);
			}
			
			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to retrieve courses pending reminder", e);
			SessionFactoryUtil.rollbackTransaction();
		}
	}

	@Override
	public void vacancyAllocation() throws CourseServiceException {
		
		// Perform auto vacancy allocation
		vacancyAllocationHandler.vacancyAllocation();
		
		// Perform broadcasting out outcome to participant for
		// course with manual broadcast setting. 
		// Only broadcast if course admin has confirm the outcome or
		// admin has not confirm after the configured cutoff date
		vacancyAllocationHandler.processBroadcastOutcomeTask ();
		
		// Perform sending reminder to course admin to confirm 
		// vacancy allocation outcome.
		vacancyAllocationHandler.processBroadcastOutcomeReminder ();
	}

	@Override
	public void handleWithdrawal(CourseParticipant participant) throws CourseServiceException {
		
		// Inform supervisor
		try {
			SessionFactoryUtil.beginTransaction();
			
			emailSmsHelper.informSupervisorWithdrawal(participant);
			
			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to notify supervisor of withdrawal", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new CourseServiceException("Fail to notify supervisor of withdrawal", e);
		}
		
		
		vacancyAllocationHandler.allocateForWithdrawal (participant);
	}

	@Override
	public List<UserCourseHistory> getUserCourseHistories (String nric, Date startDate, Date endDate) throws CourseServiceException {
		throw new UnsupportedOperationException ("Internet side only operation.");
	}

	@Override
	public void requestBroadcastAllocationOutcome (String courseId, String requester) throws CourseServiceException {
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			Course course = dao.getCourse(courseId);
			
			if (course.getStatus() != CourseStatus.ACTIVATED) {
				throw new CourseServiceException ("Request broadcast allocation outcome only applicable to activated course.");
			}
			
			if (course.getType() != CourseType.NON_COMPULSORY) {
				throw new CourseServiceException ("Request broadcast allocation outcome only applicable to non-compulsory course.");
			}
			
			if (course.getRegistrationConfig().isAutoOutcomeBroadcast()) {
				throw new CourseServiceException ("Request broadcast allocation outcome only applicable to course configured for manual broadcast.");
			}
			
			if (course.getRegistrationConfig().isOutcomeBroadcasted()) {
				throw new CourseServiceException ("Vacancy allocation outcome already broadcasted.");
			}
			
			Date now = new Date ();
			
			// Registration dates
			Date closeDate = course.getRegistrationConfig().getCloseDate();
			
			if (DateUtils.isBeforeDay(now, closeDate)) {
				throw new CourseServiceException ("Request broadcast allocation outcome only applicable to course with registration closed.");
			}
			
			course.getRegistrationConfig().setOutcomeBroadcasted(true);
			
			dao.updateCourse(course, requester);
			
			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to request broadcast allocation outcome for course:" + courseId, e);
			SessionFactoryUtil.rollbackTransaction();
			throw new CourseServiceException("Fail to request broadcast allocation outcome for course:" + courseId, e);
		}
	}

	@Override
	public boolean activateWithThread(String courseId, String requester) throws CourseServiceException {
		
		boolean result = false;
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			Course course = dao.getCourse(courseId);
			
			if (course.getStatus() != CourseStatus.DRAFT) {
				throw new CourseServiceException ("Only draft Course can be activated.");
			}
			
			// Need to have at least one slot
			if (course.getSlots() == null || course.getSlots().isEmpty()) {
				throw new CourseServiceException ("No slot defined for the course.");
			}
			
			MarketingContentSet contentSet = marketingContentDAO.getMarketingContentSet(course.getContentSetId());
			if (contentSet == null) {
				throw new CourseServiceException("Content is not configurated for this course.");
			}
			
			boolean isDateOk = validateCourseDate (course);
			
			// if course date ok, then check content date
			if (isDateOk) {
				isDateOk = validateContentDate (contentSet);
			}
			
			if (isDateOk) {
				course.setStatus(CourseStatus.ACTIVATED);
				dao.updateCourse(course, requester);
				
				Runnable runnable = new CourseActivateThread(course, requester);
				Thread activateThread = new Thread(runnable);
				activateThread.start();
				result = true;
			}

			SessionFactoryUtil.commitTransaction();
			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to activate Course" , e);
			SessionFactoryUtil.rollbackTransaction();
			throw new CourseServiceException("Fail to activate Course",e);
		}
		
		logger.log(Level.INFO,"Proceeding to return");
		return result;
	}
	
	public void addParticipant(CourseParticipant participant, String requester) throws CourseServiceException {
		try {
			SessionFactoryUtil.beginTransaction();
			dao.addCourseParticipant(participant, requester);
			
			Course course = dao.getCourse(participant.getCourseId());
			
			Slot slot = null;
			// Get Participant Slot Details
			for (Slot courseSlot : course.getSlots()) {
				if (courseSlot.getId().equals(participant.getSlotId())) {
					slot = courseSlot;
				}
			}
			
			PersonalDetail personalDetail = personnelDAO.getPersonal(participant.getNric());
			
			Set<String> targetedEmails = new HashSet<>();
			Set<String> targetedMobiles = new HashSet<>();
			
			CourseUtil.retrieveParticipantContacts(true, personalDetail, targetedEmails, targetedMobiles);
			
			if (course.getStatus() == CourseStatus.ACTIVATED && course.getType() == CourseType.COMPULSORY) {
				if (participant.getStatus() == ParticipantStatus.NOMINATED) {
					MarketingContentSet contentSet = marketingContentDAO.getMarketingContentSet(course.getContentSetId());
					
					Date now = new Date ();
					for (MarketingContent content : contentSet.getContents()) {
				
						//Only need to handle content that yet to publish
						if (now.before(content.getActualPublishDate())) {
							
							marketingContentService.appendContentRecipients(content.getId(), new ArrayList<>(targetedEmails),
									new ArrayList<>(targetedMobiles), requester);
							
							userAnnouncementService.createUserAnnouncement(UserAnnouncementModule.COURSE, course.getId(), personalDetail, content, requester);
						} else { // Publish date time has passed, will send broadcast directly to officer
							userAnnouncementService.createUserAnnouncement(UserAnnouncementModule.COURSE, course.getId(), personalDetail, content, requester);
							
							IMailSenderConfig mailSenderConfig = ServiceConfig.getInstance().getMailSenderConfig(course.getCategoryId());
							
							String senderEmail = mailSenderConfig.senderAddress();
							String senderPassword = mailSenderConfig.senderPassword();
							
							String encryptionKey = EnvironmentUtils.getEncryptionKey();
							
							if (encryptionKey != null && !encryptionKey.isEmpty()) {
								try {
									Encipher encipher = new Encipher(encryptionKey);
									senderPassword = encipher.decrypt(senderPassword);
								}
								catch (Exception e) {
									logger.info("Error while decrypting the configured password");
								}
							}
			
							if (!targetedEmails.isEmpty()){
								for (String address: targetedEmails) {
									emailUtil.sendEmail(content, address, senderEmail, senderPassword);
								}
							}
						}
					}
				} else {
					emailSmsHelper.informParticipantStatus(course, slot, participant, personalDetail, targetedEmails, targetedMobiles);
				}
			} else if (course.getStatus() == CourseStatus.ACTIVATED && course.getType() == CourseType.NON_COMPULSORY) {
				emailSmsHelper.informParticipantStatus(course, slot, participant, personalDetail, targetedEmails, targetedMobiles);
			}
			
			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to add course participant", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new CourseServiceException("Fail to add course participant", e);
		}
	}
	
	public void updateParticipant(CourseParticipant participant, String requester) throws CourseServiceException {
		try {
			SessionFactoryUtil.beginTransaction();
			dao.updateCourseParticipant(participant, requester);
			
			Course course = dao.getCourse(participant.getCourseId());
			
			Slot slot = null;
			// Get Participant Slot Details
			for (Slot courseSlot : course.getSlots()) {
				if (courseSlot.getId().equals(participant.getSlotId())) {
					slot = courseSlot;
				}
			}
			
			PersonalDetail personalDetail = personnelDAO.getPersonal(participant.getNric());
			
			Set<String> targetedEmails = new HashSet<>();
			Set<String> targetedMobiles = new HashSet<>();
			
			CourseUtil.retrieveParticipantContacts(true, personalDetail, targetedEmails, targetedMobiles);
			
			if (course.getStatus() == CourseStatus.ACTIVATED && course.getType() == CourseType.COMPULSORY) {
				if (participant.getStatus() == ParticipantStatus.NOMINATED) {
					MarketingContentSet contentSet = marketingContentDAO.getMarketingContentSet(course.getContentSetId());
					
					Date now = new Date ();
					for (MarketingContent content : contentSet.getContents()) {
				
						//Only need to handle content that yet to publish
						if (now.before(content.getActualPublishDate())) {
							
							marketingContentService.appendContentRecipients(content.getId(), new ArrayList<>(targetedEmails),
									new ArrayList<>(targetedMobiles), requester);
							
							userAnnouncementService.createUserAnnouncement(UserAnnouncementModule.COURSE, course.getId(), personalDetail, content, requester);
						} else { // Publish date time has passed, will send broadcast directly to officer
							userAnnouncementService.createUserAnnouncement(UserAnnouncementModule.COURSE, course.getId(), personalDetail, content, requester);
							
							IMailSenderConfig mailSenderConfig = ServiceConfig.getInstance().getMailSenderConfig(course.getCategoryId());
							
							String senderEmail = mailSenderConfig.senderAddress();
							String senderPassword = mailSenderConfig.senderPassword();
							
							String encryptionKey = EnvironmentUtils.getEncryptionKey();
							
							if (encryptionKey != null && !encryptionKey.isEmpty()) {
								try {
									Encipher encipher = new Encipher(encryptionKey);
									senderPassword = encipher.decrypt(senderPassword);
								}
								catch (Exception e) {
									logger.info("Error while decrypting the configured password");
								}
							}
			
							if (!targetedEmails.isEmpty()){
								for (String address: targetedEmails) {
									emailUtil.sendEmail(content, address, senderEmail, senderPassword);
								}
							}
						}
					}
				} else {
					emailSmsHelper.informParticipantStatus(course, slot, participant, personalDetail, targetedEmails, targetedMobiles);
				}
			} else if (course.getStatus() == CourseStatus.ACTIVATED && course.getType() == CourseType.NON_COMPULSORY) {
				emailSmsHelper.informParticipantStatus(course, slot, participant, personalDetail, targetedEmails, targetedMobiles);
			}
			
			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to update course participant", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new CourseServiceException("Fail to update course participant", e);
		}
	}
	
	public void addInformParticipant (CourseParticipant participant, String requester) throws CourseServiceException {
		try {
			SessionFactoryUtil.beginTransaction();
			dao.addCourseParticipant(participant, requester);
			
			Course course = dao.getCourse(participant.getCourseId());
			
			Slot slot = null;
			// Get Participant Slot Details
			for (Slot courseSlot : course.getSlots()) {
				if (courseSlot.getId().equals(participant.getSlotId())) {
					slot = courseSlot;
				}
			}
			
			PersonalDetail personalDetail = personnelDAO.getPersonal(participant.getNric());
			
			Set<String> targetedEmails = new HashSet<>();
			Set<String> targetedMobiles = new HashSet<>();
			
			CourseUtil.retrieveParticipantContacts(true, personalDetail, targetedEmails, targetedMobiles);
			
			if (course.getStatus() == CourseStatus.ACTIVATED && course.getType() == CourseType.COMPULSORY) {
				if (participant.getStatus() == ParticipantStatus.NOMINATED) {
					MarketingContentSet contentSet = marketingContentDAO.getMarketingContentSet(course.getContentSetId());
					
					for (MarketingContent content : contentSet.getContents()) {
				
						// Will send broadcast directly to officer regardless of publish date time
						userAnnouncementService.createUserAnnouncement(UserAnnouncementModule.COURSE, course.getId(), personalDetail, content, requester);
						
						IMailSenderConfig mailSenderConfig = ServiceConfig.getInstance().getMailSenderConfig(course.getCategoryId());
						
						String senderEmail = mailSenderConfig.senderAddress();
						String senderPassword = mailSenderConfig.senderPassword();
						
						String encryptionKey = EnvironmentUtils.getEncryptionKey();
						
						if (encryptionKey != null && !encryptionKey.isEmpty()) {
							try {
								Encipher encipher = new Encipher(encryptionKey);
								senderPassword = encipher.decrypt(senderPassword);
							}
							catch (Exception e) {
								logger.info("Error while decrypting the configured password");
							}
						}
		
						if (!targetedEmails.isEmpty()){
							for (String address: targetedEmails) {
								emailUtil.sendEmail(content, address, senderEmail, senderPassword);
							}
						}
						
					}
				} else if (participant.getStatus() == ParticipantStatus.SUCCESSFUL) {
					emailSmsHelper.informParticipantStatus(course, slot, participant, personalDetail, targetedEmails, targetedMobiles);
				}
			} else if (course.getStatus() == CourseStatus.ACTIVATED && course.getType() == CourseType.NON_COMPULSORY) {
				emailSmsHelper.informParticipantStatus(course, slot, participant, personalDetail, targetedEmails, targetedMobiles);
			}
			
			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to add course participant", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new CourseServiceException("Fail to add course participant", e);
		}
	}
	
	public void updateInformParticipant(CourseParticipant participant, String requester) throws CourseServiceException {
		try {
			SessionFactoryUtil.beginTransaction();
			dao.updateCourseParticipant(participant, requester);
			
			Course course = dao.getCourse(participant.getCourseId());
			
			Slot slot = null;
			// Get Participant Slot Details
			for (Slot courseSlot : course.getSlots()) {
				if (courseSlot.getId().equals(participant.getSlotId())) {
					slot = courseSlot;
				}
			}
			
			PersonalDetail personalDetail = personnelDAO.getPersonal(participant.getNric());
			
			Set<String> targetedEmails = new HashSet<>();
			Set<String> targetedMobiles = new HashSet<>();
			
			CourseUtil.retrieveParticipantContacts(true, personalDetail, targetedEmails, targetedMobiles);
			
			if (course.getStatus() == CourseStatus.ACTIVATED && course.getType() == CourseType.COMPULSORY) {
				if (participant.getStatus() == ParticipantStatus.NOMINATED) {
					MarketingContentSet contentSet = marketingContentDAO.getMarketingContentSet(course.getContentSetId());
					
					for (MarketingContent content : contentSet.getContents()) {
				
						 // Will send broadcast directly to officer regardless of publish date time
						userAnnouncementService.createUserAnnouncement(UserAnnouncementModule.COURSE, course.getId(), personalDetail, content, requester);
						
						IMailSenderConfig mailSenderConfig = ServiceConfig.getInstance().getMailSenderConfig(course.getCategoryId());
						
						String senderEmail = mailSenderConfig.senderAddress();
						String senderPassword = mailSenderConfig.senderPassword();
						
						String encryptionKey = EnvironmentUtils.getEncryptionKey();
						
						if (encryptionKey != null && !encryptionKey.isEmpty()) {
							try {
								Encipher encipher = new Encipher(encryptionKey);
								senderPassword = encipher.decrypt(senderPassword);
							}
							catch (Exception e) {
								logger.info("Error while decrypting the configured password");
							}
						}
		
						if (!targetedEmails.isEmpty()){
							for (String address: targetedEmails) {
								emailUtil.sendEmail(content, address, senderEmail, senderPassword);
							}
						}
						
					}
				} else if (participant.getStatus() == ParticipantStatus.SUCCESSFUL || participant.getStatus() == ParticipantStatus.UNSUCCESSFUL ) {
					emailSmsHelper.informParticipantStatus(course, slot, participant, personalDetail, targetedEmails, targetedMobiles);
				}
			} else if (course.getStatus() == CourseStatus.ACTIVATED && course.getType() == CourseType.NON_COMPULSORY) {
				emailSmsHelper.informParticipantStatus(course, slot, participant, personalDetail, targetedEmails, targetedMobiles);
			}
			
			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to update course participant", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new CourseServiceException("Fail to update course participant", e);
		}
	}
	
	public void processCoolingEndCourses (Date date) throws CourseServiceException{
		List<Course> courses = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			courses = dao.getCoolingEndCourses(date);
			
			logger.info("Courses with Cooling End Date on " + date.toString());
			for (Course course : courses) {
				logger.info("Course ID: " + course.getId());
				emailSmsHelper.sendCoolingEndEmail(course);
			}
			
			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to retrieve courses pending reminder", e);
			SessionFactoryUtil.rollbackTransaction();
		}
	}
	
	public void saveWaitListTask (WaitListTask task, Course course, Slot slot, CourseParticipant participant) {
			
		try {
			SessionFactoryUtil.beginTransaction();
			dao.saveWaitListTask(task);
			
			emailSmsHelper.informSelectedStatus(course, slot, participant);
			
			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to save waitlist task ", e);
			SessionFactoryUtil.rollbackTransaction();
		}

	}
}
