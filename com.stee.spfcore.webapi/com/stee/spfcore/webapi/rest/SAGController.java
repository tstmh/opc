package com.stee.spfcore.webapi.rest;

import java.util.*;

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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.stee.spfcore.webapi.model.UserSessionUtil;
import com.stee.spfcore.webapi.model.accounting.BatchFileConfig;
import com.stee.spfcore.webapi.model.benefits.SupportingDocument;
import com.stee.spfcore.webapi.model.sag.SAGAnnouncementConfig;
import com.stee.spfcore.webapi.model.sag.SAGApplication;
import com.stee.spfcore.webapi.model.sag.SAGAwardQuantum;
import com.stee.spfcore.webapi.model.sag.SAGBatchFileRecord;
import com.stee.spfcore.webapi.model.sag.SAGConfigSetup;
import com.stee.spfcore.webapi.model.sag.SAGDateConfigType;
import com.stee.spfcore.webapi.model.sag.SAGDonation;
import com.stee.spfcore.webapi.model.sag.SAGEventDetail;
import com.stee.spfcore.webapi.model.sag.SAGEventRsvp;
import com.stee.spfcore.webapi.model.sag.SAGInputType;
import com.stee.spfcore.webapi.model.sag.SAGInputs;
import com.stee.spfcore.webapi.model.sag.SAGPrivileges;
import com.stee.spfcore.webapi.model.sag.SAGSubInputs;
import com.stee.spfcore.webapi.service.SAGService;
import com.stee.spfcore.webapi.service.attachments.impl.AttachmentsServiceImpl;
import com.stee.spfcore.webapi.service.process.IProcessService;
import com.stee.spfcore.webapi.service.process.ProcessServiceFactory;
import com.stee.spfcore.webapi.utils.SupportingDocumentsUtil;

@RestController
@RequestMapping("/sag")
public class SAGController {

	private SAGService service;
	private static final Logger logger = LoggerFactory.getLogger(SAGController.class);
	private AttachmentsServiceImpl attachmentService;
	IProcessService processService = ProcessServiceFactory.getInstance();
	
	@Value("${working.folder}")
	private String filePath;
	
	@Autowired
	public SAGController(SAGService service, AttachmentsServiceImpl attachmentService) {
		this.service = service;
		this.attachmentService = attachmentService;
	}
	
	@GetMapping("/getSAGApplication")
	public SAGApplication getSAGApplication(@RequestParam String referenceNumber) {
		SAGApplication sagApplication = service.getSAGApplication(referenceNumber); 
		return sagApplication;	
	}
	
	@GetMapping("/searchSAGApplication")
	public List<SAGApplication> searchSAGApplication(@RequestParam(required = false) String nric, String childNric, String awardType, String financialYear, String isOrderAsc) {
		boolean isOrderAscBool = Boolean.parseBoolean(isOrderAsc);
		List<SAGApplication> sagApplicationList = service.searchSAGApplication(nric, childNric, awardType, financialYear, isOrderAscBool); 
		return sagApplicationList;	
	}
	
//	@GetMapping("/searchSAGApplicationBySubmission")
//	public List<SAGApplication> searchSAGApplicationBySubmission(@RequestParam String nric, String financialYear) {
//		List<SAGApplication> sagApplicationList = service.searchSAGApplicationBySubmission(nric, financialYear);
//		return sagApplicationList;
//	}
	@GetMapping("/searchSAGApplicationBySubmission")
	public List<SAGApplication> searchSAGApplicationBySubmission(@RequestParam String nric, String financialYear, @RequestParam(required = false) String date) {
		if ( date != null && !date.trim().isEmpty() ) {
			Date newDate = null;
			newDate = ConvertUtil.convertFebDateControlStringToDate( date );
			List<SAGApplication> sagApplicationList = service.searchSAGApplicationBySubmission(nric, financialYear, newDate);
			return sagApplicationList;
		}
		List<SAGApplication> sagApplicationList = service.searchSAGApplicationBySubmission(nric, financialYear);
		return sagApplicationList;
	}

	
	@GetMapping("/searchConfigSetup")
	public List< SAGConfigSetup > searchConfigSetup(@RequestParam String financialYear) {
		List< SAGConfigSetup > sagConfigSetup = service.searchConfigSetup(financialYear); 
		return sagConfigSetup;	
	}
	
	@GetMapping("/searchSAGApplicationsRetrieveFamilyBackground")
	public SAGApplication searchSAGApplicationsRetrieveFamilyBackground(@RequestParam String nric, String childNric, String awardType, String financialYear, boolean isOrderAsc) {
		SAGApplication sagApplication = service.searchSAGApplicationsRetrieveFamilyBackground(nric, childNric, awardType, financialYear, isOrderAsc); 
		return sagApplication;	
	}
	
	@GetMapping("/getConfigSetupById")
	public SAGConfigSetup getConfigSetup(@RequestParam String id) {
		SAGConfigSetup setup = service.getConfigSetup(id); 
		return setup;	
	}
	
	@GetMapping("/getConfigSetupByYearType")
	public SAGConfigSetup getConfigSetup(@RequestParam String financialYear, String configType) {
		SAGDateConfigType newConfigType = SAGDateConfigType.valueOf(configType);
		SAGConfigSetup setup = service.getConfigSetup(financialYear, newConfigType); 
		return setup;	
	}
	
	@GetMapping("/searchEventDetail")
	public List< SAGEventDetail > searchEventDetail(@RequestParam(required = false) String eventId, String financialYear ){
		List< SAGEventDetail > eventDetails = service.searchEventDetail(eventId, financialYear);
		return eventDetails;
	}
	
	@GetMapping("/searchEventRsvp")
	public List< SAGEventRsvp > searchEventRsvp(@RequestParam(required = false) String financialYear, String eventId, String refSeqNumber, String attendeeName, String attendeeId ){
		List< SAGEventRsvp > eventRsvps = service.searchEventRsvp(financialYear, eventId, refSeqNumber, attendeeName, attendeeId);
		return eventRsvps;
		
	}
	
	@GetMapping("/getListOfSAGInputs")
	public List< SAGInputs > getListOfSAGInputs(@RequestParam String awardType ){
		List< SAGInputs > sagInputs = service.getListOfSAGInputs(awardType);
		return sagInputs;
	}
	
	@GetMapping("/getSubInputListByCriteria")
	public List< SAGSubInputs > getSubInputListByCriteria(@RequestParam String awardType, String parentId, String parentType){
		SAGInputType newParentType = SAGInputType.valueOf(parentType);
		List< SAGSubInputs > subInputList = service.getSubInputListByCriteria(awardType, parentId, newParentType);
		return subInputList;
	}
	
	@GetMapping("/searchSimliarSAGApplication")
	public List< SAGApplication > searchSimliarSAGApplication(@RequestParam(required = false) String childNric, String financialYear, String referenceNumber ){
		List< SAGApplication > similarApplications = service.searchSimliarSAGApplication(childNric, financialYear, referenceNumber);
		return similarApplications;
	}
	
	@GetMapping("/getListOfSAGInputByType")
	public List< SAGInputs > getListOfSAGInputByType(@RequestParam(required = false) String awardType, String inputType ){
		SAGInputType newInputType = SAGInputType.valueOf(inputType);
		List< SAGInputs > sagInputs = service.getListOfSAGInputByType(awardType, newInputType);
		return sagInputs;
	}
	
	@GetMapping("/getSAGInput")
	public SAGInputs getSAGInput(@RequestParam String awardType, String inputType, String inputId ) {
		SAGInputType newInputType = SAGInputType.valueOf(inputType);
		SAGInputs sagInput = service.getSAGInput(awardType, newInputType, inputId );
		return sagInput;
	}
	@PostMapping("/searchSAGApplicationsByReferenceNumber")
	public List< SAGApplication > searchSAGApplicationsByReferenceNumber(@RequestBody ObjectNode objectNode) {
		
		List<String> referenceNumberList = new ArrayList<String>();
		JsonNode arrNodeRefNoList = objectNode.get("referenceNumberList");
		if (arrNodeRefNoList != null && arrNodeRefNoList.isArray()) {
		    for (JsonNode objNode : arrNodeRefNoList) {
		    	referenceNumberList.add(objNode.asText());
		    }
		}
		
		List< SAGApplication > sagApplications = service.searchSAGApplicationsByReferenceNumber(referenceNumberList); 
		return sagApplications;	
	}
	
	@PostMapping("/addSAGApplication")
	public void addSAGApplication(@RequestHeader("requester") String requester, @RequestBody SAGApplication sagApplication) throws NumberFormatException, Exception {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.addSAGApplication(sagApplication);
		if(sagApplication.getApplicationStatus() == ApplicationStatus.PENDING) {
			for (SupportingDocument doc : sagApplication.getSupportingDocuments()) {
				String filename = filePath + sagApplication.getReferenceNumber() + "/" + doc.getFebId();
				byte[] content = attachmentService.getAttachment(Long.parseLong(doc.getFebId())).getData();
				SupportingDocumentsUtil.saveFileToServer(filename, content);
			}

			try {
				processService.applySAGApplication(sagApplication);
			} catch (Exception e) {
				logger.warn("Fail to process SAG application for BPM: ", e);
			}
		}
	}
	
	
	@PostMapping("/saveSAGApplication")
	public void saveSAGApplication(@RequestHeader("requester") String requester, @RequestBody SAGApplication sagApplication, @RequestParam String isNewSave) {
		requester = (requester != null) ? requester : "";
		if (isNewSave.toLowerCase() != "true" || isNewSave.toLowerCase() != "false") {
			throw new IllegalStateException("Parameter 'isNewSave' is invalid, please check and try again.");
		}
		boolean newIsNewSave = Boolean.parseBoolean(isNewSave);
		UserSessionUtil.setUser(requester);
		service.saveSAGApplication(sagApplication, newIsNewSave);
		
	}
	
	@PostMapping("/updateSAGApplication")
	public void updateSAGApplication(@RequestHeader("requester") String requester, @RequestBody SAGApplication sagApplication) throws NumberFormatException, Exception {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.updateSAGApplication(sagApplication);
		if(sagApplication.getApplicationStatus() == ApplicationStatus.PENDING) {
			for (SupportingDocument doc : sagApplication.getSupportingDocuments()) {
				String filename = filePath + sagApplication.getReferenceNumber() + "/" + doc.getFebId();
				byte[] content = attachmentService.getAttachment(Long.parseLong(doc.getFebId())).getData();
				SupportingDocumentsUtil.saveFileToServer(filename, content);
			}

			try {
				processService.applySAGApplication(sagApplication);
			} catch (Exception e) {
				logger.warn("Fail to process SAG application for BPM: ", e);
			}
		}
	}

	@PostMapping("/searchSAGBatchFileRecord")
	public List<SAGBatchFileRecord> searchSAGBatchFileRecord(@RequestBody ObjectNode objectNode) {
		List<String> referenceNumberList = new ArrayList<String>();
		JsonNode arrNodeRefNoList = objectNode.get("referenceNumberList");
		if (arrNodeRefNoList != null && arrNodeRefNoList.isArray()) {
		    for (JsonNode objNode : arrNodeRefNoList) {
		    	referenceNumberList.add(objNode.asText());
		    }
		}
		List<SAGBatchFileRecord> batchFileRecordList = service.searchSAGBatchFileRecordByReferenceNumber(referenceNumberList);
		return batchFileRecordList;
	}
	
	@PostMapping("/saveSAGBatchFileRecord")
	public void saveSAGBatchFileRecord(@RequestHeader("requester") String requester, @RequestBody SAGBatchFileRecord batchFileRecord) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.saveSAGBatchFileRecord(batchFileRecord);
		
	}
	
	@PostMapping("/saveSAGBatchFileRecordList")
	public void saveSAGBatchFileRecordList(@RequestHeader("requester") String requester, @RequestBody List<SAGBatchFileRecord> batchFileRecordList) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.saveSAGBatchFileRecordList(batchFileRecordList);
	}
	
	@PostMapping("/saveSAGConfigSetup")
	public void saveSAGConfigSetup(@RequestHeader("requester") String requester, @RequestBody SAGConfigSetup sagConfigSetup) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.saveSAGConfigSetup(sagConfigSetup);
	}
	
	@PostMapping("/saveSAGPrivilege")
	public void saveSAGPrivilege(@RequestHeader("requester") String requester, @RequestBody SAGPrivileges sagPrivileges) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.saveSAGPrivilege(sagPrivileges);
	}	
	
	@PostMapping("/deleteSAGPrivileges")
	public void deleteSAGPrivileges(@RequestHeader("requester") String requester, @RequestBody List<String> privilegesIdList) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.deleteSAGPrivileges(privilegesIdList);
	}	
	
	@PostMapping("/saveAwardQuantum")
	public void saveAwardQuantum(@RequestHeader("requester") String requester, @RequestBody SAGAwardQuantum sagAwardQuantum) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.saveAwardQuantum(sagAwardQuantum);
	}	
	
	@PostMapping("/saveDonation")
	public void saveDonation(@RequestHeader("requester") String requester, @RequestBody SAGDonation sagDonation) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.saveDonation(sagDonation);
	}	
	
	@PostMapping("/deleteDonations")
	public void deleteDonations(@RequestHeader("requester") String requester, @RequestBody List<String> donationsIdList) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.deleteDonations(donationsIdList);
	}	
	
	@PostMapping("/deleteEventRsvp")
	public void deleteEventRsvp(@RequestHeader("requester") String requester,@RequestBody List< String > rsvpIdList) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.deleteEventRsvp(rsvpIdList);
	}
	
	@PostMapping("/saveEventRsvp")
	public void saveEventRsvp(@RequestHeader("requester") String requester,@RequestBody SAGEventRsvp sagEventRsvp) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.saveEventRsvp(sagEventRsvp);
	}
	
	@PostMapping("/saveEventRsvpList")
	public void saveEventRsvpList(@RequestHeader("requester") String requester,@RequestBody List <SAGEventRsvp> sagEventRsvpList) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		if (sagEventRsvpList.size() > 0) {
			for (SAGEventRsvp eventRsvp : sagEventRsvpList) {
				service.saveEventRsvp(eventRsvp);
			}
		}
	}
	
	@PostMapping("/saveSAGEventDetail")
	public void saveSAGEventDetail(@RequestHeader("requester") String requester, @RequestBody SAGEventDetail sagEventDetail) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.saveSAGEventDetail(sagEventDetail);
	}	
	
	@PostMapping("/saveSAGAnnouncementConfig")
	public void saveSAGAnnouncementConfig(@RequestHeader("requester") String requester, @RequestBody SAGAnnouncementConfig sagAnnouncementConfig) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.saveSAGAnnouncementConfig(sagAnnouncementConfig);
	}	
	
	@PostMapping("/batchUpdateSAGApplication")
	public void batchUpdateSAGApplication(@RequestHeader("requester") String requester, @RequestBody List<SAGApplication> sagApplicationList) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.batchUpdateSAGApplication(sagApplicationList);
	}	
	
	@PostMapping("/saveBatchFileConfig")
	public void saveBatchFileConfig(@RequestHeader("requester") String requester, @RequestBody BatchFileConfig batchConfig) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.saveBatchFileConfig(batchConfig);
	}	
	
	@GetMapping("/searchSAGApplicationsByChildNricAndEduLevel")
	public List< SAGApplication > searchSAGApplicationsByChildNricAndEduLevel(@RequestParam String childNric, String awardType, String childNewEduLevel, String childHighestEduLevel, String financialYear){
		List< SAGApplication > sagApplications = service.searchSAGApplicationsByChildNricAndEduLevel(childNric, awardType, childNewEduLevel, childHighestEduLevel, financialYear);
		return sagApplications;
	}
	
	@GetMapping("/searchSAGPrivileges")
	public List<SAGPrivileges> searchSAGPrivileges(@RequestParam (required = false) String financialYear, String memberNric) {
		List<SAGPrivileges> sagPrivileges = service.searchSAGPrivileges(financialYear, memberNric); 
		return sagPrivileges;	
	}
	
	@PostMapping("/sendEmailAfterPaymentUpdate")
	public void sendEmailAfterPaymentUpdate(@RequestBody SAGApplication sagApplication) {
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("referenceNumber", sagApplication.getReferenceNumber());
		context.put("year", sagApplication.getFinancialYear());
		context.put("awardType", sagApplication.getAwardType());
		service.updateSAGApplicationPaymentProcess(context);
	}
}
