package com.stee.spfcore.webapi.service.attachments.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.stee.spfcore.webapi.model.attachments.Attachments;
import com.stee.spfcore.webapi.repository.AttachmentRepository;
import com.stee.spfcore.webapi.rest.AttachmentsController;
import com.stee.spfcore.webapi.service.attachments.IAttachmentsService;

@Service
public class AttachmentsServiceImpl implements IAttachmentsService{
	private AttachmentRepository attachmentRepository;
	private static final Logger logger = LoggerFactory.getLogger(AttachmentsServiceImpl.class);
	
	public AttachmentsServiceImpl (AttachmentRepository attachmentRepository) {
		this.attachmentRepository = attachmentRepository;
	}
	
	@Override
	public Attachments saveAttachment(MultipartFile file) throws Exception {
		Date date = new Date();
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			if(fileName.contains("..")) {
				throw new Exception("File is invalid.");
			}
			Attachments attachment = new Attachments(date, file.getBytes(),fileName, file.getContentType());
			logger.info("Attachment info: " + attachment.getFileName() + ", " + attachment.getFileType());
			if (attachment.getData() == null || attachment.getFileName() == null || attachment.getFileType() == null) {
				throw new Exception("File contains null fields");
			}
			return attachmentRepository.save(attachment);
			
		}
		catch(Exception e){
			throw new Exception("Could not save file: " + fileName);
		}
	}

	@Override
	public Attachments getAttachment(Long id) throws Exception {
		return attachmentRepository.findById(id).orElseThrow(()-> new Exception("File not found with id: " + id));
	}
	
}
