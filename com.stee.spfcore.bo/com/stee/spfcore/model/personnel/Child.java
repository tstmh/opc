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
@Table( name = "\"PERSONAL_CHILD_DETAILS\"", schema = "\"SPFCORE\"" )
@XStreamAlias( "Child" )
@Audited
public class Child {

    @Id
    @Column( name = "\"NRIC\"", length = 50 )
    private String nric;

    @Column( name = "\"NAME\"", length = 100 )
    private String name;

    @Temporal( TemporalType.DATE )
    @Column( name = "\"DATE_OF_BIRTH\"" )
    private Date dateOfBirth;

    @Column( name = "\"ID_TYPE\"", length = 50 )
    private String idType;
    
    public Child() {
        super();
    }

    public Child( String nric, String name, Date dateOfBirth, String idType ) {
        super();
        this.nric = nric;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
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

	public void setNric( String nric ) {
        this.nric = nric;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public void setDateOfBirth( Date dateOfBirth ) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getIdType () {
			return idType;
		}
		
		public void setIdType (String idType) {
			this.idType = idType;
		}

		public void preSave() {
        if ( name != null ) {
            name = name.toUpperCase();
        }

        if ( nric != null ) {
            nric = nric.toUpperCase();
        }
    }

    public String toString() {
        return String.format( "[nric=%s,name=%s,dateOfBirth=%s]", this.nric, this.name, this.dateOfBirth );
    }
}
