package com.stee.spfcore.vo.accounting;

public class PayAdviceUOBBT extends PayAdviceSegment{
	
	public PayAdviceEntity recordType;
	public PayAdviceEntity totalTrx;
	public PayAdviceEntity totalPayAmt;
	public PayAdviceEntity filler;
	
	public PayAdviceUOBBT(){
		bank = PayAdviceUtil.Bank.UOB;
		recordType = new PayAdviceEntity("Record Type", PayAdviceUtil.entityFormat.NUM_LEFT_ALIGN, "", 1, 1, "9", 1, true);
		totalTrx = new PayAdviceEntity("Total number of transaction", PayAdviceUtil.entityFormat.NUM_RIGHT_ALIGN, "", 2, 8, "Only include record type '2'", 2, true);
		totalPayAmt = new PayAdviceEntity("Total Payment Amount", PayAdviceUtil.entityFormat.NUM_RIGHT_ALIGN, "", 10, 15, "Regardless of payment currency", 3, true);
		filler = new PayAdviceEntity("Filler", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 25, 876, "Blanks", 4, true);
	}

}
