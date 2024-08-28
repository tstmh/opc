package com.stee.spfcore.vo.accounting;

/**
 * <b>File Control Header</b>\n
 * For UOB Payment Advice - File Control Header Entity type
 * @author Sebastian
 *
 */
public class PayAdviceUOBFCH extends PayAdviceSegment {
	
	public PayAdviceEntity recordType;
	public PayAdviceEntity fileName;
	public PayAdviceEntity creationDate;
	public PayAdviceEntity creationTime;
	public PayAdviceEntity companyID;
	public PayAdviceEntity checkSummary;
	public PayAdviceEntity companyID2;
	public PayAdviceEntity filler;
	
	public PayAdviceUOBFCH()
	{
		bank = PayAdviceUtil.Bank.UOB;
		recordType = new PayAdviceEntity("Record Type", PayAdviceUtil.entityFormat.NUM_RIGHT_ALIGN_FILLER, "0", 1, 1, "0", 1, true);
		fileName = new PayAdviceEntity("File Name", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 2, 10, "UCPIddmmnn", 2, true);
		creationDate = new PayAdviceEntity("File Creation Date", PayAdviceUtil.entityFormat.NUM_RIGHT_ALIGN_FILLER, "0", 12, 8, "CCYYMMDD", 3, true);
		creationTime = new PayAdviceEntity("File Creation Time", PayAdviceUtil.entityFormat.NUM_RIGHT_ALIGN_FILLER, "0", 20, 6, "HHMMSS", 4, true);
		companyID = new PayAdviceEntity("Company ID", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 26, 12, "Capital letters only", 5, true);
		checkSummary = new PayAdviceEntity("Check Summary", PayAdviceUtil.entityFormat.NUM_RIGHT_ALIGN_FILLER, "0", 38, 15, "File Check summary (i.e. hash total) for validating the file integrity", 6, true);
		companyID2 = new PayAdviceEntity("Company ID", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 53, 12, "Capital letters only (For Internal BIB use only)", 7, true);
		filler = new PayAdviceEntity("Filler", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 65, 836, "Blanks", 8, true);
	}

	
}
