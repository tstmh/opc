package com.stee.spfcore.service.hr.impl.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class LogFormatter extends SimpleFormatter {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
	
	@Override
	public synchronized String format(LogRecord logRecord) {
		StringBuilder builder = new StringBuilder();
		builder.append(dateFormat.format(new Date())).append (" - ").append (logRecord.getMessage()).append ("\n");
		
		return builder.toString();
	}
}
