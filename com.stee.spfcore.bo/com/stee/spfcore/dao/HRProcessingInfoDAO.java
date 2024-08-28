package com.stee.spfcore.dao;

import org.hibernate.Session;

import com.stee.spfcore.model.personnel.internal.HRProcessingInfo;

public class HRProcessingInfoDAO {

	public HRProcessingInfo getHRProcessingInfo (String nric) {
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();

		return (HRProcessingInfo) session.get(HRProcessingInfo.class, nric);
	}
	
	public void saveProcessingInfo (HRProcessingInfo info) {
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();

		session.saveOrUpdate( info );
	}

	
	
}
