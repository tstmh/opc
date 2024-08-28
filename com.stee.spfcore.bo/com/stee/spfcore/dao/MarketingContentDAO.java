package com.stee.spfcore.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;

import com.stee.spfcore.dao.utils.SequenceUtil;
import com.stee.spfcore.model.marketingContent.ContentTemplate;
import com.stee.spfcore.model.marketingContent.MarketingContent;
import com.stee.spfcore.model.marketingContent.MarketingContentSet;
import com.stee.spfcore.model.marketingContent.TemplateResource;
import com.stee.spfcore.model.marketingContent.internal.BinaryFile;
import com.stee.spfcore.model.marketingContent.internal.ContentIdBinaryFileJoint;
import com.stee.spfcore.model.marketingContent.internal.EmailLog;
import com.stee.spfcore.model.marketingContent.internal.HtmlFile;
import com.stee.spfcore.model.marketingContent.internal.PublishingContent;
import com.stee.spfcore.model.marketingContent.internal.PublishingContentSetTask;
import com.stee.spfcore.model.marketingContent.internal.SmsLog;
import com.stee.spfcore.model.marketingContent.internal.UserContentViewRecord;
import com.stee.spfcore.vo.marketingContent.BinaryAttachment;
import com.stee.spfcore.vo.marketingContent.ContentTemplateInfo;

public class MarketingContentDAO {

    public void addMarketingContentSet( MarketingContentSet contentSet, String requester ) {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getCurrentSession();

        session.save( contentSet );

        session.flush();
    }

    public void updateMarketingContentSet( MarketingContentSet contentSet, String requester ) {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getCurrentSession();

        session.merge( contentSet );

        session.flush();
    }

    public void deleteMarketingContentSet( MarketingContentSet contentSet, String requester ) {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getCurrentSession();

        session.delete( contentSet );
    }

    public MarketingContentSet getMarketingContentSet( String id ) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        return ( MarketingContentSet ) session.get( MarketingContentSet.class, id );
    }

    public MarketingContent getMarketingContent( String id ) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        return ( MarketingContent ) session.get( MarketingContent.class, id );
    }

    @SuppressWarnings( "unchecked" )
    public List< MarketingContent > getMarketingContents( String module ) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        Query query = session.createQuery( "FROM MarketingContent c where c.module = :module" );

        query.setParameter( "module", module );

        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< MarketingContent > getMarketingContents( String module, boolean templateBased ) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        Query query = session.createQuery( "FROM MarketingContent c where c.module = :module and templateBased = :templateBased" );

        query.setParameter( "module", module );
        query.setParameter( "templateBased", templateBased );

        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< MarketingContentSet > getMarketingContentSets( String module ) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        Query query = session.createQuery( "select distinct ms FROM MarketingContentSet as ms inner join ms.contents as content where content.module = :module" );

        query.setParameter( "module", module );

        return query.list();
    }

    public String generateId() {
        return SequenceUtil.getInstance().getNextSequenceValue( MarketingContentSet.class );
    }

    @SuppressWarnings( "unchecked" )
    public List< String > getContentIdInSet( String contentSetId ) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        Query query = session.getNamedQuery( "findContentIdsInSetNative" );
        query.setParameter( "contentSetId", contentSetId );

        return query.list();
    }

    public void addPublishingContentSetTask( PublishingContentSetTask task, String requester ) {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getCurrentSession();

        session.save( task );

        session.flush();
    }

    public void updatePublishingContentSetTask( PublishingContentSetTask task, String requester ) {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getCurrentSession();

        session.merge( task );

        session.flush();
    }

    public PublishingContentSetTask getPublishingContentSetTask( String contentSetId ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        String queryString = "from PublishingContentSetTask task where task.contentSetId = :contentSetId";

        Query query = session.createQuery( queryString );
        query.setParameter( "contentSetId", contentSetId );

        return ( PublishingContentSetTask ) query.uniqueResult();
    }

    @SuppressWarnings( "unchecked" )
    public List< PublishingContent > getPendingTransferContents() {

        Session session = SessionFactoryUtil.getCurrentSession();

        String queryString = "select content from PublishingContentSetTask as task inner join task.contents as content " + "where task.cancelled = false and content.transferred = false";

        Query query = session.createQuery( queryString );

        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< PublishingContentSetTask > getPendingEmailSendingContentSets() {

        Session session = SessionFactoryUtil.getCurrentSession();

        String queryString = "select distinct task from PublishingContentSetTask as task inner join task.contents as content " + "where task.cancelled = false and content.notificationSent = false and content.publishDate < :dateTime";

        Query query = session.createQuery( queryString );
        query.setParameter( "dateTime", new Date() );

        return query.list();
    }

    public void updatePublishingContent( PublishingContent content, String requester ) {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getCurrentSession();

        session.merge( content );

        session.flush();
    }

    public PublishingContent getPublishingContent( String contentId ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        String queryString = "select c from PublishingContent c where c.contentId = :contentId";

        Query query = session.createQuery( queryString );
        query.setParameter( "contentId", contentId );

        return ( PublishingContent ) query.uniqueResult();
    }

    public void saveBinaryFile( BinaryFile file, String requester ) {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getCurrentSession();

        session.saveOrUpdate( file );

        session.flush();
    }

    public void saveHtmlFile( HtmlFile file, String requester ) {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getCurrentSession();

        session.saveOrUpdate( file );

        session.flush();
    }

    public BinaryFile getBinaryFile( String id ) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        return ( BinaryFile ) session.get( BinaryFile.class, id );
    }

    public BinaryFile getBinaryFile( String nric, String id ) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

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

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuilder queryString = new StringBuilder();
        queryString.append( "select file from HtmlFile file where file.contentId in ");
        queryString.append( "(select a.contentId from UserAnnouncement a where a.nric = :nric and a.contentId = :contentId)" );

        Query query = session.createQuery( queryString.toString() );
        query.setParameter( "contentId", id );
        query.setParameter( "nric", nric );

        return ( HtmlFile ) query.uniqueResult();
    }

    public void saveUserContentViewRecord( UserContentViewRecord record ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        session.saveOrUpdate( record );

        session.flush();
    }

    public void addEmailLog( EmailLog emailLog ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        session.save( emailLog );

        session.flush();
    }

    public void addEmailLogs( String contentId, List< String > emails ) {

        Date sentDate = new Date();

        Session session = SessionFactoryUtil.getCurrentSession();

        for ( int i = 0; i < emails.size(); i++ ) {
            EmailLog log = new EmailLog( 0, emails.get( i ), contentId, sentDate );
            session.save( log );

            if ( i % 20 == 0 ) { //20, same as the JDBC batch size
                //flush a batch of inserts and release memory:
                session.flush();
                session.clear();
            }
        }
    }

    public void addSmsLog( SmsLog smsLog ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        session.save( smsLog );

        session.flush();
    }

    public void addSmsLogs( String contentId, Map< String, String > smsStatusMap ) {

        Date sentDate = new Date();

        Session session = SessionFactoryUtil.getCurrentSession();
        List< String > numbers = new ArrayList< String >( smsStatusMap.keySet() );

        for ( int i = 0; i < numbers.size(); i++ ) {
            String number = numbers.get( i );
            SmsLog log = new SmsLog( 0, number, contentId, sentDate, smsStatusMap.get( number ) );
            session.save( log );

            if ( i % 20 == 0 ) { //20, same as the JDBC batch size
                //flush a batch of inserts and release memory:
                session.flush();
                session.clear();
            }
        }
    }

    @SuppressWarnings( "unchecked" )
    public List< BinaryAttachment > getAttachments( String contentId ) {
        List< BinaryAttachment > results = new ArrayList< BinaryAttachment >();
        Session session = SessionFactoryUtil.getCurrentSession();
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

    public void addContentIdBinaryFileJoint( ContentIdBinaryFileJoint joint ) {
        Session session = SessionFactoryUtil.getCurrentSession();
        joint.setId( null );
        session.save( joint );
    }

    @SuppressWarnings( "unchecked" )
    public ContentIdBinaryFileJoint getContentIdBinaryFileJoint( String contentId, String docId ) {
        Session session = SessionFactoryUtil.getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        queryString.append( "select j from ContentIdBinaryFileJoint j where 1=1 " );
        queryString.append( "and j.contentId = :contentId " );
        queryString.append( "and j.docId = :docId " );
        Query query = session.createQuery( queryString.toString() );
        query.setParameter( "contentId", contentId );
        query.setParameter( "docId", docId );
        ContentIdBinaryFileJoint result = null;
        List< ContentIdBinaryFileJoint > results = ( List< ContentIdBinaryFileJoint > ) query.list();
        if ( results != null ) {
            if ( results.size() > 0 ) {
                result = results.get( 0 );
            }
        }
        return result;
    }

    @SuppressWarnings( "unchecked" )
    public boolean isBinaryFileExist( String docId ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        queryString.append( "select count(b) from BinaryFile b where b.docId = :docId " );
        Query query = session.createQuery( queryString.toString() );
        query.setParameter( "docId", docId );
        boolean result = false;
        List< Long > results = ( List< Long > ) query.list();
        if ( results != null ) {
            if ( results.size() > 0 ) {
                Long countObj = ( Long ) results.get( 0 );
                if ( countObj != null ) {
                    if ( countObj.longValue() > 0 ) {
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<ContentTemplateInfo> getTemplateInfos () {

        Session session = SessionFactoryUtil.getCurrentSession();

        Query query = session.createQuery("select v.id as id, v.displayName as displayName FROM ContentTemplate v");
        query.setResultTransformer( new AliasToBeanResultTransformer( ContentTemplateInfo.class ) );

        return query.list();
    }

    public ContentTemplate getContentTemplate( String id ) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        return ( ContentTemplate ) session.get( ContentTemplate.class, id );
    }

    public TemplateResource getTemplateResource (String id) {

        Session session = SessionFactoryUtil.getCurrentSession();

        return (TemplateResource) session.get( TemplateResource.class, id );
    }

}
