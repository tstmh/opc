package com.stee.spfcore.dao.dac;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.model.blacklist.Blacklistee;
import com.stee.spfcore.model.membership.Membership;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.security.SecurityInfo;
import com.stee.spfcore.vo.benefits.BereavementGrantDetails;
import com.stee.spfcore.vo.benefits.NewBornGiftDetails;
import com.stee.spfcore.vo.benefits.WeddingGiftDetails;
import com.stee.spfcore.vo.membership.MembershipExtend;
import com.stee.spfcore.vo.membership.MembershipReport;


/**
 * Filter data based on the role of the caller.
 * In general, 
 * <ol>
 * 	<li>System user, default caller of EJB, System Lane task, etc, can see all data. Hence, no filtering<li>
 * 	<li>Welfare Officer will be able to see all data, hence no filtering.</li>
 *	<li>Unit processing officer can only see data belong to staff from same department and sub-unit.
 *	<li>Other user can only see its own data.
 * </ol>
 *
 */
public class DataFilter {

	private DataFilter(){}
	public static List<NewBornGiftDetails> filterNewBornGiftDetails (SecurityInfo info, List<NewBornGiftDetails> orgList) {
		
		// No need to filter result if caller is system user or welfare officer
		if (info.isSystemUser() || info.isWelfareOfficer()) {
			return orgList;
		}
		
		List<NewBornGiftDetails> filteredList = new ArrayList<>();
		
		List<String> allowedNrics = getAccessiblePersonnel (info);
		for (NewBornGiftDetails gift : orgList) {
			if (allowedNrics.contains(gift.getMemberNric())) {
				filteredList.add(gift);
			}
		}
		
		return filteredList;
	}
	
	/**
	 * Return a list of personnel that caller can access.
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getAccessiblePersonnel (SecurityInfo info) {
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		List<String> nrics = null;
		
		if (info.isSPFUnitProcessingOfficer()) {
			
			Query query = session
					.createQuery( "SELECT c.nric from Employment c, Employment d where c.organisationOrDepartment = d.organisationOrDepartment and d.nric = :nric" );
			query.setParameter( "nric", info.getUsername());
			
			nrics = query.list();
		}
		else if (info.isNonSPFUnitProcessingOfficer()) {
			
			Query query = session
					.createQuery( "SELECT c.nric from Employment c, Employment d where c.organisationOrDepartment = d.organisationOrDepartment " +
													" and c.subunit = d.subunit and d.nric = :nric" );
			query.setParameter( "nric", info.getUsername());
			
			nrics = query.list();
		}
		else {
			// Normal user can only see own data
			nrics = new ArrayList<>();
			nrics.add(info.getUsername());
		}
		
		return nrics;
	}
	
	
	public static List<WeddingGiftDetails> filterWeddingGiftDetails (SecurityInfo info, List<WeddingGiftDetails> orgList) {
		
		// No need to filter result if caller is system user or welfare officer
		if (info.isSystemUser() || info.isWelfareOfficer()) {
			return orgList;
		}
		
		List<WeddingGiftDetails> filteredList = new ArrayList<>();
		
		List<String> allowedNrics = getAccessiblePersonnel (info);
		for (WeddingGiftDetails gift : orgList) {
			if (allowedNrics.contains(gift.getMemberNric())) {
				filteredList.add(gift);
			}
		}
		
		return filteredList;
	}
	
	
	public static List<BereavementGrantDetails> filterBereavementGrantDetails (SecurityInfo info, 
														List<BereavementGrantDetails> orgList) {
		
		// No need to filter result if caller is system user or welfare officer
		if (info.isSystemUser() || info.isWelfareOfficer()) {
			return orgList;
		}
		
		List<BereavementGrantDetails> filteredList = new ArrayList<>();
		
		List<String> allowedNrics = getAccessiblePersonnel (info);
		for (BereavementGrantDetails gift : orgList) {
			if (allowedNrics.contains(gift.getMemberNric())) {
				filteredList.add(gift);
			}
		}
		
		return filteredList;
	}

	public static List<PersonalDetail> filterPersonalDetails (SecurityInfo info,
			List<PersonalDetail> orgList) {

		// No need to filter result if caller is system user or welfare officer
		if (info.isSystemUser() || info.isWelfareOfficer()) {
			return orgList;
		}

		List<PersonalDetail> filteredList = new ArrayList<>();

		List<String> allowedNrics = getAccessiblePersonnel(info);
		for (PersonalDetail personalDetail : orgList) {
			if (allowedNrics.contains(personalDetail.getNric())) {
				filteredList.add(personalDetail);
			}
		}

		return filteredList;
	}

	public static List<MembershipReport> filterMembershipReports (SecurityInfo info,
			List<MembershipReport> orgList) {

		// No need to filter result if caller is system user or welfare officer
		if (info.isSystemUser() || info.isWelfareOfficer()) {
			return orgList;
		}

		List<MembershipReport> filteredList = new ArrayList<>();

		List<String> allowedNrics = getAccessiblePersonnel(info);
		for (MembershipReport report : orgList) {
			if (allowedNrics.contains(report.getNric())) {
				filteredList.add(report);
			}
		}

		return filteredList;
	}
	
	
	public static List<Membership> filterMemberships (SecurityInfo info,
			List<Membership> orgList) {

		// No need to filter result if caller is system user or welfare officer
		if (info.isSystemUser() || info.isWelfareOfficer()) {
			return orgList;
		}

		List<Membership> filteredList = new ArrayList<>();

		List<String> allowedNrics = getAccessiblePersonnel(info);
		for (Membership membership : orgList) {
			if (allowedNrics.contains(membership.getNric())) {
				filteredList.add(membership);
			}
		}

		return filteredList;
	}
	
	
	public static List<MembershipExtend> filterMembershipExtendList (SecurityInfo info,
			List<MembershipExtend> orgList) {

		// No need to filter result if caller is system user or welfare officer
		if (info.isSystemUser() || info.isWelfareOfficer()) {
			return orgList;
		}

		List<MembershipExtend> filteredList = new ArrayList<>();

		List<String> allowedNrics = getAccessiblePersonnel(info);
		for (MembershipExtend membership : orgList) {
			if (allowedNrics.contains(membership.getPersonalDetail().getNric())) {
				filteredList.add(membership);
			}
		}

		return filteredList;
	}
	
	public static List<Blacklistee> filterBlacklistees (SecurityInfo info, List<Blacklistee> orgList) {

		// No need to filter result if caller is system user or welfare officer
		if (info.isSystemUser() || info.isWelfareOfficer()) {
			return orgList;
		}

		List<Blacklistee> filteredList = new ArrayList<>();

		List<String> allowedNrics = getAccessiblePersonnel(info);
		for (Blacklistee blacklistee : orgList) {
			if (allowedNrics.contains(blacklistee.getNric())) {
				filteredList.add(blacklistee);
			}
		}

		return filteredList;
	}
}
