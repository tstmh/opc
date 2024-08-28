package com.stee.spfcore.service.marketing.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.dao.MarketingDAO;
import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.model.marketing.MemberGroup;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.service.marketing.IMarketingService;
import com.stee.spfcore.service.marketing.MarketingServiceException;
import com.stee.spfcore.vo.marketing.MemberGroupNamedValuePair;
import com.stee.spfcore.vo.marketing.MemberGroupSummaryList;
import com.stee.spfcore.vo.personnel.PersonalNricName;

public abstract class AbstractMarketingService implements IMarketingService {

    protected static final Logger logger = Logger.getLogger( AbstractMarketingService.class.getName() );

    protected MarketingDAO dao;

    public AbstractMarketingService() {
        dao = new MarketingDAO();
    }

    @Override
    public MemberGroup getMemberGroup( String id ) throws MarketingServiceException {
        MemberGroup result = null;

        try {
            SessionFactoryUtil.beginTransaction();
            result = dao.getMemberGroup( id );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.SEVERE, "Fail to get member group", e );
            SessionFactoryUtil.rollbackTransaction();
            throw new MarketingServiceException( "Fail to get member group", e );
        }

        return result;
    }

    @Override
    public int getMemberGroupCount( boolean template, boolean includeDisabled ) throws MarketingServiceException {

        int result = 0;

        try {
            SessionFactoryUtil.beginTransaction();
            result = dao.getMemberGroupCount( template, includeDisabled );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.SEVERE, "Fail to get member group count", e );
            SessionFactoryUtil.rollbackTransaction();
            throw new MarketingServiceException( "Fail to get member group count", e );
        }

        return result;
    }

    @Override
    public int getMemberGroupCount( String module, boolean template, boolean includeDisabled ) throws MarketingServiceException {

        int result = 0;

        try {
            SessionFactoryUtil.beginTransaction();
            result = dao.getMemberGroupCount( module, template, includeDisabled );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.SEVERE, "Fail to get member group count", e );
            SessionFactoryUtil.rollbackTransaction();
            throw new MarketingServiceException( "Fail to get member group count", e );
        }

        return result;
    }

    @Override
    public List< MemberGroupSummaryList > getMemberGroupsSummaryList( boolean template, boolean includeDisabled ) throws MarketingServiceException {

        List<MemberGroupSummaryList> result = null;

        try {
            SessionFactoryUtil.beginTransaction();
            result = dao.getMemberGroupsSummaryList( template, includeDisabled );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.SEVERE, "Fail to get member group", e );
            SessionFactoryUtil.rollbackTransaction();
            throw new MarketingServiceException( "Fail to get member group", e );
        }
        return result;
    }

    @Override
    public List< MemberGroup > getMemberGroups( boolean template, boolean includeDisabled ) throws MarketingServiceException {

        List< MemberGroup > result = null;

        try {
            SessionFactoryUtil.beginTransaction();
            result = dao.getMemberGroups( template, includeDisabled );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.SEVERE, "Fail to get member group", e );
            SessionFactoryUtil.rollbackTransaction();
            throw new MarketingServiceException( "Fail to get member group", e );
        }

        return result;
    }

    @Override
    public List< MemberGroup > getMemberGroups( String module, boolean template, boolean includeDisabled ) throws MarketingServiceException {

        List< MemberGroup > result = null;

        try {
            SessionFactoryUtil.beginTransaction();
            result = dao.getMemberGroups( module, template, includeDisabled );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.SEVERE, "Fail to get member group", e );
            SessionFactoryUtil.rollbackTransaction();
            throw new MarketingServiceException( "Fail to get member group", e );
        }

        return result;
    }

    @Override
    public List< MemberGroup > getMemberGroups( int pageNum, int pageSize, boolean template, boolean includeDisabled ) throws MarketingServiceException {

        List< MemberGroup > result = null;

        try {
            SessionFactoryUtil.beginTransaction();
            result = dao.getMemberGroups( pageNum, pageSize, template, includeDisabled );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.SEVERE, "Fail to get member group", e );
            SessionFactoryUtil.rollbackTransaction();
            throw new MarketingServiceException( "Fail to get member group", e );
        }

        return result;
    }

    @Override
    public List< MemberGroup > getMemberGroups( int pageNum, int pageSize, String module, boolean template, boolean includeDisabled ) throws MarketingServiceException {

        List< MemberGroup > result = null;

        try {
            SessionFactoryUtil.beginTransaction();
            result = dao.getMemberGroups( pageNum, pageSize, module, template, includeDisabled );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.SEVERE, "Fail to get member group", e );
            SessionFactoryUtil.rollbackTransaction();
            throw new MarketingServiceException( "Fail to get member group", e );
        }

        return result;
    }

    @Override
    public List< String > getMemberInGroup( String id ) throws MarketingServiceException {

        List< String > result = null;

        try {
            SessionFactoryUtil.beginTransaction();
            result = dao.getMemberInGroup( id );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.SEVERE, "Fail to get member in group", e );
            SessionFactoryUtil.rollbackTransaction();
            throw new MarketingServiceException( "Fail to get member in group", e );
        }

        return result;
    }

    @Override
    public List< PersonalDetail > getPersonnelInGroup( String id ) throws MarketingServiceException {

        List< PersonalDetail > result = null;

        try {
            SessionFactoryUtil.beginTransaction();
            result = dao.getPersonnelInGroup( id );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.SEVERE, "Fail to get personnel in group", e );
            SessionFactoryUtil.rollbackTransaction();
            throw new MarketingServiceException( "Fail to get personnel in group", e );
        }

        return result;
    }

    @Override
    public List< PersonalNricName > getPersonnelNricNameInGroup( String id ) throws MarketingServiceException {

        List< PersonalNricName > result = null;

        try {
            SessionFactoryUtil.beginTransaction();
            result = dao.getPersonnelNricNameInGroup( id );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.SEVERE, "Fail to get personnel in group", e );
            SessionFactoryUtil.rollbackTransaction();
            throw new MarketingServiceException( "Fail to get personnel in group", e );
        }

        return result;
    }

    @Override
    public List< String > getNonExistPersonnel( List< String > users ) throws MarketingServiceException {
        List< String > result = null;

        try {
            SessionFactoryUtil.beginTransaction();
            result = dao.getNonExistPersonnel( users );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            logger.log( Level.SEVERE, "Fail to check if user exists", e );
            SessionFactoryUtil.rollbackTransaction();
            throw new MarketingServiceException( "Fail to check if user exists", e );
        }

        return result;
    }

    public List <MemberGroupNamedValuePair> getMemberGroupNVP (String module, boolean includeDisabled) throws MarketingServiceException {
        List <MemberGroupNamedValuePair> result = null;

        try {
            SessionFactoryUtil.beginTransaction();
            result = dao.getMemberGroupNVP(module, includeDisabled);

            SessionFactoryUtil.commitTransaction();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Fail to get member group named value pair",e);
            SessionFactoryUtil.rollbackTransaction();
            throw new MarketingServiceException("Fail to get member group named value pair",e);
        }

        return result;
    }

}
