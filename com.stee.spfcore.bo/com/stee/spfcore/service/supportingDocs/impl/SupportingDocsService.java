package com.stee.spfcore.service.supportingDocs.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.lingala.zip4j.exception.ZipException;

import com.stee.spfcore.dao.ProcessingInfoDAO;
import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.model.benefits.BereavementGrant;
import com.stee.spfcore.model.benefits.NewBornGift;
import com.stee.spfcore.model.benefits.WeddingGift;
import com.stee.spfcore.model.internal.ApplicationType;
import com.stee.spfcore.model.internal.ProcessingInfo;
import com.stee.spfcore.model.sag.SAGApplication;
import com.stee.spfcore.security.AccessDeniedException;
import com.stee.spfcore.service.benefits.BenefitsServiceException;
import com.stee.spfcore.service.benefits.BenefitsServiceFactory;
import com.stee.spfcore.service.benefits.IBenefitsService;
import com.stee.spfcore.service.configuration.IFileHandlingConfig;
import com.stee.spfcore.service.configuration.ServiceConfig;
import com.stee.spfcore.service.process.IProcessService;
import com.stee.spfcore.service.process.ProcessServiceException;
import com.stee.spfcore.service.process.ProcessServiceFactory;
import com.stee.spfcore.service.sag.ISAGService;
import com.stee.spfcore.service.sag.SAGServiceException;
import com.stee.spfcore.service.sag.SAGServiceFactory;
import com.stee.spfcore.service.supportingDocs.ISupportingDocsService;
import com.stee.spfcore.service.supportingDocs.SupportingDocsServiceException;
import com.stee.spfcore.utils.Util;
import com.stee.spfcore.utils.zip.ZipUtil;

public class SupportingDocsService implements ISupportingDocsService {

	private static final Logger logger = Logger
			.getLogger( SupportingDocsService.class.getName() );

	private IBenefitsService benefitsService;
	private ISAGService sagService;
	private ProcessingInfoDAO infoDao;

	private File workingFolder;
	private File ftpFolder;

	public SupportingDocsService() {
		benefitsService = BenefitsServiceFactory.getBenefitsService();
		sagService = SAGServiceFactory.getSagService();
		
		infoDao = new ProcessingInfoDAO();

		IFileHandlingConfig config = ServiceConfig.getInstance()
				.getFileHandlingConfig();

		workingFolder = new File( config.workingFolder() );
		if ( !workingFolder.exists() && !workingFolder.mkdirs() ) {
			logger.warning( "Fail to create working folder:"
					+ workingFolder.getAbsolutePath() );
		}

		ftpFolder = new File( config.ftpFolder() );
		if ( !ftpFolder.exists() && !ftpFolder.mkdirs()) {
			logger.warning( "Fail to create FTP folder:"
					+ ftpFolder.getAbsolutePath() );
		}
	}

	/**
	 * The file transfer process via SFTP is as follows:
	 * <ol>
	 * <li>In the Internet side, EJB timer service will trigger method of same
	 * name in FEBBenefitsService to create a zip file with the reference number
	 * as filename, which contains all the supporting documents for the
	 * application. The document in the zip is named using the FEB_ID.</li>
	 * <li>Windows script that is scheduled to trigger at regular interval will
	 * encrypt the zip file using SLIFT and move the file into SFTP server.</li>
	 * <li>Windows script at the Intranet side will then move the file from SFTP
	 * server into local filesystem, and decrypt the file.</li>
	 * <li>EJB timer service will trigger this method which will unzip the zip
	 * files and rename the files to using the id of the SupportingDocument as
	 * filename, and trigger the ProcessService to add document.</li>
	 * </ol>
	 * 
	 * @throws BenefitsServiceException
	 * @throws AccessDeniedException
	 */
	@Override
	public void processSupportingDocuments()
			throws SupportingDocsServiceException, AccessDeniedException {
		List<ProcessingInfo> list = null;

		try {
			SessionFactoryUtil.beginTransaction();

			list = infoDao.getAllPendingUpload();

			SessionFactoryUtil.commitTransaction();
		} catch ( Exception exception ) {
			logger.warning(String.format("Fail to retrieve pending file transfer record %s", exception ));
			SessionFactoryUtil.rollbackTransaction();
		}

		IProcessService processService = ProcessServiceFactory.getInstance();

		try {
			for ( ProcessingInfo info : list ) {
				if ( info.getType() == ApplicationType.BEREAVEMENT_GRANT 
						|| info.getType() == ApplicationType.NEWBORN_GIFT
						|| info.getType() == ApplicationType.WEDDING_GIFT ) {
					
					processBenefitsDocuments( info, processService );
				
				} else if(  info.getType() == ApplicationType.SCHOLASTIC_ACHIEVEMENT_AWARD 
						|| info.getType() == ApplicationType.STUDY_AWARD 
						|| info.getType() == ApplicationType.STUDY_GRANT ) {
					
					processSAGDocuments( info);
				}
			}
		} catch ( ProcessServiceException e ) {
			logger.warning(String.format("Fail to upload file to BPM process %s", e ));

		} catch ( Exception e ) {
			logger.warning(String.format("Fail to upload file to BPM process %s", e ));
		}
	}

	private void updateProcessInfo( ProcessingInfo info )
			throws BenefitsServiceException {

		info.setBpmFileUploaded( true );
		info.setUpdatedOn( new Date() );
		try {
			SessionFactoryUtil.beginTransaction();

			infoDao.updateProcessingInfo( info );

			SessionFactoryUtil.commitTransaction();
		} catch ( Exception exception ) {
			logger.warning(String.format("Fail to update processing info %s", exception ));
			SessionFactoryUtil.rollbackTransaction();
		}
	}

	private boolean prepareForFileUpload( ProcessingInfo info )
			throws BenefitsServiceException {

		// 1) Move the zip file from ftp folder to working folder.
		File zipFile = new File( ftpFolder, info.getReferenceNumber() + ".zip" );
		if ( !zipFile.exists() ) {
			logger.info( "File has not arrive:" + zipFile.getAbsolutePath() );
			// Doesn't exists. File has not arrive from FTP yet.
			return false;
		}

		File unzipFile = new File( workingFolder, info.getReferenceNumber()
				+ ".zip" );
		// Clean up
		if ( unzipFile.exists() ) {
			try {
				Files.delete(unzipFile.toPath());
			} catch (NoSuchFileException e) {
				// Handle the case where the file doesn't exist (optional)
				logger.warning(String.format("File '{ %s}' does not exist. Ignoring...", unzipFile.getAbsolutePath()));
			} catch (IOException e) {
				// Catch other potential IO errors
				logger.warning(String.format("An error occurred while deleting '{%s}': { %s }", unzipFile.getAbsolutePath(), e));
			}
		}

		if ( !zipFile.renameTo( unzipFile ) ) {
			logger.severe( "Fail to transfer file to working folder:"
					+ zipFile.getAbsolutePath() );
			// Move file to the working folder not successful.
			return false;
		}

		// 2) Unzip the zip file.
		try {
			ZipUtil.unzip( workingFolder.getAbsolutePath(),
					unzipFile.getAbsolutePath() );
		} catch ( ZipException e ) {
			logger.warning(String.format("Fail to unzip file for %s %s", Util.replaceNewLine( info.getReferenceNumber() ), e ));
		}

		return true;
	}
	
	private void processBenefitsDocuments( ProcessingInfo info, IProcessService processService ) throws ProcessServiceException, BenefitsServiceException, AccessDeniedException {
		if ( info.getType() == ApplicationType.BEREAVEMENT_GRANT ) {
			BereavementGrant grant = benefitsService
					.getBereavementGrant( info.getReferenceNumber() );

			if ( prepareForFileUpload( info
			) ) {
				// 4) Invoke BPM
				processService.applyBereavementGrant( grant );

				// 5) Update ProcessingInfo.
				updateProcessInfo( info );
			}
		} else if ( info.getType() == ApplicationType.NEWBORN_GIFT ) {
			NewBornGift gift = benefitsService.getNewBorn( info
					.getReferenceNumber() );
			if ( prepareForFileUpload( info
			) ) {
				// 4) Invoke BPM
				processService.applyNewBornGift( gift );

				// 5) Update ProcessingInfo.
				updateProcessInfo( info );
			}
		} else if ( info.getType() == ApplicationType.WEDDING_GIFT ) {
			WeddingGift gift = benefitsService.getWeddingGift( info
					.getReferenceNumber() );
			if ( prepareForFileUpload( info
			) ) {
				// 4) Invoke BPM
				processService.applyWeddingGift( gift );

				// 5) Update ProcessingInfo.
				updateProcessInfo( info );
			}
		}
	}
	
	private void processSAGDocuments( ProcessingInfo info )  throws ProcessServiceException, SAGServiceException, AccessDeniedException, BenefitsServiceException {
        sagService.getSAGApplication(info.getReferenceNumber());

        if ( prepareForFileUpload( info
		) ) {

			updateProcessInfo( info );
		}
		
	}

}
