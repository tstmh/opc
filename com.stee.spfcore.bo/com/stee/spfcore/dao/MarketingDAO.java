package com.stee.spfcore.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.criteria.CriteriaBuilder;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import com.stee.spfcore.model.code.CodeType;
import com.stee.spfcore.model.marketing.CodeRule;
import com.stee.spfcore.model.marketing.Field;
import com.stee.spfcore.model.marketing.ListRule;
import com.stee.spfcore.model.marketing.ListRuleValue;
import com.stee.spfcore.model.marketing.MemberGroup;
import com.stee.spfcore.model.marketing.Rule;
import com.stee.spfcore.model.marketing.RuleSet;
import com.stee.spfcore.model.marketing.RuleType;
import com.stee.spfcore.model.genericEvent.GenericEventDetail;
import com.stee.spfcore.model.genericEvent.GenericEventDepartment;
import com.stee.spfcore.model.announcement.Announcement;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.vo.marketing.MemberGroupNamedValuePair;
import com.stee.spfcore.vo.marketing.MemberGroupSummaryList;
import com.stee.spfcore.vo.personnel.PersonalNricEmailPhone;
import com.stee.spfcore.vo.personnel.PersonalNricName;

public class MarketingDAO {

    private static final Logger logger = Logger.getLogger( MarketingDAO.class.getName() );

    private static final String SELECT_ONLY_ENABLED = "(v.effectiveDate is not null and v.effectiveDate <= current_date()) " + " and (v.obsoleteDate is null or v.obsoleteDate > current_date())";

    public String addMemberGroup( MemberGroup memberGroup, String requester ) {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getCurrentSession();

        session.save( memberGroup );

        session.flush();

        return memberGroup.getId();
    }

    public void updateMemberGroup( MemberGroup memberGroup, String requester ) {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getCurrentSession();

        session.merge( memberGroup );

        session.flush();
    }

    public MemberGroup getMemberGroup( String id ) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        MemberGroup group = ( MemberGroup ) session.get( MemberGroup.class, id );

        if ( group != null ) {
            populateDisplayValues( group, session );
        }

        return group;
    }

    @SuppressWarnings( "unchecked" )
    public List< MemberGroupSummaryList > getMemberGroupsSummaryList( boolean template, boolean includeDisabled ) {

        Session session = SessionFactoryUtil.getCurrentSession();
        long beforeSessionEndTime = System.nanoTime();
        logger.log( Level.INFO, "getMemberGroups after getCurrentSession", beforeSessionEndTime);
//        StringBuilder builder = new StringBuilder( "FROM MemberGroupSummaryList v where v.template = :template" );
        StringBuilder builder = new StringBuilder( "Select new com.stee.spfcore.vo.marketing.MemberGroupSummaryList"
                + "(v.id, v.name, v.description, v.template, v.effectiveDate,"+
                "v.obsoleteDate, v.updatedByName) FROM MemberGroup v where v.template = :template" );
        if ( !includeDisabled ) {
            builder.append( " and " );
            builder.append( SELECT_ONLY_ENABLED );
        }

        Query query = session.createQuery( builder.toString() );
        query.setParameter( "template", template );

        List< MemberGroupSummaryList > groups = query.list();
        long startTime = System.nanoTime();
        logger.log( Level.INFO, "getMemberGroups dao after db query", startTime );
//        for ( MemberGroup group : groups ) {
//            populateDisplayValues( group, session );
//        }

        return groups;
    }

    @SuppressWarnings( "unchecked" )
    public List< MemberGroup > getMemberGroups( boolean template, boolean includeDisabled ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "FROM MemberGroup v where v.template = :template" );

        if ( !includeDisabled ) {
            builder.append( " and " );
            builder.append( SELECT_ONLY_ENABLED );
        }

        Query query = session.createQuery( builder.toString() );
        query.setParameter( "template", template );

        List< MemberGroup > groups = query.list();

        for ( MemberGroup group : groups ) {
            populateDisplayValues( group, session );
        }

        return groups;
    }

    @SuppressWarnings( "unchecked" )
    public List< MemberGroup > getMemberGroups( String module, boolean template, boolean includeDisabled ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "FROM MemberGroup v where v.template = :template and v.module like :module" );

        if ( !includeDisabled ) {
            builder.append( " and " );
            builder.append( SELECT_ONLY_ENABLED );
        }
        logger.log(Level.INFO, "test from eclipse");
        Query query = session.createQuery( builder.toString() );
        query.setParameter( "template", template );
        query.setParameter( "module", module );

        List< MemberGroup > groups = query.list();

        for ( MemberGroup group : groups ) {
            populateDisplayValues( group, session );
        }

        return groups;
    }

    @SuppressWarnings( "unchecked" )
    public List< MemberGroup > getMemberGroups( String module, boolean includeDisabled ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "FROM MemberGroup v where v.module like :module" );

        if ( !includeDisabled ) {
            builder.append( " and " );
            builder.append( SELECT_ONLY_ENABLED );
        }
        Query query = session.createQuery( builder.toString() );
        query.setParameter( "module", module );

        List< MemberGroup > groups = query.list();

        for ( MemberGroup group : groups ) {
            populateDisplayValues( group, session );
        }

        return groups;
    }

    public int getMemberGroupCount( boolean template, boolean includeDisabled ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "SELECT count(v) FROM MemberGroup v where v.template = :template" );

        if ( !includeDisabled ) {
            builder.append( " and " );
            builder.append( SELECT_ONLY_ENABLED );
        }

        Query query = session.createQuery( builder.toString() );
        query.setParameter( "template", template );

        return ( ( Number ) query.uniqueResult() ).intValue();
    }

    public int getMemberGroupCount( String module, boolean template, boolean includeDisabled ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "SELECT count(v) FROM MemberGroup v where v.template = :template and v.module like :module" );

        if ( !includeDisabled ) {
            builder.append( " and " );
            builder.append( SELECT_ONLY_ENABLED );
        }

        Query query = session.createQuery( builder.toString() );
        query.setParameter( "template", template );
        query.setParameter( "module", module );

        return ( ( Number ) query.uniqueResult() ).intValue();
    }

    @SuppressWarnings( "unchecked" )
    public List< MemberGroup > getMemberGroups( int pageNum, int pageSize, boolean template, boolean includeDisabled ) {

        int index = pageSize * ( pageNum - 1 );

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "FROM MemberGroup v where v.template = :template" );

        if ( !includeDisabled ) {
            builder.append( " and " );
            builder.append( SELECT_ONLY_ENABLED );
        }

        Query query = session.createQuery( builder.toString() );
        query.setFirstResult( index );
        query.setMaxResults( pageSize );
        query.setParameter( "template", template );

        List< MemberGroup > groups = query.list();

        for ( MemberGroup group : groups ) {
            populateDisplayValues( group, session );
        }

        return groups;
    }

    @SuppressWarnings( "unchecked" )
    public List< MemberGroup > getMemberGroups( int pageNum, int pageSize, String module, boolean template, boolean includeDisabled ) {

        int index = pageSize * ( pageNum - 1 );

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "FROM MemberGroup v where v.template = :template and v.module like :module" );

        if ( !includeDisabled ) {
            builder.append( " and " );
            builder.append( SELECT_ONLY_ENABLED );
        }

        Query query = session.createQuery( builder.toString() );
        query.setFirstResult( index );
        query.setMaxResults( pageSize );
        query.setParameter( "template", template );
        query.setParameter( "module", module );

        List< MemberGroup > groups = query.list();

        for ( MemberGroup group : groups ) {
            populateDisplayValues( group, session );
        }

        return groups;
    }

    private void populateDisplayValues( MemberGroup group, Session session ) {

        List< RuleSet > ruleSets = group.getRuleSets();
        for ( RuleSet set : ruleSets ) {
            List< Rule > rules = set.getRules();
            for ( Rule rule : rules ) {
                if ( RuleType.CODE == rule.getType() ) {
                    CodeRule codeRule = ( CodeRule ) rule;
                    populateCodeRuleDescription( codeRule, session );
                }
                else if ( RuleType.LIST == rule.getType() ) {
                    ListRule listRule = ( ListRule ) rule;
                    populateListValue( listRule, session );
                }
            }
        }
    }

    private void populateListValue( ListRule listRule, Session session ) {

        Field field = listRule.getField();
        if ( Field.CITIZENSHIP == field ) {
            populateCodeListDescription( listRule, CodeType.ID_TYPE, session );
        }
        else if ( Field.EDUATION_LEVEL == field ) {
            populateCodeListDescription( listRule, CodeType.EDUCATION, session );
        }
        else if ( Field.GENDER == field ) {
            populateCodeListDescription( listRule, CodeType.GENDERS, session );
        }
        else if ( Field.MARITAL_STATUS == field ) {
            populateCodeListDescription( listRule, CodeType.MARITAL_STATUS, session );
        }
        else if ( Field.NATIONALITY == field ) {
            populateCodeListDescription( listRule, CodeType.NATIONALITY, session );
        }
        else if ( Field.RACE == field ) {
            populateCodeListDescription( listRule, CodeType.RACE, session );
        }
        else if ( Field.RANK == field ) {
            populateCodeListDescription( listRule, CodeType.RANK, session );
        }
        else if ( Field.DEPARTMENT == field ) {
            populateCodeListDescription( listRule, CodeType.UNIT_DEPARTMENT, session );
        }
        else if ( Field.SUBUNIT == field ) {
            populateCodeListDescription( listRule, CodeType.SUB_UNIT, session );
        }
        else if ( Field.SERVICE_TYPE == field ) {
            populateCodeListDescription( listRule, CodeType.SERVICE_TYPE, session );
        }
        else if ( Field.EMPLOYMENT_STATUS == field ) {
            populateCodeListDescription( listRule, CodeType.EMPLOYMENT_STATUS, session );
        }
        else if ( Field.DIVISION_STATUS == field ) {
            populateCodeListDescription( listRule, CodeType.DIVISION_STATUS, session );
        }
        else if ( Field.SCHEME_OF_SERVICE == field ) {
            populateCodeListDescription( listRule, CodeType.SCHEME_OF_SERVICE, session );
        }
        else if ( Field.DESIGNATION == field ) {
            populateCodeListDescription( listRule, CodeType.DESIGNATIONS, session );
        }
        else if ( Field.INTEREST == field ) {
            populateInterestListDescription( listRule, session );
        }
        else if ( Field.MEMBERSHIP_STATUS == field ) {
            populateMembershipStatusListDescription( listRule );
        }
        else if ( Field.MEMBERSHIP_TYPE == field ) {
            populateMembershipTypeListDescription( listRule );
        }
        else if ( Field.ATTENDED_COURSE_TITLE == field ) {
            populateCourseTitleListDescription( listRule, session );
        }
        else if ( Field.BLACKLISTED_MODULE == field ) {
            populateBlacklistModuleListDescription( listRule, session );
        }
    }

    private void populateCodeListDescription( ListRule listRule, CodeType codeType, Session session ) {

        List< ListRuleValue > ruleValues = listRule.getValues();
        for ( ListRuleValue value : ruleValues ) {
            value.setDisplayValue( getDescription( session, value.getValue(), codeType ) );
        }
    }

    private void populateInterestListDescription( ListRule listRule, Session session ) {

        List< ListRuleValue > ruleValues = listRule.getValues();
        for ( ListRuleValue value : ruleValues ) {
            value.setDisplayValue( getSubcategoryName( session, value.getValue() ) );
        }
    }

    @SuppressWarnings( "unchecked" )
    private String getSubcategoryName( Session session, String id ) {

        Query query = session.createQuery( "SELECT c.name, sc.name FROM Category c inner join c.subCategories sc where sc.id = :id" );
        query.setParameter( "id", id );

        List< Object[] > result = query.list();

        if ( !result.isEmpty() ) {
            Object[] objects = result.get( 0 );
            return objects[ 0 ] + " - " + objects[ 1 ];
        }
        return null;
    }

    private void populateMembershipStatusListDescription( ListRule listRule ) {
        List< ListRuleValue > ruleValues = listRule.getValues();
        for ( ListRuleValue value : ruleValues ) {
            value.setDisplayValue( value.getValue() );
        }
    }

    private void populateMembershipTypeListDescription( ListRule listRule ) {
        List< ListRuleValue > ruleValues = listRule.getValues();
        for ( ListRuleValue value : ruleValues ) {
            value.setDisplayValue( value.getValue() );
        }
    }

    private void populateCourseTitleListDescription( ListRule listRule, Session session ) {

        List< ListRuleValue > ruleValues = listRule.getValues();
        for ( ListRuleValue value : ruleValues ) {
            value.setDisplayValue( getSubcategoryName( session, value.getValue() ) );
        }
    }

    private void populateBlacklistModuleListDescription( ListRule listRule, Session session ) {

        List< ListRuleValue > ruleValues = listRule.getValues();
        for ( ListRuleValue value : ruleValues ) {
            value.setDisplayValue( getBlacklistModuleDisplayName( session, value.getValue() ) );
        }
    }

    private String getBlacklistModuleDisplayName( Session session, String id ) {

        Query query = session.createQuery( "SELECT m.name FROM BlacklistModule m where m.id = :id" );
        query.setParameter( "id", id );

        return ( String ) query.uniqueResult();
    }

    private void populateCodeRuleDescription( CodeRule codeRule, Session session ) {

        Field field = codeRule.getField();
        if ( Field.CITIZENSHIP == field ) {
            codeRule.setDescription( getDescription( session, codeRule.getValue(), CodeType.ID_TYPE ) );
        }
        else if ( Field.EDUATION_LEVEL == field ) {
            codeRule.setDescription( getDescription( session, codeRule.getValue(), CodeType.EDUCATION ) );
        }
        else if ( Field.GENDER == field ) {
            codeRule.setDescription( getDescription( session, codeRule.getValue(), CodeType.GENDERS ) );
        }
        else if ( Field.MARITAL_STATUS == field ) {
            codeRule.setDescription( getDescription( session, codeRule.getValue(), CodeType.MARITAL_STATUS ) );
        }
        else if ( Field.NATIONALITY == field ) {
            codeRule.setDescription( getDescription( session, codeRule.getValue(), CodeType.NATIONALITY ) );
        }
        else if ( Field.RACE == field ) {
            codeRule.setDescription( getDescription( session, codeRule.getValue(), CodeType.RACE ) );
        }
        else if ( Field.RANK == field ) {
            codeRule.setDescription( getDescription( session, codeRule.getValue(), CodeType.RANK ) );
        }
        else if ( Field.DEPARTMENT == field ) {
            codeRule.setDescription( getDescription( session, codeRule.getValue(), CodeType.UNIT_DEPARTMENT ) );
        }
        else if ( Field.SUBUNIT == field ) {
            codeRule.setDescription( getDescription( session, codeRule.getValue(), CodeType.SUB_UNIT ) );
        }
        else if ( Field.SERVICE_TYPE == field ) {
            codeRule.setDescription( getDescription( session, codeRule.getValue(), CodeType.SERVICE_TYPE ) );
        }
        else if ( Field.EMPLOYMENT_STATUS == field ) {
            codeRule.setDescription( getDescription( session, codeRule.getValue(), CodeType.EMPLOYMENT_STATUS ) );
        }
        else if ( Field.DIVISION_STATUS == field ) {
            codeRule.setDescription( getDescription( session, codeRule.getValue(), CodeType.DIVISION_STATUS ) );
        }
        else if ( Field.SCHEME_OF_SERVICE == field ) {
            codeRule.setDescription( getDescription( session, codeRule.getValue(), CodeType.SCHEME_OF_SERVICE ) );
        }
        else if ( Field.DESIGNATION == field ) {
            codeRule.setDescription( getDescription( session, codeRule.getValue(), CodeType.DESIGNATIONS ) );
        }
    }

    private String getDescription( Session session, String id, CodeType type ) {

        Query query = session.createQuery( "SELECT c.description FROM Code c where c.id = :id and c.type = :type" );
        query.setParameter( "id", id );
        query.setParameter( "type", type );

        return ( String ) query.uniqueResult();
    }

    @SuppressWarnings( "unchecked" )
    public List< String > getMemberInGroup( String groupId ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        MemberGroup group = getMemberGroup( groupId );
        if ( group == null ) {
            return Collections.EMPTY_LIST;
        }

        StringBuilder builder = new StringBuilder( "SELECT distinct p.nric " );
        Map< String, Object > properties = new HashMap< String, Object >();

        String orderByField = MarketingQueryBuilder.processOrderByField( group, builder, true );

        MarketingQueryBuilder.build( group, builder, properties );

        MarketingQueryBuilder.appendOrderByField( group, orderByField, builder );

        logger.info( "Query to execute:" + builder.toString() );

        Query query = session.createQuery( builder.toString() );
        query.setProperties( properties );

        if ( orderByField != null ) {
            List< Object[] > outputList = ( List< Object[] > ) query.list();
            List< String > result = new ArrayList< String >();
            for ( Object[] output : outputList ) {
                result.add( ( String ) output[ 0 ] );
            }
            return result;
        }
        else {
            return query.list();
        }

    }

    @SuppressWarnings( "unchecked" )
    public List< PersonalDetail > getPersonnelInGroup( String groupId ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        MemberGroup group = getMemberGroup( groupId );
        if ( group == null ) {
            return Collections.EMPTY_LIST;
        }

        StringBuilder builder = new StringBuilder( "SELECT p " );
        Map< String, Object > properties = new HashMap< String, Object >();

        String orderByField = MarketingQueryBuilder.processOrderByField( group, builder, false );

        MarketingQueryBuilder.build( group, builder, properties );

        MarketingQueryBuilder.appendOrderByField( group, orderByField, builder );

        logger.info( "Query to execute:" + builder.toString() );

        Query query = session.createQuery( builder.toString() );
        query.setProperties( properties );

        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< PersonalNricName > getPersonnelNricNameInGroup( String groupId ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        MemberGroup group = getMemberGroup( groupId );
        if ( group == null ) {
            return Collections.EMPTY_LIST;
        }

        StringBuilder builder = new StringBuilder( "SELECT new com.stee.spfcore.vo.personnel.PersonalNricName( p.nric, p.name ) " );
        Map< String, Object > properties = new HashMap< String, Object >();

        String orderByField = MarketingQueryBuilder.processOrderByField( group, builder, false );

        MarketingQueryBuilder.build( group, builder, properties );

        MarketingQueryBuilder.appendOrderByField( group, orderByField, builder );

        logger.info( "Query to execute:" + builder.toString() );

        Query query = session.createQuery( builder.toString() );
        query.setProperties( properties );

        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< PersonalNricEmailPhone > getPersonnelNricEmailPhoneInGroup( String groupId ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        MemberGroup group = getMemberGroup( groupId );
        if ( group == null ) {
            return Collections.EMPTY_LIST;
        }

        StringBuilder builder = new StringBuilder( "SELECT new com.stee.spfcore.vo.personnel.PersonalNricEmailPhone( p ) " );
        Map< String, Object > properties = new HashMap< String, Object >();

        String orderByField = MarketingQueryBuilder.processOrderByField( group, builder, false );

        MarketingQueryBuilder.build( group, builder, properties );

        MarketingQueryBuilder.appendOrderByField( group, orderByField, builder );

        logger.info( "Query to execute:" + builder.toString() );

        Query query = session.createQuery( builder.toString() );
        query.setProperties( properties );

        return query.list();
    }

    public List< String > getNonExistPersonnel( List< String > users ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        Query query = session.createQuery( "SELECT v.name from PersonalDetail v where v.nric like :nric" );

        List< String > nonExistList = new ArrayList< String >();

        for ( String user : users ) {
            query.setParameter( "nric", user );

            String name = ( String ) query.uniqueResult();
            if ( name == null ) {
                nonExistList.add( user );
            }
        }

        return nonExistList;
    }

    @SuppressWarnings("unchecked")
    public List <MemberGroupNamedValuePair> getMemberGroupNVP (String module, boolean includeDisabled) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder("SELECT v.id as value , v.name as name, v.template as template FROM MemberGroup v where v.module like :module");

        if ( !includeDisabled ) {
            builder.append( " and " );
            builder.append( SELECT_ONLY_ENABLED );
        }

        Query query = session.createQuery( builder.toString() );
        query.setParameter( "module", module );

        query.setResultTransformer( Transformers.aliasToBean(MemberGroupNamedValuePair.class));

        return (List <MemberGroupNamedValuePair>)query.list();
    }
    @SuppressWarnings("unchecked")
    public List<MemberGroup> getMemberGroupsOfAnnouncement(String module, boolean template, List<String> announcementIds)
    {
        List<MemberGroup> memberGroups = new ArrayList<MemberGroup>();

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder("FROM Announcement a where a.id in (:announcementIds)");
        Query query = session.createQuery( builder.toString() );
        query.setParameterList( "announcementIds", announcementIds );

        List<Announcement> announcements = (List<Announcement>)query.list();
        if(announcements != null)
        {
            if(announcements.size() == 0)
                logger.fine("getMemberGroupsOfAnnouncement() announcements is 0");
            else {
                for(Announcement announcement : announcements)
                {
                    for(String id : announcement.getMemberGroupIds())
                    {
                        MemberGroup mg = getMemberGroup(id);
                        if(mg.isTemplate() == template && module.equals(mg.getModule()))
                            memberGroups.add(mg);
                    }
                }
            }
        } else {
            logger.fine("getMemberGroupsOfAnnouncement() memberGroupIds is null");
        }

        return memberGroups;
    }

    @SuppressWarnings("unchecked")
    public List<MemberGroup> getMemberGroupsByIds(List<String> memberGroupIds, boolean includeDisabled) {

        List< MemberGroup > groups = new ArrayList<MemberGroup>();
        logger.info("memberGroupIds: " + memberGroupIds.toString());

        if (memberGroupIds.isEmpty()) {
            return groups;
        }

        Session session = SessionFactoryUtil.getCurrentSession();
        StringBuilder builder = new StringBuilder( "FROM MemberGroup v where v.id in (:memberGroupIds)" );

        if ( !includeDisabled ) {
            builder.append( " and " );
            builder.append( SELECT_ONLY_ENABLED );
        }
        logger.info(builder.toString());
        Query query = session.createQuery( builder.toString() );
        query.setParameterList( "memberGroupIds", memberGroupIds );

        groups = (List<MemberGroup>)query.list();

        for ( MemberGroup group : groups ) {
            populateDisplayValues( group, session );
        }

        return groups;
    }

    @SuppressWarnings("unchecked")
    public List<MemberGroup> getMemberGroupsByAnnoucementIds (boolean includeDisabled, List<String> announcementIds) {

        List<MemberGroup> memberGroups = new ArrayList<MemberGroup>();
        //List<String> memberGroupIds = new ArrayList<String>();
        Set<String> memberGroupIds = new HashSet<String>();
        List<Announcement> announcements = new ArrayList<Announcement>();

        Session session = SessionFactoryUtil.getCurrentSession();
        StringBuilder builder = new StringBuilder("select a FROM Announcement a where a.id in (:announcementIds)");
        Query query = session.createQuery( builder.toString() );
        if (announcementIds.isEmpty()) {
            //Initialize list with empty string for hibernate
            announcementIds.add("");
        }
        query.setParameterList( "announcementIds", announcementIds );

        announcements = (List<Announcement>)query.list();

        for (Announcement announcement : announcements) {
            logger.fine("getMemberGroupsOfAnnouncement()" + announcement.getId());
            for (String memberGroupId : announcement.getMemberGroupIds()) {
                memberGroupIds.add(memberGroupId);
            }
            //memberGroupIds.addAll(announcement.getMemberGroupIds());
        }
        logger.fine("memberGroupIds" + memberGroupIds.toString());
        if (!memberGroupIds.isEmpty()) {
            memberGroups = getMemberGroupsByIds(new ArrayList<String>(memberGroupIds), includeDisabled);
        }
        logger.fine("Member Group Size: " + memberGroups.size());
        return memberGroups;
    }

}
