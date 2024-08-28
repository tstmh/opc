package com.stee.spfcore.service.hr.impl.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.notification.ElectronicMail;
import com.stee.spfcore.service.configuration.IHRInterfaceConfig;
import com.stee.spfcore.service.configuration.ServiceConfig;
import com.stee.spfcore.service.hr.impl.IBatchJob;
import com.stee.spfcore.service.hr.impl.RecordError;
import com.stee.spfcore.service.hr.impl.ServiceTypeChange;
import com.stee.spfcore.service.hr.impl.input.CodeInfo;
import com.stee.spfcore.service.hr.impl.input.DataException;
import com.stee.spfcore.service.notification.INotificationService;
import com.stee.spfcore.service.notification.NotificationServiceException;
import com.stee.spfcore.service.notification.NotificationServiceFactory;
import com.stee.spfcore.utils.Encipher;
import com.stee.spfcore.utils.EnvironmentUtils;
import com.stee.spfcore.utils.template.TemplateUtil;

/**
 * This class is not design to be thread-safe,
 * which is ok as the batch job shouldn't be 
 * scheduled to run at same time.
 *
 */
public class EmailUtil {

	private EmailUtil(){}
	private static final Logger logger = Logger.getLogger(EmailUtil.class.getName());
			
	private static IHRInterfaceConfig config;
	private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	private static synchronized IHRInterfaceConfig getConfig () {
		if (config == null) {
			config = ServiceConfig.getInstance().getHRInterfaceConfig();
		}
		return config;
	}
	
	public static void sendFileMissing (IBatchJob job, Date processedDate) {
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		IHRInterfaceConfig config = getConfig();
		
		String subjectTemplateName = config.fileMissingEmailSubject();
		String bodyTemplateName = config.fileMissingEmailBody();
		
		//Setup data to populate the template
		Map<String,String> context = new HashMap<>();
		context.put("ExternalSystem", job.getType().toString());
		context.put("FileName", job.getInboundFile().getName());
		context.put("ProcessedDate", DATE_FORMAT.format(processedDate));
		
		sendEmail (config.emailToAddresses(), subjectTemplateName, bodyTemplateName, context);
	}
	
	public static void sendUnableToProcess (IBatchJob job, Date processedDate, DataException exception) {
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		IHRInterfaceConfig config = getConfig();
		
		String subjectTemplateName = config.statusEmailSubject();
		String bodyTemplateName = config.statusEmailBody();
		
		//Setup data to populate the template
		Map<String,String> context = new HashMap<>();
		context.put("ExternalSystem", job.getType().toString());
		context.put("FileName", job.getInboundFile().getName());
		context.put("ProcessedDate", DATE_FORMAT.format(processedDate));
		context.put("Status", exception.getMessage());
		context.put("SuccessfulCount", "-");
		context.put("UnsuccessfulCount", "-");
		
		sendEmail (config.emailToAddresses(), subjectTemplateName, bodyTemplateName, context);
	}
	
	
	public static void sendRecordCountNotTally (IBatchJob job, Date processedDate) {
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		IHRInterfaceConfig config = getConfig();
		
		String subjectTemplateName = config.statusEmailSubject();
		String bodyTemplateName = config.statusEmailBody();
		
		//Setup data to populate the template
		Map<String,String> context = new HashMap<>();
		context.put("ExternalSystem", job.getType().toString());
		context.put("FileName", job.getInboundFile().getName());
		context.put("ProcessedDate", DATE_FORMAT.format(processedDate));
		context.put("Status", "Record count does not tally");
		context.put("SuccessfulCount", "-");
		context.put("UnsuccessfulCount", "-");
		
		sendEmail (config.emailToAddresses(), subjectTemplateName, bodyTemplateName, context);
	}
	
	public static void sendSuccessStatus (IBatchJob job, int successCount, int failCount, Date processedDate) {
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		IHRInterfaceConfig config = getConfig();
		
		String subjectTemplateName = config.statusEmailSubject();
		String bodyTemplateName = config.statusEmailBody();
		
		//Setup data to populate the template
		Map<String,String> context = new HashMap<>();
		context.put("ExternalSystem", job.getType().toString());
		context.put("FileName", job.getInboundFile().getName());
		context.put("ProcessedDate", DATE_FORMAT.format(processedDate));
		context.put("Status", "Successful");
		context.put("SuccessfulCount", String.valueOf(successCount));
		context.put("UnsuccessfulCount", String.valueOf(failCount));
		
		sendEmail (config.emailToAddresses(), subjectTemplateName, bodyTemplateName, context);
	}
	
	
	public static void sendNonMatchingCodesAndInvalidValues (IBatchJob job, Set<CodeInfo> codes, List<RecordError> recordErrors) {
		IHRInterfaceConfig config = getConfig();
		
		String subjectTemplateName = config.codeEmailSubject();
		String bodyTemplateName = config.codeEmailBody();
		
		//Setup data to populate the template
		Map<String,Object> context = new HashMap<>();
		context.put("ExternalSystem", job.getType().toString());
		
		List<Map<String,String>> list = new ArrayList<>();
		for (CodeInfo codeInfo : codes) {
			Map<String,String> map = new HashMap<>();
			map.put("tag", codeInfo.getTag());
			map.put("value", codeInfo.getValue());
			map.put("description", codeInfo.getDescription());
			
			list.add(map);
		}
		
		context.put("nonMatchingCodes", list);
		context.put("recordErrors", recordErrors);
		
		sendEmail (config.emailToAddresses(), subjectTemplateName, bodyTemplateName, context);
		
	}
	
	
	public static void sendInternalError (IBatchJob job, Exception e, Date processedDate) {
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		IHRInterfaceConfig config = getConfig();
		
		String subjectTemplateName = config.statusEmailSubject();
		String bodyTemplateName = config.statusEmailBody();
		
		//Setup data to populate the template
		Map<String,String> context = new HashMap<>();
		context.put("ExternalSystem", job.getType().toString());
		context.put("FileName", job.getInboundFile().getName());
		context.put("ProcessedDate", DATE_FORMAT.format(processedDate));
		context.put("Status", e.getMessage());
		context.put("SuccessfulCount", "-");
		context.put("UnsuccessfulCount", "-");
		
		sendEmail (config.emailToAddresses(), subjectTemplateName, bodyTemplateName, context);
	}
	
	
	public static void sendServiceTypeChange (List<ServiceTypeChange> serviceTypeChanges) {
		
		IHRInterfaceConfig config = getConfig();
		
		String subjectTemplateName = config.serviceTypeChangeEmailSubject();
		String bodyTemplateName = config.serviceTypeChangeEmailBody();
		
		//Setup data to populate the template
		Map<String,Object> context = new HashMap<>();
		context.put("serviceTypeChangeRecords", serviceTypeChanges);
			
		sendEmail (config.emailToAddresses(), subjectTemplateName, bodyTemplateName, context);
	}
	
	public static void sendApplyMembership (String email, String name) {
		
		IHRInterfaceConfig config = getConfig();
		
		String subjectTemplateName = config.applyMembershipEmailSubject();
		String bodyTemplateName = config.applyMembershipEmailBody();
		
		//Setup data to populate the template
		Map<String,Object> context = new HashMap<>();
		context.put("OfficerName", name);
			
		sendEmail (email, subjectTemplateName, bodyTemplateName, context);
	}
	
	private static void sendEmail (String toAddress, String subjectTemplate, String bodyTemplate, Map<String,?> context) {
		
		String subject = TemplateUtil.getInstance().format(subjectTemplate, context);
		String body = TemplateUtil.getInstance ().format(bodyTemplate, context);
		
		INotificationService notificationService = NotificationServiceFactory.getInstance();
		
		ElectronicMail mail = new ElectronicMail();
		mail.setSubject(subject);
		mail.setText(body);
		mail.setToAddress(toAddress);
		mail.setUserAddress(config.emailUserAddress());
		mail.setHtmlContent(true);
		
		String encryptionKey = EnvironmentUtils.getEncryptionKey();
		if (encryptionKey != null && !encryptionKey.isEmpty()) {
			try {
				Encipher encipher = new Encipher(encryptionKey);
				mail.setUserPassword(encipher.decrypt(config.emailUserPassword()));
			} 
			catch (Exception e) {
				logger.log(Level.SEVERE, "Error while decrypting the configured password.", e);
			}
		}
		else {
			mail.setUserPassword(config.emailUserPassword());
		}
		
		try {
			notificationService.send(mail);
		} 
		catch (NotificationServiceException e) {
			logger.log(Level.SEVERE, "Fail to send batch file processing fail notification.", e);
		}
	}
	
	
}
