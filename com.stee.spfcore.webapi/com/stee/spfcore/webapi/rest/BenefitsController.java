package com.stee.spfcore.webapi.rest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.stee.spfcore.webapi.model.ApplicationStatus;
import com.stee.spfcore.webapi.utils.ConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stee.spfcore.webapi.model.UserSessionUtil;
import com.stee.spfcore.webapi.model.benefits.BereavementGrant;
import com.stee.spfcore.webapi.model.benefits.HealthCareProvider;
import com.stee.spfcore.webapi.model.benefits.NewBornGift;
import com.stee.spfcore.webapi.model.benefits.SupportingDocument;
import com.stee.spfcore.webapi.model.benefits.WeddingGift;
import com.stee.spfcore.webapi.model.benefits.WeightMgmtSubsidy;
import com.stee.spfcore.webapi.service.BenefitsService;
import com.stee.spfcore.webapi.service.attachments.IAttachmentsService;
import com.stee.spfcore.webapi.service.attachments.impl.AttachmentsServiceImpl;
import com.stee.spfcore.webapi.service.process.IProcessService;
import com.stee.spfcore.webapi.service.process.ProcessServiceFactory;
import com.stee.spfcore.webapi.utils.SupportingDocumentsUtil;

@RestController
@RequestMapping("/benefits")
public class BenefitsController {

	private BenefitsService service;
	private AttachmentsServiceImpl attachmentService;
	private static final Logger logger = LoggerFactory.getLogger(BenefitsController.class);
	IProcessService processService = ProcessServiceFactory.getInstance();
	
	@Value("${working.folder}")
	private String filePath;
	
	@Autowired
	public BenefitsController(BenefitsService service, AttachmentsServiceImpl attachmentService) {
		//comments
		this.service = service;
		this.attachmentService= attachmentService;
	}
	
	@GetMapping("/getBereavementGrant")
	public BereavementGrant getBereavementGrant(@RequestParam String referenceNumber) {
		logger.info("getBereavementGrant: Starting service.");
		BereavementGrant bereavementGrant = service.getBereavementGrant(referenceNumber); 
		if (bereavementGrant != null) {
			logger.info("getBereavementGrant: Success!");
			return bereavementGrant;	                                                                                                                 
		}
		logger.info("getBereavementGrant: Object returned is empty, please verify input parameters.");
		//throw new IllegalStateException("Object returned is empty, please verify input parameters.");
		return null;
	}
	
	@GetMapping("/searchBereavementGrant")
	public List< BereavementGrant > searchBereavementGrant(@RequestParam String nric) {
		logger.info("searchBereavementGrant: Starting service.");
		List< BereavementGrant > bereavementGrantList = service.searchBereavementGrant(nric); 
		if (bereavementGrantList != null) {
			return bereavementGrantList;	
		}
		logger.info("getBereavementGrant: Object returned is empty, please verify input parameters.");
		return null;
	}
	
	@PostMapping("/saveBereavementGrant")
	public void saveBereavementGrant(@RequestHeader("requester") String requester, @RequestBody BereavementGrant bereavementGrant) throws NumberFormatException, Exception {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.saveBereavementGrant(bereavementGrant);
		if(bereavementGrant.getApplicationStatus() == ApplicationStatus.PENDING) {
			for (SupportingDocument doc : bereavementGrant.getSupportingDocuments()) {
				String filename = filePath + bereavementGrant.getReferenceNumber() + "/" + doc.getFebId();
				byte[] content = attachmentService.getAttachment(Long.parseLong(doc.getFebId())).getData();
				SupportingDocumentsUtil.saveFileToServer(filename, content);
			}

			try {
				processService.applyBereavementGrant(bereavementGrant);
			} catch (Exception e) {
				logger.warn("Fail to process Bereavement application for BPM: ", e);
			}
		}
		
	}
	
	@GetMapping("/getNewBorn")
	public NewBornGift getNewBornGift(@RequestParam String referenceNumber) {
		NewBornGift newBornGift = service.getNewBorn(referenceNumber); 
		return newBornGift;	
	}
	
	@GetMapping("/searchNewBorn")
	public List< NewBornGift > searchNewBorn(@RequestParam String nric) {
		List< NewBornGift > newBornGiftList = service.searchNewBorn(nric); 
		return newBornGiftList;	
	}
	
	@PostMapping("/saveNewBorn")
	public void saveNewBornGift(@RequestHeader("requester") String requester, @RequestBody NewBornGift newBornGift) throws NumberFormatException, Exception {

		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.saveNewBorn(newBornGift);
		if (newBornGift.getApplicationStatus() == ApplicationStatus.PENDING) {
			for (SupportingDocument doc : newBornGift.getSupportingDocuments()) {
				String filename = filePath + newBornGift.getReferenceNumber() + "/" + doc.getFebId();
				byte[] content = attachmentService.getAttachment(Long.parseLong(doc.getFebId())).getData();
				SupportingDocumentsUtil.saveFileToServer(filename, content);
			}

			try {
				processService.applyNewBornGift(newBornGift);
			} catch (Exception e) {
				logger.warn("Fail to process NewBorn application for BPM: ", e);
			}
		}
	}
	
	@GetMapping("/getWeddingGift")
	public WeddingGift getWeddingGift(@RequestParam String referenceNumber) {
		WeddingGift weddingGift = service.getWeddingGift(referenceNumber); 
		return weddingGift;	
	}
	
	@GetMapping("/searchWeddingGift")
	public List< WeddingGift > searchWeddingGift(@RequestParam String nric) {
		List< WeddingGift > weddingGiftList = service.searchWeddingGift(nric); 
		return weddingGiftList;	
	}
	
	@PostMapping("/saveWeddingGift")
	public void saveWeddingGift(@RequestHeader("requester") String requester, @RequestBody WeddingGift weddingGift) throws NumberFormatException, Exception {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.saveWeddingGift(weddingGift);
		if(weddingGift.getApplicationStatus() == ApplicationStatus.PENDING) {
			for (SupportingDocument doc : weddingGift.getSupportingDocuments()) {
				String filename = filePath + weddingGift.getReferenceNumber() + "/" + doc.getFebId();
				byte[] content = attachmentService.getAttachment(Long.parseLong(doc.getFebId())).getData();
				SupportingDocumentsUtil.saveFileToServer(filename, content);
			}

			try {
				processService.applyWeddingGift(weddingGift);
			} catch (Exception e) {
				logger.warn("Fail to process NewBorn application for BPM: ", e);
			}
		}
	}
	
	@GetMapping("/getBereavementGrantRelationships")
	public List< String > getBereavementGrantRelationships() {
		List< String > relationshipList = service.getBereavementGrantRelationships(); 
		return relationshipList;	
	}
	
	@GetMapping("/searchCountBereavementGrant")
	public Long searchCountBereavementGrant(@RequestParam (required = false) String deceasedIdType, String deceasedNric, String deathCertificateNumber, String referenceNumber) {
		Long result = service.searchCountBereavementGrant(deceasedIdType, deceasedNric, deathCertificateNumber, referenceNumber); 
		return result;	
	}
	
//	@GetMapping("/searchBereavementGrantForLoginID")
//	public List< BereavementGrant > searchBereavementGrantForLoginID(@RequestParam String nric) {
//		List< BereavementGrant > bereavementGrantList = service.searchBereavementGrantForLoginID(nric);
//		return bereavementGrantList;
//	}

	@GetMapping("/searchBereavementGrantForLoginID")
	public List< BereavementGrant > searchBereavementGrantForLoginID(@RequestParam String nric, @RequestParam (required = false) String date) {
		if ( date != null && !date.trim().isEmpty() ) {
			Date newDate = null;
			newDate = ConvertUtil.convertFebDateControlStringToDate( date );
			logger.info("searchBereavementGrantForLoginID newDate: {}", newDate.toString());
			List< BereavementGrant > bereavementGrantList = service.searchBereavementGrantForLoginID(nric, newDate);
			return bereavementGrantList;
		}
		List< BereavementGrant > bereavementGrantList = service.searchBereavementGrantForLoginID(nric);
		return bereavementGrantList;
	}
	
//	@GetMapping("/searchWeddingGiftForLoginID")
//	public List< WeddingGift > searchWeddingGiftForLoginID(@RequestParam String nric) {
//		List< WeddingGift > weddingGiftList = service.searchWeddingGiftForLoginID(nric);
//		return weddingGiftList;
//	}

	@GetMapping("/searchWeddingGiftForLoginID")
	public List< WeddingGift > searchWeddingGiftForLoginID(@RequestParam String nric, @RequestParam(required = false) String date) {
		if ( date != null && !date.trim().isEmpty() ) {
			Date newDate = null;
			newDate = ConvertUtil.convertFebDateControlStringToDate( date );
			logger.info("searchWeddingGiftForLoginID newDate: {}", newDate.toString());
			List< WeddingGift > weddingGiftList = service.searchWeddingGiftForLoginID(nric, newDate);
			return weddingGiftList;
		}

		List< WeddingGift > weddingGiftList = service.searchWeddingGiftForLoginID(nric);
		return weddingGiftList;
	}
	
//	@GetMapping("/searchNewBornForLoginID")
//	public List< NewBornGift > searchNewBornForLoginID(@RequestParam String nric) {
//		List< NewBornGift > newBornList = service.searchNewBornForLoginID(nric);
//		return newBornList;
//	}
	@GetMapping("/searchNewBornForLoginID")
	public List< NewBornGift > searchNewBornForLoginID(@RequestParam String nric, @RequestParam(required = false) String date) {
		if ( date != null && !date.trim().isEmpty() ) {
			Date newDate = null;
			newDate = ConvertUtil.convertFebDateControlStringToDate( date );
            logger.info("searchNewBornForLoginID newDate: {}", newDate.toString());
			List< NewBornGift > newBornList = service.searchNewBornForLoginID(nric, newDate);
			return newBornList;
		}
		List< NewBornGift > newBornList = service.searchNewBornForLoginID(nric);
		return newBornList;
	}

}
