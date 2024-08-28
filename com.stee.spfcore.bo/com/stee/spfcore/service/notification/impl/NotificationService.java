package com.stee.spfcore.service.notification.impl;

import java.io.*;
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

import com.stee.spfcore.notification.BatchElectronicMail;
import com.stee.spfcore.notification.BatchShortMessage;
import com.stee.spfcore.notification.ElectronicMail;
import com.stee.spfcore.notification.ShortMessage;
import com.stee.spfcore.service.configuration.IMailServerConfig;
import com.stee.spfcore.service.configuration.ISmsServerConfig;
import com.stee.spfcore.service.configuration.ServiceConfig;
import com.stee.spfcore.service.notification.INotificationService;
import com.stee.spfcore.service.notification.NotificationServiceException;
import com.stee.spfcore.utils.CleanPath;
import com.stee.spfcore.utils.Encipher;
import com.stee.spfcore.utils.EnvironmentUtils;

public class NotificationService implements INotificationService {
	
	private static final Logger logger = Logger.getLogger(NotificationService.class.getName());
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private IMailServer smtp;
	private ISmsServer smsServer;
	
	public NotificationService() {
		// DO NOTHING
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

		return new SmsServer(config.hostname(), config.port(), config.protocol(), config.trustStore(), password);
	}

	@Override
	public void sendExtSMS(String recipient, String text) {
		logger.info("sendExtSMS method started");

		List<String> command = new ArrayList<>();
		String dateString = getDateString();
		String encodedText = null;

		try {
			encodedText = URLEncoder.encode(text, "UTF-8");
			logger.info("Encoded SMS content: " + encodedText);
		} catch (UnsupportedEncodingException e) {
			logger.log(Level.SEVERE, "Failed to encode SMS content", e);
			return;
		}

		String recipientPath = "E:\\SPF\\SMS_LOGS\\SMS_Recipient\\recipient_" + dateString + ".txt";
		PrintWriter pw = null;

		try {
			pw = new PrintWriter(recipientPath, "UTF-8");
			pw.println(recipient);
			logger.info("Recipient file created at: " + recipientPath);
		} catch (FileNotFoundException | UnsupportedEncodingException ex) {
			logger.log(Level.SEVERE, "Failed to create recipient file", ex);
			return;
		} finally {
			if (pw != null) {
				pw.close();
			}
		}

		String bpmPath = "E:\\IBM\\Workflow";
		logger.info("BPM Path: " + bpmPath);

		command.add(bpmPath + "\\java\\bin\\java.exe");
		command.add("-jar");
		command.add(bpmPath + "\\java\\bin\\SMSTester.jar");
		command.add(recipientPath);
		command.add(encodedText);
		command.add("true");

		logger.info("Command to be executed: " + command);

		ProcessBuilder pb = new ProcessBuilder(command);
		File log = new File("E:\\SPF\\SMS_LOGS\\smslog_batch_" + dateString + ".txt");

		pb.redirectErrorStream(true);
		pb.redirectOutput(ProcessBuilder.Redirect.appendTo(log));

		logger.info("Process log file: " + log.getAbsolutePath());
		logger.info("ProcessBuilder environment: " + pb.environment());
		logger.info("ProcessBuilder directory: " + pb.directory());

		try {
			Process process = pb.start();
			logger.info("Process started");

			// Capture the process output
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					logger.info("Process output: " + line);
				}
			}

			int exitCode = process.waitFor();
			logger.info("Process exited with code: " + exitCode);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Failed to send SMS with standalone app", e);
		} catch (InterruptedException e) {
			logger.log(Level.SEVERE, "Process was interrupted", e);
			Thread.currentThread().interrupt(); // Preserve the interruption status
		}
		logger.info("sendExtSMS method finished");
	}

	@Override
	public void sendExtSMS(List<String> recipientList, String text) throws NotificationServiceException {
// This method is intentionally left empty
	}


	public void sendExtSMS(String smsContent, List<String> recipients) {
		logger.info("sendExtSMS method started");

		List<String> command = new ArrayList<>();
		String dateString = getDateString();
		String encodedText = null;

		try {
			encodedText = URLEncoder.encode(smsContent, "UTF-8");
			logger.info("Encoded SMS content: " + encodedText);
		} catch (UnsupportedEncodingException e) {
			logger.log(Level.SEVERE, "Failed to encode SMS content", e);
			return;
		}

		String recipientPath = "E:\\SPF\\SMS_LOGS\\SMS_Recipient\\recipient_" + dateString + ".txt";
		PrintWriter pw = null;

		try {
			pw = new PrintWriter(recipientPath, "UTF-8");
			for (String value : recipients) {
				pw.println(value);
			}
			logger.info("Recipient file created at: " + recipientPath);
		} catch (FileNotFoundException | UnsupportedEncodingException ex) {
			logger.log(Level.SEVERE, "Failed to create recipient file", ex);
			return;
		} finally {
			if (pw != null) {
				pw.close();
			}
		}

		String bpmPath = "E:\\IBM\\Workflow";
		logger.info("BPM Path: " + bpmPath);

		command.add(bpmPath + "\\java\\bin\\java.exe");
		command.add("-jar");
		command.add(bpmPath + "\\java\\bin\\SMSTester.jar");
		command.add(recipientPath);
		command.add(encodedText);
		command.add("true");

		logger.info("Command to be executed: " + command);

		ProcessBuilder pb = new ProcessBuilder(command);
		File log = new File("E:\\SPF\\SMS_LOGS\\smslog_batch_" + dateString + ".txt");

		pb.redirectErrorStream(true);
		pb.redirectOutput(ProcessBuilder.Redirect.appendTo(log));

		logger.info("Process log file: " + log.getAbsolutePath());
		logger.info("ProcessBuilder environment: " + pb.environment());
		logger.info("ProcessBuilder directory: " + pb.directory());

		try {
			Process process = pb.start();
			logger.info("Process started");

			// Capture the process output
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					logger.info("Process output: " + line);
				}
			}

			int exitCode = process.waitFor();
			logger.info("Process exited with code: " + exitCode);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Failed to send SMS with standalone app", e);
		} catch (InterruptedException e) {
			logger.log(Level.SEVERE, "Process was interrupted", e);
			Thread.currentThread().interrupt(); // Preserve the interruption status
		}

		logger.info("sendExtSMS method finished");
	}

	public static String getDateString() {
		
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar.get(Calendar.YEAR) +
		String.valueOf(calendar.get(Calendar.MONTH) + 1) +
				calendar.get(Calendar.DATE) +
				calendar.get(Calendar.HOUR_OF_DAY) +
				calendar.get(Calendar.MINUTE) +
				calendar.get(Calendar.MILLISECOND);
		
	}
}
