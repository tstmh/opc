package com.stee.spfcore.vo.announcement;

public class AnnouncementStatistic {
	
	private int totalNotification;
	private int notificationOpened;
	private int targetedEmailCount;
	private int targetedSmsCount;
	private int emailSent;
	private int smsSent;
	
	public AnnouncementStatistic() {
		super();
	}

	public AnnouncementStatistic(int totalNotification, int notificationOpened, int targetedEmailCount,
			int targetedSmsCount, int emailSent, int smsSent) {
		super();
		this.totalNotification = totalNotification;
		this.notificationOpened = notificationOpened;
		this.targetedEmailCount = targetedEmailCount;
		this.targetedSmsCount = targetedSmsCount;
		this.emailSent = emailSent;
		this.smsSent = smsSent;
	}

	public int getTotalNotification() {
		return totalNotification;
	}

	public void setTotalNotification(int totalNotification) {
		this.totalNotification = totalNotification;
	}

	public int getNotificationOpened() {
		return notificationOpened;
	}

	public void setNotificationOpened(int notificationOpened) {
		this.notificationOpened = notificationOpened;
	}

	public int getTargetedEmailCount() {
		return targetedEmailCount;
	}

	public void setTargetedEmailCount(int targetedEmailCount) {
		this.targetedEmailCount = targetedEmailCount;
	}

	public int getTargetedSmsCount() {
		return targetedSmsCount;
	}

	public void setTargetedSmsCount(int targetedSmsCount) {
		this.targetedSmsCount = targetedSmsCount;
	}

	public int getEmailSent() {
		return emailSent;
	}

	public void setEmailSent(int emailSent) {
		this.emailSent = emailSent;
	}

	public int getSmsSent() {
		return smsSent;
	}

	public void setSmsSent(int smsSent) {
		this.smsSent = smsSent;
	}

}
