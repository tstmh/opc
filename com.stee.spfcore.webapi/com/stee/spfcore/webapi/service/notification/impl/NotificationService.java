package com.stee.spfcore.webapi.service.notification.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.ProcessBuilder.Redirect;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.webapi.notification.BatchElectronicMail;
import com.stee.spfcore.webapi.notification.BatchShortMessage;
import com.stee.spfcore.webapi.notification.ElectronicMail;
import com.stee.spfcore.webapi.notification.ShortMessage;
import com.stee.spfcore.webapi.service.config.IMailServerConfig;
import com.stee.spfcore.webapi.service.config.ISmsServerConfig;
import com.stee.spfcore.webapi.service.config.ServiceConfig;
import com.stee.spfcore.webapi.service.notification.INotificationService;
import com.stee.spfcore.webapi.service.notification.NotificationServiceException;
import com.stee.spfcore.webapi.utils.CleanPath;
import com.stee.spfcore.webapi.utils.Encipher;
import com.stee.spfcore.webapi.utils.EnvironmentUtils;

public class NotificationService implements INotificationService {
	
	private static final Logger logger = Logger.getLogger(NotificationService.class.getName());
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private IMailServer smtp;
	private ISmsServer smsServer;
	
	public NotificationService() {
	}
	
	@Override
	public void send(ElectronicMail email) throws NotificationServiceException {
		
		if (this.smtp == null) {
			this.smtp = createMailServer();
		}

		this.smtp.send(email);
	}

	
	@Override
	public void send(BatchElectronicMail email) throws NotificationServiceException {
		if (this.smtp == null) {
			this.smtp = createMailServer();
		}

		this.smtp.send(email);
	}
	
	@Override
	public void send(ShortMessage sms) throws NotificationServiceException {
		
		if (smsServer == null) {
			this.smsServer = createSmsServer ();
		}
	
		this.smsServer.send(sms);
	}

	@Override
	public void send(BatchShortMessage sms) throws NotificationServiceException {
		if (smsServer == null) {
			this.smsServer = createSmsServer ();
		}
	
		this.smsServer.send(sms);
	}

	private IMailServer createMailServer() {
		IMailServerConfig config = ServiceConfig.getInstance().getMailServerConfig();

		IMailServer smtp = new MailServer(config.hostname(), config.port(), config.protocol(), config.maxRecipientsPerMessage());

		if (logger.isLoggable(Level.FINER)) {
			logger.log(Level.FINER,
					"Mail server created : " + config.hostname() + ":" + config.port() + ":" + config.protocol());
		}

		return smtp;
	}	
	
	private ISmsServer createSmsServer () {
		
		ISmsServerConfig config = ServiceConfig.getInstance().getSmsServerConfig();
		String password = "";

        String encryptionKey = EnvironmentUtils.getEncryptionKey();

        if ( encryptionKey != null && !encryptionKey.isEmpty() ) {
            try {
                Encipher encipher = new Encipher( encryptionKey );
                password = encipher.decrypt(config.trustStorePassword() );
            }
            catch ( Exception e ) {
                logger.log( Level.SEVERE, "Error while decrypting the configured password.", e );
            }
        }
		ISmsServer smsServer = new SmsServer(config.hostname(), config.port(), config.protocol(), config.trustStore(), password);
		
		return smsServer;
	}
	
	@Override
	public void sendExtSMS(String recipient, String text) {
		
		logger.info("Send External SMS Called !!");
		List<String> command = new ArrayList<String> ();
		
		String dateString = getDateString();
		//String PATH_SEPARATOR = System.getProperty("path.separator");
		String encodedText = URLEncoder.encode(text);
		//Path path = Paths.get(CleanPath.cleanString("C:"+FILE_SEPARATOR+"IBM"+FILE_SEPARATOR+"BPM"+FILE_SEPARATOR+"v8.5.6"));
		Path path = Paths.get(CleanPath.cleanString("C:"+FILE_SEPARATOR+"IBM"+FILE_SEPARATOR+"BPM"+FILE_SEPARATOR+"v8.6"));
		String bpmPath = "";
		if (Files.exists(path)) {
			//bpmPath = CleanPath.cleanString("C:"+FILE_SEPARATOR+"IBM"+FILE_SEPARATOR+"BPM"+FILE_SEPARATOR+"v8.5.6");
			bpmPath = CleanPath.cleanString("C:"+FILE_SEPARATOR+"IBM"+FILE_SEPARATOR+"BPM"+FILE_SEPARATOR+"v8.6");
		}
		else {
		    //bpmPath = CleanPath.cleanString("C:"+FILE_SEPARATOR+"IBM"+FILE_SEPARATOR+"BPM"+FILE_SEPARATOR+"v8.5");
		    bpmPath = CleanPath.cleanString("C:"+FILE_SEPARATOR+"IBM"+FILE_SEPARATOR+"BPM"+FILE_SEPARATOR+"v8.6");
		}
		logger.info("BPM Path: " + bpmPath.replace('\n', '_').replace('\r', '_'));
		//Java 1.8 path
		command.add(CleanPath.cleanString(bpmPath + FILE_SEPARATOR+"java_1.8_64"+FILE_SEPARATOR+"bin"+FILE_SEPARATOR+"java.exe"));
		command.add("-jar");
		command.add("SMSTester.jar");
		command.add(recipient);
		command.add(encodedText);
		command.add("false");
		
		ProcessBuilder pb = new ProcessBuilder(command);
		
		//pb.directory(new File(CleanPath.cleanString("E:"+FILE_SEPARATOR+"SPF"+FILE_SEPARATOR+"SMS_LOGS"+FILE_SEPARATOR)));
		
		//File log = new File(CleanPath.cleanString("E:"+FILE_SEPARATOR+"SPF"+FILE_SEPARATOR+"SMS_LOGS"+FILE_SEPARATOR+"smslog_" + dateString + ".txt"));
		pb.directory(new File(CleanPath.cleanString("C:"+FILE_SEPARATOR+"SPF"+FILE_SEPARATOR+"SMS_LOGS"+FILE_SEPARATOR)));
		
		File log = new File(CleanPath.cleanString("C:"+FILE_SEPARATOR+"SPF"+FILE_SEPARATOR+"SMS_LOGS"+FILE_SEPARATOR+"smslog_" + dateString + ".txt"));
		
		pb.redirectErrorStream(true);
		pb.redirectOutput(Redirect.appendTo(log));
		
		try {
			Process process =pb.start();
			
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Fail to send SMS with standalone app", e);
		}
	}

	@Override
	public void sendExtSMS(List<String> recipientList, String text) {
		logger.info("Send Batch External SMS Called !!");
		
		List<String> command = new ArrayList<String>();
		
		String dateString = getDateString();
		//String PATH_SEPARATOR = System.getProperty("path.separator");
		String encodedText = URLEncoder.encode(text);
		//String recipientPath = CleanPath.cleanString("E:"+FILE_SEPARATOR+"SPF"+FILE_SEPARATOR+"SMS_LOGS"+FILE_SEPARATOR+"SMS_Recipient"+FILE_SEPARATOR+"recipient_" + dateString + ".txt");
		String recipientPath = CleanPath.cleanString("C:"+FILE_SEPARATOR+"SPF"+FILE_SEPARATOR+"SMS_LOGS"+FILE_SEPARATOR+"SMS_Recipient"+FILE_SEPARATOR+"recipient_" + dateString + ".txt");
		
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(recipientPath,"UTF-8");
			
			for (String value : recipientList) {
				pw.println(value);
			}
			
		} catch (FileNotFoundException ex) {
			logger.log(Level.SEVERE, "Fail to create recipient file", ex);
		} catch (UnsupportedEncodingException ex) {
			logger.log(Level.SEVERE, "Fail to create recipient file", ex);
		}
		finally{
			if(pw != null)
				pw.close();
		}
		
		//Path path = Paths.get(CleanPath.cleanString("C:"+FILE_SEPARATOR+"IBM"+FILE_SEPARATOR+"BPM"+FILE_SEPARATOR+"v8.5.6"));
		Path path = Paths.get(CleanPath.cleanString("C:"+FILE_SEPARATOR+"IBM"+FILE_SEPARATOR+"BPM"+FILE_SEPARATOR+"v8.6"));
        String bpmPath = "";
		if (Files.exists(path)) {
			//bpmPath = CleanPath.cleanString("C:"+FILE_SEPARATOR+"IBM"+FILE_SEPARATOR+"BPM"+FILE_SEPARATOR+"v8.5.6");
			bpmPath = CleanPath.cleanString("C:"+FILE_SEPARATOR+"IBM"+FILE_SEPARATOR+"BPM"+FILE_SEPARATOR+"v8.6");
		}
		else {
		    //bpmPath = CleanPath.cleanString("C:"+FILE_SEPARATOR+"IBM"+FILE_SEPARATOR+"BPM"+FILE_SEPARATOR+"v8.5");
		    bpmPath = CleanPath.cleanString("C:"+FILE_SEPARATOR+"IBM"+FILE_SEPARATOR+"BPM"+FILE_SEPARATOR+"v8.6");
		}
		logger.info("BPM Path: " + bpmPath.replace('\n', '_').replace('\r', '_'));
		command.add(CleanPath.cleanString(bpmPath + FILE_SEPARATOR+"java_1.8_64"+FILE_SEPARATOR+"bin"+FILE_SEPARATOR+"java.exe"));
		command.add("-jar");
		command.add("SMSTester.jar");
		command.add(recipientPath);
		command.add(encodedText);
		command.add("true");
		
		ProcessBuilder pb = new ProcessBuilder(command);
		
		//pb.directory(new File(CleanPath.cleanString("E:"+FILE_SEPARATOR+"SPF"+FILE_SEPARATOR+"SMS_LOGS"+FILE_SEPARATOR)));
		pb.directory(new File(CleanPath.cleanString("C:"+FILE_SEPARATOR+"SPF"+FILE_SEPARATOR+"SMS_LOGS"+FILE_SEPARATOR)));
		//File log = new File(CleanPath.cleanString("E:"+FILE_SEPARATOR+"SPF"+FILE_SEPARATOR+"SMS_LOGS"+FILE_SEPARATOR+"smslog_batch_" + dateString + ".txt"));
		File log = new File(CleanPath.cleanString("C:"+FILE_SEPARATOR+"SPF"+FILE_SEPARATOR+"SMS_LOGS"+FILE_SEPARATOR+"smslog_batch_" + dateString + ".txt"));
		
		pb.redirectErrorStream(true);
		pb.redirectOutput(Redirect.appendTo(log));
		
		Process process = null;
		try {
			process = pb.start();
			
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Fail to send SMS with standalone app", e);
		}
	}
	
	public static String getDateString() {
		
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		String dateString = String.valueOf(calendar.get(Calendar.YEAR)) + 
		String.valueOf(calendar.get(Calendar.MONTH) + 1) + 
		String.valueOf(calendar.get(Calendar.DATE)) +
		String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + 
		String.valueOf(calendar.get(Calendar.MINUTE)) +
		String.valueOf(calendar.get(Calendar.MILLISECOND));
		
		return dateString;
		
	}
}
