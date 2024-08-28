package com.stee.spfcore.webapi.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.stee.spfcore.webapi.model.ApplicationStatus;
import com.stee.spfcore.webapi.model.accounting.BatchFileConfig;
import com.stee.spfcore.webapi.model.benefits.SupportingDocument;
import com.stee.spfcore.webapi.model.sag.SAGAnnouncementConfig;
import com.stee.spfcore.webapi.model.sag.SAGApplication;
import com.stee.spfcore.webapi.model.sag.SAGAwardQuantum;
import com.stee.spfcore.webapi.model.sag.SAGBatchFileRecord;
import com.stee.spfcore.webapi.model.sag.SAGCoCurricularActivity;
import com.stee.spfcore.webapi.model.sag.SAGConfigSetup;
import com.stee.spfcore.webapi.model.sag.SAGDateConfigType;
import com.stee.spfcore.webapi.model.sag.SAGDonation;
import com.stee.spfcore.webapi.model.sag.SAGEventDetail;
import com.stee.spfcore.webapi.model.sag.SAGEventRsvp;
import com.stee.spfcore.webapi.model.sag.SAGFamilyBackground;
import com.stee.spfcore.webapi.model.sag.SAGInputType;
import com.stee.spfcore.webapi.model.sag.SAGInputs;
import com.stee.spfcore.webapi.model.sag.SAGPrivileges;
import com.stee.spfcore.webapi.model.sag.SAGSubInputs;
import com.stee.spfcore.webapi.utils.ConvertUtil;
import com.stee.spfcore.webapi.utils.Util;

@Repository
public class SAGApplicationDAO {
	
	private EntityManager entityManager;
	
	private static final Logger logger = LoggerFactory.getLogger(SAGApplicationDAO.class);
	
	private static final String HQL_DELETE_EVENT_RSVP_BY_ID = "DELETE FROM SAGEventRsvp rsvp WHERE rsvp.id IN (:deleteList)";
	
	public SAGApplicationDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public SAGApplication getSAGApplication(String referenceNumber) {
		Session session = entityManager.unwrap(Session.class);
		
		return (SAGApplication)session.get(SAGApplication.class, referenceNumber);
	}
	
	@SuppressWarnings("unchecked")
	public List<SAGApplication> searchSAGApplication(String nric, String childNric,
			String awardType, String financialYear, boolean isOrderAsc) {
		
		logger.info("Search SAG Applications by awardType");
		
		Session session = entityManager.unwrap(Session.class);
		
        StringBuffer queryStr = new StringBuffer();
        queryStr.append( "SELECT sag FROM SAGApplication sag" );
        boolean isWhereClauseAdded = false;

        if ( null != nric && !nric.isEmpty() ) {
            isWhereClauseAdded = true;
            queryStr.append( " WHERE sag.memberNric = :nric" );
        }

        if ( null != awardType && !awardType.isEmpty() ) {
            if ( isWhereClauseAdded ) {
                queryStr.append( " AND" );
            }
            else {
                isWhereClauseAdded = true;
                queryStr.append( " WHERE" );
            }
            queryStr.append( " sag.awardType = :awardType" );
        }

        if ( null != financialYear && !financialYear.isEmpty() ) {
            if ( isWhereClauseAdded ) {
                queryStr.append( " AND" );
            }
            else {
                isWhereClauseAdded = true;
                queryStr.append( " WHERE" );
            }
            queryStr.append( " sag.financialYear = :financialYear" );
        }

        if ( null != childNric && !childNric.isEmpty() ) {
            if ( isWhereClauseAdded ) {
                queryStr.append( " AND" );
            }
            else {
                isWhereClauseAdded = true;
                queryStr.append( " WHERE" );
            }
            queryStr.append( " sag.childNric = :childNric" );
        }

        queryStr.append( " ORDER BY sag.referenceNumber" );
        queryStr.append( ( isOrderAsc ? " ASC" : " DESC" ) );

        logger.info(queryStr.toString() );
        
        Query query = session.createQuery( queryStr.toString() );
        if ( null != nric && !nric.isEmpty() ) {
            query.setParameter( "nric", nric );
        }
        if ( null != awardType && !awardType.isEmpty() ) {
            query.setParameter( "awardType", awardType );
        }

        if ( null != financialYear && !financialYear.isEmpty() ) {
            query.setParameter( "financialYear", financialYear );
        }

        if ( null != childNric && !childNric.isEmpty() ) {
            query.setParameter( "childNric", childNric );
        }
		
		List<SAGApplication> lists = query.list();
		return lists;
	}
	
	public void addSAGApplication( SAGApplication sagApplication ) {
        Session session = entityManager.unwrap(Session.class);
        logger.info("Add New SAGApplication with ReferenceNumber = " + Util.replaceNewLine( sagApplication.getReferenceNumber() ) );
        sagApplication.preSave();
        session.save( sagApplication );

        session.flush();

    }
	
	public void updateSAGApplication( SAGApplication sagApplication ) {

        Session session = entityManager.unwrap(Session.class);
        logger.info("Update SAGApplication with ReferenceNumber = " + Util.replaceNewLine( sagApplication.getReferenceNumber() ) );
        sagApplication.preSave();

        session.get( SAGApplication.class, sagApplication.getReferenceNumber(), new LockOptions( LockMode.PESSIMISTIC_WRITE ) );
        session.merge( sagApplication );
        session.flush();
    }
	
	@SuppressWarnings("unchecked")
	public List <SAGBatchFileRecord> searchSAGBatchFileRecordByReferenceNumber (List<String> referenceNumberList) {
    	logger.info("Get List of SAG Batch File Records By Reference Number" );
    	
    	Session session = entityManager.unwrap(Session.class);
		
        StringBuffer queryStr = new StringBuffer();
        
        queryStr.append("SELECT batch FROM SAGBatchFileRecord batch where 1=1 ");
        
        if (referenceNumberList != null) {
			queryStr.append("and batch.referenceNumber in (:referenceNumberList) ");
		}
        
        Query query = session.createQuery( queryStr.toString() );
        if (referenceNumberList != null && !referenceNumberList.isEmpty()){
        	query.setParameterList("referenceNumberList", referenceNumberList);
        }
		
        return ( List< SAGBatchFileRecord > ) query.list();
	}
	
	public void saveSAGBatchFileRecord( SAGBatchFileRecord sagBatchFileRecord ) {
        logger.info("save SAG Batch File Record" );
        Session session = entityManager.unwrap(Session.class);

        session.saveOrUpdate( sagBatchFileRecord );
        session.flush();
    }
	
	public void saveSAGBatchFileRecordList( List<SAGBatchFileRecord> sagBatchFileRecordList ) {
        logger.info("save SAG Batch File Record List" );
        Session session = entityManager.unwrap(Session.class);

        for (int i = 0; i < sagBatchFileRecordList.size(); i ++) {
        	SAGBatchFileRecord record = sagBatchFileRecordList.get(i);
			 session.saveOrUpdate(record);
			 
			 if ( i % 20 == 0) { //20, same as the JDBC batch size
	             //flush a batch of inserts and release memory:
				 session.flush();
				 session.clear();
			 }
		 }
		 session.flush();
    }
	
	public void saveSAGConfigSetup( SAGConfigSetup sagConfigSetup ) {

        Session session = entityManager.unwrap(Session.class);
        logger.info("save SAG config setup " + sagConfigSetup.getId() );
        
        session.saveOrUpdate( sagConfigSetup );
        session.flush();
    }
	
    public void saveSAGPrivilege( SAGPrivileges sagPrivilege ) {
        logger.info(" save SAG Privilege " );
        Session session = entityManager.unwrap(Session.class);

        session.saveOrUpdate( sagPrivilege );
        session.flush();
    }
    
    public void deleteSAGPrivileges( List< String > privilegesIdList ) {
        logger.info(" Delete SAG Privileges " );
        Session session = entityManager.unwrap(Session.class);

        Query query = session.createQuery( "DELETE FROM SAGPrivileges privilege WHERE privilege.id IN (:deleteList)" );

        query.setParameterList( "deleteList", privilegesIdList );
        query.executeUpdate();
    }
    
    public void saveAwardQuantum( SAGAwardQuantum sagAwardQuantum ) {
        logger.info(" save SAG Award Quantum " );
        Session session = entityManager.unwrap(Session.class);

        session.saveOrUpdate( sagAwardQuantum );
        session.flush();
    }
    
    public void saveDonation( SAGDonation sagDonation ) {
        logger.info( "Save SAG Donation " );
        Session session = entityManager.unwrap(Session.class);

        session.saveOrUpdate( sagDonation );
        session.flush();
    }
    
    public void deleteDonations( List< String > donationsIdList ) {
        logger.info(" Delete SAG Donation records " );
        Session session = entityManager.unwrap(Session.class);

        Query query = session.createQuery( "DELETE FROM SAGDonation donation WHERE donation.id IN (:deleteList)" );

        query.setParameterList( "deleteList", donationsIdList );
        query.executeUpdate();
    }
    
    public void saveSAGEventDetail( SAGEventDetail sagEventDetail ) {
        logger.info("Save SAG Event detail " );
        Session session = entityManager.unwrap(Session.class);

        session.saveOrUpdate( sagEventDetail );
        session.flush();
    }
    
    public void saveSAGAnnouncementConfig( SAGAnnouncementConfig sagAnnouncementConfig ) {
        logger.info("Save SAG Announcement Config" );
        Session session = entityManager.unwrap(Session.class);

        session.saveOrUpdate( sagAnnouncementConfig );
        session.flush();
    }
    
    public void batchUpdateSAGApplication( List< SAGApplication > sagApplicationList, int batchSize ) {
    	Session session = entityManager.unwrap(Session.class);

        int count = 0;
        for ( SAGApplication sagApplication : sagApplicationList ) {
            logger.info("Add New SAGApplication with ReferenceNumber = " + Util.replaceNewLine( sagApplication.getReferenceNumber() ) );
            sagApplication.preSave();


                session.merge( sagApplication );
                count++;
                if ( count % batchSize == 0 ) {
                    session.flush();
                    session.clear();
                }

        }
        if ( count % batchSize > 0 ) {
            session.flush();
            session.clear();
        }
    }
    
    public void saveBatchFileConfig( BatchFileConfig batchFileConfig ) {
        logger.info("Save BatchFileConfig" );
        Session session = entityManager.unwrap(Session.class);

        session.saveOrUpdate( batchFileConfig );
        session.flush();
    }

	@SuppressWarnings("unchecked")
	public List<SAGApplication> searchSAGApplicationBySubmission(String nric, String financialYear) {
		logger.info("Search SAG Application for: " + nric + ", " + financialYear );
		Session session = entityManager.unwrap(Session.class);
        if ( nric != null && !nric.isEmpty() ) {
            Criteria criteria = session.createCriteria( SAGApplication.class );

            criteria.add( Restrictions.eq( "submittedBy", nric ) );

            if ( null != financialYear && !financialYear.isEmpty() ) {
                criteria.add( Restrictions.eq( "financialYear", financialYear ) );
            }

            criteria.addOrder( Order.asc( "referenceNumber" ) );

            return ( List< SAGApplication > ) criteria.list();
        }

        return null;
		
	}

    @SuppressWarnings("unchecked")
    public List<SAGApplication> searchSAGApplicationBySubmission(String nric, String financialYear, Date date) {
        logger.info("1 year - Search SAG Application for: " + nric + ", " + financialYear );
        Session session = entityManager.unwrap(Session.class);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, -1);
        Date oneYearBefore = calendar.getTime();
        if ( nric != null && !nric.isEmpty() ) {
            StringBuilder builder = new StringBuilder("FROM SAGApplication sag where sag.submittedBy = :nric");
            builder.append(" and sag.dateOfSubmission >= :oneYearBefore and sag.dateOfSubmission <= :date");
            if ( null != financialYear && !financialYear.isEmpty() ) {
                builder.append(" and sag.financialYear = :financialYear");
            }
            logger.info("searchSAGApplicationBySubmission: "+builder.toString());
            Query query = session.createQuery(builder.toString());
            query.setParameter("nric", nric);
            query.setParameter("oneYearBefore", oneYearBefore);
            query.setParameter("date", date);
            if ( null != financialYear && !financialYear.isEmpty() ) {
                query.setParameter("financialYear", financialYear);
            }
            return query.list();
        }

        return null;

    }

    @SuppressWarnings( "unchecked" )
    public List< SAGConfigSetup > searchConfigSetup( String financialYear ) {
        logger.info("Search SAG Config Setup" );

        Session session = entityManager.unwrap(Session.class);

        if ( null != financialYear ) {
            Criteria criteria = session.createCriteria( SAGConfigSetup.class );

            criteria.add( Restrictions.eq( "financialYear", financialYear ) );

            return ( List< SAGConfigSetup > ) criteria.list();
        }

        return null;
    }
	
    
    @SuppressWarnings( "unchecked" )
    public List< SAGApplication > searchSAGApplicationsByReferenceNumber( List< String > referenceNumberList ) {
    	logger.info("Search SAG Application by Reference Number");
    	Session session = entityManager.unwrap(Session.class);

        if ( referenceNumberList != null && !referenceNumberList.isEmpty() ) {
            Criteria criteria = session.createCriteria( SAGApplication.class );

            criteria.add( Restrictions.in( "referenceNumber", referenceNumberList ) );

            return ( List< SAGApplication > ) criteria.list();
        }

        return null;
    }
    
    public SAGConfigSetup getConfigSetup( String id ) {
        logger.info("Get SAG Config Setup by Id: " + id );
        Session session = entityManager.unwrap(Session.class);

        SAGConfigSetup sagConfigSetup = ( SAGConfigSetup ) session.get( SAGConfigSetup.class, id );

        //if ( null != sagConfigSetup ) {
            //logger.log( Level.INFO, "sagConfigSetup year = " + Util.replaceNewLine( sagConfigSetup.getFinancialYear() ) );
        //}

        return sagConfigSetup;
    }
    
    public SAGConfigSetup getConfigSetup( String financialYear, SAGDateConfigType configType ) {
        logger.info("Get SAG Config Setup by config type: " + configType  + " " + financialYear);
        Session session = entityManager.unwrap(Session.class);
        
        if ( null != financialYear && null != configType ) {
            Criteria criteria = session.createCriteria( SAGConfigSetup.class );

            criteria.add( Restrictions.eq( "financialYear", financialYear ) );

            criteria.add( Restrictions.disjunction().add( Restrictions.eq( "configType", configType ) ) );

            return ( SAGConfigSetup ) criteria.uniqueResult();
        }

        return null;


    }
    
    @SuppressWarnings( "unchecked" )
    public List< SAGEventDetail > searchEventDetail( String eventId, String financialYear ) {
        logger.info("Search SAG Event Detail ");

        Session session = entityManager.unwrap(Session.class);

        Criteria criteria = session.createCriteria( SAGEventDetail.class );

        if ( null != financialYear && !financialYear.isEmpty() ) {
            criteria.add( Restrictions.eq( "financialYear", financialYear ) );
        }

        if ( null != eventId && !eventId.isEmpty() ) {
            criteria.add( Restrictions.eq( "id", eventId ) );
        }

        return ( List< SAGEventDetail > ) criteria.list();
    }
    
    public SAGInputs getSAGInput( String awardType, SAGInputType inputType, String inputId ) {
    	logger.info("Get SAG input");
    	Session session = entityManager.unwrap(Session.class);

        if ( awardType != null && inputId != null && inputType != null ) {
            Criteria criteria = session.createCriteria( SAGInputs.class );

            criteria.add( Restrictions.eq( "awardType", awardType ) ).add( Restrictions.eq( "inputId", inputId ) ).add( Restrictions.eq( "sagInputType", inputType ) );

            return ( SAGInputs ) criteria.uniqueResult();
        }

        return null;
    }
    
    @SuppressWarnings( "unchecked" )
    public List< SAGInputs > getListOfSAGInputs( String awardType ) {
    	logger.info("Get list of SAG Input");
        StringBuffer queryStr = new StringBuffer();

        Session session = entityManager.unwrap(Session.class);

        queryStr.append( "Select si from SAGInputs si" );

        if ( awardType != null && !awardType.isEmpty() ) {
            queryStr.append( " where si.awardType = :awardType" );
        }

        queryStr.append( " order by si.sagInputType asc, si.order asc" );

        Query query = session.createQuery( queryStr.toString() );
        if ( awardType != null && !awardType.isEmpty() ) {
            query.setParameter( "awardType", awardType );
        }
        return ( List< SAGInputs > ) query.list();

    }
    
    @SuppressWarnings( "unchecked" )
    public List< SAGSubInputs > getSubInputListByCriteria( String awardType, String parentId, SAGInputType parentType ) {
    	logger.info("Get Sub Input List By Criteria");
    	Session session = entityManager.unwrap(Session.class);

        if ( awardType != null && parentId != null && parentType != null ) {
            Criteria criteria = session.createCriteria( SAGSubInputs.class );

            criteria.add( Restrictions.eq( "awardType", awardType ) ).add( Restrictions.eq( "parentInputId", parentId ) ).add( Restrictions.eq( "parentInputType", parentType ) ).addOrder( Order.asc( "order" ) );

            return ( List< SAGSubInputs > ) criteria.list();
        }

        return null;
    }
    
    @SuppressWarnings("unchecked")
	public List< SAGApplication > searchSimliarSAGApplication( String childNric, String financialYear, String referenceNumber ) {

        logger.info("Search Similiar SAG Applications");

    	Session session = entityManager.unwrap(Session.class);

        StringBuffer queryStr = new StringBuffer();
        queryStr.append( "SELECT sag FROM SAGApplication sag" );
        boolean isWhereClauseAdded = false;

        if ( null != financialYear && !financialYear.isEmpty() ) {
            if ( isWhereClauseAdded ) {
                queryStr.append( " AND" );
            }
            else {
                isWhereClauseAdded = true;
                queryStr.append( " WHERE" );
            }
            queryStr.append( " sag.financialYear = :financialYear" );
        }

        if ( null != childNric && !childNric.isEmpty() ) {
            if ( isWhereClauseAdded ) {
                queryStr.append( " AND" );
            }
            else {
                isWhereClauseAdded = true;
                queryStr.append( " WHERE" );
            }
            queryStr.append( " sag.childNric = :childNric" );
        }

        queryStr.append( " AND sag.referenceNumber != :referenceNumber ORDER BY sag.referenceNumber DESC" );
        logger.info("Query str = " + queryStr.toString());
        

        Query query = session.createQuery( queryStr.toString() );

        if ( null != financialYear && !financialYear.isEmpty() ) {
            query.setParameter( "financialYear", financialYear );
        }

        if ( null != childNric && !childNric.isEmpty() ) {
            query.setParameter( "childNric", childNric );
        }
        
        if ( null != referenceNumber && !referenceNumber.isEmpty() ) {
            query.setParameter( "referenceNumber", referenceNumber );
        }

        return ( List< SAGApplication > ) query.list();
    }  
    
    @SuppressWarnings( "unchecked" )
    public List< SAGEventRsvp > searchEventRsvp( String financialYear, String eventId, String refSeqNumber, String attendeeName, String attendeeId ) {
        logger.info("Search SAG Event Rsvp" );
        Session session = entityManager.unwrap(Session.class);

        Criteria criteria = session.createCriteria( SAGEventRsvp.class );

        if ( null != financialYear && !financialYear.isEmpty() ) {
            criteria.add( Restrictions.eq( "financialYear", financialYear ) );
        }

        if ( null != eventId && !eventId.isEmpty() ) {
            criteria.add( Restrictions.eq( "eventId", eventId ) );
        }

        if ( null != refSeqNumber && !refSeqNumber.isEmpty() ) {
            criteria.add( Restrictions.eq( "sequenceNumberReference", refSeqNumber ) );
        }

        if ( null != attendeeName && !attendeeName.isEmpty() ) {
            criteria.add( Restrictions.ilike( "attendeeName", attendeeName, MatchMode.ANYWHERE ) );
        }

        if ( null != attendeeId && !attendeeId.isEmpty() ) {
            criteria.add( Restrictions.eq( "attendeeId", attendeeId ) );
        }

        criteria.addOrder( Order.asc( "order" ) );

        return ( List< SAGEventRsvp > ) criteria.list();
    }
    
    public void saveEventRsvp( SAGEventRsvp sagEventRsvp) {
        logger.info("Save SAG Event Rsvp " );
        Session session = entityManager.unwrap(Session.class);

        session.saveOrUpdate( sagEventRsvp );
        session.flush();

    }
   
    public void deleteEventRsvp( List< String > rsvpIdList) {
        logger.info(" Delete SAG Event Rsvp " );
        Session session = entityManager.unwrap(Session.class);

        String queryStr = HQL_DELETE_EVENT_RSVP_BY_ID;
        Query query = session.createQuery( queryStr );

        query.setParameterList( "deleteList", rsvpIdList );
        query.executeUpdate();
    }
    
    @SuppressWarnings( "unchecked" )
    public List< SAGInputs > getListOfSAGInputByType( String awardType, SAGInputType inputType ) {
    	logger.info("Get list of SAG input by type");
        StringBuffer queryStr = new StringBuffer();

        Session session = entityManager.unwrap(Session.class);

        queryStr.append( "SELECT si FROM SAGInputs si" );

        boolean isWhereClauseAdded = false;
        if ( awardType != null && !awardType.isEmpty() ) {
            isWhereClauseAdded = true;
            queryStr.append( " WHERE si.awardType = :awardType" );
        }

        if ( inputType != null ) {
            if ( isWhereClauseAdded ) {
                queryStr.append( " AND " );
            }
            else {
                queryStr.append( " WHERE " );
            }
            queryStr.append( "si.sagInputType = :inputType" );
        }

        queryStr.append( " ORDER BY si.order ASC" );

        Query query = session.createQuery( queryStr.toString() );
        if ( awardType != null && !awardType.isEmpty() ) {
            query.setParameter( "awardType", awardType );
        }

        if ( awardType != null && !awardType.isEmpty() ) {
            query.setParameter( "inputType", inputType );
        }
        return ( List< SAGInputs > ) query.list();

    }
    
    @SuppressWarnings( "unchecked" )
    public SAGApplication searchSAGApplicationsRetrieveFamilyBackground( String nric, String childNric, String awardType, String financialYear, boolean isOrderAsc ) {

        logger.info("Search SAG Applications by awardType for member: " + nric + childNric );

        Session session = entityManager.unwrap(Session.class);

        SAGApplication sagApplication = new SAGApplication();
        SAGApplication sagApplicationTemp = new SAGApplication();
        /*select sag from SAGApplication sag where sag.memberNric = :nric and sag.childNric = :childNric
         * and sag.applicationStatus = :applicationStatus and sag.financialYear = :financialYear
         * 
         * if sag == null
         * select sag from SAGApplication sag where sag.memberNric = :nric
         * and sag.financialYear = :financialYear
         */
            
        financialYear = ConvertUtil.convertToCurrentCalendarYearString();
        String lastFinancialYear = "";
        lastFinancialYear = String.valueOf(Integer.valueOf(financialYear)-1);
        StringBuffer queryStr = new StringBuffer();
        queryStr.append( "SELECT sag FROM SAGApplication sag" );
        boolean isWhereClauseAdded = false;

        if ( null != nric && !nric.isEmpty() ) {
            isWhereClauseAdded = true;
            queryStr.append( " WHERE sag.memberNric = :nric" );
        }

        if ( null != financialYear && !financialYear.isEmpty() ) {
            if ( isWhereClauseAdded ) {
                queryStr.append( " AND" );
            }
            else {
                isWhereClauseAdded = true;
                queryStr.append( " WHERE" );
            }
            queryStr.append( " sag.financialYear = :financialYear" );
        }
        
        if ( isWhereClauseAdded ) {
            queryStr.append( " AND" );
        }
        else {
            isWhereClauseAdded = true;
            queryStr.append( " WHERE" );
        }
        queryStr.append( " sag.awardType != :awardType" );

        //queryStr.append( " ORDER BY sag.dateOfSubmission,sag.referenceNumber" );
        queryStr.append( " ORDER BY sag.dateOfSubmission " );
        queryStr.append( ( isOrderAsc ? " ASC" : " DESC" ) );
        queryStr.append( " ,sag.referenceNumber " );
        queryStr.append( ( isOrderAsc ? " ASC" : " DESC" ) );
        
        Query query = session.createQuery( queryStr.toString() );
        if ( null != nric && !nric.isEmpty() ) {
            query.setParameter( "nric", nric );
        }
        if ( null != financialYear && !financialYear.isEmpty() ) {
            query.setParameter( "financialYear", financialYear );
        }
        query.setParameter( "awardType", "SAA" );
        
        List< SAGApplication > sagApplicationList = ( List< SAGApplication > ) query.list();
        logger.info("number of sagApplicationList record: "+sagApplicationList.size());
        if(sagApplicationList.size()==0)
        {
        	logger.info("no applications applied for the current year, retrieve last year record");
        	logger.info("get last year record for the child if there is successful record");
        	queryStr = new StringBuffer();
            queryStr.append( "SELECT sag FROM SAGApplication sag" );
            isWhereClauseAdded = false;
            if ( null != nric && !nric.isEmpty() ) {
                isWhereClauseAdded = true;
                queryStr.append( " WHERE sag.memberNric = :nric" );
            }
            if ( null != lastFinancialYear && !lastFinancialYear.isEmpty() ) {
                if ( isWhereClauseAdded ) {
                    queryStr.append( " AND" );
                }
                else {
                    isWhereClauseAdded = true;
                    queryStr.append( " WHERE" );
                }
                queryStr.append( " sag.financialYear = :financialYear" );
            }
            if ( null != childNric && !childNric.isEmpty() ) {
                if ( isWhereClauseAdded ) {
                    queryStr.append( " AND" );
                }
                else {
                    isWhereClauseAdded = true;
                    queryStr.append( " WHERE" );
                }
                queryStr.append( " sag.childNric  = :childNric " );
            }

            queryStr.append( " AND sag.applicationStatus = '"+ ApplicationStatus.SUCCESSFUL.name() + "'");
            
            queryStr.append( " ORDER BY sag.dateOfSubmission, sag.referenceNumber " );
            queryStr.append( ( isOrderAsc ? " ASC" : " DESC" ) );


            query = session.createQuery( queryStr.toString() );
            if ( null != nric && !nric.isEmpty() ) {
                query.setParameter( "nric", nric );
            }
            if ( null != lastFinancialYear && !lastFinancialYear.isEmpty() ) {
                query.setParameter( "financialYear", lastFinancialYear );
            }
            if ( null != childNric && !childNric.isEmpty() ) {
                query.setParameter( "childNric", childNric );
            }
            
            List< SAGApplication > sagApplicationListChildLastYear = ( List< SAGApplication > ) query.list();
            logger.info("query: "+queryStr.toString());
            if(sagApplicationListChildLastYear.size() > 0)
            {
            	//if there are successful applications for the child in the previous financial year
            	sagApplicationTemp = sagApplicationListChildLastYear.get(0);
            	sagApplication.setChildNric(sagApplicationTemp.getChildNric());
                sagApplication.setChildName(sagApplicationTemp.getChildName());
                sagApplication.setChildDateOfBirth(sagApplicationTemp.getChildDateOfBirth());
                sagApplication.setChildEmail(sagApplicationTemp.getChildEmail());
                sagApplication.setChildCurrentSchool(sagApplicationTemp.getChildCurrentSchool());
                sagApplication.setChildMusicArtsSpecialDetails(sagApplicationTemp.getChildMusicArtsSpecialDetails());
                sagApplication.setChildCommendationDetails(sagApplicationTemp.getChildCommendationDetails());
                sagApplication.setChildOtherCurrentSchool(sagApplicationTemp.getChildOtherCurrentSchool());
                List<SupportingDocument> docList = new ArrayList<SupportingDocument>();
                sagApplication.setSupportingDocuments(docList);
                //need to populate the family background info into the sagApplication
                List<SAGFamilyBackground> childFamBackgroundDetails = new ArrayList<SAGFamilyBackground>();
                for(int i=0; i<sagApplicationTemp.getChildFamBackgroundDetails().size(); i++)
                {
               	 
               	 SAGFamilyBackground SAGFamilyBackgroundTemp = new SAGFamilyBackground();
               	 SAGFamilyBackgroundTemp.setDateofBirth(sagApplicationTemp.getChildFamBackgroundDetails().get(i).getDateofBirth());
               	 SAGFamilyBackgroundTemp.setGrossSalary(sagApplicationTemp.getChildFamBackgroundDetails().get(i).getGrossSalary());
               	 SAGFamilyBackgroundTemp.setIdNo(sagApplicationTemp.getChildFamBackgroundDetails().get(i).getIdNo());
               	 SAGFamilyBackgroundTemp.setIdType(sagApplicationTemp.getChildFamBackgroundDetails().get(i).getIdType());
               	 SAGFamilyBackgroundTemp.setName(sagApplicationTemp.getChildFamBackgroundDetails().get(i).getName());
               	 SAGFamilyBackgroundTemp.setOccupation(sagApplicationTemp.getChildFamBackgroundDetails().get(i).getOccupation());
               	 SAGFamilyBackgroundTemp.setOtherRelationship(sagApplicationTemp.getChildFamBackgroundDetails().get(i).getOtherRelationship());
               	 SAGFamilyBackgroundTemp.setRelationship(sagApplicationTemp.getChildFamBackgroundDetails().get(i).getRelationship());
               	 SAGFamilyBackgroundTemp.setSpecialAllowance(sagApplicationTemp.getChildFamBackgroundDetails().get(i).getSpecialAllowance());
               	 childFamBackgroundDetails.add(SAGFamilyBackgroundTemp);
                }
               
                sagApplication.setChildFamBackgroundDetails(childFamBackgroundDetails);
                
            }
            else
            {
            	sagApplication = null;
            }
            
        	
        }
        else
        {
        	 logger.info("existing applications for the current financial year. retrieve child info from previous year applications");
        	 logger.info("Search SAG Applications by awardType for member: " + nric + childNric );

             queryStr = new StringBuffer();
             queryStr.append( "SELECT sag FROM SAGApplication sag" );
             isWhereClauseAdded = false;

             if ( null != nric && !nric.isEmpty() ) {
                 isWhereClauseAdded = true;
                 queryStr.append( " WHERE sag.memberNric = :nric" );
             }

             if ( null != childNric && !childNric.isEmpty() ) {
                 if ( isWhereClauseAdded ) {
                     queryStr.append( " AND" );
                 }
                 else {
                     isWhereClauseAdded = true;
                     queryStr.append( " WHERE" );
                 }
                 queryStr.append( " sag.childNric = :childNric" );
             }
             
             queryStr.append( " AND sag.applicationStatus = '"+ ApplicationStatus.SUCCESSFUL.name() + "'");

             queryStr.append( " ORDER BY sag.referenceNumber" );
             queryStr.append( ( isOrderAsc ? " ASC" : " DESC" ) );

             query = session.createQuery( queryStr.toString() );
             if ( null != nric && !nric.isEmpty() ) {
                 query.setParameter( "nric", nric );
             }

             if ( null != childNric && !childNric.isEmpty() ) {
                 query.setParameter( "childNric", childNric );
             }
             List< SAGApplication > sagApplicationListChild = ( List< SAGApplication > ) query.list();
             if(sagApplicationListChild.size()>0)
             {
             //sagApplication = sagApplicationListChild.get( 0 );
             sagApplication.setChildNric(sagApplicationListChild.get(0).getChildNric());
             sagApplication.setChildName(sagApplicationListChild.get(0).getChildName());
             sagApplication.setChildDateOfBirth(sagApplicationListChild.get(0).getChildDateOfBirth());
             sagApplication.setChildEmail(sagApplicationListChild.get(0).getChildEmail());
             sagApplication.setChildCurrentSchool(sagApplicationListChild.get(0).getChildCurrentSchool());
             sagApplication.setChildMusicArtsSpecialDetails(sagApplicationListChild.get(0).getChildMusicArtsSpecialDetails());
             sagApplication.setChildCommendationDetails(sagApplicationListChild.get(0).getChildCommendationDetails());
             sagApplication.setChildOtherCurrentSchool(sagApplicationListChild.get(0).getChildOtherCurrentSchool());
             List<SupportingDocument> docList = new ArrayList<SupportingDocument>();
             /*for(int i=0; i<sagApplicationListChild.get(0).getSupportingDocuments().size(); i++)
             {
            	 SupportingDocument doc = sagApplicationListChild.get(0).getSupportingDocuments().get(i);
            	 doc.setFebId(sagApplicationListChild.get(0).getSupportingDocuments().get(i).getFebId());
            	 doc.setBpmId(sagApplicationListChild.get(0).getSupportingDocuments().get(i).getBpmId());
            	 doc.setDocumentName(sagApplicationListChild.get(0).getSupportingDocuments().get(i).getDocumentName());
            	 doc.setDocumentType(sagApplicationListChild.get(0).getSupportingDocuments().get(i).getDocumentType());
            	 doc.setOtherDocumentType(sagApplicationListChild.get(0).getSupportingDocuments().get(i).getOtherDocumentType());
            	 docList.add(doc);
             }*/
             sagApplication.setSupportingDocuments(docList);
             //need to populate the family background info into the sagApplication
             
             List<SAGFamilyBackground> childFamBackgroundDetails = new ArrayList<SAGFamilyBackground>();
             for(int i=0; i<sagApplicationList.get(0).getChildFamBackgroundDetails().size(); i++)
             {
            	 
            	 SAGFamilyBackground SAGFamilyBackgroundTemp = new SAGFamilyBackground();
            	 SAGFamilyBackgroundTemp.setDateofBirth(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getDateofBirth());
            	 SAGFamilyBackgroundTemp.setGrossSalary(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getGrossSalary());
            	 SAGFamilyBackgroundTemp.setIdNo(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getIdNo());
            	 SAGFamilyBackgroundTemp.setIdType(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getIdType());
            	 SAGFamilyBackgroundTemp.setName(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getName());
            	 SAGFamilyBackgroundTemp.setOccupation(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getOccupation());
            	 SAGFamilyBackgroundTemp.setOtherRelationship(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getOtherRelationship());
            	 SAGFamilyBackgroundTemp.setRelationship(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getRelationship());
            	 SAGFamilyBackgroundTemp.setSpecialAllowance(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getSpecialAllowance());
            	 childFamBackgroundDetails.add(SAGFamilyBackgroundTemp);
             }
             
             //sagApplication.setChildFamBackgroundDetails(null);
             sagApplication.setChildFamBackgroundDetails(childFamBackgroundDetails);
             for(int j=0; j< sagApplication.getChildFamBackgroundDetails().size(); j++)
             {
            	 logger.info("id no: "+sagApplication.getChildFamBackgroundDetails().get(j).getIdNo());
            	 logger.info("name: "+sagApplication.getChildFamBackgroundDetails().get(j).getName());
            	 logger.info("relationship: "+sagApplication.getChildFamBackgroundDetails().get(j).getRelationship());
            	 logger.info("gross salary: "+sagApplication.getChildFamBackgroundDetails().get(j).getGrossSalary());
             }
             
             }
             else
             {
            	 //sagApplication.setChildNric("");
                 //sagApplication.setChildName("");
                 //sagApplication.setChildDateOfBirth(null);
                 //sagApplication.setChildEmail("");
                 //sagApplication.setChildCurrentSchool("");
                 //sagApplication.setChildMusicArtsSpecialDetails(sagApplicationListChild.get(0).getChildMusicArtsSpecialDetails());
                // sagApplication.setChildCommendationDetails(sagApplicationListChild.get(0).getChildCommendationDetails());
                 //sagApplication.setChildOtherCurrentSchool(sagApplicationListChild.get(0).getChildOtherCurrentSchool());
                 //List<SupportingDocument> docList = new ArrayList<SupportingDocument>();
                 //sagApplication.setSupportingDocuments(docList);
                 //need to populate the family background info into the sagApplication
                 List<SAGFamilyBackground> childFamBackgroundDetails = new ArrayList<SAGFamilyBackground>();
                 for(int i=0; i<sagApplicationList.get(0).getChildFamBackgroundDetails().size(); i++)
                 {
                	 
                	 SAGFamilyBackground SAGFamilyBackgroundTemp = new SAGFamilyBackground();
                	 SAGFamilyBackgroundTemp.setDateofBirth(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getDateofBirth());
                	 SAGFamilyBackgroundTemp.setGrossSalary(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getGrossSalary());
                	 SAGFamilyBackgroundTemp.setIdNo(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getIdNo());
                	 SAGFamilyBackgroundTemp.setIdType(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getIdType());
                	 SAGFamilyBackgroundTemp.setName(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getName());
                	 SAGFamilyBackgroundTemp.setOccupation(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getOccupation());
                	 SAGFamilyBackgroundTemp.setOtherRelationship(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getOtherRelationship());
                	 SAGFamilyBackgroundTemp.setRelationship(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getRelationship());
                	 SAGFamilyBackgroundTemp.setSpecialAllowance(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getSpecialAllowance());
                	 childFamBackgroundDetails.add(SAGFamilyBackgroundTemp);
                 }
                 
                 //sagApplication.setChildFamBackgroundDetails(null);
                 sagApplication.setChildFamBackgroundDetails(childFamBackgroundDetails);
             }
             
             
        }
        return sagApplication;
    }

	public void saveSAGApplication(SAGApplication sagApplication, boolean isNewSave) {
        if ( isNewSave ) {
            addSAGApplication( sagApplication);
        }
        else {
            updateSAGApplication( sagApplication);
        }
		
	}

	@SuppressWarnings( "unchecked" )
	public List<SAGApplication> searchSAGApplicationsByChildNricAndEduLevel(String childNric, String awardType, String childNewEduLevel,
			String childHighestEduLevel, String financialYear) {
		logger.info("Search SAG Applications by child NRIC and Education Level: " + childNric );

		Session session = entityManager.unwrap(Session.class);
        
        logger.info(childNric + awardType + childNewEduLevel + childHighestEduLevel + financialYear);

        StringBuffer queryStr = new StringBuffer();
        queryStr.append( "SELECT sag FROM SAGApplication sag" );
        boolean isWhereClauseAdded = false;
        
        Date currentDate = new Date();
    	Calendar c = Calendar.getInstance();
    	c.setTime(currentDate);
    	logger.info(financialYear+c.get(Calendar.YEAR));
        
        if ( null != childNric && !childNric.isEmpty() ) {
            if ( isWhereClauseAdded ) {
                queryStr.append( " AND" );
            }
            else {
                isWhereClauseAdded = true;
                queryStr.append( " WHERE" );
            }
            queryStr.append( " sag.childNric = :childNric" );
            queryStr.append( " AND sag.awardType = :awardType" );
        
        
	        if(awardType.equals("SA")){
	        	logger.info("SA");
	        	if( null != childNewEduLevel && !childNewEduLevel.isEmpty() ){
	        		logger.info("SA child new edu lvl");
	                 queryStr.append( " AND sag.childNewEduLevel = :childNewEduLevel" );
	        	}       	
	        	
	        }
	        
	        if (awardType.equals("SAA")){
	        	logger.info("SAA");
	        	if( null != childHighestEduLevel && !childHighestEduLevel.isEmpty() ){
	        		logger.info("SAA child highest edu lvl");
	                queryStr.append( " AND sag.childHighestEduLevel = :childHighestEduLevel" );
	                
	        	}       		       	
	        }
	        
	        queryStr.append( " AND sag.applicationStatus = :applicationStatus" );
	        
	       
        	
	        if (null != financialYear && !financialYear.isEmpty()){
		        	if ( isWhereClauseAdded ) {
		                queryStr.append( " AND" );
		            }
		            else {
		                isWhereClauseAdded = true;
		                queryStr.append( " WHERE" );
		            }
		            queryStr.append( " sag.financialYear != :financialYear" );
	        }	        
        }
        
        logger.info(queryStr.toString());
        
        Query query = session.createQuery( queryStr.toString() );
        
        if ( null != childNric && !childNric.isEmpty() ) {
            query.setParameter( "childNric", childNric );
        }
        
        if ( null != awardType && !awardType.isEmpty() ) {
            query.setParameter( "awardType", awardType );
        }
        
        if(awardType.equals("SA")){
	        if ( null != childNewEduLevel && !childNewEduLevel.isEmpty() ) {
	            query.setParameter( "childNewEduLevel", childNewEduLevel );
	        }
        }
        
        if (awardType.equals("SAA")){
	        if ( null != childHighestEduLevel && !childHighestEduLevel.isEmpty() ) {
	            query.setParameter( "childHighestEduLevel", childHighestEduLevel );
	        }
        }
        
        query.setParameter( "applicationStatus", ApplicationStatus.SUCCESSFUL );
	        if ( null != financialYear && !financialYear.isEmpty() ) {
	            query.setParameter( "financialYear", financialYear );
	        }

    	return ( List< SAGApplication > ) query.list();
        
		
	}

	@SuppressWarnings("unchecked")
	public List<SAGPrivileges> searchSAGPrivileges(String financialYear, String memberNric) {
		logger.info("Search SAG Privileges" );

		Session session = entityManager.unwrap(Session.class);

        Criteria criteria = session.createCriteria( SAGPrivileges.class );

        if ( null != financialYear && !financialYear.isEmpty() ) {
            criteria.add( Restrictions.eq( "financialYear", financialYear ) );
        }

        if ( null != memberNric && !memberNric.isEmpty() ) {
            criteria.add( Restrictions.eq( "memberNric", memberNric ) );
        }
        return ( List< SAGPrivileges > ) criteria.list();
	}

}
