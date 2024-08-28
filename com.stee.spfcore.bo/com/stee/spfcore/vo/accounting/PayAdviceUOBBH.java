package com.stee.spfcore.vo.accounting;

/**
 * <b>Batch Header</b>\n
 * For UOB Payment Advice - Batch Header Entity type
 * @author Sebastian
 *
 */
public class PayAdviceUOBBH extends PayAdviceSegment {

	public PayAdviceEntity recordType;
	public PayAdviceEntity batchNumber;
	public PayAdviceEntity header1;
	public PayAdviceEntity header2;
	public PayAdviceEntity filler;
	
	public PayAdviceUOBBH()
	{
		bank = PayAdviceUtil.Bank.UOB;
		recordType = new PayAdviceEntity("Record Type", PayAdviceUtil.entityFormat.NUM_RIGHT_ALIGN_FILLER, "0", 1, 1, "1", 1, true);
		batchNumber = new PayAdviceEntity("Batch Number", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 2, 20, "For customer reference", 2, true);
		header1 = new PayAdviceEntity("Payment Advice Header 1", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 22, 105, "Payment advice header line (see appendix 3)", 3, true);
		header2 = new PayAdviceEntity("Payment Advice Header 2", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 127, 105, "Payment advice header line (see appendix 3)", 4, true);
		filler = new PayAdviceEntity("Filler", PayAdviceUtil.entityFormat.CHAR_LEFT_ALIGN, "", 232, 669, "Blanks", 5, true);
	}
}
