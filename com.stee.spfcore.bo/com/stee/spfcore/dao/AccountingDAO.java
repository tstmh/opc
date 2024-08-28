package com.stee.spfcore.dao;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Query;
import org.hibernate.Session;

import com.stee.spfcore.model.accounting.BankInformation;
import com.stee.spfcore.model.accounting.BatchFileConfig;

public class AccountingDAO {
	
	private static final Logger logger = Logger.getLogger( AccountingDAO.class.getName() );
	
	public Object getBankInformation (String id) {

		Session session = SessionFactoryUtil.getCurrentSession();
		
		return session.get(BankInformation.class, id);
	}
	
	@SuppressWarnings( "unchecked" )
    public List< BatchFileConfig > getBatchFileConfig() {
        logger.log( Level.INFO, "Get BatchFileConfig" );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        Query query = session.createQuery("FROM BatchFileConfig batch order by batch.financialYear desc");

		return query.list();

    }
	
	@SuppressWarnings( "unchecked" )
    public List< BatchFileConfig > searchBatchFileConfigByYear( String financialYear ) {
        logger.log( Level.INFO, "Search BatchFileConfig By Year" );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        
        if ( null != financialYear ) {
            StringBuilder queryStr = new StringBuilder();
            queryStr.append("select bfc from BatchFileConfig bfc where bfc.financialYear = :financialYear ");
    		
            Query query = session.createQuery(queryStr.toString());
            query.setParameter("financialYear", financialYear);
    		
    		List<BatchFileConfig> batchFileConfigList = (List<BatchFileConfig>)query.list();
    		logger.info("finish retrieving BatchFileConfig");
    		return batchFileConfigList;
        }
        return null;
    }
	
	@SuppressWarnings( "unchecked" )
    public List<String> getOfficerNameByReferenceNumber( String referenceNumber ) {
        logger.log( Level.INFO, "Search Officer Name By Reference Number" );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        
        if ( null != referenceNumber ) {
            StringBuilder queryStr = new StringBuilder();
            queryStr.append("select pd.name from PersonalDetail pd where pd.nric in (select sag.memberNric from SAGApplication sag where sag.referenceNumber = :referenceNumber)");
    		
            Query query = session.createQuery(queryStr.toString());
            query.setParameter("referenceNumber", referenceNumber);
    		
    		List<String> officerName = query.list();
    		logger.info("finish Searching Officer Name By Reference Number");
    		return officerName;
        }
        return Collections.emptyList();
    }

    public void saveBatchFileConfig( BatchFileConfig batchFileConfig, String requestor ) {
        logger.log( Level.INFO, "Save BatchFileConfig" );
        SessionFactoryUtil.setUser( requestor );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        session.saveOrUpdate( batchFileConfig );
        session.flush();
    }
    
    @SuppressWarnings( "unchecked" )
    public List< BankInformation > getBankInformation() {
        logger.log( Level.INFO, "Get Bank Information" );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        Query query = session.createQuery("FROM BankInformation bank order by bank.bankName asc");

		return query.list();
    }
    
    @SuppressWarnings( "unchecked" )
    public List< BankInformation > getBankInformationFromCode(String bankCode) {
        logger.log( Level.INFO, "Get Bank Information From Bank Code" );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuilder queryString = new StringBuilder();
        queryString.append( "select bank from BankInformation bank where 1=1" );
        if ( bankCode != null ) {
            queryString.append( "and bank.bankCode = :bankCode " );
        }
        queryString.append( "order by bank.bankName asc" );
        
        Query query = session.createQuery( queryString.toString() );
        if ( bankCode != null ) {
            query.setParameter( "bankCode", bankCode );
        }
        
        return ( List< BankInformation > ) query.list();
    }
    
    @SuppressWarnings( "unchecked" )
    public List< BankInformation > getBankInformationFromSwiftCode(String swiftBicCode) {
        logger.log( Level.INFO, "Get Bank Information From Bank Swift BIC Code" );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuilder queryString = new StringBuilder();
        queryString.append( "select bank from BankInformation bank where 1=1" );
        if ( swiftBicCode != null ) {
            queryString.append( "and bank.swiftBicCode = :swiftBicCode " );
        }
        queryString.append( "order by bank.bankName asc" );
        
        Query query = session.createQuery( queryString.toString() );
        if ( swiftBicCode != null ) {
            query.setParameter( "swiftBicCode", swiftBicCode );
        }
        
        return ( List< BankInformation > ) query.list();
    }
    
}
