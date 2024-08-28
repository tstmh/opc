package com.stee.spfcore.service.supportingDocs;

import com.stee.spfcore.security.AccessDeniedException;

public interface ISupportingDocsService {

	/**
	 * For Internet side:
	 * <ol>
	 * <li>Download files for application that need to be transfer to Intranet
	 * side from FEB into a folder with reference number as folder name.</li>
	 * <li>Zip the folder into a zip file with reference number as file name.</li>
	 * <li>Move the zip file into the SFTP transfer folder.</li>
	 * </ol>
	 * For Intranet side:
	 * <ol>
	 * <li>Move the zip file from the SFTP transfer folder into working folder.</li>
	 * <li>Unzip the zip file</li>
	 * <li>Upload the files in the unzip folder into BPM.</li>
	 * </ol>
	 */
	public void processSupportingDocuments() throws SupportingDocsServiceException, AccessDeniedException;

	
}
