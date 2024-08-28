package com.stee.spfcore.webapi.model.marketingContent.internal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table( name = "MARKETING_CONTENT_ID_BINARY_FILES_JOINT", schema = "SPFCORE" )
@XStreamAlias( "MarketingContentIdBinaryFile" )
public class ContentIdBinaryFileJoint {
    @Id
    @GenericGenerator( name = "MarketingContentIdBinaryFileGenerator", strategy = "com.stee.spfcore.webapi.dao.utils.StringIdHibernateGenerator" )
    @GeneratedValue( generator = "MarketingContentIdBinaryFileGenerator" )
    @Column( name = "ID" )
    private String id;

    @Column( name = "CONTENT_ID" )
    @Index( name = "MARKETING_CONTENT_ID_BINARY_FILES_JOINT_IDX_CONTENT_ID" )
    private String contentId;

    @Column( name = "DOC_ID" )
    @Index( name = "MARKETING_CONTENT_ID_BINARY_FILES_JOINT_IDX_DOC_ID" )
    private String docId;

    public ContentIdBinaryFileJoint() {
    }

    public ContentIdBinaryFileJoint( String contentId, String docId ) {
        this.contentId = contentId;
        this.docId = docId;
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId( String contentId ) {
        this.contentId = contentId;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId( String docId ) {
        this.docId = docId;
    }
}

