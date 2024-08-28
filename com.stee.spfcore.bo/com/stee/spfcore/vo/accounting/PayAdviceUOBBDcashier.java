package com.stee.spfcore.vo.accounting;

public class PayAdviceUOBBDcashier extends PayAdviceSegment{

	public PayAdviceEntity recordType;
	public PayAdviceEntity paymentType;
	public PayAdviceEntity paymentCurrency;
	public PayAdviceEntity paymentAmount;
	public PayAdviceEntity valueDate;
	public PayAdviceEntity beneficiaryName1;
	public PayAdviceEntity beneficiaryName2;
	public PayAdviceEntity beneficiaryName3;
	public PayAdviceEntity beneficiaryAddress1;
	public PayAdviceEntity beneficiaryAddress2;
	public PayAdviceEntity beneficiaryAddress3;
	public PayAdviceEntity beneficiaryPostalCode;
	public PayAdviceEntity beneficiaryCountryCode;
	public PayAdviceEntity filler1;
	public PayAdviceEntity settlementAcct;
	public PayAdviceEntity settlementCurrency;
	public PayAdviceEntity handlingOption;
	public PayAdviceEntity mailToParty;
	public PayAdviceEntity mailingNameAdd1;
	public PayAdviceEntity mailingNameAdd2;
	public PayAdviceEntity mailingNameAdd3;
	public PayAdviceEntity mailingNameAdd4;
	public PayAdviceEntity mailingPostalCode;
	public PayAdviceEntity mailingCountryCode;
	public PayAdviceEntity filler2;
	public PayAdviceEntity printPayAdvIndicator;
	public PayAdviceEntity printMode;
	public PayAdviceEntity beneficiaryID;
	public PayAdviceEntity printAdvInstruction;
	public PayAdviceEntity filler3;
	public PayAdviceEntity payerName1;
	public PayAdviceEntity payerName2;
	public PayAdviceEntity payerRefNum;
	public PayAdviceEntity beneficiaryEmail;
	public PayAdviceEntity beneficiaryFacsimile;
	public PayAdviceEntity filler4;
	
	public PayAdviceUOBBDcashier(){
		bank = PayAdviceUtil.Bank.UOB;
	}
	
	
}
