package com.stee.spfcore.vo.accounting;

public class PayAdviceUOBBDcheque extends PayAdviceSegment {
	
	public PayAdviceEntity recordType;
	public PayAdviceEntity payType;
	public PayAdviceEntity payCurrency;
	public PayAdviceEntity payAmt;
	public PayAdviceEntity valueDate;
	public PayAdviceEntity beneficiaryName1;
	public PayAdviceEntity beneficiaryName2;
	public PayAdviceEntity beneficiaryName3;
	public PayAdviceEntity beneficiaryAddr1;
	public PayAdviceEntity beneficiaryAddr2;
	public PayAdviceEntity beneficiaryAddr3;
	public PayAdviceEntity beneficiaryPostal;
	public PayAdviceEntity beneficiaryCountryCode;
	public PayAdviceEntity filler1;
	public PayAdviceEntity settlementAcct;
	public PayAdviceEntity settlementCurrency;
	public PayAdviceEntity handlingOpt;
	public PayAdviceEntity mailToParty;
	public PayAdviceEntity mailingNameAddr1;
	public PayAdviceEntity mailingNameAddr2;
	public PayAdviceEntity mailingNameAddr3;
	public PayAdviceEntity mailingNameAddr4;
	public PayAdviceEntity mailingPostal;
	public PayAdviceEntity mailingCountryCode;
	public PayAdviceEntity filler2;
	public PayAdviceEntity printPayAdvInd;
	public PayAdviceEntity printMode;
	public PayAdviceEntity beneficiaryID;
	public PayAdviceEntity printAdvInstr;
	public PayAdviceEntity filler3;
	public PayAdviceEntity payerName1;
	public PayAdviceEntity payerName2;
	public PayAdviceEntity payerRefNum;
	public PayAdviceEntity emailAddrOfBeneficiary;
	public PayAdviceEntity facsimileNumofBeneficiary;
	public PayAdviceEntity filler4;
	
	public PayAdviceUOBBDcheque()
	{
		bank = PayAdviceUtil.Bank.UOB;
		recordType = new PayAdviceEntity("Record Type", PayAdviceUtil.entityFormat.NUM_LEFT_ALIGN, "", 1, 1, "2", 1, true);
		payType = new PayAdviceEntity("Payment Type", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 2, 3, "CHQ", 2, true);
		payCurrency = new PayAdviceEntity("Payment Currency", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 5, 3, "SGD or USD", 3, true);
		payAmt = new PayAdviceEntity("Payment Amount", PayAdviceUtil.entityFormat.NUM_RIGHT_ALIGN, "", 8, 15, "", 4, true);
		valueDate = new PayAdviceEntity("Value Date", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 23, 8, "CCYYMMDD", 5, true);
		beneficiaryName1 = new PayAdviceEntity("Beneficiary Name Line 1", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 31, 35, "", 6, true);
		beneficiaryName2 = new PayAdviceEntity("Beneficiary Name Line 2", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 66, 35, "", 7, true);
		beneficiaryName3 = new PayAdviceEntity("Beneficiary Namw Line 3", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 101, 35, "", 8, true);
		beneficiaryAddr1 = new PayAdviceEntity("Beneficiary Address Line 1", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 136, 35, "Manadatory if mail to party is 'BEN'", 9, true);
		beneficiaryAddr2 = new PayAdviceEntity("Beneficiary Address Line 2", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 171, 35, "Applicable only if mail to party is 'BEN'", 10, true);
		beneficiaryAddr3 = new PayAdviceEntity("Beneficiary Address Line 3", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 206, 35, "Applicable only if mail to party is 'BEN'", 11, true);
		beneficiaryPostal = new PayAdviceEntity("Beneficiary Postal Code", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 241, 15, "For printing if singapore postal bar use only. 6 Digit format", 12, true);
		beneficiaryCountryCode = new PayAdviceEntity("Beneficiary Country Code", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "Singapore Postal use 'SG'", 256, 3, "", 13, true);
		filler1 = new PayAdviceEntity("Filler", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 259, 6, "", 14, true);
		settlementAcct = new PayAdviceEntity("Settlement Account No", PayAdviceUtil.entityFormat.NUM_RIGHT_ALIGN, "", 265, 20, "Debiting A/C no", 15, true);
		settlementCurrency = new PayAdviceEntity("Settlement Currency", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 285, 3, "Debiting A/C Currency. Must be same as payment currency", 16, true);
		handlingOpt = new PayAdviceEntity("Handling Option", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 288, 1, "Indicate how to handle the cheque. 'M' - mail it, 'C' - hold for customer collection", 17, true);
		mailToParty = new PayAdviceEntity("Mail To Party", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 289, 3, "Mandatory if handling option is 'M'. 'BEN' - Beneficiary, 'OTH' - Others", 18, true);
		mailingNameAddr1 = new PayAdviceEntity("Mailing name and address 1", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 292, 35, "Mandatory if mail to party is 'OTH'", 19, true);
		mailingNameAddr2 = new PayAdviceEntity("Mailing name and address 2", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 327, 35, "", 20, true);
		mailingNameAddr3 = new PayAdviceEntity("Mailing name and address 3", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 362, 35, "", 21, true);
		mailingNameAddr4 = new PayAdviceEntity("Mailing name and address 4", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 397, 35, "", 22, true);
		mailingPostal = new PayAdviceEntity("Mailing postal code", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 432, 15, "Singapore postal bar code use only. 6 difigt format", 23, true);
		mailingCountryCode = new PayAdviceEntity("Mailing Country code", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 447, 3, "Singapore postal code use 'SG'", 24, true);
		filler2 = new PayAdviceEntity("Filler", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 450, 50, "", 25, true);
		printPayAdvInd = new PayAdviceEntity("Print payment advice Indicator", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 500, 1, "Not used", 26, true);
		printMode = new PayAdviceEntity("Print Mode", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 501, 1, "Default to 'P'", 27, true);
		beneficiaryID = new PayAdviceEntity("Beneficiary ID", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 502, 20, "Beneficiary ID", 28, true);
		printAdvInstr = new PayAdviceEntity("Print Advice Instruction", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 522, 1, "Default to '1'", 29, true);
		filler3 = new PayAdviceEntity("Filler", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 523, 198, "Not used", 30, true);
		payerName1 = new PayAdviceEntity("Payer name 1", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 721, 35, "Payer name 1", 31, true);
		payerName2 = new PayAdviceEntity("Payer name 2", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 756, 35, "Payer name 2", 32, true);
		payerRefNum = new PayAdviceEntity("Payer Reference number", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 791, 30, "Payer reference number", 33, true);
		emailAddrOfBeneficiary = new PayAdviceEntity("Email address of beneficiary", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 821, 50, "Not used", 34, true);
		facsimileNumofBeneficiary = new PayAdviceEntity("Facsimile number of beneficiary", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 871, 20, "Not Used", 35, true);
		filler4 = new PayAdviceEntity("Filler", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 891, 10, "Blanks", 36, true);
	}
}
