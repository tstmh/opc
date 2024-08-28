package com.stee.spfcore.service.system.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.HibernateException;

import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.dao.SystemDAO;
import com.stee.spfcore.model.system.Heartbeat;
import com.stee.spfcore.model.system.SystemStatus;
import com.stee.spfcore.notification.ElectronicMail;
import com.stee.spfcore.service.configuration.ISystemStatusConfig;
import com.stee.spfcore.service.configuration.ServiceConfig;
import com.stee.spfcore.service.notification.INotificationService;
import com.stee.spfcore.service.notification.NotificationServiceException;
import com.stee.spfcore.service.notification.NotificationServiceFactory;
import com.stee.spfcore.service.system.ISystemService;
import com.stee.spfcore.service.system.SystemServiceException;
import com.stee.spfcore.utils.Encipher;
import com.stee.spfcore.utils.EnvironmentUtils;
import com.stee.spfcore.utils.Util;
import com.stee.spfcore.utils.template.TemplateUtil;

public class SystemService implements ISystemService {

	private static final Logger logger = Logger.getLogger(SystemService.class.getName());

	private SystemDAO dao;
	private ISystemStatusConfig config;

	private long heartbeatIntervalInMillisecond;
	private long reportIntervalInMillisecond;

	public SystemService() {
		dao = new SystemDAO();
		config = ServiceConfig.getInstance().getSystemStatusConfig();

		int heartbeatIntervalInSecond = config.heartbeatReportIntervalInSeconds() + config.heartbeatGracePeriodInSeconds();
		heartbeatIntervalInMillisecond = heartbeatIntervalInSecond * 1000L;

		int reportIntervalInSecond = config.notServiceableReportIntervalInSeconds();
		reportIntervalInMillisecond = reportIntervalInSecond * 1000L;
	}

	@Override
	public void process() throws SystemServiceException {
		updateSystemStatus();
	}

	@Override
	public void sendHeartbeat() throws SystemServiceException {
		throw new UnsupportedOperationException ("Only Internet side is allow to call send heartbeat.");
	}

	@Override
	public void receiveHeartbeat(Heartbeat heartbeat) throws SystemServiceException {
		Date now = new Date();

		String systemName = heartbeat.getSystemName();
		Date timestamp = heartbeat.getTimestamp();
		long sinceLastUpdated = now.getTime() - timestamp.getTime();
		if (sinceLastUpdated > (heartbeatIntervalInMillisecond * 2)) {
			logger.log(Level.WARNING, "Heartbeat message is discarded = " + Util.replaceNewLine( systemName ) + " / " + timestamp);

			return;
		}

		try {
			SessionFactoryUtil.beginTransaction();

			SystemStatus systemStatus = dao.getSystemStatus(systemName);

			if (systemStatus == null) {
				systemStatus = new SystemStatus(systemName, now);
				dao.addSystemStatus(systemStatus);
			}
			else {
				systemStatus.setLastUpdatedTime(now);

				// Report if the system is now serviceable.
				if (systemStatus.isNotServiceable())
				{
					systemStatus.setServiceable(true);
					reportSystemIsServiceable(systemStatus);

					if (logger.isLoggable(Level.FINER)) {
						logger.log(Level.FINER, "Sent status up report for " + Util.replaceNewLine( systemName ) + " / " + now + " ...");
					}
				}

				dao.updateSystemStatus(systemStatus);
			}

			if (logger.isLoggable(Level.FINER)) {
				logger.log(Level.FINER, "Received Heartbeat = " + systemStatus.getLastUpdatedTime());
			}

			SessionFactoryUtil.commitTransaction();
		} 
		catch (HibernateException exception) {
			logger.log (Level.SEVERE, "Fail to handle HeartbeatMessageHandler : ", exception);
			SessionFactoryUtil.rollbackTransaction();
		}		
	}

	@Override
	public void updateSystemStatus() throws SystemServiceException {
		Date now = new Date();
		List<SystemStatus> systemStatusList = null;

		try {
			SessionFactoryUtil.beginTransaction();

			systemStatusList = dao.getSystemStatusList();			

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception exception) {
			SessionFactoryUtil.rollbackTransaction();
			throw new SystemServiceException("Fail to get heartbeat list:" + now, exception);
		}

		try {
			for (SystemStatus systemStatus : systemStatusList) {

				try {
					String systemName = systemStatus.getSystemName();

					if (logger.isLoggable(Level.FINER)) {
						logger.log(Level.FINER, "Update system status for " + Util.replaceNewLine( systemName ) + " / " + now + " ...");
					}

					Date lastUpdatedTime = systemStatus.getLastUpdatedTime();
					long sinceLastUpdated = now.getTime() - lastUpdatedTime.getTime();

					// If this system has reported its status, then continue to the next system.
					// NOTE: Allow 2 times the heartbeat interval
					if (sinceLastUpdated >= 0 &&
							sinceLastUpdated <= (heartbeatIntervalInMillisecond * 2)) {
						continue;
					}
					
					// Send the first report to the support team.
					if (systemStatus.isServiceable()) {
						systemStatus.setServiceable(false);
						systemStatus.setLastReportTime(now);

						try {
							SessionFactoryUtil.beginTransaction();
							dao.updateSystemStatus(systemStatus);

							SessionFactoryUtil.commitTransaction();
						}
						catch ( Exception exception ) {
							logger.log( Level.WARNING, "Fail to update system status " + Util.replaceNewLine( systemName ), exception );
							SessionFactoryUtil.rollbackTransaction();
							continue;
						}

						reportSystemIsNotServiceable(systemStatus);

						if (logger.isLoggable(Level.FINER)) {
							logger.log(Level.FINER, "Sent status down report for " + Util.replaceNewLine( systemName ) + " / " + now + " ...");
						}

						continue;
					}

					// Send the reminder report after the report interval expired.
					Date lastReportTime = systemStatus.getLastReportTime();
					long sinceLastReport = now.getTime() - lastReportTime.getTime();
					long reportIntervalReached = sinceLastReport - reportIntervalInMillisecond;

					if (reportIntervalReached >= 0) {
						systemStatus.setLastReportTime(now);

						try {
							SessionFactoryUtil.beginTransaction();
							dao.updateSystemStatus(systemStatus);

							SessionFactoryUtil.commitTransaction();
						}
						catch ( HibernateException exception ) {
							logger.log( Level.WARNING, "Fail to update system status " + Util.replaceNewLine( systemName ), exception );
							SessionFactoryUtil.rollbackTransaction();
							continue;
						}

						reportSystemIsNotServiceable(systemStatus);

						if (logger.isLoggable(Level.FINER)) {
							logger.log(Level.FINER, "Sent reminder report for " + Util.replaceNewLine( systemName ) + " / " + now + " ...");
						}
					}
				}
				catch (Exception exception) {
					logger.log (Level.SEVERE, "Fail to update system status for : " + Util.replaceNewLine( systemStatus.getSystemName() ), exception);
				}
			}
		}
		catch (Exception exception) {
			throw new SystemServiceException("Fail to get heartbeat list:" + now, exception);
		}
	}

	private void reportSystemIsServiceable(SystemStatus systemStatus) {

		String subjectTemplateName = config.systemServicableEmailSubject();
		String bodyTemplateName = config.systemServicableEmailBody();

		Map<String,String> context = new HashMap<>();
		context.put("SystemName", systemStatus.getSystemName());
		context.put("SystemStatus", "is serviceable");

		sendEmail(subjectTemplateName, bodyTemplateName, context);
	}

	private void reportSystemIsNotServiceable(SystemStatus systemStatus) {

		String subjectTemplateName = config.systemServicableEmailSubject();
		String bodyTemplateName = config.systemServicableEmailBody();

		Map<String,String> context = new HashMap<>();
		context.put("SystemName", systemStatus.getSystemName());
		context.put("SystemStatus", "is not serviceable");

		sendEmail(subjectTemplateName, bodyTemplateName, context);
	}

	private void sendEmail(String subjectTemplate, String bodyTemplate, Map<String,?> context) {

		String subject = TemplateUtil.getInstance().format(subjectTemplate, context);
		String body = TemplateUtil.getInstance ().format(bodyTemplate, context);

		ISystemStatusConfig iSystemStatusConfig = ServiceConfig.getInstance().getSystemStatusConfig();
		INotificationService notificationService = NotificationServiceFactory.getInstance();

		ElectronicMail mail = new ElectronicMail();
		mail.setSubject(subject);
		mail.setText(body);
		mail.setToAddress(iSystemStatusConfig.emailToAddresses());
		mail.setUserAddress(iSystemStatusConfig.emailUserAddress());
		mail.setHtmlContent(true);

		String encryptionKey = EnvironmentUtils.getEncryptionKey();

		if (encryptionKey != null && !encryptionKey.isEmpty()) {
			try {
				Encipher encipher = new Encipher(encryptionKey);
				mail.setUserPassword(encipher.decrypt(iSystemStatusConfig.emailUserPassword()));
			} 
			catch (Exception exception) {
				logger.log(Level.SEVERE, "Error while decrypting the configured password.", exception);
			}
		}
		else {
			mail.setUserPassword(iSystemStatusConfig.emailUserPassword());
		}

		try {
			notificationService.send(mail);
		} 
		catch (NotificationServiceException exception) {
			logger.log(Level.SEVERE, "Fail to send system status fail notification.", exception);
		}
	}
}
