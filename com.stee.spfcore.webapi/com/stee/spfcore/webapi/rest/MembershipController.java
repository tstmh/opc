package com.stee.spfcore.webapi.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;

import com.stee.spfcore.webapi.model.ApplicationStatus;
import com.stee.spfcore.webapi.model.UserSessionUtil;
import com.stee.spfcore.webapi.model.membership.DiscrepancyRecord;
import com.stee.spfcore.webapi.model.membership.Insurance;
import com.stee.spfcore.webapi.model.membership.Membership;
import com.stee.spfcore.webapi.model.membership.MembershipPaymentCheckRecord;
import com.stee.spfcore.webapi.model.membership.PaymentDataSource;
import com.stee.spfcore.webapi.service.MembershipService;
import com.stee.spfcore.webapi.service.process.IProcessService;
import com.stee.spfcore.webapi.service.process.ProcessServiceFactory;

@RestController
@RequestMapping("/membership")
public class MembershipController {

	private MembershipService service;
	
	private static final Logger logger = LoggerFactory.getLogger(MembershipController.class);
	IProcessService processService = ProcessServiceFactory.getInstance();
	
	@Autowired
	public MembershipController(MembershipService service) {
		this.service = service;
	}
	
	@GetMapping("/getMembership")
	public Membership getMembership(String nric) {
		Membership membership = service.getMembership(nric);
		return membership;
	}
	
	@PostMapping("/applyMembership")
	public void applyMembership(@RequestHeader("requester") String requester, @RequestBody Membership membership) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.updateMembership(membership);
		logger.info("applyMembership complete ");
	}
	
	@PostMapping("/submitInsuranceNomination")
	public void submitInsuranceNomination(@RequestHeader("requester") String requester, @RequestBody Membership membership) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.updateMembership(membership);
		logger.info("submitInsuranceNomination complete ");
	}
	
//	@PostMapping("/withdrawMembership")
//	public void withdrawMembership(@RequestHeader("requester") String requester, @RequestBody Membership membership) {
//		requester = (requester != null) ? requester : "";
//		UserSessionUtil.setUser(requester);
//		service.updateMembership(membership);
//		logger.info("withdrawMembership complete ");
//	}
	
	@PostMapping("/createMembership")
	public void createMembership(@RequestHeader("requester") String requester, @RequestBody Membership membership) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.addMembership(membership);
		logger.info("createMembership complete ");
	}
	
	@PostMapping("/updateMembership")
	public void updateMembership(@RequestHeader("requester") String requester, @RequestBody Membership membership) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.updateMembership(membership);
		logger.info("updateMembership complete ");
		ApplicationStatus withdrawalStatus = membership.getWithdrawalStatus();
		Insurance insurance = membership.getInsurance();
		
		// Do processing for membership withdrawal
		if(withdrawalStatus == ApplicationStatus.PENDING) {
			if(membership.getDateOfWithdrawalRequest() != null) {
				try {
					logger.info("Processing withdraw membership request.");
					processService.withdrawMembershipRequest(membership);
				}
				catch (Exception e) {
					logger.warn ("Fail to process withdraw membership for BPM: " + e);
				}
			}
		}
		
	}
	
	@PostMapping("/saveDiscrepancyRecord")
	public void saveDiscrepancyRecord(@RequestHeader("requester") String requester, @RequestBody DiscrepancyRecord discrepencyRecord) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.saveDiscrepancyRecord(discrepencyRecord);
		logger.info("saveDiscrepancyRecord complete ");
	}
	
	@PostMapping("/deleteDiscrepancyRecords")
	public void deleteDiscrepancyRecords(@RequestHeader("requester") String requester, @RequestBody String nric, Integer month, Integer year) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		
		int result = service.deleteDiscrepancyRecord(nric, month, year);
		logger.info("deleteDiscrepancyRecord complete with result " + result);
	}
	
	@PostMapping("/deletePaymentHistories")
	public void deletePaymentHistories(@RequestHeader("requester") String requester, @RequestBody Integer month, Integer year, PaymentDataSource source) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		
		int result = service.deletePaymentHistories(month, year, source);
		logger.info("deletePaymentHistories complete with result " + result);
	}
	
	@PostMapping("/addMembershipPaymentCheckRecord")
	public void addMembershipPaymentCheckRecord(@RequestBody MembershipPaymentCheckRecord checkRecord) {
		service.addMembershipPaymentCheckRecord(checkRecord);
		logger.info("addMembershipPaymentCheckRecord complete ");
	}
	
}
