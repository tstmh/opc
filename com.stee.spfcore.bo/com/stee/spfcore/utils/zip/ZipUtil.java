package com.stee.spfcore.utils.zip;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class ZipUtil {

	private ZipUtil(){}
	public static void zip(String folder, String zipFilename) throws ZipException {

		// Initiate ZipFile object with the path/name of the zip file.
		ZipFile zipFile = new ZipFile(zipFilename);

		// Initiate Zip Parameters which define various properties such
		// as compression method, etc.
		ZipParameters parameters = new ZipParameters();

		// set compression method to store compression
		parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

		// Set the compression level
		parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

		// Add folder to the zip file
		zipFile.addFolder(folder, parameters);

	}

	public static void unzip(String folder, String zipFilename) throws ZipException {

		// Initiate ZipFile object with the path/name of the zip file.
		ZipFile zipFile = new ZipFile(zipFilename);

		// Extracts all files to the path specified
		zipFile.extractAll(folder);

	}

}
