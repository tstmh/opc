package com.stee.spfcore.dao;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.stee.spfcore.model.internal.ProcessingInfo;

public class ProcessingInfoDAO {

	public ProcessingInfo getProcessingInfo( String referenceNumber ) {
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();

		return (ProcessingInfo) session.get( ProcessingInfo.class,
				referenceNumber );
	}

	public void addProcessingInfo( ProcessingInfo info ) {
		saveProcessingInfo( info );
	}

	public void updateProcessingInfo( ProcessingInfo info ) {
		saveProcessingInfo( info );
	}

	public void saveProcessingInfo( ProcessingInfo info ) {
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();

		session.saveOrUpdate( info );
	}

	/**
	 * Note that this will return records that is at least 5 second old. This is
	 * need to ensure that the file is in the FEB server before attempt to
	 * download the file.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProcessingInfo> getAllPendingFileTransfer() {

		Calendar calendar = Calendar.getInstance();
		calendar.add( Calendar.SECOND, -5 );

		Session session = SessionFactoryUtil.getInstance().getCurrentSession();

		Criteria criteria = session.createCriteria( ProcessingInfo.class );

		criteria.add( Restrictions.eq( "febFileTransferred", false ) );
		criteria.add( Restrictions.lt( "updatedOn", calendar.getTime() ) );

		return (List<ProcessingInfo>) criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<ProcessingInfo> getAllPendingUpload() {

		Session session = SessionFactoryUtil.getInstance().getCurrentSession();

		Criteria criteria = session.createCriteria( ProcessingInfo.class );

		criteria.add( Restrictions.eq( "bpmFileUploaded", false ) );

		return (List<ProcessingInfo>) criteria.list();
	}

}
