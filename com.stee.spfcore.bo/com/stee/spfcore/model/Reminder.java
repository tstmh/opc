package com.stee.spfcore.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table( name = "REMINDER", schema = "SPFCORE" )
@XStreamAlias( "Reminder" )
@Audited
public class Reminder {
    @Id
    @GenericGenerator( name = "ReminderGenerator", strategy = "com.stee.spfcore.dao.utils.StringIdHibernateGenerator" )
    @GeneratedValue( generator = "ReminderGenerator" )
    @Column( name = "ID" )
    @XStreamOmitField
    private String id;

    @Column( name = "WORKING_DAYS" )
    private int workingDays;

    @Temporal( TemporalType.TIMESTAMP )
    private Date time;

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true )
    @JoinTable( name = "REMINDER_RECIPIENT_CC_ROLES", schema = "SPFCORE", joinColumns = @JoinColumn( name = "RECIPIENT_CC_ROLES_ID" ), inverseJoinColumns = @JoinColumn( name = "REMINDER_ID" ) )
    @Fetch( value = FetchMode.SUBSELECT )
    private List< RecipientCcRoles > recipentCcRoles;

    public Reminder() {
        // DO NOTHING
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public int getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays( int workingDays ) {
        this.workingDays = workingDays;
    }

    public List< RecipientCcRoles > getRecipentCcRoles() {
        return recipentCcRoles;
    }

    public void setRecipentCcRoles( List< RecipientCcRoles > recipentCcRoles ) {
        if ( this.recipentCcRoles == null ) {
            this.recipentCcRoles = new ArrayList<>();
        }
        this.recipentCcRoles.clear();
        if ( recipentCcRoles != null ) {
            this.recipentCcRoles.addAll( recipentCcRoles );
        }
    }

    public Date getTime() {
        return time;
    }

    public void setTime( Date time ) {
        this.time = time;
    }
}
