package com.stee.spfcore.service.hr.impl.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Note that this class is not thread-safe due to SimpleDataFormat.
 * Need to synchronize the method if it is to be used in multi-threaded 
 * environment. Currently, it is used in HRService which is assume to 
 * be only trigger by a single timer EJB.  
 * 
 */
public class FileUtil {

	private FileUtil(){}
	private static final Logger LOGGER = Logger.getLogger(FileUtil.class.getName());
	
	public static File moveFile (File inputFile, File workingFolder) {
		
		// Make sure that working folder exists.
		if (!workingFolder.exists() && !workingFolder.mkdirs()) {
			LOGGER.severe("Fail to create folder:" + workingFolder.getAbsolutePath());
		}
		
		if (inputFile.exists()) {
			File newFile = new File (workingFolder, inputFile.getName());
			
			// If file of the same name already exists in the working folder, remove it first before file move
			if (newFile.exists()) {
				try {
					Files.delete(Paths.get(newFile.toURI()));
					if ( LOGGER.isLoggable( Level.INFO ) ) {
						LOGGER.info(String.format("File deleted successfully: %s", newFile.getAbsolutePath()));
					}
				} catch (IOException e) {
					LOGGER.severe(String.format("Failed to delete file: %s %s", newFile.getAbsolutePath(), e));
					// Handle the error appropriately, e.g., retry, throw a custom exception, or log for analysis
				}
			}
			
			if (inputFile.renameTo(newFile)) {
				return newFile;
			}
		}
		
		return null;
	}
	
	
	public static void archiveFile (File workingFile, File archiveFolder) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
		
		// Make sure archive folder exists
		if (!archiveFolder.exists()) {
			archiveFolder.mkdirs();
		}
		
		String postfix = dateFormat.format(new Date());
		String orgFileName = workingFile.getName();
		
		// Remove the extension.
		int index = orgFileName.lastIndexOf(".");
		String fileName = orgFileName.substring(0, index);
		String extension = orgFileName.substring(index);
		
		fileName = fileName + "-" + postfix + extension;
		
		File newFile = new File (archiveFolder, fileName);
		
		if (!workingFile.renameTo(newFile) && LOGGER.isLoggable( Level.INFO ) ) {
			LOGGER.severe(String.format("Fail to archieve file: %s", workingFile.getAbsolutePath()));
		}
	}
	
	
	public static void copyFile (File sourceFile, File destFile) {
		try {
			Files.copy(sourceFile.toPath(), destFile.toPath());
		} 
		catch (IOException e) {
			LOGGER.severe(String.format("Fail to copy file %s to %s", sourceFile.getAbsolutePath(), destFile.getAbsolutePath()));
		}
	}
}
