package com.stee.spfcore.webapi.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stee.spfcore.webapi.dao.BenefitsDAO;
import com.stee.spfcore.webapi.model.benefits.BereavementGrant;
import com.stee.spfcore.webapi.model.benefits.HealthCareProvider;
import com.stee.spfcore.webapi.model.benefits.NewBornGift;
import com.stee.spfcore.webapi.model.benefits.WeddingGift;
import com.stee.spfcore.webapi.model.benefits.WeightMgmtSubsidy;
import com.stee.spfcore.webapi.service.process.IProcessService;
import com.stee.spfcore.webapi.service.process.ProcessServiceFactory;

@Service
public class BenefitsService {

	private BenefitsDAO benefitsDAO;
	IProcessService processService = ProcessServiceFactory.getInstance();
	
	@Autowired
	public BenefitsService (BenefitsDAO benefitsDAO) {
		this.benefitsDAO = benefitsDAO;
	}
	
	@Transactional
	public BereavementGrant getBereavementGrant(String applicationNumber) {
		return benefitsDAO.getBereavementGrant(applicationNumber);
	}
	
	@Transactional
	public List< BereavementGrant > searchBereavementGrant(String nric) {
		return benefitsDAO.searchBereavementGrant(nric);
	}
	
	@Transactional
	public void saveBereavementGrant(BereavementGrant bereavementGrant) {
		benefitsDAO.saveBereavementGrant(bereavementGrant);
	}
	
	@Transactional
	public NewBornGift getNewBorn(String referenceNumber) {
		return benefitsDAO.getNewBorn(referenceNumber);
	}
	
	@Transactional
	public List< NewBornGift > searchNewBorn(String nric) {
		return benefitsDAO.searchNewBorn(nric);
	}
	
	@Transactional
	public void saveNewBorn(NewBornGift newBornGift) {
		benefitsDAO.saveNewBorn(newBornGift);
	}
	
	@Transactional
	public WeddingGift getWeddingGift(String referenceNumber) {
		return benefitsDAO.getWeddingGift(referenceNumber);
	}
	
	@Transactional
	public List< WeddingGift > searchWeddingGift(String nric) {
		return benefitsDAO.searchWeddingGift(nric);
	}
	
	@Transactional
	public void saveWeddingGift(WeddingGift weddingGift) {
		benefitsDAO.saveWeddingGift(weddingGift);
	}
	
	@Transactional
	public List< String > getBereavementGrantRelationships() {
		return benefitsDAO.getBereavementGrantRelationships();
	}
	
	@Transactional
	public WeightMgmtSubsidy getWeightMgmtSubsidy(String referenceNumber) {
		return benefitsDAO.getWeightMgmtSubsidy(referenceNumber);
	}
	
	@Transactional
	public List<WeightMgmtSubsidy> searchWeightMgmtSubsidyByNric(String nric, Calendar date){
		return benefitsDAO.searchWeightMgmtSubsidyByNric(nric, date);
	}
	
	@Transactional
	public HealthCareProvider getHealthCareProviderByServiceProvider(String serviceProvider) {
		return benefitsDAO.getHealthCareProviderByServiceProvider(serviceProvider);
	}
	
	@Transactional
	public List<HealthCareProvider> getHealthCareProviderList() {
		return benefitsDAO.getHealthCareProviderList();
	}
	
	@Transactional
	public Long searchCountBereavementGrant(String deceasedIdType, String deceasedNric, String deathCertificateNumber, String referenceNumber) {
		return benefitsDAO.searchCountBereavementGrant(deceasedIdType, deceasedNric, deathCertificateNumber, referenceNumber);
	}

	@Transactional
	public List < BereavementGrant > searchBereavementGrantForLoginID(String nric) {
		return benefitsDAO.searchBereavementGrantForLoginID(nric);
	}

	@Transactional
	public List < BereavementGrant > searchBereavementGrantForLoginID(String nric, Date date) {
		return benefitsDAO.searchBereavementGrantForLoginID(nric, date);
	}

	@Transactional
	public List < WeddingGift > searchWeddingGiftForLoginID(String nric) {
		return benefitsDAO.searchWeddingGiftForLoginID(nric);
	}

	@Transactional
	public List < WeddingGift > searchWeddingGiftForLoginID(String nric, Date date) {
		return benefitsDAO.searchWeddingGiftForLoginID(nric, date);
	}

	@Transactional
	public List<NewBornGift> searchNewBornForLoginID(String nric) {
		return benefitsDAO.searchNewBornForLoginID(nric);
	}

	@Transactional
	public List < NewBornGift > searchNewBornForLoginID(String nric, Date date) {
		return benefitsDAO.searchNewBornForLoginID(nric, date);
	}

	@Transactional
	public void saveWeightManagementSubsidy(WeightMgmtSubsidy weightMgmtSubsidy) {
		benefitsDAO.saveWeightManagementSubsidy(weightMgmtSubsidy);
		
	}
	
}
