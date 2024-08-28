package com.stee.spfcore.webapi.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SupportingDocumentsUtil {
	
	private static final Logger logger = Logger.getLogger(SupportingDocumentsUtil.class.getName());
	
	public static void saveFileToServer (String filename, byte[] content) throws IOException {
		FileOutputStream output = null;
		
		try {
			File file = new File(filename);
			
			//Make sure the parent folder exists.
			File folder = file.getParentFile();
			if (!folder.exists()) {
				// Folder doesn't exists, create it.
				if (!folder.mkdirs()) {
					logger.log(Level.SEVERE, ("Fail to create parent folder" + folder.getAbsolutePath()).replace('\n','_').replace('\n','_'));
				}
			}
			
			logger.info( String.format( "Writing file %s", filename ).replace('\n','_').replace('\r','_') );
			output = new FileOutputStream(file);
			output.write(content);
			
		} 
		catch (FileNotFoundException exception) {
			throw new IOException("Fail to read file: " + filename, exception);
		} 
		catch (IOException exception) {
			throw new IOException("Fail to read file: " + filename, exception);
		}
		finally {
			try {
				if (output != null)
					output.close();
			} 
			catch (IOException e) {
				logger.info( String.format("Fail to close the FileOutputStream", e));
			}
			
		}

	}
}
