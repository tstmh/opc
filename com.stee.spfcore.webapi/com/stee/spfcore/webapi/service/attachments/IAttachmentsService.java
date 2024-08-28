package com.stee.spfcore.webapi.service.attachments;

import org.springframework.web.multipart.MultipartFile;

import com.stee.spfcore.webapi.model.attachments.Attachments;

public interface IAttachmentsService {

	Attachments saveAttachment(MultipartFile file) throws Exception;

	Attachments getAttachment(Long id) throws Exception;

}
