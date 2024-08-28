package com.stee.spfcore.model.template;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table( name = "TEMPLATES", schema = "SPFCORE" )
@XStreamAlias( "Template" )
@Audited
public class Template {
    @Id
    @Column( name = "ID" )
    private long id;

    @Column( name = "MODULE_NAME", length = 50 )
    private String moduleName;

    @Column( name = "NAME", length = 50 )
    private String name;

    @Column( name = "EMAIL_SUBJECT", length = 256 )
    private String emailSubject;

    @Column( name = "EMAIL_BODY", length = 32000 )
    private String emailBody;

    @Column( name = "SMS_TEXT", length = 1500 )
    private String smsText;

    @Column( name = "IS_FOR_EMAIL" )
    private boolean isForEmail;

    @Column( name = "IS_FOR_SMS" )
    private boolean isForSms;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "TIMESTAMP", nullable = false )
    private Date timestamp;

    @ManyToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    @JoinTable( name = "TEMPLATE_TAG_JOINTS", schema = "SPFCORE", joinColumns = @JoinColumn( name = "TEMPLATE_ID" ), inverseJoinColumns = @JoinColumn( name = "TAG_ID" ) )
    private List< Tag > usableTags;

    public Template() {
        // DO NOTHING
    }

    @PrePersist
    protected void onCreate() {
        timestamp = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        timestamp = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName( String moduleName ) {
        this.moduleName = moduleName;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject( String emailSubject ) {
        this.emailSubject = emailSubject;
    }

    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody( String emailBody ) {
        this.emailBody = emailBody;
    }

    public String getSmsText() {
        return smsText;
    }

    public void setSmsText( String smsText ) {
        this.smsText = smsText;
    }

    public List< Tag > getUsableTags() {
        return usableTags;
    }

    public void setUsableTags( List< Tag > usableTags ) {
        this.usableTags = usableTags;
    }

    public boolean isForEmail() {
        return isForEmail;
    }

    public void setForEmail( boolean isForEmail ) {
        this.isForEmail = isForEmail;
    }

    public boolean isForSms() {
        return isForSms;
    }

    public void setForSms( boolean isForSms ) {
        this.isForSms = isForSms;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp( Date timestamp ) {
        this.timestamp = timestamp;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( emailBody == null ) ? 0 : emailBody.hashCode() );
        result = prime * result + ( ( emailSubject == null ) ? 0 : emailSubject.hashCode() );
        result = prime * result + ( int ) ( id ^ ( id >>> 32 ) );
        result = prime * result + ( isForEmail ? 1231 : 1237 );
        result = prime * result + ( isForSms ? 1231 : 1237 );
        result = prime * result + ( ( moduleName == null ) ? 0 : moduleName.hashCode() );
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        result = prime * result + ( ( smsText == null ) ? 0 : smsText.hashCode() );
        result = prime * result + ( ( timestamp == null ) ? 0 : timestamp.hashCode() );
        result = prime * result + ( ( usableTags == null ) ? 0 : usableTags.hashCode() );
        return result;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        Template other = ( Template ) obj;
        if ( emailBody == null ) {
            if ( other.emailBody != null )
                return false;
        }
        else if ( !emailBody.equals( other.emailBody ) )
            return false;
        if ( emailSubject == null ) {
            if ( other.emailSubject != null )
                return false;
        }
        else if ( !emailSubject.equals( other.emailSubject ) )
            return false;
        if ( id != other.id )
            return false;
        if ( isForEmail != other.isForEmail )
            return false;
        if ( isForSms != other.isForSms )
            return false;
        if ( moduleName == null ) {
            if ( other.moduleName != null )
                return false;
        }
        else if ( !moduleName.equals( other.moduleName ) )
            return false;
        if ( name == null ) {
            if ( other.name != null )
                return false;
        }
        else if ( !name.equals( other.name ) )
            return false;
        if ( smsText == null ) {
            if ( other.smsText != null )
                return false;
        }
        else if ( !smsText.equals( other.smsText ) )
            return false;
        if ( timestamp == null ) {
            if ( other.timestamp != null )
                return false;
        }
        else if ( !timestamp.equals( other.timestamp ) )
            return false;
        if ( usableTags == null ) {
            if ( other.usableTags != null )
                return false;
        }
        else if ( !usableTags.equals( other.usableTags ) )
            return false;
        return true;
    }
}
