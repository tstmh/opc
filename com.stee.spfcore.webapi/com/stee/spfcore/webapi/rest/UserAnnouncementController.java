package com.stee.spfcore.webapi.rest;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stee.spfcore.webapi.model.userAnnouncement.UserAnnouncement;
import com.stee.spfcore.webapi.service.UserAnnouncementService;

@RestController
@RequestMapping("/userAnnouncement")
public class UserAnnouncementController {

	private UserAnnouncementService service;
	
	@Autowired
	public UserAnnouncementController(UserAnnouncementService service) {
		//comments
		this.service = service;
	}
	
	@GetMapping("/getUserAnnouncements")
	public List<UserAnnouncement> getUserAnnouncements (@RequestParam String nric){
		List<UserAnnouncement> userAnnouncements = service.getUserAnnouncements(nric);
		return userAnnouncements;
	}
	
}
