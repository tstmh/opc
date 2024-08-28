package com.stee.spfcore.webapi.rest;

import java.util.Date;

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
import com.stee.spfcore.webapi.model.personnel.ChangeRecord;
import com.stee.spfcore.webapi.model.personnel.Employment;
import com.stee.spfcore.webapi.model.personnel.PersonalDetail;
import com.stee.spfcore.webapi.service.PersonalDetailService;
import com.stee.spfcore.webapi.utils.ConvertUtil;

@RestController
@RequestMapping("/personnel")
public class PersonalDetailController {

	private PersonalDetailService service;
	private static final Logger logger = LoggerFactory.getLogger(PersonalDetailController.class);
	@Autowired
	public PersonalDetailController(PersonalDetailService service) {
		this.service = service;
	}
	
	@GetMapping("/getPersonalDetails")
	public PersonalDetail getPersonalDetail(@RequestParam String nric) {
		logger.info("getPersonalDetail: Starting service.");
		PersonalDetail personalDetail = service.getPersonalDetail(nric); 
		if (null != personalDetail) {
			logger.info("getPersonalDetail: Success!");
			return personalDetail;
		}
		else {
			logger.warn("getPersonalDetail: Failed, object returned is null.");
			throw new IllegalStateException("Object returned is empty, please verify input parameters!");
		}
	}
	
	@PostMapping("/updatePersonalDetail")
	public void updatePersonalDetail(@RequestHeader("requester") String requester, @RequestBody PersonalDetail personal) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.updatePersonalDetail(personal);
	}
	
	@PostMapping("/createPersonalDetail")
	public void createPersonalDetail(@RequestHeader("requester") String requester, @RequestBody PersonalDetail personal) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.addPersonalDetail(personal);
	}
	
	@GetMapping("/getChangeRecord")
	public ChangeRecord getChangeRecord(@RequestParam String nric) {
		logger.info("getChangeRecord: API Called.");
		ChangeRecord changeRecord = service.getChangeRecord(nric); 
		if (null != changeRecord) {
			logger.info("getChangeRecord: Success!");
			return changeRecord;
		}
		logger.info("getChangeRecord: Empty object");
		return null;
	
	}
	
	@PostMapping("/addChangeRecord")
	public void addChangeRecord(@RequestHeader("requester") String requester, @RequestBody ChangeRecord record) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.addChangeRecord(record);
	}
	
	@PostMapping("/updateChangeRecord")
	public void updateChangeRecord(@RequestHeader("requester") String requester, @RequestBody ChangeRecord record) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.updateChangeRecord(record);
	}
	
	@GetMapping("/getPersonalName")
	public String getPersonalName(@RequestParam String nric) {
		String name = service.getPersonalName(nric); 
		return name;	
	}
	
	@GetMapping("/getEmployment")
	public Employment getEmployment ( @RequestParam String nric) {
		Employment employment = service.getEmployment(nric);
		return employment;	
	}
	
	@GetMapping("/isOnFullMonthNoPayLeave")
	public boolean isOnFullMonthNoPayLeave ( @RequestParam String nric, String date) {
		Date newDate = null;
		if ( date != null && !date.trim().isEmpty() ) {
			newDate = ConvertUtil.convertFebDateControlStringToDate( date );
		}
		Boolean output = service.isOnFullMonthNoPayLeave(nric, newDate);
		return output;	
	}
	
}
