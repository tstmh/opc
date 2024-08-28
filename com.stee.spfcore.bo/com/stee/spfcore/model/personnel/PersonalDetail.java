package com.stee.spfcore.model.personnel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table( name = "PERSONAL_DETAILS", schema = "SPFCORE" )
@XStreamAlias( "PersonalDetail" )
@Audited
public class PersonalDetail {

    @Id
    @Column( name = "\"NRIC\"", length = 10 )
    private String nric;

    @Column( name = "\"NAME\"", length = 255 )
    private String name;

    @Temporal( TemporalType.DATE )
    @Column( name = "\"DATE_OF_BIRTH\"" )
    private Date dateOfBirth;

    @Column( name = "\"CITIZENSHIP\"", length = 50 )
    private String citizenship;

    @Column( name = "\"NATIONALITY\"", length = 50 )
    private String nationality;

    @Column( name = "\"RACE\"", length = 50 )
    private String race;

    @Column( name = "\"GENDER\"", length = 50 )
    private String gender;

    @Column( name = "\"ADDRESS_TYPE\"", length = 50 )
    private String addressType;

    @Column( name = "\"BLOCK_NUMBER\"", length = 10 )
    private String blockNumber;

    @Column( name = "\"STREET_NAME\"", length = 32 )
    private String streetName;

    @Column( name = "\"FLOOR_NUMBER\"", length = 3 )
    private String floorNumber;

    @Column( name = "\"UNIT_NUMBER\"", length = 5 )
    private String unitNumber;

    @Column( name = "\"BUILDING_NAME\"", length = 32 )
    private String buildingName;

    @Column( name = "\"POSTAL_CODE\"", length = 6 )
    private String postalCode;

    @Column( name = "\"RESIDENTIAL_ADDRESS\"", length = 500 )
    private String residentialAddress;

    @Column( name = "\"MARITAL_STATUS\"", length = 50 )
    private String maritalStatus;

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true )
    @JoinColumn( name = "\"PERSONAL_NRIC\"" )
    @Fetch( value = FetchMode.SUBSELECT )
    private List< Spouse > spouses;

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true )
    @JoinColumn( name = "\"PERSONAL_NRIC\"" )
    @Fetch( value = FetchMode.SUBSELECT )
    private List< Child > children;

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true )
    @JoinColumn( name = "\"PERSONAL_NRIC\"" )
    @Fetch( value = FetchMode.SUBSELECT )
    private List< Phone > phoneContacts;

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true )
    @JoinColumn( name = "\"PERSONAL_NRIC\"" )
    @Fetch( value = FetchMode.SUBSELECT )
    private List< Email > emailContacts;

    @Enumerated( EnumType.STRING )
    @Column( name = "\"PREFERRED_CONTACT_MODE\"", length = 10 )
    private ContactMode preferredContactMode;

    @Column( name = "\"EDUATION_LEVEL\"", length = 50 )
    private String eduationLevel;

    @OneToOne( cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true )
    @JoinColumn( name = "\"NRIC\"" )
    private Employment employment;

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true )
    @JoinColumn( name = "\"PERSONAL_NRIC\"" )
    @Fetch( value = FetchMode.SUBSELECT )
    private List< Leave > leaves;

    @Column( name = "\"REMARKS\"", length = 256 )
    private String remarks;

    @ElementCollection( fetch = FetchType.EAGER )
    @CollectionTable( name = "PERSONAL_DETAILS_SUB_CATEGORY_IDS", schema = "SPFCORE", joinColumns = @JoinColumn( name = "PERSONAL_DETAILS_NRIC" ) )
    @Column( name = "\"SUB_CATEGORY_ID\"", length = 10 )
    @Fetch( value = FetchMode.SUBSELECT )
    private List< String > subCategoryIds;

    public PersonalDetail() {
        super();
    }

    public PersonalDetail( String nric ) {
        super();
        this.nric = nric;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth( Date dateOfBirth ) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship( String citizenship ) {
        this.citizenship = citizenship;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality( String nationality ) {
        this.nationality = nationality;
    }

    public String getRace() {
        return race;
    }

    public void setRace( String race ) {
        this.race = race;
    }

    public String getGender() {
        return gender;
    }

    public void setGender( String gender ) {
        this.gender = gender;
    }

    public String getResidentialAddress() {
        return residentialAddress;
    }

    public void setResidentialAddress( String residentialAddress ) {
// DO NOTHING
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus( String maritalStatus ) {
        this.maritalStatus = maritalStatus;
    }

    public List< Spouse > getSpouses() {
        return spouses;
    }

    public void setSpouses( List< Spouse > spouses ) {
        if ( this.spouses == null ) {
            this.spouses = new ArrayList<>();
        }
        this.spouses.clear();
        if ( spouses != null ) {
            this.spouses.addAll( spouses );
        }
    }

    public List< Child > getChildren() {
        return children;
    }

    public void setChildren( List< Child > children ) {
        if ( this.children == null ) {
            this.children = new ArrayList<>();
        }
        this.children.clear();
        if ( children != null ) {
            this.children.addAll( children );
        }
    }

    public List< Phone > getPhoneContacts() {
        return phoneContacts;
    }

    public void setPhoneContacts( List< Phone > phoneContacts ) {
        if ( this.phoneContacts == null ) {
            this.phoneContacts = new ArrayList<>();
        }
        this.phoneContacts.clear();
        if ( phoneContacts != null ) {
            this.phoneContacts.addAll( phoneContacts );
        }
    }

    public List< Email > getEmailContacts() {
        return emailContacts;
    }

    public void setEmailContacts( List< Email > emailContacts ) {
        if ( this.emailContacts == null ) {
            this.emailContacts = new ArrayList<>();
        }
        this.emailContacts.clear();
        if ( emailContacts != null ) {
            this.emailContacts.addAll( emailContacts );
        }
    }

    public ContactMode getPreferredContactMode() {
        return preferredContactMode;
    }

    public void setPreferredContactMode( ContactMode preferredContactMode ) {
        this.preferredContactMode = preferredContactMode;
    }

    public String getEduationLevel() {
        return eduationLevel;
    }

    public void setEduationLevel( String eduationLevel ) {
        this.eduationLevel = eduationLevel;
    }

    public Employment getEmployment() {
        return employment;
    }

    public void setEmployment( Employment employment ) {
        this.employment = employment;
        this.employment.setNric( nric );
    }

    public String getNric() {
        return nric;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber( String blockNumber ) {
        this.blockNumber = blockNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName( String streetName ) {
        this.streetName = streetName;
    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber( String floorNumber ) {
        this.floorNumber = floorNumber;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber( String unitNumber ) {
        this.unitNumber = unitNumber;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName( String buildingName ) {
        this.buildingName = buildingName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode( String postalCode ) {
        this.postalCode = postalCode;
    }

    public List< Leave > getLeaves() {
        return leaves;
    }

    public void setLeaves( List< Leave > leaves ) {
        if ( this.leaves == null ) {
            this.leaves = new ArrayList<>();
        }
        this.leaves.clear();
        if ( leaves != null ) {
            this.leaves.addAll( leaves );
        }
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType( String addressType ) {
        this.addressType = addressType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks( String remarks ) {
        this.remarks = remarks;
    }

    public List< String > getSubCategoryIds() {
        return subCategoryIds;
    }

    public void setSubCategoryIds( List< String > subCategoryIds ) {
        if ( this.subCategoryIds == null ) {
            this.subCategoryIds = new ArrayList<>();
        }
        this.subCategoryIds.clear();
        if ( subCategoryIds != null ) {
            this.subCategoryIds.addAll( subCategoryIds );
        }
    }

    private void buildResidentialAddress() {

        StringBuilder builder = new StringBuilder();

        if ( ( blockNumber != null ) && ( blockNumber.length() > 0 ) ) {
            builder.append( blockNumber );
        }

        if ( ( streetName != null ) && ( streetName.length() > 0 ) ) {
            builder.append( builder.length() > 0 ? " " : "" );
            builder.append( streetName );
        }

        if ( ( floorNumber != null ) && ( floorNumber.length() > 0 ) ) {
            builder.append( builder.length() > 0 ? " #" : "#" );
            builder.append( floorNumber );
        }

        if ( ( unitNumber != null ) && ( unitNumber.length() > 0 ) ) {
            builder.append( ( ( floorNumber != null ) && ( floorNumber.length() > 0 ) ) ? "-" : "" );
            builder.append( unitNumber );
        }

        if ( ( buildingName != null ) && ( buildingName.length() > 0 ) ) {
            builder.append( builder.length() > 0 ? " " : "" );
            builder.append( buildingName );
        }

        if ( ( postalCode != null ) && ( postalCode.length() > 0 ) ) {
            builder.append( builder.length() > 0 ? " " : "" );
            builder.append( "S(" ).append( postalCode ).append( ")" );
        }

        if ( builder.length() == 0 ) {
            residentialAddress = "-";
        }
        else {
            residentialAddress = builder.toString();
        }
    }

    public void preSave() {

        // Convert to uppercase
        if ( name != null ) {
            name = name.toUpperCase();
        }

        if ( blockNumber != null ) {
            blockNumber = blockNumber.toUpperCase();
        }

        if ( buildingName != null ) {
            buildingName = buildingName.toUpperCase();
        }

        if ( floorNumber != null ) {
            floorNumber = floorNumber.toUpperCase();
        }

        if ( nric != null ) {
            nric = nric.toUpperCase();
        }

        if ( postalCode != null ) {
            postalCode = postalCode.toUpperCase();
        }

        if ( streetName != null ) {
            streetName = streetName.toUpperCase();
        }

        if ( unitNumber != null ) {
            unitNumber = unitNumber.toUpperCase();
        }

        buildResidentialAddress();

        if ( children != null ) {
            for ( Child child : children ) {
                child.preSave();
            }
        }

        if ( emailContacts != null ) {
            for ( Email email : emailContacts ) {
                email.preSave();
            }
        }

        if ( employment != null ) {
            employment.preSave();
        }

        if ( spouses != null ) {
            for ( Spouse spouse : spouses ) {
                spouse.preSave();
            }
        }

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( nric == null ) ? 0 : nric.hashCode() );
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
        PersonalDetail other = ( PersonalDetail ) obj;
        if ( nric == null ) {
            if ( other.nric != null )
                return false;
        }
        else if ( !nric.equals( other.nric ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PersonalDetail [nric=" + nric + ", name=" + name + ", dateOfBirth=" + dateOfBirth + ", citizenship=" + citizenship + ", nationality=" + nationality + ", race=" + race + ", gender=" + gender + ", addressType=" + addressType
                + ", blockNumber=" + blockNumber + ", streetName=" + streetName + ", floorNumber=" + floorNumber + ", unitNumber=" + unitNumber + ", buildingName=" + buildingName + ", postalCode=" + postalCode + ", residentialAddress="
                + residentialAddress + ", maritalStatus=" + maritalStatus + ", spouses=" + spouses + ", children=" + children + ", phoneContacts=" + phoneContacts + ", emailContacts=" + emailContacts + ", preferredContactMode="
                + preferredContactMode + ", eduationLevel=" + eduationLevel + ", employment=" + employment + ", leaves=" + leaves + "]";
    }

}
