package com.stee.spfcore.service.accounting.impl;

public class FieldLengthConstants {
	
	/* STATIC FIELDS */
	//record types
	public static final int HEADER_RECORD_TYPE = 1;
	public static final int DETAILS_RECORD_TYPE = 2;
	public static final int PAYMENT_ADVICE_RECORD_TYPE = 4;
	public static final int TRAILER_RECORD_TYPE = 9;
	
	public static final String SERVICE_TYPE = "NORMAL"; //EXPRESS - ALL ACCOUNT MUST BE UOB 
	
	//no change fields
	public static final String BENEFICIARY_COUNTRY_CODE = "SG";
	public static final String CURRENCY = "SGD";
	public static final String ADVICE_FORMAT = "2";
	public static final String ORIGINATING_BIC_CODE = "UOVBSGSGXXX";
	
	public static final String SAG_PAYMENT_MODE_GIRO = "GIRO";
	public static final String SAG_PAYMENT_MODE_PAYNOW = "PAYNOW";
	
	public static final String PAYNOW_PROXY_TYPE_MOBILE = "MSISDN";
	public static final String PAYNOW_PROXY_TYPE_NRIC = "NRIC";
	
	public static final String MOBILE_COUNTRY_CODE_SG = "+65";
	
	//Fate File Name
	public static final int FILENAME_PAYMENT_ADVICE_LEN = 2;
	public static final int FILENAME_PAYMENT_ADVICE_START = 2;
	public static final int FILENAME_PAYMENT_ADVICE_END = 3;
	
	public static final int FILENAME_INFINITY_REF_NO_LEN = 14;
	public static final int FILENAME_INFINITY_REF_NO_START = 5;
	public static final int FILENAME_INFINITY_REF_NO_END = 18;
	
	/* Batch Header */
	public static final int HEADER_RECORD_TYPE_LEN = 1;
	public static final int HEADER_RECORD_TYPE_START = 1;
	public static final int HEADER_RECORED_TYPE_END = 1;
	
	public static final int HEADER_FILE_NAME_LEN = 10;
	public static final int HEADER_FILE_NAME_START = 2;
	public static final int HEADER_FILE_NAME_END = 11;
	
	public static final int HEADER_PAYMENT_TYPE_LEN = 1;
	public static final int HEADER_PAYMENT_TYPE_START = 12;
	public static final int HEADER_PAYMENT_TYPE_END = 12;
	
	public static final int HEADER_SERVICE_TYPE_LEN = 10;
	public static final int HEADER_SERVICE_TYPE_START = 13;
	public static final int HEADER_SERVICE_TYPE_END = 22;
	
	public static final int HEADER_PROCESSING_MODE_LEN = 1;
	public static final int HEADER_PROCESSING_MODE_START = 23;
	public static final int HEADER_PROCESSING_MODE_END = 23;
	
	public static final int HEADER_COMPANY_ID_LEN = 12;
	public static final int HEADER_COMPANY_ID_START = 24;
	public static final int HEADER_COMPANY_ID_END = 35;
	
	public static final int HEADER_BIC_CODE_LEN = 11;
	public static final int HEADER_BIC_CODE_START = 36;
	public static final int HEADER_BIC_CODE_END = 46;
	
	public static final int HEADER_AC_NO_CURRENCY_LEN = 3;
	public static final int HEADER_AC_NO_CURRENCY_START = 47;
	public static final int HEADER_AC_NO_CURRENCY_END = 49;
	
	public static final int HEADER_AC_NO_LEN = 34;
	public static final int HEADER_AC_NO_START = 50;
	public static final int HEADER_AC_NO_END = 83;
	
	public static final int HEADER_AC_NAME_LEN = 140;
	public static final int HEADER_AC_NAME_START = 84;
	public static final int HEADER_AC_NAME_END = 223;
	
	public static final int HEADER_FILE_CREATION_DATE_LEN = 8;
	public static final int HEADER_FILE_CREATION_DATE_START = 224;
	public static final int HEADER_FILE_CREATION_DATE_END = 231;
	
	public static final int HEADER_VALUE_DATE_LEN = 8;
	public static final int HEADER_VALUE_DATE_START = 232;
	public static final int HEADER_VALUE_DATE_END = 239;
	
	public static final int HEADER_ULTI_ORIGINATING_CUSTOMER_LEN = 140;
	public static final int HEADER_ULTI_ORIGINATING_CUSTOMER_START = 240;
	public static final int HEADER_ULTI_ORIGINATING_CUSTOMER_END = 379;
	
	public static final int HEADER_BULK_CUSTOMER_REFERENCE_LEN = 16;
	public static final int HEADER_BULK_CUSTOMER_REFERENCE_START = 380;
	public static final int HEADER_BULK_CUSTOMER_REFERENCE_END = 395;
	
	public static final int HEADER_SOFTWARE_LABEL_LEN = 10;
	public static final int HEADER_SOFTWARE_LABEL_START = 396;
	public static final int HEADER_SOFTWARE_LABEL_END = 405;
	
	public static final int HEADER_FILLER_LEN = 210;
	public static final int HEADER_FILLER_START = 406;
	public static final int HEADER_FILLER_END = 615;
	
	public static final int HEADER_PAYMENT_ADVICE_1_LEN = 105;
	public static final int HEADER_PAYMENT_ADVICE_1_START = 406;
	public static final int HEADER_PAYMENT_ADVICE_1_END = 510;
	
	public static final int HEADER_PAYMENT_ADVICE_2_LEN = 105;
	public static final int HEADER_PAYMENT_ADVICE_2_START = 511;
	public static final int HEADER_PAYMENT_ADVICE_2_END = 615;
	
	public static final int HEADER_PAYMENT_ADVICE_FILLER_LEN = 440;
	public static final int HEADER_PAYMENT_ADVICE_FILLER_START = 616;
	public static final int HEADER_PAYMENT_ADVICE_FILLER_END = 1055;
	
	/* Batch Details */
	public static final int DETAILS_RECORD_TYPE_LEN = 1;
	public static final int DETAILS_RECORD_TYPE_START = 1;
	public static final int DETAILS_RECORED_TYPE_END = 1;
	
	public static final int DETAILS_BIC_CODE_PROXY_TYPE_LEN = 11;
	public static final int DETAILS_BIC_CODE_PROXY_TYPE_START = 2;
	public static final int DETAILS_BIC_CODE_PROXY_TYPE_END = 12;
	
	public static final int DETAILS_AC_NO_PROXY_VALUE_LEN = 34;
	public static final int DETAILS_AC_NO_PROXY_VALUE_START = 13;
	public static final int DETAILS_AC_NO_PROXY_VALUE_END = 46;
	
	public static final int DETAILS_AC_NAME_LEN = 140;
	public static final int DETAILS_AC_NAME_START = 47;
	public static final int DETAILS_AC_NAME_END = 186;
	
	public static final int DETAILS_AC_NO_CURRENCY_LEN = 3;
	public static final int DETAILS_AC_NO_CURRENCY_START = 187;
	public static final int DETAILS_AC_NO_CURRENCY_END = 189;
	
	public static final int DETAILS_AMOUNT_LEN = 18;
	public static final int DETAILS_AMOUNT_START = 190;
	public static final int DETAILS_AMOUNT_END = 207;
	
	public static final int DETAILS_END_TO_END_ID_LEN = 35;
	public static final int DETAILS_END_TO_END_ID_START = 208;
	public static final int DETAILS_END_TO_END_ID_END = 242;
	
	public static final int DETAILS_MANDATE_ID_LEN = 35;
	public static final int DETAILS_MANDATE_ID_START = 243;
	public static final int DETAILS_MANDATE_ID_END = 277;
	
	public static final int DETAILS_PURPOSE_CODE_LEN = 4;
	public static final int DETAILS_PURPOSE_CODE_START = 278;
	public static final int DETAILS_PURPOSE_CODE_END = 281;
	
	public static final int DETAILS_REMITTANCE_INFORMATION_LEN = 140;
	public static final int DETAILS_REMITTANCE_INFORMATION_START = 282;
	public static final int DETAILS_REMITTANCE_INFORMATION_END = 421;

	public static final int DETAILS_ULT_PAYER_BENEFICIARY_NAME_LEN = 140;
	public static final int DETAILS_ULT_PAYER_BENEFICIARY_NAME_START = 422;
	public static final int DETAILS_ULT_PAYER_BENEFICIARY_NAME_END = 561;
	
	public static final int DETAILS_CUSTOMER_REFERENCE_LEN = 16;
	public static final int DETAILS_CUSTOMER_REFERENCE_START = 562;
	public static final int DETAILS_CUSTOMER_REFERENCE_END = 577;

	public static final int DETAILS_NON_PAYMENT_ADVICE_FILLER_LEN = 38;
	public static final int DETAILS_NON_PAYMENT_ADVICE_FILLER_START = 578;
	public static final int DETAILS_NON_PAYMENT_ADVICE_FILLER_END = 615;
	
	public static final int DETAILS_PAYMENT_ADVICE_INDICATOR_LEN = 1;
	public static final int DETAILS_PAYMENT_ADVICE_INDICATOR_START = 578;
	public static final int DETAILS_PAYMENT_ADVICE_INDICATOR_END = 578;

	public static final int DETAILS_DELIVERY_MODE_POST_LEN = 1;
	public static final int DETAILS_DELIVERY_MODE_POST_START = 579;
	public static final int DETAILS_DELIVERY_MODE_POST_END = 579;

	public static final int DETAILS_DELIVERY_MODE_EMAIL_LEN = 1;
	public static final int DETAILS_DELIVERY_MODE_EMAIL_START = 580;
	public static final int DETAILS_DELIVERY_MODE_EMAIL_END = 580;

	public static final int DETAILS_PAYMENT_ADVICE_FILLER_1_LEN = 1;
	public static final int DETAILS_PAYMENT_ADVICE_FILLER_1_START = 581;
	public static final int DETAILS_PAYMENT_ADVICE_FILLER_1_END = 581;
	
	public static final int DETAILS_PAYMENT_ADVICE_FILLER_2_LEN = 1;
	public static final int DETAILS_PAYMENT_ADVICE_FILLER_2_START = 582;
	public static final int DETAILS_PAYMENT_ADVICE_FILLER_2_END = 582;

	public static final int DETAILS_ADVICE_FORMAT_LEN = 1;
	public static final int DETAILS_ADVICE_FORMAT_START = 583;
	public static final int DETAILS_ADVICE_FORMAT_END = 583;

	public static final int DETAILS_BENEFICIARY_NAME_LINE_1_LEN = 35;
	public static final int DETAILS_BENEFICIARY_NAME_LINE_1_START = 584;
	public static final int DETAILS_BENEFICIARY_NAME_LINE_1_END = 618;
	
	public static final int DETAILS_BENEFICIARY_NAME_LINE_2_LEN = 35;
	public static final int DETAILS_BENEFICIARY_NAME_LINE_2_START = 619;
	public static final int DETAILS_BENEFICIARY_NAME_LINE_2_END = 653;
	
	public static final int DETAILS_BENEFICIARY_NAME_LINE_3_LEN = 35;
	public static final int DETAILS_BENEFICIARY_NAME_LINE_3_START = 654;
	public static final int DETAILS_BENEFICIARY_NAME_LINE_3_END = 688;
	
	public static final int DETAILS_BENEFICIARY_NAME_LINE_4_LEN = 35;
	public static final int DETAILS_BENEFICIARY_NAME_LINE_4_START = 689;
	public static final int DETAILS_BENEFICIARY_NAME_LINE_4_END = 723;
	
	public static final int DETAILS_BENEFICIARY_ADDRESS_LINE_1_LEN = 35;
	public static final int DETAILS_BENEFICIARY_ADDRESS_LINE_1_START = 724;
	public static final int DETAILS_BENEFICIARY_ADDRESS_LINE_1_END = 758;
	
	public static final int DETAILS_BENEFICIARY_ADDRESS_LINE_2_LEN = 35;
	public static final int DETAILS_BENEFICIARY_ADDRESS_LINE_2_START = 759;
	public static final int DETAILS_BENEFICIARY_ADDRESS_LINE_2_END = 793;
	
	public static final int DETAILS_BENEFICIARY_ADDRESS_LINE_3_LEN = 35;
	public static final int DETAILS_BENEFICIARY_ADRESS_LINE_3_START = 794;
	public static final int DETAILS_BENEFICIARY_ADDRESS_LINE_3_END = 828;
	
	public static final int DETAILS_BENEFICIARY_ADDRESS_LINE_4_LEN = 35;
	public static final int DETAILS_BENEFICIARY_ADDRESS_LINE_4_START = 829;
	public static final int DETAILS_BENEFICIARY_ADDRESS_LINE_4_END = 863;

	public static final int DETAILS_BENEFICIARY_CITY_LEN = 17;
	public static final int DETAILS_BENEFICIARY_CITY_START = 864;
	public static final int DETAILS_BENEFICIARY_CITY_END = 880;

	public static final int DETAILS_BENEFICIARY_COUNTRY_CODE_LEN = 3;
	public static final int DETAILS_BENEFICIARY_COUNTRY_CODE_START = 881;
	public static final int DETAILS_BENEFICIARY_COUNTRY_CODE_END = 883;

	public static final int DETAILS_BENEFICIARY_POSTAL_CODE_LEN = 15;
	public static final int DETAILS_BENEFICIARY_POSTAL_CODE_START = 884;
	public static final int DETAILS_BENEFICIARY_POSTAL_CODE_END = 898;
	
	public static final int DETAILS_BENEFICIARY_EMAIL_LEN = 50;
	public static final int DETAILS_BENEFICIARY_EMAIL_START = 899;
	public static final int DETAILS_BENEFICIARY_EMAIL_END = 948;

	public static final int DETAILS_BENEFICIARY_FACSIMILE_NO_LEN = 20;
	public static final int DETAILS_BENEFICIARY_FACSIMILE_NO_START = 949;
	public static final int DETAILS_BENEFICIARY_FACSIMILE_NO_END = 968;

	public static final int DETAILS_PAYER_NAME_LINE_1_LEN = 35;
	public static final int DETAILS_PAYER_NAME_LINE_1_START = 969;
	public static final int DETAILS_PAYER_NAME_LINE_1_END = 1003;

	public static final int DETAILS_PAYER_NAME_LINE_2_LEN = 35;
	public static final int DETAILS_PAYER_NAME_LINE_2_START = 1004;
	public static final int DETAILS_PAYER_NAME_LINE_2_END = 1038;

	public static final int DETAILS_PAYMENT_ADVICE_FILLER_3_LEN = 17;
	public static final int DETAILS_PAYMENT_ADVICE_FILLER_3_START = 1039;
	public static final int DETAILS_PAYMENT_ADVICE_FILLER_3_END = 1055;
	
	/* PAYMENT ADVIDE FORMAT */
	public static final int PAYMENT_ADVICE_RECORD_TYPE_LEN = 1;
	public static final int PAYMENT_ADVICE_RECORD_TYPE_START = 1;
	public static final int PAYMENT_ADVICE_RECORD_TYPE_END = 1;
	
	public static final int PAYMENT_ADVICE_SPACING_LINES_LEN = 2;
	public static final int PAYMENT_ADVICE_SPACING_LINES_START = 2;
	public static final int PAYMENT_ADVICE_SPACING_LINES_END = 3;
	
	public static final int PAYMENT_ADVICE_DETAILS_LEN = 105;
	public static final int PAYMENT_ADVICE_DETAILS_START = 4;
	public static final int PAYMENT_ADVICE_DETAILS_END = 108;
	
	public static final int PAYMENT_ADVICE_FILLER_LEN = 947;
	public static final int PAYMENT_ADVICE_FILLER_START = 109;
	public static final int PAYMENT_ADVICE_FILLER_END = 1055;
	
	//BATCH TRAILER
	public static final int TRAILER_RECORD_TYPE_LEN = 1;
	public static final int TRAILER_RECORD_TYPE_START = 1;
	public static final int TRAILER_RECORED_TYPE_END = 1;
	
	public static final int TRAILER_TOTAL_PAYMENT_AMOUNT_LEN = 18;
	public static final int TRAILER_TOTAL_PAYMENT_AMOUNT_START = 2;
	public static final int TRAILER_TOTAL_PAYMENT_AMOUNT__END = 19;
	
	public static final int TRAILER_TOTAL_NO_OF_TRANSACTIONS_LEN = 7;
	public static final int TRAILER_TOTAL_NO_OF_TRANSACTIONS_START = 20;
	public static final int TRAILER_TOTAL_NO_OF_TRANSACTIONS_END = 26;
	
	public static final int TRAILER_HASH_TOTAL_LEN = 16;
	public static final int TRAILER_HASH_TOTAL_START = 27;
	public static final int TRAILER_HASH_TOTAL_END = 42;
	
	public static final int TRAILER_PAYMENT_ADVICE_FILLER_LEN = 1013;
	public static final int TRAILER_PAYMENT_ADVICE_FILLER_START = 43;
	public static final int TRAILER_PAYMENT_ADVICE_FILLER_END = 1055;
	
	public static final int TRAILER_NON_PAYMENT_ADVICE_FILLER_LEN = 573;
	public static final int TRAILER_NON_PAYMENT_ADVICE_FILLER_START = 43;
	public static final int TRAILER_NON_PAYMENT_ADVICE_FILLER_END = 615;
	
	
	/* Batch Header from UOB*/
	public static final int UOB_HEADER_RECORD_TYPE_LEN = 1;
	public static final int UOB_HEADER_RECORD_TYPE_START = 1;
	public static final int UOB_HEADER_RECORD_TYPE_END = 1;
		
	public static final int UOB_HEADER_PAYMENT_TYPE_LEN = 1;
	public static final int UOB_HEADER_PAYMENT_TYPE_START = 2;
	public static final int UOB_HEADER_PAYMENT_TYPE_END = 2;
		
	public static final int UOB_HEADER_SERVICE_TYPE_LEN = 10;
	public static final int UOB_HEADER_SERVICE_TYPE_START = 3;
	public static final int UOB_HEADER_SERVICE_TYPE_END = 12;
		
	public static final int UOB_HEADER_PROCESSING_MODE_LEN = 1;
	public static final int UOB_HEADER_PROCESSING_MODE_START = 13;
	public static final int UOB_HEADER_PROCESSING_MODE_END = 13;
		
	public static final int UOB_HEADER_COMPANY_ID_LEN = 12;
	public static final int UOB_HEADER_COMPANY_ID_START = 14;
	public static final int UOB_HEADER_COMPANY_ID_END = 25;
		
	public static final int UOB_HEADER_BIC_CODE_LEN = 11;
	public static final int UOB_HEADER_BIC_CODE_START = 26;
	public static final int UOB_HEADER_BIC_CODE_END = 36;
		
	public static final int UOB_HEADER_AC_NO_CURRENCY_LEN = 3;
	public static final int UOB_HEADER_AC_NO_CURRENCY_START = 37;
	public static final int UOB_HEADER_AC_NO_CURRENCY_END = 39;
		
	public static final int UOB_HEADER_AC_NO_LEN = 34;
	public static final int UOB_HEADER_AC_NO_START = 40;
	public static final int UOB_HEADER_AC_NO_END = 73;
		
	public static final int UOB_HEADER_AC_NAME_LEN = 140;
	public static final int UOB_HEADER_AC_NAME_START = 74;
	public static final int UOB_HEADER_AC_NAME_END = 213;
		
	public static final int UOB_HEADER_FILE_CREATION_DATE_LEN = 8;
	public static final int UOB_HEADER_FILE_CREATION_DATE_START = 214;
	public static final int UOB_HEADER_FILE_CREATION_DATE_END = 221;
		
	public static final int UOB_HEADER_VALUE_DATE_LEN = 8;
	public static final int UOB_HEADER_VALUE_DATE_START = 222;
	public static final int UOB_HEADER_VALUE_DATE_END = 229;
		
	public static final int UOB_HEADER_ULTI_ORIGINATING_CUSTOMER_LEN = 140;
	public static final int UOB_HEADER_ULTI_ORIGINATING_CUSTOMER_START = 230;
	public static final int UOB_HEADER_ULTI_ORIGINATING_CUSTOMER_END = 369;
		
	public static final int UOB_HEADER_BULK_CUSTOMER_REFERENCE_LEN = 16;
	public static final int UOB_HEADER_BULK_CUSTOMER_REFERENCE_START = 370;
	public static final int UOB_HEADER_BULK_CUSTOMER_REFERENCE_END = 385;
		
	public static final int UOB_HEADER_SOFTWARE_LABEL_LEN = 10;
	public static final int UOB_HEADER_SOFTWARE_LABEL_START = 386;
	public static final int UOB_HEADER_SOFTWARE_LABEL_END = 395;
		
	public static final int UOB_HEADER_FILLER_LEN = 220;
	public static final int UOB_HEADER_FILLER_START = 396;
	public static final int UOB_HEADER_FILLER_END = 665;
		
	public static final int UOB_HEADER_PAYMENT_ADVICE_FILLER_LEN = 270;
	public static final int UOB_HEADER_PAYMENT_ADVICE_FILLER_START = 396;
	public static final int UOB_HEADER_PAYMENT_ADVICE_FILLER_END = 615;
	
	/* Batch Details from UOB*/
	public static final int UOB_DETAILS_RECORD_TYPE_LEN = 1;
	public static final int UOB_DETAILS_RECORD_TYPE_START = 1;
	public static final int UOB_DETAILS_RECORD_TYPE_END = 1;
	
	public static final int UOB_DETAILS_BIC_CODE_PROXY_TYPE_LEN = 11;
	public static final int UOB_DETAILS_BIC_CODE_PROXY_TYPE_START = 2;
	public static final int UOB_DETAILS_BIC_CODE_PROXY_TYPE_END = 12;
	
	public static final int UOB_DETAILS_AC_NO_PROXY_VALUE_LEN = 34;
	public static final int UOB_DETAILS_AC_NO_PROXY_VALUE_START = 13;
	public static final int UOB_DETAILS_AC_NO_PROXY_VALUE_END = 46;
	
	public static final int UOB_DETAILS_AC_NAME_LEN = 140;
	public static final int UOB_DETAILS_AC_NAME_START = 47;
	public static final int UOB_DETAILS_AC_NAME_END = 186;
	
	public static final int UOB_DETAILS_AC_NO_CURRENCY_LEN = 3;
	public static final int UOB_DETAILS_AC_NO_CURRENCY_START = 187;
	public static final int UOB_DETAILS_AC_NO_CURRENCY_END = 189;
	
	public static final int UOB_DETAILS_AMOUNT_LEN = 18;
	public static final int UOB_DETAILS_AMOUNT_START = 190;
	public static final int UOB_DETAILS_AMOUNT_END = 207;
	
	public static final int UOB_DETAILS_END_TO_END_ID_LEN = 35;
	public static final int UOB_DETAILS_END_TO_END_ID_START = 208;
	public static final int UOB_DETAILS_END_TO_END_ID_END = 242;
	
	public static final int UOB_DETAILS_MANDATE_ID_LEN = 35;
	public static final int UOB_DETAILS_MANDATE_ID_START = 243;
	public static final int UOB_DETAILS_MANDATE_ID_END = 277;
	
	public static final int UOB_DETAILS_PURPOSE_CODE_LEN = 4;
	public static final int UOB_DETAILS_PURPOSE_CODE_START = 278;
	public static final int UOB_DETAILS_PURPOSE_CODE_END = 281;
	
	public static final int UOB_DETAILS_REMITTANCE_INFORMATION_LEN = 140;
	public static final int UOB_DETAILS_REMITTANCE_INFORMATION_START = 282;
	public static final int UOB_DETAILS_REMITTANCE_INFORMATION_END = 421;

	public static final int UOB_DETAILS_ULT_PAYER_BENEFICIARY_NAME_LEN = 140;
	public static final int UOB_DETAILS_ULT_PAYER_BENEFICIARY_NAME_START = 422;
	public static final int UOB_DETAILS_ULT_PAYER_BENEFICIARY_NAME_END = 561;
		
	public static final int UOB_DETAILS_CUSTOMER_REFERENCE_LEN = 16;
	public static final int UOB_DETAILS_CUSTOMER_REFERENCE_START = 562;
	public static final int UOB_DETAILS_CUSTOMER_REFERENCE_END = 577;

	public static final int UOB_DETAILS_RETURN_CODE_LEN = 4;
	public static final int UOB_DETAILS_RETURN_CODE_START = 578;
	public static final int UOB_DETAILS_RETURN_CODE_END = 581;
	
	public static final int UOB_DETAILS_CLEAR_FATE_LEN = 1;
	public static final int UOB_DETAILS_CLEAR_FATE_START = 582;
	public static final int UOB_DETAILS_CLEAR_FATE_END = 582;
	
	public static final int UOB_DETAILS_NON_PAYMENT_ADVICE_FILLER_LEN = 33;
	public static final int UOB_DETAILS_NON_PAYMENT_ADVICE_FILLER_START = 583;
	public static final int UOB_DETAILS_NON_PAYMENT_ADVICE_FILLER_END = 615;
		
	public static final int UOB_DETAILS_REASON_NOT_SENT_LEN = 50;
	public static final int UOB_DETAILS_REASON_NOT_SENT_START = 583;
	public static final int UOB_DETAILS_REASON_NOT_SENT_END = 632;

	public static final int UOB_DETAILS_PAYMENT_ADVICE_FILLER_LEN = 33;
	public static final int UOB_DETAILS_PAYMENT_ADVICE_FILLER_START = 633;
	public static final int UOB_DETAILS_PAYMENT_ADVICE_FILLER_END = 665;
	
	/* BATCH TRAILER from UOB*/
	public static final int UOB_TRAILER_RECORD_TYPE_LEN = 1;
	public static final int UOB_TRAILER_RECORD_TYPE_START = 1;
	public static final int UOB_TRAILER_RECORD_TYPE_END = 1;
	
	public static final int UOB_TRAILER_TOTAL_AMOUNT_LEN = 18;
	public static final int UOB_TRAILER_TOTAL_AMOUNT_START = 2;
	public static final int UOB_TRAILER_TOTAL_AMOUNT_END = 19;
	
	public static final int UOB_TRAILER_TOTAL_NO_TRANSACTION_LEN = 7;
	public static final int UOB_TRAILER_TOTAL_NO_TRANSACTION_START = 20;
	public static final int UOB_TRAILER_TOTAL_NO_TRANSACTION_END = 26;
	
	public static final int UOB_TRAILER_TOTAL_ACCEPTED_AMOUNT_LEN = 18;
	public static final int UOB_TRAILER_TOTAL_ACCEPTED_AMOUNT_START = 27;
	public static final int UOB_TRAILER_TOTAL_ACCEPTED_AMOUNT_END = 44;
	
	public static final int UOB_TRAILER_TOTAL_ACCEPTED_NO_TRANSACTION_LEN = 7;
	public static final int UOB_TRAILER_TOTAL_ACCEPTED_NO_TRANSACTION_START = 45;
	public static final int UOB_TRAILER_TOTAL_ACCEPTED_NO_TRANSACTION_END = 51;
	
	public static final int UOB_TRAILER_TOTAL_REJECTED_AMOUNT_LEN = 18;
	public static final int UOB_TRAILER_TOTAL_REJECTED_AMOUNT_START = 52;
	public static final int UOB_TRAILER_TOTAL_REJECTED_AMOUNT_END = 69;
	
	public static final int UOB_TRAILER_TOTAL_REJECTED_NO_TRANSACTION_LEN = 7;
	public static final int UOB_TRAILER_TOTAL_REJECTED_NO_TRANSACTION_START = 70;
	public static final int UOB_TRAILER_TOTAL_REJECTED_NO_TRANSACTION_END = 76;
	
	public static final int UOB_TRAILER_TOTAL_PENDING_AMOUNT_LEN = 18;
	public static final int UOB_TRAILER_TOTAL_PENDING_AMOUNT_START = 77;
	public static final int UOB_TRAILER_TOTAL_PENDING_AMOUNT_END = 94;
	
	public static final int UOB_TRAILER_TOTAL_PENDING_NO_TRANSACTION_LEN = 7;
	public static final int UOB_TRAILER_TOTAL_PENDING_NO_TRANSACTION_START = 95;
	public static final int UOB_TRAILER_TOTAL_PENDING_NO_TRANSACTION_END = 101;
	
	public static final int UOB_TRAILER_TOTAL_STOPPED_AMOUNT_LEN = 18;
	public static final int UOB_TRAILER_TOTAL_STOPPED_AMOUNT_START = 102;
	public static final int UOB_TRAILER_TOTAL_STOPPED_AMOUNT_END = 119;
	
	public static final int UOB_TRAILER_TOTAL_STOPPED_NO_TRANSACTION_LEN = 7;
	public static final int UOB_TRAILER_TOTAL_STOPPED_NO_TRANSACTION_START = 120;
	public static final int UOB_TRAILER_TOTAL_STOPPED_NO_TRANSACTION_END = 126;
	
	public static final int UOB_TRAILER_NON_PAYMENT_ADVICE_FILLER_LEN = 489;
	public static final int UOB_TRAILER_NON_PAYMENT_ADVICE_FILLER_START = 127;
	public static final int UOB_TRAILER_NON_PAYMENT_ADVICE_FILLER_END = 615;
	
	public static final int UOB_TRAILER_PAYMENT_ADVICE_FILLER_LEN = 539;
	public static final int UOB_TRAILER_PAYMENT_ADVICE_FILLER_START = 127;
	public static final int UOB_TRAILER_PAYMENT_ADVICE_FILLER_END = 665;
	
	//ACCPAC
	public static final String ACCPAC_HEADER_BATCH_ID = "BATCH ID";
	public static final String ACCPAC_HEADER_ENTRY_NUMBER = "ENTRY NUMBER";
	public static final String ACCPAC_HEADER_ACC_CODE = "ACC CODE";
	public static final String ACCPAC_HEADER_BATCH_DESC = "BATCH DESC";
	public static final String ACCPAC_HEADER_PAYMENT_MODE = "PAYMENT MODE";
	public static final String ACCPAC_HEADER_PAYMENT_REFERENCE = "PAYMENT REFERENCE";
	public static final String ACCPAC_HEADER_ENTRY_DESCRIPTION ="ENTRY DESCIPTION";
	public static final String ACCPAC_HEADER_DESTINATION_BANK_BRANCH = "DESTINATION BANK BRANCH"; //DESCRIPTION BANK BRANCH?
	public static final String ACCPAC_HEADER_DESTINATION_ACC_NO = "DESTINATION ACC NO";
	public static final String ACCPAC_HEADER_DESTINATION_ACC_NAME = "DESTINATION ACC NAME";
	public static final String ACCPAC_HEADER_LINE_DESCRIPTION = "LINE DESCIPTION";
	public static final String ACCPAC_HEADER_AMOUNT = "AMOUNT";
	public static final String ACCPAC_HEADER_GST_AMOUNT =  "GST AMOUNT";
	public static final String ACCPAC_HEADER_GST_INCLUSIVE =  "GST INCLUSIVE";
	public static final String ACCPAC_HEADER_GST_RATE =  "GST RATE";
	public static final String ACCPAC_HEADER_TRANSACTION_DATE =  "TRANSACTION DATE";
	public static final String ACCPAC_HEADER_CURRENCY =  "CURRENCY";
	public static final String ACCPAC_HEADER_EX_RATE =  "EX RATE";
	public static final String ACCPAC_HEADER_BANK_CODE =  "BANK CODE";
	public static final String ACCPAC_HEADER_TRANSACTION_TYPE =  "TRANSACTION TYPE";
	
	public static final String ACCPAC_SAG_FILENAME =  "LeeFoundation-";
	public static final String ACCPAC_BENE_FILENAME =  "WelfareDiv-";
	
	//SAG
	public static final String ACCPAC_SAG_ACC_CODE_SA = "6010";
	public static final String ACCPAC_SAG_ACC_CODE_SAA = "6020";
	public static final String ACCPAC_SAG_ACC_CODE_SG = "6030";
	
	public static final String ACCPAC_SAG_BATCH_DESC_SA = "Study Award(SA)";
	public static final String ACCPAC_SAG_BATCH_DESC_SAA = "Scholastic Achievement Award(SAA)";
	public static final String ACCPAC_SAG_BATCH_DESC_SG = "Study Grant(SG)";
	
	public static final String ACCPAC_SAG_PAYMENT_MODE_GIRO = "GIRO";
	public static final String ACCPAC_SAG_PAYMENT_MODE_PAYNOW = "PAYNOW";
	public static final String ACCPAC_SAG_PAYMENT_MODE_CHEQUE = "CHQ";
	
	public static final String ACCPAC_SAG_GST_AMOUNT = "0.00";
	public static final String ACCPAC_SAG_GST_INCLUSIVE = "No";
	public static final String ACCPAC_SAG_GST_RATE = "0.00";
	
	public static final String ACCPAC_SAG_CURRENCY = "SGD";
	public static final String ACCPAC_SAG_EX_RATE = "1.00";
	public static final String ACCPAC_SAG_BANK_CODE = "UOB";
	
	//BENEFITS
	public static final String ACCPAC_BENE_ACC_CODE_BEREAVEMENT = "8015";
	public static final String ACCPAC_BENE_ACC_CODE_NEWBORN = "8035";
	public static final String ACCPAC_BENE_ACC_CODE_WEDDING = "8030";
	
	public static final String ACCPAC_BENE_BATCH_DESC_BEREAVEMENT = "Bereavement Grant";
	public static final String ACCPAC_BENE_BATCH_DESC_NEWBORN = "Newborn Grant";
	public static final String ACCPAC_BENE_BATCH_DESC_WEDDING = "Wedding Grant";
	
	public static final String ACCPAC_BENE_PAYMENT_MODE_DEPT_EXT_MHA = "GIRO";
	public static final String ACCPAC_BENE_PAYMENT_MODE = "SALARY";
	
	public static final String ACCPAC_BENE_DEST_BANK_GIRO = "7375";
	public static final String ACCPAC_BENE_DEST_BANK_BRANCH_NON_GIRO = "NA";
	public static final String ACCPAC_BENE_DEST_ACC_NO_NON_GIRO = "NA";
	public static final String ACCPAC_BENE_DEST_ACC_NAME_NON_GIRO = "NA";
	
	public static final String ACCPAC_BENE_GST_AMOUNT = "0.00";
	public static final String ACCPAC_BENE_GST_INCLUSIVE = "No";
	public static final String ACCPAC_BENE_GST_RATE = "0.00";
	public static final String ACCPAC_BENE_CURRENCY = "SGD";
	public static final String ACCPAC_BENE_EX_RATE = "1.00";
	public static final String ACCPAC_BENE_BANK_CODE = "UOB";
	public static final String ACCPAC_BENE_TRANSACTION_TYPE = "PV";
	
	//Prevent instantiation
	private FieldLengthConstants () {}
}
