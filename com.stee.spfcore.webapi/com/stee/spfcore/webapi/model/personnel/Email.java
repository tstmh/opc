package com.stee.spfcore.webapi.model.personnel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table( name = "\"PERSONAL_EMAIL_DETAILS\"", schema = "\"SPFCORE\"" )
@XStreamAlias( "Email" )
@Audited
public class Email {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "\"ID\"" )
    @XStreamOmitField
    private long id;

    @Column( name = "\"ADDRESS\"", length = 256 )
    private String address;

    @Enumerated( EnumType.STRING )
    @Column( name = "\"LABEL\"", length = 10 )
    private ContactLabel label;

    @Column( name = "\"PREFER\"" )
    private boolean prefer;

    public Email() {
        super();
    }

    public Email( ContactLabel label, String address, boolean prefer ) {
        super();
        this.label = label;
        this.address = address;
        this.prefer = prefer;
    }

    public ContactLabel getLabel() {
        return label;
    }

    public String getAddress() {
        return address;
    }

    public boolean isPrefer() {
        return prefer;
    }

    public void setLabel( ContactLabel label ) {
        this.label = label;
    }

    public void setAddress( String address ) {
        this.address = address;
    }

    public void setPrefer( boolean prefer ) {
        this.prefer = prefer;
    }

    public void preSave() {
        if ( address != null ) {
            address = address.toLowerCase();
        }
    }

    public String toString() {
        return String.format( "[id=%s,address=%s,label=%s,prefer=%s]", this.id, this.address, this.label, this.prefer );
    }
}
