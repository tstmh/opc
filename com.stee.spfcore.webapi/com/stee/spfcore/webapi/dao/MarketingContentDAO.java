package com.stee.spfcore.webapi.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;
import com.stee.spfcore.webapi.model.marketingContent.BinaryAttachment;
import com.stee.spfcore.webapi.model.marketingContent.internal.UserContentViewRecord;
import com.stee.spfcore.webapi.model.marketingContent.internal.BinaryFile;
import com.stee.spfcore.webapi.model.marketingContent.internal.HtmlFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class MarketingContentDAO {
	
	private EntityManager entityManager;
	private static final Logger logger = LoggerFactory.getLogger(MarketingContentDAO.class);
	
	public MarketingContentDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
    public void saveUserContentViewRecord( UserContentViewRecord record ) {
    	logger.info("Save User Content View Record");
    	Session session = entityManager.unwrap(Session.class);

        session.saveOrUpdate( record );

        session.flush();
    }
    
    public BinaryFile getBinaryFile( String nric, String id ) {

    	logger.info("Get Binary File");
    	Session session = entityManager.unwrap(Session.class);
        
        StringBuilder queryString = new StringBuilder();
        queryString.append( "select file from BinaryFile file where file.docId = :docId and file.contentId in ");
        queryString.append( "(select a.contentId from UserAnnouncement a where a.nric = :nric)" );
        
        Query query = session.createQuery( queryString.toString() );
        query.setParameter( "docId", id );
        query.setParameter( "nric", nric );
        
        BinaryFile binaryFile = (BinaryFile) query.uniqueResult();
        
        if (binaryFile == null) {
        	StringBuilder queryString2 = new StringBuilder();
        	
        	queryString2.append( "select file from BinaryFile file, ContentIdBinaryFileJoint fileJoint ");
        	queryString2.append("where file.docId = :docId and file.docId = fileJoint.docId and fileJoint.contentId in ");
			queryString2.append( "(select a.contentId from UserAnnouncement a where a.nric = :nric)" );
			  
			Query query2 = session.createQuery( queryString2.toString() );
			query2.setParameter( "docId", id );
			query2.setParameter( "nric", nric );
			  
			binaryFile = (BinaryFile) query2.uniqueResult();
        }
        
        return binaryFile;
    }
    
    public HtmlFile getHtmlFile( String nric, String id ) {
    	logger.info("Get Html File");
    	Session session = entityManager.unwrap(Session.class);
        
//        StringBuilder queryString = new StringBuilder();
//        queryString.append( "select file from HtmlFile file where file.contentId in ");
//        queryString.append( "(select a.contentId from UserAnnouncement a where a.nric = :nric and a.contentId = :contentId)" );
//        
//        Query query = session.createQuery( queryString.toString() );
//        query.setParameter( "contentId", id );
//        query.setParameter( "nric", nric );
//        
//        return ( com.stee.spfcore.webapi.model.marketingContent.internal.HtmlFile ) query.uniqueResult();
        StringBuilder queryString = new StringBuilder();
        queryString.append( "select file from HtmlFile file where file.contentId in ");
        queryString.append( "(select a.contentId from UserAnnouncement a where a.nric = :nric and a.contentId = :contentId)" );
        
        Query query = session.createQuery( queryString.toString() );
        query.setParameter( "contentId", id );
        query.setParameter( "nric", nric );
        
        return ( HtmlFile ) query.uniqueResult();
    }
    
    @SuppressWarnings( "unchecked" )
    public List< BinaryAttachment > getAttachments( String contentId ) {
    	logger.info("Get Attachments");
        List< BinaryAttachment > results = new ArrayList< BinaryAttachment >();
        Session session = entityManager.unwrap(Session.class);
        StringBuilder queryString = new StringBuilder();
        queryString.append( "select b.docId as docId, b.name as name, b.contentType as contentType from BinaryFile b " );
        queryString.append( "where b.contentId = :contentId " );
        queryString.append( "and b.attachment = true " );
        Query query = session.createQuery( queryString.toString() );
        query.setParameter( "contentId", contentId );
        query.setResultTransformer( new AliasToBeanResultTransformer( BinaryAttachment.class ) );
        results.addAll( query.list() );
        queryString.setLength( 0 );
        queryString.append( "select b.docId as docId, b.name as name, b.contentType as contentType from BinaryFile b " );
        queryString.append( "where b.attachment = true and b.docId in " );
        queryString.append( "(select j.docId from ContentIdBinaryFileJoint j where j.contentId = :contentId)" );
        query = session.createQuery( queryString.toString() );
        query.setParameter( "contentId", contentId );
        query.setResultTransformer( new AliasToBeanResultTransformer( BinaryAttachment.class ) );
        results.addAll( query.list() );
        return results;
    }

	public UserContentViewRecord getUserContentViewRecord(String id) {
    	logger.info("Get User Content View Record");
		Session session = entityManager.unwrap(Session.class);
		return ( UserContentViewRecord ) session.get( UserContentViewRecord.class, id );
	}
	
}
