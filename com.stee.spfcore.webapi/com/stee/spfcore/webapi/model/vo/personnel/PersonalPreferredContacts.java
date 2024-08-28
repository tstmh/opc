package com.stee.spfcore.webapi.model.vo.personnel;

import java.util.List;

import com.stee.spfcore.webapi.model.personnel.ContactLabel;
import com.stee.spfcore.webapi.model.personnel.ContactMode;
import com.stee.spfcore.webapi.model.personnel.Email;
import com.stee.spfcore.webapi.model.personnel.PersonalDetail;
import com.stee.spfcore.webapi.model.personnel.Phone;

public class PersonalPreferredContacts {
    protected String nric;
    protected String name;
    protected ContactMode preferredContactMode;
    protected String preferredMobile;
    protected String preferredEmail;
    protected String workEmail;

    public PersonalPreferredContacts( PersonalDetail detail ) {
        this.nric = ( detail != null ) ? detail.getNric() : null;
        this.name = ( detail != null ) ? detail.getName() : null;
        this.preferredContactMode = ( detail != null ) ? detail.getPreferredContactMode() : null;
        this.preferredEmail = this.derivePreferredEmail( detail );
        this.preferredMobile = this.derivePreferredMobile( detail );
        this.workEmail = this.deriveWorkEmail( detail );
    }

    private String derivePreferredEmail( PersonalDetail detail ) {
        String preferredEmail = null;
        if ( detail != null ) {
            List< Email > emails = detail.getEmailContacts();
            if ( emails != null ) {
                for ( Email email : emails ) {
                    if ( email.isPrefer() ) {
                        preferredEmail = email.getAddress();
                        break;
                    }
                }
            }
        }
        return preferredEmail;
    }

    private String derivePreferredMobile( PersonalDetail detail ) {
        String preferredMobile = null;
        if ( detail != null ) {
            List< Phone > phones = detail.getPhoneContacts();
            if ( phones != null ) {
                for ( Phone phone : phones ) {
                    if ( ( phone.getLabel() == ContactLabel.MOBILE ) || ( phone.getLabel() == ContactLabel.OTHERS ) ) {
                        if ( phone.isPrefer() ) {
                            preferredMobile = phone.getNumber();
                            break;
                        }
                    }
                }
            }
        }
        return preferredMobile;
    }
    
    private String deriveWorkEmail( PersonalDetail detail ) {
        String workEmail = null;
        if ( detail != null ) {
            List< Email > emails = detail.getEmailContacts();
            if ( emails != null ) {
                for ( Email email : emails ) {
                    ContactLabel label = email.getLabel();
                    if ( label == ContactLabel.WORK ) {
                        workEmail = email.getAddress();
                        break;
                    }
                }
            }
        }
        return workEmail;
    }

    public String getNric() {
        return nric;
    }

    public void setNric( String nric ) {
        this.nric = nric;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public ContactMode getPreferredContactMode() {
        return preferredContactMode;
    }

    public void setPreferredContactMode( ContactMode preferredContactMode ) {
        this.preferredContactMode = preferredContactMode;
    }

    public String getPreferredMobile() {
        return preferredMobile;
    }

    public void setPreferredMobile( String preferredMobile ) {
        this.preferredMobile = preferredMobile;
    }

    public String getPreferredEmail() {
        return preferredEmail;
    }

    public void setPreferredEmail( String preferredEmail ) {
        this.preferredEmail = preferredEmail;
    }

    public String toString() {
        return String.format( "[nric=%s, name=%s, contactMode=%s, mobile=%s, email=%s]", this.nric, this.name, this.preferredContactMode, this.preferredMobile, this.preferredEmail );
    }

    public String getWorkEmail() {
        return workEmail;
    }

    public void setWorkEmail( String workEmail ) {
        this.workEmail = workEmail;
    }
}
