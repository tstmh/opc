package com.stee.spfcore.webapi.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stee.spfcore.webapi.model.code.CodeType;
import com.stee.spfcore.webapi.model.marketing.CodeRule;
import com.stee.spfcore.webapi.model.marketing.Field;
import com.stee.spfcore.webapi.model.marketing.ListRule;
import com.stee.spfcore.webapi.model.marketing.ListRuleValue;
import com.stee.spfcore.webapi.model.marketing.MemberGroup;
import com.stee.spfcore.webapi.model.marketing.Rule;
import com.stee.spfcore.webapi.model.marketing.RuleSet;
import com.stee.spfcore.webapi.model.marketing.RuleType;

@Repository
public class MarketingDAO {
	
	private EntityManager entityManager;
	private static final Logger logger = LoggerFactory.getLogger(MarketingDAO.class);
	
	public MarketingDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
    public MemberGroup getMemberGroup( String id ) {
    	logger.info("Get Member Group");

    	Session session = entityManager.unwrap(Session.class);

        MemberGroup group = ( MemberGroup ) session.get( MemberGroup.class, id );

        if ( group != null ) {
            populateDisplayValues( group, session );
        }

        return group;
    }
    
    @SuppressWarnings( "unchecked" )
    public List< String > getMemberInGroup( String groupId ) {
    	logger.info("Get Member in Group");
    	Session session = entityManager.unwrap(Session.class);

        MemberGroup group = getMemberGroup( groupId );
        if ( group == null ) {
            return Collections.EMPTY_LIST;
        }

        StringBuilder builder = new StringBuilder( "SELECT distinct p.nric " );
        Map< String, Object > properties = new HashMap< String, Object >();

        String orderByField = MarketingQueryBuilder.processOrderByField( group, builder, true );

        MarketingQueryBuilder.build( group, builder, properties );

        MarketingQueryBuilder.appendOrderByField( group, orderByField, builder );

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
    private void populateDisplayValues( MemberGroup group, Session session ) {
    	logger.info("Populate Display Values");
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
    
    private void populateCodeRuleDescription( CodeRule codeRule, Session session ) {
    	logger.info("Populate Code Rule Description");
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
    private void populateListValue( ListRule listRule, Session session ) {
    	logger.info("Populate List Value");
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
    	logger.info("Populate Code List Description");
        List< ListRuleValue > ruleValues = listRule.getValues();
        for ( ListRuleValue value : ruleValues ) {
            value.setDisplayValue( getDescription( session, value.getValue(), codeType ) );
        }
    }
    
    private void populateInterestListDescription( ListRule listRule, Session session ) {
    	logger.info("Populate Interest List Description");
        List< ListRuleValue > ruleValues = listRule.getValues();
        for ( ListRuleValue value : ruleValues ) {
            value.setDisplayValue( getSubcategoryName( session, value.getValue() ) );
        }
    }
    
    private void populateMembershipStatusListDescription( ListRule listRule ) {
    	logger.info("Populate Membership Status List Description");
        List< ListRuleValue > ruleValues = listRule.getValues();
        for ( ListRuleValue value : ruleValues ) {
            value.setDisplayValue( value.getValue() );
        }
    }
    
    private void populateMembershipTypeListDescription( ListRule listRule ) {
    	logger.info("Populate Membershup Type List Description");
        List< ListRuleValue > ruleValues = listRule.getValues();
        for ( ListRuleValue value : ruleValues ) {
            value.setDisplayValue( value.getValue() );
        }
    }
    
    private void populateCourseTitleListDescription( ListRule listRule, Session session ) {
    	logger.info("Populate Course Title List Description");
        List< ListRuleValue > ruleValues = listRule.getValues();
        for ( ListRuleValue value : ruleValues ) {
            value.setDisplayValue( getSubcategoryName( session, value.getValue() ) );
        }
    }
    
    private void populateBlacklistModuleListDescription( ListRule listRule, Session session ) {
    	logger.info("Populate Blacklist Module List Description");
        List< ListRuleValue > ruleValues = listRule.getValues();
        for ( ListRuleValue value : ruleValues ) {
            value.setDisplayValue( getBlacklistModuleDisplayName( session, value.getValue() ) );
        }
    }
    
    private String getBlacklistModuleDisplayName( Session session, String id ) {
    	logger.info("Get Blacklist Module Display Name");
        Query query = session.createQuery( "SELECT m.name FROM BlacklistModule m where m.id = :id" );
        query.setParameter( "id", id );

        return ( String ) query.uniqueResult();
    }
    
    private String getDescription( Session session, String id, CodeType type ) {
    	logger.info("Get Description");
        Query query = session.createQuery( "SELECT c.description FROM Code c where c.id = :id and c.type = :type" );
        query.setParameter( "id", id );
        query.setParameter( "type", type );

        return ( String ) query.uniqueResult();
    }
    
    @SuppressWarnings( "unchecked" )
    private String getSubcategoryName( Session session, String id ) {
    	logger.info("Get Subcategory Name");
        Query query = session.createQuery( "SELECT c.name, sc.name FROM Category c inner join c.subCategories sc where sc.id = :id" );
        query.setParameter( "id", id );

        List< Object[] > result = query.list();

        if ( !result.isEmpty() ) {
            Object[] objects = result.get( 0 );
            return objects[ 0 ] + " - " + objects[ 1 ];
        }
        return null;
    }
    
    
}
