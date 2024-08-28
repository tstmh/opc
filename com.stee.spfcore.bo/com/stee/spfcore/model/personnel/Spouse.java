package com.stee.spfcore.model.personnel;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table( name = "\"PERSONAL_SPOUSE_DETAILS\"", schema = "\"SPFCORE\"" )
@XStreamAlias( "Spouse" )
@Audited
public class Spouse {

    @Id
    @Column( name = "\"NRIC\"", length = 50 )
    private String nric;

    @Column( name = "\"NAME\"", length = 100 )
    private String name;

    @Temporal( TemporalType.DATE )
    @Column( name = "\"DATE_OF_BIRTH\"" )
    private Date dateOfBirth;

    @Temporal( TemporalType.DATE )
    @Column( name = "\"DATE_OF_MARRIAGE\"" )
    private Date dateOfMarriage;

    @Column( name = "\"CERTIFICATE_NO\"", length = 50 )
    private String certificateNumber;

    @Column( name = "\"ID_TYPE\"", length = 50 )
    private String idType;

    public Spouse() {
        super();
    }

    public Spouse( String nric, String name, Date dateOfBirth, Date dateOfMarriage, String certificateNumber, String idType ) {
        super();
        this.nric = nric;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.dateOfMarriage = dateOfMarriage;
        this.certificateNumber = certificateNumber;
        this.idType = idType;
    }

    public String getNric() {
        return nric;
    }

    public String getName() {
        return name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public Date getDateOfMarriage() {
        return dateOfMarriage;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setNric( String nric ) {
        this.nric = nric;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public void setDateOfBirth( Date dateOfBirth ) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setDateOfMarriage( Date dateOfMarriage ) {
        this.dateOfMarriage = dateOfMarriage;
    }

    public void setCertificateNumber( String certificateNumber ) {
        this.certificateNumber = certificateNumber;
    }

    public String getIdType () {
			return idType;
		}
		
		public void setIdType (String idType) {
			this.idType = idType;
		}

		public void preSave() {
        if ( nric != null ) {
            nric = nric.toUpperCase();
        }

        if ( name != null ) {
            name = name.toUpperCase();
        }

        if ( certificateNumber != null ) {
            certificateNumber = certificateNumber.toUpperCase();
        }

    }

    public String toString() {
        return String.format( "[nric=%s,name=%s,dateOfBirth=%s,dateOfMarriage=%s,certificateNumber=%s]", this.nric, this.name, this.dateOfBirth, this.dateOfMarriage, this.certificateNumber );
    }
}
