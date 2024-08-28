package com.stee.spfcore.webapi.dao;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.stee.spfcore.webapi.model.accounting.BankInformation;

@Repository
public class AccountingDAO {
	
	private EntityManager entityManager;
	private static final Logger logger = LoggerFactory.getLogger(AccountingDAO.class);
	
	public AccountingDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
    @SuppressWarnings( "unchecked" )
    public List< BankInformation > getBankInformation() {
    	logger.info("Get Bank Information");
    	Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("FROM BankInformation bank order by bank.bankName asc");
        
		return query.list();
    }
	
}
