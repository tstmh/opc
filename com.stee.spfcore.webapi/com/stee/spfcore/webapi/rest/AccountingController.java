package com.stee.spfcore.webapi.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stee.spfcore.webapi.model.accounting.BankInformation;
import com.stee.spfcore.webapi.service.AccountingService;

@RestController
@RequestMapping("/accounting")
public class AccountingController {

	private AccountingService service;
	private static final Logger LOG = LoggerFactory.getLogger(AccountingController.class);
	
	@Autowired
	public AccountingController(AccountingService service) {
		//comments
		this.service = service;
	}
	
	@GetMapping("/getBankInformation")
	public List< BankInformation > getbankInformation () {
		LOG.info("getBankInformation: Starting service.");
		List< BankInformation > bankInformation = service.getBankInformation(); 
		LOG.info("getBankInformation: Success!");
		return bankInformation;	
	}
	
}
