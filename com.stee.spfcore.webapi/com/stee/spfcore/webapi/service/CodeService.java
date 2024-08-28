package com.stee.spfcore.webapi.service;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stee.spfcore.webapi.dao.CodeDAO;
import com.stee.spfcore.webapi.model.code.Code;
import com.stee.spfcore.webapi.model.code.CodeType;

@Service
public class CodeService {

	private CodeDAO codeDAO;
	
	private static final Logger logger = Logger.getLogger(CodeService.class.getName());
	
	@Autowired
	public CodeService ( CodeDAO codeDAO ) {
		this.codeDAO = codeDAO;
	}
	
	@Transactional
	public Code getCode ( CodeType type, String id ) {
		return codeDAO.getCode( type, id );
	}
	
	@Transactional
	public void addCode ( Code code ) {
		codeDAO.addCode( code );
	}
	
	@Transactional
	public void updateCode ( Code code ) {
		codeDAO.updateCode( code );
	}

	@Transactional
	public List<Code> getCodes(CodeType type) {
		return codeDAO.getCodes( type );
	}
	
}
