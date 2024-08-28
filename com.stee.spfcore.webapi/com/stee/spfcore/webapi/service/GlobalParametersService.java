package com.stee.spfcore.webapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stee.spfcore.webapi.dao.GlobalParametersDAO;
import com.stee.spfcore.webapi.model.globalParameters.GlobalParameters;

@Service
public class GlobalParametersService {

	private GlobalParametersDAO globalParametersDAO;
	
	@Autowired
	public GlobalParametersService (GlobalParametersDAO globalParametersDAO) {
		this.globalParametersDAO = globalParametersDAO;
	}
	
	@Transactional
	public GlobalParameters getGlobalParametersByUnit(String unit, String subunit) {
		return globalParametersDAO.getGlobalParametersByUnit(unit, subunit);
	}
	
	
	

	
}
