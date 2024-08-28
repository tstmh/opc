package com.stee.spfcore.utils;
import java.util.logging.Logger;

import org.json.simple.JSONObject;

import com.stee.spfcore.model.membership.Membership;

/**
 * Used for SPFCORE Interface used by external system.
 * <br>Implement the required getJSON() overloader here
 * @author Sebastian
 *
 */
public class JSONEncoder {

	private static final Logger logger = Logger.getLogger( JSONEncoder.class.getName() );
	
	
	private JSONEncoder()
	{
		
	}
	@SuppressWarnings("unchecked")
	public static String getJSON(Membership mbr)
	{
		if(mbr == null)
		{
			logger.info("[JSONEncoder] Membership passed is null, skipping");
			return null;
		}
		JSONObject jo = new JSONObject();
		jo.put("NRIC",mbr.getNric());
		jo.put("MembershipType", mbr.getMembershipType());
		jo.put("MembershipStatus", mbr.getMembershipStatus());
		jo.put("EffectiveDate", mbr.getEffectiveDate());
		jo.put("EffectiveDateOfCoverage",mbr.getEffectiveDateOfCoverage());
		jo.put("ExpiryDate",mbr.getExpiryDate());
		jo.put("CessationDate",mbr.getDateOfCessation());
		jo.put("CessationDateUpdatedBy",mbr.getCessationDateUpdatedBy());
		jo.put("CessationDateUpdatedOn",mbr.getCessationDateUpdatedOn());
		jo.put("MembershipCardIssuedOn",mbr.getDateOfMembershipCardIssued());
		jo.put("NSCardIssuedOn",mbr.getDateOfNSCardIssued());
		jo.put("WithdrawalDate",mbr.getDateOfWithdrawalRequest());
		jo.put("FinanceOfficerRemarks",mbr.getFinanceOfficerRemarks());
		jo.put("RequestedDateOfTermination",mbr.getRequestedDateOfTermination());
		jo.put("UpdatedBy",mbr.getUpdatedBy());
		jo.put("UpdatedOn",mbr.getUpdatedOn());
		jo.put("Remarks",mbr.getRemarks());
		
		return jo.toJSONString();
	}
	
}
