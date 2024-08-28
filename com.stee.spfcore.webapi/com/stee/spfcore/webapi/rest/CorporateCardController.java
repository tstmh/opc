package com.stee.spfcore.webapi.rest;

import java.util.ArrayList;
import java.util.Date;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.stee.spfcore.webapi.model.UserSessionUtil;
import com.stee.spfcore.webapi.model.blacklist.BlacklistModuleNameConstants;
import com.stee.spfcore.webapi.model.calendar.PublicHoliday;
import com.stee.spfcore.webapi.model.corporateCard.CardBooking;
import com.stee.spfcore.webapi.model.corporateCard.CardBookingStatus;
import com.stee.spfcore.webapi.model.corporateCard.CardTypeDetail;
import com.stee.spfcore.webapi.model.membership.Membership;
import com.stee.spfcore.webapi.model.personnel.PersonalDetail;
import com.stee.spfcore.webapi.service.BlacklistService;
import com.stee.spfcore.webapi.service.CorporateCardService;
import com.stee.spfcore.webapi.service.corporateCard.CorporateCardException;
import com.stee.spfcore.webapi.utils.ConvertUtil;

@RestController
@RequestMapping("/corporateCard")
public class CorporateCardController {

	private CorporateCardService service;
	private BlacklistService blacklistService;
	
	private static final Logger logger = LoggerFactory.getLogger(CorporateCardController.class);
	
	@Autowired
	public CorporateCardController(CorporateCardService service, BlacklistService blacklistService) {
		this.service = service;
		this.blacklistService = blacklistService;
	}
	
	@GetMapping("/getCardBooking")
	public CardBooking getCardBooking(@RequestParam String cardBookingId) {
				
		CardBooking cardBooking = service.getCardBooking(cardBookingId);
		return cardBooking;	
	}
	
	@GetMapping("/getCardTypeDetail")
	public CardTypeDetail getCardTypeDetail(@RequestParam String cardTypeId) {
				
		CardTypeDetail cardTypeDetail = service.getCardTypeDetail(cardTypeId);
		return cardTypeDetail;	
	}
	
	@GetMapping("/getMembership")
	public Membership getMembership(@RequestParam String nric) {
				
		Membership membership = service.getMembership(nric);
		return membership;	
	}

	@PostMapping("/getCardBookings")
	public List<CardBooking> getCardBookings(@RequestBody ObjectNode objectNode) {
		String nric = null;
		String startDate = null;
		String endDate = null;

		if (objectNode.get("nric") != null) {
			nric = objectNode.get("nric").asText();
		}

		if (objectNode.get("startDate") != null) {
			startDate = objectNode.get("startDate").asText();
		}

		Date newStartDate = null;
		if ( startDate != null && !startDate.trim().isEmpty() ) {
			newStartDate = ConvertUtil.convertFebDateControlStringToDate( startDate );
			logger.info("getCardBookings startDate: " + newStartDate.toString());
		}

		if (objectNode.get("endDate") != null) {
			endDate = objectNode.get("endDate").asText();
		}
		Date newEndDate = null;
		if ( endDate != null && !endDate.trim().isEmpty() ) {
			newEndDate = ConvertUtil.convertFebDateControlStringToDate( endDate );
			logger.info("getCardBookings endDate: " + newEndDate.toString());
		}
		
		List<String> cardTypeIds = new ArrayList<String>();
		JsonNode arrNodeCardTypeIds = objectNode.get("cardTypeIds");
		logger.info( "getCardBookings arrNodeCardTypeIds: " + arrNodeCardTypeIds);
		if (arrNodeCardTypeIds != null && arrNodeCardTypeIds.isArray()) {
		    for (JsonNode objNode : arrNodeCardTypeIds) {
		    	cardTypeIds.add(objNode.asText());
		    }
		    logger.info( "getCardBookings cardTypeIds >> " + cardTypeIds);
		}
		
		List<String> cardBookingStatuses = new ArrayList<String>();
		JsonNode arrNodeCardBookingStatuses = objectNode.get("cardBookingStatuses");
		if (arrNodeCardBookingStatuses != null && arrNodeCardBookingStatuses.isArray()) {
		    for (JsonNode objNode : arrNodeCardBookingStatuses) {
		        cardBookingStatuses.add(objNode.asText());
		    }
		    logger.info( "getCardBookings cardBookingStatuses >> " + cardBookingStatuses);
		}
		List<CardBookingStatus> newCardBookingStatuses = null;
		if (cardBookingStatuses != null && !cardBookingStatuses.isEmpty()) {
			newCardBookingStatuses = new ArrayList<CardBookingStatus>();
			for (String cardBookingStatus : cardBookingStatuses) {
				CardBookingStatus status = CardBookingStatus.get(cardBookingStatus.trim());
				newCardBookingStatuses.add(status);
			}			
			logger.info( "getCardBookings newCardBookingStatuses: " + newCardBookingStatuses);
		}
		
		List<CardBooking> cardBookings = service.getCardBookings(newStartDate, newEndDate, cardTypeIds, newCardBookingStatuses, nric);
		if (cardBookings != null) {
			logger.info( "getCardBookings cardBooking: " + cardBookings.size());
		}
		return cardBookings;	
	}	
	
	@GetMapping("/getApprovedCardBooking")
	public CardBooking getApprovedCardBooking(@RequestParam(required = false) String startDate, String endDate, String allocatedSerialNumber) {
				
		Date newStartDate = null;
		if ( startDate != null && !startDate.trim().isEmpty() ) {
			newStartDate = ConvertUtil.convertFebDateControlStringToDate( startDate );
			logger.info( "getApprovedCardBooking startDate: " + newStartDate.toString());
		}

		Date newEndDate = null;
		if ( endDate != null && !endDate.trim().isEmpty() ) {
			newEndDate = ConvertUtil.convertFebDateControlStringToDate( endDate );
			logger.info( "getApprovedCardBooking endDate: " + newEndDate.toString());
		}

		logger.info( "getApprovedCardBooking allocatedSerialNumber: " + allocatedSerialNumber);
		
		CardBooking cardBooking = service.getApprovedCardBooking(newStartDate, newEndDate, allocatedSerialNumber);
		if (cardBooking != null) {
			logger.info( "getApprovedCardBooking cardBooking: " + cardBooking.getId());
		}
		return cardBooking;	
	}
	
	@GetMapping("/getAllCreatedEligibleCardTypeDetails")
	public List< CardTypeDetail > getAllCreatedEligibleCardTypeDetails(@RequestParam boolean isPcwfMember, String department) {
		
		List< CardTypeDetail > cardTypeDetails = service.getAllCreatedEligibleCardTypeDetails(isPcwfMember, department);
		if (cardTypeDetails != null) {
			logger.info( "getAllCreatedEligibleCardTypeDetails cardTypeDetails: " + cardTypeDetails.size());
		}
		return cardTypeDetails;
	}

	@GetMapping("/getPublicHolidays")
	public List<PublicHoliday> getPublicHolidays(@RequestParam String date) {
				
		Date newDate = null;
		if ( date != null && !date.trim().isEmpty() ) {
			newDate = ConvertUtil.convertFebDateControlStringToDate( date );
			logger.info( "getMembership newDate: " + newDate.toString());
		}
		
		List<PublicHoliday> publicHolidays = service.getPublicHolidays(newDate);
		return publicHolidays;	
	}
	
	@PostMapping("/addCardBooking")
	public String addCardBooking(@RequestHeader("requester") String requester, @RequestBody CardBooking cardBooking) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		CardBookingStatus cardBookingStatus = cardBooking.getStatus();
		if (cardBookingStatus == CardBookingStatus.APPROVED) {
			try {
				service.sendEmailForSuccessfulBookings(cardBooking);
			} catch (CorporateCardException e) {
				logger.warn("Failed to send email:" + e);
			}
		}
		return service.addCardBooking(cardBooking, requester);
	}
	
	@PostMapping("/updateCardBooking")
	public void updateCardBooking(@RequestHeader("requester") String requester, @RequestBody CardBooking cardBooking) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.updateCardBooking(cardBooking, requester);
		
		CardBookingStatus cardBookingStatus = cardBooking.getStatus();
		if (cardBookingStatus == CardBookingStatus.CANCELLED) {
			try {
				service.sendEmailForCancelledBooking(cardBooking);
			} catch (Exception e) {
				logger.warn("Failed to send email:" + e);
			}
		}
	}
	
	@GetMapping("/isOfficerBlacklisted")
	public boolean isOfficerBlacklisted ( @RequestParam String nric) {

		boolean isBlacklist = blacklistService.isBlacklisted(nric, BlacklistModuleNameConstants.CORPORATE_CARD);
		return isBlacklist;	
	}
	
	@GetMapping("/getNumberOfCardBookingCancellations")
	public int getNumberOfCardBookingCancellations(@RequestParam String nric, String startDate, String todayDate) {
		
		Date newStartDate = null;
		if ( startDate != null && !startDate.trim().isEmpty() ) {
			newStartDate = ConvertUtil.convertFebDateControlStringToDate( startDate );
			logger.info( "getApprovedCardBooking startDate: " + newStartDate.toString());
		}

		Date newTodayDate = null;
		if ( todayDate != null && !todayDate.trim().isEmpty() ) {
			newTodayDate = ConvertUtil.convertFebDateControlStringToDate( todayDate );
			logger.info( "getApprovedCardBooking endDate: " + newTodayDate.toString());
		}
		
		int cancellationCount = service.getNumberOfCardBookingCancellations(nric, newStartDate, newTodayDate);
		
		return cancellationCount;	
	}
	
	@PostMapping("/createCardTypeDetail")
	public String createCardTypeDetail(@RequestHeader("requester") String requester, @RequestBody CardTypeDetail cardTypeDetail) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		
		logger.info("createCardTypeDetail: " + cardTypeDetail);
		
		return service.addCardTypeDetail(cardTypeDetail, requester);
	}
	
	@PostMapping("/updateCardTypeDetail")
	public void updateCardTypeDetail(@RequestHeader("requester") String requester, @RequestBody CardTypeDetail cardTypeDetail) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.updateCardTypeDetail(cardTypeDetail, requester);
	}
	
	@GetMapping("/getPersonal")
	public PersonalDetail getPersonal(@RequestParam String nric) {
		PersonalDetail personalDetail = service.getPersonal(nric); 
		return personalDetail;	
	}
	
//	@PostMapping("/sendEmailForSuccessfulBookings")
//	public void sendEmailForSuccessfulBookings(@RequestBody CardBooking cardBooking){
//		try {
//			service.sendEmailForSuccessfulBookings(cardBooking);
//		} catch (CorporateCardException e) {
//			logger.info("Failed to send email: " + e);
//		}
//	}
	
//	@PostMapping("/sendEmailForCancelledBooking")
//	public void sendEmailForCancelledBooking(@RequestBody CardBooking cardBooking){
//		try {
//			service.sendEmailForCancelledBooking(cardBooking);
//		} catch (Exception e) {
//			logger.info("Failed to send email: " + e);
//		}
//	}
	

	
}
