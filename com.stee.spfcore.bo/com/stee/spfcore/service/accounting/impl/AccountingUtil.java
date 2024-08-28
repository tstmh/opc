package com.stee.spfcore.service.accounting.impl;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.stee.spfcore.model.accounting.AccpacDetail;
import com.stee.spfcore.model.accounting.PaymentAdviceDetail;
import com.stee.spfcore.model.accounting.PaymentStatus;
import com.stee.spfcore.model.accounting.ReturnCode;
import com.stee.spfcore.model.accounting.UOBBatchDetail;
import com.stee.spfcore.model.accounting.UOBBatchHeader;
import com.stee.spfcore.model.accounting.UOBBatchTrailer;
import com.stee.spfcore.model.benefits.ApprovalRecord;
import com.stee.spfcore.model.internal.SAGPaymentType;
import com.stee.spfcore.model.sag.SAGBatchFileRecord;
import com.stee.spfcore.utils.FileUtil;
import com.stee.spfcore.vo.sag.SAGApplicationsApprovedForAwardWithPayment;

public class AccountingUtil {

	private AccountingUtil(){}
	private static final Logger LOGGER = Logger.getLogger(AccountingUtil.class.getName());
	
	private static final char CSV_DELIMITER = ',';
	private static final String SPACE = " ";
	private static final String DATE_FORMAT = "yyyyMMdd";
	private static final String TIME_FORMAT = "HHmmss";


	public static String rightPadSpaces (String data, int totalLength) {
		data = (data == null) ? "" : data;
		
		//remove line separator (if any)
		data = data.replace("\\r\\n|\\r|\\n", SPACE);
		
		if (data.length() >= totalLength) {
			data = data.substring(0, totalLength);
			return data;
		}
		
		return StringUtils.rightPad(data, totalLength, SPACE);
	}
	
	public static String leftPadZeros(String data, int totalLength) {
		data = (data == null) ? "" : data;
		
		//remove line separator (if any)
		data = data.replace("\\r\\n|\\r|\\n", SPACE);
		
		if (data.length() >= totalLength) {
			data = data.substring(0, totalLength);
			return data;
		}
		return StringUtils.leftPad(data, totalLength, "0");
	}
	
	public static String getFateFileName(boolean hasPaymentAdvice, Date date, int seqNo) {
		
		if (seqNo > 99){
			LOGGER.warning( "getFateFileName seqNo > 99 ");
			return null;
		}
		
		StringBuilder builder = new StringBuilder();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMM");
		String NN = (seqNo < 10) ? ("0" + Integer.toString(seqNo)): (Integer.toString(seqNo));
		
		builder.append("U");
		if (hasPaymentAdvice) {
			builder.append("GA");
		} else {
			builder.append("GB");
		}
		builder.append("I");
		builder.append(simpleDateFormat.format(date));
		builder.append(NN);
		
		return builder.toString();
	}
	
	public static String getFateFolderName(Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return simpleDateFormat.format(date);
	}
	
	public static String getAccpacSAGFolderName(Date date) {
		SimpleDateFormat dayFormat = new SimpleDateFormat(DATE_FORMAT);
		SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);

		return FieldLengthConstants.ACCPAC_SAG_FILENAME + dayFormat.format(date) + "-" + timeFormat.format(date);
	}
	
	public static String getAccpacBeneFolderName(Date date) {
		SimpleDateFormat dayFormat = new SimpleDateFormat(DATE_FORMAT);
		SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);

		return FieldLengthConstants.ACCPAC_BENE_FILENAME + dayFormat.format(date) + "-" + timeFormat.format(date);
	}
	
	public static String getProcessingMode(boolean giro, String processingType){
		String processingMode = null;
		if (giro){
			processingMode = (Objects.equals(processingType, "FAST")) ? "I" : "B";
		} else {
			processingMode = (Objects.equals(processingType, "FAST")) ? "F" : "G";
		}
		return processingMode;
	}
	
	public static void deleteFiles (File folder){
		
		if (folder == null){
			return;
		}
		if ( LOGGER.isLoggable( Level.INFO ) ) {
			LOGGER.info(String.format("deleteFiles %s ", folder.getAbsolutePath()));
		}
		if (FileUtil.deleteDirectoryRecursively(folder) && LOGGER.isLoggable( Level.INFO ) ){
			LOGGER.info( String.format( "deleted file: %s", folder.getAbsolutePath()));
		}
	}
	
	public static String getDateTimeFormatFromDate (Date date) {
		
		if (date == null){
			return null;
		}
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		String sDate = simpleDateFormat.format(date);
		if ( LOGGER.isLoggable( Level.INFO ) ) {
			LOGGER.info(String.format("getDateTimeFormatFromDate: Date:%s , String:%s", date, sDate));
		}
		return sDate;
	}
	
	public static Date getDateTimeFormatFromString (String sDate) throws ParseException {
		
		if (sDate == null || sDate.trim().equals("")){
			return null;
		}
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		Date date = simpleDateFormat.parse(sDate);
		if ( LOGGER.isLoggable( Level.INFO ) ) {
			LOGGER.info(String.format("getDateTimeFormatFromString: String:%s , Date:%s", sDate, date));
		}
		return date;
	}
	
	public static boolean checkValueDate(Date valueDate){
		
		if (valueDate == null){
			return false;
		}
		
		boolean check = false;
		Date currentDate = new Date();
		
		//value date must not be 30 calendar days later then the current date
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, 30);
		Date addedDate = c.getTime();
		if ( valueDate.after(currentDate) && valueDate.after(addedDate) ){
			check = true;
		}
		
		//for GIRO Normal, Value Date cannot fall on Saturday, Sunday, PH
		if (valueDate.getDay() == Calendar.SATURDAY || valueDate.getDay() == Calendar.SUNDAY) {
			return check;
		}
		//checking for weekends
        check = true;

        return check;
	}
	
	public static String getAmountFormatFromDouble(double amount){
		
		//round to 2dp
		BigDecimal bd = BigDecimal.valueOf(amount);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		amount = bd.doubleValue() * 100;
		
		String sAmount = String.format("%f", amount).split("\\.")[0];

		if ( LOGGER.isLoggable( Level.INFO ) ) {
		LOGGER.info( String.format( "getAmountFormatFromDouble: originalAmount:%s , formattedString:%s", amount, sAmount));
		}
		return sAmount;
	}
	
	public static double getAmountFormatFromString(String sAmount){
		
		if (sAmount == null){
			return 0;
		}
		
		double amount = Double.parseDouble(sAmount);
		amount /= 100;

		if ( LOGGER.isLoggable( Level.INFO ) ) {
			LOGGER.info(String.format("getAmountFormatFromString: originalString:%s , formattedDouble:%s", sAmount, amount));
		}
		return amount;
	}
	
	public static boolean checkAmount(double amount, String processingMode){
			
		boolean valid = true;
		//0 amount is not allowed
		if (amount == 0) { 
			valid = false; 
		}
		//For processing mode via FAST (I,F), max amount allowed is 200k
		if ((Objects.equals(processingMode, "I") || Objects.equals(processingMode, "F")) && (amount > 20000000)){
			valid = false;
		} 
		
		return valid;
	}
	
	public static List<String> splitStringBySize(String text, int sizeLimit){
		
		List<String> list = new ArrayList<>();

		//text is null, return empty list
		if (text == null){
			return list;
		}
		
		//text is within limit, return text in list.
		if (text.trim().length() <= sizeLimit){
			list.add(text.trim());
			return list;
		}
		
		//regex to split string into fixed length rows, without breaking the words.
		Pattern regex = Pattern.compile("\\b.{1," + (sizeLimit-1) + "}\\b\\W?");
		Matcher regexMatcher = regex.matcher(text.trim());
		while (regexMatcher.find()){
			if (regexMatcher.group().length() != 0){
				list.add(regexMatcher.group().trim());
			}
		}
		
		return list; 
	}
	
	public static int calculateFieldCheckSummary(String field){
		
		if (field == null){
			return 0;
		}
		
		int sum = 0;
		int length = field.length();
		if (length > 0){
			for (int col=0; col<length; col++){
				int ascii = field.charAt(col);
				int times = (col + 1) * ascii;
				sum += times;
			}
		}
		return sum;
	}
	
	public static String getSubString(String data, int startIndex, int endIndex){
		
		if (startIndex > 0 && endIndex > 0){
			int start = startIndex - 1;
			
			if (start > endIndex && start > data.length()){
				if ( LOGGER.isLoggable( Level.INFO ) ) {
					LOGGER.info(String.format("getSubString: start %s is more than end %s & data length %s", start, endIndex, data.length()));
				}
				return null;
			}
			
			String subData = data.substring(start, Math.min(data.length(), endIndex));
			return subData.trim();
		}

		if ( LOGGER.isLoggable( Level.INFO ) ) {
			LOGGER.info(String.format("getSubString 0 found!! startIndex: %s endIndex: %s", startIndex, endIndex));
		}
		return null;
	}
	
	public static List<String> getPaymentModeList(String paymentMode){
		List<String> paymentModeList = new ArrayList<>();
		if (paymentMode != null){
			if (paymentMode.contains(SAGPaymentType.GIRO.name())){
				paymentModeList.add(SAGPaymentType.GIRO.name());
			}
			if (paymentMode.contains(SAGPaymentType.PAYNOW.name())){
				paymentModeList.add(SAGPaymentType.PAYNOW.name());
			}
		}

		if ( LOGGER.isLoggable( Level.INFO ) ) {
			LOGGER.info(String.format("getPaymentModeList: paymentMode:%s list:%s", paymentMode, paymentModeList.size()));
		}
		return paymentModeList;
	}
	
	private static String[] splitStringByLineBreak(String line){
		
		if (line == null){
			return new String[0];
		}
		
		String delimiter = "\n";
		String[] lines = line.split(delimiter);

		if ( LOGGER.isLoggable( Level.INFO ) ) {
			LOGGER.info(String.format("splitStringByLineBreak: splitSize:%s", lines.length));
		}
		return lines;
	}
	
	public static List<PaymentAdviceDetail> getPaymentAdviceDetailsFromString(String sPaymentAdviceDetail){
		
		if(sPaymentAdviceDetail == null || sPaymentAdviceDetail.equals("")){
			return null;
		}
		if ( LOGGER.isLoggable( Level.INFO ) ) {
			LOGGER.info(String.format("start setPaymentAdviceDetailsFromString >> sPaymentAdviceDetail: %s", sPaymentAdviceDetail));
		}
		
		List<PaymentAdviceDetail> paymentAdviceDetailList = new ArrayList<>();
		
		String regexWithDelimiter = "((?<=%1$s)|(?=%1$s))"; //split [a,$,$,b,$,c] //"(?=%1$s)" split [a,$,$b,$c]
		String delimiter = "\n";
	
		String[] lines = sPaymentAdviceDetail.split(String.format(regexWithDelimiter, delimiter));
		
		int lineBreaks = 0;
		String lineString = "";
		
		for (String line: lines){
			if (line.length() != 0){
				lineString = line.replace(delimiter, "").trim();
				if ( LOGGER.isLoggable( Level.INFO ) ) {
					LOGGER.info(String.format("lineString:'%s' empty:%s", lineString, lineString.isEmpty()));
				}
				
				if (lineString.equals("") || lineString.isEmpty()){
					lineBreaks++;
				} else {
					//reached text
					lineBreaks = (lineBreaks > 0) ? lineBreaks -1 : lineBreaks;
					
					PaymentAdviceDetail paymentAdviceDetail = new PaymentAdviceDetail();
					paymentAdviceDetail.setSpacingLinesBefore(lineBreaks);
					paymentAdviceDetail.setDetails(lineString);
					
					LOGGER.info( String.format( "paymentAdviceDetail>> spacingLinesBefore:%s line:'%s'",paymentAdviceDetail.getSpacingLinesBefore(),paymentAdviceDetail.getDetails()));
					
					paymentAdviceDetailList.add(paymentAdviceDetail);
					
					//reset
					lineBreaks = 0;
                }
			}
		}
		
		return paymentAdviceDetailList;
	}
	
	public static String replaceSpecialChar(String text, String replacement){
		//special char handling in UOB (to be removed/replaced with space)
		// [ ] { } | ~ * ! & ' @ # $ % ^ _ = < > \ " 
		if(text == null){
			return null;
		}
		
		String regexExp = "[`~!@#=$%^&*_\\[\\]\\\\\'/{}|\"<>?]";
		String formattedText = text.replaceAll(regexExp, replacement); //replace with space

		if ( LOGGER.isLoggable( Level.INFO ) ) {
			LOGGER.info(String.format("replaceSpecialChar >> text:%s formatted:%s", text, formattedText));
		}
		return formattedText.trim();
	}
	
	public static String removeAllButDigits(String text){
		if(text == null){
			return null;
		}
		
		String regexExp = "\\D+";
		String formattedText = text.replaceAll(regexExp, "");

		if ( LOGGER.isLoggable( Level.INFO ) ) {
			LOGGER.info(String.format("removeAllButDigits >> text:%s formatted:%s", text, formattedText));
		}
		return formattedText.trim();
	}
	
	public static String appendCountryCodeToMobileNo(String mobileNo, String countryCode){
		if(mobileNo == null){
			return null;
		}
		
		if (mobileNo.trim().length() == 0){
			return null;
		}
		
		StringBuilder sb = new StringBuilder(mobileNo);
		sb.insert(0, countryCode);

		if ( LOGGER.isLoggable( Level.INFO ) ) {
			LOGGER.info(String.format("appendCountryCodeToMobileNo >> mobileNo:%s formatted:%s", mobileNo, sb.toString().trim()));
		}
		
		return sb.toString().trim();
	}

	public static String getReturnFateFileNameBIBPlus (boolean hasPaymentAdvice, Date date, int seqNo) {
		if (seqNo > 99){
			return null;
		}
		
		StringBuilder builder = new StringBuilder();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMM");
		String NN = (seqNo < 10) ? ("0" + Integer.toString(seqNo)): (Integer.toString(seqNo));
		
		builder.append("U");
		if (hasPaymentAdvice) {
			builder.append("GA");
		} else {
			builder.append("GM");
		}
		builder.append("O");
		builder.append(simpleDateFormat.format(date));
		
		int dateLength = simpleDateFormat.format(date).length();
		int length = 14 - dateLength;
		builder.append(leftPadZeros(NN, length));
		
		return builder.toString();
	}
	
	public static String generateBatchHeader(boolean hasPaymentAdvice, String fileName, String paymentType, String serviceType, String processingMode, 
			String originatingBicCode, String accountNumber, String accountName, Date valueDate, String ultiOriginatingCustomer, 
			String bulkCustomerRef, String paymentAdviceHeader){
		
		StringBuilder builder = new StringBuilder();
		
		builder.append(Integer.toString(FieldLengthConstants.HEADER_RECORD_TYPE));
		builder.append(rightPadSpaces(fileName, FieldLengthConstants.HEADER_FILE_NAME_LEN));
		builder.append(rightPadSpaces(paymentType, FieldLengthConstants.HEADER_PAYMENT_TYPE_LEN));
		builder.append(rightPadSpaces(serviceType, FieldLengthConstants.HEADER_SERVICE_TYPE_LEN));
		builder.append(rightPadSpaces(processingMode, FieldLengthConstants.HEADER_PROCESSING_MODE_LEN));
		builder.append(rightPadSpaces(null, FieldLengthConstants.HEADER_COMPANY_ID_LEN));
		builder.append(rightPadSpaces(originatingBicCode, FieldLengthConstants.HEADER_BIC_CODE_LEN));
		builder.append(rightPadSpaces(FieldLengthConstants.CURRENCY, FieldLengthConstants.HEADER_AC_NO_CURRENCY_LEN));
		builder.append(rightPadSpaces(accountNumber, FieldLengthConstants.HEADER_AC_NO_LEN));
		builder.append(rightPadSpaces(accountName, FieldLengthConstants.HEADER_AC_NAME_LEN));
		builder.append(getDateTimeFormatFromDate(new Date()));
		builder.append(getDateTimeFormatFromDate(valueDate));
		
		ultiOriginatingCustomer = (Objects.equals(ultiOriginatingCustomer, accountName)) ? null : ultiOriginatingCustomer;
		builder.append(rightPadSpaces(ultiOriginatingCustomer, FieldLengthConstants.HEADER_ULTI_ORIGINATING_CUSTOMER_LEN));
		
		builder.append(rightPadSpaces(bulkCustomerRef, FieldLengthConstants.HEADER_BULK_CUSTOMER_REFERENCE_LEN));
		builder.append(rightPadSpaces(null, FieldLengthConstants.HEADER_SOFTWARE_LABEL_LEN));
		
		if (!hasPaymentAdvice){
			builder.append(rightPadSpaces(null, FieldLengthConstants.HEADER_FILLER_LEN));
		} else {
			
			String paymentAdviceHeader1 = null;
			String paymentAdviceHeader2 = null;
			
			String[] paymentAdviceLines = splitStringByLineBreak(paymentAdviceHeader);
            if (paymentAdviceLines.length == 1) { //no line breaks, split string by size
                List<String> paymentAdviceHeaderList = splitStringBySize(paymentAdviceLines[0], FieldLengthConstants.HEADER_PAYMENT_ADVICE_1_LEN);
                paymentAdviceHeader1 = ((!paymentAdviceHeaderList.isEmpty()) ? paymentAdviceHeaderList.get(0) : null);
                paymentAdviceHeader2 = ((paymentAdviceHeaderList.size() > 1) ? paymentAdviceHeaderList.get(1) : null);
            } else if (paymentAdviceLines.length > 1) {
                paymentAdviceHeader1 = paymentAdviceLines[0];
                paymentAdviceHeader2 = paymentAdviceLines[1];
            }

            builder.append(rightPadSpaces(paymentAdviceHeader1, FieldLengthConstants.HEADER_PAYMENT_ADVICE_1_LEN));
			builder.append(rightPadSpaces(paymentAdviceHeader2, FieldLengthConstants.HEADER_PAYMENT_ADVICE_2_LEN));
			builder.append(rightPadSpaces(null, FieldLengthConstants.HEADER_PAYMENT_ADVICE_FILLER_LEN));
		}
		
		return builder.toString();
	} 
	
	public static String generateBatchDetails(boolean paymentAdviceIndicator, boolean deliveryModePost, boolean deliveryModeEmail, 
			String endToEndID, String mandateId, String purposeCode, String remittanceInformation, 
			String ultiPayerBeneName, String customerRef, String payerName, 
			String bicCodeProxyType, String accNoProxyValue, String accName, String amount,
			String beneficiaryCity, String beneficiaryPostal, 
			String beneficiaryEmailAddress, String beneficiaryMailingAddress){
		StringBuilder builder = new StringBuilder();
		
		builder.append(Integer.toString(FieldLengthConstants.DETAILS_RECORD_TYPE));
		builder.append(rightPadSpaces(bicCodeProxyType, FieldLengthConstants.DETAILS_BIC_CODE_PROXY_TYPE_LEN));
		builder.append(rightPadSpaces(accNoProxyValue, FieldLengthConstants.DETAILS_AC_NO_PROXY_VALUE_LEN));
		builder.append(rightPadSpaces(accName, FieldLengthConstants.DETAILS_AC_NAME_LEN));
		builder.append(rightPadSpaces(FieldLengthConstants.CURRENCY, FieldLengthConstants.DETAILS_AC_NO_CURRENCY_LEN));
		
		builder.append(leftPadZeros(amount, FieldLengthConstants.DETAILS_AMOUNT_LEN));
		
		builder.append(rightPadSpaces(endToEndID, FieldLengthConstants.DETAILS_END_TO_END_ID_LEN));
		builder.append(rightPadSpaces(mandateId, FieldLengthConstants.DETAILS_MANDATE_ID_LEN));
		builder.append(rightPadSpaces(purposeCode, FieldLengthConstants.DETAILS_PURPOSE_CODE_LEN));
		builder.append(rightPadSpaces(remittanceInformation, FieldLengthConstants.DETAILS_REMITTANCE_INFORMATION_LEN));

		builder.append(rightPadSpaces(null, FieldLengthConstants.DETAILS_ULT_PAYER_BENEFICIARY_NAME_LEN));
		builder.append(rightPadSpaces(customerRef, FieldLengthConstants.DETAILS_CUSTOMER_REFERENCE_LEN));
		
		if (!paymentAdviceIndicator){
			builder.append(rightPadSpaces(null, FieldLengthConstants.DETAILS_NON_PAYMENT_ADVICE_FILLER_LEN));
			
		} else {
			builder.append( (paymentAdviceIndicator) ? "Y" : "N");
			builder.append( (paymentAdviceIndicator && deliveryModePost) ? "P" : SPACE);
			builder.append( (paymentAdviceIndicator && deliveryModeEmail) ? "E" : SPACE);
			
			builder.append(rightPadSpaces(null, FieldLengthConstants.DETAILS_PAYMENT_ADVICE_FILLER_1_LEN));
			builder.append(rightPadSpaces(null, FieldLengthConstants.DETAILS_PAYMENT_ADVICE_FILLER_2_LEN));
			
			builder.append(rightPadSpaces((paymentAdviceIndicator) ? FieldLengthConstants.ADVICE_FORMAT : null, FieldLengthConstants.DETAILS_ADVICE_FORMAT_LEN));
			
			String beneName1 = null;
			String beneName2 = null;
			String beneName3 = null;
			String beneName4 = null;
			String[] beneNameLines = splitStringByLineBreak(ultiPayerBeneName);
			if (beneNameLines != null){

				if (beneNameLines.length == 1){ //no line breaks, split string by size
					List<String> beneNameList = splitStringBySize(beneNameLines[0], FieldLengthConstants.DETAILS_BENEFICIARY_NAME_LINE_1_LEN);
					beneName1 = ((!beneNameList.isEmpty()) ? beneNameList.get(0): null);
					beneName2 = ((beneNameList.size()>1) ? beneNameList.get(1): null);
					beneName3 = ((beneNameList.size()>2) ? beneNameList.get(2): null);
					beneName4 = ((beneNameList.size()>3) ? beneNameList.get(3): null);
					
				} else {
					beneName1 = ((beneNameLines.length>0) ? beneNameLines[0]: null);
					beneName2 = ((beneNameLines.length>1) ? beneNameLines[1]: null);
					beneName3 = ((beneNameLines.length>2) ? beneNameLines[2]: null);
					beneName4 = ((beneNameLines.length>3) ? beneNameLines[3]: null);
				}
			}
			builder.append(rightPadSpaces(beneName1, FieldLengthConstants.DETAILS_BENEFICIARY_NAME_LINE_1_LEN));
			builder.append(rightPadSpaces(beneName2, FieldLengthConstants.DETAILS_BENEFICIARY_NAME_LINE_2_LEN)); 
			builder.append(rightPadSpaces(beneName3, FieldLengthConstants.DETAILS_BENEFICIARY_NAME_LINE_3_LEN)); 
			builder.append(rightPadSpaces(beneName4, FieldLengthConstants.DETAILS_BENEFICIARY_NAME_LINE_4_LEN)); 
		 	
			String beneAdd1 = null;
			String beneAdd2 = null;
			String beneAdd3 = null;
			String beneAdd4 = null;
			String[] beneAddLines = splitStringByLineBreak(beneficiaryMailingAddress);
			if (beneAddLines != null){
				if (beneAddLines.length == 1){ //no line breaks, split string by size
					List<String> beneAddList = splitStringBySize(beneAddLines[0], FieldLengthConstants.DETAILS_BENEFICIARY_ADDRESS_LINE_1_LEN);
					beneAdd1 = ((!beneAddList.isEmpty()) ? beneAddList.get(0): null);
					beneAdd2 = ((beneAddList.size()>1) ? beneAddList.get(1): null);
					beneAdd3 = ((beneAddList.size()>2) ? beneAddList.get(2): null);
					beneAdd4 = ((beneAddList.size()>3) ? beneAddList.get(3): null);
				} else {
					beneAdd1 = ((beneAddLines.length>0) ? beneAddLines[0]: null);
					beneAdd2 = ((beneAddLines.length>1) ? beneAddLines[1]: null);
					beneAdd3 = ((beneAddLines.length>2) ? beneAddLines[2]: null);
					beneAdd4 = ((beneAddLines.length>3) ? beneAddLines[3]: null);
				}
			}
			builder.append(rightPadSpaces(beneAdd1, FieldLengthConstants.DETAILS_BENEFICIARY_ADDRESS_LINE_1_LEN)); 
			builder.append(rightPadSpaces(beneAdd2, FieldLengthConstants.DETAILS_BENEFICIARY_ADDRESS_LINE_2_LEN)); 
			builder.append(rightPadSpaces(beneAdd3, FieldLengthConstants.DETAILS_BENEFICIARY_ADDRESS_LINE_3_LEN)); 
			builder.append(rightPadSpaces(beneAdd4, FieldLengthConstants.DETAILS_BENEFICIARY_ADDRESS_LINE_4_LEN)); 
		
			builder.append(rightPadSpaces(beneficiaryCity , FieldLengthConstants.DETAILS_BENEFICIARY_CITY_LEN));
			builder.append(rightPadSpaces((deliveryModePost) ? "SG" : null, FieldLengthConstants.DETAILS_BENEFICIARY_COUNTRY_CODE_LEN) );
			builder.append(rightPadSpaces(beneficiaryPostal , FieldLengthConstants.DETAILS_BENEFICIARY_POSTAL_CODE_LEN));
			builder.append(rightPadSpaces(beneficiaryEmailAddress , FieldLengthConstants.DETAILS_BENEFICIARY_EMAIL_LEN));
			builder.append(rightPadSpaces(null, FieldLengthConstants.DETAILS_BENEFICIARY_FACSIMILE_NO_LEN));
			
			String payerName1 = null;
			String payerName2 = null;
			String[] payerNameLines = splitStringByLineBreak(payerName);
			if (payerNameLines != null){
				if (payerNameLines.length == 1){ //no line breaks, split string by size
					List<String> payerNameList = splitStringBySize(payerNameLines[0], FieldLengthConstants.DETAILS_PAYER_NAME_LINE_1_LEN);
					payerName1 = ((!payerNameList.isEmpty()) ? payerNameList.get(0): null);
					payerName2 = ((payerNameList.size()>1) ? payerNameList.get(1): null);
				} else if (payerNameLines.length > 1){
					payerName1 = payerNameLines[0];
					payerName2 = payerNameLines[1];
				}
			}
			builder.append(rightPadSpaces(payerName1 , FieldLengthConstants.DETAILS_PAYER_NAME_LINE_1_LEN));
			builder.append(rightPadSpaces(payerName2 , FieldLengthConstants.DETAILS_PAYER_NAME_LINE_2_LEN));
			
			builder.append(rightPadSpaces(null , FieldLengthConstants.DETAILS_PAYMENT_ADVICE_FILLER_3_LEN));			
		
		}
		
		return builder.toString();
	}
	
	public static String generatePaymentAdviceFormat(int spacingLines, String paymentAdviceDetails){
		StringBuilder builder = new StringBuilder();
		
		builder.append(Integer.toString(FieldLengthConstants.PAYMENT_ADVICE_RECORD_TYPE));
		 
		spacingLines = (spacingLines > 50) ? 50 : spacingLines; //cannot be more than 50
		builder.append(leftPadZeros(Integer.toString(spacingLines), FieldLengthConstants.PAYMENT_ADVICE_SPACING_LINES_LEN));
		
		builder.append(rightPadSpaces(paymentAdviceDetails, FieldLengthConstants.PAYMENT_ADVICE_DETAILS_LEN));
		builder.append(rightPadSpaces(null, FieldLengthConstants.PAYMENT_ADVICE_FILLER_LEN));

		if ( LOGGER.isLoggable( Level.INFO ) ) {
			LOGGER.info(String.format("generatePaymentAdviceFormat size:%s", builder.length()));
		}
		return builder.toString();
	}
	
	public static String generateBatchTrailer(boolean hasPaymentAdvice, String totalPaymentAmount, 
			String bicCode, String accountNo, String accountName, String paymentType, 
			List<SAGApplicationsApprovedForAwardWithPayment> applicationList, String purposeCode){
		StringBuilder builder = new StringBuilder();
		
		builder.append(Integer.toString(FieldLengthConstants.TRAILER_RECORD_TYPE));
		builder.append(leftPadZeros(totalPaymentAmount, FieldLengthConstants.TRAILER_TOTAL_PAYMENT_AMOUNT_LEN));
		builder.append(leftPadZeros(Integer.toString(applicationList.size()), FieldLengthConstants.TRAILER_TOTAL_NO_OF_TRANSACTIONS_LEN));
		
		int hashTotal = calculateHashTotal(bicCode, accountNo, accountName, paymentType, applicationList, purposeCode);
		builder.append(leftPadZeros(Integer.toString(hashTotal), FieldLengthConstants.TRAILER_HASH_TOTAL_LEN));
		if ( LOGGER.isLoggable( Level.INFO ) ) {
			LOGGER.info(String.format("hashTotal: %s", hashTotal));
		}
		
		if (hasPaymentAdvice){
			builder.append(rightPadSpaces(null, FieldLengthConstants.TRAILER_PAYMENT_ADVICE_FILLER_LEN));
		} else {
			builder.append(rightPadSpaces(null, FieldLengthConstants.TRAILER_NON_PAYMENT_ADVICE_FILLER_LEN));
		}
		
		return builder.toString();
	}
	
	public static int calculateHashTotal(String bicCode, String accountNo, String accountName, String paymentType, 
			List<SAGApplicationsApprovedForAwardWithPayment> applicationList, String purposeCode){
		
		int headerSum1 = calculateFieldCheckSummary(rightPadSpaces(bicCode, FieldLengthConstants.HEADER_BIC_CODE_LEN));
		int headerSum2 = calculateFieldCheckSummary(rightPadSpaces(accountNo, FieldLengthConstants.HEADER_AC_NO_LEN));
		int headerSum3 = calculateFieldCheckSummary(rightPadSpaces(accountName, FieldLengthConstants.HEADER_AC_NAME_LEN));
		
		int total1 = headerSum1 + headerSum2 + headerSum3;
		
		int paymentCode = 0;
		switch (paymentType){
			case "P": paymentCode = 20;
				break;
			case "R": paymentCode = 22;
				break;
			case "C": paymentCode = 30;
				break;
			default: paymentCode = 0;
				break;
		}

		int hashCode = 0;
		int total2 = 0;
		
		for (SAGApplicationsApprovedForAwardWithPayment a : applicationList){
			if (hashCode == 9){
				hashCode = 1;
			} else {
				hashCode ++;
			}
			
			int sum1 = calculateFieldCheckSummary(rightPadSpaces(a.getBicCodeProxyType(), FieldLengthConstants.DETAILS_BIC_CODE_PROXY_TYPE_LEN));
			int sum2 = calculateFieldCheckSummary(rightPadSpaces(a.getAccNoProxyValue(), FieldLengthConstants.DETAILS_AC_NO_PROXY_VALUE_LEN));
			sum2 *= hashCode;
			
			String accName = AccountingUtil.replaceSpecialChar(a.getAccName(), SPACE);
			int sum3 = calculateFieldCheckSummary(rightPadSpaces(accName, FieldLengthConstants.DETAILS_AC_NAME_LEN));
			sum3 *= hashCode;
			int sum4 = calculateFieldCheckSummary(FieldLengthConstants.CURRENCY);
			
			String formattedAmount = getAmountFormatFromDouble(a.getAwardAmount());
			int sum5 = calculateFieldCheckSummary(leftPadZeros(formattedAmount, FieldLengthConstants.DETAILS_AMOUNT_LEN));
			
			int sum6 = calculateFieldCheckSummary(rightPadSpaces(purposeCode, FieldLengthConstants.DETAILS_PURPOSE_CODE_LEN));
			
			int sum7 = sum1+sum2+sum3+sum4+sum5+sum6+ (paymentCode * hashCode);
			
			total2 += sum7;
			if ( LOGGER.isLoggable( Level.INFO ) ) {
				LOGGER.info(String.format("sum1:%s sum2:%s sum3:%s sum4:%s sum5:%s sum6:%s sum7:%s paymentCode:%s hashCode:%s total2:%s", sum1, sum2, sum3, sum4, sum5, sum6, sum7, paymentCode, hashCode, total2));
			}
		}
		if ( LOGGER.isLoggable( Level.INFO ) ) {
			LOGGER.info(String.format("total1: %s , total2: %s", total1, total2));
		}
		return total1+total2;
	}
	
	public static String getRefNoFromFileName(String sName){
		
		if (sName.length() >= FieldLengthConstants.FILENAME_INFINITY_REF_NO_END){
			return getSubString(sName, FieldLengthConstants.FILENAME_INFINITY_REF_NO_START, FieldLengthConstants.FILENAME_INFINITY_REF_NO_END);
		}
		
		return null;
	}
	
	public static boolean getPaymentAdviceFromFileName (String fileName){
		boolean paymentAdvice = false;
		String pa = getSubString(fileName, FieldLengthConstants.FILENAME_PAYMENT_ADVICE_START, FieldLengthConstants.FILENAME_PAYMENT_ADVICE_END);
		if ( LOGGER.isLoggable( Level.INFO ) ) {
			LOGGER.info(String.format("payment advice: %s", pa));
		}
		if (pa != null){
			switch (pa){
				case "GA": paymentAdvice = true;
					break;
				case "GB":
                    break;
				case "GM":
                    break;
				default: 
					LOGGER.log(Level.WARNING, "Fail to find the payment advice");
					break;
			}
		}
			
		return paymentAdvice;
	}
	
	public static SAGBatchFileRecord getRecordFromList (String referenceNumber, List<SAGBatchFileRecord> recordList){
		
		for (SAGBatchFileRecord sagbatchfilerecord : recordList){
			if (sagbatchfilerecord.getReferenceNumber().equals(referenceNumber)){
				LOGGER.log(Level.INFO, "getRecordFromList >> found !! " + sagbatchfilerecord.getReferenceNumber());
				return sagbatchfilerecord;
			}
		}
		return null;
	}
	
	public static List<SAGApplicationsApprovedForAwardWithPayment> populateMockPaymentStatusListFromList (List<SAGApplicationsApprovedForAwardWithPayment> applicationList,
			List<SAGBatchFileRecord> applicationListWithMockStatus){
		
		for (SAGApplicationsApprovedForAwardWithPayment app : applicationList){
			
			for (SAGBatchFileRecord appWithStatus : applicationListWithMockStatus){	
				if (app.getReferenceNumber().equals(appWithStatus.getReferenceNumber())){
					LOGGER.log(Level.INFO, "populateMockPaymentStatusListFromList >> found " + appWithStatus.getReferenceNumber() + " status " + appWithStatus.getPaymentStatus().toString());
					app.setPaymentStatus(appWithStatus.getPaymentStatus());
					break;
				}
			}
		}
		return applicationList;
	}
	
	public static String getTotalAmountByStatus(List<SAGApplicationsApprovedForAwardWithPayment> applicationList, String status){
		
		Double amount = 0.0;
		for (SAGApplicationsApprovedForAwardWithPayment application : applicationList){
			if ((application.getPaymentStatus() != null) && (application.getPaymentStatus().name().equals(status))){
				amount += application.getAwardAmount();
			}
		}
		return getAmountFormatFromDouble(amount);
	}
	
	public static String getApplicationCountByStatus(List<SAGApplicationsApprovedForAwardWithPayment> applicationList, String status){
		
		int count = 0;
		for (SAGApplicationsApprovedForAwardWithPayment application : applicationList){
			if ((application.getPaymentStatus() != null) && (application.getPaymentStatus().name().equals(status))){
				count ++;
			}
		}
		return Integer.toString(count);
	}
	
	public static UOBBatchHeader processBatchHeader(String sHeader) throws ParseException{
		if ( LOGGER.isLoggable( Level.INFO ) ) {
			LOGGER.info(String.format("processBatchHeader length:%s", sHeader.length()));
		}
		UOBBatchHeader batchHeader = new UOBBatchHeader();
		
		batchHeader.setRecordType(getSubString(sHeader, FieldLengthConstants.UOB_HEADER_RECORD_TYPE_START, FieldLengthConstants.UOB_HEADER_RECORD_TYPE_END));
		batchHeader.setPaymentType(getSubString(sHeader,FieldLengthConstants.UOB_HEADER_PAYMENT_TYPE_START, FieldLengthConstants.UOB_HEADER_PAYMENT_TYPE_END));
		batchHeader.setServiceType(getSubString(sHeader,FieldLengthConstants.UOB_HEADER_SERVICE_TYPE_START, FieldLengthConstants.UOB_HEADER_SERVICE_TYPE_END));
		batchHeader.setProcessingMode(getSubString(sHeader,FieldLengthConstants.UOB_HEADER_PROCESSING_MODE_START, FieldLengthConstants.UOB_HEADER_PROCESSING_MODE_END));
		
		batchHeader.setCompanyId(getSubString(sHeader,FieldLengthConstants.UOB_HEADER_COMPANY_ID_START, FieldLengthConstants.UOB_HEADER_COMPANY_ID_END));
		batchHeader.setOriginatingBICCode(getSubString(sHeader,FieldLengthConstants.UOB_HEADER_BIC_CODE_START, FieldLengthConstants.UOB_HEADER_BIC_CODE_END));
		batchHeader.setOriginatingAccountNoCurrency(getSubString(sHeader,FieldLengthConstants.UOB_HEADER_AC_NO_CURRENCY_START, FieldLengthConstants.UOB_HEADER_AC_NO_CURRENCY_END));
		batchHeader.setOriginatingAccountNo(getSubString(sHeader,FieldLengthConstants.UOB_HEADER_AC_NO_START, FieldLengthConstants.UOB_HEADER_AC_NO_END));
		batchHeader.setOriginatingAccountName(getSubString(sHeader,FieldLengthConstants.UOB_HEADER_AC_NAME_START, FieldLengthConstants.UOB_HEADER_AC_NAME_END));
		
		batchHeader.setCreationDate(getDateTimeFormatFromString(getSubString(sHeader,FieldLengthConstants.UOB_HEADER_FILE_CREATION_DATE_START, FieldLengthConstants.UOB_HEADER_FILE_CREATION_DATE_END)));
		batchHeader.setValueDate(getDateTimeFormatFromString(getSubString(sHeader,FieldLengthConstants.UOB_HEADER_VALUE_DATE_START, FieldLengthConstants.UOB_HEADER_VALUE_DATE_END)));
		
		batchHeader.setUltimateOriginatingCustomer(getSubString(sHeader,FieldLengthConstants.UOB_HEADER_ULTI_ORIGINATING_CUSTOMER_START, FieldLengthConstants.UOB_HEADER_ULTI_ORIGINATING_CUSTOMER_END));
		batchHeader.setBulkCustomerReference(getSubString(sHeader,FieldLengthConstants.UOB_HEADER_BULK_CUSTOMER_REFERENCE_START, FieldLengthConstants.UOB_HEADER_BULK_CUSTOMER_REFERENCE_END));
		batchHeader.setSoftwareLabel(getSubString(sHeader,FieldLengthConstants.UOB_HEADER_SOFTWARE_LABEL_START, FieldLengthConstants.UOB_HEADER_SOFTWARE_LABEL_END));
		
		return batchHeader;
	}
	
	public static UOBBatchDetail processBatchDetails(String sDetail, boolean hasPaymentAdvice){
		if ( LOGGER.isLoggable( Level.INFO ) ) {
			LOGGER.info(String.format("processBatchDetails length: %s", sDetail.length()));
		}
		UOBBatchDetail batchDetail = new UOBBatchDetail();
		
		batchDetail.setRecordType(getSubString(sDetail,FieldLengthConstants.UOB_DETAILS_RECORD_TYPE_START, FieldLengthConstants.UOB_DETAILS_RECORD_TYPE_END));
		batchDetail.setReceivingBICCode(getSubString(sDetail,FieldLengthConstants.UOB_DETAILS_BIC_CODE_PROXY_TYPE_START, FieldLengthConstants.UOB_DETAILS_BIC_CODE_PROXY_TYPE_END));
		batchDetail.setReceivingAccountNo(getSubString(sDetail,FieldLengthConstants.UOB_DETAILS_AC_NO_PROXY_VALUE_START, FieldLengthConstants.UOB_DETAILS_AC_NO_PROXY_VALUE_END));
		batchDetail.setReceivingAccountName(getSubString(sDetail,FieldLengthConstants.UOB_DETAILS_AC_NAME_START, FieldLengthConstants.UOB_DETAILS_AC_NAME_END));
		batchDetail.setCurrency(getSubString(sDetail,FieldLengthConstants.UOB_DETAILS_AC_NO_CURRENCY_START, FieldLengthConstants.UOB_DETAILS_AC_NO_CURRENCY_END));
		batchDetail.setAmount(getAmountFormatFromString(getSubString(sDetail,FieldLengthConstants.UOB_DETAILS_AMOUNT_START, FieldLengthConstants.UOB_DETAILS_AMOUNT_END)));
		batchDetail.setEndToEndId(getSubString(sDetail,FieldLengthConstants.UOB_DETAILS_END_TO_END_ID_START, FieldLengthConstants.UOB_DETAILS_END_TO_END_ID_END));
		batchDetail.setMandateId(getSubString(sDetail,FieldLengthConstants.UOB_DETAILS_MANDATE_ID_START, FieldLengthConstants.UOB_DETAILS_MANDATE_ID_END));
		batchDetail.setPurposeCode(getSubString(sDetail,FieldLengthConstants.UOB_DETAILS_PURPOSE_CODE_START, FieldLengthConstants.UOB_DETAILS_PURPOSE_CODE_END));
		
		batchDetail.setRemittanceInformation(getSubString(sDetail,FieldLengthConstants.UOB_DETAILS_REMITTANCE_INFORMATION_START, FieldLengthConstants.UOB_DETAILS_REMITTANCE_INFORMATION_END));
		batchDetail.setUltimatePayerBeneName(getSubString(sDetail,FieldLengthConstants.UOB_DETAILS_ULT_PAYER_BENEFICIARY_NAME_START, FieldLengthConstants.UOB_DETAILS_ULT_PAYER_BENEFICIARY_NAME_END));
			
		batchDetail.setCustomerReference(getSubString(sDetail,FieldLengthConstants.UOB_DETAILS_CUSTOMER_REFERENCE_START, FieldLengthConstants.UOB_DETAILS_CUSTOMER_REFERENCE_END));
			
		batchDetail.setReturnCode(getSubString(sDetail,FieldLengthConstants.UOB_DETAILS_RETURN_CODE_START, FieldLengthConstants.UOB_DETAILS_RETURN_CODE_END));
		batchDetail.setPaymentStatus(PaymentStatus.get(getSubString(sDetail,FieldLengthConstants.UOB_DETAILS_CLEAR_FATE_START, FieldLengthConstants.UOB_DETAILS_CLEAR_FATE_END)));
		
		if (hasPaymentAdvice){
			batchDetail.setReasonOfNotSent(getSubString(sDetail,FieldLengthConstants.UOB_DETAILS_REASON_NOT_SENT_START, FieldLengthConstants.UOB_DETAILS_REASON_NOT_SENT_END));
		}

		return batchDetail;
	}
	
	public static UOBBatchTrailer processBatchTrailer(String sTrailer){
		if ( LOGGER.isLoggable( Level.INFO ) ) {
			LOGGER.info(String.format("processBatchTrailer length: %s", sTrailer.length()));
		}
		UOBBatchTrailer batchTrailer = new UOBBatchTrailer();
		
		batchTrailer.setRecordType(getSubString(sTrailer, FieldLengthConstants.UOB_TRAILER_RECORD_TYPE_START, FieldLengthConstants.UOB_TRAILER_RECORD_TYPE_END));
		
		batchTrailer.setTotalAmount(getAmountFormatFromString(getSubString(sTrailer, FieldLengthConstants.UOB_TRAILER_TOTAL_AMOUNT_START, FieldLengthConstants.UOB_TRAILER_TOTAL_AMOUNT_END)));
		batchTrailer.setTotalNoOfTransaction(Integer.parseInt(getSubString(sTrailer, FieldLengthConstants.UOB_TRAILER_TOTAL_NO_TRANSACTION_START, FieldLengthConstants.UOB_TRAILER_TOTAL_NO_TRANSACTION_END)));
		
		batchTrailer.setTotalAcceptedAmount(getAmountFormatFromString(getSubString(sTrailer, FieldLengthConstants.UOB_TRAILER_TOTAL_ACCEPTED_AMOUNT_START, FieldLengthConstants.UOB_TRAILER_TOTAL_ACCEPTED_AMOUNT_END)));
		batchTrailer.setTotalAcceptedNoOfTransaction(Integer.parseInt(getSubString(sTrailer, FieldLengthConstants.UOB_TRAILER_TOTAL_ACCEPTED_NO_TRANSACTION_START, FieldLengthConstants.UOB_TRAILER_TOTAL_ACCEPTED_NO_TRANSACTION_END)));
		
		batchTrailer.setTotalRejectedAmount(getAmountFormatFromString(getSubString(sTrailer, FieldLengthConstants.UOB_TRAILER_TOTAL_REJECTED_AMOUNT_START, FieldLengthConstants.UOB_TRAILER_TOTAL_REJECTED_AMOUNT_END)));
		batchTrailer.setTotalRejectedNoOfTransaction(Integer.parseInt(getSubString(sTrailer, FieldLengthConstants.UOB_TRAILER_TOTAL_REJECTED_NO_TRANSACTION_START, FieldLengthConstants.UOB_TRAILER_TOTAL_REJECTED_NO_TRANSACTION_END)));
		
		batchTrailer.setTotalPendingAmount(getAmountFormatFromString(getSubString(sTrailer, FieldLengthConstants.UOB_TRAILER_TOTAL_PENDING_AMOUNT_START, FieldLengthConstants.UOB_TRAILER_TOTAL_PENDING_AMOUNT_END)));
		batchTrailer.setTotalPendingNoOfTransaction(Integer.parseInt(getSubString(sTrailer, FieldLengthConstants.UOB_TRAILER_TOTAL_PENDING_NO_TRANSACTION_START, FieldLengthConstants.UOB_TRAILER_TOTAL_PENDING_NO_TRANSACTION_END)));
		
		batchTrailer.setTotalStoppedAmount(getAmountFormatFromString(getSubString(sTrailer, FieldLengthConstants.UOB_TRAILER_TOTAL_STOPPED_AMOUNT_START, FieldLengthConstants.UOB_TRAILER_TOTAL_STOPPED_AMOUNT_END)));
		batchTrailer.setTotalStoppedNoOfTransaction(Integer.parseInt(getSubString(sTrailer, FieldLengthConstants.UOB_TRAILER_TOTAL_STOPPED_NO_TRANSACTION_START, FieldLengthConstants.UOB_TRAILER_TOTAL_STOPPED_NO_TRANSACTION_END)));
						
		return batchTrailer;
	}
	
	public static List< List< SAGBatchFileRecord >> splitIntoBatches( List< SAGBatchFileRecord > sagBatchFileRecordList, int batchSize ) {
        List< List< SAGBatchFileRecord >> batchList = new ArrayList<>();
        if ( sagBatchFileRecordList != null ) {
            List< SAGBatchFileRecord > tempList = null;
            for ( SAGBatchFileRecord sagBatchRecord : sagBatchFileRecordList ) 
            {
                if ((tempList == null) || (tempList.size() >= batchSize))
                {
                    tempList = new ArrayList<>();
                    batchList.add( tempList );
                }
                tempList.add( sagBatchRecord );
            }
        }
        return batchList;
    }
	
	public static String getAccpacHeaderAsCsvString(){
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(FieldLengthConstants.ACCPAC_HEADER_BATCH_ID);
		sb.append(CSV_DELIMITER);
		sb.append(FieldLengthConstants.ACCPAC_HEADER_ENTRY_NUMBER);
		sb.append(CSV_DELIMITER);
		sb.append(FieldLengthConstants.ACCPAC_HEADER_ACC_CODE);
		sb.append(CSV_DELIMITER);
		sb.append(FieldLengthConstants.ACCPAC_HEADER_BATCH_DESC);
		sb.append(CSV_DELIMITER);
		sb.append(FieldLengthConstants.ACCPAC_HEADER_PAYMENT_MODE);
		sb.append(CSV_DELIMITER);
		sb.append(FieldLengthConstants.ACCPAC_HEADER_PAYMENT_REFERENCE);
		sb.append(CSV_DELIMITER);
		sb.append(FieldLengthConstants.ACCPAC_HEADER_ENTRY_DESCRIPTION);
		sb.append(CSV_DELIMITER);
		sb.append(FieldLengthConstants.ACCPAC_HEADER_DESTINATION_BANK_BRANCH);
		sb.append(CSV_DELIMITER);
		sb.append(FieldLengthConstants.ACCPAC_HEADER_DESTINATION_ACC_NO);
		sb.append(CSV_DELIMITER);
		sb.append(FieldLengthConstants.ACCPAC_HEADER_DESTINATION_ACC_NAME);
		sb.append(CSV_DELIMITER);
		sb.append(FieldLengthConstants.ACCPAC_HEADER_LINE_DESCRIPTION);
		sb.append(CSV_DELIMITER);
		sb.append(FieldLengthConstants.ACCPAC_HEADER_AMOUNT);
		sb.append(CSV_DELIMITER);
		sb.append(FieldLengthConstants.ACCPAC_HEADER_GST_AMOUNT);
		sb.append(CSV_DELIMITER);
		sb.append(FieldLengthConstants.ACCPAC_HEADER_GST_INCLUSIVE);
		sb.append(CSV_DELIMITER);
		sb.append(FieldLengthConstants.ACCPAC_HEADER_GST_RATE);
		sb.append(CSV_DELIMITER);
		sb.append(FieldLengthConstants.ACCPAC_HEADER_TRANSACTION_DATE);
		sb.append(CSV_DELIMITER);
		sb.append(FieldLengthConstants.ACCPAC_HEADER_CURRENCY);
		sb.append(CSV_DELIMITER);
		sb.append(FieldLengthConstants.ACCPAC_HEADER_EX_RATE);
		sb.append(CSV_DELIMITER);
		sb.append(FieldLengthConstants.ACCPAC_HEADER_BANK_CODE);
		sb.append(CSV_DELIMITER);
		sb.append(FieldLengthConstants.ACCPAC_HEADER_TRANSACTION_TYPE);
		
		return sb.toString();
	}
	
	public static String getAccpacDetailAsCsvString (AccpacDetail detail){
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(checkNullTrim(detail.getBatchID()));
		sb.append(CSV_DELIMITER);
		sb.append(checkNullTrim(detail.getEntryNumber()));
		sb.append(CSV_DELIMITER);
		sb.append(checkNullTrim(detail.getAccCode()));
		sb.append(CSV_DELIMITER);
		sb.append(checkNullTrim(detail.getBatchDesc()));
		sb.append(CSV_DELIMITER);
		sb.append(checkNullTrim(detail.getPaymentMode()));
		sb.append(CSV_DELIMITER);
		sb.append(checkNullTrim(detail.getPaymentReference()));
		sb.append(CSV_DELIMITER);
		sb.append(setDoubleQuote(checkNullTrim(detail.getEntryDescription())));
		sb.append(CSV_DELIMITER);
		sb.append(setDoubleQuote(checkNullTrim(detail.getDestinationBankBranch())));
		sb.append(CSV_DELIMITER);
		sb.append(checkNullTrim(detail.getDestinationAccNo()));
		sb.append(CSV_DELIMITER);
		sb.append(setDoubleQuote(checkNullTrim(detail.getDestinationAccName())));
		sb.append(CSV_DELIMITER);
		sb.append(setDoubleQuote(checkNullTrim(detail.getLineDescription())));
		sb.append(CSV_DELIMITER);
		sb.append(checkNullTrim(detail.getAmount()));
		sb.append(CSV_DELIMITER);
		sb.append(checkNullTrim(detail.getGstAmount()));
		sb.append(CSV_DELIMITER);
		sb.append(checkNullTrim(detail.getGstInclusive()));
		sb.append(CSV_DELIMITER);
		sb.append(checkNullTrim(detail.getGstRate()));
		sb.append(CSV_DELIMITER);
		sb.append(checkNullTrim(detail.getTransactionDate()));
		sb.append(CSV_DELIMITER);
		sb.append(checkNullTrim(detail.getCurrency()));
		sb.append(CSV_DELIMITER);
		sb.append(checkNullTrim(detail.getExRate()));
		sb.append(CSV_DELIMITER);
		sb.append(checkNullTrim(detail.getBankCode()));
		sb.append(CSV_DELIMITER);
		sb.append(checkNullTrim(detail.getTransactionType()));
		
		return sb.toString();
	}
	
	private static String checkNullTrim (String data){
		if (data != null){
			return data.trim();
		} 
		return "";
	}
	
	public static String setDoubleQuote (String data){
		
		if (data != null){
			return "\"" + data.trim() + "\"";
		}
		return "";
	}
	
	public static String getAccpacTransactionDatefromApprovalRecords (List<ApprovalRecord> approvalRecordList){
		
		String sDate = "";
		if (approvalRecordList != null && !approvalRecordList.isEmpty()) {
			
			ApprovalRecord approvalrecord = approvalRecordList.get(approvalRecordList.size() - 1);
			if (approvalrecord != null){
				Date transactionDate = approvalrecord.getDateOfCompletion();
				if (transactionDate != null){
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					sDate = df.format(transactionDate);
				}
			}
		}
		return sDate;
	}
}
