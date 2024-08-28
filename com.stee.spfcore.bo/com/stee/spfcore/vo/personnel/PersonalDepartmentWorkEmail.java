package com.stee.spfcore.vo.personnel;

import java.util.List;

import com.stee.spfcore.model.personnel.ContactLabel;
import com.stee.spfcore.model.personnel.Email;
import com.stee.spfcore.model.personnel.Employment;
import com.stee.spfcore.model.personnel.PersonalDetail;

public class PersonalDepartmentWorkEmail {
    private String nric;
    private String name;
    private String departmentCode;
    private String workEmail;

    public PersonalDepartmentWorkEmail( PersonalDetail detail ) {
        this.nric = ( detail != null ) ? detail.getNric() : null;
        this.name = ( detail != null ) ? detail.getName() : null;
        this.departmentCode = this.deriveDepartmentCode( detail );
        this.workEmail = this.deriveWorkEmail( detail );
    }

    private String deriveDepartmentCode( PersonalDetail detail ) {
        String departmentCodeDetail = null;
        if ( detail != null ) {
            Employment employment = detail.getEmployment();
            if ( employment != null ) {
                departmentCodeDetail = employment.getOrganisationOrDepartment();
            }
        }
        return departmentCodeDetail;
    }

    private String deriveWorkEmail( PersonalDetail detail ) {
        String workEmailDetail = null;
        if ( detail != null ) {
            List< Email > emails = detail.getEmailContacts();
            if ( emails != null ) {
                for ( Email email : emails ) {
                    if ( email.getLabel() == ContactLabel.WORK ) {
                        workEmailDetail = email.getAddress();
                        break;
                    }
                }
            }
        }
        return workEmailDetail;
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

    public String getWorkEmail() {
        return workEmail;
    }

    public void setWorkEmail( String workEmail ) {
        this.workEmail = workEmail;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode( String departmentCode ) {
        this.departmentCode = departmentCode;
    }
}
