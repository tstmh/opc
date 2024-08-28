package com.stee.spfcore.service.extSysInterface.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.dao.MembershipDAO;
import com.stee.spfcore.dao.PersonnelDAO;
import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.model.membership.Membership;
import com.stee.spfcore.service.extSysInterface.IExtInterfacer;
import com.stee.spfcore.utils.JSONEncoder;

public class ExtInterfacerCommon implements IExtInterfacer {

	private static final Logger logger = Logger.getLogger( ExtInterfacerCommon.class.getName() );

    private MembershipDAO mdao;
	
	public ExtInterfacerCommon()
	{
        PersonnelDAO pdao = new PersonnelDAO();
		mdao = new MembershipDAO();
	}
	
	@Override
	public String getPersonnel(String nric) {
		return null;
	}

	@Override
	public String getMembership(String nric) {
		Membership result = null;
		String str = null;
		if ( logger.isLoggable( Level.INFO ) ) {
			logger.info(String.format("[ExtInterfacer getMembership] NRIC %s", nric));
		}
		try {
            SessionFactoryUtil.beginTransaction();

            result = mdao.getMembership( nric );
            if(result != null)
            {
            	logger.info("[ExtInterfacer getMembership] retrieved membership: "+result.toString());
            	str = JSONEncoder.getJSON(result);
            	if(str != null && logger.isLoggable( Level.INFO ))
            	{
            		logger.info(String.format("[ExtInterfacer getMembership] retrieved membership JSON: %s",str));
            	}
            	else
            	{
            		logger.info("[ExtInterfacer getMembership] json string is null");
            	}
            }
            else
            {
            	logger.info("[ExtInterfacer getMembership] membership retrieved is null");
            }
            SessionFactoryUtil.commitTransaction();
            return str;
        } catch(Exception e) {
        	logger.warning("[ExtInterfacer getMembership] error retrieving membership for NRIC "+nric);
        }
		return null;
	}

}
