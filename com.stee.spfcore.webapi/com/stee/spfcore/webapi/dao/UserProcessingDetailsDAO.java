package com.stee.spfcore.webapi.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stee.spfcore.webapi.model.OperationCode;
import com.stee.spfcore.webapi.model.ProcessFlag;
import com.stee.spfcore.webapi.model.UserProcessingDetails;
import com.stee.spfcore.webapi.model.sag.SAGBatchFileRecord;


@Repository
public class UserProcessingDetailsDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(UserProcessingDetailsDAO.class);
	
	private EntityManager entityManager;
	
	@Autowired
	public UserProcessingDetailsDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	public List<UserProcessingDetails> getUserProcessingDetails(ProcessFlag processFlag, OperationCode operationCode) {
		logger.info(String.format("Get UserProcessingDetails >> processFlag:%s operationCode:%s", processFlag,  operationCode));
		
		Session session = entityManager.unwrap(Session.class);

		String sQuery = "SELECT pd FROM UserProcessingDetails as pd WHERE 1=1 ";
		
		if (processFlag != null) {
			sQuery += "and pd.processFlag = :processFlag ";
        }
        if (operationCode != null) {
        	sQuery += "and pd.operationCode = :operationCode ";
        }
        
        logger.info(String.format("Get UserProcessingDetails >> %s", sQuery));
		
        Query query = session.createQuery( sQuery );
        if (processFlag != null) {
        	query.setParameter( "processFlag", processFlag );
        }
        if (operationCode != null) {
        	query.setParameter( "operationCode", operationCode );
        } 
        
        List<?> list = query.list();
        if (list != null) {
        	logger.info("Finish getUserProcessingDetails >> size:" + list.size());
        }
        
        return ( List<UserProcessingDetails> ) list;
	}
	
	public void updateUserProcessingDetailsList (List<UserProcessingDetails> detailsList) {
		logger.info("Update User Processing Details List");
		Session session = entityManager.unwrap(Session.class);

	    for (UserProcessingDetails details : detailsList) {
	    	session.merge( details );
	    }
	    session.flush();
	}
	
}
