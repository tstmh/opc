package com.stee.spfcore.webapi.rest;

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

import com.stee.spfcore.webapi.model.UserSessionUtil;
import com.stee.spfcore.webapi.model.genericEvent.GETargetedUser;
import com.stee.spfcore.webapi.model.genericEvent.GenericEventApplication;
import com.stee.spfcore.webapi.model.genericEvent.GenericEventDetail;
import com.stee.spfcore.webapi.service.GenericEventService;
import com.stee.spfcore.webapi.utils.ConvertUtil;

@RestController
@RequestMapping("/genericEvent")
public class GenericEventController {

	private GenericEventService service;
	
	private static final Logger logger = LoggerFactory.getLogger(GenericEventController.class);
	
	@Autowired
	public GenericEventController (GenericEventService service) {
		this.service = service;
	}
	
	@GetMapping("/getGenericEventDetail")
	public GenericEventDetail getGenericEventDetail (@RequestParam String eventId) {
				
		GenericEventDetail eventDetail = service.getEventDetail(eventId);
		return eventDetail;	
	}
	
	@GetMapping("/getGenericEventApplicationsByDate")
	public List<GenericEventApplication> getGenericEventApplications (@RequestParam String nric, String startDate, String endDate) {
		
		Date newStartDate = null;
		if ( startDate != null && !startDate.trim().isEmpty() ) {
			newStartDate = ConvertUtil.convertFebDateControlStringToDate( startDate );
		}

		Date newEndDate = null;
		if ( endDate != null && !endDate.trim().isEmpty() ) {
			newEndDate = ConvertUtil.convertFebDateControlStringToDate( endDate );
		}
		
		List<GenericEventApplication> eventApplications = service.getGenericEventApplications(nric, newStartDate, newEndDate);
		return eventApplications;	
	}
	
	@GetMapping("/getGenericEventPendingRegistration")
	public List<GenericEventDetail> getGenericEventPendingRegistration (@RequestParam String nric) {
		
		List<GenericEventDetail> pendingRegistrations = service.getGenericEventPendingRegistration(nric);
		return pendingRegistrations;	
	}
	
	@GetMapping("/getGenericEventApplication")
	public GenericEventApplication getGenericEventApplication (@RequestParam String eventId, String nric) {
		
		GenericEventApplication application = service.getGenericEventApplication(eventId, nric);
		return application;	
	}
	
	@PostMapping("/addGenericEventApplication")
	public String addGenericEventApplication(@RequestHeader("requester") String requester, @RequestBody GenericEventApplication application) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		
		GenericEventApplication newApp = new GenericEventApplication(null, application.getNric(), application.getEventId(), new Date(), 
				application.getUpdatedBy(), new Date(), application.getStatus(), application.getChoices()
				, application.getDietaryPreference(), application.getBranch());
		
		logger.info( "addGenericEventApplication: " + newApp.getId());
		logger.info( "addGenericEventApplication: " + newApp.toString());
		logger.info( "addGenericEventApplication: " , newApp);
		
		return service.addGenericEventApplication(application);
	}
	
	@PostMapping("/updateGenericEventApplication")
	public void updateGenericEventApplication(@RequestHeader("requester") String requester, @RequestBody GenericEventApplication application) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.updateGenericEventApplication(application);
		logger.info("updateGenericEventApplication complete ");
	}
	
	@PostMapping("/addGenericEventDetail")
	public String addGenericEventDetail(@RequestHeader("requester") String requester, @RequestBody GenericEventDetail eventDetail) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		return service.addGenericEventDetail(eventDetail);
	}
	
	@PostMapping("/updateGenericEventDetail")
	public void updateGenericEventDetail(@RequestHeader("requester") String requester, @RequestBody GenericEventDetail eventDetail) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.updateGenericEventDetail(eventDetail);
		logger.info("updateGenericEventDetail complete ");
	}
	
	@PostMapping("/deleteGenericEventDetail")
	public void deleteGenericEventDetail(@RequestHeader("requester") String requester, @RequestBody GenericEventDetail eventDetail) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.deleteGenericEventDetail(eventDetail);
		logger.info("deleteGenericEventDetail complete ");
	}
	
	@PostMapping("/addGenericEventTargetedUser")
	public String addGenericEventTargetedUser(@RequestHeader("requester") String requester, @RequestBody GETargetedUser targetedUser) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		return service.addGenericEventTargetedUser(targetedUser);
	}
	
	@GetMapping("/isTargetedUser")
	public boolean isTargetedUser (@RequestParam String eventId, String nric) {
		
		boolean output = service.isTargetedUser(eventId, nric);
		return output;	
	}
}
