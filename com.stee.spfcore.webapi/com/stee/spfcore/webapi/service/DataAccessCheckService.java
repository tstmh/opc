package com.stee.spfcore.webapi.service;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stee.spfcore.webapi.dao.DataAccessCheckDAO;
import com.stee.spfcore.webapi.dao.dac.DepartmentAndSubUnit;

@Service
public class DataAccessCheckService {

	private DataAccessCheckDAO dataAccessCheckDAO;

	private static final Logger logger = Logger.getLogger(DataAccessCheckService.class.getName());
	
	@Autowired
	public DataAccessCheckService (DataAccessCheckDAO dataAccessCheckDAO) {
		this.dataAccessCheckDAO = dataAccessCheckDAO;
	}
	
	@Transactional
	public DepartmentAndSubUnit getDepartmentAndSubUnit (String nric) {
		return dataAccessCheckDAO.getDepartmentAndSubUnit(nric);
	}
	
}
