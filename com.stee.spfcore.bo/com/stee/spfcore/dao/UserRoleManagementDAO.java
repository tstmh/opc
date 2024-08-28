package com.stee.spfcore.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;

import com.stee.spfcore.dao.dac.DataAccessCheck;
import com.stee.spfcore.model.code.CodeType;
import com.stee.spfcore.model.personnel.Employment;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.model.userRoleManagement.UrmBpmGroup;
import com.stee.spfcore.model.userRoleManagement.UrmBrpGroupUser;
import com.stee.spfcore.security.AccessDeniedException;
import com.stee.spfcore.security.SecurityInfo;
import com.stee.spfcore.service.configuration.IDAOSessionConfig;
import com.stee.spfcore.vo.code.CodeNamedValuePair;
import com.stee.spfcore.vo.userRoleManagement.BpmsGroup;
import com.stee.spfcore.vo.userRoleManagement.BpmsUser;
import com.stee.spfcore.vo.userRoleManagement.SearchBpmsGroupResults;
import com.stee.spfcore.vo.userRoleManagement.SearchBpmsUserResults;

public class UserRoleManagementDAO {
    private static final Logger LOGGER = Logger.getLogger( UserRoleManagementDAO.class.getName() );
    private static final String[] NON_SPF_SERVICE_TYPES = new String[] { "000", "111" };
    private static final String SQL_ESCAPE_CHAR = "!";

    public UserRoleManagementDAO() {
        // DO NOTHING
    }

    public SearchBpmsUserResults searchBpmsUser( IDAOSessionConfig config, String userId, String userName, List< String > roles, List< String > departmentCodes, Integer maxResults ) {
        // if user in multiple roles, return multiple results of same user
        SearchBpmsUserResults results = new SearchBpmsUserResults();
        String formattedMsg = String.format("start UserRoleManagementDAO.searchBpmsUser(), userId=%s, userName=%s, roles=%s, departmentCodes=%s, maxResults=%s", userId, userName, roles, departmentCodes, maxResults);
        LOGGER.log(Level.INFO, formattedMsg);

        // get dept code desc map
        Map< String, String > deptCodeDescMap = this.getDepartmentCodeDescriptionMap();

        // get employment status desc map
        Map< String, String > employmentStatusDescMap = this.getEmploymentStatusDescriptionMap();
        
        // get subunit desc map
        Map< String, String > subunitDescMap = this.getSubunitCodeDescriptionMap();

        // get all group ids and sub group ids
        List< String > groupSubGroupIds = new ArrayList<>();
        List< String > groupIds = this.getGroupIdsByGroupNames( config, roles );
        if ( ( groupIds != null ) && ( !groupIds.isEmpty() ) ) {
            groupSubGroupIds.addAll( groupIds );
        }
        List< String > subGroupIds = this.getSubGroupIdsByGroupIds( config, groupIds );
        while ( ( subGroupIds != null ) && ( !subGroupIds.isEmpty() ) ) {
            groupSubGroupIds.addAll( groupIds );
            subGroupIds = this.getSubGroupIdsByGroupIds( config, subGroupIds );
        }

        Integer tempMaxResults = ( maxResults == null ) ? null : maxResults + 1;
        List< BpmsUser > bpmsUsers = this.getBpmsUsersByGroupIds( config, groupIds, userId, userName, departmentCodes, deptCodeDescMap, employmentStatusDescMap, subunitDescMap, tempMaxResults );

        if ( bpmsUsers.size() == 0 ) {
            String responseMsg = "No record(s) found.";
            results.getResponseMsgs().add( responseMsg );
            LOGGER.info( responseMsg );
        }

        if ( maxResults != null ) {
            if ( bpmsUsers.size() > maxResults ) {
                String responseMsg = String.format( "Too many results. Only first %s results are displayed below. Please refine search criteria.", maxResults );
                results.getResponseMsgs().add( responseMsg );
                LOGGER.info( responseMsg );

                while ( bpmsUsers.size() > maxResults ) {
                    bpmsUsers.remove( bpmsUsers.size() - 1 );
                }
            }
            results.getUserResults().addAll( bpmsUsers );
        }

        LOGGER.info( String.format( "end UserRoleManagementDAO.searchBpmsUser(), result count=%s", bpmsUsers.size() ) );
        return results;
    }

    public SearchBpmsUserResults searchBpmsUser( IDAOSessionConfig config, String userId, String userName, Integer maxResults ) {
        // if user in multiple roles, return multiple results of same user
        SearchBpmsUserResults results = new SearchBpmsUserResults();
        String formattedMsg = String.format("start UserRoleManagementDAO.searchBpmsUser(), userId=%s, userName=%s, maxResults=%s", userId, userName, maxResults);
        LOGGER.log(Level.INFO, formattedMsg);

        // get employment status desc map
        this.getEmploymentStatusDescriptionMap();

        Integer tempMaxResults = ( maxResults == null ) ? null : maxResults + 1;
        List< BpmsUser > bpmsUsers = this.getBpmsUsers( config, userId, userName, tempMaxResults );

        if ( bpmsUsers.size() == 0 ) {
            String responseMsg = "No record(s) found.";
            results.getResponseMsgs().add( responseMsg );
            LOGGER.info( responseMsg );
        }

        if ( maxResults != null ) {
            if ( bpmsUsers.size() > maxResults ) {
                String responseMsg = String.format( "Too many results. Only first %s results are displayed below. Please refine search criteria.", maxResults );
                results.getResponseMsgs().add( responseMsg );
                LOGGER.info( responseMsg );

                while ( bpmsUsers.size() > maxResults ) {
                    bpmsUsers.remove( bpmsUsers.size() - 1 );
                }
            }
            results.getUserResults().addAll( bpmsUsers );
        }

        LOGGER.info( String.format( "end UserRoleManagementDAO.searchBpmsUser(), result count=%s", bpmsUsers.size() ) );
        return results;
    }

    @SuppressWarnings( "unchecked" )
    public SearchBpmsGroupResults searchBpmsGroup( IDAOSessionConfig config, String groupName, String groupDescription, Integer maxResults ) {
        SearchBpmsGroupResults results = new SearchBpmsGroupResults();
        String formattedMsg = String.format("start UserRoleManagementDAO.searchBpmsGroup(), groupName=%s, groupDescription=%s, maxResults=%s", groupName, groupDescription, maxResults);
        LOGGER.log(Level.INFO, formattedMsg);

        String tempGroupName = null;
        String tempGroupDescription = null;

        Session bpmsUserSession = BpmsUserSessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append( "select distinct A.DISPLAY_NAME as groupName, A.DESCRIPTION as groupDescription " );
        queryBuilder.append( "from " + config.bpmsuserSchema() + ".LSW_USR_GRP_XREF A " );
        queryBuilder.append( "where A.GROUP_TYPE='3' and A.GROUP_NAME IS NOT NULL and A.DISPLAY_NAME like 'SPFCORE%' " );
        if ( groupName != null ) {
            tempGroupName = this.sqlContainsPatternString( groupName );
            if ( tempGroupName.length() > 2 ) {
                queryBuilder.append( "and A.GROUP_NAME LIKE :groupName ESCAPE '" + SQL_ESCAPE_CHAR + "' " );
            }
        }
        if ( groupDescription != null ) {
            tempGroupDescription = this.sqlContainsPatternString( groupDescription );
            if ( tempGroupDescription.length() > 2 ) {
                queryBuilder.append( "and A.DESCRIPTION LIKE :groupDescription ESCAPE '" + SQL_ESCAPE_CHAR + "' " );
            }
        }
        SQLQuery query = bpmsUserSession.createSQLQuery( queryBuilder.toString() );
        if ( groupName != null && tempGroupName.length() > 2) {
            query.setParameter( "groupName", tempGroupName );
        }
        if ( groupDescription != null && tempGroupDescription.length() > 2 ) {
            query.setParameter( "groupDescription", tempGroupDescription );
        }
        query.addScalar( "groupName", StandardBasicTypes.STRING );
        query.addScalar( "groupDescription", StandardBasicTypes.STRING );
        query.setResultTransformer( Transformers.aliasToBean( BpmsGroup.class ) );
        if ( maxResults != null ) {
            query.setMaxResults( maxResults + 1 );
        }
        List< BpmsGroup > bpmsGroups = query.list();
        if ( bpmsGroups == null ) {
            bpmsGroups = new ArrayList<>();
        }
        if (bpmsGroups.isEmpty()) {
            String responseMsg = "No record(s) found.";
            results.getResponseMsgs().add( responseMsg );
            LOGGER.info( responseMsg );
        }

        if ( maxResults != null ) {
            if ( bpmsGroups.size() > maxResults ) {
                String responseMsg = String.format( "Too many results. Only first %s results are displayed below. Please refine search criteria.", maxResults );
                results.getResponseMsgs().add( responseMsg );
                LOGGER.info( responseMsg );

                while ( bpmsGroups.size() > maxResults ) {
                    bpmsGroups.remove( bpmsGroups.size() - 1 );
                }
            }
            results.getGroupResults().addAll( bpmsGroups );
        }
        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("end UserRoleManagementDAO.searchBpmsGroup(), result count=%s", bpmsGroups.size()));
        }
        return results;
    }

    @SuppressWarnings( "unchecked" )
    protected Map< String, String > getDepartmentCodeDescriptionMap() {
        LOGGER.log(Level.INFO, "start getDepartmentCodeDescriptionMap()" );
        Map< String, String > departmentCodeDescriptionMap = new HashMap<>();
        Session session = SessionFactoryUtil.getCurrentSession();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append( "SELECT c.id as name, c.description as value FROM Code as c WHERE c.type = :codeType and c.enabled = :enabled order by c.order asc" );
        Query query = session.createQuery( queryBuilder.toString() );
        query.setParameter( "codeType", CodeType.UNIT_DEPARTMENT );
        query.setParameter( "enabled", Boolean.TRUE );
        query.setResultTransformer( Transformers.aliasToBean( CodeNamedValuePair.class ) );
        List< CodeNamedValuePair > results = ( List< CodeNamedValuePair > ) query.list();
        if ( results != null ) {
            for ( CodeNamedValuePair result : results ) {
                departmentCodeDescriptionMap.put( result.getName(), result.getValue() );
            }
        }
        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("end getDepartmentCodeDescriptionMap(), count=%s", departmentCodeDescriptionMap.size()));
        }
        return departmentCodeDescriptionMap;
    }

    @SuppressWarnings( "unchecked" )
    protected Map< String, String > getEmploymentStatusDescriptionMap() {
        LOGGER.log(Level.INFO, "start getEmploymentStatusDescriptionMap()" ) ;
        Map< String, String > employmentStatusDescriptionMap = new HashMap<>();
        Session session = SessionFactoryUtil.getCurrentSession();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append( "SELECT c.id as name, c.description as value FROM Code as c WHERE c.type = :codeType and c.enabled = :enabled order by c.order asc" );
        Query query = session.createQuery( queryBuilder.toString() );
        query.setParameter( "codeType", CodeType.EMPLOYMENT_STATUS );
        query.setParameter( "enabled", Boolean.TRUE );
        query.setResultTransformer( Transformers.aliasToBean( CodeNamedValuePair.class ) );
        List< CodeNamedValuePair > results = ( List< CodeNamedValuePair > ) query.list();
        if ( results != null ) {
            for ( CodeNamedValuePair result : results ) {
                employmentStatusDescriptionMap.put( result.getName(), result.getValue() );
            }
        }
        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("end getEmploymentStatusDescriptionMap(), count=%s", employmentStatusDescriptionMap.size()));
        }
        return employmentStatusDescriptionMap;
    }

    @SuppressWarnings( "unchecked" )
    protected List< String > getGroupIdsByGroupNames( IDAOSessionConfig config, List< String > groupNames ) {
        LOGGER.info( String.format( "start getGroupIdsByGroupNames(), bpms user schema=%s, groupNames=%s", config.bpmsuserSchema(), groupNames ) );
        List< String > groupIds = null;

        Session bpmsUserSession = BpmsUserSessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append( "select distinct A.GROUP_ID as groupId " );
        queryBuilder.append( "from " + config.bpmsuserSchema() + ".LSW_USR_GRP_XREF A " );
        queryBuilder.append( "where A.GROUP_TYPE='3' and A.DISPLAY_NAME like 'SPFCORE%' " );
        if (( groupNames != null ) && ( !groupNames.isEmpty() )) {
            queryBuilder.append( "and A.DISPLAY_NAME in :groupNames " );
        }
        SQLQuery query = bpmsUserSession.createSQLQuery( queryBuilder.toString() );
        if (( groupNames != null ) && ( !groupNames.isEmpty() )) {
            query.setParameterList( "groupNames", groupNames );
        }
        query.addScalar( "groupId", StandardBasicTypes.STRING );
        groupIds = query.list();

        LOGGER.info( String.format( "end getGroupIdsByGroupNames(), bpms user schema=%s, groupNames=%s, groupIds=%s", config.bpmsuserSchema(), groupNames, groupIds ) );
        return groupIds;
    }

    @SuppressWarnings( "unchecked" )
    protected List< String > getSubGroupIdsByGroupIds( IDAOSessionConfig config, List< String > groupIds ) {
        LOGGER.info( String.format( "start getSubGroupIdsByGroupIds(), bpms user schema=%s, groupIds=%s", config.bpmsuserSchema(), groupIds ) );
        List< String > subGroupIds = null;

        Session bpmsUserSession = BpmsUserSessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append( "select distinct C.GROUP_ID as subGroupId " );
        queryBuilder.append( "from " + config.bpmsuserSchema() + ".LSW_USR_GRP_XREF A " );
        queryBuilder.append( "LEFT JOIN " + config.bpmsuserSchema() + ".LSW_GRP_GRP_MEM_EXPLODED_XREF B ON A.GROUP_ID = B.CONTAINER_GROUP_ID " );
        queryBuilder.append( "LEFT JOIN " + config.bpmsuserSchema() + ".LSW_USR_GRP_XREF C ON B.GROUP_ID = C.GROUP_ID " );
        queryBuilder.append( "where A.GROUP_TYPE='3' and A.DISPLAY_NAME like 'SPFCORE%' and C.GROUP_TYPE='3' and C.DISPLAY_NAME like 'SPFCORE%' " );
        if ( groupIds != null ) {
            queryBuilder.append( "and A.GROUP_ID in :groupIds " );
        }
        SQLQuery query = bpmsUserSession.createSQLQuery( queryBuilder.toString() );
        if ( groupIds != null ) {
            query.setParameterList( "groupIds", groupIds );
        }
        query.addScalar( "subGroupId", StandardBasicTypes.STRING );
        subGroupIds = query.list();

        LOGGER.info( String.format( "end getSubGroupIdsByGroupIds(), bpms user schema=%s, groupIds=%s, subGroupIds=%s", config.bpmsuserSchema(), groupIds, subGroupIds ) );
        return subGroupIds;
    }

    @SuppressWarnings( "unchecked" )
    protected List< BpmsUser > getBpmsUsersByGroupIds( IDAOSessionConfig config, List< String > groupIds, String userId, String userName, List< String > departmentCodes, Map< String, String > deptCodeDescMap,
            Map< String, String > employmentStatusDescMap, Map< String, String> subunitDescMap,Integer maxResults ) {
        LOGGER.info( String.format( "start getBpmsUsersByGroupIds(), bpms user schema=%s, groupIds=%s, userId=%s, userName=%s, departmentCodes=%s, maxResults=%s", config.bpmsuserSchema(), groupIds, userId, userName, departmentCodes,
                maxResults ) );
        List< BpmsUser > bpmsUsers = new ArrayList<>();
        String tempUserId = null;
        String tempUserName = null;

        Session bpmsUserSession = BpmsUserSessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append( "select distinct A.DISPLAY_NAME as userGroupName, C.USER_NAME as userCode, C.FULL_NAME as userName, C.USER_ID as userId " );
        queryBuilder.append( "from " + config.bpmsuserSchema() + ".LSW_USR_GRP_XREF A " );
        queryBuilder.append( "LEFT JOIN " + config.bpmsuserSchema() + ".LSW_USR_GRP_MEM_XREF B ON A.GROUP_ID = B.GROUP_ID " );
        queryBuilder.append( "LEFT JOIN " + config.bpmsuserSchema() + ".LSW_USR_XREF C ON B.USER_ID = C.USER_ID " );
        queryBuilder.append( "where A.GROUP_TYPE='3' and A.DISPLAY_NAME like 'SPFCORE%' and C.USER_NAME IS NOT NULL " );
        if ( groupIds != null ) {
            queryBuilder.append( "and A.GROUP_ID in :groupIds " );
        }
        if ( userId != null ) {
            tempUserId = this.sqlContainsPatternString( userId );
            if ( tempUserId.length() > 2 ) {
                queryBuilder.append( "and C.USER_NAME LIKE :userId ESCAPE '" + SQL_ESCAPE_CHAR + "' " );
            }
        }
        if ( userName != null ) {
            tempUserName = this.sqlContainsPatternString( userName );
            if ( tempUserName.length() > 2 ) {
                queryBuilder.append( "and C.FULL_NAME LIKE :userName ESCAPE '" + SQL_ESCAPE_CHAR + "' " );
            }
        }
        SQLQuery query = bpmsUserSession.createSQLQuery( queryBuilder.toString() );
        if ( groupIds != null ) {
            query.setParameterList( "groupIds", groupIds );
        }
        if ( userId != null && tempUserId.length() > 2) {
            query.setParameter( "userId", tempUserId );
        }
        if ( userName != null && tempUserName.length() > 2) {
            query.setParameter( "userName", tempUserName );
        }
        query.addScalar( "userGroupName", StandardBasicTypes.STRING );
        query.addScalar( "userCode", StandardBasicTypes.STRING );
        query.addScalar( "userName", StandardBasicTypes.STRING );
        query.addScalar( "userId", StandardBasicTypes.STRING );
        query.setResultTransformer( Transformers.aliasToBean( BpmsUser.class ) );
        List< BpmsUser > results = ( List< BpmsUser > ) query.list();

        if ( results != null ) {
            for ( BpmsUser result : results ) {
                if ( bpmsUsers.size() > maxResults ) {
                    break;
                }
                
                this.populateBpmsUserPersonalDetails(result, deptCodeDescMap, employmentStatusDescMap, subunitDescMap);

                if ( ( departmentCodes == null ) || ( departmentCodes.contains( result.getDepartmentCode() ) ) ) {
                    bpmsUsers.add( result );
                }
            }
        }

        LOGGER.info( String.format( "end getBpmsUsersByGroupIds(), bpms user schema=%s, groupIds=%s, userId=%s, userName=%s, departmentCodes=%s, user count=%s", config.bpmsuserSchema(), groupIds, userId, userName, departmentCodes,
                bpmsUsers.size() ) );
        return bpmsUsers;
    }

    @SuppressWarnings( "unchecked" )
    protected List< BpmsUser > getBpmsUsers(IDAOSessionConfig config, String userId, String userName, Integer maxResults ) {
        LOGGER.info( String.format( "start getBpmsUsers(), bpms user schema=%s, userId=%s, userName=%s,  maxResults=%s", config.bpmsuserSchema(), userId, userName, maxResults ) );
        List< BpmsUser > bpmsUsers = new ArrayList<>();
        String tempUserId = null;
        String tempUserName = null;

        Session bpmsUserSession = BpmsUserSessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append( "select distinct C.USER_NAME as userCode, C.FULL_NAME as userName, C.USER_ID as userId " );
        queryBuilder.append( "from " + config.bpmsuserSchema() + ".LSW_USR_GRP_XREF A " );
        queryBuilder.append( "LEFT JOIN " + config.bpmsuserSchema() + ".LSW_USR_GRP_MEM_XREF B ON A.GROUP_ID = B.GROUP_ID " );
        queryBuilder.append( "LEFT JOIN " + config.bpmsuserSchema() + ".LSW_USR_XREF C ON B.USER_ID = C.USER_ID " );
        queryBuilder.append( "where A.GROUP_TYPE='3' and A.DISPLAY_NAME='tw_allusers' and C.USER_NAME IS NOT NULL " );
        if ( userId != null ) {
            tempUserId = this.sqlContainsPatternString( userId );
            if ( tempUserId.length() > 2 ) {
                queryBuilder.append( "and C.USER_NAME LIKE :userId ESCAPE '" + SQL_ESCAPE_CHAR + "' " );
            }
        }
        if ( userName != null ) {
            tempUserName = this.sqlContainsPatternString( userName );
            if ( tempUserName.length() > 2 ) {
                queryBuilder.append( "and C.FULL_NAME LIKE :userName ESCAPE '" + SQL_ESCAPE_CHAR + "' " );
            }
        }
        SQLQuery query = bpmsUserSession.createSQLQuery( queryBuilder.toString() );
        if ( userId != null && tempUserId.length() > 2) {
            query.setParameter( "userId", tempUserId );
        }
        if ( userName != null && tempUserName.length() > 2) {
            query.setParameter( "userName", tempUserName );
        }
        query.addScalar( "userCode", StandardBasicTypes.STRING );
        query.addScalar( "userName", StandardBasicTypes.STRING );
        query.addScalar( "userId", StandardBasicTypes.STRING );
        query.setResultTransformer( Transformers.aliasToBean( BpmsUser.class ) );
        List< BpmsUser > results = ( List< BpmsUser > ) query.list();

        if ( results != null ) {
            for ( BpmsUser result : results ) {
                if ( bpmsUsers.size() > maxResults ) {
                    break;
                }

                bpmsUsers.add( result );
            }
        }

        LOGGER.info( String.format( "end getBpmsUsers(), bpms user schema=%s, userId=%s, userName=%s, user count=%s", config.bpmsuserSchema(), userId, userName, bpmsUsers.size() ) );
        return bpmsUsers;
    }

    protected void populateBpmsUserPersonalDetails( BpmsUser user, Map< String, String > deptCodeDescMap, Map< String, String > employmentStatusDescMap, Map< String, String> subunitDescMap) {
        if ( user == null ) {
            return;
        }

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        String userId = user.getUserCode();
        PersonalDetail personalDetail = ( PersonalDetail ) session.get( PersonalDetail.class, userId );
        Employment employment = ( personalDetail == null ) ? null : personalDetail.getEmployment();

        String deptCode = ( employment == null ) ? null : employment.getOrganisationOrDepartment();
        user.setDepartmentCode( deptCode );

        String potentialDeptDesc = deptCode != null ? deptCodeDescMap.get(deptCode) : null;
        String deptDesc = potentialDeptDesc != null ? potentialDeptDesc : "-";
        user.setDepartment( deptDesc );

        String employmentStatusCode = ( employment == null ) ? null : employment.getEmploymentStatus();
        String potentialEmploymentStatusDesc = employmentStatusCode != null ? employmentStatusDescMap.get(employmentStatusCode) : null;
        String employmentStatusDesc = potentialEmploymentStatusDesc != null ? potentialEmploymentStatusDesc : null;
        employmentStatusDesc = ( employmentStatusDesc != null ) ? employmentStatusDesc : "-";
        user.setEmploymentStatus( employmentStatusDesc );

        String serviceType = ( employment == null ) ? null : employment.getServiceType();
        String isSpfOfficer;
        if (serviceType != null) {
            isSpfOfficer = this.isNonSpf(serviceType) ? "No" : "Yes";
        } else {
            isSpfOfficer = "-";
        }
        user.setSpfOfficer( isSpfOfficer );
        
        String subunitCode = ( employment == null ) ? null : employment.getSubunit();
        user.setSubUnitCode(subunitCode);

        String potentialSubunitDesc = (subunitCode == null) ? null : subunitDescMap.get(subunitCode);
        String subunitDesc = potentialSubunitDesc != null ? potentialSubunitDesc : null;
        subunitDesc = ( subunitDesc == null || subunitDesc.equals("") ) ? "-" : subunitDesc;
        user.setSubUnit(subunitDesc);
    }

    private boolean isNonSpf( String serviceType ) {
        for ( String nonSpfServiceType : NON_SPF_SERVICE_TYPES ) {
            if ( nonSpfServiceType.equals( serviceType ) ) {
                return true;
            }
        }
        return false;
    }

    private String sqlContainsPatternString( String pattern ) {
        return "%" + pattern.replace( SQL_ESCAPE_CHAR, SQL_ESCAPE_CHAR + SQL_ESCAPE_CHAR ).replace( "_", SQL_ESCAPE_CHAR + "_" ).replace( "%", SQL_ESCAPE_CHAR + "%" ) + "%";
    }
    
    /**
     * Add new bpm user group
     * 
     * @param newBornGift
     * @throws AccessDeniedException
     */
    public void addUserGroup( UrmBpmGroup urmBpmGroup, String requester ) throws AccessDeniedException {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        urmBpmGroup.preSave();

        if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), urmBpmGroup.getCreatedBy() ) ) {
            throw new AccessDeniedException( "Caller has not access to data belong to " + urmBpmGroup.getCreatedBy() );
        }

        session.saveOrUpdate( urmBpmGroup );

        session.flush();
    }
    
    public void addUserIntoGroup( UrmBrpGroupUser urmBrpGroupUser , String requester ) throws AccessDeniedException {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), urmBrpGroupUser.getbpmUserName() )) {
            throw new AccessDeniedException( "Caller has not access to data belong to " + urmBrpGroupUser.getbpmUserName());
        }
        urmBrpGroupUser.preSave();
        session.saveOrUpdate( urmBrpGroupUser );
        session.flush();
    }
    

    @SuppressWarnings("unchecked")
	public List< UrmBpmGroup > getUrmBpmGroup(int bpmGroupId) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        String bpmGroupIdString = String.valueOf(bpmGroupId);
        Query query = session.createQuery( "SELECT c FROM UrmBpmGroup as c WHERE c.bpmGroupId = :bpmGroupId" );
        query.setParameter( "bpmGroupId", bpmGroupIdString );

        return ( List<UrmBpmGroup> ) query.list();
      
    }
    
    public void saveUrmBrpGroupUser( UrmBrpGroupUser urmBrpGroupUser, String requester ) throws AccessDeniedException {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        urmBrpGroupUser.preSave();

        session.saveOrUpdate( urmBrpGroupUser );

        session.flush();
    }
    
    @SuppressWarnings("unchecked")
	public List< UrmBrpGroupUser > getUrmBrpGroupUser(String bpmUserId, String urmBpmGroupId, String status) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        Query query = session.createQuery( "SELECT c FROM UrmBrpGroupUser as c WHERE c.bpmUserId = :bpmUserId AND c.urmBpmGroupId = :urmBpmGroupId AND c.status = :status" );
        query.setParameter( "bpmUserId", bpmUserId );
        query.setParameter( "urmBpmGroupId", urmBpmGroupId );
        query.setParameter( "status", status );

        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("bpmUserId: %s", bpmUserId));
            LOGGER.info(String.format("urmBpmGroupId: %s", urmBpmGroupId));
            LOGGER.info(String.format("status: %s", status));
        }
        return ( List<UrmBrpGroupUser> ) query.list();
      
    }
    
    public SearchBpmsUserResults searchBpmsUserByUnit (IDAOSessionConfig config, String userId, String userName, List<String> departmentCodes, String subunit,Integer maxResults) {
    	SearchBpmsUserResults results = new SearchBpmsUserResults();
        String message = String.format(
                "start UserRoleManagementDAO.searchBpmsUser(), userId=%s, userName=%s, departmentCodes=%s, subunit=%s, maxResults=%s",
                userId, userName, departmentCodes, subunit, maxResults);

        LOGGER.log(Level.INFO, message);
        // get dept code desc map
        Map< String, String > deptCodeDescMap = this.getDepartmentCodeDescriptionMap();

        // get employment status desc map
        Map< String, String > employmentStatusDescMap = this.getEmploymentStatusDescriptionMap();
        
        Map< String, String > subunitDescMap = this.getSubunitCodeDescriptionMap();
        
        Integer tempMaxResults = ( maxResults == null ) ? null : maxResults + 1;
        
        List<BpmsUser> bpmsUsers = this.getBpmsUserList(config, userId, userName, employmentStatusDescMap, deptCodeDescMap, departmentCodes, subunitDescMap, subunit, tempMaxResults);
        
        if ( bpmsUsers.size() == 0 ) {
            String responseMsg = "No record(s) found.";
            results.getResponseMsgs().add( responseMsg );
            LOGGER.info( responseMsg );
        }

        if ( maxResults != null && bpmsUsers.size() > maxResults) {
            String responseMsg = String.format( "Too many results. Only first %s results are displayed below. Please refine search criteria.", maxResults );
            results.getResponseMsgs().add( responseMsg );
            LOGGER.info( responseMsg );

            while ( bpmsUsers.size() > maxResults ) {
                bpmsUsers.remove( bpmsUsers.size() - 1 );
            }
            results.getUserResults().addAll( bpmsUsers );
        }

        LOGGER.info( String.format( "end UserRoleManagementDAO.searchBpmsUser(), result count=%s", bpmsUsers.size() ) );
        return results;
    }
    
    @SuppressWarnings("unchecked")
	protected List<BpmsUser> getBpmsUserList (IDAOSessionConfig config, String userId, String userName, Map< String, String > employmentStatusDescMap, Map< String, String > deptCodeDescMap, List< String > departmentCodes, Map< String, String >subunitDescMap, String subunit,Integer maxResults) {
    	
    	LOGGER.info( String.format( "start getBpmsUserList(), bpms user schema=%s, userId=%s, userName=%s,  maxResults=%s", config.bpmsuserSchema(), userId, userName, maxResults ) );
        List< BpmsUser > bpmsUsers = new ArrayList<>();
        String tempUserId = null;
        String tempUserName = null;
        boolean firstRule = true;

        Session bpmsUserSession = BpmsUserSessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append( "select distinct C.USER_NAME as userCode, C.FULL_NAME as userName, C.USER_ID as userId " );
        queryBuilder.append( "from " + config.bpmsuserSchema() + ".LSW_USR_XREF C " );
        if ( userId != null ) {
            tempUserId = this.sqlContainsPatternString( userId );
            if ( tempUserId.length() > 2 ) {
                if (firstRule) {
                	queryBuilder.append( "where C.USER_NAME LIKE :userId ESCAPE '" + SQL_ESCAPE_CHAR + "' " );
                } else {
                	queryBuilder.append( "and C.USER_NAME LIKE :userId ESCAPE '" + SQL_ESCAPE_CHAR + "' " );
                }
                firstRule = false;
            }
        }
        if ( userName != null ) {
            tempUserName = this.sqlContainsPatternString( userName );
            if ( tempUserName.length() > 2 ) {
            	if (firstRule) {
            		queryBuilder.append( "where C.FULL_NAME LIKE :userName ESCAPE '" + SQL_ESCAPE_CHAR + "' " );
            	} else {
            		queryBuilder.append( "and C.FULL_NAME LIKE :userName ESCAPE '" + SQL_ESCAPE_CHAR + "' " );
            	}
            }
        }
        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("SQL to execute: %s", queryBuilder));
        }
        SQLQuery query = bpmsUserSession.createSQLQuery( queryBuilder.toString() );
        if ( userId != null && tempUserId.length() > 2) {
            query.setParameter( "userId", tempUserId );
        }
        if ( userName != null && tempUserName.length() > 2) {
            query.setParameter( "userName", tempUserName );
        }
        query.addScalar( "userCode", StandardBasicTypes.STRING );
        query.addScalar( "userName", StandardBasicTypes.STRING );
        query.addScalar( "userId", StandardBasicTypes.STRING );
        query.setResultTransformer( Transformers.aliasToBean( BpmsUser.class ) );
        List< BpmsUser > results = ( List< BpmsUser > ) query.list();

        if ( results != null ) {
            for ( BpmsUser result : results ) {
                if ( bpmsUsers.size() > maxResults ) {
                    break;
                }
                
                this.populateBpmsUserPersonalDetails(result, deptCodeDescMap, employmentStatusDescMap, subunitDescMap);
                
	            if ( ( departmentCodes == null ) || ( departmentCodes.contains( result.getDepartmentCode() ) ) ) {
	            	if (result.getDepartmentCode().equals("XUNIT36")) {
	            		LOGGER.info("EXTERNAL AGENCIES, Checking for Subunit");
	            		if (result.getSubUnitCode().equals(subunit)) {
	            			bpmsUsers.add(result);
	            		}
	            	} else {
	            		bpmsUsers.add( result );
	            	}
                }
            }
        }

        LOGGER.info( String.format( "end getBpmsUsers(), bpms user schema=%s, userId=%s, userName=%s, user count=%s", config.bpmsuserSchema(), userId, userName, bpmsUsers.size() ) );
        return bpmsUsers;
    }
    
    @SuppressWarnings("unchecked")
	protected Map< String, String > getSubunitCodeDescriptionMap() {
        LOGGER.log( Level.INFO,( "start getSubunitCodeDescriptionMap()" ) );
        Map< String, String > subunitCodeDescriptionMap = new HashMap<>();
        Session session = SessionFactoryUtil.getCurrentSession();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append( "SELECT c.id as name, c.description as value FROM Code as c WHERE c.type = :codeType and c.enabled = :enabled order by c.order asc" );
        Query query = session.createQuery( queryBuilder.toString() );
        query.setParameter( "codeType", CodeType.SUB_UNIT );
        query.setParameter( "enabled", Boolean.TRUE );
        query.setResultTransformer( Transformers.aliasToBean( CodeNamedValuePair.class ) );
        List< CodeNamedValuePair > results = ( List< CodeNamedValuePair > ) query.list();
        if ( results != null ) {
            for ( CodeNamedValuePair result : results ) {
                subunitCodeDescriptionMap.put( result.getName(), result.getValue() );
            }
        }
        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("end getSubunitCodeDescriptionMap(), count= %s", subunitCodeDescriptionMap.size()));
        }
        return subunitCodeDescriptionMap;
    }
}

