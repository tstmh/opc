package com.stee.spfcore.service.hrps.impl;

public class FieldLengthConstants {
	
	//Inbound Header
	public static final int INBOUND_HEADER_RECORD_TYPE_LEN = 1;
	public static final int INBOUND_HEADER_RECORD_TYPE_START = 1;
	public static final int INBOUND_HEADER_RECORED_TYPE_END = 1;
	
	public static final int INBOUND_HEADER_SOURCE_LEN = 20;
	public static final int INBOUND_HEADER_SOURCE_START = 2;
	public static final int INBOUND_HEADER_SOURCE_END = 21;
	
	public static final int INBOUND_HEADER_DATETIME_LEN = 26;
	public static final int INBOUND_HEADER_DATETIME_START = 22;
	public static final int INBOUND_HEADER_DATETIME_END = 47;
	
	//Inbound Detail
	public static final int INBOUND_DETAIL_RECORD_TYPE_LEN = 1;
	public static final int INBOUND_DETAIL_RECORD_TYPE_START = 1;
	public static final int INBOUND_DETAIL_RECORD_TYPE_END = 1;
	
	public static final int INBOUND_DETAIL_ID_LEN = 9;
	public static final int INBOUND_DETAIL_ID_START = 2;
	public static final int INBOUND_DETAIL_ID_END = 10;
	
	public static final int INBOUND_DETAIL_STARTDATE_LEN = 8;
	public static final int INBOUND_DETAIL_STARTDATE_START = 11;
	public static final int INBOUND_DETAIL_STARTDATE_END = 18;
	
	public static final int INBOUND_DETAIL_ENDDATE_LEN = 8;
	public static final int INBOUND_DETAIL_ENDDATE_START = 19;
	public static final int INBOUND_DETAIL_ENDDATE_END = 26;
	
	public static final int INBOUND_DETAIL_WAGE_TYPE_LEN = 4;
	public static final int INBOUND_DETAIL_WAGE_TYPE_START = 27;
	public static final int INBOUND_DETAIL_WAGE_TYPE_END = 30;
	
	public static final int INBOUND_DETAIL_AMOUNT_LEN = 14; //13 + 1 Signed bit
	public static final int INBOUND_DETAIL_AMOUNT_START = 31;
	public static final int INBOUND_DETAIL_AMOUNT_END = 44;
	
	public static final int INBOUND_DETAIL_REFERENCE_LEN = 20;
	public static final int INBOUND_DETAIL_REFERENCE_START = 45;
	public static final int INBOUND_DETAIL_REFERENCE_END = 64;
	
	//Inbound Trailer
	public static final int INBOUND_TRAILER_RECORD_TYPE_LEN = 1;
	public static final int INBOUND_TRAILER_RECORD_TYPE_START = 1;
	public static final int INBOUND_TRAILER_RECORD_TYPE_END = 1;
	
	public static final int INBOUND_TRAILER_TOTAL_RECORD_LEN = 7;
	public static final int INBOUND_TRAILER_TOTAL_RECORD_START = 2;
	public static final int INBOUND_TRAILER_TOTAL_RECORD_END = 8;
	
	public static final int INBOUND_TRAILER_TOTAL_AMOUNT_LEN = 16; // 15 + 1 Signed bit
	public static final int INBOUND_TRAILER_TOTAL_AMOUNT_START = 9;
	public static final int INBOUND_TRAILER_TOTAL_AMOUNT_END = 24;
	
	//Outbound Header
	public static final int OUTBOUND_HEADER_RECORD_TYPE_LEN = 1;
	public static final int OUTBOUND_HEADER_RECORD_TYPE_START = 1;
	public static final int OUTBOUND_HEADER_RECORD_TYPE_END = 1;
	
	public static final int OUTBOUND_HEADER_SOURCE_LEN = 20;
	public static final int OUTBOUND_HEADER_SOURCE_START = 2;
	public static final int OUTBOUND_HEADER_SOURCE_END = 21;
	
	public static final int OUTBOUND_HEADER_DATETIME_LEN = 26;
	public static final int OUTBOUND_HEADER_DATETIME_START = 22;
	public static final int OUTBOUND_HEADER_DATETIME_END = 47;
	
	//Outbound Detail
	public static final int OUTBOUND_DETAIL_RECORD_TYPE_LEN = 1;
	public static final int OUTBOUND_DETAIL_RECORD_TYPE_START = 1;
	public static final int OUTBOUND_DETAIL_RECORD_TYPE_END = 1;
	
	public static final int OUTBOUND_DETAIL_ID_LEN = 9;
	public static final int OUTBOUND_DETAIL_ID_START = 2;
	public static final int OUTBOUND_DETAIL_ID_END = 10;
	
	public static final int OUTBOUND_DETAIL_STARTDATE_LEN = 8;
	public static final int OUTBOUND_DETAIL_STARTDATE_START = 11;
	public static final int OUTBOUND_DETAIL_STARTDATE_END = 18;
	
	public static final int OUTBOUND_DETAIL_ENDDATE_LEN = 8;
	public static final int OUTBOUND_DETAIL_ENDDATE_START = 19;
	public static final int OUTBOUND_DETAIL_ENDDATE_END = 26;
	
	public static final int OUTBOUND_DETAIL_WAGE_TYPE_LEN = 4;
	public static final int OUTBOUND_DETAIL_WAGE_TYPE_START = 27;
	public static final int OUTBOUND_DETAIL_WAGE_TYPE_END = 30;
	
	public static final int OUTBOUND_DETAIL_AMOUNT_LEN = 14;
	public static final int OUTBOUND_DETAIL_AMOUNT_START = 31;
	public static final int OUTBOUND_DETAIL_AMOUNT_END = 44;
	
	public static final int OUTBOUND_DETAIL_PAYROLL_MONTH_LEN = 6;
	public static final int OUTBOUND_DETAIL_PAYROLL_MONTH_START = 45;
	public static final int OUTBOUND_DETAIL_PAYROLL_MONTH_END = 50;
	
	public static final int OUTBOUND_DETAIL_REFERENCE_LEN = 20;
	public static final int OUTBOUND_DETAIL_REFERENCE_START = 51;
	public static final int OUTBOUND_DETAIL_REFERENCE_END = 70;
	
	public static final int OUTBOUND_DETAIL_STATUS_LEN = 3;
	public static final int OUTBOUND_DETAIL_STATUS_START = 71;
	public static final int OUTBOUND_DETAIL_STATUS_END =73;
	
	public static final int OUTBOUND_DETAIL_REASON_LEN = 47;
	public static final int OUTBOUND_DETAIL_REASON_START = 74;
	public static final int OUTBOUND_DETAIL_REASON_END = 120;
	
	//Outbound Trailer
	public static final int OUTBOUND_TRAILER_RECORD_TYPE_LEN = 1;
	public static final int OUTBOUND_TRAILER_RECORD_TYPE_START = 1;
	public static final int OUTBOUND_TRAILER_RECORD_TYPE_END = 1;
	
	public static final int OUTBOUND_TRAILER_TOTAL_RECORD_LEN = 7;
	public static final int OUTBOUND_TRAILER_TOTAL_RECORD_START = 2;
	public static final int OUTBOUND_TRAILER_TOTAL_RECORD_END = 8;
	
	public static final int OUTBOUND_TRAILER_TOTAL_SUC_RECORD_LEN = 7;
	public static final int OUTBOUND_TRAILER_TOTAL_SUC_RECORD_START = 9;
	public static final int OUTBOUND_TRAILER_TOTAL_SUC_RECORD_END = 15;
	
	public static final int OUTBOUND_TRAILER_TOTAL_SUC_AMOUNT_LEN = 14; //13 + 1 Signed bit
	public static final int OUTBOUND_TRAILER_TOTAL_SUC_AMOUNT_START = 16;
	public static final int OUTBOUND_TRAILER_TOTAL_SUC_AMOUNT_END = 29;
	
	public static final int OUTBOUND_TRAILER_TOTAL_REJ_RECORD_LEN = 7;
	public static final int OUTBOUND_TRAILER_TOTAL_REJ_RECORD_START = 30;
	public static final int OUTBOUND_TRAILER_TOTAL_REJ_RECORD_END = 36;
	
	public static final int OUTBOUND_TRAILER_TOTAL_REJ_AMOUNT_LEN = 14; //13 + 1 Signed bit
	public static final int OUTBOUND_TRAILER_TOTAL_REJ_AMOUNT_START = 37;
	public static final int OUTBOUND_TRAILER_TOTAL_REJ_AMOUNT_END = 50;
	
	public static final int OUTBOUND_TRAILER_FILE_LEVEL_REJ_LEN = 20;
	public static final int OUTBOUND_TRAILER_FILE_LEVEL_REJ_START = 51;
	public static final int OUTBOUND_TRAILER_FILE_LEVEL_REJ_END = 70;
	
	//Outbound Post Header
	public static final int OUTBOUND_POST_HEADER_RECORD_TYPE_LEN = 1;
	public static final int OUTBOUND_POST_HEADER_RECORD_TYPE_START = 1;
	public static final int OUTBOUND_POST_HEADER_RECORD_TYPE_END = 1;
	
	public static final int OUTBOUND_POST_HEADER_SOURCE_LEN = 20;
	public static final int OUTBOUND_POST_HEADER_SOURCE_START = 2;
	public static final int OUTBOUND_POST_HEADER_SOURCE_END = 21;
	
	public static final int OUTBOUND_POST_HEADER_DATETIME_LEN = 26;
	public static final int OUTBOUND_POST_HEADER_DATETIME_START = 22;
	public static final int OUTBOUND_POST_HEADER_DATETIME_END = 47;
	
	//Outbound Post Detail
	public static final int OUTBOUND_POST_DETAIL_RECORD_TYPE_LEN = 1;
	public static final int OUTBOUND_POST_DETAIL_RECORD_TYPE_START = 1;
	public static final int OUTBOUND_POST_DETAIL_RECORD_TYPE_END = 1;
	
	public static final int OUTBOUND_POST_DETAIL_PAYROLL_MONTH_LEN = 8;
	public static final int OUTBOUND_POST_DETAIL_PAYROLL_MONTH_START = 2;
	public static final int OUTBOUND_POST_DETAIL_PAYROLL_MOTNH_END = 9;
	
	public static final int OUTBOUND_POST_DETAIL_ID_LEN = 11;
	public static final int OUTBOUND_POST_DETAIL_ID_START = 10;
	public static final int OUTBOUND_POST_DETAIL_ID_END = 20;
	
	public static final int OUTBOUND_POST_DETAIL_REFERENCE_LEN = 20;
	public static final int OUTBOUND_POST_DETAIL_REFERENCE_START = 21;
	public static final int OUTBOUND_POST_DETAIL_REFERENCE_END = 40;
	
	public static final int OUTBOUND_POST_DETAIL_AMOUNT_LEN = 14; //13 + 1 signed bit
	public static final int OUTBOUND_POST_DETAIL_AMOUNT_START = 41;
	public static final int OUTBOUND_POST_DETAIL_AMOUNT_END = 54;
	
	public static final int OUTBOUND_POST_DETAIL_STATUS_LEN = 3;
	public static final int OUTBOUND_POST_DETAIL_STATUS_START = 55;
	public static final int OUTBOUND_POST_DETAIL_STATUS_END = 57;
	
	public static final int OUTBOUND_POST_DETAIL_REASON_LEN = 80;
	public static final int OUTBOUND_POST_DETAIL_REASON_START = 58;
	public static final int OUTBOUND_POST_DETAIL_REASON_END = 137;
	
	//Outbound Post Trailer
	public static final int OUTBOUND_POST_TRAILER_RECORD_TYPE_LEN = 1;
	public static final int OUTBOUND_POST_TRAILER_RECORD_TYPE_START = 1;
	public static final int OUTBOUND_POST_TRAILER_RECORD_TYPE_END = 1;
	
	public static final int OUTBOUND_POST_TRAILER_TOTAL_RECORD_LEN = 7;
	public static final int OUTBOUND_POST_TRAILER_TOTAL_RECORD_START = 2;
	public static final int OUTBOUND_POST_TRAILER_TOTAL_RECORD_END = 8;
	
	public static final int OUTBOUND_POST_TRAILER_TOTAL_SUC_RECORD_LEN = 7;
	public static final int OUTBOUND_POST_TRAILER_TOTAL_SUC_RECORD_START = 9;
	public static final int OUTBOUND_POST_TRAILER_TOTAL_SUC_RECORD_END = 15;
	
	public static final int OUTBOUND_POST_TRAILER_TOTAL_SUC_AMOUNT_LEN = 14; //13 + 1 Signed bit
	public static final int OUTBOUND_POST_TRAILER_TOTAL_SUC_AMOUNT_START = 16;
	public static final int OUTBOUND_POST_TRAILER_TOTAL_SUC_AMOUNT_END = 29;
	
	public static final int OUTBOUND_POST_TRAILER_TOTAL_REJ_RECORD_LEN = 7;
	public static final int OUTBOUND_POST_TRAILER_TOTAL_REJ_RECORD_START = 30;
	public static final int OUTBOUND_POST_TRAILER_TOTAL_REJ_RECORD_END = 36;
	
	public static final int OUTBOUND_POST_TRAILER_TOTAL_REJ_AMOUNT_LEN = 14; //13 + 1 Signed bit
	public static final int OUTBOUND_POST_TRAILER_TOTAL_REJ_AMOUNT_START = 37;
	public static final int OUTBOUND_POST_TRAILER_TOTAL_REJ_AMOUNT_END = 50;
	
	//Prevent instantiation
	private FieldLengthConstants () {}
}
