package com.stee.spfcore.webapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stee.spfcore.webapi.dao.AccountingDAO;
import com.stee.spfcore.webapi.model.accounting.BankInformation;


@Service
public class AccountingService {

	private AccountingDAO accountingDAO;
	
	@Autowired
	public AccountingService (AccountingDAO accountingDAO) {
		this.accountingDAO = accountingDAO;
	}
	
	@Transactional
	public List< BankInformation > getBankInformation() {
		return accountingDAO.getBankInformation();
	}
		
}
