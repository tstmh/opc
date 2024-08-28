package com.stee.spfcore.webapi.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stee.spfcore.webapi.dao.CalendarDAO;
import com.stee.spfcore.webapi.dao.CodeDAO;
import com.stee.spfcore.webapi.dao.CorporateCardDAO;
import com.stee.spfcore.webapi.dao.GlobalParametersDAO;
import com.stee.spfcore.webapi.dao.PersonalDetailDAO;
import com.stee.spfcore.webapi.dao.TemplateDAO;
import com.stee.spfcore.webapi.model.DateStartEnd;
import com.stee.spfcore.webapi.model.calendar.PublicHoliday;
import com.stee.spfcore.webapi.model.code.Code;
import com.stee.spfcore.webapi.model.code.CodeType;
import com.stee.spfcore.webapi.model.corporateCard.CardBooking;
import com.stee.spfcore.webapi.model.corporateCard.CardBookingStatus;
import com.stee.spfcore.webapi.model.corporateCard.CardCollectionDaysDescriptionConstants;
import com.stee.spfcore.webapi.model.corporateCard.CardCollectionDetail;
import com.stee.spfcore.webapi.model.corporateCard.CardType;
import com.stee.spfcore.webapi.model.corporateCard.CardTypeDetail;
import com.stee.spfcore.webapi.model.globalParameters.GlobalParameters;
import com.stee.spfcore.webapi.model.membership.Membership;
import com.stee.spfcore.webapi.model.personnel.ContactMode;
import com.stee.spfcore.webapi.model.personnel.Employment;
import com.stee.spfcore.webapi.model.personnel.PersonalDetail;
import com.stee.spfcore.webapi.model.template.Template;
import com.stee.spfcore.webapi.model.vo.corporateCard.CardLessDetail;
import com.stee.spfcore.webapi.model.vo.corporateCard.CardNextHolderPreferredContacts;
import com.stee.spfcore.webapi.model.vo.corporateCard.CardPreviousHolderPreferredContacts;
import com.stee.spfcore.webapi.model.vo.personnel.PersonalPreferredContacts;
import com.stee.spfcore.webapi.notification.BatchElectronicMail;
import com.stee.spfcore.webapi.service.config.IMailSenderConfig;
import com.stee.spfcore.webapi.service.config.ServiceConfig;
import com.stee.spfcore.webapi.service.corporateCard.CorporateCardException;
import com.stee.spfcore.webapi.service.notification.INotificationService;
import com.stee.spfcore.webapi.service.notification.NotificationServiceException;
import com.stee.spfcore.webapi.service.notification.NotificationServiceFactory;
import com.stee.spfcore.webapi.utils.ConvertUtil;
import com.stee.spfcore.webapi.utils.DateTimeUtil;
import com.stee.spfcore.webapi.utils.Encipher;
import com.stee.spfcore.webapi.utils.EnvironmentUtils;
import com.stee.spfcore.webapi.utils.Util;

@Service
public class CorporateCardService {
    private static final String PCWF_CARD_MODULE_NAME = "CCBP";
    private static final String GROUP_CARD_MODULE_NAME = "CCBG";
    private static final String SUCCESS_CARD_BOOKING_TEMPLATE_NAME = "CCB-E012";
    private static final String SUCCESS_PREV_HOLDER_TEMPLATE_NAME = "CCB-E013";
    private static final String SUCCESS_NEXT_HOLDER_TEMPLATE_NAME = "CCB-E014";
    private static final String PCWF_CARD = "PCWF";
    private static final String POLICE_WELFARE_DIVISION = "Police Welfare Division";
    private static final String CANCEL_CARD_FOR_NEXT_PREV_HOLDER_TEMPLATE_NAME = "CCB-E015";

	private CorporateCardDAO corporateCardDAO;
	private GlobalParametersDAO globalParametersDao;
	private TemplateDAO templateDao;
	private CalendarDAO calendarDao;
	private CodeDAO codeDao;
	private PersonalDetailDAO personnelDao;
	
	private static final Logger logger = Logger.getLogger(CorporateCardService.class.getName());
	
	@Autowired
	public CorporateCardService ( CorporateCardDAO corporateCardDAO, GlobalParametersDAO globalParametersDao, 
			TemplateDAO templateDao, CalendarDAO calendarDao, CodeDAO codeDao, PersonalDetailDAO personnelDao) {
		this.corporateCardDAO = corporateCardDAO;
		this.globalParametersDao = globalParametersDao;
		this.templateDao = templateDao;
		this.calendarDao = calendarDao;
		this.codeDao = codeDao;
		this.personnelDao = personnelDao;
	}
	
	@Transactional
	public CardBooking getCardBooking ( String cardBookingId ) {
		return corporateCardDAO.getCardBooking(cardBookingId);
	}
	
	@Transactional
	public CardTypeDetail getCardTypeDetail ( String cardTypeId ) {
		return corporateCardDAO.getCardTypeDetail(cardTypeId);
	}
	
	@Transactional
	public Membership getMembership ( String nric ) {
		return corporateCardDAO.getMembership(nric);
	}
	
	@Transactional
	public List<CardBooking> getCardBookings ( Date startDate, Date endDate, List< String > cardTypeIds, List< CardBookingStatus > cardBookingStatuses, String nric ) {
		return corporateCardDAO.getCardBookings(startDate, endDate, cardTypeIds, cardBookingStatuses, nric);
	}
		
	@Transactional
	public CardBooking getApprovedCardBooking ( Date startDate, Date endDate, String allocatedSerialNumber ) {
		return corporateCardDAO.getApprovedCardBooking(startDate, endDate, allocatedSerialNumber);
	}
	
	@Transactional
	public List< CardTypeDetail > getAllCreatedEligibleCardTypeDetails ( boolean isPcwfMember, String department ) {
		return corporateCardDAO.getAllCreatedPcwfGroupCardTypeDetails(isPcwfMember, department, department);
	}
	
	@Transactional
	public List<PublicHoliday> getPublicHolidays ( Date date ) {
		return corporateCardDAO.getPublicHolidays(date);
	}

	@Transactional
	public int getNumberOfCardBookingCancellations ( String nric, Date startDate, Date endDate ) {
		
		return corporateCardDAO.getNumberOfCardBookingCancellations(nric, startDate, endDate);
	}
	
	@Transactional
	public String addCardBooking ( CardBooking cardBooking, String requester ) {
		cardBooking.setId(null);
		cardBooking.setUpdater(requester);
		cardBooking.setUpdateDateTime(new Date());
		
		return corporateCardDAO.addCardBooking(cardBooking);
	}

	@Transactional
	public void updateCardBooking ( CardBooking cardBooking, String requester ) {
		
		cardBooking.setUpdater(requester);
		cardBooking.setUpdateDateTime(new Date());
		
		corporateCardDAO.updateCardBooking(cardBooking);
	}
		
	@Transactional
	public String addCardTypeDetail ( CardTypeDetail cardTypeDetail, String requester ) {
		
		if ( cardTypeDetail.getType() == CardType.PCWF ) {
            cardTypeDetail.setDepartment( null );
        }
		
		cardTypeDetail.setId(null);
        cardTypeDetail.setUpdater( requester );
        cardTypeDetail.setUpdateDateTime( new Date() );

        logger.log (Level.INFO, "addCardTypeDetail: " + cardTypeDetail);
        
		return corporateCardDAO.addCardTypeDetail(cardTypeDetail);
	}

	@Transactional
	public void updateCardTypeDetail ( CardTypeDetail cardTypeDetail, String requester ) {
		
		cardTypeDetail.setUpdater(requester);
		cardTypeDetail.setUpdateDateTime(new Date());
		
		corporateCardDAO.updateCardTypeDetail(cardTypeDetail);
	}

	@Transactional
	public PersonalDetail getPersonal(String nric) {
		return corporateCardDAO.getPersonal(nric);
	}
	
	public void sendEmailForSuccessfulBookings(CardBooking cardBooking) throws CorporateCardException
    {
    		 try {
                 
                 String cardTypeId = ( cardBooking != null ) ? cardBooking.getCardTypeId() : null;
                 
                 CardTypeDetail cardTypeDetail = ( cardTypeId != null ) ? corporateCardDAO.getCardTypeDetail( cardTypeId ) : null;
                 CardType cardType = ( cardTypeDetail != null ) ? cardTypeDetail.getType() : null;
                 logger.info("cardType>>>"+cardType.toString());
                 
                 Template successTemplate = templateDao.getTemplateByName(SUCCESS_CARD_BOOKING_TEMPLATE_NAME);
                 logger.info("successTemplate>>>>>>"+successTemplate.toString());
                 Template prevHolderTemplate = templateDao.getTemplateByName(SUCCESS_PREV_HOLDER_TEMPLATE_NAME);
                 logger.info("prevHolderTemplate>>>>>>"+prevHolderTemplate.toString());
                 Template nextHolderTemplate = templateDao.getTemplateByName(SUCCESS_NEXT_HOLDER_TEMPLATE_NAME);
                 logger.info("nextHolderTemplate>>>>>>"+nextHolderTemplate.toString());
                 GlobalParameters globalParameters = null;
                 String senderEmail;
                 String senderEmailPassword;
                 String emailSignOff;
                 
                 if (cardType == CardType.PCWF) {
                	 globalParameters = globalParametersDao.getGlobalParametersByUnit(PCWF_CARD, null);
                 } else {
                	 globalParameters = globalParametersDao.getGlobalParametersByUnit(cardTypeDetail.getDepartment(), null);
                 }
                 
                 if (globalParameters != null) {
                	   senderEmail = globalParameters.getSenderEmail();
                       senderEmailPassword = globalParameters.getSenderEmailPassword();
                       emailSignOff = globalParameters.getEmailSignOff();
                 } else {
                	 logger.info("globalParameter is null, using default setting from IMailSenderConfig ..");
                	 if (cardType == CardType.PCWF) {
                		 IMailSenderConfig config = ServiceConfig.getInstance().getMailSenderConfig( PCWF_CARD_MODULE_NAME );
                    	 senderEmail = config.senderAddress();
                    	 senderEmailPassword = config.senderPassword();
                    	 emailSignOff = POLICE_WELFARE_DIVISION;
                	 } else {
                		 IMailSenderConfig config = ServiceConfig.getInstance().getMailSenderConfig( GROUP_CARD_MODULE_NAME );
                    	 senderEmail = config.senderAddress();
                    	 senderEmailPassword = config.senderPassword();
                    	 emailSignOff = null;
                	 }
                 }
                 
    			 logger.info("=== START populatePreferredContacts ===");
                 this.populatePreferredContacts( cardBooking, true, true, true );
                 logger.info("=== END populatePreferredContacts ===");
                 
                 PersonalPreferredContacts personalPreferredContact = ( cardBooking != null ) ? cardBooking.getPersonalPreferredContacts() : null;
                 String recipientName = ( personalPreferredContact != null ) ? personalPreferredContact.getName() : "Officer";
                 List <CardPreviousHolderPreferredContacts> previousHolderPreferredContacts = ( cardBooking != null ) ? cardBooking.getPreviousHolderPreferredContacts() : new ArrayList<CardPreviousHolderPreferredContacts>();
                 List <CardNextHolderPreferredContacts> nextHolderPreferredContacts = ( cardBooking != null ) ? cardBooking.getNextHolderPreferredContacts() : new ArrayList<CardNextHolderPreferredContacts>();
                 
                 List <String> allocatedSerialNumbers = ( cardBooking != null) ? cardBooking.getAllocatedCardSerialNumbers() : new ArrayList<String>();
                         
                 String successEmailBody = ( successTemplate != null) ? successTemplate.getEmailBody() : null;
                 String successEmailSubject = ( successTemplate != null) ? successTemplate.getEmailSubject() : null;
                 String successSmsText = ( successTemplate != null ) ? successTemplate.getSmsText() : null;
                 
                 DateStartEnd dateStartEnd = cardBooking.getDateStartEnd();
             	 String visitStartDate = ( dateStartEnd != null ) ? ConvertUtil.convertDateToDateString(dateStartEnd.getStart()): "-";
             	 String visitEndDate = ( dateStartEnd != null ) ? ConvertUtil.convertDateToDateString(dateStartEnd.getEnd()) : "-";
             	 
             	 //Email to Officer
             	 successEmailBody = this.getTemplateForHandTakeOver(successEmailBody, cardTypeDetail, cardBooking, allocatedSerialNumbers, recipientName, null, emailSignOff);
             	 successEmailSubject = this.getTemplateForHandTakeOver(successEmailSubject, cardTypeDetail, cardBooking, allocatedSerialNumbers, recipientName, null, emailSignOff);
             	 successSmsText = this.getTemplateForHandTakeOver(successSmsText, cardTypeDetail, cardBooking, allocatedSerialNumbers, recipientName, null, emailSignOff);
             	 
             	 logger.info("Officer Name: " + recipientName + " ,Serial Numbers: " + allocatedSerialNumbers);
             	 //this.sendCancelCardBookingNotification(personalPreferredContact, cardType, successEmailSubject, successEmailBody, successSmsText);
             	 this.sendCardBookingNotification(personalPreferredContact, senderEmail, senderEmailPassword, successEmailSubject, successEmailBody, successSmsText);
             	 
                 //Serial Numbers to take over from previous holder
                 if (previousHolderPreferredContacts.size() > 0) {
                	 logger.info("Need to inform previous holder");
                     for (CardPreviousHolderPreferredContacts previousHolderPreferredContact : previousHolderPreferredContacts) {
                    	 
                    	 String prevHolderEmailBody = ( prevHolderTemplate != null) ? prevHolderTemplate.getEmailBody() : null;
                         String prevHolderEmailSubject = ( prevHolderTemplate != null) ? prevHolderTemplate.getEmailSubject() : null;
                         String prevHolderSmsText = ( prevHolderTemplate != null ) ? prevHolderTemplate.getSmsText() : null;
                    	 
                    	 List<CardLessDetail> cardLessDetails = ( previousHolderPreferredContact != null ) ? previousHolderPreferredContact.getCardLessDetails(): new ArrayList<CardLessDetail>();
                    	 List <String> cardSerialNumbers = new ArrayList<String>();
                    	 for (CardLessDetail cardLessDetail : cardLessDetails) {
                    		 String serialNumber = cardLessDetail.getSerialNumber();
                    		 if (serialNumber != null) {
                    			 cardSerialNumbers.add(serialNumber);
                    		 }
                    	 }
                    	 
                    	 String handTakeOverName = (previousHolderPreferredContact != null) ? previousHolderPreferredContact.getName() : "an officer";
                    	 logger.info("Previous Holder Name: " + handTakeOverName + ", Serial Numbers: " + cardSerialNumbers);
                    	 
                    	 prevHolderEmailBody = this.getTemplateForHandTakeOver(prevHolderEmailBody, cardTypeDetail, cardBooking, cardSerialNumbers, handTakeOverName, recipientName, emailSignOff);
                    	 prevHolderEmailSubject = this.getTemplateForHandTakeOver(prevHolderEmailSubject, cardTypeDetail, cardBooking, cardSerialNumbers, handTakeOverName, recipientName, emailSignOff);
                    	 prevHolderSmsText = this.getTemplateForHandTakeOver(prevHolderSmsText, cardTypeDetail, cardBooking, cardSerialNumbers, handTakeOverName, recipientName, emailSignOff);
                    	 
                    	 //Inform previous holder who to hand over to 
                    	 //this.sendCancelCardBookingNotification(previousHolderPreferredContact, cardType, prevHolderEmailSubject, prevHolderEmailBody, prevHolderSmsText);
                    	 this.sendCardBookingNotification(previousHolderPreferredContact, senderEmail, senderEmailPassword, prevHolderEmailSubject, prevHolderEmailBody, prevHolderSmsText);
                     }	  	
                }
                //Serial numbers to hand over to next holder
                if (nextHolderPreferredContacts.size() > 0) {
                	logger.info("Need to inform next holder");
                    for (CardNextHolderPreferredContacts nextHolderPreferredContact : nextHolderPreferredContacts) {
                    	
                    	String nextHolderEmailBody = ( nextHolderTemplate != null) ? nextHolderTemplate.getEmailBody() : null;
                        String nextHolderEmailSubject = ( nextHolderTemplate != null) ? nextHolderTemplate.getEmailSubject() : null;
                        String nextHolderSmsText = ( nextHolderTemplate != null ) ? nextHolderTemplate.getSmsText() : null;
                    	
                    	List<CardLessDetail> cardLessDetails = ( nextHolderPreferredContact != null ) ? nextHolderPreferredContact.getCardLessDetails() : new ArrayList<CardLessDetail>();
                    	List <String> cardSerialNumbers = new ArrayList<String>();
                    	for (CardLessDetail cardLessDetail : cardLessDetails) {
                    		String serialNumber = cardLessDetail.getSerialNumber();
                    			if (serialNumber != null) {
                    				cardSerialNumbers.add(serialNumber);
                    			}
                    	}
                    	
                    	 String handTakeOverName = (nextHolderPreferredContact != null) ? nextHolderPreferredContact.getName() : "an officer";
                    	 logger.info("Next Holder Name: " + handTakeOverName + ", Serial Numbers: " + cardSerialNumbers);
                    	 
                    	 nextHolderEmailSubject = this.getTemplateForHandTakeOver(nextHolderEmailSubject, cardTypeDetail, cardBooking, cardSerialNumbers, handTakeOverName, recipientName, emailSignOff);
                    	 nextHolderEmailBody = this.getTemplateForHandTakeOver(nextHolderEmailBody, cardTypeDetail, cardBooking, cardSerialNumbers, handTakeOverName, recipientName, emailSignOff);
                    	 nextHolderSmsText  = this.getTemplateForHandTakeOver(nextHolderSmsText, cardTypeDetail, cardBooking, cardSerialNumbers, handTakeOverName, recipientName, emailSignOff);
                    	 
                    	 this.sendCancelCardBookingNotification(nextHolderPreferredContact, cardType, nextHolderEmailSubject, nextHolderEmailBody, nextHolderSmsText);
                    	 this.sendCardBookingNotification(nextHolderPreferredContact, senderEmail, senderEmailPassword, nextHolderEmailSubject, nextHolderEmailBody, nextHolderSmsText);
                    }
                 }
    		 }	 
             catch ( Exception e ) {
                 logger.log( Level.WARNING, "sendEmailForApprovedBookings failed.", e );
                 //Do not throw even if send notification fails
                 //throw new CorporateCardException( "sendEmailForApprovedBookings()", e );
             }
    }
	
    public void sendEmailForCancelledBooking (CardBooking cardBooking) {
    	if ( cardBooking == null ) {
            return;
        }

        String cardBookingId = ( cardBooking != null ) ? cardBooking.getId() : null;
        String cardTypeId = ( cardBooking != null ) ? cardBooking.getCardTypeId() : null;
        String nric = ( cardBooking != null ) ? cardBooking.getNric() : null;

        logger.info( String.format( "sendCancelCardBookingNotifications(). cardBookingId=%s, cardTypeId=%s, nric=%s", cardBookingId, cardTypeId, nric ) );
        Template cancelCardTemplate = templateDao.getTemplateByName( CANCEL_CARD_FOR_NEXT_PREV_HOLDER_TEMPLATE_NAME );
        
        try {
            this.populatePreferredContacts( cardBooking, true, true, true );
            this.populateOnBehalfUposPreferredContacts( cardBooking );

            CardTypeDetail cardTypeDetail = ( cardTypeId != null ) ? corporateCardDAO.getCardTypeDetail( cardTypeId ) : null;
            CardType cardType = ( cardTypeDetail != null ) ? cardTypeDetail.getType() : null;
            
        	GlobalParameters globalParameters = null;
            String senderEmail;
            String senderEmailPassword;
            String emailSignOff;
            
            if (cardType == CardType.PCWF) {
           	 globalParameters = globalParametersDao.getGlobalParametersByUnit(PCWF_CARD, null);
            } else {
           	 globalParameters = globalParametersDao.getGlobalParametersByUnit(cardTypeDetail.getDepartment(), null);
            }
            
            if (globalParameters != null) {
         	   senderEmail = globalParameters.getSenderEmail();
                senderEmailPassword = globalParameters.getSenderEmailPassword();
                emailSignOff = globalParameters.getEmailSignOff();
            } else {
         	 logger.info("globalParameter is null, using default setting from IMailSenderConfig ..");
         	 if (cardType == CardType.PCWF) {
         		 IMailSenderConfig config = ServiceConfig.getInstance().getMailSenderConfig( PCWF_CARD_MODULE_NAME );
             	 senderEmail = config.senderAddress();
             	 senderEmailPassword = config.senderPassword();
             	 emailSignOff = POLICE_WELFARE_DIVISION;
         	 } else {
         		 IMailSenderConfig config = ServiceConfig.getInstance().getMailSenderConfig( GROUP_CARD_MODULE_NAME );
             	 senderEmail = config.senderAddress();
             	 senderEmailPassword = config.senderPassword();
             	 emailSignOff = null;
         	 }
          }
            
            List <String> allocatedSerialNumbers = ( cardBooking != null) ? cardBooking.getAllocatedCardSerialNumbers() : new ArrayList<String>();
            
            PersonalPreferredContacts personalPreferredContact = ( cardBooking != null ) ? cardBooking.getPersonalPreferredContacts() : null;
            String cancelledPersonnelName = ( personalPreferredContact != null ) ? personalPreferredContact.getName() : null;
            List< CardPreviousHolderPreferredContacts > previousHolderPreferredContacts = ( cardBooking != null ) ? cardBooking.getPreviousHolderPreferredContacts() : null;
            List< CardNextHolderPreferredContacts > nextHolderPreferredContacts = ( cardBooking != null ) ? cardBooking.getNextHolderPreferredContacts() : null;
            
            String emailSubject = (cancelCardTemplate != null) ? cancelCardTemplate.getEmailSubject(): null;
            String emailBody = (cancelCardTemplate != null) ? cancelCardTemplate.getEmailBody() : null;
            String smsText = (cancelCardTemplate != null) ? cancelCardTemplate.getSmsText() : null;
            if (previousHolderPreferredContacts != null) {
            	logger.info("I am running 1");
            	for (CardPreviousHolderPreferredContacts prevHolderPreferredContact : previousHolderPreferredContacts) {
            		String prevHolderPersonnelName = (prevHolderPreferredContact != null) ? prevHolderPreferredContact.getName() : "-";
            		List<CardLessDetail> cardLessDetails = (prevHolderPreferredContact != null) ? prevHolderPreferredContact.getCardLessDetails() : null;
            		if (cardLessDetails != null) {
            			logger.info("I am running 1-1");
            			for (CardLessDetail cardLessDetail : cardLessDetails) {
            				//For every serial number for previous holder determine who to hand over to
            				String cardSerialNumber = cardLessDetail.getSerialNumber();
            				Date bookingEndDate = cardLessDetail.getBookingEndDate();
            				String handOverEmailSubject = emailSubject;
                        	String handOverEmailBody = emailBody;
                        	String handOverSmsText = smsText;
            				String[] handOverDetails = this.getHandOverToStringForCancel(cardTypeDetail, cardSerialNumber, bookingEndDate);
            				logger.info(String.format("Serial No: %s, Hand Over Details: %s",cardSerialNumber, handOverDetails.toString()));
            				handOverEmailSubject = this.getTemplateForCancel(handOverEmailSubject, cardTypeDetail, handOverDetails[1], cardSerialNumber, prevHolderPersonnelName, cancelledPersonnelName, handOverDetails[0], emailSignOff);
                        	handOverEmailBody = this.getTemplateForCancel(handOverEmailBody, cardTypeDetail, handOverDetails[1], cardSerialNumber, prevHolderPersonnelName, cancelledPersonnelName, handOverDetails[0], emailSignOff);
                        	handOverSmsText = this.getTemplateForCancel(handOverSmsText, cardTypeDetail, handOverDetails[1], cardSerialNumber, prevHolderPersonnelName, cancelledPersonnelName, handOverDetails[0], emailSignOff);
                        	
                        	//this.sendCancelCardBookingNotification(prevHolderPreferredContact, cardType, handOverEmailSubject, handOverEmailBody, handOverSmsText);
                        	this.sendCardBookingNotification(prevHolderPreferredContact, senderEmail, senderEmailPassword, handOverEmailSubject, handOverEmailBody, handOverSmsText);
            			}
            		}
            	}
            }
            if (nextHolderPreferredContacts != null) {
            	logger.info("I am running 2");
            	for (CardNextHolderPreferredContacts nextHolderPreferredContact : nextHolderPreferredContacts) {
            		String nextHolderPersonnelName = (nextHolderPreferredContact != null) ? nextHolderPreferredContact.getName() : "-";
            		List<CardLessDetail> cardLessDetails = (nextHolderPreferredContact != null) ? nextHolderPreferredContact.getCardLessDetails() : null;
            		if (cardLessDetails != null) {
            			logger.info("I am running 2-1");
            			for (CardLessDetail cardLessDetail : cardLessDetails) {
            				//For every serial number for next holder determine who to take over from
            				String cardSerialNumber = cardLessDetail.getSerialNumber();
            				Date bookingStartDate = cardLessDetail.getBookingStartDate();
            				String takeOverEmailSubject = emailSubject;
            				String takeOverEmailBody = emailBody;
            				String takeOverSmsText = smsText;
            				
            				String[] takeOverDetails = this.getTakeOverFromStringForCancel(cardTypeDetail, cardSerialNumber, bookingStartDate);
            				logger.info(String.format("Serial No: %s, Take Over Details: %s",cardSerialNumber, takeOverDetails.toString()));
            				takeOverEmailSubject = this.getTemplateForCancel(takeOverEmailSubject, cardTypeDetail, takeOverDetails[1], cardSerialNumber, nextHolderPersonnelName, cancelledPersonnelName, takeOverDetails[0], emailSignOff);
                        	takeOverEmailBody = this.getTemplateForCancel(takeOverEmailBody, cardTypeDetail, takeOverDetails[1], cardSerialNumber, nextHolderPersonnelName, cancelledPersonnelName, takeOverDetails[0], emailSignOff);
                        	takeOverSmsText = this.getTemplateForCancel(takeOverSmsText, cardTypeDetail, takeOverDetails[1], cardSerialNumber, nextHolderPersonnelName, cancelledPersonnelName, takeOverDetails[0], emailSignOff);
                        	
                        	//this.sendCancelCardBookingNotification(nextHolderPreferredContact, cardType, takeOverEmailSubject, takeOverEmailBody, takeOverSmsText);
                        	this.sendCardBookingNotification(nextHolderPreferredContact, senderEmail, senderEmailPassword, takeOverEmailSubject, takeOverEmailBody, takeOverSmsText);
            			}
            		}
            	}
            }
        }
        catch ( Exception e ) {
            logger.log( Level.WARNING, "sendCancelCardBookingNotifications() failed.", e );
        }
        logger.info( String.format( "sendCancelCardBookingNotifications() ended. cardBookingId=%s, cardTypeId=%s, nric=%s", cardBookingId, cardTypeId, nric ) );
    }
    
    private void populatePreferredContacts( CardBooking cardBooking, boolean populatePersonal, boolean populatePreviousHolders, boolean populateNextHolders ) {
        logger.info("populatePreferredContacts service");
        
    	if ( populatePersonal ) {
            String nric = cardBooking.getNric();
            if (nric != null){
            	PersonalDetail personalDetail = corporateCardDAO.getPersonal( nric );      	
            	if (personalDetail != null){
	            	PersonalPreferredContacts personalPreferredContacts = new PersonalPreferredContacts( personalDetail );
	                String department = this.deriveDepartmentDescription( personalDetail );
	
	                cardBooking.setDepartment( department );
	                cardBooking.setPersonalPreferredContacts( personalPreferredContacts );
	
	                // following are deprecated, should remove in future
	                // use personalPreferredContacts instead     
	                if (personalPreferredContacts != null){
	                	String name = personalPreferredContacts.getName();
		                ContactMode preferredContactMode = personalPreferredContacts.getPreferredContactMode();
		                String preferredMobile = personalPreferredContacts.getPreferredMobile();
		                String preferredEmail = personalPreferredContacts.getPreferredEmail();
		                
		                cardBooking.setName( name );
		                cardBooking.setPreferredContactMode( preferredContactMode );
		                cardBooking.setPreferredMobile( preferredMobile );
		                cardBooking.setPreferredEmail( preferredEmail );
		
		                logger.info( String.format( "== holder == " ) );
		                logger.info( String.format( "%s", personalPreferredContacts ) );	
	                }
            	}
            }
        }

        Map< String, CardPreviousHolderPreferredContacts > previousHoldersMap = new HashMap< String, CardPreviousHolderPreferredContacts >();
        Map< String, CardNextHolderPreferredContacts > nextHoldersMap = new HashMap< String, CardNextHolderPreferredContacts >();

        List< String > cardSerialNumbersToTakeoverFromCollectionCenter = new ArrayList< String >();
        List< String > cardSerialNumbersToHandoverToCollectionCenter = new ArrayList< String >();
        List< CardPreviousHolderPreferredContacts > previousHolders = new ArrayList< CardPreviousHolderPreferredContacts >();
        List< CardNextHolderPreferredContacts > nextHolders = new ArrayList< CardNextHolderPreferredContacts >();
        boolean isPublicHoliday = false;
        
        DateStartEnd visitStartEnd = cardBooking.getDateStartEnd();
        if ( visitStartEnd != null ) {
            Date visitStart = visitStartEnd.getStart();
            Date visitEnd = visitStartEnd.getEnd();
            if ( visitStart != null ) {
                if ( visitEnd != null ) {
                    List< String > serialNumbers = cardBooking.getAllocatedCardSerialNumbers();
                    if ( serialNumbers != null ) {
                        for ( String serialNumber : serialNumbers ) {
                        	//Removed for SAST Fixes 2021
                        	//logger.info("card serial number is: "+serialNumber);
                            if ( populatePreviousHolders ) {
                            	Calendar currentDate = Calendar.getInstance();
                                currentDate.setTime(visitEnd);
                            	int currentDay = currentDate.get(currentDate.DAY_OF_WEEK);
                            	
                            	//ICalendarService previousCalendarService = CalendarServiceFactory.getInstance();
                            	isPublicHoliday = true;
                            	Date dayBeforeVisitStart = new Date();
                            	dayBeforeVisitStart = visitStart;
                            	logger.info("Visit Start Date: "+visitStart);
                            	String previousNric = null;
                            	CardBooking previousApprovedCardBooking = null;
                            	while(isPublicHoliday == true && previousNric == null)
                            	{
                            		dayBeforeVisitStart = DateTimeUtil.getDateDaysAfter( dayBeforeVisitStart, -1 );
                            		logger.info("1 day before Visit Start Date is: "+dayBeforeVisitStart);
                            		isPublicHoliday = calendarDao.isPublicHoliday(dayBeforeVisitStart);
                            		//isPublicHoliday = isPublicHoliday(previousCalendarService, dayBeforeVisitStart);
                            		if(isPublicHoliday)
                                	{
                                		logger.info("1 day before visit state date is a public holiday, to check if handover/takeover is possible for the day before");
                                		
                                    	previousApprovedCardBooking = corporateCardDAO.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
                                    	previousNric = ( previousApprovedCardBooking == null ) ? null : previousApprovedCardBooking.getNric();
                                    	if(previousNric == null)
                                    	{
                                    		logger.info("Previous owner is not found");
                                    	}
                                    	else
                                    	{
                                    		logger.info("Previous owner is found");
                                    	}
                                		
                                	}
                            		else
                            		{
                            			logger.info("1 day before visit start date is not a public holiday");
                            			previousApprovedCardBooking = corporateCardDAO.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
                                    	previousNric = ( previousApprovedCardBooking == null ) ? null : previousApprovedCardBooking.getNric();
                                    	if(previousNric == null)
                                    	{
                                    		logger.info("Previous owner is not found");
                                    	}
                                    	else
                                    	{
                                    		logger.info("Previous owner is found");
                                    	}
                            		}
                            	}
                            	Calendar dayBeforeVisitDate = Calendar.getInstance();
                            	dayBeforeVisitDate.setTime(dayBeforeVisitStart);
                            	int dayBeforeVisitDay = dayBeforeVisitDate.get(dayBeforeVisitDate.DAY_OF_WEEK);
                            	if(previousNric == null && dayBeforeVisitDay == Calendar.SUNDAY)
                            	{
                            		logger.info("1 day before visit start date is a Sunday");
                            		dayBeforeVisitStart = DateTimeUtil.getDateDaysAfter( dayBeforeVisitStart, -1 );
                            		previousApprovedCardBooking = corporateCardDAO.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
                                	previousNric = ( previousApprovedCardBooking == null ) ? null : previousApprovedCardBooking.getNric();
                                	if(previousNric == null)
                                	{
                                		dayBeforeVisitStart = DateTimeUtil.getDateDaysAfter( dayBeforeVisitStart, -1 );
                                    	previousApprovedCardBooking = corporateCardDAO.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
                                    	previousNric = ( previousApprovedCardBooking == null ) ? null : previousApprovedCardBooking.getNric();	
                                	}	
                            	}
                            	if(previousNric == null && dayBeforeVisitDay == Calendar.SATURDAY)
                            	{
                            		logger.info("1 day before visit start date is a Saturday");
                            		dayBeforeVisitStart = DateTimeUtil.getDateDaysAfter( dayBeforeVisitStart, -1 );
                            		previousApprovedCardBooking = corporateCardDAO.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
                                	previousNric = ( previousApprovedCardBooking == null ) ? null : previousApprovedCardBooking.getNric();

                            	}

                            	isPublicHoliday = true;
                            	dayBeforeVisitStart = DateTimeUtil.getDateDaysAfter( dayBeforeVisitStart,0);
                            	logger.info("current date to check: "+dayBeforeVisitStart);
                                while(isPublicHoliday == true && previousNric == null)
                            	{
                            		isPublicHoliday = calendarDao.isPublicHoliday(dayBeforeVisitStart);
                            		//isPublicHoliday = isPublicHoliday(previousCalendarService, dayBeforeVisitStart);
                            		if(isPublicHoliday)
                                	{
                                		logger.info("previous date is a public holiday, cannot directly add to collection point, to check handover/takeover");
                                		
                                    	previousApprovedCardBooking = corporateCardDAO.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
                                    	previousNric = ( previousApprovedCardBooking == null ) ? null : previousApprovedCardBooking.getNric();
                                    	if(previousNric == null)
                                    	{
                                    	dayBeforeVisitStart = DateTimeUtil.getDateDaysAfter( dayBeforeVisitStart,-1);
                                    	}
                                		
                                	}
                            		else
                            		{
                            			logger.info("current date is not a public holiday, can add to collection point");
                            			previousApprovedCardBooking = corporateCardDAO.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
                                    	previousNric = ( previousApprovedCardBooking == null ) ? null : previousApprovedCardBooking.getNric();
                            		}
                            		
                            	}
                                CardPreviousHolderPreferredContacts previousHolder = ( previousNric == null ) ? null : previousHoldersMap.get( previousNric );
                                if ( previousNric != null ) {
                                    if ( previousHolder == null ) {
                                        // not in previousHoldersMap
                                        // fetch from database
                                    	logger.info("before get personal");
                                        PersonalDetail previousPersonalDetail = corporateCardDAO.getPersonal( previousNric );
                                        if ( previousPersonalDetail != null ) {
                                            previousHolder = new CardPreviousHolderPreferredContacts( previousPersonalDetail );
                                            previousHoldersMap.put( previousNric, previousHolder );
                                            previousHolders.add( previousHolder );
                                        }
                                    }
                                }
                                logger.info("before previous holder 1913");
                                if ( previousHolder != null ) {
                                	logger.info("inside previous holder 1915, there is a previous owner");
                                    CardLessDetail cardLessDetail = ( previousApprovedCardBooking == null ) ? null : new CardLessDetail( serialNumber, previousApprovedCardBooking );
                                    if ( cardLessDetail != null ) {
                                        previousHolder.getCardLessDetails().add( cardLessDetail );
                                        logger.info("Previous holder: "+previousHolder);
                                    }
                                }
                                else {
                                	logger.info("add to collection point");

                                		cardSerialNumbersToTakeoverFromCollectionCenter.add( serialNumber );
                                	
                                    //if today is public holidy, check the day before to see if its still public holiday.
                                    //if public holiday, check -1, if -1 no people, then continue collection point,
                                    //if have people, directly do handover/takeover?
                                }

                            }
                            //lapkan: change populateLastHolders to populateNextHolders, believed to be a typo error
                            if ( populateNextHolders ) {
                            	logger.info("populate next holders");
                                Calendar todayDate = Calendar.getInstance();
                                todayDate.setTime(visitEnd);
                                CardBooking nextApprovedCardBooking = new CardBooking();
                                logger.info("determine next holder");
                                nextApprovedCardBooking = determineNextHolder (visitEnd, true, serialNumber);
                                String nextNric = ( nextApprovedCardBooking == null ) ? null : nextApprovedCardBooking.getNric();
                                logger.info("next nric is: "+nextNric);
                                CardNextHolderPreferredContacts nextHolder = ( nextNric == null ) ? null : nextHoldersMap.get( nextNric );
                                if ( nextNric != null ) {
                                    if ( nextHolder == null ) {
                                        // not in nextHoldersMap
                                        // fetch from database
                                    	logger.info("next holder is not empty");
                                        PersonalDetail nextPersonalDetail = corporateCardDAO.getPersonal( nextNric );
                                        if ( nextPersonalDetail != null ) {
                                            nextHolder = new CardNextHolderPreferredContacts( nextPersonalDetail );
                                            nextHoldersMap.put( nextNric, nextHolder );
                                            nextHolders.add( nextHolder );
                                        }
                                    }
                                }
                                if ( nextHolder != null ) {
                                    CardLessDetail cardLessDetail = ( nextApprovedCardBooking == null ) ? null : new CardLessDetail( serialNumber, nextApprovedCardBooking );
                                    if ( cardLessDetail != null ) {
                                    	logger.info("add in cardlessdetails");
                                        nextHolder.getCardLessDetails().add( cardLessDetail );
                                        logger.info("Next holder: "+nextHolder);
                                    }
                                }
                                else {
                                	
                                	logger.info("add to collection point");
                                    cardSerialNumbersToHandoverToCollectionCenter.add( serialNumber );
                                    
                                }
                            }
                        }
                    }
                }
            }
        }

        if ( populatePreviousHolders ) {
            logger.info( String.format( "== previous holders, count=%s == ", previousHolders.size() ) );
            for ( CardPreviousHolderPreferredContacts previousHolder : previousHolders ) {
                logger.info( previousHolder.toString() );
            }

            cardBooking.setPreviousHolderPreferredContacts( previousHolders );
            cardBooking.setCardSerialNumbersToTakeoverFromCollectionCenter( cardSerialNumbersToTakeoverFromCollectionCenter );
        }
        if ( populateNextHolders ) {
            logger.info( String.format( "== next holders, count=%s == ", nextHolders.size() ) );
            for ( CardNextHolderPreferredContacts nextHolder : nextHolders ) {
                logger.info( nextHolder.toString() );
            }

            cardBooking.setNextHolderPreferredContacts( nextHolders );
            cardBooking.setCardSerialNumbersToHandoverToCollectionCenter( cardSerialNumbersToHandoverToCollectionCenter );
        }
    }
    
    private String deriveDepartmentDescription( PersonalDetail detail ) {
        String departmentDescription = null;
        if ( detail != null ) {
            Employment employment = detail.getEmployment();
            if ( employment != null ) {
                String departmentId = employment.getOrganisationOrDepartment();
                if ( departmentId != null ) {
                    Code departmentCode = codeDao.getCode( CodeType.UNIT_DEPARTMENT, departmentId );
                    if ( departmentCode != null ) {
                        departmentDescription = departmentCode.getDescription();
                    }
                }
            }
        }
        return departmentDescription;
    }
    
    private CardBooking determineNextHolder(Date visitEnd, Boolean populateNextHolders, String serialNumber)
    {
    	Calendar todayDate = Calendar.getInstance();
    	todayDate.setTime(visitEnd);
    	int day = todayDate.get(todayDate.DAY_OF_WEEK);
    	logger.info("today date is: "+day);
    	CardBooking nextApprovedCardBooking = new CardBooking();
    	//ICalendarService nextCalendarService = CalendarServiceFactory.getInstance();
    	Date dayAfterVisitEnd = null;
    	dayAfterVisitEnd = visitEnd;
    	logger.info("dayAfterVisitEnd before getting into loop: "+ dayAfterVisitEnd);
    	String nextNric = null;
    	boolean isPublicHoliday = true;
    	while(isPublicHoliday == true && nextNric == null)
    	{
    		dayAfterVisitEnd = DateTimeUtil.getDateDaysAfter( dayAfterVisitEnd, 1 );
    		logger.info("dayAfterVisitEnd inside loop: "+ dayAfterVisitEnd);
    		isPublicHoliday = calendarDao.isPublicHoliday(dayAfterVisitEnd);
    		//isPublicHoliday = isPublicHoliday(nextCalendarService, dayAfterVisitEnd);
    		if(isPublicHoliday)
        	{
        		logger.info("next date is a public holiday, to check if handover/takeover is possible for the day after");
        		
            	nextApprovedCardBooking = corporateCardDAO.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber );
            	nextNric = ( nextApprovedCardBooking == null ) ? null :nextApprovedCardBooking.getNric();
        		
        	}
    		else
    		{
    			logger.info("next date is not a public holiday");
    			nextApprovedCardBooking = corporateCardDAO.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber );
            	nextNric = ( nextApprovedCardBooking == null ) ? null :nextApprovedCardBooking.getNric();
    		}
    	}
    	logger.info("dayAfterVisitEnd"+ dayAfterVisitEnd);
    	Calendar dayAfterVisitDate = Calendar.getInstance();
    	dayAfterVisitDate.setTime(dayAfterVisitEnd);
    	int dayAfterVisitDay = dayAfterVisitDate.get(dayAfterVisitDate.DAY_OF_WEEK);
    	if(nextNric == null && dayAfterVisitDay == Calendar.SATURDAY)
    	{
    		dayAfterVisitEnd = DateTimeUtil.getDateDaysAfter( dayAfterVisitEnd, 1 );
    		nextApprovedCardBooking = corporateCardDAO.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber );
        	nextNric = ( nextApprovedCardBooking == null ) ? null : nextApprovedCardBooking.getNric();
        	if(nextNric == null)
        	{
        		dayAfterVisitEnd = DateTimeUtil.getDateDaysAfter( dayAfterVisitEnd, 1 );
            	nextApprovedCardBooking = corporateCardDAO.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber );
            	nextNric = ( nextApprovedCardBooking == null ) ? null : nextApprovedCardBooking.getNric();	
        	}	
    	}
    	if(nextNric == null && dayAfterVisitDay == Calendar.SUNDAY)
    	{
    		dayAfterVisitEnd = DateTimeUtil.getDateDaysAfter( dayAfterVisitEnd, 1 );
    		nextApprovedCardBooking = corporateCardDAO.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber );
    		nextNric = ( nextApprovedCardBooking == null ) ? null : nextApprovedCardBooking.getNric();	

    	}
    	isPublicHoliday = true;
    	dayAfterVisitEnd = DateTimeUtil.getDateDaysAfter( dayAfterVisitEnd,0);
        while(isPublicHoliday == true && nextNric == null)
    	{
        	isPublicHoliday = calendarDao.isPublicHoliday(dayAfterVisitEnd);
    		//isPublicHoliday = isPublicHoliday(nextCalendarService, dayAfterVisitEnd);
    		if(isPublicHoliday)
        	{
        		logger.info("next date is a public holiday, to check if handover/takeover is possible for the day after");
        		
            	nextApprovedCardBooking = corporateCardDAO.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber );
            	nextNric = ( nextApprovedCardBooking == null ) ? null : nextApprovedCardBooking.getNric();
            	dayAfterVisitEnd = DateTimeUtil.getDateDaysAfter( dayAfterVisitEnd,1);
        		
        	}
    		else
    		{
    			logger.info("previous date is not a public holiday");
    			nextApprovedCardBooking = corporateCardDAO.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber );
    		}
    		
    	}

        return nextApprovedCardBooking;
    }
	
    public String getTemplateForHandTakeOver (String templateString, CardTypeDetail cardTypeDetail, CardBooking cardBooking, 
    		List <String> cardSerialNumberList, String recipientName, String handTakeOverName, String signOff) {
    	
    	String resultString = templateString;
    	String cardName = ( cardTypeDetail != null ) ? cardTypeDetail.getName() : "-";
    	DateStartEnd dateStartEnd = cardBooking.getDateStartEnd();
    	String visitStartDate = ( dateStartEnd != null ) ? ConvertUtil.convertDateToDateString(dateStartEnd.getStart()): "-";
    	String visitEndDate = ( dateStartEnd != null ) ? ConvertUtil.convertDateToDateString(dateStartEnd.getEnd()) : "-";
    	
    	StringBuilder cardSerialNumbers = new StringBuilder();
    	if (cardSerialNumberList != null) {
    		for (String cardSerialNumber: cardSerialNumberList) {
    			if (cardSerialNumbers.length() > 0) {
    				cardSerialNumbers.append(", ");
    			}
    			cardSerialNumbers.append(cardSerialNumber);
    		}
    	}
    	resultString = resultString.replaceAll("\\$Officer\\$", recipientName);
    	resultString = resultString.replaceAll("\\$passName\\$", cardName);
    	resultString = resultString.replaceAll("\\$passSerialNo\\$", cardSerialNumbers.toString());
    	resultString = resultString.replaceAll("\\$visitStartDate\\$", visitStartDate);
    	resultString = resultString.replaceAll("\\$visitEndDate\\$", visitEndDate);
      	if (handTakeOverName != null) {
      		resultString = resultString.replaceAll("\\$name\\$", handTakeOverName);
      	}
      	if (signOff != null) {
      		if (signOff.length() > 0) {
      			resultString = resultString.replaceAll("\\$BestRegards\\$", "Best Regards,");
          		resultString = resultString.replaceAll("\\$signOff\\$", this.formatSignOff(signOff));
      		} else {
      			resultString = resultString.replaceAll("\\$BestRegards\\$", "Best Regards.");
      			resultString = resultString.replaceAll("\\$signOff\\$", "");
      		}
      	} else {
      		resultString = resultString.replaceAll("\\$BestRegards\\$", "Best Regards.");
      		resultString = resultString.replaceAll("\\$signOff\\$", "");
      	}
      	//Removed for SAST Fixes 2021
    	//logger.info("Result String: " + resultString);
    	return resultString;
    }
    
	private String formatSignOff (String signOff) {
		String[] lines = signOff.split("\\r?\\n");
		
		StringBuilder builder = new StringBuilder (lines[0]);
		for (int i = 1; i < lines.length; i++) {
			builder.append("<BR>");
			builder.append(lines[i]);
		}
		
		return builder.toString();
	}
	
	private void sendCardBookingNotification( PersonalPreferredContacts contact, String senderAddress, String senderPassword, String emailSubject, String emailBody, String smsText ) {
		logger.info("sendCardBookingNotification() called.");
		boolean success = false;
        String response = null;

        if ( contact == null ) {
        	logger.info("contact is null");
            return;
        }
        
        if ( senderAddress == null ) {
        	logger.info("sender address is null");
        	return;
        }

        String nric = contact.getNric();
        logger.info( String.format( "sendCardBookingNotification() started. nric=%s", nric ) );
        
        if (nric != null) {
        	if (smsText != null) {
        		String preferredMobile = contact.getPreferredMobile();
        		if (preferredMobile != null) {
        			ArrayList<String> smsRecipients = new ArrayList<String>();
        			smsRecipients.add(preferredMobile);
        			success = this.sendSMS(smsRecipients, smsText);
        			response = "sms.";
        		} else {
        			response = "preferredMobile is null.";
        		}
        	} else {
        		response = "smsText is null.";
        	}
        	logger.info( String.format( "Send SMS ended. nric=%s, success=%s, response=%s", nric, success, response ) );
        	
        	if (emailSubject != null) {
        		if (emailBody != null) {
        			String preferredEmail = contact.getPreferredEmail();
        			if (preferredEmail != null) {
        				ArrayList<String> emailRecipients = new ArrayList<String>();
        				emailRecipients.add(preferredEmail);
        				success = this.sendEmail(senderAddress, senderPassword, emailRecipients, emailSubject, emailBody);
        			} else {
        				response = "preferredEmail is null.";
        			}
        		} else {
        			response = "emailBody is null.";
        		}
        	} else {
        		response = "emailSubject is null.";
        	} 
        	logger.info( String.format( "Send Email ended. nric=%s, success=%s, response=%s", nric, success, response ) );
        } 
        logger.info( "sendCardBookingNotification() ended." );
    }
	
    private boolean sendCancelCardBookingNotification( PersonalPreferredContacts contact, CardType cardType, String emailSubject, String emailBody, String smsText ) {
        boolean success = false;
        String response = null;

        if ( contact == null ) {
            return false;
        }

        String nric = contact.getNric();
        logger.info( String.format( "sendCancelCardBookingNotification() started. nric=%s", nric ) );

        if ( nric != null ) {
            ContactMode preferredContactMode = contact.getPreferredContactMode();
            if ( preferredContactMode == ContactMode.SMS ) {
                if ( smsText != null ) {
                    String preferredMobile = contact.getPreferredMobile();
                    if ( preferredMobile != null ) {
                        ArrayList< String > recipients = new ArrayList< String >();
                        recipients.add( preferredMobile );
                        success = this.sendSMS( recipients, smsText );
                        response = "sms.";
                    }
                    else {
                        response = "preferredMobile is null.";
                    }
                }
                else {
                    response = "smsText is null.";
                }
            }
            else if ( preferredContactMode == ContactMode.EMAIL ) {
                if ( emailSubject != null ) {
                    if ( emailBody != null ) {
                        String preferredEmail = contact.getPreferredEmail();
                        if ( preferredEmail != null ) {
                            ArrayList< String > recipients = new ArrayList< String >();
                            recipients.add( preferredEmail );
                            if ( cardType == CardType.PCWF ) {
                                success = this.sendEmail( PCWF_CARD_MODULE_NAME, recipients, emailSubject, emailBody );
                                response = "email.";
                            }
                            else if ( cardType == CardType.GROUP ) {
                                success = this.sendEmail( GROUP_CARD_MODULE_NAME, recipients, emailSubject, emailBody );
                                response = "email.";
                            }
                            else {
                                response = String.format( "unknown cardType %s.", cardType );
                            }
                        }
                    }
                    else {
                        response = "emailBody is null.";
                    }
                }
                else {
                    response = "emailSubject is null.";
                }
            }
            else {
                response = String.format( "unknown preferredContactMode %s.", preferredContactMode );
            }
        }
        else {
            response = "nric is null.";
        }
        logger.info("success:"+success);
        logger.info( String.format( "sendCancelCardBookingNotification() ended. nric=%s, success=%s, response=%s", nric, success, response ) );
        return success;
    }
	
	private boolean sendEmail( String senderAddress, String senderPassword, List< String > recipients, String subject, String body ) {
		logger.info("sendEmail is called");
        BatchElectronicMail mail = new BatchElectronicMail();
        mail.setHtmlContent( true );
        mail.setSubject( subject );
        mail.setUserAddress( senderAddress );
        mail.setToRecipients( recipients );
        mail.setText( body );
        mail.setUserPassword( senderPassword );

        INotificationService notificationService = NotificationServiceFactory.getInstance();
        try {
            notificationService.send( mail );
            return true;
        }
        catch ( NotificationServiceException e ) {
            logger.log( Level.SEVERE, "Fail to send email to " + recipients, e );
            return false;
        }
    }
	
    private boolean sendSMS( List< String > recipients, String text ) {
        //BatchShortMessage message = new BatchShortMessage( recipients, text );
        INotificationService notificationService = NotificationServiceFactory.getInstance();

        try {
            //notificationService.send( message );
            notificationService.sendExtSMS(recipients, text);
            return true;
        }
        catch ( NotificationServiceException e ) {
            logger.log( Level.SEVERE, "Fail to send SMS to " + recipients, e );
            return false;
        }
    }
    
    private boolean sendEmail( String module, List< String > recipients, String subject, String body ) {
        IMailSenderConfig config = ServiceConfig.getInstance().getMailSenderConfig( module );

        BatchElectronicMail mail = new BatchElectronicMail();
        mail.setHtmlContent( true );
        mail.setSubject( subject );
        mail.setUserAddress( config.senderAddress() );
        mail.setToRecipients( recipients );
        mail.setText( body );
        mail.setUserAddress( config.senderAddress() );

        String systemPassword = config.senderPassword();

        String encryptionKey = EnvironmentUtils.getEncryptionKey();
        if ( encryptionKey != null && !encryptionKey.isEmpty() ) {
            try {
                Encipher encipher = new Encipher( encryptionKey );
                systemPassword = encipher.decrypt( systemPassword );
            }
            catch ( Exception e ) {
                logger.log( Level.SEVERE, "Error while decrypting the configured system email password.", e );
            }
        }
   
        mail.setUserPassword( systemPassword );

        INotificationService notificationService = NotificationServiceFactory.getInstance();
        try {
            notificationService.send( mail );
            return true;
        }
        catch ( NotificationServiceException e ) {
            logger.log( Level.SEVERE, "Fail to send email to " + recipients, e );
            return false;
        }
    }
    
    private void populateOnBehalfUposPreferredContacts( CardBooking cardBooking ) {
        List< PersonalPreferredContacts > uposPreferredContacts = null;
        String nric = cardBooking.getNric();
        String submitter = cardBooking.getSubmitter();
        if ( !Objects.equals( nric, submitter ) ) {
            // booking is applied on behalf

            // notification is sent to the user who apply on behalf
            List< String > upoUsers = new ArrayList< String >();
            if ( submitter != null ) {
                upoUsers.add( submitter );
            }

            if ( upoUsers != null ) {
                if ( upoUsers.size() > 0 ) {
                    uposPreferredContacts = personnelDao.getActivePersonnelPreferredContacts( null, null, upoUsers );
                }
            }
        }
        if ( uposPreferredContacts == null ) {
            uposPreferredContacts = new ArrayList< PersonalPreferredContacts >();
        }
        cardBooking.setOnBehalfUposPreferredContacts( uposPreferredContacts );
    }
    
    private String[] getTakeOverFromStringForCancel( CardTypeDetail cardTypeDetail, String serialNumber, Date visitStart ) {
        String takeOverFrom = "-";
        String takeOverFromDates = "-";
        
        Calendar visitStartDate = Calendar.getInstance();
        visitStartDate.setTime(visitStart);
        int visitStartDay = 0;
        visitStartDay = visitStartDate.get(visitStartDate.DAY_OF_WEEK);
        boolean isPublicHoliday = true;
        //ICalendarService previousCalendarService = CalendarServiceFactory.getInstance();
        CardBooking cardBooking = null;
        Date dayBeforeVisitStart = visitStart;
        while(isPublicHoliday == true && cardBooking == null)
    	{
        	dayBeforeVisitStart = DateTimeUtil.getDateDaysAfter( dayBeforeVisitStart, -1 );
        	isPublicHoliday = calendarDao.isPublicHoliday(dayBeforeVisitStart);
    		//isPublicHoliday = isPublicHoliday(previousCalendarService, dayBeforeVisitStart);
    		if(isPublicHoliday)
        	{
        		logger.info("previous date is a public holiday, to check if handover/takeover is possible for the day before");
        		
        		cardBooking = corporateCardDAO.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
        		
        	}
    		else
    		{
    			logger.info("previous date is not a public holiday");
    			cardBooking = corporateCardDAO.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
    		}
    	}
        Calendar dayBeforeVisitDate = Calendar.getInstance();
    	dayBeforeVisitDate.setTime(dayBeforeVisitStart);
    	int dayBeforeVisitDay = dayBeforeVisitDate.get(dayBeforeVisitDate.DAY_OF_WEEK);
    	if(cardBooking == null && dayBeforeVisitDay == Calendar.SUNDAY)
    	{
    		dayBeforeVisitStart = DateTimeUtil.getDateDaysAfter( dayBeforeVisitStart, -1 );
    		cardBooking = corporateCardDAO.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
        	if(cardBooking == null)
        	{
        		dayBeforeVisitStart = DateTimeUtil.getDateDaysAfter( dayBeforeVisitStart, -1 );
        		cardBooking = corporateCardDAO.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
        	}	
    	}
    	if(cardBooking == null && dayBeforeVisitDay == Calendar.SATURDAY)
    	{
    		dayBeforeVisitStart = DateTimeUtil.getDateDaysAfter( dayBeforeVisitStart, -1 );
    		cardBooking = corporateCardDAO.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
    	}
    	isPublicHoliday = true;
    	dayBeforeVisitStart = DateTimeUtil.getDateDaysAfter( dayBeforeVisitStart,0);
        while(isPublicHoliday == true && cardBooking == null)
    	{
    		isPublicHoliday = calendarDao.isPublicHoliday(dayBeforeVisitStart);
    		//isPublicHoliday = isPublicHoliday(previousCalendarService, dayBeforeVisitStart);
    		if(isPublicHoliday)
        	{
        		logger.info("previous date is a public holiday, to check if handover/takeover is possible for the day before");
        		
        		cardBooking = corporateCardDAO.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
        		if(cardBooking == null)
        		{
        		dayBeforeVisitStart = DateTimeUtil.getDateDaysAfter( dayBeforeVisitStart,-1);
        		}
        	}
    		else
    		{
    			logger.info("previous date is not a public holiday");
    			cardBooking = corporateCardDAO.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
    		}
    	}

        if ( cardBooking != null ) {
            String nric = cardBooking.getNric();
            PersonalDetail personalDetail = null;
            if ( nric != null ) {
                personalDetail = corporateCardDAO.getPersonal( nric );
            }
            if ( personalDetail != null ) {
                String name = personalDetail.getName();
                takeOverFrom = "take over the pass(es) from " + name;
                takeOverFromDates = ConvertUtil.convertDateToDateString( visitStart );
                
            }
            else {
                logger.info( String.format( "takeOverFrom, nric=%s personal details not found.", Util.replaceNewLine( nric ) ) );
            }
        }
        else if ( cardTypeDetail != null ) {
            if ( dayBeforeVisitStart != null ) {
            	logger.info("to add to collection detail, dayBeforeVisitStart is: " + dayBeforeVisitStart);
                CardCollectionDetail cardCollectionDetail = this.getCardCollectionDetail( cardTypeDetail, dayBeforeVisitStart );
                if ( cardCollectionDetail != null ) {
                    takeOverFrom = "collect the pass(es) from " + cardCollectionDetail.getLocation();
                    takeOverFromDates = ConvertUtil.convertDateToDateString( visitStart );
                }
                else {
                    logger.info( String.format( "takeOverFrom, card collection details not found. cardTypeId=%s", Util.replaceNewLine( cardTypeDetail.getId() ) ) );
                }
            }
        }

        return new String[] { takeOverFrom, takeOverFromDates };
    }
    
    private CardCollectionDetail getCardCollectionDetail( CardTypeDetail cardTypeDetail, Date date ) {
        CardCollectionDetail cardCollectionDetail = null;
        if ( cardTypeDetail != null ) {
            if ( date != null ) {
                String daysDescription = CardCollectionDaysDescriptionConstants.WEEK_DAYS;
                if ( this.isPublicHoliday( date ) ) {
                    daysDescription = CardCollectionDaysDescriptionConstants.PUBLIC_HOLIDAYS;
                }
                else if ( DateTimeUtil.isWeekEnd( date ) ) {
                    daysDescription = CardCollectionDaysDescriptionConstants.WEEK_ENDS;
                }

                List< CardCollectionDetail > cardCollectionDetails = cardTypeDetail.getCollectionDetails();
                if ( cardCollectionDetails != null ) {
                    for ( CardCollectionDetail tmpCardCollectionDetail : cardCollectionDetails ) {
                        String tmpDaysDescription = tmpCardCollectionDetail.getDaysDescription();
                        if ( tmpDaysDescription != null ) {
                            if ( tmpDaysDescription.equals( daysDescription ) ) {
                                cardCollectionDetail = tmpCardCollectionDetail;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return cardCollectionDetail;
    }
    
    public boolean isPublicHoliday( Date date ) {
        boolean isPublicHoliday = false;
        List< PublicHoliday > publicHolidays = null;
        if ( date != null ) {
            publicHolidays = calendarDao.getPublicHolidays( date );
        }
        if ( publicHolidays != null ) {
            if ( publicHolidays.size() > 0 ) {
                isPublicHoliday = true;
            }
        }
        return isPublicHoliday;
    }
    
    private String[] getHandOverToStringForCancel( CardTypeDetail cardTypeDetail, String serialNumber, Date visitEnd ) {
        String handOverTo = "-";
        String handOverToDates = "-";
        
        Calendar visitEndDate = Calendar.getInstance();
        visitEndDate.setTime(visitEnd);
        int visitEndDay = 0;
        visitEndDay = visitEndDate.get(visitEndDate.DAY_OF_WEEK);

        Date dayAfterVisitEnd = visitEnd;
        CardBooking cardBooking = null;
        
        boolean isPublicHoliday = true;
        //ICalendarService nextCalendarService = CalendarServiceFactory.getInstance();
     
    	while(isPublicHoliday == true && cardBooking == null)
    	{
    		dayAfterVisitEnd = DateTimeUtil.getDateDaysAfter( dayAfterVisitEnd, 1 );
    		isPublicHoliday = calendarDao.isPublicHoliday(dayAfterVisitEnd);
    		//isPublicHoliday = isPublicHoliday(nextCalendarService, dayAfterVisitEnd);
    		if(isPublicHoliday)
        	{
        		logger.info("next date is a public holiday, to check if handover/takeover is possible for the day after");
        		
            	cardBooking = corporateCardDAO.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber );
        		
        	}
    		else
    		{
    			logger.info("next date is not a public holiday");
    			cardBooking = corporateCardDAO.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber );
    		}
    	}
    	Calendar dayAfterVisitDate = Calendar.getInstance();
    	dayAfterVisitDate.setTime(dayAfterVisitEnd);
    	int dayAfterVisitDay = dayAfterVisitDate.get(dayAfterVisitDate.DAY_OF_WEEK);
    	if(cardBooking == null && dayAfterVisitDay == Calendar.SATURDAY)
    	{
    		dayAfterVisitEnd = DateTimeUtil.getDateDaysAfter( dayAfterVisitEnd, 1 );
    		cardBooking = corporateCardDAO.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber );
        	if(cardBooking == null)
        	{
        		dayAfterVisitEnd = DateTimeUtil.getDateDaysAfter( dayAfterVisitEnd, 1 );
        		cardBooking = corporateCardDAO.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber );
        	}	
    	}
    	if(cardBooking == null && dayAfterVisitDay == Calendar.SUNDAY)
    	{
    		dayAfterVisitEnd = DateTimeUtil.getDateDaysAfter( dayAfterVisitEnd, 1 );
    		cardBooking = corporateCardDAO.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber );

    	}
    	isPublicHoliday = true;
    	dayAfterVisitEnd = DateTimeUtil.getDateDaysAfter( dayAfterVisitEnd,0);
        while(isPublicHoliday == true && cardBooking == null)
    	{
    		
    		//isPublicHoliday = isPublicHoliday(nextCalendarService, dayAfterVisitEnd);
    		isPublicHoliday = calendarDao.isPublicHoliday(dayAfterVisitEnd);
    		if(isPublicHoliday)
        	{
        		logger.info("next date is a public holiday, to check if handover/takeover is possible for the day after");
        		
        		cardBooking = corporateCardDAO.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber ); 
        		if(cardBooking == null)
        		{
        		dayAfterVisitEnd = DateTimeUtil.getDateDaysAfter( dayAfterVisitEnd,1);
        		}
        	}
    		else
    		{
    			logger.info("after date is not a public holiday");
    			cardBooking = corporateCardDAO.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber );
    		}
    		
    	}
        if ( cardBooking != null ) {
            String nric = cardBooking.getNric();
            PersonalDetail personalDetail = null;
            if ( nric != null ) {
                personalDetail = corporateCardDAO.getPersonal( nric );
            }
            if ( personalDetail != null ) {
                String name = personalDetail.getName();
                handOverTo = "hand over the pass(es) to " + name;
                handOverToDates = ConvertUtil.convertDateToDateString( dayAfterVisitEnd );
            }
            else {
                logger.info( String.format( "handOverTo, nric=%s personal details not found.", Util.replaceNewLine( nric ) ) );
            }
        }
        else if ( cardTypeDetail != null ) {
            if ( dayAfterVisitEnd != null ) {
                CardCollectionDetail cardCollectionDetail = this.getCardCollectionDetail( cardTypeDetail, dayAfterVisitEnd );
                if ( cardCollectionDetail != null ) {
                    handOverTo = "return the pass(es) to " + cardCollectionDetail.getLocation();
                    handOverToDates = ConvertUtil.convertDateToDateString( dayAfterVisitEnd );
                }
                else {
                    logger.info( String.format( "handOverTo, card collection details not found. cardTypeId=%s", Util.replaceNewLine( cardTypeDetail.getId() ) ) );
                }
            }
        }

        return new String[] { handOverTo, handOverToDates };
    }
    
    public String getTemplateForCancel (String templateString, CardTypeDetail cardTypeDetail, String bookingEndDate, 
    		String cardSerialNumber, String recipientName, String cancelledName, String handTakeOverString, String signOff) {
    	
    	String resultString = templateString;
    	String cardName = ( cardTypeDetail != null ) ? cardTypeDetail.getName() : "-";
    	
    	resultString = resultString.replaceAll("\\$Officer\\$", recipientName);
    	resultString = resultString.replaceAll("\\$passName\\$", cardName);
    	resultString = resultString.replaceAll("\\$passSerialNo\\$", cardSerialNumber);
    	resultString = resultString.replaceAll("\\$visitEndDate\\$", bookingEndDate);
    	resultString = resultString.replaceAll("\\$collectReturnHandTakeOver\\$", handTakeOverString);
    	resultString = resultString.replaceAll("\\$OfficerA\\$", cancelledName);
    	
      	if (signOff != null) {
      		if (signOff.length() > 0) {
      			resultString = resultString.replaceAll("\\$BestRegards\\$", "Best Regards,");
          		resultString = resultString.replaceAll("\\$signOff\\$", this.formatSignOff(signOff));
      		} else {
      			resultString = resultString.replaceAll("\\$BestRegards\\$", "Best Regards.");
      			resultString = resultString.replaceAll("\\$signOff\\$", "");
      		}
      	} else {
      		resultString = resultString.replaceAll("\\$BestRegards\\$", "Best Regards.");
      		resultString = resultString.replaceAll("\\$signOff\\$", "");
      	}
      
    	logger.info("Result String: " + resultString);
    	return resultString;
    }
    

}
