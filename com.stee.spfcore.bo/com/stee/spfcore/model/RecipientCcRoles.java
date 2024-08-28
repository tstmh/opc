package com.stee.spfcore.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table( name = "RECIPIENT_CC_ROLES", schema = "SPFCORE" )
@XStreamAlias( "RecipientCcRoles" )
@Audited
public class RecipientCcRoles {
    @Id
    @GenericGenerator( name = "RecipientCcRolesGenerator", strategy = "com.stee.spfcore.dao.utils.StringIdHibernateGenerator" )
    @GeneratedValue( generator = "RecipientCcRolesGenerator" )
    @Column( name = "ID" )
    @XStreamOmitField
    private String id;

    @ElementCollection( fetch = FetchType.EAGER )
    @CollectionTable( name = "RECIPIENT_CC_ROLES_RECIPENT_ROLES", schema = "SPFCORE", joinColumns = @JoinColumn( name = "RECIPIENT_CC_ROLES_ID" ) )
    @Column( name = "RECIPENT_ROLE" )
    @Fetch( value = FetchMode.SUBSELECT )
    private List< String > recipentRoles;

    @ElementCollection( fetch = FetchType.EAGER )
    @CollectionTable( name = "RECIPIENT_CC_ROLES_CC_ROLES", schema = "SPFCORE", joinColumns = @JoinColumn( name = "RECIPIENT_CC_ROLES_ID" ) )
    @Column( name = "CC_ROLE" )
    @Fetch( value = FetchMode.SUBSELECT )
    private List< String > ccRoles;

    public RecipientCcRoles() {
        // DO NOTHING
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public List< String > getRecipentRoles() {
        return recipentRoles;
    }

    public void setRecipentRoles( List< String > recipentRoles ) {
        if ( this.recipentRoles == null ) {
            this.recipentRoles = new ArrayList<>();
        }
        this.recipentRoles.clear();
        if ( recipentRoles != null ) {
            this.recipentRoles.addAll( recipentRoles );
        }
    }

    public List< String > getCcRoles() {
        return ccRoles;
    }

    public void setCcRoles( List< String > ccRoles ) {
        if ( this.ccRoles == null ) {
            this.ccRoles = new ArrayList<>();
        }
        this.ccRoles.clear();
        if ( ccRoles != null ) {
            this.ccRoles.addAll( ccRoles );
        }
    }
}
