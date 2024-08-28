package com.stee.spfcore.webapi.model.attachments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseData {
	private String fileName;
	private String downloadURL;
	private String fileType;
	private long fileSize;
	
	public ResponseData(String fileName, String downloadURL, String fileType, long fileSize) {
		this.fileName = fileName;
		this.downloadURL = downloadURL;
		this.fileType = fileType;
		this.fileSize = fileSize;
	}

}
