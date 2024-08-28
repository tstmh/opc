package com.stee.spfcore.service.configuration;

import org.aeonbits.owner.Accessible;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.FIRST)
@Sources({ "file:${spfcore.config}/IAccountingConfig.properties" })
public interface IAccountingConfig extends Accessible {

	@Key("acc.recordType")
	@DefaultValue("2")
	String recordType();
	
	@Key("acc.paymentType")
	@DefaultValue("CHQ")
	String paymentType();
	
	@Key("acc.paymentCurrency")
	@DefaultValue("SGD")
	String paymentCurrency();
	
	@Key("acc.beneficiaryAddress1")
	@DefaultValue("SPF-Lee Foundation")
	String beneficiaryAddress1();
	
	@Key("acc.beneficiaryAddress2")
	@DefaultValue("Study Award")
	String beneficiaryAddress2();
	
	@Key("acc.beneficiaryAddress3")
	@DefaultValue("")
	String beneficiaryAddress3();
	
	@Key("acc.beneficiaryPostalCode")
	@DefaultValue("088762")
	String beneficiaryPostalCode();
	
	@Key("acc.beneficiaryCountryCode")
	@DefaultValue("SG")
	String beneficiaryCountryCode();
	
	@Key("acc.settlementAcNo")
	@DefaultValue("00000000009223418143")
	String settlementAcNo();
	
	@Key("acc.settlementCurrency")
	@DefaultValue("SGD")
	String settlementCurrency();
	
	@Key("acc.handlingOption")
	@DefaultValue("C")
	String handlingOption();

	@Key("acc.mailToParty")
	@DefaultValue("BEN")
	String mailToParty();
	
	@Key("acc.mailingAdd1")
	@DefaultValue("")
	String mailingAdd1();
	
	@Key("acc.mailingAdd2")
	@DefaultValue("")
	String mailingAdd2();
	
	@Key("acc.mailingAdd3")
	@DefaultValue("")
	String mailingAdd3();
	
	@Key("acc.mailingAdd4")
	@DefaultValue("")
	String mailingAdd4();
	
	@Key("acc.mailingPostalCode")
	@DefaultValue("088762")
	String mailingPostalCode();
	
	@Key("acc.mailingCountry")
	@DefaultValue("SG")
	String mailingCountry();
	
	@Key("acc.printPaymentAdviceIndicator")
	@DefaultValue("")
	String printPaymentAdviceIndicator();
	
	@Key("acc.printMode")
	@DefaultValue("P")
	String printMode();
	
	@Key("acc.printAdviceInstruction")
	@DefaultValue("1")
	String printAdviceInstruction();
	
	@Key("acc.payerName1")
	@DefaultValue("SPF-Lee Foundation")
	String payerName1();
	
	@Key("acc.payerName2")
	@DefaultValue("Study Award")
	String payerName2();
}
