package com.stee.spfcore.webapi.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stee.spfcore.webapi.dao.PersonalDetailDAO;
import com.stee.spfcore.webapi.dao.SAGApplicationDAO;
import com.stee.spfcore.webapi.dao.TemplateDAO;
import com.stee.spfcore.webapi.model.accounting.BatchFileConfig;
import com.stee.spfcore.webapi.model.sag.SAGAnnouncementConfig;
import com.stee.spfcore.webapi.model.sag.SAGApplication;
import com.stee.spfcore.webapi.model.sag.SAGAwardQuantum;
import com.stee.spfcore.webapi.model.sag.SAGBatchFileRecord;
import com.stee.spfcore.webapi.model.sag.SAGConfigSetup;
import com.stee.spfcore.webapi.model.sag.SAGDateConfigType;
import com.stee.spfcore.webapi.model.sag.SAGDonation;
import com.stee.spfcore.webapi.model.sag.SAGEventDetail;
import com.stee.spfcore.webapi.model.sag.SAGEventRsvp;
import com.stee.spfcore.webapi.model.sag.SAGInputType;
import com.stee.spfcore.webapi.model.sag.SAGInputs;
import com.stee.spfcore.webapi.model.sag.SAGPrivileges;
import com.stee.spfcore.webapi.model.sag.SAGSubInputs;
import com.stee.spfcore.webapi.model.template.Template;
import com.stee.spfcore.webapi.notification.BatchElectronicMail;
import com.stee.spfcore.webapi.service.config.IMailRecipientConfig;
import com.stee.spfcore.webapi.service.config.IMailSenderConfig;
import com.stee.spfcore.webapi.service.config.ServiceConfig;
import com.stee.spfcore.webapi.service.notification.INotificationService;
import com.stee.spfcore.webapi.service.notification.NotificationServiceException;
import com.stee.spfcore.webapi.service.notification.NotificationServiceFactory;
import com.stee.spfcore.webapi.utils.Encipher;
import com.stee.spfcore.webapi.utils.EnvironmentUtils;
import com.stee.spfcore.webapi.utils.UserGroupUtil;

@Service
public class SAGService {

	private SAGApplicationDAO sagApplicationDAO;
	private PersonalDetailDAO personnelDAO;
	private TemplateDAO templateDAO;
	private static final Logger logger = Logger.getLogger(CorporateCardService.class.getName());
	private static final String CATEGORY_ID = "SAG";
	private static final String NOTIFY_PO_AMENDED_SAG_EMAIL = "SAG-E014";
	
	@Autowired
	public SAGService (SAGApplicationDAO sagApplicationDAO, PersonalDetailDAO personnelDAO, TemplateDAO templateDAO) {
		this.sagApplicationDAO = sagApplicationDAO;
		this.personnelDAO = personnelDAO;
		this.templateDAO = templateDAO;
	}
	
	@Transactional
	public SAGApplication getSAGApplication(String referenceNumber) {
		return sagApplicationDAO.getSAGApplication(referenceNumber);
	}
	
	@Transactional
	public List<SAGApplication> searchSAGApplication(String nric, 
			String childNric, String awardType, String financialYear, boolean isOrderAsc) {
		return sagApplicationDAO.searchSAGApplication(nric, childNric, awardType, financialYear, isOrderAsc);
	}
	
	@Transactional
	public void addSAGApplication(SAGApplication sagApplication) {
			
		sagApplicationDAO.addSAGApplication(sagApplication);
	}
	
	@Transactional
	public void updateSAGApplication(SAGApplication sagApplication) {
			
		sagApplicationDAO.updateSAGApplication(sagApplication);
	}
	
	@Transactional
	public List <SAGBatchFileRecord> searchSAGBatchFileRecordByReferenceNumber(List<String> referenceNumberList) {
			
		return sagApplicationDAO.searchSAGBatchFileRecordByReferenceNumber(referenceNumberList);
	}
	
	@Transactional
	public void saveSAGBatchFileRecord(SAGBatchFileRecord sagBatchFileRecord) {
			
		sagApplicationDAO.saveSAGBatchFileRecord(sagBatchFileRecord);
	}
	
	@Transactional
	public void saveSAGBatchFileRecordList(List<SAGBatchFileRecord> sagBatchFileRecordList) {
			
		sagApplicationDAO.saveSAGBatchFileRecordList(sagBatchFileRecordList);
	}
	
	@Transactional
	public void saveSAGConfigSetup(SAGConfigSetup sagConfigSetup) {
			
		sagApplicationDAO.saveSAGConfigSetup(sagConfigSetup);
	}
	
	@Transactional
	public void saveSAGPrivilege(SAGPrivileges sagPrivileges) {
			
		sagApplicationDAO.saveSAGPrivilege(sagPrivileges);
	}
	
	@Transactional
	public void deleteSAGPrivileges(List< String > privilegesIdList) {
			
		sagApplicationDAO.deleteSAGPrivileges(privilegesIdList);
	}
	
	@Transactional
	public void saveAwardQuantum(SAGAwardQuantum sagAwardQuantum) {
			
		sagApplicationDAO.saveAwardQuantum(sagAwardQuantum);
	}
	
	@Transactional
	public void saveDonation(SAGDonation sagDonation) {
			
		sagApplicationDAO.saveDonation(sagDonation);
	}
	
	@Transactional
	public void deleteDonations(List< String > donationsIdList) {
			
		sagApplicationDAO.deleteDonations(donationsIdList);
	}
	
	@Transactional
	public void saveSAGEventDetail(SAGEventDetail sagEventDetail) {
			
		sagApplicationDAO.saveSAGEventDetail(sagEventDetail);
	}
	
	@Transactional
	public void saveSAGAnnouncementConfig(SAGAnnouncementConfig sagAnnouncementConfig) {
			
		sagApplicationDAO.saveSAGAnnouncementConfig(sagAnnouncementConfig);
	}
	
	@Transactional
	public void batchUpdateSAGApplication(List<SAGApplication> sagApplicationList) {
			
		sagApplicationDAO.batchUpdateSAGApplication(sagApplicationList, 20);
	}
	
	@Transactional
	public void saveBatchFileConfig(BatchFileConfig batchConfig) {
			
		sagApplicationDAO.saveBatchFileConfig(batchConfig);
	}

	@Transactional
	public List<SAGApplication> searchSAGApplicationBySubmission(String nric, String financialYear) {

		return sagApplicationDAO.searchSAGApplicationBySubmission(nric, financialYear);
	}

	@Transactional
	public List<SAGApplication> searchSAGApplicationBySubmission(String nric, String financialYear, Date date) {

		return sagApplicationDAO.searchSAGApplicationBySubmission(nric, financialYear, date);
	}

	@Transactional
	public List<SAGConfigSetup> searchConfigSetup(String financialYear) {
		return sagApplicationDAO.searchConfigSetup(financialYear);
	}

	@Transactional
	public SAGApplication searchSAGApplicationsRetrieveFamilyBackground(String nric, String childNric, String awardType,
			String financialYear, boolean isOrderAsc) {
		return sagApplicationDAO.searchSAGApplicationsRetrieveFamilyBackground(nric, childNric, awardType, financialYear, isOrderAsc);
	}

	@Transactional
	public List<SAGApplication> searchSAGApplicationsByReferenceNumber(List<String> referenceNumberList) {
		return sagApplicationDAO.searchSAGApplicationsByReferenceNumber(referenceNumberList);
	}

	@Transactional
	public SAGConfigSetup getConfigSetup(String id) {
		return sagApplicationDAO.getConfigSetup(id);
	}

	@Transactional
	public SAGConfigSetup getConfigSetup(String financialYear, SAGDateConfigType configType) {
		return sagApplicationDAO.getConfigSetup(financialYear, configType);
	}
	
	@Transactional
	public List<SAGEventDetail> searchEventDetail(String eventId, String financialYear) {
		return sagApplicationDAO.searchEventDetail(eventId, financialYear);
	}

	@Transactional
	public List<SAGEventRsvp> searchEventRsvp(String financialYear, String eventId, String refSeqNumber,
			String attendeeName, String attendeeId) {
		return sagApplicationDAO.searchEventRsvp(financialYear, eventId, refSeqNumber, attendeeName, attendeeId);
	}

	@Transactional
	public List<SAGInputs> getListOfSAGInputs(String awardType) {
		return sagApplicationDAO.getListOfSAGInputs(awardType);
	}

	@Transactional
	public List<SAGSubInputs> getSubInputListByCriteria(String awardType, String parentId, SAGInputType parentType) {
		return sagApplicationDAO.getSubInputListByCriteria(awardType, parentId, parentType);
	}

	@Transactional
	public List<SAGApplication> searchSimliarSAGApplication(String childNric, String financialYear, String referenceNumber) {
		return sagApplicationDAO.searchSimliarSAGApplication(childNric, financialYear, referenceNumber);
	}

	@Transactional
	public List<SAGInputs> getListOfSAGInputByType(String awardType, SAGInputType inputType) {
		return sagApplicationDAO.getListOfSAGInputByType(awardType, inputType);
	}

	@Transactional
	public SAGInputs getSAGInput(String awardType, SAGInputType inputType, String inputId) {
		return sagApplicationDAO.getSAGInput(awardType, inputType, inputId);
	}

	@Transactional
	public void deleteEventRsvp(List<String> rsvpIdList) {
		sagApplicationDAO.deleteEventRsvp(rsvpIdList);
	}

	@Transactional
	public void saveEventRsvp(SAGEventRsvp sagEventRsvp) {
		sagApplicationDAO.saveEventRsvp(sagEventRsvp);
	}

	@Transactional
	public void saveSAGApplication(SAGApplication sagApplication, boolean isNewSave) {
		sagApplicationDAO.saveSAGApplication(sagApplication, isNewSave);
		
	}

	@Transactional
	public List<SAGApplication> searchSAGApplicationsByChildNricAndEduLevel(String childNric, String awardType,
			String childNewEduLevel, String childHighestEduLevel, String financialYear) {
		return sagApplicationDAO.searchSAGApplicationsByChildNricAndEduLevel(childNric, awardType,
				childNewEduLevel, childHighestEduLevel, financialYear);
	}

	@Transactional
	public List<SAGPrivileges> searchSAGPrivileges(String financialYear, String memberNric) {
		return sagApplicationDAO.searchSAGPrivileges(financialYear, memberNric);
	}
	
	public void updateSAGApplicationPaymentProcess (Map<String, Object> context) {
		//sender email
		IMailSenderConfig mailSenderConfig = ServiceConfig.getInstance().getMailSenderConfig(CATEGORY_ID);
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
		//retrieve group email list from config of group "SPFCORE_Welfare Planning Processing Officer Admin"
		IMailRecipientConfig recipientConfig = ServiceConfig.getInstance().getMailRecipientConfig(CATEGORY_ID);
		
		List<String> toAddress = retrieveGroupEmails (recipientConfig.toRecipientGroups());
		
		//send email notification 
		sendEmail(context, senderEmail, senderPassword, toAddress, NOTIFY_PO_AMENDED_SAG_EMAIL);
		
	}

	private List<String> retrieveGroupEmails (List<String> groups) {
		
		List<String> users = UserGroupUtil.getUsersInGroups(groups);
		if (users == null || users.isEmpty()) {
			return null;
		}
		
		List<String> addresses = personnelDAO.getOfficeEmailAddress(users);
		
		if (addresses == null || addresses.isEmpty()) {
			return null;
		}
		
		return addresses;
	}
	
	private boolean sendEmail ( Map<String, Object> context, String senderAddress, String senderPassword, List< String > recipients, String templateName) {

		
		Template emailTemplate = templateDAO.getTemplateByName(templateName);
		
		String emailBody = ( emailTemplate != null) ? emailTemplate.getEmailBody() : null;
        String emailSubject = ( emailTemplate != null) ? emailTemplate.getEmailSubject() : null;
		
		String referenceNumber = (String) context.get("referenceNumber");
		String year = (String) context.get("year");
		String awardType = (String) context.get("awardType");
        
        emailBody = this.getTemplateForAmend(emailBody, referenceNumber, year, awardType);
        emailSubject = this.getTemplateForAmend(emailSubject, referenceNumber, year, awardType);
        
        BatchElectronicMail mail = new BatchElectronicMail();
        mail.setHtmlContent( true );
        mail.setSubject( emailSubject );
        mail.setUserAddress( senderAddress );
        mail.setToRecipients( recipients );
        mail.setText( emailBody );
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

	public String getTemplateForAmend (String templateString, String referenceNumber, String year, String awardType) {
    	
    	String resultString = templateString;
    	
    	// Replace template string with parameters
    	resultString = resultString.replaceAll("\\$year", year);
    	resultString = resultString.replaceAll("\\$referenceNumber", referenceNumber);
    	if ("SA".equals(awardType))
    	{
    		resultString = resultString.replaceAll("\\$awardType", "Study Award");
    	}
    	if ("SAA".equals(awardType))
    	{
    		resultString = resultString.replaceAll("\\$awardType", "Scholastic Achievement Award");
    	}
    	if ("SG".equals(awardType))
    	{
    		resultString = resultString.replaceAll("\\$awardType", "Study Grant");
    	}
    	return resultString;
    }
}
