package com.stee.spfcore.webapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stee.spfcore.webapi.dao.MembershipDAO;
import com.stee.spfcore.webapi.model.membership.DiscrepancyRecord;
import com.stee.spfcore.webapi.model.membership.Membership;
import com.stee.spfcore.webapi.model.membership.MembershipPaymentCheckRecord;
import com.stee.spfcore.webapi.model.membership.PaymentDataSource;

@Service
public class MembershipService {

	private MembershipDAO membershipDAO;
	
	@Autowired
	public MembershipService (MembershipDAO membershipDAO) {
		this.membershipDAO = membershipDAO;
	}
	
	@Transactional
	public Membership getMembership(String nric) {
		return membershipDAO.getMembership(nric);
	}
	
	@Transactional
	public void updateMembership(Membership membership) {
		membershipDAO.updateMembership(membership);
	}
	
	@Transactional
	public void addMembership(Membership membership) {
		membershipDAO.updateMembership(membership);
	}
	
	@Transactional
	public void saveDiscrepancyRecord(DiscrepancyRecord discrepencyRecord) {
		membershipDAO.saveDiscrepancyRecord(discrepencyRecord);
	}
	
	@Transactional
	public int deleteDiscrepancyRecord(String nric, Integer month, Integer year) {
		return membershipDAO.deleteDiscrepancyRecord(nric, month, year);
	}
	
	@Transactional
	public int deletePaymentHistories(Integer month, Integer year, PaymentDataSource source) {
		return membershipDAO.deletePaymentHistories(month, year, source);
	}
	
	@Transactional
	public void addMembershipPaymentCheckRecord(MembershipPaymentCheckRecord checkRecord) {
		membershipDAO.addMembershipPaymentCheckRecord(checkRecord);
	}
	
}
