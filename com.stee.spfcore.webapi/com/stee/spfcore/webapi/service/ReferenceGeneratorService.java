package com.stee.spfcore.webapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stee.spfcore.webapi.dao.ReferenceGeneratorDAO;
import com.stee.spfcore.webapi.model.ReferenceGenerator;
import com.stee.spfcore.webapi.model.internal.ApplicationType;

@Service
public class ReferenceGeneratorService {
	
	private ReferenceGeneratorDAO referenceGeneratorDAO;
	
	@Autowired
	public ReferenceGeneratorService (ReferenceGeneratorDAO referenceGeneratorDAO) {
		this.referenceGeneratorDAO = referenceGeneratorDAO;
	}
	
	@Transactional
	public ReferenceGenerator getReferenceGenerator(ApplicationType applicationType) {
		return referenceGeneratorDAO.getReferenceGenerator(applicationType);
	}
	
	@Transactional
	public void addReferenceGenerator(ReferenceGenerator referenceGenerator) {
		referenceGeneratorDAO.addReferenceGenerator(referenceGenerator);
	}
	
	@Transactional
	public void updateReferenceGenerator(ReferenceGenerator referenceGenerator) {
		referenceGeneratorDAO.updateReferenceGenerator(referenceGenerator);
	}

}
