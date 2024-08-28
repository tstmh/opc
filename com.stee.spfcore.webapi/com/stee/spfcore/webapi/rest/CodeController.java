package com.stee.spfcore.webapi.rest;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stee.spfcore.webapi.model.UserSessionUtil;
import com.stee.spfcore.webapi.model.code.Code;
import com.stee.spfcore.webapi.model.code.CodeType;
import com.stee.spfcore.webapi.service.CodeService;

@RestController
@RequestMapping("/code")
public class CodeController {

	private CodeService service;
	
	@Autowired
	public CodeController(CodeService service) {
		this.service = service;
	}
	
	@GetMapping("/getCode")
	public Code getCode(@RequestParam String type, String id) {
		Code code = service.getCode(CodeType.valueOf(type), id); 
		return code;	
	}
	
	@GetMapping("/getCodes")
	public List < Code > getCodes(@RequestParam String type) {
		List < Code > codes = service.getCodes(CodeType.valueOf(type)); 
		return codes;	
	}
	
	@PostMapping("/addCode")
	public void addCode(@RequestHeader("requester") String requester, @RequestBody Code code) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.addCode(code);
	}
	
	@PostMapping("/updateCode")
	public void updateCode(@RequestHeader("requester") String requester, @RequestBody Code code) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.updateCode(code);
	}
	
}
