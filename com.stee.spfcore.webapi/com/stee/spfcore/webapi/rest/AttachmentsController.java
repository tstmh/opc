package com.stee.spfcore.webapi.rest;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.stee.spfcore.webapi.model.attachments.Attachments;
import com.stee.spfcore.webapi.service.attachments.IAttachmentsService;

@RestController
@RequestMapping("/attachments")
public class AttachmentsController {
	private IAttachmentsService attachmentService;
	private static final Logger logger = LoggerFactory.getLogger(AttachmentsController.class);
	
	public AttachmentsController(IAttachmentsService attachmentService) {
		this.attachmentService = attachmentService;
	}

	@PostMapping("/upload")
	public List <String> uploadFile(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws Exception {
		logger.info("uploadFile starting...");
		response.addHeader("Access-Control-Allow-Origin", "*");
		List <String> outList = new ArrayList<>();
		Attachments attachment = null;
		logger.info("uploadFile: saving attachment...");
		attachment = attachmentService.saveAttachment(file);
		logger.info("uploadFile: attachment saved.");
		String id = Long.toString(attachment.getId());
		String fileName = attachment.getFileName();
		outList.add(id);
		outList.add(fileName);
		return outList;
		
	}

	
	@GetMapping("/download")
	public ResponseEntity <Resource> downloadFile(@RequestParam String id) throws Exception{
		logger.info("Retrieving attachment with id: " + id);
		Attachments attachment = null;
		Long longId = Long.parseLong(id);
		attachment = attachmentService.getAttachment(longId);
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(attachment.getFileType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFileName()
				+ "\"")
				.body(new ByteArrayResource(attachment.getData()));
	}
	


	

}
