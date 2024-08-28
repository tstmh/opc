package com.stee.spfcore.service.notification.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.stee.spfcore.notification.ShortMessage;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("MTREQUEST")
public class SingleRequest {

	@XStreamAlias("REQUESTID")
	private String requestId;
	
	@XStreamAlias("APPCODE")
	private String applicationCode;
	
	@XStreamAlias("CPCODE")
	private String senderCode;
	
	@XStreamAlias("USETPOA")
	private String useTPOA;

	@XStreamAlias("REQDATE")
	private String requestDate;

	@XStreamAlias("REQTIME")
	private String requestTime;
	
	@XStreamAlias("DCS")
	private String dataCodingScheme;
	
	@XStreamAlias("SMS")
	private SmsRequest sms;

	public SingleRequest(ShortMessage message) {
		
		this.requestId = "UATRQA00010";
		this.applicationCode = "SPFAPP01";
		this.senderCode = "NCS78794";
		this.useTPOA = "1";
		
		Date now = new Date();
		SimpleDateFormat date = new SimpleDateFormat( "yyyyMMdd" );
		SimpleDateFormat time = new SimpleDateFormat( "hhmmss" );
		
		this.requestDate = date.format(now);
		this.requestTime = time.format(now);
		
		this.dataCodingScheme = "001";
		this.sms = new SmsRequest(message);
	}
}
