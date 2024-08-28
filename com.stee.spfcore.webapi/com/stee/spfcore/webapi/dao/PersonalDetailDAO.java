package com.stee.spfcore.webapi.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stee.spfcore.webapi.model.personnel.ChangeRecord;
import com.stee.spfcore.webapi.model.personnel.ContactLabel;
import com.stee.spfcore.webapi.model.personnel.Employment;
import com.stee.spfcore.webapi.model.personnel.ExtraEmploymentInfo;
import com.stee.spfcore.webapi.model.personnel.Leave;
import com.stee.spfcore.webapi.model.personnel.LeaveType;
import com.stee.spfcore.webapi.model.personnel.PersonalDetail;
import com.stee.spfcore.webapi.model.vo.personnel.PersonalPreferredContacts;
import com.stee.spfcore.webapi.utils.DateTimeUtil;

@Repository
public class PersonalDetailDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(PersonalDetailDAO.class);
	
	private EntityManager entityManager;
	
	@Autowired
	public PersonalDetailDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@SuppressWarnings("unchecked")
	public List<PersonalDetail> getPersonalDetails() {
		logger.info("Get Personal Details");
		
		Session session = entityManager.unwrap(Session.class);
		Query<PersonalDetail> query = session.createQuery("select p from PersonalDetail");
		
		List<PersonalDetail> details = query.list();
		return details;
		
	}
	

	public PersonalDetail getPersonalDetail(String nric) {
		logger.info("Get Personal Detail");
		Session session = entityManager.unwrap(Session.class);
		
		return (PersonalDetail)session.get(PersonalDetail.class, nric);
	}
	
	public Employment getEmployment( String nric ) {
		logger.info("Get Employment");
		Session session = entityManager.unwrap(Session.class);

        return ( Employment ) session.get( Employment.class, nric );
    }
	
	public void updatePersonalDetail(PersonalDetail personal) {
		logger.info("Update Personal Detail");
		Session session = entityManager.unwrap(Session.class);
		
		personal.preSave();
		
	    session.get( PersonalDetail.class, personal.getNric(), new LockOptions( LockMode.PESSIMISTIC_WRITE ) );

	    session.merge( personal );
	    session.flush();
	}
	
	public void addPersonalDetail(PersonalDetail personal) {
		logger.info("Add Personal Detail");
		Session session = entityManager.unwrap(Session.class);
		
		personal.preSave();
		
	    session.save( personal );
	    session.flush();
	}
	
	public ChangeRecord getChangeRecord( String nric ) {
		logger.info("Get Change Record");
		Session session = entityManager.unwrap(Session.class);
        return ( ChangeRecord ) session.get( ChangeRecord.class, nric );
    }
	
	public void addChangeRecord(ChangeRecord record) {
		logger.info("Add Change Record");
		Session session = entityManager.unwrap(Session.class);
		
		record.preSave();
		
	    session.save( record );
	    session.flush();
	}
	
	public void updateChangeRecord(ChangeRecord record) {
		logger.info("Update Change Record");
		Session session = entityManager.unwrap(Session.class);
		
		record.preSave();
		
	    session.merge( record );
	    session.flush();
	}
	
	public void saveExtraEmploymentInfo(ExtraEmploymentInfo info) {
		logger.info("Save Extra Employment Info");
		Session session = entityManager.unwrap(Session.class);

        session.saveOrUpdate( info );
        session.flush();
    }

	public String getPersonalName( String nric ) {
		logger.info("Get Personal Name");
		Session session = entityManager.unwrap(Session.class);

        Query query = session.createQuery( "SELECT p.name FROM PersonalDetail p where p.nric = :nric" );
        query.setParameter( "nric", nric );

        return ( String ) query.uniqueResult();
    }

	public boolean isOnFullMonthNoPayLeave(String nric, Date date) {
		logger.info("Is On Full Month No Pay Leave");
		Session session = entityManager.unwrap(Session.class);
		PersonalDetail detail = ( PersonalDetail ) session.get( PersonalDetail.class, nric );
		return this.isOnFullMonthLeave( detail, LeaveType.NO_PAY_LEAVE_CODE_IDS, date );
	}
	
    private boolean isOnLeave( PersonalDetail personalDetail, String[] leaveTypes, Date leaveDateWithoutTime ) {
    	boolean isOnLeave = false;
        if ( personalDetail != null ) {
            if ( leaveTypes != null ) {
                if ( leaveDateWithoutTime != null ) {
                    List< Leave > leaves = personalDetail.getLeaves();
                    if ( leaves != null ) {
                        for ( Leave leave : leaves ) {
                            if ( leave.getLeaveType() != null ) {
                                if ( ArrayUtils.contains( leaveTypes, leave.getLeaveType() ) ) {
                                    if ( leave.getStartDate() != null ) {
                                        if ( leave.getStartDate().getTime() <= leaveDateWithoutTime.getTime() ) {
                                            if ( leave.getEndDate() != null ) {
                                                if ( leave.getEndDate().getTime() >= leaveDateWithoutTime.getTime() ) {
                                                    isOnLeave = true;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return isOnLeave;
    }

    
    private boolean isOnFullMonthLeave( PersonalDetail personalDetail, String[] leaveTypes, Date currentDate ) {
    	boolean isOnFullMonthLeave = false;
        if ( personalDetail != null ) {
            if ( leaveTypes != null ) {
                if ( currentDate != null ) {
                    isOnFullMonthLeave = true;
                    List< Date > fullMonthDates = DateTimeUtil.getFullMonthDates( currentDate );
                    for ( Date d : fullMonthDates ) {
                        if ( !this.isOnLeave( personalDetail, leaveTypes, d ) ) {
                            isOnFullMonthLeave = false;
                            break;
                        }
                    }
                }
            }
        }
        return isOnFullMonthLeave;
    }
    
    @SuppressWarnings( "unchecked" )
    public List< PersonalPreferredContacts > getActivePersonnelPreferredContacts( List< String > departments, List< String > serviceTypes, List< String > nrics ) {
        List< PersonalPreferredContacts > personalPreferredContacts = new ArrayList< PersonalPreferredContacts >();
        Session session = entityManager.unwrap(Session.class);
        StringBuilder queryString = new StringBuilder();
        queryString.append( "SELECT p FROM PersonalDetail p WHERE p.employment.employmentStatus='3' " );
        if ( departments != null ) {
            if ( departments.size() > 0 ) {
                queryString.append( "and p.employment.organisationOrDepartment in :depts " );
            }
        }
        if ( serviceTypes != null ) {
            if ( serviceTypes.size() > 0 ) {
                queryString.append( "and p.employment.serviceType in :serviceTypes " );
            }
        }
        if ( nrics != null ) {
            if ( nrics.size() > 0 ) {
                queryString.append( "and p.nric in :nrics " );
            }
        }
        Query query = session.createQuery( queryString.toString() );
        if ( departments != null ) {
            if ( departments.size() > 0 ) {
                query.setParameterList( "depts", departments );
            }
        }
        if ( serviceTypes != null ) {
            if ( serviceTypes.size() > 0 ) {
                query.setParameterList( "serviceTypes", serviceTypes );
            }
        }
        if ( nrics != null ) {
            if ( nrics.size() > 0 ) {
                query.setParameterList( "nrics", nrics );
            }
        }
        List< PersonalDetail > results = ( List< PersonalDetail > ) query.list();
        if ( results != null ) {
            for ( PersonalDetail result : results ) {
                personalPreferredContacts.add( new PersonalPreferredContacts( result ) );
            }
        }
        logger.info( String.format( "preferred contacts count=%s", personalPreferredContacts.size() ) );
        return personalPreferredContacts;
    }
    
    @SuppressWarnings( "unchecked" )
    public List< String > getOfficeEmailAddress( List< String > nrics ) {

    	Session session = entityManager.unwrap(Session.class);

        StringBuilder builder = new StringBuilder( "select email.address from PersonalDetail as personal " );
        builder.append( "inner join personal.emailContacts as email where " );
        builder.append( "personal.nric in (:nrics) and email.label = :emailType" );

        Query query = session.createQuery( builder.toString() );
        query.setParameterList( "nrics", nrics );
        query.setParameter( "emailType", ContactLabel.WORK );

        return query.list();
    }
	
}
