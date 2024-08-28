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
@Table( name = "\"PERSONAL_PHONE_DETAILS\"", schema = "\"SPFCORE\"" )
@XStreamAlias( "Phone" )
@Audited
public class Phone {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "\"ID\"" )
    @XStreamOmitField
    private long id;

    @Column( name = "\"PHONE_NUMBER\"", length = 16 )
    private String number;

    @Enumerated( EnumType.STRING )
    @Column( name = "\"LABEL\"", length = 10 )
    private ContactLabel label;

    @Column( name = "\"PREFER\"" )
    private boolean prefer;

    public Phone() {
        super();
    }

    public Phone( ContactLabel label, String number, boolean prefer ) {
        super();
        this.label = label;
        this.number = number;
        this.prefer = prefer;
    }

    public ContactLabel getLabel() {
        return label;
    }

    public String getNumber() {
        return number;
    }

    public boolean isPrefer() {
        return prefer;
    }

    public void setLabel( ContactLabel label ) {
        this.label = label;
    }

    public void setNumber( String number ) {
        this.number = number;
    }

    public void setPrefer( boolean prefer ) {
        this.prefer = prefer;
    }

    public String toString() {
        return String.format( "[id=%s,number=%s,label=%s,prefer=%s]", this.id, this.number, this.label, this.prefer );
    }
}
