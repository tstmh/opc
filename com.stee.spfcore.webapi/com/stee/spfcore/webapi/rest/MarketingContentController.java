package com.stee.spfcore.webapi.rest;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stee.spfcore.webapi.model.marketingContent.BinaryAttachment;
import com.stee.spfcore.webapi.model.marketingContent.internal.BinaryFile;
import com.stee.spfcore.webapi.model.marketingContent.internal.HtmlFile;
import com.stee.spfcore.webapi.model.marketingContent.internal.UserContentViewRecord;
import com.stee.spfcore.webapi.service.MarketingContentService;

@RestController
@RequestMapping("/marketingContent")
public class MarketingContentController {

	private MarketingContentService service;
	
	private static final Logger logger = LoggerFactory.getLogger(MarketingContentController.class);
	
	@Autowired
	public MarketingContentController(MarketingContentService service) {
		this.service = service;
	}
	
	@GetMapping("/getUserContentViewRecord")
	public UserContentViewRecord getUserContentViewRecord(@RequestParam String id) {
		UserContentViewRecord output = service.getUserContentViewRecord(id); 
		return output;	
	}
	
	@GetMapping("/getBinaryFile")
	public BinaryFile getBinaryFile(@RequestParam String nric, String contentId) {
		BinaryFile output = service.getBinaryFile(nric, contentId); 
		return output;	
	}
	
	@GetMapping("/getHtmlFile")
	public HtmlFile getHtmlFile(@RequestParam String nric, String contentId) {
		HtmlFile output = service.getHtmlFile(nric, contentId); 
		return output;	
	}
	
	@GetMapping("/getAttachments")
	public List< BinaryAttachment > getAttachments(@RequestParam String contentId) {
		List< BinaryAttachment > output = service.getAttachments(contentId); 
		return output;	
	}
	
	@PostMapping("/saveUserContentViewRecord")
	public void saveUserContentViewRecord(@RequestBody UserContentViewRecord record) {
		service.saveUserContentViewRecord(record);
		logger.info("saveUserContentViewRecord complete ");
	}

	

	
}
